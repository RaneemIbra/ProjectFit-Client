package com.example.projectfit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectfit.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

public class WorkoutsFilterActivity extends AppCompatActivity {
    private final int[] workoutDurationLayoutIds = {
            R.id.minutes15layout, R.id.minute30layout, R.id.minute60layout
    };

    private final int[] workoutTypeLayoutIds = {
            R.id.BodyBuildingLayout, R.id.MobilityLayout, R.id.CalisthenicsLayout
    };

    private final int[] difficultyLayoutIds = {
            R.id.beginner_level, R.id.intermediate_level, R.id.advanced_level
    };

    private final int[] muscleLayoutIds = {
            R.id.body_part_chest, R.id.body_part_back, R.id.body_part_legs, R.id.body_part_abs,
            R.id.body_part_core, R.id.body_part_biceps, R.id.body_part_shoulders, R.id.body_part_triceps
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
        setupDifficultyClickListeners();
        setupMuscleClickListeners();
        setupDurationClickListeners();
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

    private void setupDurationClickListeners() {
        for (int layoutId : workoutDurationLayoutIds) {
            LinearLayout durationLayout = findViewById(layoutId);
            durationLayout.setOnClickListener(v -> {
                int minDuration = 0;
                int maxDuration = 15;

                if (v.getId() == R.id.minute30layout) {
                    minDuration = 15;
                    maxDuration = 30;
                } else if (v.getId() == R.id.minute60layout) {
                    minDuration = 30;
                    maxDuration = 60;
                }

                Intent intent = new Intent(WorkoutsFilterActivity.this, WorkoutsListActivity.class);
                intent.putExtra("min_duration", minDuration);
                intent.putExtra("max_duration", maxDuration);
                startActivity(intent);
            });
        }
    }

    private void setupCategoryClickListeners() {
        for (int layoutId : workoutTypeLayoutIds) {
            LinearLayout categoryLayout = findViewById(layoutId);
            categoryLayout.setOnClickListener(v -> {
                String selectedCategory = getCategoryById(v.getId());
                Intent intent = new Intent(WorkoutsFilterActivity.this, WorkoutsListActivity.class);
                intent.putExtra("workout_category", selectedCategory); // Pass selected category
                startActivity(intent);
            });
        }
    }

    private void setupDifficultyClickListeners() {
        for (int layoutId : difficultyLayoutIds) {
            LinearLayout difficultyLayout = findViewById(layoutId);
            difficultyLayout.setOnClickListener(v -> {
                int selectedDifficulty = getDifficultyById(v.getId());
                Intent intent = new Intent(WorkoutsFilterActivity.this, WorkoutsListActivity.class);
                intent.putExtra("difficulty_level", selectedDifficulty);
                startActivity(intent);
            });
        }
    }

    private void setupMuscleClickListeners() {
        for (int layoutId : muscleLayoutIds) {
            LinearLayout muscleLayout = findViewById(layoutId);
            muscleLayout.setOnClickListener(v -> {
                String selectedMuscle = getMuscleById(v.getId());
                Intent intent = new Intent(WorkoutsFilterActivity.this, WorkoutsListActivity.class);
                intent.putExtra("muscle", selectedMuscle);
                startActivity(intent);
            });
        }
    }

    private int getDifficultyById(int difficultyId) {
        if (difficultyId == R.id.beginner_level) {
            return 1;
        } else if (difficultyId == R.id.intermediate_level) {
            return 2;
        } else if (difficultyId == R.id.advanced_level) {
            return 3;
        } else {
            return -1;
        }
    }

    private String getCategoryById(int categoryId) {
        if (categoryId == R.id.BodyBuildingLayout) {
            return "Bodybuilding";
        } else if (categoryId == R.id.MobilityLayout) {
            return "Mobility";
        } else if (categoryId == R.id.CalisthenicsLayout) {
            return "Calisthenics";
        } else {
            return null;
        }
    }

    private String getMuscleById(int muscleId) {
        if (muscleId == R.id.body_part_chest) {
            return "Chest";
        } else if (muscleId == R.id.body_part_back) {
            return "Back";
        } else if (muscleId == R.id.body_part_legs) {
            return "Legs";
        } else if (muscleId == R.id.body_part_abs) {
            return "Abs";
        } else if (muscleId == R.id.body_part_core) {
            return "Core";
        } else if (muscleId == R.id.body_part_biceps) {
            return "Biceps";
        } else if (muscleId == R.id.body_part_shoulders) {
            return "Shoulders";
        } else if (muscleId == R.id.body_part_triceps) {
            return "Triceps";
        } else {
            return null;
        }
    }
}
