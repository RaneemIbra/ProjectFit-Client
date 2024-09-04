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
        initViews();
        initClickListeners();
    }

    private void navigateTo(Class<?> targetActivity) {
        startActivity(new Intent(WorkoutsListActivity.this, targetActivity));
    }
    private void initClickListeners() {
        button_home.setOnClickListener(v -> navigateTo(HomePageActivity.class));
        button_profile.setOnClickListener(v -> navigateTo(ProfileActivity.class));
        button_build_plan.setOnClickListener(v -> navigateTo(PlanQuestionsActivity.class));
        layout1.setOnClickListener(v -> navigateTo(WorkoutActivity.class));
        layout2.setOnClickListener(v -> navigateTo(WorkoutActivity.class));
        layout3.setOnClickListener(v -> navigateTo(WorkoutActivity.class));
        layout4.setOnClickListener(v -> navigateTo(WorkoutActivity.class));
        layout5.setOnClickListener(v -> navigateTo(WorkoutActivity.class));
        layout6.setOnClickListener(v -> navigateTo(WorkoutActivity.class));
    }
    private void initViews() {
        button_home = findViewById(R.id.button_home_list);
        button_profile = findViewById(R.id.button_profile_list);
        button_build_plan = findViewById(R.id.button_build_plan_list);
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
        layout4 = findViewById(R.id.layout4);
        layout5 = findViewById(R.id.layout5);
        layout6 = findViewById(R.id.layout6);
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image1));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image2));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image3));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image4));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image5));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image6));
    }
}