package com.example.projectfit.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.projectfit.Models.Workout;
import com.example.projectfit.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.projectfit.Room.Repositories.WorkoutRoomRepository;
import com.example.projectfit.Server.Repositories.WorkoutServerRepository;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class WorkoutsListActivity extends AppCompatActivity {

    BottomNavigationView bottomBar;
    Button button_home, button_profile, button_build_plan;
    LinearLayout layoutContainer;

    private WorkoutRoomRepository workoutRoomRepository;
    private WorkoutServerRepository workoutServerRepository;

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
        loadWorkouts();
        initClickListeners();
    }


    private void initClickListeners() {
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener (){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                int id_item=item.getItemId();
                if(id_item==R.id.home_BottomIcon)
                {
                    navigateTo(HomePageActivity.class);
                    return true;
                }
                else if (id_item == R.id.plan_BottomIcon)
                {
                    navigateTo(MyPlanActivity.class);
                    return true;
                }
                else if (id_item==R.id.workouts_BottomIcon)
                {
                    navigateTo(WorkoutsFilterActivity.class);
                    return true;
                }
                else if ( id_item==R.id.profile_BottomIcon)
                {
                    navigateTo(ProfileActivity.class);
                    return true;
                }
                else
                    return false;

            }
        });
    }

    private void initRepositories() {
        workoutRoomRepository = new WorkoutRoomRepository(this);
        workoutServerRepository = new WorkoutServerRepository();
    }

    private void initViews() {
        bottomBar=findViewById(R.id.bottom_navigation);
        button_home = findViewById(R.id.button_home_list);
        button_profile = findViewById(R.id.button_profile_list);
        button_build_plan = findViewById(R.id.button_build_plan_list);
        layoutContainer = findViewById(R.id.layoutContainer);
    }

    private void loadWorkouts() {
        workoutRoomRepository.getAllWorkoutsLocally().observe(this, workouts -> {
            if (workouts != null) {
                displayWorkouts(workouts);
            } else {
                Toast.makeText(WorkoutsListActivity.this, "No workouts available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayWorkouts(List<Workout> workouts) {
        LayoutInflater inflater = LayoutInflater.from(this);

        layoutContainer.removeAllViews();

        for (Workout workout : workouts) {
            View workoutView = inflater.inflate(R.layout.item_workout, layoutContainer, false);

            TextView workoutName = workoutView.findViewById(R.id.workoutName);
            TextView workoutDuration = workoutView.findViewById(R.id.workoutDuration);
            TextView workoutCalories = workoutView.findViewById(R.id.workoutCalories);
            ShapeableImageView workoutLogo = workoutView.findViewById(R.id.workoutLogo);

            workoutName.setText(workout.getWorkoutName());
            workoutDuration.setText(workout.getDurationInMinutes() + " Minutes");
            workoutCalories.setText(workout.getCalories() + " Kcal");

            if (workout.getWorkoutImageBase64() != null && !workout.getWorkoutImageBase64().isEmpty()) {
                byte[] imageBytes = Base64.decode(workout.getWorkoutImageBase64(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                workoutLogo.setImageBitmap(bitmap);
            } else {
                Glide.with(this).load(R.drawable.img).into(workoutLogo);
            }


            workoutView.setOnClickListener(v -> {
                Intent intent = new Intent(WorkoutsListActivity.this, WorkoutActivity.class);
                intent.putExtra("workout_name", workout.getWorkoutName());
                intent.putExtra("workout_duration", workout.getDurationInMinutes());
                intent.putExtra("workout_calories", workout.getCalories());
                intent.putExtra("workout_description", workout.getWorkoutDescription());
                intent.putExtra("workout_image_base64", workout.getWorkoutImageBase64());
                startActivity(intent);
            });

            layoutContainer.addView(workoutView);
        }
    }

    private void navigateTo(Class<?> targetActivity) {
        startActivity(new Intent(WorkoutsListActivity.this, targetActivity));
    }
}
