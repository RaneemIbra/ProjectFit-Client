package com.example.projectfit.Activities;

import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectfit.R;

public class WorkoutFilterFragment extends Fragment {

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

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    ShapeableImageView imageView;
    LinearLayout durationLayout;
    public WorkoutFilterFragment() {
        // Required empty public constructor
    }

    public static WorkoutFilterFragment newInstance(String param1, String param2) {
        WorkoutFilterFragment fragment = new WorkoutFilterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_workout_filter, container, false);
        for (int i = 0; i < imageViewIds.length; i++) {

            imageView = view.findViewById(imageViewIds[i]);
            imageView.setImageResource(imageResIds[i]);
        }
        for (int layoutId : workoutDurationLayoutIds) {
            LinearLayout durationLayout = view.findViewById(layoutId);
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

                Fragment workoutListFragment = new WorkoutsListFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("min_duration", minDuration);
                bundle.putInt("max_duration", maxDuration);
                workoutListFragment.setArguments(bundle);
                replaceFragment(workoutListFragment);
            });
        }

        for (int layoutId : workoutTypeLayoutIds) {
            LinearLayout categoryLayout = view.findViewById(layoutId);
            categoryLayout.setOnClickListener(v -> {
                String selectedCategory = getCategoryById(v.getId());
                Fragment workoutListFragment = new WorkoutsListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("workout_category", selectedCategory);
                workoutListFragment.setArguments(bundle);
                replaceFragment(workoutListFragment);
            });
        }
        for (int layoutId : difficultyLayoutIds) {
            LinearLayout difficultyLayout = view.findViewById(layoutId);
            difficultyLayout.setOnClickListener(v -> {
                int selectedDifficulty = getDifficultyById(v.getId());
                Fragment workoutListFragment = new WorkoutsListFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("difficulty_level", selectedDifficulty);
                workoutListFragment.setArguments(bundle);
                replaceFragment(workoutListFragment);
            });
        }
        for (int layoutId : muscleLayoutIds) {
            LinearLayout muscleLayout = view.findViewById(layoutId);
            muscleLayout.setOnClickListener(v -> {
                String selectedMuscle = getMuscleById(v.getId());
                Fragment workoutListFragment = new WorkoutsListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("muscle", selectedMuscle);
                workoutListFragment.setArguments(bundle);
                replaceFragment(workoutListFragment);
            });
        }
        return view;
    }

        void replaceFragment(Fragment fragment)
        {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
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