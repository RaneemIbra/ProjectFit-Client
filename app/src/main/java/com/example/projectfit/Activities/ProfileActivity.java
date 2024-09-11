package com.example.projectfit.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectfit.Models.User;
import com.example.projectfit.Room.Repositories.UserRoomRepository;
import com.example.projectfit.Server.Repositories.UserServerRepository;
import com.example.projectfit.Utils.Converters;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectfit.R;
import com.example.projectfit.Utils.GsonProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

public class ProfileActivity extends AppCompatActivity {
    private EditText phoneEditText, heightEditText, weightEditText, dateEditText, nameEditText;
    private Button UpdateProfileButton, editDetailsButton, logoutButton;
    private TextView userNameTextView, userMailTextView, userAgeTextView, userHeightTextView,
            userWeightTextView, userBirthdateTextView, userPhoneTextView;
    private TextView fullNameTextView , weightTextView, heightTextView, PhoneTextView, dateTextView;
    private boolean isEditing = false; // To track whether we are in edit mode
    private ImageView profileImageView , goldCaloriesMedal, silverCaloriesMedal, bronzeCaloriesMedal,
            diamondCaloriesMedal,goldRunningMedal,silverRunningMedal,bronzeRunningMedal,diamondRunningMedal;
    BottomNavigationView bottomBar;
    private ImageButton selectFromGalleryButton;
    private Bitmap profilePhoto;
    private String photoPath;
    private UserServerRepository userServerRepository;
    private UserRoomRepository userRoomRepository;
    User user;
    private  List<Boolean> achievements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
            return insets;
        });
        user = getUserFromSharedPreferences();
        System.out.println("user name: " + user.getFullName());
        System.out.println("user email: " + user.getEmailAddress());
        System.out.println("user phone: " + user.getPhoneNum());
        System.out.println("user height: " + user.getHeight());
        System.out.println("user weight: " + user.getWeight());
        System.out.println("user birthday: " + user.getBirthday());
        System.out.println("user in profile");



        initViews();
        userServerRepository = new UserServerRepository();
        userRoomRepository = new UserRoomRepository(this);

        // Initialize other elements (medals, gallery button, etc.)
        selectFromGalleryButton = findViewById(R.id.selectFromGalleryButton);
        selectFromGalleryButton.setOnClickListener(v -> openGallery());

        setupButtonListeners();
        editDetailsButton.setOnClickListener(v -> {
            setEditTextsVisibility(View.VISIBLE);
            setViewTextsVisibility(View.VISIBLE);
            UpdateProfileButton.setVisibility(View.VISIBLE);
            editDetailsButton.setVisibility(View.GONE);
        });

        UpdateProfileButton.setOnClickListener(v -> {
            if (isEditing) {
                saveProfileChanges();
                setEditTextsVisibility(View.GONE);
                setViewTextsVisibility(View.GONE);

                UpdateProfileButton.setText("Update Profile");
                isEditing = false;
            } else {
                setEditTextsVisibility(View.VISIBLE);
                setViewTextsVisibility(View.VISIBLE);
                UpdateProfileButton.setText("Save");
                isEditing = true;
            }
        });


        logoutButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Toast.makeText(ProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
            startActivity(intent);
            finish();
        });
    }

    private void initViews() {
        profileImageView = findViewById(R.id.profileImageView);
        heightEditText = findViewById(R.id.heightEditText);
        weightEditText = findViewById(R.id.weightEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        dateEditText = findViewById(R.id.dateEditText);

        nameEditText = findViewById(R.id.nameEditText);
        editDetailsButton = findViewById(R.id.editDetailsButton);
        UpdateProfileButton = findViewById(R.id.UpdateProfileButton);
        logoutButton = findViewById(R.id.logoutButton);
        bottomBar = findViewById(R.id.bottom_navigation);
        userNameTextView = findViewById(R.id.userNameTextView);
        userMailTextView = findViewById(R.id.r9t1dlwyyn1);
        userBirthdateTextView = findViewById(R.id.rbd44z6os1qv);
        userHeightTextView = findViewById(R.id.rdmhc6s0vgl4);
        userWeightTextView = findViewById(R.id.ritmf488dqb);
        userAgeTextView = findViewById(R.id.r9ox48lkbrn);
        userPhoneTextView = findViewById(R.id.userPhoneTextView);


        fullNameTextView= findViewById(R.id.fullNameTextView);
        weightTextView=findViewById(R.id.weightTextView);
        heightTextView=findViewById(R.id.heightTextView);
        PhoneTextView=findViewById(R.id.PhoneTextView);
        dateTextView=findViewById(R.id.dateTextView);



        // Initialize medals
        goldCaloriesMedal = findViewById(R.id.gold2);
        silverCaloriesMedal = findViewById(R.id.silver2);
        bronzeCaloriesMedal = findViewById(R.id.medal2);
        diamondCaloriesMedal = findViewById(R.id.diamond2);

        goldRunningMedal = findViewById(R.id.gold);
         silverRunningMedal = findViewById(R.id.silver);
         bronzeRunningMedal = findViewById(R.id.medal);
         diamondRunningMedal = findViewById(R.id.diamond);

        // Initialize achievements to size 8, all set to false (not earned)
        achievements = new ArrayList<>(Collections.nCopies(8, false));
        user.setAchievements(achievements);
        achievements.set(0, true); // first medal for Running earned
        // Show or hide medals based on achievements list
        // Achievements for Calories Burned
        goldCaloriesMedal.setVisibility(achievements.get(6) ? View.VISIBLE : View.GONE);
        silverCaloriesMedal.setVisibility(achievements.get(5) ? View.VISIBLE : View.GONE);
        bronzeCaloriesMedal.setVisibility(achievements.get(4) ? View.VISIBLE : View.GONE);
        diamondCaloriesMedal.setVisibility(achievements.get(7) ? View.VISIBLE : View.GONE);

        // Achievements for Running
        goldRunningMedal.setVisibility(achievements.get(2) ? View.VISIBLE : View.GONE);
        silverRunningMedal.setVisibility(achievements.get(1) ? View.VISIBLE : View.GONE);
        bronzeRunningMedal.setVisibility(achievements.get(0) ? View.VISIBLE : View.GONE);
        diamondRunningMedal.setVisibility(achievements.get(3) ? View.VISIBLE : View.GONE);


        // Set values from the user object
        if (user != null) {
            userNameTextView.setText(user.getFullName());
            userMailTextView.setText(user.getEmailAddress());
            userBirthdateTextView.setText("Birthday: " + user.getBirthday());
            userHeightTextView.setText("Height: " + user.getHeight());
            userWeightTextView.setText("Weight: " + user.getWeight());

            if (user.getBirthday() != null) {
                int age = calculateAge(user.getBirthday());
                userAgeTextView.setText("Age: " + age);
            } else {
                userAgeTextView.setText("Age: Not Available");
            }

            userPhoneTextView.setText("Phone: " + user.getPhoneNum());

            if (user.getProfilePicture() != null) {
                byte[] profilePictureBytes = user.getProfilePicture();
                Bitmap bitmap = BitmapFactory.decodeByteArray(profilePictureBytes, 0, profilePictureBytes.length);
                profileImageView.setImageBitmap(bitmap);
            } else {
                profileImageView.setImageResource(R.drawable.user);
            }
        }
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

    private void setupButtonListeners() {
        bottomBar.setOnNavigationItemSelectedListener(item -> {
            int id_item = item.getItemId();

            if (id_item == R.id.home_BottomIcon) {
                return true;
            } else if (id_item == R.id.plan_BottomIcon) {
                navigateTo(MyPlanActivity.class);
                return true;
            } else if (id_item == R.id.workouts_BottomIcon) {
                navigateTo(WorkoutsFilterActivity.class);
                return true;
            } else if (id_item == R.id.profile_BottomIcon) {
                navigateTo(ProfileActivity.class);
                return true;
            } else {
                return false;
            }
        });
        dateEditText.setOnClickListener(v -> {
            showDatePickerDialog();
        });
    }


    private void navigateTo(Class<?> targetActivity) {
        startActivity(new Intent(ProfileActivity.this, targetActivity));
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryActivityResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    try {
                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                        profilePhoto = BitmapFactory.decodeStream(imageStream);

                        byte[] profilePictureBytes = convertBitmapToByteArray(profilePhoto);
                        user.setProfilePicture(profilePictureBytes);

                        profileImageView.setImageBitmap(profilePhoto);

                        // Update user in the local database and server
                        Executors.newSingleThreadExecutor().execute(() -> userRoomRepository.updateUserLocally(user));
                        updateUserOnServer(user, "Profile picture updated successfully!");

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load image from gallery", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void saveProfileChanges() {
        Log.d("ProfileActivity", "saveProfileChanges called"); // Log for debugging

        // Get updated values from EditText fields
        String updatedName = nameEditText.getText().toString();
        String updatedPhone = phoneEditText.getText().toString();
        String updatedHeight = heightEditText.getText().toString();
        String updatedWeight = weightEditText.getText().toString();
        String updatedDate = dateEditText.getText().toString();

        // Validation: Ensure no fields are empty
        if (updatedHeight.isEmpty() || updatedWeight.isEmpty() || updatedPhone.isEmpty() || updatedDate.isEmpty() || updatedName.isEmpty()) {
            Toast.makeText(this, "All fields must be filled out", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert date using the Converters class
        Converters converters = new Converters();
        LocalDate updatedLocalDate = converters.toLocalDate(updatedDate);
        if (updatedLocalDate == null) {
            Toast.makeText(this, "Invalid date format. Please use the correct format (YYYY-MM-DD).", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update user object with the new data
        user.setFullName(updatedName);
        user.setBirthday(updatedLocalDate);

        try {
            long phone = Long.parseLong(updatedPhone);
            double height = Double.parseDouble(updatedHeight);
            double weight = Double.parseDouble(updatedWeight);

            user.setPhoneNum(phone);
            user.setHeight(height);
            user.setWeight(weight);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format. Please enter valid data.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save updated user data in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = GsonProvider.getGson();
        String userJson = gson.toJson(user);
        editor.putString("logged_in_user", userJson);
        editor.apply(); // Apply changes to SharedPreferences

        // Update the local Room database (save on a background thread)
        Executors.newSingleThreadExecutor().execute(() -> userRoomRepository.updateUserLocally(user));

        // Update the UI with the new values
        userNameTextView.setText(updatedName);
        userPhoneTextView.setText("Phone: " + updatedPhone);
        userHeightTextView.setText("Height: " + updatedHeight);
        userWeightTextView.setText("Weight: " + updatedWeight);
        userBirthdateTextView.setText("Birthday: " + updatedDate);

        int age = calculateAge(updatedLocalDate);
        userAgeTextView.setText("Age: " + age);

        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
    }




    private void updateUserOnServer(User user, String successMessage) {
        new Thread(() -> userServerRepository.updateUser(user, new UserServerRepository.OnUserUpdateCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, successMessage, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> showError("Server update failed: " + errorMessage));
            }
        })).start();
    }

    private void showError(String errorMessage) {
        runOnUiThread(() -> Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show());
    }

    private User getUserFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("logged_in_user", null);

        if (userJson != null) {
            Gson gson = GsonProvider.getGson();
            Type userType = new TypeToken<User>() {}.getType();
            return gson.fromJson(userJson, userType);
        }
        return null;
    }

    private void setEditTextsVisibility(int visibility) {
        heightEditText.setVisibility(visibility);
        weightEditText.setVisibility(visibility);
        phoneEditText.setVisibility(visibility);
        dateEditText.setVisibility(visibility);
        nameEditText.setVisibility(visibility);
    }
    private void setViewTextsVisibility(int visibility) {
        fullNameTextView.setVisibility(visibility);
        weightTextView.setVisibility(visibility);
        heightTextView.setVisibility(visibility);
        PhoneTextView.setVisibility(visibility);
        dateTextView.setVisibility(visibility);

    }
    // Method to display DatePickerDialog
    private void showDatePickerDialog() {
        // Get the current date as default values
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and set the listener
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Update EditText with the selected date (increment month by 1, as it starts from 0)
                    String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    dateEditText.setText(selectedDate);
                },
                year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

}
