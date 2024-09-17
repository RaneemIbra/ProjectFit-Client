package com.example.projectfit.Server.API;

import com.example.projectfit.Utils.GsonProvider;
import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppAPI {
    private static final String BASE_URL = "http://192.168.1.73:8080/";
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            Gson gson = GsonProvider.getGson();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
