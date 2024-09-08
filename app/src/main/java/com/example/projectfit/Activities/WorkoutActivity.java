package com.example.projectfit.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

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

    private BottomNavigationView bottomBar;
    private WebView webView;

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
        webView = findViewById(R.id.webview);

        setupWebView();
        loadWorkoutDetails();
        setupBottomNavigation();
    }

    private void setupWebView() {
        String videoUrl = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/SCVCLChPQFY?si=7pouIAZ4ioc2cCan\" " +
                "title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; " +
                "picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadData(videoUrl, "text/html", "utf-8");
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

    private void loadWorkoutDetails(){
        Intent intent = getIntent();
        String workoutName = intent.getStringExtra("workout_name");
        String workoutDescription = intent.getStringExtra("workout_description");
        int workoutImageResId = intent.getIntExtra("workout_image_res_id", R.drawable.img);

        TextView workoutNameTextView = findViewById(R.id.WorkoutTitle);
        TextView workoutDescriptionTextView = findViewById(R.id.WorkoutDescription);
        ImageView workoutImageView = findViewById(R.id.WorkoutImage);

        workoutNameTextView.setText(workoutName);
        workoutDescriptionTextView.setText(workoutDescription);

        Glide.with(this).load(workoutImageResId).into(workoutImageView);
    }


    private Bitmap decodeSampledBitmapFromBase64(String base64Image) {
        byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);
        options.inSampleSize = calculateInSampleSize(options, 100, 100);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public void onBackClicked(View view) {
        navigateTo(WorkoutsListActivity.class);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
        }
    }
}
