package com.example.projectfit.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectfit.Models.Workout;
import com.example.projectfit.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private final List<Workout> workouts = new ArrayList<>();
    private final OnWorkoutClickListener listener;

    public interface OnWorkoutClickListener {
        void onWorkoutSelected(Workout workout);
    }

    public WorkoutAdapter(OnWorkoutClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        Workout workout = workouts.get(position);

        holder.workoutName.setText(workout.getWorkoutName());
        holder.workoutDuration.setText(workout.getDurationInMinutes() + " Minutes");
        holder.workoutCalories.setText(workout.getCalories() + " Kcal");

        Glide.with(holder.itemView.getContext()).load(workout.getWorkoutLogoResId()).into(holder.workoutLogo);

        holder.itemView.setOnClickListener(v -> listener.onWorkoutSelected(workout));
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public void setWorkouts(List<Workout> newWorkouts) {
        workouts.clear();
        workouts.addAll(newWorkouts);
        notifyDataSetChanged();
    }

    static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView workoutName, workoutDuration, workoutCalories;
        ShapeableImageView workoutLogo;

        WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutName = itemView.findViewById(R.id.workoutName);
            workoutDuration = itemView.findViewById(R.id.workoutDuration);
            workoutCalories = itemView.findViewById(R.id.workoutCalories);
            workoutLogo = itemView.findViewById(R.id.workoutLogo);
        }
    }
}
