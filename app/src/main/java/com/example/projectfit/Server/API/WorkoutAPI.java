package com.example.projectfit.Server.API;

import com.example.projectfit.Server.Models.Workout;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WorkoutAPI {
    @GET("workout")
    Call<List<Workout>> getAllWorkouts();

    @POST("workout")
    Call<Workout> createWorkout(@Body Workout workout);

    @GET("workout/search")
    Call<List<Workout>> findWorkoutByType(@Query("type") String type, @Query("id") Long id);
    @GET("workout/search")
    Call<List<Workout>> findWorkoutByDuration(@Query("duration") int duration, @Query("id") Long id);
    @GET("workout/search")
    Call<List<Workout>> findWorkoutByMuscle(@Query("muscles") List<String> muscles, @Query("id") Long id);
    @GET("workout/search")
    Call<List<Workout>> findWorkoutByDifficulty(@Query("difficulty") int difficulty, @Query("id") Long id);
}
