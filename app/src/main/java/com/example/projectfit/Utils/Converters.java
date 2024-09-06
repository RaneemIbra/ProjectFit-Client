package com.example.projectfit.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;

import androidx.room.TypeConverter;

import com.example.projectfit.Models.Workout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class Converters {

    public static String convertDrawableToBase64(Context context, int drawableId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    public static byte[] convertDrawableToByteArray(Context context, int drawableId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    // LocalDate to String
    @TypeConverter
    public String fromLocalDate(LocalDate date) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return date != null ? date.format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
        }
        return null;
    }

    // String to LocalDate
    @TypeConverter
    public LocalDate toLocalDate(String dateString) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return dateString != null ? LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE) : null;
        }
        return null;
    }

    // List<Boolean> to String
    @TypeConverter
    public String fromBooleanList(List<Boolean> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    // String to List<Boolean>
    @TypeConverter
    public List<Boolean> toBooleanList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Boolean>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // Map<Integer, Workout> to String
    @TypeConverter
    public String fromWorkoutMap(Map<Integer, Workout> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }

    // String to Map<Integer, Workout>
    @TypeConverter
    public Map<Integer, Workout> toWorkoutMap(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<Integer, Workout>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // List<String> to String
    @TypeConverter
    public String fromStringList(List<String> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    // String to List<String>
    @TypeConverter
    public List<String> toStringList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // List<Integer> to String
    @TypeConverter
    public String fromIntegerList(List<Integer> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    // String to List<Integer>
    @TypeConverter
    public List<Integer> toIntegerList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // Map<LocalDate, Integer> to String
    @TypeConverter
    public String fromLocalDateMap(Map<LocalDate, Integer> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }

    // String to Map<LocalDate, Integer>
    @TypeConverter
    public Map<LocalDate, Integer> toLocalDateMap(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<LocalDate, Integer>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
