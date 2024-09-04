package com.example.projectfit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.projectfit.Models.User;
import com.example.projectfit.R;
import com.example.projectfit.Room.Repositories.UserRoomRepository;
import com.example.projectfit.Server.Repositories.UserServerRepository;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private UserRoomRepository userRoomRepository;
    private UserServerRepository userServerRepository;

    EditText emailEditText,passwordEditText;
    Button signInButton;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailEditText = findViewById(R.id.email_input);
        passwordEditText = findViewById(R.id.password_input);
        signInButton = findViewById(R.id.login_button);

        userRoomRepository = new UserRoomRepository(getApplicationContext());
        userServerRepository = new UserServerRepository();

        signInButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                user = userRoomRepository.getUserByEmail(email);
                if (user != null && user.getPassword().equals(password)) {
                    runOnUiThread(() -> navigateToHome());
                } else {
                    userServerRepository.getUserByEmail(email, new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                User serverUser = response.body();
                                if (serverUser.getPassword().equals(password)) {
                                    navigateToHome();
                                } else {
                                    runOnUiThread(() -> showError("Invalid email or password"));
                                }
                            } else {
                                runOnUiThread(() -> showError("Invalid email or password"));
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            runOnUiThread(() -> showError("Error connecting to the server"));
                        }
                    });
                }
            });
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}


