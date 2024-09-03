package com.example.projectfit.Activities;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectfit.R;
import com.google.android.material.imageview.ShapeableImageView;

public class WorkoutsFilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workouts_filter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int[] imageViewsIds = {
                R.id.body_building_image, R.id.mobility_image, R.id.calisthenics_image,
                R.id.chest_image, R.id.back_image, R.id.legs_image,
                R.id.abs_image, R.id.core_image, R.id.biceps_image,
                R.id.shoulders_image, R.id.triceps_image, R.id.beginner_image,
                R.id.intermediate_image, R.id.advanced_image
        };

        int[] drawableResources = {
                R.drawable.bodybuilding1, R.drawable.stretching, R.drawable.strength,
                R.drawable.chest, R.drawable.back, R.drawable.quadriceps,
                R.drawable.abdominal, R.drawable.exercise, R.drawable.biceps_curl,
                R.drawable.gym, R.drawable.gym1, R.drawable.thin,
                R.drawable.abs, R.drawable.bodybuilding
        };

        for (int i = 0; i < imageViewsIds.length; i++) {
            ShapeableImageView imageView = findViewById(imageViewsIds[i]);
            imageView.setImageResource(drawableResources[i]);
        }
    }
}
