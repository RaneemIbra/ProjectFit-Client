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
//            String pushupImage = Converters.convertDrawableToBase64(this, R.drawable.pushupsimage);
//            String pullupImage = Converters.convertDrawableToBase64(this, R.drawable.pullupsimage);
            List<Workout> workouts = Arrays.asList(
                    new Workout("Push-Up", 10, "Strength",
                            "A bodyweight exercise that strengthens the chest, triceps, and shoulders",
                            Arrays.asList("Chest", "Triceps", "Shoulders"), 80,
                            null,
                            null, null, Arrays.asList(3, 15), 2),

                    new Workout("Jump Rope", 15, "Cardio", "A high-intensity cardio workout that burns fat and improves coordination",
                            Arrays.asList("Calves", "Quads", "Hamstrings"), 200, null, null, null, Arrays.asList(1, 15), 3),

                    new Workout("Squats", 12, "Strength", "Strengthens the lower body including the glutes, hamstrings, and quads",
                            Arrays.asList("Glutes", "Quads", "Hamstrings"), 100, null, null, null, Arrays.asList(4, 12), 3),

                    new Workout("Deadlift", 15, "Strength", "A compound lift that strengthens the entire posterior chain",
                            Arrays.asList("Lower Back", "Glutes", "Hamstrings"), 150, null, null, null, Arrays.asList(5, 8), 4),

                    new Workout("Burpees", 8, "HIIT", "A full-body exercise combining cardio and strength",
                            Arrays.asList("Full Body"), 120, null, null, null, Arrays.asList(3, 12), 4),

                    new Workout("Lunges", 10, "Strength", "Great for targeting the glutes, quads, and hamstrings",
                            Arrays.asList("Quads", "Glutes", "Hamstrings"), 90, null, null, null, Arrays.asList(3, 12), 2),

                    new Workout("Plank", 5, "Core Strength", "Strengthens the core and improves overall stability",
                            Arrays.asList("Core", "Shoulders", "Glutes"), 40, null, null, null, Arrays.asList(3, 1), 2),

                    new Workout("Mountain Climbers", 8, "Cardio", "Combines cardio and core strength",
                            Arrays.asList("Core", "Quads", "Shoulders"), 110, null, null, null, Arrays.asList(4, 30), 3),

                    new Workout("Bench Press", 15, "Strength", "Bench press workouts are a staple in many strength training routines — it’s a dynamic, functional movement that works several muscles at once, including:\n" +
                            "\n" +
                            "Front deltoids and pectorals (chest muscles)\n" +
                            "Shoulders\n" +
                            "Biceps\n" +
                            "Triceps\n" +
                            "While the bench press is a simple movement, it’s highly effective for strengthening the chest and shoulders — and it can be easily modified to work different muscles with various grips and positions",
                            Arrays.asList("Chest", "Triceps", "Shoulders"), 140, null, null, null, Arrays.asList(4, 10), 4),

                    new Workout("Pull-Ups", 10, "Strength", "A compound movement targeting the upper back and arms",
                            Arrays.asList("Back", "Biceps"), 100, null, null, null, Arrays.asList(3, 8), 5),

                    new Workout("Bicycle Crunch", 7, "Core", "A dynamic ab workout targeting the obliques",
                            Arrays.asList("Core", "Obliques"), 70, null, null, null, Arrays.asList(4, 20), 2),

                    new Workout("Russian Twists", 8, "Core", "Strengthens the obliques and the core",
                            Arrays.asList("Core", "Obliques"), 80, null, null, null, Arrays.asList(3, 20), 2),

                    new Workout("Dumbbell Rows", 12, "Strength", "A great workout for the back and arms",
                            Arrays.asList("Back", "Biceps"), 90, null, null, null, Arrays.asList(3, 12), 3),

                    new Workout("Yoga: Sun Salutation", 20, "Flexibility and Strength", "A series of poses that improve flexibility and strength",
                            Arrays.asList("Core", "Shoulders", "Legs"), 70, null, null, null, Arrays.asList(4, 1), 2),

                    new Workout("Cycling", 30, "Cardio", "A low-impact cardio workout",
                            Arrays.asList("Quads", "Calves", "Glutes"), 250, null, null, null, Arrays.asList(1, 30), 2),

                    new Workout("Swimming", 30, "Cardio", "A full-body workout that is great for endurance",
                            Arrays.asList("Full Body"), 300, null, null, null, Arrays.asList(1, 30), 3),

                    new Workout("Kettlebell Swings", 10, "Strength & Cardio", "Targets the hips and lower back while improving conditioning",
                            Arrays.asList("Hips", "Lower Back", "Core"), 150, null, null, null, Arrays.asList(4, 15), 3),

                    new Workout("Box Jumps", 6, "Plyometrics", "A lower body explosive power workout",
                            Arrays.asList("Quads", "Glutes", "Calves"), 120, null, null, null, Arrays.asList(3, 12), 4),

                    new Workout("Wall Sit", 5, "Strength", "Builds endurance in the quads and core",
                            Arrays.asList("Quads", "Glutes", "Core"), 40, null, null, null, Arrays.asList(3, 1), 2),

                    new Workout("Side Plank", 5, "Core", "Strengthens the obliques and core",
                            Arrays.asList("Obliques", "Core"), 40, null, null, null, Arrays.asList(3, 1), 2),

                    new Workout("Rowing Machine", 20, "Cardio", "A full-body cardio workout that strengthens the back",
                            Arrays.asList("Back", "Core", "Legs"), 250, null, null, null, Arrays.asList(1, 20), 3),

                    new Workout("Farmer’s Walk", 8, "Strength", "Builds grip strength, core stability, and lower body endurance",
                            Arrays.asList("Forearms", "Shoulders", "Core"), 100, null, null, null, Arrays.asList(3, 60), 3),

                    new Workout("Leg Raises", 7, "Core", "A great ab workout focusing on the lower abs",
                            Arrays.asList("Abs", "Core"), 60, null, null, null, Arrays.asList(4, 12), 2),

                    new Workout("Tricep Dips", 6, "Strength", "Bodyweight exercise targeting the triceps",
                            Arrays.asList("Triceps", "Shoulders"), 80, null, null, null, Arrays.asList(3, 12), 2),

                    new Workout("Bicep Curls", 8, "Strength", "Isolates the biceps for strength",
                            Arrays.asList("Biceps"), 50, null, null, null, Arrays.asList(3, 10), 2),

                    new Workout("Leg Press", 12, "Strength", "Targets the quads and glutes using a machine",
                            Arrays.asList("Quads", "Glutes"), 120, null, null, null, Arrays.asList(4, 10), 3),

                    new Workout("Overhead Press", 10, "Strength", "Strengthens the shoulders and upper chest",
                            Arrays.asList("Shoulders", "Upper Chest"), 90, null, null, null, Arrays.asList(3, 10), 3),

                    new Workout("Incline Bench Press", 15, "Strength", "Focuses on the upper chest and shoulders",
                            Arrays.asList("Upper Chest", "Shoulders"), 150, null, null, null, Arrays.asList(4, 8), 4),

                    new Workout("Glute Bridge", 8, "Strength", "A lower-body exercise targeting the glutes",
                            Arrays.asList("Glutes", "Core"), 60, null, null, null, Arrays.asList(3, 15), 2),

                    new Workout("Full-Body Circuit", 60, "Strength & Cardio",
                            "A high-intensity circuit that targets all major muscle groups with strength and cardio exercises",
                            Arrays.asList("Full Body"), 600, null, null, null, Arrays.asList(4, 12), 4),

                    new Workout("Functional Training", 60, "Functional Strength",
                            "Focus on functional movements like squats, lunges, push-ups, and pull-ups to improve overall strength",
                            Arrays.asList("Full Body"), 550, null, null, null, Arrays.asList(4, 15), 4),

                    new Workout("HIIT Full Body", 60, "HIIT",
                            "A high-intensity interval training workout targeting the full body, alternating between strength and cardio",
                            Arrays.asList("Full Body"), 650, null, null, null, Arrays.asList(5, 10), 5),

                    new Workout("Endurance Full Body", 90, "Strength & Endurance",
                            "A 90-minute endurance workout incorporating both strength exercises and steady-state cardio",
                            Arrays.asList("Full Body"), 900, null, null, null, Arrays.asList(5, 15), 4),

                    new Workout("Advanced Strength Full Body", 90, "Strength",
                            "An advanced strength training workout focusing on heavy compound movements to target all muscle groups",
                            Arrays.asList("Full Body"), 850, null, null, null, Arrays.asList(4, 10), 5),

                    new Workout("Full Body Yoga and Strength", 90, "Yoga & Strength",
                            "A combination of yoga and strength exercises to improve flexibility, endurance, and muscle strength",
                            Arrays.asList("Full Body"), 800, null, null, null, Arrays.asList(6, 12), 3)
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
