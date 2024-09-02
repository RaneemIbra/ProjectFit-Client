package com.example.projectfit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.projectfit.R;
import com.google.android.material.imageview.ShapeableImageView;

public class WorkoutsListActivity extends AppCompatActivity {
    Button button_home, button_profile, button_build_plan;
    LinearLayout layout1,layout2,layout3,layout4,layout5,layout6;

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
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image1));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image2));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image3));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image4));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image5));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image6));

        button_home = findViewById(R.id.button_home_list);
        button_profile = findViewById(R.id.button_profile_list);
        button_build_plan = findViewById(R.id.button_build_plan_list);
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
        layout4 = findViewById(R.id.layout4);
        layout5 = findViewById(R.id.layout5);
        layout6 = findViewById(R.id.layout6);
        setupNavigation();
    }
    private void setupNavigation() {
        button_home.setOnClickListener(view -> {
            Intent intent = new Intent(WorkoutsListActivity.this, HomePageActivity.class);
            startActivity(intent);
        });
        button_profile.setOnClickListener(view -> {
            Intent intent = new Intent(WorkoutsListActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
        button_build_plan.setOnClickListener(view -> {
            Intent intent = new Intent(WorkoutsListActivity.this, PlanQuestionsActivity.class);
            startActivity(intent);
        });
        layout1.setOnClickListener(view -> {
            Intent intent = new Intent(WorkoutsListActivity.this, WorkoutActivity.class);
            startActivity(intent);
        });
        layout2.setOnClickListener(view -> {
            Intent intent = new Intent(WorkoutsListActivity.this, WorkoutActivity.class);
            startActivity(intent);
        });
        layout3.setOnClickListener(view -> {
            Intent intent = new Intent(WorkoutsListActivity.this, WorkoutActivity.class);
            startActivity(intent);
        });
        layout4.setOnClickListener(view -> {
            Intent intent = new Intent(WorkoutsListActivity.this, WorkoutActivity.class);
            startActivity(intent);
        });
        layout5.setOnClickListener(view -> {
            Intent intent = new Intent(WorkoutsListActivity.this, WorkoutActivity.class);
            startActivity(intent);
        });
        layout6.setOnClickListener(view -> {
            Intent intent = new Intent(WorkoutsListActivity.this, WorkoutActivity.class);
            startActivity(intent);
        });
    }
}