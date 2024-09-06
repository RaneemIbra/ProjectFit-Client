package com.example.projectfit.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "workouts")
public class Workout {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    String workoutName;
    int durationInMinutes;
    String workoutType;
    String workoutDescription;
    List<String> muscles;
    int calories;
    private String workoutImageBase64;
    private String workoutIconBase64;
    private String workoutAnimationBase64;
    List<Integer> sets_reps;
    int difficulty;

    public Workout(){

    }

    public Workout(String workoutName, int durationInMinutes, String workoutType, String workoutDescription,
                   List<String> muscles, int calories, String workoutImage, String workoutIconBase64,
                   String workoutAnimation, List<Integer> sets_reps, int difficulty) {
        this.workoutName = workoutName;
        this.durationInMinutes = durationInMinutes;
        this.workoutType = workoutType;
        this.workoutDescription = workoutDescription;
        this.muscles = muscles;
        this.calories = calories;
        this.workoutImageBase64 = workoutImage;
        this.workoutIconBase64 = workoutIconBase64;
        this.workoutAnimationBase64 = workoutAnimation;
        this.sets_reps = sets_reps;
        this.difficulty = difficulty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public String getWorkoutDescription() {
        return workoutDescription;
    }

    public void setWorkoutDescription(String workoutDescription) {
        this.workoutDescription = workoutDescription;
    }

    public List<String> getMuscles() {
        return muscles;
    }

    public void setMuscles(List<String> muscles) {
        this.muscles = muscles;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getWorkoutImageBase64() {
        return workoutImageBase64;
    }

    public void setWorkoutImageBase64(String workoutImageBase64) {
        this.workoutImageBase64 = workoutImageBase64;
    }

    public String getWorkoutIconBase64() {
        return workoutIconBase64;
    }

    public void setWorkoutIconBase64(String workoutIconBase64) {
        this.workoutIconBase64 = workoutIconBase64;
    }

    public String getWorkoutAnimationBase64() {
        return workoutAnimationBase64;
    }

    public void setWorkoutAnimationBase64(String workoutAnimationBase64) {
        this.workoutAnimationBase64 = workoutAnimationBase64;
    }

    public List<Integer> getSets_reps() {
        return sets_reps;
    }

    public void setSets_reps(List<Integer> sets_reps) {
        this.sets_reps = sets_reps;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
