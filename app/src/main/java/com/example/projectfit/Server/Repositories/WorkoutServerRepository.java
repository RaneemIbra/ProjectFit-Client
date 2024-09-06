package com.example.projectfit.Server.Repositories;

import com.example.projectfit.Models.Workout;
import com.example.projectfit.Server.API.AppAPI;
import com.example.projectfit.Server.API.WorkoutAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkoutServerRepository {
    private WorkoutAPI workoutAPI;

    public WorkoutServerRepository() {
        workoutAPI = AppAPI.getClient().create(WorkoutAPI.class);
    }

    public void addWorkoutInServer(Workout workout) {
        Call<Workout> call = workoutAPI.createWorkout(workout);
        call.enqueue(new Callback<Workout>() {
            @Override
            public void onResponse(Call<Workout> call, Response<Workout> response) {
                if (response.isSuccessful()) {
                    System.out.println("Workout submitted successfully");
                } else {
                    System.out.println("Workout wasn't submitted successfully");
                }
            }

            @Override
            public void onFailure(Call<Workout> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
            }
        });
    }

    public void getAllWorkoutsFromServer(OnWorkoutsReceivedCallback callback) {
        Call<List<Workout>> call = workoutAPI.getAllWorkouts();
        call.enqueue(new Callback<List<Workout>>() {
            @Override
            public void onResponse(Call<List<Workout>> call, Response<List<Workout>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to fetch workouts");
                }
            }

            @Override
            public void onFailure(Call<List<Workout>> call, Throwable t) {
                callback.onFailure("Error: " + t.getMessage());
            }
        });
    }

    public interface OnWorkoutsReceivedCallback {
        void onSuccess(List<Workout> workouts);
        void onFailure(String errorMessage);
    }
}
