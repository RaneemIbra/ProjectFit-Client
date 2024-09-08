package com.example.projectfit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.projectfit.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WorkoutActivity extends AppCompatActivity {

    private BottomNavigationView bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomBar = findViewById(R.id.bottom_navigation);
        loadWorkoutDetails();
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        bottomBar.setOnNavigationItemSelectedListener(item -> {
            int id_item = item.getItemId();

            if (id_item == R.id.home_BottomIcon) {
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
            } else {
                return false;
            }
        });
    }

    private void navigateTo(Class<?> targetActivity) {
        startActivity(new Intent(WorkoutActivity.this, targetActivity));
    }

    private void loadWorkoutDetails() {
        Intent intent = getIntent();
        String workoutName = intent.getStringExtra("workout_name");
        String workoutDescription = intent.getStringExtra("workout_description");
        int workoutImageResId = intent.getIntExtra("workout_image_res_id", R.drawable.img);
        int workoutGifResId = intent.getIntExtra("workout_gif_res_id", -1);

        TextView workoutNameTextView = findViewById(R.id.WorkoutTitle);
        TextView workoutDescriptionTextView = findViewById(R.id.WorkoutDescription);
        ImageView workoutImageView = findViewById(R.id.WorkoutImage);
        ImageView workoutGifImageView = findViewById(R.id.workout_gif);

        workoutNameTextView.setText(workoutName);
        workoutDescriptionTextView.setText(workoutDescription);

        Glide.with(this).load(workoutImageResId).into(workoutImageView);
        Glide.with(this).asGif().load(workoutGifResId).into(workoutGifImageView);
    }


    public void onBackClicked(View view) {
        navigateTo(WorkoutsListActivity.class);
        finish();
    }
}
