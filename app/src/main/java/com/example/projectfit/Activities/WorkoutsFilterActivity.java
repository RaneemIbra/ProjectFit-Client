package com.example.projectfit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectfit.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

public class WorkoutsFilterActivity extends AppCompatActivity {

    private final int[] categoryLayoutIds = {
            R.id.body_part_chest, R.id.body_part_back, R.id.body_part_legs, R.id.body_part_abs,
            R.id.body_part_core, R.id.body_part_biceps, R.id.body_part_shoulders, R.id.body_part_triceps,
            R.id.beginner_level, R.id.intermediate_level, R.id.advanced_level,
            R.id.r7kmbxwvlz6o, R.id.rcdhqfptnugj, R.id.r17jd3ea8ta1,
            R.id.rvg9e9e3fjjq, R.id.ro2x0y9luguo, R.id.rzxm2hmf0u7
    };

    private final int[] imageViewIds = {
            R.id.body_building_image, R.id.mobility_image, R.id.calisthenics_image, R.id.chest_image,
            R.id.back_image, R.id.legs_image, R.id.abs_image, R.id.core_image,
            R.id.biceps_image, R.id.shoulders_image, R.id.triceps_image,
            R.id.beginner_image, R.id.intermediate_image, R.id.advanced_image
    };

    private final int[] imageResIds = {
            R.drawable.bodybuilding1, R.drawable.stretching, R.drawable.strength, R.drawable.chest,
            R.drawable.back, R.drawable.quadriceps, R.drawable.abdominal, R.drawable.exercise,
            R.drawable.biceps_curl, R.drawable.gym, R.drawable.gym1,
            R.drawable.thin, R.drawable.abs, R.drawable.bodybuilding
    };

    private BottomNavigationView bottomBar;

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

        bottomBar = findViewById(R.id.bottom_navigation);
        setupBottomNavigation();
        setupCategoryImages();
        setupCategoryClickListeners();
    }

    private void setupBottomNavigation() {
        bottomBar.setOnNavigationItemSelectedListener(item -> {
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
                navigateTo(WorkoutsFilterActivity.class);
                return true;
            }
            else if ( id_item==R.id.profile_BottomIcon)
            {
                navigateTo(ProfileActivity.class);
                return true;
            }
            else
                return false;
        });
    }

    private void navigateTo(Class<?> targetActivity) {
        startActivity(new Intent(WorkoutsFilterActivity.this, targetActivity));
    }

    private void setupCategoryImages() {
        for (int i = 0; i < imageViewIds.length; i++) {
            ShapeableImageView imageView = findViewById(imageViewIds[i]);
            imageView.setImageResource(imageResIds[i]);
        }
    }

    private void setupCategoryClickListeners() {
        for (int layoutId : categoryLayoutIds) {
            LinearLayout categoryLayout = findViewById(layoutId);
            categoryLayout.setOnClickListener(v -> {
                Intent intent = new Intent(WorkoutsFilterActivity.this, WorkoutsListActivity.class);
                intent.putExtra("selected_category", v.getId());
                startActivity(intent);
            });
        }
    }
}
