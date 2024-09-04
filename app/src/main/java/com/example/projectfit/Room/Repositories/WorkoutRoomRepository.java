package com.example.projectfit.Room.Repositories;

import android.content.Context;

import com.example.projectfit.Models.Workout;
import com.example.projectfit.Room.Databases.WorkoutDatabase;

import java.util.concurrent.Executors;

public class WorkoutRoomRepository {
    private final WorkoutDatabase workoutDatabase;

    public WorkoutRoomRepository(Context context) {
        workoutDatabase = WorkoutDatabase.getInstance(context.getApplicationContext());
    }

    public void addWorkoutLocally(Workout workout) {
        Executors.newSingleThreadExecutor().execute(() -> {
            workoutDatabase.workoutDAO().insertWorkout(workout);
            System.out.println("is it working?");
        });
    }
}
