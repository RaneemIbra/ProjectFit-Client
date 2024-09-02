package com.example.projectfit.Activities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity implements SensorEventListener {
    BarChart stepChart, waterChart;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    LinearLayout addCupSizeButton;
    ProgressBar circularProgressBar;
    private TextView stepCountTextView;
    private ImageView runningImageView;
    private RelativeLayout progressBarContainer;
    private int stepCount = 0;
    CircleProgress waterCupProgress;
    RelativeLayout progressBarLayout;

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
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rks7n0a9hm5l));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rrif4xi0ks2s));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rt6mp0emoe48));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rei0kw491b7u));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.r1ya0vac26d6));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.image3));
        stepChart = findViewById(R.id.stepChart);
        waterChart = findViewById(R.id.WaterChart);
        progressBarLayout = findViewById(R.id.progressBarLayout);

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

        addCupSizeButton.setOnClickListener(view -> showAddCupSizeDialog());
        progressBarLayout.setOnClickListener(view -> {
            increaseWaterCupProgress();
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            System.out.println("Sensor couldn't be found");
        }

        progressBarContainer.setOnClickListener(view -> {
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
        });
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

        builder.setPositiveButton("Add", (dialog, which) -> {
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void increaseWaterCupProgress() {
        int currentProgress = waterCupProgress.getProgress();
        int maxProgress = waterCupProgress.getMax();
        int newProgress = currentProgress + (maxProgress / 20);
        if (newProgress > maxProgress) {
            newProgress = maxProgress;
        }
        waterCupProgress.setProgress(newProgress);
    }
}
