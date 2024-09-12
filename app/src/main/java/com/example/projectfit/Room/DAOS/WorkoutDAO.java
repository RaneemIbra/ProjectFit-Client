package com.example.projectfit.Room.DAOS;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.projectfit.Models.Workout;

import java.util.List;

@Dao
public interface WorkoutDAO {
    @Insert
    void insertWorkout(Workout workout);

    @Update
    void updateWorkout(Workout workout);

    @Query("SELECT * FROM workouts")
    LiveData<List<Workout>> getAllWorkouts();

    @Query("SELECT * FROM workouts WHERE workoutName = :workoutName LIMIT 1")
    Workout getWorkoutByName(String workoutName);

    @Query("SELECT * FROM workouts WHERE workoutType = :type")
    LiveData<List<Workout>> getWorkoutsByType(String type);

    @Query("SELECT * FROM workouts WHERE durationInMinutes = :duration")
    LiveData<List<Workout>> getWorkoutsByDuration(int duration);

    @Query("SELECT * FROM workouts WHERE difficulty = :difficulty")
    LiveData<List<Workout>> getWorkoutsByDifficulty(int difficulty);

    @Query("SELECT * FROM workouts WHERE muscles LIKE '%' || :muscle || '%'")
    LiveData<List<Workout>> getWorkoutsByMuscle(String muscle);

    @Query("SELECT * FROM workouts WHERE durationInMinutes BETWEEN :minDuration AND :maxDuration")
    LiveData<List<Workout>> getWorkoutsByDurationRange(int minDuration, int maxDuration);

}
