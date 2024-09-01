package com.example.projectfit.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.projectfit.R;
import com.google.android.material.imageview.ShapeableImageView;

public class MyPlanActivity extends AppCompatActivity {
    Button button_homePage, button_profilePage, button_workoutPage;
    Button button_sunday, button_monday, button_tuesday, button_wednesday, button_thursday, button_friday, button_saturday;
    Button selectedDayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_plan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rom7ulx78ml));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rd6bw1i18hr));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rj9f97ph8grd));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.re4ohojcs3l));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rwfhrlkzvuq));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rj8vvk076jho));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rlaob0eufl3));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rhh070fllio8));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rb39xm020i8));

        button_homePage = findViewById(R.id.button_home_my_plan);
        button_profilePage = findViewById(R.id.button_profile_my_plan);
        button_workoutPage = findViewById(R.id.button_workout_my_plan);
        button_sunday = findViewById(R.id.sunday);
        button_monday = findViewById(R.id.monday);
        button_tuesday = findViewById(R.id.tuesday);
        button_wednesday = findViewById(R.id.wednesday);
        button_thursday = findViewById(R.id.thursday);
        button_friday = findViewById(R.id.friday);
        button_saturday = findViewById(R.id.saturday);

        selectedDayButton = button_sunday;
        setupNavigation();
        setupDayButtons();
    }
    private void setupNavigation() {
        button_homePage.setOnClickListener(view -> {
            Intent intent = new Intent(MyPlanActivity.this, HomePageActivity.class);
            startActivity(intent);
        });
        button_profilePage.setOnClickListener(view -> {
            Intent intent = new Intent(MyPlanActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
        button_workoutPage.setOnClickListener(view -> {
            Intent intent = new Intent(MyPlanActivity.this, WorkoutsFilterActivity.class);
            startActivity(intent);
        });
    }
    private void setupDayButtons(){
        View.OnClickListener dayButtonClickListener = view -> {
            if (selectedDayButton != null) {
                selectedDayButton.setBackgroundResource(R.drawable.s000000sw1cr18lr27017b7d9cc0c6073cc);
                selectedDayButton.setSelected(false);
            }
            Button clickedButton = (Button) view;
            clickedButton.setBackgroundResource(R.drawable.s000000sw1cr18bffffff);
            clickedButton.setSelected(true);
            selectedDayButton = clickedButton;
        };

        button_sunday.setOnClickListener(dayButtonClickListener);
        button_monday.setOnClickListener(dayButtonClickListener);
        button_tuesday.setOnClickListener(dayButtonClickListener);
        button_wednesday.setOnClickListener(dayButtonClickListener);
        button_thursday.setOnClickListener(dayButtonClickListener);
        button_friday.setOnClickListener(dayButtonClickListener);
        button_saturday.setOnClickListener(dayButtonClickListener);
    }
}