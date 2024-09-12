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
}
