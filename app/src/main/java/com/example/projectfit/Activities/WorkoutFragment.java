package com.example.projectfit.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectfit.R;

public class WorkoutFragment extends Fragment {
    Bundle bundleArg;
    ImageView workoutGifImageView,workoutImageView;
    TextView workoutDescriptionTextView,workoutNameTextView;
    ImageButton backButton;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public WorkoutFragment() {
        // Required empty public constructor
    }

    public static WorkoutFragment newInstance(String param1, String param2) {
        WorkoutFragment fragment = new WorkoutFragment();
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
         View view = inflater.inflate(R.layout.fragment_workout, container, false);
         bundleArg = getArguments();
         workoutNameTextView = view.findViewById(R.id.WorkoutTitle);
         workoutDescriptionTextView = view.findViewById(R.id.WorkoutDescription);
         workoutImageView = view.findViewById(R.id.WorkoutImage);
         workoutGifImageView = view.findViewById(R.id.workout_gif);
         backButton = view.findViewById(R.id.back_button_workout);

         backButton.setOnClickListener(view1 -> {
             onBackClicked(view1);
         });

        loadWorkoutDetails();
        return  view;
    }
    private void loadWorkoutDetails() {
        Intent intent = getActivity().getIntent();
        String workoutName = bundleArg.getString("workout_name");
        String workoutDescription = bundleArg.getString("workout_description");
        int workoutImageResId = bundleArg.getInt("workout_image_res_id", R.drawable.img);
        int workoutGifResId = bundleArg.getInt("workout_gif_res_id", -1);
        workoutNameTextView.setText(workoutName);
        workoutDescriptionTextView.setText(workoutDescription);
        Glide.with(this).load(workoutImageResId).into(workoutImageView);
        Glide.with(this).asGif().load(workoutGifResId).into(workoutGifImageView);
    }

    public void onBackClicked(View view) {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

}