package com.example.projectfit.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.projectfit.Models.Workout;
import com.example.projectfit.R;

import java.util.List;

public class WorkoutAdapterForMyplan extends BaseAdapter {
    private final Context context;
    private final List<Workout> workouts;
    private final LayoutInflater inflater;

    public WorkoutAdapterForMyplan(Context context, List<Workout> workouts) {
        this.context = context;
        this.workouts = workouts;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return workouts.size();
    }

    @Override
    public Object getItem(int position) {
        return workouts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return workouts.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.workout_item_for_myplan, parent, false);
            holder = new ViewHolder();
            holder.workoutName = convertView.findViewById(R.id.workout_name);
            holder.workoutMuscles = convertView.findViewById(R.id.workout_muscles);
            holder.workoutCalories = convertView.findViewById(R.id.workout_calories);
            holder.workoutSetsReps = convertView.findViewById(R.id.workout_sets_reps);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Workout workout = workouts.get(position);
        holder.workoutName.setText(workout.getWorkoutName());
        holder.workoutMuscles.setText("Muscles: " + String.join(", ", workout.getMuscles()));
        holder.workoutCalories.setText("Calories: " + workout.getCalories() + " Kcal");
        holder.workoutSetsReps.setText("Sets/Reps: " + workout.getSets_reps().toString());

        return convertView;
    }

    public void removeItem(int position) {
        if (position >= 0 && position < workouts.size()) {
            workouts.remove(position);  // Remove from data source
            notifyDataSetChanged();  // Notify adapter of changes
        }
    }

    static class ViewHolder {
        TextView workoutName;
        TextView workoutMuscles;
        TextView workoutCalories;
        TextView workoutSetsReps;
    }
}