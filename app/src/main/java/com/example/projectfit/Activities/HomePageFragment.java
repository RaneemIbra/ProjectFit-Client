package com.example.projectfit.Activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.projectfit.Models.User;
import com.example.projectfit.R;
import com.example.projectfit.Room.Repositories.UserRoomRepository;
import com.example.projectfit.Server.Repositories.UserServerRepository;
import com.example.projectfit.Utils.AnimationUtils;
import com.example.projectfit.Utils.DialogUtils;
import com.example.projectfit.Utils.GsonProvider;
import com.example.projectfit.Utils.LoadModel;
import com.example.projectfit.Utils.NotificationReceiver;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomePageFragment extends Fragment implements SensorEventListener {
    private BarChart stepChart, waterChart;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private LinearLayout addCupSizeButton, circularContainer;
    private ProgressBar circularProgressBar;
    private TextView stepCountTextView, waterProgressTextView, nameTextView;
    private ImageView runningImageView;
    private RelativeLayout progressBarContainer;
    private int stepCount = 0;
    private CircleProgress waterCupProgress;
    private RelativeLayout progressBarLayout;
    public User user;
    private SharedPreferences sharedPreferences;
    private LocalDate lastDate;
    private static final String PREFS_NAME = "StepCounterPrefs";
    private static final String KEY_INITIAL_STEPS = "initialSteps";
    private static final String KEY_LAST_DATE = "lastDate";
    private int initialStepCount = 0;
    private int maxSteps = 0;
    private int maxWaterIntake = 0;
    private LoadModel loadModelSteps, loadModelWater;
    private ExecutorService executorService;
    private UserRoomRepository userRoomRepository;
    private UserServerRepository userServerRepository;
    private boolean isFirstTime=true;

    private static final String KEY_IS_NOTIFICATION_SCHEDULED = "isNotificationScheduled";
    private static final int NOTIFICATION_INTERVAL_HOURS = 3;  // Interval in hours
    private static final int TOTAL_NOTIFICATIONS = 8;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRoomRepository = new UserRoomRepository(getContext());
        userServerRepository = new UserServerRepository();
        executorService = Executors.newCachedThreadPool();

        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        try {
            loadModelSteps = new LoadModel(requireActivity().getAssets(), "step_prediction_model.tflite");
            loadModelWater = new LoadModel(requireActivity().getAssets(), "water_prediction_model.tflite");
        } catch (IOException e) {
            e.printStackTrace();
        }
        requestExactAlarmPermission();
        checkAndScheduleNotification();  // Check and schedule notifications
    }





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews(view);
        getUserFromSharedPreferencesAsync();
        setupSensors();
        loadStepDataAsync();
        initClickListeners();

        return view;
    }

    private void initViews(View view) {
        circularProgressBar = view.findViewById(R.id.circularProgressBar);
        stepCountTextView = view.findViewById(R.id.step_count_text_view);
        runningImageView = view.findViewById(R.id.running_image);
        progressBarContainer = view.findViewById(R.id.progressBarContainer);
        addCupSizeButton = view.findViewById(R.id.addCupSizeButton);
        waterCupProgress = view.findViewById(R.id.waterCupProgress);
        waterProgressTextView = view.findViewById(R.id.water_progress_text_view);
        circularContainer = view.findViewById(R.id.circularContainer);
        stepChart = view.findViewById(R.id.stepChart);
        waterChart = view.findViewById(R.id.WaterChart);
        progressBarLayout = view.findViewById(R.id.progressBarLayout);
        nameTextView = view.findViewById(R.id.NameTextView);
    }

    private void getUserFromSharedPreferencesAsync() {
        executorService.submit(() -> {
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            String userJson = sharedPreferences.getString("logged_in_user", null);

            if (userJson != null) {
                Gson gson = GsonProvider.getGson();
                Type userType = new TypeToken<User>() {}.getType();
                user = gson.fromJson(userJson, userType);

                requireActivity().runOnUiThread(() -> {
                    predictMaxStepsForUser(() -> {
                        predictMaxWaterForUser(this::setProgressForStepsAndWater);
                    });

                    setupCharts();
                });
                nameTextView.setText(user.getFullName());
            }
        });
    }



    private int calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            return 0;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Period.between(birthDate, LocalDate.now()).getYears();
        }
        return 0;
    }

    private void setupCharts() {
        setupStepChart();
        setupWaterChart();
    }

    private void setupStepChart() {
        if (user != null && user.getStepsHistory() != null) {
            List<BarEntry> stepEntries = new ArrayList<>();
            Map<LocalDate, Integer> stepsHistory = user.getStepsHistory();

            LocalDate today = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                today = LocalDate.now();
            }
            for (int i = 0; i < 7; i++) {
                LocalDate date = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    date = today.minusDays(i);
                }
                int steps = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    steps = stepsHistory.getOrDefault(date, 0);
                }
                stepEntries.add(new BarEntry(i, steps));
            }

            BarDataSet stepDataSet = new BarDataSet(stepEntries, "Steps in Last Week");
            BarData stepBarData = new BarData(stepDataSet);
            stepChart.setData(stepBarData);
            stepChart.invalidate();
        }
    }

    private void setupWaterChart() {
        if (user != null && user.getWaterHistory() != null) {
            List<BarEntry> waterEntries = new ArrayList<>();
            Map<LocalDate, Integer> waterHistory = user.getWaterHistory();

            LocalDate today = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                today = LocalDate.now();
            }
            for (int i = 0; i < 7; i++) {
                LocalDate date = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    date = today.minusDays(i);
                }
                int waterIntake = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    waterIntake = waterHistory.getOrDefault(date, 0);
                }
                waterEntries.add(new BarEntry(i, waterIntake));
            }

            BarDataSet waterDataSet = new BarDataSet(waterEntries, "Water Intake in Last Week (ml)");
            BarData waterBarData = new BarData(waterDataSet);
            waterChart.setData(waterBarData);
            waterChart.invalidate();
        }
    }

    private void setupSensors() {
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void predictMaxStepsForUser(Runnable onMaxCalculated) {
        if (user != null) {
            int gender = user.isGender() ? 1 : 0;
            float height = (float) user.getHeight();
            float weight = (float) user.getWeight();
            int age = calculateAge(user.getBirthday());

            executorService.submit(() -> {
                maxSteps = (int) loadModelSteps.predictMaxSteps(gender, height, weight, age);
                maxSteps *= 3;

                requireActivity().runOnUiThread(() -> {
                    circularProgressBar.setMax(maxSteps);
                    System.out.println("Max steps AI: " + circularProgressBar.getMax());

                    if (onMaxCalculated != null) {
                        onMaxCalculated.run();
                    }
                });
            });
        }
    }


    private void predictMaxWaterForUser(Runnable onMaxCalculated) {
        if (user != null) {
            int gender = user.isGender() ? 1 : 0;
            float height = (float) user.getHeight();
            float weight = (float) user.getWeight();
            int age = calculateAge(user.getBirthday());

            executorService.submit(() -> {
                maxWaterIntake = (int) loadModelWater.predictMaxWater(gender, height, weight, age);
                maxWaterIntake *= 1.8;

                requireActivity().runOnUiThread(() -> {
                    waterCupProgress.setMax(maxWaterIntake);
                    System.out.println("Max water intake AI: " + waterCupProgress.getMax());

                    if (onMaxCalculated != null) {
                        onMaxCalculated.run();
                    }
                });
            });
        }
    }


    private void setProgressForStepsAndWater() {
        if (user != null) {
            LocalDate today = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                today = LocalDate.now();
            }

            int todayWaterIntake = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                todayWaterIntake = user.getWaterHistory().getOrDefault(today, 0);
            }

            int displayedWaterIntake = Math.min(todayWaterIntake, maxWaterIntake);
            waterCupProgress.setProgress(displayedWaterIntake);
            waterProgressTextView.setText("Water: " + displayedWaterIntake + " ml out of " + maxWaterIntake + " ml");

            int todaySteps = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                todaySteps = user.getStepsHistory().getOrDefault(today, 0);
            }

            int displayedSteps = Math.min(todaySteps, maxSteps);
            circularProgressBar.setProgress(displayedSteps);
            stepCountTextView.setText("Steps: " + displayedSteps + "\nout of " + maxSteps);
        }
    }

    private void initClickListeners() {
        progressBarContainer.setOnClickListener(v -> animatedProgressBarStepCount());
        waterCupProgress.setOnClickListener(v -> animatedProgressBarWaterTracker());
        addCupSizeButton.setOnClickListener(v -> showAddCupSizeDialog());
        progressBarLayout.setOnClickListener(v -> increaseWaterCupProgress(0));
    }

    private void animatedProgressBarStepCount() {
        AnimationUtils.fadeOutView(runningImageView, 500, View.GONE);
        AnimationUtils.fadeInView(stepCountTextView, 500);

        new Handler().postDelayed(() -> {
            AnimationUtils.fadeOutView(stepCountTextView, 500, View.GONE);
            AnimationUtils.fadeInView(runningImageView, 500);
        }, 3000);
    }

    private void animatedProgressBarWaterTracker() {
        int currentProgress = waterCupProgress.getProgress();
        waterProgressTextView.setText("Water Progress: " + currentProgress + " ml");
        AnimationUtils.fadeInView(waterProgressTextView, 300);

        new Handler().postDelayed(() -> {
            AnimationUtils.fadeOutView(waterProgressTextView, 300, View.GONE);
        }, 3000);
    }

    private void showAddCupSizeDialog() {
        DialogUtils.showAddCupSizeDialog(requireContext(), this::addCupSizeLayout);
    }

    private void increaseWaterCupProgress(int cupSize) {
        int currentProgress = waterCupProgress.getProgress();
        int newProgress = currentProgress + cupSize;

        int finalProgress = Math.min(newProgress, maxWaterIntake);
        waterCupProgress.setProgress(finalProgress);

        LocalDate today = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            today = LocalDate.now();
        }

        int totalWater = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            totalWater = user.getWaterHistory().getOrDefault(today, 0) + cupSize;
        }

        user.getWaterHistory().put(today, totalWater);
        userRoomRepository.updateWaterHistory(user);

        saveUserToSharedPreferences();
        setupWaterChart();
    }


    private void saveUserToSharedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = GsonProvider.getGson();
        String userJson = gson.toJson(user);
        editor.putString("logged_in_user", userJson);
        editor.apply();
    }

    private void addCupSizeLayout(int cupSize) {
        executorService.submit(() -> {
            int size = 100;

            LinearLayout circleLayout = new LinearLayout(requireContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.setMargins(10, 0, 10, 0);
            circleLayout.setLayoutParams(params);
            circleLayout.setGravity(android.view.Gravity.CENTER);
            circleLayout.setOrientation(LinearLayout.VERTICAL);
            circleLayout.setPadding(20, 20, 20, 20);

            GradientDrawable circleDrawable = new GradientDrawable();
            circleDrawable.setShape(GradientDrawable.OVAL);
            circleDrawable.setColor(getResources().getColor(android.R.color.holo_blue_light));
            circleLayout.setBackground(circleDrawable);

            TextView cupSizeText = new TextView(requireContext());
            cupSizeText.setText(Integer.toString(cupSize));
            cupSizeText.setTextColor(getResources().getColor(android.R.color.white));
            cupSizeText.setTextSize(12);
            cupSizeText.setGravity(android.view.Gravity.CENTER);

            circleLayout.addView(cupSizeText);

            requireActivity().runOnUiThread(() -> {
                circleLayout.setOnClickListener(view -> increaseWaterCupProgress(cupSize));
                circularContainer.addView(circleLayout);
            });
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int totalSteps = (int) event.values[0];
            checkForDayReset();
            updateStepCountAsync(totalSteps);
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
            saveStepDataAsync();
            userRoomRepository.updateStepsHistory(user);
        }
    }

    private void updateStepCountAsync(int totalSteps) {
        executorService.submit(() -> {
            stepCount = totalSteps - initialStepCount;
            LocalDate today = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                today = LocalDate.now();
            }
            user.getStepsHistory().put(today, stepCount);

            userRoomRepository.updateStepsHistory(user);
            saveUserToSharedPreferences();

            requireActivity().runOnUiThread(() -> {
                circularProgressBar.setProgress(stepCount);
                stepCountTextView.setText("Steps: " + stepCount + "\nout of: " + maxSteps);

                setupStepChart();
            });
        });
    }

    private void loadStepDataAsync() {
        executorService.submit(() -> {
            initialStepCount = sharedPreferences.getInt(KEY_INITIAL_STEPS, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                lastDate = LocalDate.parse(sharedPreferences.getString(KEY_LAST_DATE, LocalDate.now().toString()));
            }

            requireActivity().runOnUiThread(() -> stepCountTextView.setText("Steps: " + (stepCount - initialStepCount) + "\nout of: " + maxSteps));
        });
    }

    private void saveStepDataAsync() {
        executorService.submit(() -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(KEY_INITIAL_STEPS, initialStepCount);
            editor.putString(KEY_LAST_DATE, lastDate.toString());
            editor.apply();
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        saveStepDataAsync();
        executorService.shutdown();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        executorService = Executors.newCachedThreadPool();

        // Handle increase water intent
        Intent intent = requireActivity().getIntent();
        if (intent != null && intent.hasExtra("ACTION_INCREASE_WATER")) {
            int amount = intent.getIntExtra("ACTION_INCREASE_WATER", 0);
            increaseWaterCupProgress(amount);
            requireActivity().getIntent().removeExtra("ACTION_INCREASE_WATER"); // Clear the intent to avoid duplication
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }



    private void checkAndScheduleNotification() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isNotificationScheduled = sharedPreferences.getBoolean(KEY_IS_NOTIFICATION_SCHEDULED, false);

        if (!isNotificationScheduled) {
            // Schedule notifications for the first time
            scheduleNotifications();

            // Update SharedPreferences to mark that notifications have been scheduled
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(KEY_IS_NOTIFICATION_SCHEDULED, true);
            editor.apply();
        }
    }

    private void scheduleNotifications() {
        long currentTimeMillis = System.currentTimeMillis();
        Context context = requireContext();

        // Schedule notifications at intervals of 3 hours for a total of 8 times
        for (int i = 0; i < TOTAL_NOTIFICATIONS; i++) {
            long triggerTimeMillis = currentTimeMillis + (i * NOTIFICATION_INTERVAL_HOURS * 60 * 60 * 1000L);
            scheduleNotificationAt(context, triggerTimeMillis, i);
        }
    }
    private void scheduleNotificationAt(Context context, long triggerTimeMillis, int requestCode) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent);
            }
        }
    }
    private void requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {  // Android 12 (API level 31) and higher
            AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                // Show a dialog to explain to the user why this permission is needed
                new AlertDialog.Builder(requireContext())
                        .setTitle("Exact Alarm Permission Needed")
                        .setMessage("This app needs permission to schedule exact alarms for important reminders.")
                        .setPositiveButton("Go to Settings", (dialog, which) -> {
                            // Open the app's settings page to request exact alarm permission
                            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                            intent.setData(Uri.parse("package:" + requireContext().getPackageName()));
                            startActivity(intent);
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        }
    }
}

