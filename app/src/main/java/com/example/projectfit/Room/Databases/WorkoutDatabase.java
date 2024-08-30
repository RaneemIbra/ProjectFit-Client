package com.example.projectfit.Room.Databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.projectfit.Room.DAOS.WorkoutDAO;
import com.example.projectfit.Room.Entities.Workout;
import com.example.projectfit.Utils.Converters;

@Database(entities = {Workout.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class WorkoutDatabase extends RoomDatabase {
    private static WorkoutDatabase instance;
    public abstract WorkoutDAO workoutDAO();

    public static synchronized WorkoutDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            WorkoutDatabase.class, "workouts_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
