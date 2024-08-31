package com.example.projectfit.Server.Repositories;

import com.example.projectfit.Server.API.AppAPI;
import com.example.projectfit.Server.API.UserAPI;
import com.example.projectfit.Server.Models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserServerRepository {
    private UserAPI userAPI;
    public UserServerRepository() {
        userAPI = AppAPI.getClient().create(UserAPI.class);
    }
    public void addUser(User user) {
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
}
