package com.example.projectfit.Server.Repositories;

import com.example.projectfit.Server.API.AppAPI;
import com.example.projectfit.Server.API.UserAPI;
import com.example.projectfit.Models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserServerRepository {
    private UserAPI userAPI;

    public UserServerRepository() {
        userAPI = AppAPI.getClient().create(UserAPI.class);
    }

    public void getUserByEmail(String email, Callback<User> callback) {
        Call<User> call = userAPI.getUserByEmail(email);
        call.enqueue(callback);
    }

    public void addUserInServer(User user) {
        Call<User> call = userAPI.createUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    System.out.println("User submitted successfully");
                } else {
                    System.out.println("User wasn't submitted successfully");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
            }
        });
    }

    public void validateUserServer(String email, String password, OnUserValidationCallback callback) {
        getUserByEmail(email, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User serverUser = response.body();
                    if (serverUser.getPassword().equals(password)) {
                        callback.onSuccess(serverUser);
                    } else {
                        callback.onFailure("Incorrect password");
                    }
                } else {
                    callback.onFailure("User not found on server");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onFailure("Server error: " + t.getMessage());
            }
        });
    }

    public void validateUserServerByAnswer(String email, String answer, OnUserValidationCallback callback) {
        getUserByEmail(email, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User serverUser = response.body();
                    if (serverUser.getAnswer().equals(answer)) {
                        callback.onSuccess(serverUser);
                    } else {
                        callback.onFailure("Incorrect security answer");
                    }
                } else {
                    callback.onFailure("User not found on server");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onFailure("Server error: " + t.getMessage());
            }
        });
    }

    public void updateUserPassword(User user, OnUserUpdateCallback callback) {
        Call<User> call = userAPI.updateUser(user.getId(), user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure("Failed to update password on server");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onFailure("Error: " + t.getMessage());
            }
        });
    }

    public interface OnUserValidationCallback {
        void onSuccess(User user);
        void onFailure(String errorMessage);
    }

    public interface OnUserUpdateCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
}
