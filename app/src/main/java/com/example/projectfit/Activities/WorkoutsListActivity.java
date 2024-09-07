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
        observeWorkouts();
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

    private void observeWorkouts() {
        workoutRoomRepository.getAllWorkoutsLocally().observe(this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(List<Workout> workouts) {
                if (workouts != null && !workouts.isEmpty()) {
                    workoutAdapter.setWorkouts(workouts);
                } else {
                    Toast.makeText(WorkoutsListActivity.this, "No workouts available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onWorkoutSelected(Workout workout) {
        Intent intent = new Intent(WorkoutsListActivity.this, WorkoutActivity.class);
        intent.putExtra("workout_name", workout.getWorkoutName());
        intent.putExtra("workout_duration", workout.getDurationInMinutes());
        intent.putExtra("workout_calories", workout.getCalories());
        intent.putExtra("workout_description", workout.getWorkoutDescription());
        intent.putExtra("workout_image_res_id", workout.getWorkoutImageResId());
        startActivity(intent);
    }

    private void navigateTo(Class<?> targetActivity) {
        startActivity(new Intent(WorkoutsListActivity.this, targetActivity));
    }
}
