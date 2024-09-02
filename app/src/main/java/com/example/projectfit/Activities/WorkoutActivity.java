package com.example.projectfit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.projectfit.R;

public class WorkoutActivity extends AppCompatActivity {

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
        WebView webView = findViewById(R.id.webview);
        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/SCVCLChPQFY?si=7pouIAZ4ioc2cCan\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        webView.loadData(video, "text/html", "utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        Button homePageBtn = findViewById(R.id.homePageBtn);
        homePageBtn.setOnClickListener(view -> {
            Intent intent = new Intent(WorkoutActivity.this, MainActivity.class);
            startActivity(intent);
        });
        Button planBtn = findViewById(R.id.plan);
        planBtn.setOnClickListener(view -> {
            Intent intent = new Intent(WorkoutActivity.this, MyPlanActivity.class);
            startActivity(intent);
        });
        Button profileBtn = findViewById(R.id.profile);
        profileBtn.setOnClickListener(view -> {
            Intent intent = new Intent(WorkoutActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
        Button workoutsBtn = findViewById(R.id.workouts);
        workoutsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(WorkoutActivity.this, WorkoutsFilterActivity.class);
            startActivity(intent);
        });
    }
}