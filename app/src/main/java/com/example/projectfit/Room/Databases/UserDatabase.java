package com.example.projectfit.Room.Databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.projectfit.Room.DAOS.UserDAO;
import com.example.projectfit.Models.User;
import com.example.projectfit.Utils.Converters;

@Database(entities = {User.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class UserDatabase extends RoomDatabase {
    private static UserDatabase instance;

    public abstract UserDAO userDAO();

    public static synchronized UserDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            UserDatabase.class, "users_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
