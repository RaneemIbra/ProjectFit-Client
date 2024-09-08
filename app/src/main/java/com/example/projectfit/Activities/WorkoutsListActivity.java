package com.example.projectfit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectfit.Models.Workout;
import com.example.projectfit.R;
import com.example.projectfit.Room.Repositories.WorkoutRoomRepository;
import com.example.projectfit.Utils.WorkoutAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class WorkoutsListActivity extends AppCompatActivity {

    private BottomNavigationView bottomBar;
    private WorkoutRoomRepository workoutRoomRepository;
    private RecyclerView workoutRecyclerView;
    private WorkoutAdapter workoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workouts_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        initRepositories();
        observeFilteredWorkouts();
        initClickListeners();
    }

    private void initClickListeners() {
        bottomBar.setOnNavigationItemSelectedListener(item -> {
            int id_item = item.getItemId();
            if (id_item == R.id.home_BottomIcon) {
                navigateTo(HomePageActivity.class);
                return true;
            } else if (id_item == R.id.plan_BottomIcon) {
                navigateTo(MyPlanActivity.class);
                return true;
            } else if (id_item == R.id.workouts_BottomIcon) {
                navigateTo(WorkoutsFilterActivity.class);
                return true;
            } else if (id_item == R.id.profile_BottomIcon) {
                navigateTo(ProfileActivity.class);
                return true;
            } else
                return false;
        });
    }

    private void initRepositories() {
        workoutRoomRepository = new WorkoutRoomRepository(this);
    }

    private void initViews() {
        bottomBar = findViewById(R.id.bottom_navigation);
        workoutRecyclerView = findViewById(R.id.workoutRecyclerView);
        workoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        workoutAdapter = new WorkoutAdapter(this::onWorkoutSelected);
        workoutRecyclerView.setAdapter(workoutAdapter);
    }

    private void observeFilteredWorkouts() {
        String workoutCategory = getIntent().getStringExtra("workout_category");
        int difficultyLevel = getIntent().getIntExtra("difficulty_level", -1);
        String muscle = getIntent().getStringExtra("muscle");
        int minDuration = getIntent().getIntExtra("min_duration", -1);
        int maxDuration = getIntent().getIntExtra("max_duration", -1);

        if (workoutCategory != null) {
            workoutRoomRepository.getWorkoutsByType(workoutCategory).observe(this, workouts -> {
                if (workouts != null && !workouts.isEmpty()) {
                    workoutAdapter.setWorkouts(workouts);
                } else {
                    Toast.makeText(WorkoutsListActivity.this, "No workouts available for the selected category", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (difficultyLevel != -1) {
            workoutRoomRepository.getWorkoutsByDifficulty(difficultyLevel).observe(this, workouts -> {
                if (workouts != null && !workouts.isEmpty()) {
                    workoutAdapter.setWorkouts(workouts);
                } else {
                    Toast.makeText(WorkoutsListActivity.this, "No workouts available for the selected difficulty", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (muscle != null) {
            workoutRoomRepository.getWorkoutsByMuscle(muscle).observe(this, workouts -> {
                if (workouts != null && !workouts.isEmpty()) {
                    workoutAdapter.setWorkouts(workouts);
                } else {
                    Toast.makeText(WorkoutsListActivity.this, "No workouts available for the selected muscle", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (minDuration != -1 && maxDuration != -1) {
            workoutRoomRepository.getWorkoutsByDurationRange(minDuration, maxDuration).observe(this, workouts -> {
                if (workouts != null && !workouts.isEmpty()) {
                    workoutAdapter.setWorkouts(workouts);
                } else {
                    Toast.makeText(WorkoutsListActivity.this, "No workouts available for the selected duration", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            workoutRoomRepository.getAllWorkoutsLocally().observe(this, workouts -> {
                if (workouts != null && !workouts.isEmpty()) {
                    workoutAdapter.setWorkouts(workouts);
                } else {
                    Toast.makeText(WorkoutsListActivity.this, "No workouts available", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void onWorkoutSelected(Workout workout) {
        Intent intent = new Intent(WorkoutsListActivity.this, WorkoutActivity.class);
        intent.putExtra("workout_name", workout.getWorkoutName());
        intent.putExtra("workout_duration", workout.getDurationInMinutes());
        intent.putExtra("workout_calories", workout.getCalories());
        intent.putExtra("workout_description", workout.getWorkoutDescription());
        intent.putExtra("workout_image_res_id", workout.getWorkoutImageResId());
        intent.putExtra("workout_gif_res_id", workout.getWorkoutGifResId());
        startActivity(intent);
    }

    private void navigateTo(Class<?> targetActivity) {
        startActivity(new Intent(WorkoutsListActivity.this, targetActivity));
    }
}
