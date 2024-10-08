package com.example.projectfit.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.projectfit.Utils.Converters;

import java.util.List;

@Entity(tableName = "workouts")
public class Workout {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    String workoutName;
    int durationInMinutes;
    String workoutType;
    String workoutDescription;
    @TypeConverters(Converters.class)
    private List<String> muscles;
    int calories;
    int workoutImageResId;
    int workoutLogoResId;
    int workoutGifResId;
    List<Integer> sets_reps;
    int difficulty;
    int sets;

    public Workout(){

    }

    public Workout(String workoutName, int durationInMinutes, String workoutType, String workoutDescription,
                   List<String> muscles, int calories, int workoutImageResId,
                    List<Integer> sets_reps, int difficulty, int workoutLogoResId, int workoutGifResId, int sets) {
        this.workoutName = workoutName;
        this.durationInMinutes = durationInMinutes;
        this.workoutType = workoutType;
        this.workoutDescription = workoutDescription;
        this.muscles = muscles;
        this.calories = calories;
        this.workoutImageResId = workoutImageResId;
        this.sets_reps = sets_reps;
        this.difficulty = difficulty;
        this.workoutLogoResId = workoutLogoResId;
        this.workoutGifResId = workoutGifResId;
        this.sets = sets;
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

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getWorkoutGifResId() {
        return workoutGifResId;
    }

    public void setWorkoutGifResId(int workoutGifResId) {
        this.workoutGifResId = workoutGifResId;
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

    public int getWorkoutLogoResId() {
        return workoutLogoResId;
    }

    public void setWorkoutLogoResId(int workoutLogoResId) {
        this.workoutLogoResId = workoutLogoResId;
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

    public int getWorkoutImageResId() {
        return workoutImageResId;
    }

    public void setWorkoutImageResId(int workoutImageResId) {
        this.workoutImageResId = workoutImageResId;
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
