package com.example.projectfit.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
        setupWindowInsets();
        setupWebView();
        setupNavigationButtons();
        loadWorkoutDetails();
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

    private void setupNavigationButtons() {
        int[] buttonIds = {R.id.homePageBtn, R.id.plan, R.id.profile, R.id.workouts};
        Class<?>[] activities = {MainActivity.class, MyPlanActivity.class, ProfileActivity.class, WorkoutsFilterActivity.class};

        for (int i = 0; i < buttonIds.length; i++) {
            Button button = findViewById(buttonIds[i]);
            Class<?> targetActivity = activities[i];
            button.setOnClickListener(view -> startActivity(new Intent(WorkoutActivity.this, targetActivity)));
        }
    }

    private void loadWorkoutDetails(){
        Intent intent = getIntent();
        String workoutName = intent.getStringExtra("workout_name");
        String workoutDescription = intent.getStringExtra("workout_description");
        String workoutImageBase64 = intent.getStringExtra("workout_image_base64");
        TextView workoutNameTextView = findViewById(R.id.WorkoutTitle);
        TextView workoutDescriptionTextView = findViewById(R.id.WorkoutDescription);
        ImageView workoutImageView = findViewById(R.id.WorkoutImage);

        workoutNameTextView.setText(workoutName);
        workoutDescriptionTextView.setText(workoutDescription);
        if (workoutImageBase64 != null && !workoutImageBase64.isEmpty()) {
            byte[] imageBytes = Base64.decode(workoutImageBase64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            workoutImageView.setImageBitmap(bitmap);
        } else {
            workoutImageView.setImageResource(R.drawable.img);
        }
    }
}