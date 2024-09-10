package com.example.projectfit.Activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectfit.Models.Workout;
import com.example.projectfit.Room.Repositories.UserRoomRepository;
import com.example.projectfit.R;
import com.example.projectfit.Models.User;
import com.example.projectfit.Room.Repositories.WorkoutRoomRepository;
import com.example.projectfit.Server.Repositories.UserServerRepository;
import com.example.projectfit.Server.Repositories.WorkoutServerRepository;
import com.example.projectfit.Utils.Converters;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private UserServerRepository userServerRepository;
    private UserRoomRepository userRoomRepository;
    private WorkoutRoomRepository workoutRoomRepository;
    private WorkoutServerRepository workoutServerRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeRepositories();
        addAllWorkouts();
        transferUserDataToServer();
        setupNavigation();
    }

    private void initializeRepositories() {
        userServerRepository = new UserServerRepository();
        userRoomRepository = new UserRoomRepository(this);
        workoutRoomRepository = new WorkoutRoomRepository(this);
        workoutServerRepository = new WorkoutServerRepository();
    }

    private void addAllWorkouts() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Workout> workouts = Arrays.asList(
                    new Workout("Push-Up", 10, "Calisthenics",
                            "A bodyweight exercise that strengthens the chest, triceps, and shoulders",
                            Arrays.asList("Chest", "Triceps", "Shoulders"), 80,
                            R.drawable.pushupsimage, Arrays.asList(3, 15), 1, R.drawable.push_up_thumbnail,R.drawable.pushupgif),

                    new Workout("Jump Rope", 15, "Calisthenics",
                            "A high-intensity cardio workout that burns fat and improves coordination",
                            Arrays.asList("Legs", "Core"), 200, R.drawable.jumpropesimage, Arrays.asList(1, 15), 1, R.drawable.jump_rope_thumbnail,R.drawable.jumpropegif),

                    new Workout("Squats", 12, "Bodybuilding",
                            "Strengthens the lower body including the glutes, hamstrings, and quads",
                            Arrays.asList("Legs"), 100, R.drawable.squatimage, Arrays.asList(4, 12), 1, R.drawable.squats_thumbnail,R.drawable.squatgif),

                    new Workout("Deadlift", 15, "Bodybuilding",
                            "A compound lift that strengthens the entire posterior chain",
                            Arrays.asList("Back", "Legs"), 150, R.drawable.deadliftimage, Arrays.asList(5, 8), 2, R.drawable.deadlift_thumbnail,R.drawable.deadliftgif),

                    new Workout("Burpees", 8, "Calisthenics",
                            "A full-body exercise combining cardio and strength",
                            Arrays.asList("Legs", "Core"), 120, R.drawable.burpeeimage, Arrays.asList(3, 12), 4, R.drawable.burpees_thumbnail,R.drawable.burpreesgif),

                    new Workout("Lunges", 10, "Bodybuilding",
                            "Great for targeting the glutes, quads, and hamstrings",
                            Arrays.asList("Legs"), 90, R.drawable.lunges, Arrays.asList(3, 12), 2, R.drawable.lunges_thumbnail,R.drawable.lungesgif),

                    new Workout("Plank", 5, "Calisthenics",
                            "Strengthens the core and improves overall stability",
                            Arrays.asList("Core", "Shoulders"), 40, R.drawable.plankimage, Arrays.asList(3, 1), 1, R.drawable.plank_thumbnail,R.drawable.plankgif),

                    new Workout("Mountain Climbers", 8, "Calisthenics",
                            "Combines cardio and core strength",
                            Arrays.asList("Core", "Legs", "Shoulders"), 110, R.drawable.mountainclimbersimage, Arrays.asList(4, 30), 2, R.drawable.mountain_climbers_thumbnail,R.drawable.mountainclimbersgif),

                    new Workout("Bench Press", 15, "Bodybuilding",
                            "Bench press workouts strengthen the chest and shoulders",
                            Arrays.asList("Chest", "Triceps", "Shoulders"), 140, R.drawable.benchpressimage, Arrays.asList(4, 10), 2, R.drawable.bench_press_thumbnail,R.drawable.bench_press),

                    new Workout("Pull-Ups", 10, "Calisthenics",
                            "A compound movement targeting the upper back and arms",
                            Arrays.asList("Back", "Biceps"), 100, R.drawable.pullupsimage, Arrays.asList(3, 8), 3, R.drawable.pull_ups_thumbnail,R.drawable.pullupsgif),

                    new Workout("Bicycle Crunch", 7, "Calisthenics",
                            "A dynamic ab workout targeting the obliques",
                            Arrays.asList("Abs", "Core"), 70, R.drawable.bicyclecrunchimage, Arrays.asList(4, 20), 1, R.drawable.bicycle_crunch_thumbnail,R.drawable.bicepcurlsgif),

                    new Workout("Russian Twists", 8, "Calisthenics",
                            "Strengthens the obliques and the core",
                            Arrays.asList("Abs", "Core"), 80, R.drawable.russiantwistsimage, Arrays.asList(3, 20), 1, R.drawable.russian_twists_thumbnail,R.drawable.russiantwistsgif),

                    new Workout("Dumbbell Rows", 12, "Bodybuilding",
                            "A great workout for the back and arms",
                            Arrays.asList("Back", "Biceps"), 90, R.drawable.dumbelrowsimage, Arrays.asList(3, 12), 2, R.drawable.dumbbell_rows_thumbnail,R.drawable.dumbelrowsgif),

                    new Workout("Yoga: Sun Salutation", 20, "Mobility",
                            "A series of poses that improve flexibility and strength",
                            Arrays.asList("Core", "Shoulders", "Legs"), 70, R.drawable.sunsalutationimage, Arrays.asList(4, 1), 1, R.drawable.yoga_sun_salutation_thumbnail,R.drawable.sunsalutationgif),

                    new Workout("Cycling", 30, "Calisthenics",
                            "A low-impact cardio workout",
                            Arrays.asList("Legs"), 250, R.drawable.cyclingimage, Arrays.asList(1, 30), 2, R.drawable.cycling_thumbnail,R.drawable.cyclinggif),

                    new Workout("Swimming", 30, "Calisthenics",
                            "A full-body workout that is great for endurance",
                            Arrays.asList("Legs", "Core", "Back"), 300, R.drawable.swimmingimage, Arrays.asList(1, 30), 3, R.drawable.swimming_thumbnail,R.drawable.swimming_thumbnail),

                    new Workout("Kettlebell Swings", 10, "Bodybuilding",
                            "Targets the hips and lower back while improving conditioning",
                            Arrays.asList("Lower Back", "Core"), 150, R.drawable.kettlebellswingimage, Arrays.asList(4, 15), 2, R.drawable.kettlebell_swings_thumbnail,R.drawable.kettlebellswingsgif),

                    new Workout("Box Jumps", 6, "Calisthenics",
                            "A lower body explosive power workout",
                            Arrays.asList("Legs"), 120, R.drawable.boxjumpimage, Arrays.asList(3, 12), 3, R.drawable.box_jumps_thumbnail,R.drawable.boxjumpsgif),

                    new Workout("Wall Sit", 5, "Calisthenics",
                            "Builds endurance in the quads and core",
                            Arrays.asList("Legs", "Core"), 40, R.drawable.wallsitimage, Arrays.asList(3, 1), 1, R.drawable.wall_sit_thumbnail,R.drawable.wallsitsgif),

                    new Workout("Side Plank", 5, "Calisthenics",
                            "Strengthens the obliques and core",
                            Arrays.asList("Obliques", "Core"), 40, R.drawable.sideplankimage, Arrays.asList(3, 1), 2, R.drawable.side_plank_thumbnail,R.drawable.sideplankgif),

                    new Workout("Rowing Machine", 20, "Calisthenics",
                            "A full-body cardio workout that strengthens the back",
                            Arrays.asList("Back", "Core", "Legs"), 250, R.drawable.rowingmachineimage, Arrays.asList(1, 20), 2, R.drawable.rowing_machine_thumbnail,R.drawable.rowingmachinegif),

                    new Workout("Farmer’s Walk", 8, "Bodybuilding",
                            "Builds grip strength, core stability, and lower body endurance",
                            Arrays.asList("Core", "Shoulders"), 100, R.drawable.farmerswalkimage, Arrays.asList(3, 60), 2, R.drawable.farmers_walk_thumbnail,R.drawable.farmerswalkgif),

                    new Workout("Leg Raises", 7, "Calisthenics",
                            "A great ab workout focusing on the lower abs",
                            Arrays.asList("Abs", "Core"), 60, R.drawable.legraisesimage, Arrays.asList(4, 12), 1, R.drawable.leg_raises_thumbnail,R.drawable.legraisesgif),

                    new Workout("Tricep Dips", 6, "Calisthenics",
                            "Bodyweight exercise targeting the triceps",
                            Arrays.asList("Triceps", "Shoulders"), 80, R.drawable.tricepdipsimage, Arrays.asList(3, 12), 1 ,R.drawable.tricep_dips_thumbnail,R.drawable.tricepsdipsgif),

                    new Workout("Bicep Curls", 8, "Bodybuilding",
                            "Isolates the biceps for strength",
                            Arrays.asList("Biceps"), 50, R.drawable.bicepcurlsimage, Arrays.asList(3, 10), 1, R.drawable.bicep_curls_thumbnail,R.drawable.bicepcurlsgif),

                    new Workout("Leg Press", 12, "Bodybuilding",
                            "Targets the quads and glutes using a machine",
                            Arrays.asList("Legs"), 120, R.drawable.legpressimage, Arrays.asList(4, 10), 2, R.drawable.leg_press_thumbnail,R.drawable.legraisesgif),

                    new Workout("Overhead Press", 10, "Bodybuilding",
                            "Strengthens the shoulders and upper chest",
                            Arrays.asList("Shoulders"), 90, R.drawable.overheadpressimage, Arrays.asList(3, 10), 3, R.drawable.overhead_press_thumbnail,R.drawable.overheadpressgif),

                    new Workout("Incline Bench Press", 15, "Bodybuilding",
                            "Focuses on the upper chest and shoulders",
                            Arrays.asList("Upper Chest", "Shoulders"), 150, R.drawable.inclinebenchpressimage, Arrays.asList(4, 8), 3, R.drawable.incline_bench_press_thumbnail,R.drawable.inclinebenchpressgif),

                    new Workout("Glute Bridge", 8, "Bodybuilding",
                            "A lower-body exercise targeting the glutes",
                            Arrays.asList("Core"), 60, R.drawable.glutesbridgeimage, Arrays.asList(3, 15), 2, R.drawable.glute_bridge_thumbnail,R.drawable.glutesbridgegif),

                    new Workout("Full-Body Circuit", 60, "Calisthenics",
                            "A high-intensity circuit that targets all major muscle groups with strength and cardio exercises",
                            Arrays.asList("Full Body", "Legs", "Core"), 600, R.drawable.fullcircuitimage, Arrays.asList(4, 12), 3, R.drawable.full_body_circuit_thumbnail,R.drawable.fullbodygif),

                    new Workout("Functional Training", 60, "Bodybuilding",
                            "Focus on functional movements like squats, lunges, push-ups, and pull-ups to improve overall strength",
                            Arrays.asList("Full Body", "Legs", "Core"), 550, R.drawable.functionaltrainingimage, Arrays.asList(4, 15), 3, R.drawable.functional_training_thumbnail,R.drawable.functionaltraininggif),

                    new Workout("HIIT Full Body", 60, "Calisthenics",
                            "A high-intensity interval training workout targeting the full body, alternating between strength and cardio",
                            Arrays.asList("Full Body", "Core"), 650, R.drawable.hiitimage, Arrays.asList(5, 10), 3, R.drawable.hiit_full_body_thumbnail,R.drawable.hiitgif),

                    new Workout("Endurance Full Body", 90, "Calisthenics",
                            "A 90-minute endurance workout incorporating both strength exercises and steady-state cardio",
                            Arrays.asList("Full Body", "Legs", "Core"), 900, R.drawable.endurancimage, Arrays.asList(5, 15), 3, R.drawable.endurance_full_body_thumbnail,R.drawable.endurancegif),

                    new Workout("Advanced Strength Full Body", 90, "Bodybuilding",
                            "An advanced strength training workout focusing on heavy compound movements to target all muscle groups",
                            Arrays.asList("Full Body", "Legs", "Core"), 850, R.drawable.advancedfullbodyimage, Arrays.asList(4, 10), 3, R.drawable.advanced_strength_full_body_thumbnail,R.drawable.advancedstrngthgif),

                    new Workout("Full Body Yoga and Strength", 90, "Mobility",
                            "A combination of yoga and strength exercises to improve flexibility, endurance, and muscle strength",
                            Arrays.asList("Full Body", "Legs", "Core"), 800, R.drawable.yogastrengthimage, Arrays.asList(6, 12), 2, R.drawable.full_body_yoga_and_strength_thumbnail,R.drawable.yogastrengthgif)
            );


            for (Workout workout : workouts) {
                if (workoutRoomRepository.getWorkoutByName(workout.getWorkoutName()) == null) {
                    workoutRoomRepository.addWorkoutLocally(workout);
                    workoutServerRepository.addWorkoutInServer(workout);
                }
            }
        });
        executor.shutdown();
    }

    private void setupNavigation() {
        setupButtonNavigation(R.id.HomePageBTN, HomePageActivity.class);
        setupButtonNavigation(R.id.MyPlanBTN, MyPlanActivity.class);
        setupButtonNavigation(R.id.PlanQuestionsBTN, PlanQuestionsActivity.class);
        setupButtonNavigation(R.id.WorkoutBTN, WorkoutActivity.class);
        setupButtonNavigation(R.id.WorkoutFilterBTN, WorkoutsFilterActivity.class);
        setupButtonNavigation(R.id.ListBTN, WorkoutsListActivity.class);
        setupButtonNavigation(R.id.resetPasswordPageBTN, ResetPasswordActivity.class);
        setupButtonNavigation(R.id.ProfilePageBTN, ProfileActivity.class);
        setupButtonNavigation(R.id.LoginPageBTN, LoginActivity.class);
        setupButtonNavigation(R.id.RegisterPageBTN, RegisterActivity.class);
        setupButtonNavigation(R.id.LoadingScreenBTN, LoadingScreenActivity.class);
    }

    private void setupButtonNavigation(int buttonId, Class<?> activityClass) {
        findViewById(buttonId).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, activityClass);
            startActivity(intent);
        });
    }

    private boolean isConnectedToServer() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void transferUserDataToServer() {
        if (isConnectedToServer()) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                List<User> localUsers = userRoomRepository.getAllUsersLocally();

                for (User user : localUsers) {
                    userServerRepository.addUserInServer(user);
                }

                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "All local users transferred to the server", Toast.LENGTH_SHORT).show();
                });
            });
            executor.shutdown();
        } else {
            Toast.makeText(MainActivity.this, "No network connection. Unable to transfer users.", Toast.LENGTH_SHORT).show();
        }
    }

}
