package com.example.projectfit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppAPI {
    private static final String BASE_URL = "http://192.168.252.206:8080/";
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
