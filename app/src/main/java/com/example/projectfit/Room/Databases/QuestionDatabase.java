package com.example.projectfit.Room.Databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.projectfit.Room.DAOS.QuestionDAO;
import com.example.projectfit.Models.Question;
import com.example.projectfit.Utils.Converters;

@Database(entities = {Question.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class QuestionDatabase extends RoomDatabase {
    private static QuestionDatabase instance;

    public abstract QuestionDAO questionDAO();

    public static synchronized QuestionDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            QuestionDatabase.class, "questions_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
