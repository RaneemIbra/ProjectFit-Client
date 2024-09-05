package com.example.projectfit.Server.Repositories;

import com.example.projectfit.Models.Workout;
import com.example.projectfit.Server.API.AppAPI;
import com.example.projectfit.Server.API.WorkoutAPI;

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
}
