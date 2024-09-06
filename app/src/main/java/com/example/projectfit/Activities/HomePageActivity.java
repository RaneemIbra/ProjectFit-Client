package com.example.projectfit.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectfit.Models.User;
import com.example.projectfit.R;
import com.example.projectfit.Utils.AnimationUtils;
import com.example.projectfit.Utils.DialogUtils;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity implements SensorEventListener {
    BarChart stepChart, waterChart;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    LinearLayout addCupSizeButton, circularContainer;
    ProgressBar circularProgressBar;
    TextView stepCountTextView, waterProgressTextView;
    ImageView runningImageView;
    RelativeLayout progressBarContainer;
    int stepCount = 0;
    CircleProgress waterCupProgress;
    RelativeLayout progressBarLayout;
    public static User user;
    BottomNavigationView bottomBar;

    private static final String PREFS_NAME = "StepCounterPrefs";
    private static final String KEY_INITIAL_STEPS = "initialSteps";
    private static final String KEY_LAST_DATE = "lastDate";
    private int initialStepCount = 0;
    private LocalDate lastDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = getIntent().getParcelableExtra("user");
        initViews();
        setupCharts();
        setupSensors();
        loadStepData();
        initClickListeners();
        waterCupProgress.setMax(2000);
    }

    private void initViews() {
        circularProgressBar = findViewById(R.id.circularProgressBar);
        stepCountTextView = findViewById(R.id.step_count_text_view);
        runningImageView = findViewById(R.id.running_image);
        progressBarContainer = findViewById(R.id.progressBarContainer);
        addCupSizeButton = findViewById(R.id.addCupSizeButton);
        waterCupProgress = findViewById(R.id.waterCupProgress);
        waterProgressTextView = findViewById(R.id.water_progress_text_view);
        circularContainer = findViewById(R.id.circularContainer);
        stepChart = findViewById(R.id.stepChart);
        waterChart = findViewById(R.id.WaterChart);
        progressBarLayout = findViewById(R.id.progressBarLayout);
        bottomBar=findViewById(R.id.bottom_navigation);
    }

    private void setupCharts() {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            entries.add(new BarEntry(i, i + 1));
        }
        BarDataSet dataSet = new BarDataSet(entries, "Sample Data");
        BarData barData = new BarData(dataSet);
        stepChart.setData(barData);
        waterChart.setData(barData);
        stepChart.invalidate();
        waterChart.invalidate();
    }

    private void setupSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void initClickListeners() {

        progressBarContainer.setOnClickListener(v -> animatedProgressBarStepCount());
        waterCupProgress.setOnClickListener(v -> animatedProgressBarWaterTracker());
        addCupSizeButton.setOnClickListener(v -> showAddCupSizeDialog());
        progressBarLayout.setOnClickListener(v -> increaseWaterCupProgress(0));
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener (){
            @Override
            public boolean onNavigationItemSelected( @NonNull MenuItem item)
            {
                   int id_item=item.getItemId();
                   if(id_item==R.id.home_BottomIcon)
                        return true;
                   else if (id_item == R.id.plan_BottomIcon) {
                       navigateTo(MyPlanActivity.class);
                       return true;
                   } else if (id_item==R.id.workouts_BottomIcon) {
                       navigateTo(WorkoutsFilterActivity.class);
                       return true;
                   } else if ( id_item==R.id.profile_BottomIcon) {
                       navigateTo(ProfileActivity.class);
                       return true;
                   }
                    else
                        return false;

                }

        });
    }

    private void navigateTo(Class<?> targetActivity) {
        startActivity(new Intent(HomePageActivity.this, targetActivity));
    }

    private void animatedProgressBarWaterTracker() {
        int currentProgress = waterCupProgress.getProgress();
        waterProgressTextView.setText("Water Progress: " + currentProgress + "ml");
        AnimationUtils.fadeInView(waterProgressTextView, 300);
        new Handler().postDelayed(() -> {
            AnimationUtils.fadeOutView(waterProgressTextView, 300, View.GONE);
        }, 2000);
    }

    private void animatedProgressBarStepCount() {
        AnimationUtils.fadeOutView(runningImageView, 500, View.GONE);
        AnimationUtils.fadeInView(stepCountTextView, 500);
        new Handler().postDelayed(() -> {
            AnimationUtils.fadeOutView(stepCountTextView, 500, View.GONE);
            AnimationUtils.fadeInView(runningImageView, 500);
        }, 3000);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int totalSteps = (int) event.values[0];
            checkForDayReset();
            updateStepCount(totalSteps);
        }
    }

    private void checkForDayReset() {
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }
        if (!currentDate.equals(lastDate)) {
            initialStepCount = stepCount;
            lastDate = currentDate;
            saveStepData();
        }
    }

    private void updateStepCount(int totalSteps) {
        stepCount = totalSteps - initialStepCount;
        circularProgressBar.setProgress(stepCount);
        stepCountTextView.setText("Steps: " + stepCount);
    }

    private void loadStepData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        initialStepCount = prefs.getInt(KEY_INITIAL_STEPS, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lastDate = LocalDate.parse(prefs.getString(KEY_LAST_DATE, LocalDate.now().toString()));
        }
        stepCountTextView.setText("Steps: " + (stepCount - initialStepCount));
    }

    private void saveStepData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_INITIAL_STEPS, initialStepCount);
        editor.putString(KEY_LAST_DATE, lastDate.toString());
        editor.apply();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void showAddCupSizeDialog() {
        DialogUtils.showAddCupSizeDialog(this, this::addCupSizeLayout);
    }

    private void increaseWaterCupProgress(int cupSize) {
        int currentProgress = waterCupProgress.getProgress();
        int newProgress = currentProgress + cupSize;
        waterCupProgress.setProgress(Math.min(newProgress, waterCupProgress.getMax()));
    }

    private void addCupSizeLayout(int cupSize) {
        int size = 100;

        LinearLayout circleLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        params.setMargins(10, 0, 10, 0);
        circleLayout.setLayoutParams(params);
        circleLayout.setGravity(Gravity.CENTER);
        circleLayout.setOrientation(LinearLayout.VERTICAL);
        circleLayout.setPadding(20, 20, 20, 20);

        GradientDrawable circleDrawable = new GradientDrawable();
        circleDrawable.setShape(GradientDrawable.OVAL);
        circleDrawable.setColor(getResources().getColor(android.R.color.holo_blue_light));
        circleLayout.setBackground(circleDrawable);

        TextView cupSizeText = new TextView(this);
        cupSizeText.setText(Integer.toString(cupSize));
        cupSizeText.setTextColor(getResources().getColor(android.R.color.white));
        cupSizeText.setTextSize(12);
        cupSizeText.setGravity(Gravity.CENTER);

        circleLayout.addView(cupSizeText);

        circleLayout.setOnClickListener(view -> increaseWaterCupProgress(cupSize));
        circularContainer.addView(circleLayout);
    }
}
