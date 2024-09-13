package com.example.projectfit.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.projectfit.Models.User;
import com.example.projectfit.R;
import com.example.projectfit.Utils.GsonProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class BottomNavigate extends AppCompatActivity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bottom_navigate);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("logged_in_user", null);
        if (userJson != null) {
            Gson gson = GsonProvider.getGson();
            Type userType = new TypeToken<User>() {}.getType();
            user = gson.fromJson(userJson, userType);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        String navigateTo = getIntent().getStringExtra("navigateTo");

        if (navigateTo != null && navigateTo.equals("plan")) {
            navigateToPlan();
        } else {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomePageFragment())
                        .commit();
            }
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();
            if (id == R.id.home_BottomIcon) {
                selectedFragment = new HomePageFragment();
            } else if (id == R.id.profile_BottomIcon) {
                selectedFragment = new ProfileFragment();
            } else if (id == R.id.plan_BottomIcon) {
                navigateToPlan();  // Add navigation check for PlanFragment
            } else if (id == R.id.workouts_BottomIcon) {
                selectedFragment = new WorkoutFilterFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }

    private void navigateToPlan() {
        if (user != null && user.isBuildPlan()) {
            startActivity(new Intent(BottomNavigate.this, PlanQuestionsActivity.class));
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new PlanFragment())
                    .commit();
        }
    }
}
