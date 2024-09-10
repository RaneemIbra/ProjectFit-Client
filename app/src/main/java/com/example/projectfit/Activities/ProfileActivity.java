package com.example.projectfit.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import com.example.projectfit.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class ProfileActivity extends AppCompatActivity {
    private EditText emailEditText, phoneEditText, heightEditText, weightEditText, dateEditText, nameEditText;
    private Button UpdateProfileButton, editDetailsButton, logoutButton;
    private TextView userNameTextView, userMailTextView, userAgeTextView, userHeightTextView,
            userWeightTextView, userBirthdateTextView, userPhoneTextView;
    private boolean isEditing = false; // To track whether we are in edit mode
    private ImageView profileImageView;
    BottomNavigationView bottomBar;
    private List<Boolean> achievements;
   // private ImageButton takePhotoButton;
    private ImageButton selectFromGalleryButton;
    private Bitmap profilePhoto;
    private String photoPath;
    private UserServerRepository userServerRepository;
    private UserRoomRepository userRoomRepository;
    // Camera permission launcher
   /* private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    launchCamera();
                } else {
                    Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show();
                }
            });

    // Camera result launcher
    private final ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    profilePhoto = (Bitmap) result.getData().getExtras().get("data");
                    profileImageView.setImageBitmap(profilePhoto);
                }
            });
            */


    // Gallery result launcher
    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    try {
                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                        profilePhoto = BitmapFactory.decodeStream(imageStream);
                        profileImageView.setImageBitmap(profilePhoto);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load image from gallery", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        userServerRepository = new UserServerRepository();
        userRoomRepository = new UserRoomRepository(this);
        // Initialize views for achievements
        ShapeableImageView runningMedal = findViewById(R.id.medal);
        ShapeableImageView runningSilver = findViewById(R.id.silver);
        ShapeableImageView runningGold = findViewById(R.id.gold);
        ShapeableImageView runningDiamond = findViewById(R.id.diamond);

        ShapeableImageView caloriesMedal = findViewById(R.id.medal2);
        ShapeableImageView caloriesSilver = findViewById(R.id.silver2);
        ShapeableImageView caloriesGold = findViewById(R.id.gold2);
        ShapeableImageView caloriesDiamond = findViewById(R.id.diamond2);

        selectFromGalleryButton = findViewById(R.id.selectFromGalleryButton);
        selectFromGalleryButton.setOnClickListener(v -> openGallery());
        // Initialize ImageButton for taking photo
        ///takePhotoButton = findViewById(R.id.takePhotoButton);
        // Set the click listener for the ImageButton
        //takePhotoButton.setOnClickListener(v -> checkCameraHardwareAndRequestPermission());
        // Get current user's achievements
        HomePageActivity.user.setAchievements(new ArrayList<>(8));
        achievements = HomePageActivity.user.getAchievements();
        // Dynamically update the UI based on the achievements
        // Check if the achievements list has at least 8 elements before updating the icons
        if (achievements.size() >=8) {
            updateAchievementIcons(runningMedal, runningSilver, runningGold, runningDiamond, achievements.subList(0, 4));
            updateAchievementIcons(caloriesMedal, caloriesSilver, caloriesGold, caloriesDiamond, achievements.subList(4, 8));
        } else {
            // Handle cases where there are fewer achievements

            Toast.makeText(this, "Not enough achievements to display.", Toast.LENGTH_SHORT).show();
        }
        setupButtonListeners();
        // Set a click listener on the "Edit Details" button
        editDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the visibility of the form elements to VISIBLE
                userNameTextView.setVisibility(View.VISIBLE);
                nameEditText.setVisibility(View.VISIBLE);
                emailEditText.setVisibility(View.VISIBLE);
                phoneEditText.setVisibility(View.VISIBLE);
                heightEditText.setVisibility(View.VISIBLE);
                weightEditText.setVisibility(View.VISIBLE);
                dateEditText.setVisibility(View.VISIBLE);
                UpdateProfileButton.setVisibility(View.VISIBLE);


                // hide the "Edit Details" button
                editDetailsButton.setVisibility(View.GONE);


            }
        });
        UpdateProfileButton.setOnClickListener(v -> {
            if (isEditing) {
                // Save changes and hide EditTexts
                saveProfileChanges();
                setEditTextsVisibility(View.GONE);
                UpdateProfileButton.setText("Update Profile");
                isEditing = false;
            } else {
                // Make EditTexts visible for editing
                setEditTextsVisibility(View.VISIBLE);
                UpdateProfileButton.setText("Save");
                isEditing = true;
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear user session or data (e.g., SharedPreferences)
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Clear all stored user data
                editor.apply();

                // Optionally, show a Toast message
                Toast.makeText(ProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                // Redirect the user to the login activity
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
                startActivity(intent);
                finish(); // Close the current activity
            }
        });


    }

    private void initViews() {
        heightEditText = findViewById(R.id.heightEditText);
        weightEditText = findViewById(R.id.weightEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        dateEditText = findViewById(R.id.dateEditText);
        emailEditText = findViewById(R.id.emailEditText);
        nameEditText = findViewById(R.id.nameEditText);
        editDetailsButton = findViewById(R.id.editDetailsButton);
        UpdateProfileButton = findViewById(R.id.UpdateProfileButton);
        logoutButton = findViewById(R.id.logoutButton);
        bottomBar = findViewById(R.id.bottom_navigation);


        // setting the current user name
        userNameTextView = findViewById(R.id.userNameTextView);
        // Get the current user's name
        String currentUserName = HomePageActivity.user.getFullName();
        // Set the name to the TextView
        userNameTextView.setText(currentUserName);


        //setting the current user mail
        userMailTextView = findViewById(R.id.r9t1dlwyyn1);
        // Get the current user's mail
        String currentUserMail = HomePageActivity.user.getEmailAddress();
        // Set the mail to the TextView
        userMailTextView.setText(currentUserMail);


        //setting the current user birthdayDate
        userBirthdateTextView = findViewById(R.id.rbd44z6os1qv);
        // Get the current user's birthdayDate
        String currentUserBirthdate = String.valueOf(HomePageActivity.user.getBirthday());
        // Set the birthday date to the TextView
        userBirthdateTextView.setText("Birthday: " + currentUserBirthdate);


        // setting the current user height
        userHeightTextView = findViewById(R.id.rdmhc6s0vgl4);
        // Get the current user's height
        String currentUserHeight = String.valueOf(HomePageActivity.user.getHeight());
        // Set the height to the TextView
        userHeightTextView.setText("Height: " + currentUserHeight);


        // setting the current user weight
        userWeightTextView = findViewById(R.id.ritmf488dqb);
        // Get the current user's weight
        String currentUserWeight = String.valueOf(HomePageActivity.user.getWeight());
        // Set the weight to the TextView
        userWeightTextView.setText("Weight: " + currentUserWeight);


        //setting the current user age
        userAgeTextView = findViewById(R.id.r9ox48lkbrn);
        // Ensure that HomePageActivity.user is not null and getBirthday() returns a valid Date
        if (HomePageActivity.user != null && HomePageActivity.user.getBirthday() != null) {
            LocalDate birthDate = HomePageActivity.user.getBirthday();

            // Calculate the age from the birthday
            int age = calculateAge(birthDate);

            // Set the age to the TextView
            userAgeTextView.setText("Age: " + age);
        } else {
            // Handle the case where user or birthDate is null
            userAgeTextView.setText("Age: Not Available");
        }


        // Find the TextView for the phone number
        userPhoneTextView = findViewById(R.id.userPhoneTextView);

        // Ensure that HomePageActivity.user is not null and getPhoneNumber() returns a valid phone number
        if (HomePageActivity.user != null && HomePageActivity.user.getPhoneNum() != null) {
            String phoneNumber = String.valueOf(HomePageActivity.user.getPhoneNum());

            // Set the phone number to the TextView
            userPhoneTextView.setText("Phone: " + phoneNumber);
        } else {
            // Handle the case where the phone number is not available
            userPhoneTextView.setText("Phone: Not Available");
        }

        // In your activity or fragment

        // Find the ShapeableImageView for the profile picture
        ShapeableImageView profileImageView = findViewById(R.id.profileImageView);

        // Check if the user object and profile picture URL exist
        if (HomePageActivity.user != null && HomePageActivity.user.getProfilePicture() != null) {
            String profilePictureUrl = HomePageActivity.user.getProfilePicture();

            // Load the profile picture using Glide
            Glide.with(this)
                    .load(profilePictureUrl)  // URL or drawable
                    .placeholder(R.drawable.user)  // Placeholder while loading
                    .error(R.drawable.user)        // Error image if loading fails
                    .into(profileImageView);
        } else {
            // Handle the case where profile picture URL is not available
            profileImageView.setImageResource(R.drawable.user);


        }


    }
    // Update achievement icons based on the list of achievements
    private void updateAchievementIcons(ShapeableImageView medal, ShapeableImageView silver, ShapeableImageView gold, ShapeableImageView diamond, List<Boolean> achievementList) {
        // Hide medals if there are no achievements
        if (achievementList.get(0)) {
            medal.setVisibility(View.VISIBLE); // Unlocked bronze icon
            medal.setImageResource(R.drawable.medal); // Set icon for unlocked state
        } else {
            medal.setVisibility(View.GONE);  // Hide if no achievement
        }

        if (achievementList.get(1)) {
            silver.setVisibility(View.VISIBLE); // Unlocked silver icon
            silver.setImageResource(R.drawable.silver); // Set icon for unlocked state
        } else {
            silver.setVisibility(View.GONE); // Hide if no achievement
        }

        if (achievementList.get(2)) {
            gold.setVisibility(View.VISIBLE); // Unlocked gold icon
            gold.setImageResource(R.drawable.gold); // Set icon for unlocked state
        } else {
            gold.setVisibility(View.GONE); // Hide if no achievement
        }

        if (achievementList.get(3)) {
            diamond.setVisibility(View.VISIBLE); // Unlocked diamond icon
            diamond.setImageResource(R.drawable.diamond); // Set icon for unlocked state
        } else {
            diamond.setVisibility(View.GONE); // Hide if no achievement
        }
    }

    private void updateUserOnServer(User user, String successMessage) {
        new Thread(() -> {
            userServerRepository.updateUser(user, new UserServerRepository.OnUserUpdateCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> {
                        Toast.makeText(ProfileActivity.this, successMessage, Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onFailure(String errorMessage) {
                    runOnUiThread(() -> showError("Server update failed: " + errorMessage));
                }
            });
        }).start(); // Run this on a background thread
    }

    private void showError(String errorMessage) {
        runOnUiThread(() -> {
            Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

        });
    }
    private int calculateAge(LocalDate birthDate) {
        Calendar birth = Calendar.getInstance();

        int year = birth.get(Calendar.YEAR);
        int month = birth.get(Calendar.MONTH);
        int day = birth.get(Calendar.DAY_OF_MONTH);

        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR);

        // Check if the birthday has occurred this year or not
        if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }

    private void setupButtonListeners() {
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id_item = item.getItemId();
                if (id_item == R.id.home_BottomIcon) {
                    navigateTo(HomePageActivity.class);
                    return true;
                } else if (id_item == R.id.plan_BottomIcon) {
                    navigateTo(MyPlanActivity.class);
                    return true;
                } else if (id_item == R.id.workouts_BottomIcon) {
                    navigateTo(WorkoutsFilterActivity.class);
                    return true;
                } else if (id_item == R.id.profile_BottomIcon) {

                    return true;
                } else
                    return false;

            }
        });
    }

    private void navigateTo(Class<?> targetActivity) {
        startActivity(new Intent(ProfileActivity.this, targetActivity));
    }

    private void validateEmail() {
        String emailInput = emailEditText.getText().toString().trim();

        if (emailInput.isEmpty()) {
            emailEditText.setError("Email cannot be empty");
        } else if (!emailInput.contains("@")) {
            emailEditText.setError("Missing @ symbol");
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailEditText.setError("Invalid email format");
        }
    }

    // Show or hide the EditTexts
    private void setEditTextsVisibility(int visibility) {
        heightEditText.setVisibility(visibility);
        weightEditText.setVisibility(visibility);
        phoneEditText.setVisibility(visibility);
        dateEditText.setVisibility(visibility);
        emailEditText.setVisibility(visibility);
        nameEditText.setVisibility(visibility);
    }

    private void savePhoto(Bitmap photo) {

        if (isExternalStorageWritable()) {
            File photoFile = new File(getExternalFilesDir(null), "profile_" + System.currentTimeMillis() + ".jpg");
            try (FileOutputStream fos = new FileOutputStream(photoFile)) {
                photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                photoPath = photoFile.getAbsolutePath();
                Log.d("ProfileActivity", "Photo saved at: " + photoPath);
                Toast.makeText(this, "Photo saved", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to save photo", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "External storage not available", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


    /*private void checkCameraHardwareAndRequestPermission() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                launchCamera();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.);
            }
        } else {
            Toast.makeText(this, "This device does not have a camera", Toast.LENGTH_SHORT).show();
        }
    }
    private void launchCamera() {
        try {
            Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraActivityResultLauncher.launch(camIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryActivityResultLauncher.launch(intent);
    }
    private void saveProfileImageChanges() {
        // Get the values from the EditTexts
        String updatedName = nameEditText.getText().toString();


        // Save the profile picture
        if (profilePhoto != null) {
            savePhoto(profilePhoto);
            HomePageActivity.user.setProfilePicture(photoPath);  // Save path to user object
        }

        // Update the TextViews and show a Toast
        userNameTextView.setText(updatedName);
        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
    }


    // Save the changes made by the user
    private void saveProfileChanges() {
        // Get the values from the EditTexts
        String updatedName = nameEditText.getText().toString();
        String updatedEmail = emailEditText.getText().toString();
        String updatedPhone = phoneEditText.getText().toString();
        String updatedHeight = heightEditText.getText().toString();
        String updatedWeight = weightEditText.getText().toString();
        String updatedDate = dateEditText.getText().toString();



        // Validate the fields
        if (updatedHeight.isEmpty() || updatedWeight.isEmpty() || updatedPhone.isEmpty()) {
            Toast.makeText(this, "All fields must be filled out", Toast.LENGTH_SHORT).show();
            return;
        }

        Converters converters = new Converters();
        LocalDate updatedLocalDate = converters.toLocalDate(updatedDate);
        if (updatedLocalDate == null) {
            Toast.makeText(this, "Invalid date format. Please use the correct format (e.g., YYYY-MM-DD).", Toast.LENGTH_SHORT).show();
            return; // Stop further processing if the date is invalid
        }

        // Save the changes
        // Update fields first
        HomePageActivity.user.setFullName(updatedName);
        HomePageActivity.user.setEmailAddress(updatedEmail);
        HomePageActivity.user.setBirthday(updatedLocalDate);

        try {
            long phone = Long.parseLong(updatedPhone);
            double height = Double.parseDouble(updatedHeight);
            double weight = Double.parseDouble(updatedWeight);

            // Update user fields only if parsing is successful
            HomePageActivity.user.setPhoneNum(phone);
            HomePageActivity.user.setHeight(height);
            HomePageActivity.user.setWeight(weight);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format. Please enter valid data.", Toast.LENGTH_SHORT).show();
            return; // Stop the process if data is invalid
        }


        // Then send a single update to the server
        updateUserOnServer(HomePageActivity.user, "Profile updated successfully!");;

        //  update the TextViews to reflect the changes
        userNameTextView.setText(updatedName);
        userMailTextView.setText(updatedEmail);
        userPhoneTextView.setText("Phone: " + updatedPhone);
        userHeightTextView.setText("Height: " + updatedHeight);
        userWeightTextView.setText("Weight: " + updatedWeight);
        userBirthdateTextView.setText("Birthday: " + updatedDate);

        //  show a Toast message to indicate success
        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        Executors.newSingleThreadExecutor().execute(() -> {
            // Save changes to Room (local database)
            userRoomRepository.updateUserLocally(HomePageActivity.user);
        });


    }



    }

