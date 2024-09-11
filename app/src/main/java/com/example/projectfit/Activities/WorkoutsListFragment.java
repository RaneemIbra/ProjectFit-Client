package com.example.projectfit.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.projectfit.Models.Workout;
import com.example.projectfit.R;
import com.example.projectfit.Room.Repositories.WorkoutRoomRepository;
import com.example.projectfit.Utils.WorkoutAdapter;

public class WorkoutsListFragment extends Fragment {
    Bundle bundleArguments = getArguments();
    private WorkoutRoomRepository workoutRoomRepository;
    private RecyclerView workoutRecyclerView;
    private WorkoutAdapter workoutAdapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public WorkoutsListFragment() {
        // Required empty public constructor
    }
    public static WorkoutsListFragment newInstance(String param1, String param2) {
        WorkoutsListFragment fragment = new WorkoutsListFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workouts_list, container, false);
        workoutRecyclerView = view.findViewById(R.id.workoutRecyclerView);
        workoutRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        workoutAdapter = new WorkoutAdapter(this::onWorkoutSelected);
        workoutRecyclerView.setAdapter(workoutAdapter);
        initRepositories();
        observeFilteredWorkouts();
        return view;

    }

    private void initRepositories() {
        workoutRoomRepository = new WorkoutRoomRepository(getContext());
    }

    private void observeFilteredWorkouts() {
        String workoutCategory = bundleArguments.getString("workout_category", null);
        int difficultyLevel = bundleArguments.getInt("difficulty_level", -1);
        String muscle = bundleArguments.getString("muscle",null);
        int minDuration = bundleArguments.getInt("min_duration", -1);
        int maxDuration = bundleArguments.getInt("max_duration", -1);

        if (workoutCategory != null) {
            workoutRoomRepository.getWorkoutsByType(workoutCategory).observe(getViewLifecycleOwner(), workouts -> {
                if (workouts != null && !workouts.isEmpty()) {
                    workoutAdapter.setWorkouts(workouts);
                } else {
                    Toast.makeText(getContext(), "No workouts available for the selected category", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (difficultyLevel != -1) {
            workoutRoomRepository.getWorkoutsByDifficulty(difficultyLevel).observe(getViewLifecycleOwner(), workouts -> {
                if (workouts != null && !workouts.isEmpty()) {
                    workoutAdapter.setWorkouts(workouts);
                } else {
                    Toast.makeText(getContext(), "No workouts available for the selected difficulty", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (muscle != null) {
            workoutRoomRepository.getWorkoutsByMuscle(muscle).observe(getViewLifecycleOwner(), workouts -> {
                if (workouts != null && !workouts.isEmpty()) {
                    workoutAdapter.setWorkouts(workouts);
                } else {
                    Toast.makeText(getContext(), "No workouts available for the selected muscle", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (minDuration != -1 && maxDuration != -1) {
            workoutRoomRepository.getWorkoutsByDurationRange(minDuration, maxDuration).observe(getViewLifecycleOwner(), workouts -> {
                if (workouts != null && !workouts.isEmpty()) {
                    workoutAdapter.setWorkouts(workouts);
                } else {
                    Toast.makeText(getContext(), "No workouts available for the selected duration", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            workoutRoomRepository.getAllWorkoutsLocally().observe(getViewLifecycleOwner(), workouts -> {
                if (workouts != null && !workouts.isEmpty()) {
                    workoutAdapter.setWorkouts(workouts);
                } else {
                    Toast.makeText(getContext(), "No workouts available", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void onWorkoutSelected(Workout workout) {

        // Create an instance of the target fragment
        Fragment workoutFragment = new WorkoutFragment();

        // Create a bundle to pass data
        Bundle bundle = new Bundle();
        bundle.putString("workout_name", workout.getWorkoutName());
        bundle.putInt("workout_duration", workout.getDurationInMinutes());
        bundle.putInt("workout_calories", workout.getCalories());
        bundle.putString("workout_description", workout.getWorkoutDescription());
        bundle.putInt("workout_image_res_id", workout.getWorkoutImageResId());
        bundle.putInt("workout_gif_res_id", workout.getWorkoutGifResId());

        // Set the bundle as arguments for the new fragment
        workoutFragment.setArguments(bundle);

        // Perform the fragment transaction
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, workoutFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}