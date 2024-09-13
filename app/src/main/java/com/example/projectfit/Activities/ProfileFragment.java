package com.example.projectfit.Activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.projectfit.R;
import com.example.projectfit.Room.Repositories.UserRoomRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
        setupButtonListeners();
        return view;
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
            return gson.fromJson(userJson, userType);
        }
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
}
