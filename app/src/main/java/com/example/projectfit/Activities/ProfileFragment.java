package com.example.projectfit.Activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projectfit.Models.User;
import com.example.projectfit.Models.Workout;
import com.example.projectfit.R;
import com.example.projectfit.Room.Repositories.UserRoomRepository;
import com.example.projectfit.Room.Repositories.WorkoutRoomRepository;
import com.example.projectfit.Server.Repositories.UserServerRepository;
import com.example.projectfit.Utils.Converters;
import com.example.projectfit.Utils.GsonProvider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment {

    private EditText phoneEditText, heightEditText, weightEditText, dateEditText, nameEditText;
    private Button UpdateProfileButton, editDetailsButton, logoutButton;
    private TextView userNameTextView, userMailTextView, userAgeTextView, userHeightTextView, userWeightTextView, userBirthdateTextView, userPhoneTextView;
    private TextView fullNameTextView, weightTextView, heightTextView, PhoneTextView, dateTextView;
    private boolean isEditing = false;
    private ImageView profileImageView;
    private ImageButton selectFromGalleryButton;
    private Bitmap profilePhoto;
    private UserServerRepository userServerRepository;
    private UserRoomRepository userRoomRepository;
    private User user;
    private WorkoutRoomRepository workoutRepository;
    ImageView medal1, silver1, gold1, diamond1, medal2, silver2, gold2, diamond2, medal3, silver3, gold3, diamond3, medal4, silver4, gold4, diamond4;

    private List<Boolean> achievements;

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userServerRepository = new UserServerRepository();
        userRoomRepository = new UserRoomRepository(getContext());

        galleryActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                Uri selectedImage = result.getData().getData();
                try {
                    InputStream imageStream = getContext().getContentResolver().openInputStream(selectedImage);
                    profilePhoto = BitmapFactory.decodeStream(imageStream);

                    byte[] profilePictureBytes = convertBitmapToByteArray(profilePhoto);
                    user.setProfilePicture(profilePictureBytes);

                    profileImageView.setImageBitmap(profilePhoto);

                    Executors.newSingleThreadExecutor().execute(() -> userRoomRepository.updateUserLocally(user));
                    updateUserOnServer(user, "Profile picture updated successfully!");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed to load image from gallery", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        user = getUserFromSharedPreferences();
        initViews(view);
        initAchievements(view);

        // Observe the LiveData for workouts
        workoutRepository = new WorkoutRoomRepository(getContext());
        workoutRepository.getAllWorkoutsLocally().observe(getViewLifecycleOwner(), workouts -> {
            if (workouts != null) {
                int totalSteps = calulateTotalSteps(user);
                int totalCalories = (int) calculateCaloriesBurned(user) + calculateCaloriesBurnedForWorkouts(workouts);
                int totalWaterDrank = calculateTotalWaterDrank(user);
                int totalSetsCompleted = calculateSetsCompleted(workouts);
                System.out.println("totalSteps: " + totalSteps);
                System.out.println("totalCalories: " + totalCalories);
                System.out.println("totalWaterDrank: " + totalWaterDrank);
                System.out.println("totalSetsCompleted: " + totalSetsCompleted);

                loadAchievements(totalSteps, totalCalories, totalWaterDrank, totalSetsCompleted);
                setupMedalClickListeners(workouts);
            }
        });

        setupButtonListeners();
        return view;
    }




    private int calculateCaloriesBurnedForWorkouts(List<Workout> workouts) {
        int totalCalories = 0;
        for (Workout workout : workouts) {
            double timesDidTheWorkout= (double) workout.getSets() /workout.getSets_reps().get(1);
            totalCalories += (int) (workout.getCalories() * timesDidTheWorkout);
        }
        return totalCalories;
    }

    private void initViews(View view) {
        profileImageView = view.findViewById(R.id.profileImageView);
        heightEditText = view.findViewById(R.id.heightEditText);
        weightEditText = view.findViewById(R.id.weightEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        dateEditText = view.findViewById(R.id.dateEditText);
        nameEditText = view.findViewById(R.id.nameEditText);
        editDetailsButton = view.findViewById(R.id.editDetailsButton);
        UpdateProfileButton = view.findViewById(R.id.UpdateProfileButton);
        logoutButton = view.findViewById(R.id.logoutButton);

        userNameTextView = view.findViewById(R.id.userNameTextView);
        userMailTextView = view.findViewById(R.id.userMailTextView);
        userAgeTextView = view.findViewById(R.id.userAgeTextView);
        userHeightTextView = view.findViewById(R.id.userHeightTextView);
        userWeightTextView = view.findViewById(R.id.userWeightTextView);
        userBirthdateTextView = view.findViewById(R.id.userBirthdateTextView);
        userPhoneTextView = view.findViewById(R.id.userPhoneTextView);

        fullNameTextView = view.findViewById(R.id.fullNameTextView);
        weightTextView = view.findViewById(R.id.weightTextView);
        heightTextView = view.findViewById(R.id.heightTextView);
        PhoneTextView = view.findViewById(R.id.PhoneTextView);
        dateTextView = view.findViewById(R.id.dateTextView);

        selectFromGalleryButton = view.findViewById(R.id.selectFromGalleryButton);
        selectFromGalleryButton.setOnClickListener(v -> openGallery());

        // Populate views from user object
        if (user != null) {
            userNameTextView.setText(user.getFullName());
            userMailTextView.setText(user.getEmailAddress());
            userBirthdateTextView.setText("Birthday: " + user.getBirthday());
            userHeightTextView.setText("Height: " + user.getHeight());
            userWeightTextView.setText("Weight: " + user.getWeight());

            if (user.getBirthday() != null) {
                int age = calculateAge(user.getBirthday());
                userAgeTextView.setText("Age: " + age);
            }

            userPhoneTextView.setText("Phone: " + user.getPhoneNum());
        }

    }



    private void initAchievements(View view) {
        // Initialize views and apply the grayscale filter
        medal1 = view.findViewById(R.id.medal1);
        silver1 = view.findViewById(R.id.silver1);
        gold1 = view.findViewById(R.id.gold1);
        diamond1 = view.findViewById(R.id.diamond1);

        medal2 = view.findViewById(R.id.medal2);
        silver2 = view.findViewById(R.id.silver2);
        gold2 = view.findViewById(R.id.gold2);
        diamond2 = view.findViewById(R.id.diamond2);

        medal3 = view.findViewById(R.id.medal3);
        silver3 = view.findViewById(R.id.silver3);
        gold3 = view.findViewById(R.id.gold3);
        diamond3 = view.findViewById(R.id.diamond3);

        medal4 = view.findViewById(R.id.medal4);
        silver4 = view.findViewById(R.id.silver4);
        gold4 = view.findViewById(R.id.gold4);
        diamond4 = view.findViewById(R.id.diamond4);

        // Apply grayscale filter to all views
        applyGrayscaleFilter(medal1);
        applyGrayscaleFilter(silver1);
        applyGrayscaleFilter(gold1);
        applyGrayscaleFilter(diamond1);
        applyGrayscaleFilter(medal2);
        applyGrayscaleFilter(silver2);
        applyGrayscaleFilter(gold2);
        applyGrayscaleFilter(diamond2);
        applyGrayscaleFilter(medal3);
        applyGrayscaleFilter(silver3);
        applyGrayscaleFilter(gold3);
        applyGrayscaleFilter(diamond3);
        applyGrayscaleFilter(medal4);
        applyGrayscaleFilter(silver4);
        applyGrayscaleFilter(gold4);
        applyGrayscaleFilter(diamond4);
    }
    private void loadAchievements(int totalSteps, int totalCalories, int totalWaterDrinked, int totalSetsCompleted) {
        if(totalSteps>10000)
            removeGrayScaleFilter(medal1);
        if(totalSteps>50000)
            removeGrayScaleFilter(silver1);
        if(totalSteps>100000)
            removeGrayScaleFilter(gold1);
        if(totalSteps>1000000)
            removeGrayScaleFilter(diamond1);

        if(totalCalories>2350)
            removeGrayScaleFilter(medal2);
        if(totalCalories>11750)
            removeGrayScaleFilter(silver2);
        if(totalCalories>23500)
            removeGrayScaleFilter(gold2);
        if(totalCalories>235000)
            removeGrayScaleFilter(diamond2);

        if(totalWaterDrinked>3200)
            removeGrayScaleFilter(medal3);
        if(totalWaterDrinked>16000)
            removeGrayScaleFilter(silver3);
        if(totalWaterDrinked>32000)
            removeGrayScaleFilter(gold3);
        if(totalWaterDrinked>320000)
            removeGrayScaleFilter(diamond3);

        if(totalSetsCompleted>18)
            removeGrayScaleFilter(medal4);
        if(totalSetsCompleted>90)
            removeGrayScaleFilter(silver4);
        if(totalSetsCompleted>180)
            removeGrayScaleFilter(gold4);
        if(totalSetsCompleted>1800)
            removeGrayScaleFilter(diamond4);
    }
    // Function to set up click listeners for medals and show a message based on completion status
    private void setupMedalClickListeners(List<Workout> workouts) {
        // Set click listener for medal1
        medal1.setOnClickListener(v -> showMedalAchievementMessage("steps", 10000, calulateTotalSteps(user)));

        // Set click listener for silver1
        silver1.setOnClickListener(v -> showMedalAchievementMessage("steps", 50000, calulateTotalSteps(user)));

        // Set click listener for gold1
        gold1.setOnClickListener(v -> showMedalAchievementMessage("steps", 100000, calulateTotalSteps(user)));

        // Set click listener for diamond1
        diamond1.setOnClickListener(v -> showMedalAchievementMessage("steps", 1000000, calulateTotalSteps(user)));

        // Set click listener for medal2
        medal2.setOnClickListener(v -> showMedalAchievementMessage("calories", 2350, (int) calculateCaloriesBurned(user)));

        // Set click listener for silver2
        silver2.setOnClickListener(v -> showMedalAchievementMessage("calories", 11750, (int) calculateCaloriesBurned(user)));

        // Set click listener for gold2
        gold2.setOnClickListener(v -> showMedalAchievementMessage("calories", 23500, (int) calculateCaloriesBurned(user)));

        // Set click listener for diamond2
        diamond2.setOnClickListener(v -> showMedalAchievementMessage("calories", 235000, (int) calculateCaloriesBurned(user)));

        // Set click listener for medal3
        medal3.setOnClickListener(v -> showMedalAchievementMessage("water", 3200, calculateTotalWaterDrank(user)));

        // Set click listener for silver3
        silver3.setOnClickListener(v -> showMedalAchievementMessage("water", 16000, calculateTotalWaterDrank(user)));

        // Set click listener for gold3
        gold3.setOnClickListener(v -> showMedalAchievementMessage("water", 32000, calculateTotalWaterDrank(user)));

        // Set click listener for diamond3
        diamond3.setOnClickListener(v -> showMedalAchievementMessage("water", 320000, calculateTotalWaterDrank(user)));

        // Set click listener for medal4
        medal4.setOnClickListener(v -> showMedalAchievementMessage("sets", 18, calculateSetsCompleted(workouts)));

        // Set click listener for silver4
        silver4.setOnClickListener(v -> showMedalAchievementMessage("sets", 90, calculateSetsCompleted(workouts)));

        // Set click listener for gold4
        gold4.setOnClickListener(v -> showMedalAchievementMessage("sets", 180, calculateSetsCompleted(workouts)));

        // Set click listener for diamond4
        diamond4.setOnClickListener(v -> showMedalAchievementMessage("sets", 1800, calculateSetsCompleted(workouts)));
    }

    // Function to show achievement message based on completion status
    private void showMedalAchievementMessage(String type, int required, int current) {
        String message;
        if (current >= required) {
            message = getAchievementMessage(type, required, true);
        } else {
            int remaining = required - current;
            message = getAchievementMessage(type, remaining, false);
        }

        // Show a Toast message near the clicked medal
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Helper function to get the achievement message
    private String getAchievementMessage(String type, int value, boolean achieved) {
        String message;
        if (achieved) {
            switch (type) {
                case "steps":
                    message = "You Walked " + value + " Steps!";
                    break;
                case "calories":
                    message = "You Burned " + value + " Calories!";
                    break;
                case "water":
                    message = "You Drank " + value + " ml of Water!";
                    break;
                case "sets":
                    message = "You Completed " + value + " Sets!";
                    break;
                default:
                    message = "Achievement Unlocked!";
            }
        } else {
            switch (type) {
                case "steps":
                    message = "You need to walk " + value + " more steps to achieve this medal!";
                    break;
                case "calories":
                    message = "You need to burn " + value + " more calories to achieve this medal!";
                    break;
                case "water":
                    message = "You need to drink " + value + " more ml of water to achieve this medal!";
                    break;
                case "sets":
                    message = "You need to complete " + value + " more sets to achieve this medal!";
                    break;
                default:
                    message = "Keep going to unlock this achievement!";
            }
        }
        return message;
    }



    private void setupButtonListeners() {
        editDetailsButton.setOnClickListener(v -> {
            setEditTextsVisibility(View.VISIBLE);
            UpdateProfileButton.setVisibility(View.VISIBLE);
            editDetailsButton.setVisibility(View.GONE);
        });
        dateEditText.setOnClickListener(v -> {
            showDatePickerDialog();
        });
        logoutButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            requireActivity().finish();
        });

        UpdateProfileButton.setOnClickListener(v -> saveProfileChanges());
    }
    private int calulateTotalSteps(User user) {
        int totalSteps = 0;
        for (Integer steps : user.getStepsHistory().values()) {
            totalSteps += steps;
        }
        return totalSteps;
    }

    private double calculateCaloriesBurned(User user) {
        // Extract user data
        int age = calculateAge(user.getBirthday()); // Calculate age from birthdate
        double height = user.getHeight(); // in cm
        double weight = user.getWeight(); // in kg
        boolean isMale = user.isGender(); // true for male, false for female
        int totalSteps =calulateTotalSteps(user);

        // Calculate the Basal Metabolic Rate (BMR) using the Mifflin-St Jeor Equation
        double bmr;
        if (isMale) {
            // Mifflin-St Jeor Equation for males
            bmr = 10 * weight + 6.25 * height - 5 * age + 5;
        } else {
            // Mifflin-St Jeor Equation for females
            bmr = 10 * weight + 6.25 * height - 5 * age - 161;
        }

        // Convert BMR to Calories burned per minute (cal/min)
        // Assume Light activity level: TDEE = BMR * 1.375 for light activity (light exercise/sports 1-3 days a week)
        double tdee = bmr * 1.375; // Total Daily Energy Expenditure (lightly active)

        // Calculate user's average steps per day from step history
        int averageStepsPerDay = calculateAverageStepsPerDay(user);

        // If there is not enough data to calculate an average, use a default value
        if (averageStepsPerDay == 0) {
            averageStepsPerDay = 10000; // Default average steps per day
        }

        // Calculate calories burned per step based on TDEE
        double caloriesPerStep = tdee / averageStepsPerDay; // Calories burned per step

        // Calculate total calories burned based on the actual number of steps
        return totalSteps * caloriesPerStep;

    }

    private int calculateTotalWaterDrank(User user) {
        int totalWater = 0;
        for (Integer water : user.getWaterHistory().values()) {
            totalWater += water;
        }
        return totalWater;
    }


    // Helper function to calculate the average steps per day
    private int calculateAverageStepsPerDay(User user) {
        if (user.getStepsHistory() == null || user.getStepsHistory().isEmpty()) {
            return 0; // No step data available
        }

        // Calculate total steps and the number of days recorded
        int totalSteps = 0;
        int totalDays = user.getStepsHistory().size();

        for (Integer steps : user.getStepsHistory().values()) {
            totalSteps += steps;
        }

        // Calculate and return the average steps per day
        return totalSteps / totalDays;
    }
    private int calculateSetsCompleted(List<Workout> workouts) {
        int setsCompleted = 0;
        for (Workout workout : workouts) {
            setsCompleted += workout.getSets();
        }
        return setsCompleted;
    }


    private void saveProfileChanges() {
        Log.d("ProfileFragment", "saveProfileChanges called");

        String updatedName = nameEditText.getText().toString();
        String updatedPhone = phoneEditText.getText().toString();
        String updatedHeight = heightEditText.getText().toString();
        String updatedWeight = weightEditText.getText().toString();
        String updatedDate = dateEditText.getText().toString();

        Converters converters = new Converters();

        if (!updatedName.isEmpty()) {
            user.setFullName(updatedName);
            userNameTextView.setText(updatedName);
        }

        if (!updatedPhone.isEmpty()) {
            try {
                long phone = Long.parseLong(updatedPhone);
                user.setPhoneNum(phone);
                userPhoneTextView.setText("Phone: " + updatedPhone);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid phone number format.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!updatedHeight.isEmpty()) {
            try {
                double height = Double.parseDouble(updatedHeight);
                user.setHeight(height);
                userHeightTextView.setText("Height: " + updatedHeight);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid height format.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!updatedWeight.isEmpty()) {
            try {
                double weight = Double.parseDouble(updatedWeight);
                user.setWeight(weight);
                userWeightTextView.setText("Weight: " + updatedWeight);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid weight format.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!updatedDate.isEmpty()) {
            LocalDate updatedLocalDate = converters.toLocalDate(updatedDate);
            if (updatedLocalDate != null) {
                user.setBirthday(updatedLocalDate);
                userBirthdateTextView.setText("Birthday: " + updatedDate);
                int age = calculateAge(updatedLocalDate);
                userAgeTextView.setText("Age: " + age);
            } else {
                Toast.makeText(getContext(), "Invalid date format. Please use YYYY-MM-DD.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = GsonProvider.getGson();
        String userJson = gson.toJson(user);
        editor.putString("logged_in_user", userJson);
        editor.apply();

        Executors.newSingleThreadExecutor().execute(() -> userRoomRepository.updateUserLocally(user));

        setEditTextsVisibility(View.GONE);
        setViewTextsVisibility(View.GONE);
        editDetailsButton.setVisibility(View.VISIBLE);
        UpdateProfileButton.setVisibility(View.GONE);

        Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
    }


    private void setViewTextsVisibility(int visibility) {
        fullNameTextView.setVisibility(visibility);
        weightTextView.setVisibility(visibility);
        heightTextView.setVisibility(visibility);
        PhoneTextView.setVisibility(visibility);
        dateTextView.setVisibility(visibility);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryActivityResultLauncher.launch(intent);
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private int calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            return 0;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Period.between(birthDate, LocalDate.now()).getYears();
        }
        return 0;
    }

    private User getUserFromSharedPreferences() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("logged_in_user", null);

        if (userJson != null) {
            Gson gson = GsonProvider.getGson();
            Type userType = new TypeToken<User>() {}.getType();
            User user = gson.fromJson(userJson, userType);
            Log.d("ProfileFragment", "User retrieved: " + user.getFullName()); // Debug log
            return user;
        }
        Log.e("ProfileFragment", "No user found in SharedPreferences");
        return null;
    }


    private void updateUserOnServer(User user, String successMessage) {
        new Thread(() -> userServerRepository.updateUser(user, new UserServerRepository.OnUserUpdateCallback() {
            @Override
            public void onSuccess() {
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), successMessage, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onFailure(String errorMessage) {
                getActivity().runOnUiThread(() -> showError("Server update failed: " + errorMessage));
            }
        })).start();
    }

    private void showError(String errorMessage) {
        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show());
    }

    private void setEditTextsVisibility(int visibility) {
        heightEditText.setVisibility(visibility);
        weightEditText.setVisibility(visibility);
        phoneEditText.setVisibility(visibility);
        dateEditText.setVisibility(visibility);
        nameEditText.setVisibility(visibility);
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, selectedYear, selectedMonth, selectedDay) -> {
            String selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
            dateEditText.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }
    // Create a method to apply grayscale filter
    private void applyGrayscaleFilter(ImageView imageView) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0); // Set saturation to 0 to convert the image to grayscale
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(filter);
    }
    private void removeGrayScaleFilter(ImageView imageView) {
        imageView.setColorFilter(null);
    }

}