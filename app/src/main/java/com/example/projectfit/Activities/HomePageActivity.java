package com.example.projectfit.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.projectfit.R;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.imageview.ShapeableImageView;

import android.os.Handler;
import android.view.View;
import android.widget.Toast;

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
    Button planPage, ProfilePage;

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
        circularProgressBar = findViewById(R.id.r13x7x0kfrgcc);
        stepCountTextView = findViewById(R.id.step_count_text_view);
        runningImageView = findViewById(R.id.running_image);
        progressBarContainer = findViewById(R.id.progressBarContainer);
        addCupSizeButton = findViewById(R.id.r04ysmm44lgo5);
        waterCupProgress = findViewById(R.id.waterCupProgress);
        waterProgressTextView = findViewById(R.id.water_progress_text_view);
        circularContainer = findViewById(R.id.circularContainer);
        stepChart = findViewById(R.id.stepChart);
        waterChart = findViewById(R.id.WaterChart);
        progressBarLayout = findViewById(R.id.progressBarLayout);
        planPage = findViewById(R.id.button_plan_home);
        ProfilePage = findViewById(R.id.button_profile_home);

        addCupSizeLayout(100);
        addCupSizeLayout(250);
        addCupSizeLayout(500);
        waterCupProgress.setMax(2000);

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 1f));
        entries.add(new BarEntry(1f, 2f));
        entries.add(new BarEntry(2f, 3f));
        entries.add(new BarEntry(3f, 4f));
        entries.add(new BarEntry(4f, 5f));

        BarDataSet dataSet = new BarDataSet(entries, "Sample Data");
        BarData barData = new BarData(dataSet);

        stepChart.setData(barData);
        waterChart.setData(barData);
        stepChart.invalidate();
        waterChart.invalidate();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            System.out.println("Sensor couldn't be found");
        }
        initClickListeners();
    }
    private void initClickListeners(){
        ProfilePage.setOnClickListener(view -> {
            Intent intent = new Intent(HomePageActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
        planPage.setOnClickListener(view -> {
            Intent intent = new Intent(HomePageActivity.this, MyPlanActivity.class);
            startActivity(intent);
        });
        progressBarContainer.setOnClickListener(view -> {
            animatedProgressBarStepCount();
        });
        waterCupProgress.setOnClickListener(view -> {
            animatedProgressBarWaterTracker();
        });
        addCupSizeButton.setOnClickListener(view -> showAddCupSizeDialog());
        progressBarLayout.setOnClickListener(view -> {
            increaseWaterCupProgress(0);
        });
    }
    private void animatedProgressBarWaterTracker() {
        int currentProgress = waterCupProgress.getProgress();
        waterProgressTextView.setText("Water Progress: " + currentProgress + "ml");

        waterProgressTextView.setVisibility(View.VISIBLE);
        waterProgressTextView.animate()
                .alpha(1f)
                .setDuration(300)
                .withEndAction(() -> new Handler().postDelayed(() -> {
                    waterProgressTextView.animate()
                            .alpha(0f)
                            .setDuration(300)
                            .withEndAction(() -> waterProgressTextView.setVisibility(View.GONE))
                            .start();
                }, 2000))
                .start();
    }
    private void animatedProgressBarStepCount() {
        runningImageView.animate()
                .alpha(0f)
                .setDuration(500)
                .withEndAction(() -> {
                    runningImageView.setVisibility(View.GONE);
                    stepCountTextView.setAlpha(0f);
                    stepCountTextView.setVisibility(View.VISIBLE);

                    stepCountTextView.animate()
                            .alpha(1f)
                            .setDuration(500)
                            .withEndAction(() -> {
                                new Handler().postDelayed(() -> {
                                    stepCountTextView.animate()
                                            .alpha(0f)
                                            .setDuration(500)
                                            .withEndAction(() -> {
                                                stepCountTextView.setVisibility(View.GONE);
                                                runningImageView.setAlpha(0f);
                                                runningImageView.setVisibility(View.VISIBLE);
                                                runningImageView.animate()
                                                        .alpha(1f)
                                                        .setDuration(500)
                                                        .start();
                                            })
                                            .start();
                                }, 3000);
                            })
                            .start();
                })
                .start();
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            stepCount = (int) event.values[0];
            updateProgressBar(stepCount);
        }
    }

    private void updateProgressBar(int steps) {
        int maxSteps = 10000;
        circularProgressBar.setMax(maxSteps);
        circularProgressBar.setProgress(steps);

        if (stepCountTextView != null) {
            stepCountTextView.setText("Steps: " + steps);
        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_cup_size, null);
        builder.setView(dialogView);

        EditText cupSizeInput = dialogView.findViewById(R.id.cup_size_input);

        builder.setPositiveButton("Add", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String cupSizeStr = cupSizeInput.getText().toString().trim();

            if (!cupSizeStr.isEmpty() && cupSizeStr.matches("\\d+")) {
                int cupSize = Integer.parseInt(cupSizeStr);
                addCupSizeLayout(cupSize);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Please enter a valid integer for cup size", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void increaseWaterCupProgress(int cupSize) {
        int currentProgress = waterCupProgress.getProgress();
        int newProgress = currentProgress + cupSize;
        int maxProgress = waterCupProgress.getMax();
        if (newProgress > maxProgress) {
            newProgress = maxProgress;
        }
        waterCupProgress.setProgress(newProgress);
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

        circleLayout.setOnClickListener(view -> {
            increaseWaterCupProgress(cupSize);
        });
        circularContainer.addView(circleLayout);
    }
}
