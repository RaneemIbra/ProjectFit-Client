package com.example.projectfit;

import java.util.List;

public class Workout {
    private Long id;
    String workoutName;
    int durationInMinutes;
    String workoutType;
    String workoutDescription;
    List<String> muscles;
    int calories;
    private byte[] workoutImage;
    private byte[] profileIcon;
    private byte[] workoutAnimation;
    List<Integer> sets_reps;
    int difficulty;

    public Workout(Long id, String workoutName, int durationInMinutes, String workoutType,
                   String workoutDescription, List<String> muscles, int calories, byte[] workoutImage,
                   byte[] profileIcon, byte[] workoutAnimation, List<Integer> sets_reps, int difficulty) {
        this.id = id;
        this.workoutName = workoutName;
        this.durationInMinutes = durationInMinutes;
        this.workoutType = workoutType;
        this.workoutDescription = workoutDescription;
        this.muscles = muscles;
        this.calories = calories;
        this.workoutImage = workoutImage;
        this.profileIcon = profileIcon;
        this.workoutAnimation = workoutAnimation;
        this.sets_reps = sets_reps;
        this.difficulty = difficulty;
    }

    public Long getId() {
        return id;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
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

    public byte[] getWorkoutImage() {
        return workoutImage;
    }

    public void setWorkoutImage(byte[] workoutImage) {
        this.workoutImage = workoutImage;
    }

    public byte[] getProfileIcon() {
        return profileIcon;
    }

    public void setProfileIcon(byte[] profileIcon) {
        this.profileIcon = profileIcon;
    }

    public byte[] getWorkoutAnimation() {
        return workoutAnimation;
    }

    public void setWorkoutAnimation(byte[] workoutAnimation) {
        this.workoutAnimation = workoutAnimation;
    }

    public List<Integer> getSets_reps() {
        return sets_reps;
    }

    public void setSets_reps(List<Integer> sets_reps) {
        this.sets_reps = sets_reps;
    }
}
