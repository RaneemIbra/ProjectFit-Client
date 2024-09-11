package com.example.projectfit.Activities;


import static android.content.Context.MODE_PRIVATE;
import android.content.Context;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.projectfit.Models.User;
import com.example.projectfit.R;
import com.example.projectfit.Utils.AnimationUtils;
import com.example.projectfit.Utils.DialogUtils;
import com.example.projectfit.Utils.GsonProvider;
import com.example.projectfit.Utils.LoadModel;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;


public class HomeFragment extends Fragment implements SensorEventListener {
    CircleProgress waterCupProgress;
    ProgressBar circularProgressBar;
    TextView stepCountTextView,waterProgressTextView;
    ImageView  runningImageView;
    RelativeLayout progressBarContainer ,progressBarLayout;
    LinearLayout addCupSizeButton ,circularContainer;
    BarChart stepChart,waterChart;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private int stepCount = 0;
    public User user;
    private LocalDate lastDate;
    private static final String PREFS_NAME = "StepCounterPrefs";
    private static final String KEY_INITIAL_STEPS = "initialSteps";
    private static final String KEY_LAST_DATE = "lastDate";
    private int initialStepCount = 0;
    private int maxSteps = 0;
    private int maxWaterIntake = 0;
    private LoadModel loadModelSteps, loadModelWater;
    ExecutorService executorService;
    SharedPreferences sharedPreferences;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for the fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        circularProgressBar = view.findViewById(R.id.circularProgressBar);
        stepCountTextView = view.findViewById(R.id.step_count_text_view);
        runningImageView = view.findViewById(R.id.running_image);
        progressBarContainer =view.findViewById(R.id.progressBarContainer);
        addCupSizeButton = view.findViewById(R.id.addCupSizeButton);
        waterCupProgress = view.findViewById(R.id.waterCupProgress);
        waterProgressTextView = view.findViewById(R.id.water_progress_text_view);
        circularContainer = view.findViewById(R.id.circularContainer);
        stepChart = view.findViewById(R.id.stepChart);
        waterChart = view.findViewById(R.id.WaterChart);
        progressBarLayout =view.findViewById(R.id.progressBarLayout);
        executorService = Executors.newCachedThreadPool();
        sharedPreferences =requireActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        try {
            loadModelSteps = new LoadModel(requireActivity().getAssets(), "step_prediction_model.tflite");
            loadModelWater = new LoadModel(requireActivity().getAssets(), "water_prediction_model.tflite");
        } catch (IOException e) {
            e.printStackTrace();
        }

        user = getUserFromSharedPreferences();
        System.out.println(user);
        if (user != null) {
            System.out.println("User name: " + user.getFullName() + " User birthday: " + user.getBirthday());
        }

        setupCharts();
        setupSensors();
        predictMaxStepsForUser();
        predictMaxWaterForUser();
        loadStepDataAsync();
        initClickListeners();
        return view;
    }

    private User getUserFromSharedPreferences() {
        SharedPreferences sharedPreferences =  requireActivity().getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("logged_in_user", null);

        if (userJson != null) {
            Gson gson = GsonProvider.getGson();
            Type userType = new TypeToken<User>() {}.getType();
            return gson.fromJson(userJson, userType);
        }

        return null;
    }

    private void setupCharts() {
        executorService.submit(() -> {
            List<BarEntry> entries = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                entries.add(new BarEntry(i, i + 1));
            }
            BarDataSet dataSet = new BarDataSet(entries, "Sample Data");
            BarData barData = new BarData(dataSet);

            getActivity().runOnUiThread(() -> {
                stepChart.setData(barData);
                waterChart.setData(barData);
                stepChart.invalidate();
                waterChart.invalidate();
            });
        });
    }

    private void setupSensors() {
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private int calculateAge(LocalDate birthday) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate currentDate = LocalDate.now();
            if ((birthday != null) && (currentDate != null)) {
                return Period.between(birthday, currentDate).getYears();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    private void predictMaxStepsForUser() {
        if (user != null) {
            int gender = user.isGender() ? 1 : 0;
            float height = (float) user.getHeight();
            float weight = (float) user.getWeight();
            int age = calculateAge(user.getBirthday());

            executorService.submit(() -> {
                maxSteps = (int) loadModelSteps.predictMaxSteps(gender, height, weight, age);
                getActivity().runOnUiThread(() -> {
                    circularProgressBar.setMax(maxSteps);
                    stepCountTextView.setText("Steps: " + stepCount + " out of " + maxSteps);
                });
            });
        }
    }

    private void predictMaxWaterForUser() {
        if (user != null) {
            int gender = user.isGender() ? 1 : 0;
            float height = (float) user.getHeight();
            float weight = (float) user.getWeight();
            int age = calculateAge(user.getBirthday());

            executorService.submit(() -> {
                maxWaterIntake = (int) loadModelWater.predictMaxWater(gender, height, weight, age);
                getActivity().runOnUiThread(() -> {
                    waterCupProgress.setMax(maxWaterIntake);
                    waterProgressTextView.setText("Max Water: " + maxWaterIntake + " ml");
                    System.out.println("water intake" + maxWaterIntake);
                });
            });
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
        waterCupProgress.setProgress(Math.min(newProgress, waterCupProgress.getMax()));
    }

    private void addCupSizeLayout(int cupSize) {
        executorService.submit(() -> {
            int size = 100;

            LinearLayout circleLayout = new LinearLayout(requireContext());
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

            TextView cupSizeText = new TextView(requireContext());
            cupSizeText.setText(Integer.toString(cupSize));
            cupSizeText.setTextColor(getResources().getColor(android.R.color.white));
            cupSizeText.setTextSize(12);
            cupSizeText.setGravity(Gravity.CENTER);

            circleLayout.addView(cupSizeText);

            getActivity().runOnUiThread(() -> {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate currentDate = LocalDate.now();
            if (!currentDate.equals(lastDate)) {
                initialStepCount = stepCount;
                lastDate = currentDate;
                saveStepDataAsync();
            }
        }
    }

    private void updateStepCountAsync(int totalSteps) {
        executorService.submit(() -> {
            stepCount = totalSteps - initialStepCount;
            getActivity().runOnUiThread(() -> {
                circularProgressBar.setProgress(stepCount);
                stepCountTextView.setText("Steps: " + stepCount + " out of: " + maxSteps);
            });
        });
    }

    private void loadStepDataAsync() {
        executorService.submit(() -> {
            initialStepCount = sharedPreferences.getInt(KEY_INITIAL_STEPS, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                lastDate = LocalDate.parse(sharedPreferences.getString(KEY_LAST_DATE, LocalDate.now().toString()));
            }
            getActivity().runOnUiThread(() -> stepCountTextView.setText("Steps: " + (stepCount - initialStepCount) + " out of: " + maxSteps));
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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        executorService.shutdown();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        executorService = Executors.newCachedThreadPool();
    }
}
