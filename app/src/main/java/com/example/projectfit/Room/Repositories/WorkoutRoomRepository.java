package com.example.projectfit.Room.Repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.projectfit.Models.Workout;
import com.example.projectfit.Room.DAOS.WorkoutDAO;
import com.example.projectfit.Room.Databases.WorkoutDatabase;

import java.util.List;
import java.util.concurrent.Executors;

public class WorkoutRoomRepository {
    private final WorkoutDatabase workoutDatabase;

    public WorkoutRoomRepository(Context context) {
        workoutDatabase = WorkoutDatabase.getInstance(context.getApplicationContext());
    }

    public void addWorkoutLocally(Workout workout) {
        Executors.newSingleThreadExecutor().execute(() -> {
            workoutDatabase.workoutDAO().insertWorkout(workout);
        });
    }

    public LiveData<List<Workout>> getAllWorkoutsLocally() {
        return workoutDatabase.workoutDAO().getAllWorkouts();
    }

    public Workout getWorkoutByName(String workoutName) {
        return workoutDatabase.workoutDAO().getWorkoutByName(workoutName);
    }

    public void updateWorkout(Workout workout) {
        Executors.newSingleThreadExecutor().execute(() -> {
            workoutDatabase.workoutDAO().updateWorkout(workout);
        });
    }
    public LiveData<List<Workout>> getWorkoutsByDifficulty(int difficulty) {
        return workoutDatabase.workoutDAO().getWorkoutsByDifficulty(difficulty);
    }

    public LiveData<List<Workout>> getWorkoutsByType(String type) {
        return workoutDatabase.workoutDAO().getWorkoutsByType(type);
    }

    public LiveData<List<Workout>> getWorkoutsByMuscle(String muscle) {
        return workoutDatabase.workoutDAO().getWorkoutsByMuscle(muscle);
    }

    public LiveData<List<Workout>> getWorkoutsByDurationRange(int minDuration, int maxDuration) {
        return workoutDatabase.workoutDAO().getWorkoutsByDurationRange(minDuration, maxDuration);

    }
}
