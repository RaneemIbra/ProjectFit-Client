package com.example.projectfit.Activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.LinearLayout;
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
        // Set images for each category
        ShapeableImageView imageView = findViewById(R.id.body_building_image);
        imageView.setImageResource(R.drawable.bodybuilding1);
        ShapeableImageView imageView2 = findViewById(R.id.mobility_image);
        imageView2.setImageResource(R.drawable.stretching);
        ShapeableImageView imageView3 = findViewById(R.id.calisthenics_image);
        imageView3.setImageResource(R.drawable.strength);
        ShapeableImageView imageView5 = findViewById(R.id.chest_image);
        imageView5.setImageResource(R.drawable.chest);
        ShapeableImageView imageView6 = findViewById(R.id.back_image);
        imageView6.setImageResource(R.drawable.back);
        ShapeableImageView imageView7 = findViewById(R.id.legs_image);
        imageView7.setImageResource(R.drawable.quadriceps);
        ShapeableImageView imageView8 = findViewById(R.id.abs_image);
        imageView8.setImageResource(R.drawable.abdominal);
        ShapeableImageView imageView9 = findViewById(R.id.core_image);
        imageView9.setImageResource(R.drawable.exercise);
        ShapeableImageView imageView10 = findViewById(R.id.biceps_image);
        imageView10.setImageResource(R.drawable.biceps_curl);
        ShapeableImageView imageView11 = findViewById(R.id.shoulders_image);
        imageView11.setImageResource(R.drawable.gym);
        ShapeableImageView imageView12 = findViewById(R.id.triceps_image);
        imageView12.setImageResource(R.drawable.gym1);
        ShapeableImageView imageView13 = findViewById(R.id.beginner_image);
        imageView13.setImageResource(R.drawable.thin);
        ShapeableImageView imageView14 = findViewById(R.id.intermediate_image);
        imageView14.setImageResource(R.drawable.abs);
        ShapeableImageView imageView15 = findViewById(R.id.advanced_image);
        imageView15.setImageResource(R.drawable.bodybuilding);

        // Set onClickListeners for each category LinearLayout
        setCategoryClickListener(R.id.body_part_chest);
        setCategoryClickListener(R.id.body_part_back);
        setCategoryClickListener(R.id.body_part_legs);
        setCategoryClickListener(R.id.body_part_abs);
        setCategoryClickListener(R.id.body_part_core);
        setCategoryClickListener(R.id.body_part_biceps);
        setCategoryClickListener(R.id.body_part_shoulders);
        setCategoryClickListener(R.id.body_part_triceps);
        setCategoryClickListener(R.id.beginner_level);
        setCategoryClickListener(R.id.intermediate_level);
        setCategoryClickListener(R.id.advanced_level);
        setCategoryClickListener(R.id.r7kmbxwvlz6o); // Body Building
        setCategoryClickListener(R.id.rcdhqfptnugj); // Mobility
        setCategoryClickListener(R.id.r17jd3ea8ta1); // Calisthenics
        setCategoryClickListener(R.id.rvg9e9e3fjjq); // 30 Minutes
        setCategoryClickListener(R.id.ro2x0y9luguo); // 60 Minutes
        setCategoryClickListener(R.id.rzxm2hmf0u7); // 90 Minutes
    }

    // Method to set OnClickListener for each category
    private void setCategoryClickListener(int layoutId) {
        LinearLayout categoryLayout = findViewById(layoutId);
        categoryLayout.setOnClickListener(v -> {
            // Navigate to WorkoutsListActivity when clicked
            Intent intent = new Intent(WorkoutsFilterActivity.this, WorkoutsListActivity.class);
            // Optionally pass extra data about the selected category
            intent.putExtra("selected_category", v.getId());
            startActivity(intent);
        });
    }
}
