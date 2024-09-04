package com.example.projectfit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectfit.R;

public class MyPlanActivity extends AppCompatActivity {
    private Button buttonHomePage, buttonProfilePage, buttonWorkoutPage;
    private Button[] dayButtons;
    private Button selectedDayButton;
    private ProgressBar[] progressBars;
    private LinearLayout[] trainingLayouts;
    private int[] progressStatuses = {0, 0, 0};

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
        initViews();
        setupNavigation();
        setupDayButtons();
        setupProgressBars();
    }

    private void initViews() {
        buttonHomePage = findViewById(R.id.button_home_my_plan);
        buttonProfilePage = findViewById(R.id.button_profile_my_plan);
        buttonWorkoutPage = findViewById(R.id.button_workout_my_plan);

        dayButtons = new Button[]{
                findViewById(R.id.sunday),
                findViewById(R.id.monday),
                findViewById(R.id.tuesday),
                findViewById(R.id.wednesday),
                findViewById(R.id.thursday),
                findViewById(R.id.friday),
                findViewById(R.id.saturday)
        };
        selectedDayButton = dayButtons[0];

        progressBars = new ProgressBar[]{
                findViewById(R.id.training_progress_bar1),
                findViewById(R.id.training_progress_bar2),
                findViewById(R.id.training_progress_bar3)
        };
        trainingLayouts = new LinearLayout[]{
                findViewById(R.id.Layout1),
                findViewById(R.id.Layout2),
                findViewById(R.id.Layout3)
        };
    }

    private void setupProgressBars() {
        for (int i = 0; i < trainingLayouts.length; i++) {
            final int index = i;
            trainingLayouts[i].setOnClickListener(view -> increaseProgressBar(index));
        }
    }

    private void increaseProgressBar(int index) {
        if (progressStatuses[index] < 100) {
            progressStatuses[index] += 10;
            progressBars[index].setProgress(progressStatuses[index]);
        }
    }

    private void setupNavigation() {
        buttonHomePage.setOnClickListener(view -> navigateTo(HomePageActivity.class));
        buttonProfilePage.setOnClickListener(view -> navigateTo(ProfileActivity.class));
        buttonWorkoutPage.setOnClickListener(view -> navigateTo(WorkoutsFilterActivity.class));
    }

    private void navigateTo(Class<?> targetActivity) {
        Intent intent = new Intent(MyPlanActivity.this, targetActivity);
        startActivity(intent);
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
        };

        for (Button dayButton : dayButtons) {
            dayButton.setOnClickListener(dayButtonClickListener);
        }
    }
}
