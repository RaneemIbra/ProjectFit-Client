package com.example.projectfit.Activities;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectfit.R;
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

public class ProfileFragment extends Fragment {
    private EditText emailEditText, phoneEditText, heightEditText, weightEditText, dateEditText, nameEditText;
    private Button UpdateProfileButton, editDetailsButton, logoutButton;
    private TextView userNameTextView, userMailTextView, userAgeTextView, userHeightTextView,
            userWeightTextView, userBirthdateTextView, userPhoneTextView;
    private boolean isEditing = false; // To track whether we are in edit mode
    private ImageView profileImageView;
    BottomNavigationView bottomBar;
    private ImageButton selectFromGalleryButton;
    private Bitmap profilePhoto;
    private String photoPath;
    private UserServerRepository userServerRepository;
    private UserRoomRepository userRoomRepository;
    User user;


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
           View view = inflater.inflate(R.layout.fragment_profile, container, false);
        user = getUserFromSharedPreferences();
        userServerRepository = new UserServerRepository();
        userRoomRepository = new UserRoomRepository(getContext());
        profileImageView = view.findViewById(R.id.profileImageView);
        heightEditText = view.findViewById(R.id.heightEditText);
        weightEditText = view.findViewById(R.id.weightEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        dateEditText = view.findViewById(R.id.dateEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        nameEditText = view.findViewById(R.id.nameEditText);
        editDetailsButton = view.findViewById(R.id.editDetailsButton);
        UpdateProfileButton = view.findViewById(R.id.UpdateProfileButton);
        logoutButton = view.findViewById(R.id.logoutButton);
        userNameTextView = view.findViewById(R.id.userNameTextView);
        userMailTextView = view.findViewById(R.id.r9t1dlwyyn1);
        userBirthdateTextView = view.findViewById(R.id.rbd44z6os1qv);
        userHeightTextView = view.findViewById(R.id.rdmhc6s0vgl4);
        userWeightTextView = view.findViewById(R.id.ritmf488dqb);
        userAgeTextView = view.findViewById(R.id.r9ox48lkbrn);
        userPhoneTextView = view.findViewById(R.id.userPhoneTextView);
        // Initialize other elements (medals, gallery button, etc.)
        selectFromGalleryButton = view.findViewById(R.id.selectFromGalleryButton);


        selectFromGalleryButton.setOnClickListener(v -> openGallery());
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
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
            startActivity(intent);
            getActivity().finish();
        });

           initViews();
           return view;

    }

    private void initViews() {
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

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryActivityResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    try {
                        InputStream imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                        profilePhoto = BitmapFactory.decodeStream(imageStream);

                        byte[] profilePictureBytes = convertBitmapToByteArray(profilePhoto);
                        user.setProfilePicture(profilePictureBytes);

                        profileImageView.setImageBitmap(profilePhoto);

                        // Update user in the local database and server
                        Executors.newSingleThreadExecutor().execute(() -> userRoomRepository.updateUserLocally(user));
                        updateUserOnServer(user, "Profile picture updated successfully!");

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Failed to load image from gallery", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "All fields must be filled out", Toast.LENGTH_SHORT).show();
            return;
        }

        Converters converters = new Converters();
        LocalDate updatedLocalDate = converters.toLocalDate(updatedDate);
        if (updatedLocalDate == null) {
            Toast.makeText(getContext(), "Invalid date format. Please use the correct format (YYYY-MM-DD).", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "Invalid number format. Please enter valid data.", Toast.LENGTH_SHORT).show();
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

        Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
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

    private User getUserFromSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", MODE_PRIVATE);
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