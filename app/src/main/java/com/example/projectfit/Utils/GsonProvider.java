package com.example.projectfit.Utils;

import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.time.LocalDate;

public class GsonProvider {

    public static Gson getGson() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) ->
                            LocalDate.parse(json.getAsString()))
                    .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                            new JsonPrimitive(src.toString()))
                    .create();
        }
        return null;
    }
}
