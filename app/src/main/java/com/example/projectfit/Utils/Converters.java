package com.example.projectfit.Utils;

import android.os.Build;
import androidx.room.TypeConverter;
import com.example.projectfit.Models.Workout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Converters {

    @TypeConverter
    public String fromLocalDate(LocalDate date) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return date != null ? date.format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
        }
        return null;
    }

    @TypeConverter
    public static LocalDate toLocalDate(String dateString) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return dateString != null ? LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE) : null;
        }
        return null;
    }

    @TypeConverter
    public String fromBooleanList(List<Boolean> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public List<Boolean> toBooleanList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Boolean>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String fromWorkoutMap(Map<Integer, Workout> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }

    @TypeConverter
    public Map<Integer, Workout> toWorkoutMap(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<Integer, Workout>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String fromStringList(List<String> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public List<String> toStringList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String fromIntegerList(List<Integer> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public List<Integer> toIntegerList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private static final Gson gson = new Gson();

    @TypeConverter
    public static String fromLocalDateMap(Map<LocalDate, Integer> map) {
        return gson.toJson(map);
    }

    @TypeConverter
    public static Map<LocalDate, Integer> toLocalDateMap(String json) {
        if (json == null) {
            return new HashMap<>();
        }

        Type type = new TypeToken<Map<String, Integer>>() {}.getType();
        Map<String, Integer> stringMap = gson.fromJson(json, type);

        if (stringMap == null) {
            return new HashMap<>();
        }

        Map<LocalDate, Integer> localDateMap = new HashMap<>();

        for (Map.Entry<String, Integer> entry : stringMap.entrySet()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                localDateMap.put(LocalDate.parse(entry.getKey()), entry.getValue());
            }
        }
        return localDateMap;
    }

    @TypeConverter
    public static String fromMap(Map<String, List<Workout>> map) {
        if (map == null) {
            return null;
        }
        Type type = new TypeToken<Map<String, List<Workout>>>() {}.getType();
        return gson.toJson(map, type);
    }

    @TypeConverter
    public static Map<String, List<Workout>> toMap(String mapString) {
        if (mapString == null) {
            return null;
        }
        Type type = new TypeToken<Map<String, List<Workout>>>() {}.getType();
        return gson.fromJson(mapString, type);
    }

    @TypeConverter
    public String fromWorkoutList(List<Workout> workoutList) {
        if (workoutList == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Workout>>() {}.getType();
        return gson.toJson(workoutList, type);
    }

    @TypeConverter
    public List<Workout> toWorkoutList(String workoutListString) {
        if (workoutListString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Workout>>() {}.getType();
        return gson.fromJson(workoutListString, type);
    }
}
