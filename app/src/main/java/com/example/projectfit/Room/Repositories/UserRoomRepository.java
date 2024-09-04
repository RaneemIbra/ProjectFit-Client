package com.example.projectfit.Room.Repositories;

import android.content.Context;

import com.example.projectfit.Room.Databases.UserDatabase;
import com.example.projectfit.Models.User;

import java.util.List;
import java.util.concurrent.Executors;
import androidx.lifecycle.LiveData;

public class UserRoomRepository {
    private final UserDatabase userDatabase;

    public UserRoomRepository(Context context) {
        this.userDatabase = UserDatabase.getInstance(context.getApplicationContext());
    }

    public void addUserLocally(User user) {
        Executors.newSingleThreadExecutor().execute(() -> {
            userDatabase.userDAO().insertUser(user);
        });
    }

    public LiveData<List<User>> getAllUsersLocally() {
        return userDatabase.userDAO().getAllUsers();
    }

    public User getUserByEmail(String email) {
        return userDatabase.userDAO().getUserByEmail(email);
    }
}
