package com.example.projectfit.Activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.projectfit.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigate extends AppCompatActivity {

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
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        // set home page as a default page
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomePageFragment())
                    .commit();
        }

        // navigate to different pages
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id= item.getItemId();
            if( id == R.id.home_BottomIcon) {
                selectedFragment = new HomePageFragment();}
            if( id == R.id.profile_BottomIcon) {
                selectedFragment = new ProfileFragment();
            }
            if( id == R.id.plan_BottomIcon) {
                selectedFragment = new PlanFragment();
            }
            if( id == R.id.workouts_BottomIcon) {
                selectedFragment = new WorkoutFilterFragment();
            }

            // Replace fragment
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}