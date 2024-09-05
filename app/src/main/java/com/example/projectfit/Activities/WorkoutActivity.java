package com.example.projectfit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.projectfit.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WorkoutActivity extends AppCompatActivity {

    BottomNavigationView bottomBar;

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
        setupWindowInsets();
        setupWebView();
        setupButtonListeners();
        //setupNavigationButtons
    }


    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupWebView() {
        WebView webView = findViewById(R.id.webview);
        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/SCVCLChPQFY?si=7pouIAZ4ioc2cCan\" " +
                "title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; " +
                "picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        webView.loadData(video, "text/html", "utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
    }

    private void setupButtonListeners() {
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
                    navigateTo(WorkoutsListActivity.class);
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

    private void navigateTo(Class<?> targetActivity) {
        startActivity(new Intent(WorkoutActivity.this, targetActivity));
    }
/*
    private void setupNavigationButtons() {
        int[] buttonIds = {R.id.homePageBtn, R.id.plan, R.id.profile, R.id.workouts};
        Class<?>[] activities = {MainActivity.class, MyPlanActivity.class, ProfileActivity.class, WorkoutsFilterActivity.class};

        for (int i = 0; i < buttonIds.length; i++) {
            Button button = findViewById(buttonIds[i]);
            Class<?> targetActivity = activities[i];
            button.setOnClickListener(view -> startActivity(new Intent(WorkoutActivity.this, targetActivity)));
        }
    }
    */
}