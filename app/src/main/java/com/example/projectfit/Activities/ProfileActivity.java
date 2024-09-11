package com.example.projectfit.Activities;

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
import java.util.List;
import java.util.concurrent.Executors;

public class ProfileActivity extends AppCompatActivity {
    private EditText emailEditText, phoneEditText, heightEditText, weightEditText, dateEditText, nameEditText;
    private Button UpdateProfileButton, editDetailsButton, logoutButton;
    private TextView userNameTextView, userMailTextView, userAgeTextView, userHeightTextView,
            userWeightTextView, userBirthdateTextView, userPhoneTextView;
    private boolean isEditing = false;
    private ImageView profileImageView;
    BottomNavigationView bottomBar;
    private ImageButton selectFromGalleryButton;
    private Bitmap profilePhoto;
    private String photoPath;
    private UserServerRepository userServerRepository;
    private UserRoomRepository userRoomRepository;
    User user;

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
        initViews();
        userServerRepository = new UserServerRepository();
        userRoomRepository = new UserRoomRepository(this);

        // Initialize other elements (medals, gallery button, etc.)
        selectFromGalleryButton = findViewById(R.id.selectFromGalleryButton);
        selectFromGalleryButton.setOnClickListener(v -> openGallery());

        setupButtonListeners();
        editDetailsButton.setOnClickListener(v -> {
            setEditTextsVisibility(View.VISIBLE);
            UpdateProfileButton.setVisibility(View.VISIBLE);
            editDetailsButton.setVisibility(View.GONE);
        });

        UpdateProfileButton.setOnClickListener(v -> {
            if (isEditing) {
                saveProfileChanges();
                setEditTextsVisibility(View.GONE);
                UpdateProfileButton.setText("Update Profile");
                isEditing = false;
            } else {
                setEditTextsVisibility(View.VISIBLE);
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
        emailEditText = findViewById(R.id.emailEditText);
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
        String updatedName = nameEditText.getText().toString();
        String updatedEmail = emailEditText.getText().toString();
        String updatedPhone = phoneEditText.getText().toString();
        String updatedHeight = heightEditText.getText().toString();
        String updatedWeight = weightEditText.getText().toString();
        String updatedDate = dateEditText.getText().toString();

        if (updatedHeight.isEmpty() || updatedWeight.isEmpty() || updatedPhone.isEmpty()) {
            Toast.makeText(this, "All fields must be filled out", Toast.LENGTH_SHORT).show();
            return;
        }

        Converters converters = new Converters();
        LocalDate updatedLocalDate = converters.toLocalDate(updatedDate);
        if (updatedLocalDate == null) {
            Toast.makeText(this, "Invalid date format. Please use the correct format (YYYY-MM-DD).", Toast.LENGTH_SHORT).show();
            return;
        }

        user.setFullName(updatedName);
        user.setEmailAddress(updatedEmail);
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

        // Update the UI and local database
        updateUserOnServer(user, "Profile updated successfully!");
        Executors.newSingleThreadExecutor().execute(() -> userRoomRepository.updateUserLocally(user));

        userNameTextView.setText(updatedName);
        userMailTextView.setText(updatedEmail);
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
        emailEditText.setVisibility(visibility);
        nameEditText.setVisibility(visibility);
    }
}
