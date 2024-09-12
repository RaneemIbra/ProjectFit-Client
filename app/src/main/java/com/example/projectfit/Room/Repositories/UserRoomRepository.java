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

    public void updateStepsHistory(User user) {
        Executors.newSingleThreadExecutor().execute(() -> {
            userDatabase.userDAO().updateUser(user);
        });
    }

    public void updateWaterHistory(User user) {
        Executors.newSingleThreadExecutor().execute(() -> {
            userDatabase.userDAO().updateUser(user);
        });
    }

    public List<User> getAllUsersLocally() {
        return userDatabase.userDAO().getAllUsers();
    }

    public User getUserByEmail(String email) {
        return userDatabase.userDAO().getUserByEmail(email);
    }

    public void validateUserLocal(String email, String password, OnUserValidationCallback callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            User localUser = userDatabase.userDAO().getUserByEmail(email);
            if (localUser != null && localUser.getPassword().equals(password)) {
                callback.onSuccess(localUser);
            } else if (localUser != null) {
                callback.onFailure("Incorrect password");
            } else {
                callback.onFailure("User not found locally");
            }
        });
    }

    public void validateUserLocalByAnswer(String email, String answer, OnUserValidationCallback callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            User localUser = userDatabase.userDAO().getUserByEmailAndAnswer(email, answer);
            if (localUser != null) {
                callback.onSuccess(localUser);
            } else {
                callback.onFailure("Incorrect email or security answer");
            }
        });
    }

    public void updateUserLocally(User user) {
        Executors.newSingleThreadExecutor().execute(() -> {
            userDatabase.userDAO().updateUser(user);
        });
    }

    public interface OnUserValidationCallback {
        void onSuccess(User user);
        void onFailure(String errorMessage);
    }
}
