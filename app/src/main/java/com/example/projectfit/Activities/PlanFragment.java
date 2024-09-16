package com.example.projectfit.Activities;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.projectfit.Models.User;
import com.example.projectfit.Models.Workout;
import com.example.projectfit.R;
import com.example.projectfit.Room.Repositories.UserRoomRepository;
import com.example.projectfit.Room.Repositories.WorkoutRoomRepository;
import com.example.projectfit.Server.Repositories.UserServerRepository;
import com.example.projectfit.Utils.GsonProvider;
import com.example.projectfit.Utils.WorkoutAdapterForMyplan;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.lang.reflect.Type;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;

public class PlanFragment extends Fragment {

    private Button[] dayButtons;
    private Button selectedDayButton;
    private ProgressBar[] progressBars;
    private LinearLayout[] trainingLayouts;
    private int[] progressStatuses = {0, 0, 0};
    private LinearLayout editPlanButton;
    private TextView editPlanText;
    private boolean isEditModeEnabled = false;
    private ImageView trashIcon;
    private boolean isFirstTime = true;
    private ImageButton addWorkoutButton;
    private ListView workoutListView;
    private Button cancelButton;
    private boolean isListVisible = false;
    private ScrollView mainScrollView;
    private LinearLayout dayButtonsContainer;
    private TextView[] setsCompletedTextViews;
    private WorkoutRoomRepository workoutRepository;
    private WorkoutAdapterForMyplan workoutAdapter;
    private Boolean isWorkoutDeleted = false;
    private BottomNavigationView bottomBar;
    private User user;
    private boolean ranodomSuggestion = false;
    private SharedPreferences sharedPreferences;
    private UserServerRepository userServerRepository;
    private UserRoomRepository userRoomRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan, container, false);

        sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        user = getUserFromSharedPreferences();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews(view);
        setupDayButtons();
        setupEditPlanButton();
        setupAddWorkoutButton();
        workoutRepository = new WorkoutRoomRepository(requireContext());
        userRoomRepository = new UserRoomRepository(requireContext());
        userServerRepository = new UserServerRepository();
        setupWorkoutListView();
        setupContainers(view);
        loadTodayWorkouts();
    }

    private String getRecommendedPlan() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("recommended_plan", "default");
    }

    private void loadTodayWorkouts() {
        Date today = Calendar.getInstance().getTime();
        String dayOfWeek = getDayOfWeek(today);
        for (int i = 0; i < dayButtons.length; i++) {
            if (dayButtons[i].getText().toString().equals(dayOfWeek)) {
                dayButtons[i].performClick();
                break;
            }
        }
    }

    private User getUserFromSharedPreferences() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("logged_in_user", null);

        if (userJson != null) {
            Gson gson = GsonProvider.getGson();
            Type userType = new TypeToken<User>() {}.getType();
            return gson.fromJson(userJson, userType);
        }
        return null;
    }

    private void saveUserToSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = GsonProvider.getGson();
        String userJson = gson.toJson(user);
        editor.putString("logged_in_user", userJson);
        editor.apply();
    }

    private String getDayOfWeek(Date today) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String[] days = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        return days[dayOfWeek - 1];
    }

    @SuppressLint("WrongViewCast")
    private void initViews(View view) {
        dayButtons = new Button[]{
                view.findViewById(R.id.sunday),
                view.findViewById(R.id.monday),
                view.findViewById(R.id.tuesday),
                view.findViewById(R.id.wednesday),
                view.findViewById(R.id.thursday),
                view.findViewById(R.id.friday),
                view.findViewById(R.id.saturday)
        };
        selectedDayButton = dayButtons[0];
        LinearLayout workoutsContainer = view.findViewById(R.id.workoutsContainer);
        editPlanButton = view.findViewById(R.id.ryd3katlt2w);
        editPlanText = view.findViewById(R.id.r1q5rejf6pyw);
        trashIcon = view.findViewById(R.id.trash_icon);
        bottomBar = view.findViewById(R.id.bottom_navigation);
        addWorkoutButton = view.findViewById(R.id.add_workout_button);
        workoutListView = view.findViewById(R.id.workout_list_view);
        cancelButton = view.findViewById(R.id.cancel_button);
        mainScrollView = view.findViewById(R.id.main_content);
        dayButtonsContainer = view.findViewById(R.id.day_buttons_container);
        setsCompletedTextViews = new TextView[]{}; // No need for static TextViews anymore
        trainingLayouts = new LinearLayout[0];
        cancelButton.setOnClickListener(v -> toggleWorkoutListVisibility(false));
    }

    private void setupAddWorkoutButton() {
        addWorkoutButton.setOnClickListener(v -> toggleWorkoutListVisibility(true));
    }

    private void toggleWorkoutListVisibility(boolean show) {
        if (show) {
            workoutListView.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
            adjustLayoutForSplitView();
        } else {
            workoutListView.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            resetLayout();
        }
        isListVisible = show;
    }

    private void adjustLayoutForSplitView() {
        LinearLayout workoutDetailsLayout = requireView().findViewById(R.id.workout_details_layout);
        workoutDetailsLayout.setVisibility(View.VISIBLE);

        LinearLayout.LayoutParams mainContentParams = (LinearLayout.LayoutParams) mainScrollView.getLayoutParams();
        mainContentParams.weight = 0.7F;
        mainScrollView.setLayoutParams(mainContentParams);

        LinearLayout.LayoutParams workoutDetailsParams = (LinearLayout.LayoutParams) workoutDetailsLayout.getLayoutParams();
        workoutDetailsParams.weight = 0.3F;
        workoutDetailsLayout.setLayoutParams(workoutDetailsParams);

        dayButtonsContainer.setGravity(Gravity.START);
    }

    private void resetLayout() {
        LinearLayout workoutDetailsLayout = requireView().findViewById(R.id.workout_details_layout);
        workoutDetailsLayout.setVisibility(View.GONE);

        LinearLayout.LayoutParams mainContentParams = (LinearLayout.LayoutParams) mainScrollView.getLayoutParams();
        mainContentParams.weight = 1;
        mainScrollView.setLayoutParams(mainContentParams);

        dayButtonsContainer.setGravity(Gravity.CENTER);
    }

    private void handleClick(int index) {
        if (!isEditModeEnabled) {
            increaseProgressBar(index);
        } else {
            showEditDialog(index);
        }
    }

    private boolean handleLongClick(View view, int index) {
        if (!isEditModeEnabled) {
            undoProgress(index);
        } else {
            startDrag(view);
        }
        return true;
    }

    private void startDrag(View view) {
        ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.startDragAndDrop(data, shadowBuilder, view, 0);
        } else {
            view.startDrag(data, shadowBuilder, view, 0);
        }
        view.setBackgroundColor(Color.GRAY);
    }

    private void increaseProgressBar(int index) {
        if (progressStatuses[index] < 100) {
            progressStatuses[index] += 10;
            progressBars[index].setProgress(progressStatuses[index]);
            int completedSets = progressStatuses[index] / 10;
            setsCompletedTextViews[index].setText(completedSets + "/10");
        }
    }

    private void undoProgress(int index) {
        if (progressStatuses[index] > 0) {
            progressStatuses[index] -= 10;
            progressBars[index].setProgress(progressStatuses[index]);
            int completedSets = progressStatuses[index] / 10;
            setsCompletedTextViews[index].setText(completedSets + "/10");
        }
    }

    private void showEditDialog(int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Edit Workout");

        LinearLayout layout = new LinearLayout(requireActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputSets = new EditText(requireActivity());
        inputSets.setHint("Sets");
        layout.addView(inputSets);

        final EditText inputReps = new EditText(requireActivity());
        inputReps.setHint("Reps");
        layout.addView(inputReps);

        builder.setView(layout);

        builder.setPositiveButton("Change", (dialog, which) -> {
            String sets = inputSets.getText().toString();
            String reps = inputReps.getText().toString();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private Map<String, List<Workout>> getWorkoutsForPlan(String plan) {
        List<Workout> workouts = Arrays.asList(
                new Workout("Push-Up", 10, "Calisthenics",
                        "A bodyweight exercise that strengthens the chest, triceps, and shoulders",
                        Arrays.asList("Chest", "Triceps", "Shoulders"), 80,
                        R.drawable.pushupsimage, Arrays.asList(3, 15), 1, R.drawable.push_up_thumbnail,R.drawable.pushupgif,0),

                new Workout("Jump Rope", 15, "Calisthenics",
                        "A high-intensity cardio workout that burns fat and improves coordination",
                        Arrays.asList("Legs", "Core"), 200, R.drawable.jumpropesimage, Arrays.asList(1, 15), 1, R.drawable.jump_rope_thumbnail,R.drawable.jumpropegif,0),

                new Workout("Squats", 12, "Bodybuilding",
                        "Strengthens the lower body including the glutes, hamstrings, and quads",
                        Arrays.asList("Legs"), 100, R.drawable.squatimage, Arrays.asList(4, 12), 1, R.drawable.squats_thumbnail,R.drawable.squatgif,0),

                new Workout("Deadlift", 15, "Bodybuilding",
                        "A compound lift that strengthens the entire posterior chain",
                        Arrays.asList("Back", "Legs"), 150, R.drawable.deadliftimage, Arrays.asList(5, 8), 2, R.drawable.deadlift_thumbnail,R.drawable.deadliftgif,0),

                new Workout("Burpees", 8, "Calisthenics",
                        "A full-body exercise combining cardio and strength",
                        Arrays.asList("Legs", "Core"), 120, R.drawable.burpeeimage, Arrays.asList(3, 12), 4, R.drawable.burpees_thumbnail,R.drawable.burpreesgif,0),

                new Workout("Lunges", 10, "Bodybuilding",
                        "Great for targeting the glutes, quads, and hamstrings",
                        Arrays.asList("Legs"), 90, R.drawable.lunges, Arrays.asList(3, 12), 2, R.drawable.lunges_thumbnail,R.drawable.lungesgif,0),

                new Workout("Plank", 5, "Calisthenics",
                        "Strengthens the core and improves overall stability",
                        Arrays.asList("Core", "Shoulders"), 40, R.drawable.plankimage, Arrays.asList(3, 1), 1, R.drawable.plank_thumbnail,R.drawable.plankgif,0),

                new Workout("Mountain Climbers", 8, "Calisthenics",
                        "Combines cardio and core strength",
                        Arrays.asList("Core", "Legs", "Shoulders"), 110, R.drawable.mountainclimbersimage, Arrays.asList(4, 30), 2, R.drawable.mountain_climbers_thumbnail,R.drawable.mountainclimbersgif,0),

                new Workout("Bench Press", 15, "Bodybuilding",
                        "Bench press workouts strengthen the chest and shoulders",
                        Arrays.asList("Chest", "Triceps", "Shoulders"), 140, R.drawable.benchpressimage, Arrays.asList(4, 10), 2, R.drawable.bench_press_thumbnail,R.drawable.bench_press,0),

                new Workout("Pull-Ups", 10, "Calisthenics",
                        "A compound movement targeting the upper back and arms",
                        Arrays.asList("Back", "Biceps"), 100, R.drawable.pullupsimage, Arrays.asList(3, 8), 3, R.drawable.pull_ups_thumbnail,R.drawable.pullupsgif,0),

                new Workout("Bicycle Crunch", 7, "Calisthenics",
                        "A dynamic ab workout targeting the obliques",
                        Arrays.asList("Abs", "Core"), 70, R.drawable.bicyclecrunchimage, Arrays.asList(4, 20), 1, R.drawable.bicycle_crunch_thumbnail,R.drawable.bicepcurlsgif,0),

                new Workout("Russian Twists", 8, "Calisthenics",
                        "Strengthens the obliques and the core",
                        Arrays.asList("Abs", "Core"), 80, R.drawable.russiantwistsimage, Arrays.asList(3, 20), 1, R.drawable.russian_twists_thumbnail,R.drawable.russiantwistsgif,0),

                new Workout("Dumbbell Rows", 12, "Bodybuilding",
                        "A great workout for the back and arms",
                        Arrays.asList("Back", "Biceps"), 90, R.drawable.dumbelrowsimage, Arrays.asList(3, 12), 2, R.drawable.dumbbell_rows_thumbnail,R.drawable.dumbelrowsgif,0),

                new Workout("Yoga: Sun Salutation", 20, "Mobility",
                        "A series of poses that improve flexibility and strength",
                        Arrays.asList("Core", "Shoulders", "Legs"), 70, R.drawable.sunsalutationimage, Arrays.asList(4, 1), 1, R.drawable.yoga_sun_salutation_thumbnail,R.drawable.sunsalutationgif,0),

                new Workout("Cycling", 30, "Calisthenics",
                        "A low-impact cardio workout",
                        Arrays.asList("Legs"), 250, R.drawable.cyclingimage, Arrays.asList(1, 30), 2, R.drawable.cycling_thumbnail,R.drawable.cyclinggif,0),

                new Workout("Swimming", 30, "Calisthenics",
                        "A full-body workout that is great for endurance",
                        Arrays.asList("Legs", "Core", "Back"), 300, R.drawable.swimmingimage, Arrays.asList(1, 30), 3, R.drawable.swimming_thumbnail,R.drawable.swimming_thumbnail,0),

                new Workout("Kettlebell Swings", 10, "Bodybuilding",
                        "Targets the hips and lower back while improving conditioning",
                        Arrays.asList("Lower Back", "Core"), 150, R.drawable.kettlebellswingimage, Arrays.asList(4, 15), 2, R.drawable.kettlebell_swings_thumbnail,R.drawable.kettlebellswingsgif,0),

                new Workout("Box Jumps", 6, "Calisthenics",
                        "A lower body explosive power workout",
                        Arrays.asList("Legs"), 120, R.drawable.boxjumpimage, Arrays.asList(3, 12), 3, R.drawable.box_jumps_thumbnail,R.drawable.boxjumpsgif,0),

                new Workout("Wall Sit", 5, "Calisthenics",
                        "Builds endurance in the quads and core",
                        Arrays.asList("Legs", "Core"), 40, R.drawable.wallsitimage, Arrays.asList(3, 1), 1, R.drawable.wall_sit_thumbnail,R.drawable.wallsitsgif,0),

                new Workout("Side Plank", 5, "Calisthenics",
                        "Strengthens the obliques and core",
                        Arrays.asList("Obliques", "Core"), 40, R.drawable.sideplankimage, Arrays.asList(3, 1), 2, R.drawable.side_plank_thumbnail,R.drawable.sideplankgif,0),

                new Workout("Rowing Machine", 20, "Calisthenics",
                        "A full-body cardio workout that strengthens the back",
                        Arrays.asList("Back", "Core", "Legs"), 250, R.drawable.rowingmachineimage, Arrays.asList(1, 20), 2, R.drawable.rowing_machine_thumbnail,R.drawable.rowingmachinegif,0),

                new Workout("Farmer’s Walk", 8, "Bodybuilding",
                        "Builds grip strength, core stability, and lower body endurance",
                        Arrays.asList("Core", "Shoulders"), 100, R.drawable.farmerswalkimage, Arrays.asList(3, 60), 2, R.drawable.farmers_walk_thumbnail,R.drawable.farmerswalkgif,0),

                new Workout("Leg Raises", 7, "Calisthenics",
                        "A great ab workout focusing on the lower abs",
                        Arrays.asList("Abs", "Core"), 60, R.drawable.legraisesimage, Arrays.asList(4, 12), 1, R.drawable.leg_raises_thumbnail,R.drawable.legraisesgif,0),

                new Workout("Tricep Dips", 6, "Calisthenics",
                        "Bodyweight exercise targeting the triceps",
                        Arrays.asList("Triceps", "Shoulders"), 80, R.drawable.tricepdipsimage, Arrays.asList(3, 12), 1 ,R.drawable.tricep_dips_thumbnail,R.drawable.tricepsdipsgif,0),

                new Workout("Bicep Curls", 8, "Bodybuilding",
                        "Isolates the biceps for strength",
                        Arrays.asList("Biceps"), 50, R.drawable.bicepcurlsimage, Arrays.asList(3, 10), 1, R.drawable.bicep_curls_thumbnail,R.drawable.bicepcurlsgif,0),

                new Workout("Leg Press", 12, "Bodybuilding",
                        "Targets the quads and glutes using a machine",
                        Arrays.asList("Legs"), 120, R.drawable.legpressimage, Arrays.asList(4, 10), 2, R.drawable.leg_press_thumbnail,R.drawable.legraisesgif,0),

                new Workout("Overhead Press", 10, "Bodybuilding",
                        "Strengthens the shoulders and upper chest",
                        Arrays.asList("Shoulders"), 90, R.drawable.overheadpressimage, Arrays.asList(3, 10), 3, R.drawable.overhead_press_thumbnail,R.drawable.overheadpressgif,0),

                new Workout("Incline Bench Press", 15, "Bodybuilding",
                        "Focuses on the upper chest and shoulders",
                        Arrays.asList("Upper Chest", "Shoulders"), 150, R.drawable.inclinebenchpressimage, Arrays.asList(4, 8), 3, R.drawable.incline_bench_press_thumbnail,R.drawable.inclinebenchpressgif,0),

                new Workout("Glute Bridge", 8, "Bodybuilding",
                        "A lower-body exercise targeting the glutes",
                        Arrays.asList("Core"), 60, R.drawable.glutesbridgeimage, Arrays.asList(3, 15), 2, R.drawable.glute_bridge_thumbnail,R.drawable.glutesbridgegif,0),

                new Workout("Full-Body Circuit", 60, "Calisthenics",
                        "A high-intensity circuit that targets all major muscle groups with strength and cardio exercises",
                        Arrays.asList("Full Body", "Legs", "Core"), 600, R.drawable.fullcircuitimage, Arrays.asList(4, 12), 3, R.drawable.full_body_circuit_thumbnail,R.drawable.fullbodygif,0),

                new Workout("Functional Training", 60, "Bodybuilding",
                        "Focus on functional movements like squats, lunges, push-ups, and pull-ups to improve overall strength",
                        Arrays.asList("Full Body", "Legs", "Core"), 550, R.drawable.functionaltrainingimage, Arrays.asList(4, 15), 3, R.drawable.functional_training_thumbnail,R.drawable.functionaltraininggif,0),

                new Workout("HIIT Full Body", 60, "Calisthenics",
                        "A high-intensity interval training workout targeting the full body, alternating between strength and cardio",
                        Arrays.asList("Full Body", "Core"), 650, R.drawable.hiitimage, Arrays.asList(5, 10), 3, R.drawable.hiit_full_body_thumbnail,R.drawable.hiitgif,0),

                new Workout("Endurance Full Body", 90, "Calisthenics",
                        "A 90-minute endurance workout incorporating both strength exercises and steady-state cardio",
                        Arrays.asList("Full Body", "Legs", "Core"), 900, R.drawable.endurancimage, Arrays.asList(5, 15), 3, R.drawable.endurance_full_body_thumbnail,R.drawable.endurancegif,0),

                new Workout("Advanced Strength Full Body", 90, "Bodybuilding",
                        "An advanced strength training workout focusing on heavy compound movements to target all muscle groups",
                        Arrays.asList("Full Body", "Legs", "Core"), 850, R.drawable.advancedfullbodyimage, Arrays.asList(4, 10), 3, R.drawable.advanced_strength_full_body_thumbnail,R.drawable.advancedstrngthgif,0),

                new Workout("Full Body Yoga and Strength", 90, "Mobility",
                        "A combination of yoga and strength exercises to improve flexibility, endurance, and muscle strength",
                        Arrays.asList("Full Body", "Legs", "Core"), 800, R.drawable.yogastrengthimage, Arrays.asList(6, 12), 2, R.drawable.full_body_yoga_and_strength_thumbnail,R.drawable.yogastrengthgif,0)
        );

        Random random = new Random();
        int workoutsPerDay = random.nextInt(3) + 3;

        Map<String, List<Workout>> workoutsPerDayMap = new HashMap<>();

        if(!ranodomSuggestion){
            switch (plan) {
                case "planA":
                    user.setSundayList(getRandomWorkouts(workouts, workoutsPerDay));
                    user.setTuesdayList(getRandomWorkouts(workouts, workoutsPerDay));
                    user.setThursdayList(getRandomWorkouts(workouts, workoutsPerDay));
                    user.setSaturdayList(getRandomWorkouts(workouts, workoutsPerDay));
                    break;

                case "planB":
                    user.setMondayList(getRandomWorkouts(workouts, workoutsPerDay));
                    user.setWednesdayList(getRandomWorkouts(workouts, workoutsPerDay));
                    user.setFridayList(getRandomWorkouts(workouts, workoutsPerDay));
                    user.setSundayList(getRandomWorkouts(workouts, workoutsPerDay));
                    break;

                case "planC":
                    user.setTuesdayList(getRandomWorkouts(workouts, workoutsPerDay));
                    user.setThursdayList(getRandomWorkouts(workouts, workoutsPerDay));
                    user.setSaturdayList(getRandomWorkouts(workouts, workoutsPerDay));
                    user.setMondayList(getRandomWorkouts(workouts, workoutsPerDay));
                    break;

                default:
                    break;
            }
        }

        ranodomSuggestion = true;
        Executors.newSingleThreadExecutor().execute(() -> userRoomRepository.updateUserLocally(user));
        new Thread(() -> userServerRepository.updateUser(user, new UserServerRepository.OnUserUpdateCallback() {
            @Override
            public void onSuccess() {
                getActivity().runOnUiThread(() -> System.out.println("successfully updated"));
            }

            @Override
            public void onFailure(String errorMessage) {
                System.out.println("failed to update");
            }
        })).start();
        workoutsPerDayMap.put("SUN", user.getSundayList());
        workoutsPerDayMap.put("MON", user.getMondayList());
        workoutsPerDayMap.put("TUE", user.getTuesdayList());
        workoutsPerDayMap.put("WED", user.getWednesdayList());
        workoutsPerDayMap.put("THU", user.getThursdayList());
        workoutsPerDayMap.put("FRI", user.getFridayList());
        workoutsPerDayMap.put("SAT", user.getSaturdayList());
        return workoutsPerDayMap;
    }

    private List<Workout> getRandomWorkouts(List<Workout> allWorkouts, int workoutCount) {
        List<Workout> selectedWorkouts = new ArrayList<>();

        List<Workout> shuffledWorkouts = new ArrayList<>(allWorkouts);
        Collections.shuffle(shuffledWorkouts, new Random());

        for (int i = 0; i < workoutCount && i < shuffledWorkouts.size(); i++) {
            selectedWorkouts.add(shuffledWorkouts.get(i));
        }

        return selectedWorkouts;
    }

    private void displayRecommendedWorkoutsForDay(String dayOfWeek) {
        String recommendedPlan = getRecommendedPlan();
        Map<String, List<Workout>> workoutsForPlan = getWorkoutsForPlan(recommendedPlan);

        List<Workout> workouts = workoutsForPlan.get(dayOfWeek);

        LinearLayout workoutsContainer = requireView().findViewById(R.id.workoutsContainer);
        workoutsContainer.removeAllViews();

        if (workouts != null) {
            trainingLayouts = new LinearLayout[workouts.size()];

            for (int i = 0; i < workouts.size(); i++) {
                Workout workout = workouts.get(i);

                LinearLayout workoutTemplate = (LinearLayout) LayoutInflater.from(requireContext())
                        .inflate(R.layout.workout_item_template, workoutsContainer, false);

                populateWorkoutDetails(workoutTemplate, workout);

                workoutsContainer.addView(workoutTemplate);

                trainingLayouts[i] = workoutTemplate;

                setupClickListenersForWorkout(workoutTemplate, workout);
                setupDragAndDropForWorkout(workoutTemplate);
            }
        } else {
            Toast.makeText(requireContext(), "No workouts available for " + dayOfWeek, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupDayButtons() {
        View.OnClickListener dayButtonClickListener = view -> {
            if (selectedDayButton != null) {
                selectedDayButton.setBackgroundResource(R.drawable.s000000sw1cr18lr27017b7d9cc0c6073cc);
                selectedDayButton.setSelected(false);
            }

            Button clickedButton = (Button) view;
            clickedButton.setBackgroundResource(R.drawable.s000000sw1cr18bffffff);
            clickedButton.setSelected(true);
            selectedDayButton = clickedButton;

            String dayOfWeek = clickedButton.getText().toString();
            displayRecommendedWorkoutsForDay(dayOfWeek);
        };

        for (Button dayButton : dayButtons) {
            dayButton.setOnClickListener(dayButtonClickListener);
        }
    }


    private void setupEditPlanButton() {
        editPlanButton.setOnClickListener(v -> toggleEditMode());
        trashIcon.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Delete All Workouts");
        builder.setMessage("Are you sure you want to delete all workouts from your plan?");

        builder.setPositiveButton("Yes", (dialog, which) -> deleteAllWorkoutsFromPlan());
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void deleteAllWorkoutsFromPlan() {
        LinearLayout todaysTrainingSection = requireView().findViewById(R.id.workoutsContainer);
        todaysTrainingSection.removeAllViews();
        Toast.makeText(requireContext(), "All workouts have been deleted from the plan.", Toast.LENGTH_SHORT).show();
    }

    private void toggleEditMode() {
        isEditModeEnabled = !isEditModeEnabled;
        editPlanText.setText(isEditModeEnabled ? "Done" : "Edit Plan");
        if (isFirstTime) {
            setupDragAndDrop();
            isFirstTime = false;
        }
        LinearLayout workoutDetailsLayout = requireView().findViewById(R.id.workout_details_layout);
        if (workoutDetailsLayout.getVisibility() == View.VISIBLE)
            resetLayout();
        trashIcon.setVisibility(isEditModeEnabled ? View.VISIBLE : View.GONE);
        addWorkoutButton.setVisibility(isEditModeEnabled ? View.VISIBLE : View.GONE);
        enableDrag(isEditModeEnabled);
        if (isEditModeEnabled) {
            Toast.makeText(requireContext(), "Dragging workouts(by long clicking) to rearrange them or delete them is enabled", Toast.LENGTH_LONG).show();

        }
        else{
            Toast.makeText(requireContext(), "Edit mode is disabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void enableDrag(boolean enable) {
        if (trainingLayouts != null) {
            for (int i = 0; i < trainingLayouts.length; i++) {
                final int index = i;
                trainingLayouts[i].setOnLongClickListener(enable ? view -> handleLongClick(view, index) : null);
            }
        }
    }

    private void setupWorkoutListView() {
        workoutListView = requireView().findViewById(R.id.workout_list_view);
        workoutRepository.getAllWorkoutsLocally().observe(getViewLifecycleOwner(), workouts -> {
            if (workoutAdapter == null) {
                workoutAdapter = new WorkoutAdapterForMyplan(requireContext(), workouts);
                workoutListView.setAdapter(workoutAdapter);
            } else {
                workoutAdapter.notifyDataSetChanged();
            }
        });

        workoutListView.setOnItemLongClickListener((parent, view, position, id) -> {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.startDragAndDrop(data, shadowBuilder, view, 0);
            }
            view.setBackgroundColor(Color.WHITE);
            isWorkoutDeleted = true;
            return true;
        });
    }

    private void setupContainers(View view) {
        LinearLayout todaysTrainingSection = view.findViewById(R.id.rzacy26dzg1h);
        setupDropContainer(todaysTrainingSection);
    }

    private void setupDropContainer(LinearLayout dropContainer) {
        dropContainer.setOnDragListener((view, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    return true;
                case DragEvent.ACTION_DROP:
                    View draggedView = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) draggedView.getParent();
                    if (owner instanceof AdapterView) {
                        int position = workoutListView.getPositionForView(draggedView);
                        Object item = workoutAdapter.getItem(position);
                        if (item instanceof Workout) {
                            workoutAdapter.removeItem(position);
                            workoutAdapter.notifyDataSetChanged();
                            Workout workout = (Workout) item;
                            addWorkoutToPlan(workout);
                        }
                    } else if (owner != dropContainer) {
                        if (view instanceof LinearLayout && !(view == draggedView)) {
                            dropContainer.addView(draggedView);
                            owner.removeView(draggedView);
                        }
                    }
                    draggedView.setBackgroundResource(R.drawable.cr18bffffff);
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    return true;
                default:
                    return false;
            }
        });
    }

    private void addWorkoutToPlan(Workout workout) {
        LinearLayout todaysTrainingSection = requireView().findViewById(R.id.workoutsContainer);

        LinearLayout workoutTemplate = (LinearLayout) LayoutInflater.from(requireContext()).inflate(R.layout.workout_item_template, todaysTrainingSection, false);

        populateWorkoutDetails(workoutTemplate, workout);

        todaysTrainingSection.addView(workoutTemplate);

        setupClickListenersForWorkout(workoutTemplate, workout);
        setupDragAndDropForWorkout(workoutTemplate);
        System.out.println("workout name: " + workout.getWorkoutName());
        addWorkoutToUserForDay(workout);
    }

    private void addWorkoutToUserForDay(Workout workout) {
        String dayOfWeek = selectedDayButton.getText().toString();
        System.out.println("entered the add");
        switch (dayOfWeek) {
            case "SUN":
                if (user.getSundayList() == null) {
                    user.setSundayList(new ArrayList<>());
                }
                user.getSundayList().add(workout);
                break;
            case "MON":
                if (user.getMondayList() == null) {
                    user.setMondayList(new ArrayList<>());
                }
                user.getMondayList().add(workout);
                break;
            case "TUE":
                if (user.getTuesdayList() == null) {
                    user.setTuesdayList(new ArrayList<>());
                    System.out.println("hello from the inside");
                }
                user.getTuesdayList().add(workout);
                System.out.println("hello after");
                System.out.println(user.getTuesdayList());
                break;
            case "WED":
                if (user.getWednesdayList() == null) {
                    user.setWednesdayList(new ArrayList<>());
                }
                user.getWednesdayList().add(workout);
                break;
            case "THU":
                if (user.getThursdayList() == null) {
                    user.setThursdayList(new ArrayList<>());
                }
                user.getThursdayList().add(workout);
                break;
            case "FRI":
                if (user.getFridayList() == null) {
                    user.setFridayList(new ArrayList<>());
                }
                user.getFridayList().add(workout);
                break;
            case "SAT":
                if (user.getSaturdayList() == null) {
                    user.setSaturdayList(new ArrayList<>());
                }
                user.getSaturdayList().add(workout);
                break;
            default:
                System.out.println("it is here");
                break;
        }

        updateUserInDatabase();
        saveUserToSharedPreferences();
    }

    private void deleteWorkoutFromPlan(Workout workout) {
        String dayOfWeek = selectedDayButton.getText().toString();

        switch (dayOfWeek) {
            case "SUN":
                if (user.getSundayList() == null) {
                    user.setSundayList(new ArrayList<>());
                }
                user.getSundayList().remove(workout);
                break;
            case "MON":
                if (user.getMondayList() == null) {
                    user.setMondayList(new ArrayList<>());
                }
                user.getMondayList().remove(workout);
                break;
            case "TUE":
                if (user.getTuesdayList() == null) {
                    user.setTuesdayList(new ArrayList<>());
                }
                user.getTuesdayList().remove(workout);
                break;
            case "WED":
                if (user.getWednesdayList() == null) {
                    user.setWednesdayList(new ArrayList<>());
                }
                user.getWednesdayList().remove(workout);
                break;
            case "THU":
                if (user.getThursdayList() == null) {
                    user.setThursdayList(new ArrayList<>());
                }
                user.getThursdayList().remove(workout);
                break;
            case "FRI":
                if (user.getFridayList() == null) {
                    user.setFridayList(new ArrayList<>());
                }
                user.getFridayList().remove(workout);
                break;
            case "SAT":
                if (user.getSaturdayList() == null) {
                    user.setSaturdayList(new ArrayList<>());
                }
                user.getSaturdayList().remove(workout);
                break;
        }

        updateUserInDatabase();
        saveUserToSharedPreferences();
    }

    private Workout findWorkoutFromView(View workoutView) {
        TextView workoutNameTextView = workoutView.findViewById(R.id.r58ginjerv5oa);
        String workoutName = workoutNameTextView.getText().toString();

        String dayOfWeek = selectedDayButton.getText().toString();
        List<Workout> dayWorkouts = null;

        switch (dayOfWeek) {
            case "SUN":
                dayWorkouts = user.getSundayList();
                break;
            case "MON":
                dayWorkouts = user.getMondayList();
                break;
            case "TUE":
                dayWorkouts = user.getTuesdayList();
                break;
            case "WED":
                dayWorkouts = user.getWednesdayList();
                break;
            case "THU":
                dayWorkouts = user.getThursdayList();
                break;
            case "FRI":
                dayWorkouts = user.getFridayList();
                break;
            case "SAT":
                dayWorkouts = user.getSaturdayList();
                break;
        }

        if (dayWorkouts != null) {
            for (Workout workout : dayWorkouts) {
                if (workout.getWorkoutName().equals(workoutName)) {
                    return workout;
                }
            }
        }

        return null;
    }


    private void updateUserInDatabase() {
        Executors.newSingleThreadExecutor().execute(() -> userRoomRepository.updateUserLocally(user));
        new Thread(() -> userServerRepository.updateUser(user, new UserServerRepository.OnUserUpdateCallback() {
            @Override
            public void onSuccess() {
                getActivity().runOnUiThread(() -> System.out.println("Successfully updated user on server"));
            }

            @Override
            public void onFailure(String errorMessage) {
                System.out.println("Failed to update user on server: " + errorMessage);
            }
        })).start();
    }

    private void populateWorkoutDetails(LinearLayout workoutLayout, Workout workout) {
        TextView workoutNameTextView = workoutLayout.findViewById(R.id.r58ginjerv5oa);
        TextView caloriesTextView = workoutLayout.findViewById(R.id.rqd9c60hxzfa);
        TextView nextSetTextView = workoutLayout.findViewById(R.id.rg5slbkteo9wa);
        ProgressBar progressBar = workoutLayout.findViewById(R.id.training_progress_bar2);
        TextView setsCompletedTextView = workoutLayout.findViewById(R.id.setsCompleted2);
        TextView durationTextView = workoutLayout.findViewById(R.id.rc5h6rvwtrkra);
        ShapeableImageView imageView = workoutLayout.findViewById(R.id.rlaob0eufl3a);

        if (workoutNameTextView != null) {
            workoutNameTextView.setText(workout.getWorkoutName());
        }

        if (caloriesTextView != null) {
            caloriesTextView.setText(workout.getCalories() + " Kcal");
        }

        if (nextSetTextView != null) {
            nextSetTextView.setText("Next set: " + workout.getSets_reps().get(0) + " reps");
        }

        if (setsCompletedTextView != null) {
            setsCompletedTextView.setText(workout.getSets()+ " / " + workout.getSets_reps().get(1));
        }

        if (durationTextView != null) {
            durationTextView.setText(workout.getDurationInMinutes() + " Mins");
        }

        if (imageView != null) {
            imageView.setImageResource(workout.getWorkoutLogoResId());
        }

        if (progressBar != null) {
            progressBar.setMax(workout.getSets_reps().get(1));
            progressBar.setProgress(workout.getSets());
            progressBar.setProgressDrawable(requireContext().getDrawable(R.drawable.progress_bar_custom));
        }
    }

    private void setupClickListenersForWorkout(View workoutView, Workout workout) {
        workoutView.setOnClickListener(view -> {
            if (!isEditModeEnabled) {
                increaseProgressBarForDynamic(workoutView, workout);
            } else {
                showEditDialogForDynamic(workoutView, workout);
            }
        });

        workoutView.setOnLongClickListener(view -> {
            if (isEditModeEnabled) {
                startDrag(view);
            } else {
                undoProgressForDynamic(workoutView, workout);
            }
            return true;
        });
    }

    private void showEditDialogForDynamic(View workoutView, Workout workout) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Edit Workout");

        LinearLayout layout = new LinearLayout(requireActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView setsTextView = workoutView.findViewById(R.id.setsCompleted2);

        final EditText inputSets = new EditText(requireActivity());
        inputSets.setHint("Sets:(" + workout.getSets_reps().get(1) + ")");
        layout.addView(inputSets);

        final EditText inputReps = new EditText(requireActivity());
        inputReps.setHint("Reps(" + workout.getSets_reps().get(0) + ")");
        layout.addView(inputReps);

        builder.setView(layout);

        builder.setPositiveButton("Change", (dialog, which) -> {
            String sets = inputSets.getText().toString();
            String reps = inputReps.getText().toString();
            int setsNum;
            int repsNum;
            if (sets.isEmpty() || reps.isEmpty()) {
                Toast.makeText(requireContext(), "Empty input", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                setsNum = Integer.parseInt(sets);
                repsNum = Integer.parseInt(reps);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                return;
            }
            if (setsNum <= 0 || repsNum <= 0) {
                Toast.makeText(requireContext(), "Sets and reps cannot be negative or zero", Toast.LENGTH_SHORT).show();
                return;
            }
            if (setsNum > 20 || repsNum > 20) {
                Toast.makeText(requireContext(), "Reps and sets cannot be greater than 20", Toast.LENGTH_SHORT).show();
                return;
            }
            workout.setSets_reps(Arrays.asList(Integer.parseInt(sets), Integer.parseInt(reps)));
            workoutRepository.updateWorkout(workout);

            TextView nextSetTextView = workoutView.findViewById(R.id.rg5slbkteo9wa);
            nextSetTextView.setText("Next set: " + reps + " reps");
            ProgressBar progressBar = workoutView.findViewById(R.id.training_progress_bar2);
            progressBar.setMax(workout.getSets_reps().get(0) * 10);
            progressBar.setProgress(0);
            setsTextView.setText("0/" + sets);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void setupDragAndDropForWorkout(View workoutView) {

        workoutView.setOnDragListener((view, event) -> {
            if (!isEditModeEnabled) {
                return false;
            }
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    view.setBackgroundColor(Color.LTGRAY);
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    view.setBackgroundColor(Color.WHITE);
                    return true;
                case DragEvent.ACTION_DROP:
                    View draggedView = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) draggedView.getParent();
                    ViewGroup targetParent = (ViewGroup) view.getParent();

                    if (owner instanceof AdapterView) {
                        int position = workoutListView.getPositionForView(draggedView);
                        Object item = workoutAdapter.getItem(position);
                        if (item instanceof Workout) {
                            workoutAdapter.removeItem(position);
                            workoutAdapter.notifyDataSetChanged();

                            Workout workout = (Workout) item;
                            addWorkoutToPlan(workout);
                        }
                    }
                    else if (owner instanceof LinearLayout && targetParent instanceof LinearLayout) {
                        if (view != draggedView) {
                            int sourceIndex = owner.indexOfChild(draggedView);
                            int targetIndex = targetParent.indexOfChild(view);

                            if (sourceIndex != -1 && targetIndex != -1) {
                                owner.removeView(draggedView);
                                targetParent.addView(draggedView, targetIndex);
                            }
                        }
                    }

                    draggedView.setBackgroundResource(R.drawable.cr18bffffff);

                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    view.setBackgroundResource(R.drawable.cr18bffffff);
                    return true;
                default:
                    return false;
            }
        });
    }

    private void increaseProgressBarForDynamic(View workoutView, Workout workout) {
        ProgressBar progressBar = workoutView.findViewById(R.id.training_progress_bar2);
        TextView setsCompletedTextView = workoutView.findViewById(R.id.setsCompleted2);

        int maxSets = workout.getSets_reps().get(1);

        int progress = workout.getSets();

        if (progress < maxSets) {
            progress++;
            workout.setSets(progress);

            progressBar.setProgress(progress);
            setsCompletedTextView.setText(progress + "/" + maxSets);

            updateUserInDatabase();
            saveUserToSharedPreferences();
        }
    }

    private void undoProgressForDynamic(View workoutView, Workout workout) {
        ProgressBar progressBar = workoutView.findViewById(R.id.training_progress_bar2);
        TextView setsCompletedTextView = workoutView.findViewById(R.id.setsCompleted2);

        int progress = workout.getSets();
        int maxSets = workout.getSets_reps().get(1);

        if (progress > 0) {
            progress--;
            workout.setSets(progress);

            progressBar.setProgress(progress);
            setsCompletedTextView.setText(progress + "/" + maxSets);

            updateUserInDatabase();
            saveUserToSharedPreferences();
        }
    }


    private void setupDragAndDrop() {
        View.OnLongClickListener longClickListener = view -> {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);

            view.setBackgroundColor(Color.TRANSPARENT);

            return true;
        };

        View.OnDragListener dragListener = (view, event) -> {
            LinearLayout layout = (LinearLayout) view;
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    view.setBackgroundColor(Color.LTGRAY);
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    view.setBackgroundColor(Color.WHITE);
                    return true;
                case DragEvent.ACTION_DROP:
                    View sourceView = (View) event.getLocalState();
                    ViewGroup sourceParent = (ViewGroup) sourceView.getParent();
                    if (sourceParent instanceof ListView) {
                        ListView listView = (ListView) sourceParent;
                        int position = listView.getPositionForView(sourceView);
                        Workout workout = (Workout) workoutAdapter.getItem(position);

                        addWorkoutToPlan(workout);
                        return true;
                    }
                    LinearLayout targetLayout = (LinearLayout) view;
                    ViewGroup targetParent = (ViewGroup) targetLayout.getParent();

                    int sourceIndex = sourceParent.indexOfChild(sourceView);
                    int targetIndex = targetParent.indexOfChild(targetLayout);

                    if (sourceParent == targetParent) {
                        if (sourceIndex != targetIndex) {
                            rearrangeWorkouts(targetParent, sourceIndex, targetIndex);
                        }
                    }

                    sourceView.setVisibility(View.VISIBLE);
                    view.setBackgroundColor(Color.TRANSPARENT);
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    layout.setBackgroundResource(R.drawable.cr18bffffff);
                    return true;
                default:
                    return false;
            }
        };
        View.OnDragListener trashDragListener = new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        v.setAlpha(0.5f);
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        v.setAlpha(1.0f);
                        return true;
                    case DragEvent.ACTION_DROP:
                        View sourceView = (View) event.getLocalState();
                        ViewGroup sourceParent = (ViewGroup) sourceView.getParent();
                        sourceParent.removeView(sourceView);
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        v.setAlpha(1.0f);
                        return true;
                    default:
                        break;
                }
                return false;
            }
        };
        trashIcon.setOnDragListener(trashDragListener);
        trashIcon.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        trashIcon.setColorFilter(Color.RED);
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        trashIcon.setColorFilter(null);
                        break;
                    case DragEvent.ACTION_DROP:
                        if(!isWorkoutDeleted) {
                            View view = (View) event.getLocalState();
                            ViewGroup owner = (ViewGroup) view.getParent();
                            Workout workoutToRemove = findWorkoutFromView(view);
                            if (workoutToRemove != null) {
                                deleteWorkoutFromPlan(workoutToRemove);
                            }
                            owner.removeView(view);
                            break;
                        }

                    case DragEvent.ACTION_DRAG_ENDED:
                        trashIcon.setColorFilter(null);
                        trashIcon.setVisibility(View.VISIBLE);
                        isWorkoutDeleted=false;
                        break;
                }
                return true;
            }
        });

        LinearLayout workoutsContainer = requireView().findViewById(R.id.workoutsContainer);
        for (int i = 0; i < workoutsContainer.getChildCount(); i++) {
            View workoutView = workoutsContainer.getChildAt(i);
            workoutView.setOnLongClickListener(longClickListener);
            workoutView.setOnDragListener(dragListener);
        }
    }

    private void rearrangeWorkouts(ViewGroup parent, int sourceIndex, int targetIndex) {
        View draggedView = parent.getChildAt(sourceIndex);
        parent.removeViewAt(sourceIndex);
        if (targetIndex <= parent.getChildCount()) {
            parent.addView(draggedView, targetIndex);
        } else {
            parent.addView(draggedView);
        }
    }
}