package com.example.projectfit.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.projectfit.R;
import com.google.android.material.imageview.ShapeableImageView;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText passwordEditText = findViewById(R.id.password_txt);
    EditText confirmedPasswordEditText = findViewById(R.id.confirmed_password_txt);
    Button reset_button = findViewById(R.id.Reset_button);
    EditText emailEditText = findViewById(R.id.email_txt);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up password real-time feedback
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePasswordInput(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        reset_button.setOnClickListener(view -> {
            passwordEditText.setText("");
            confirmedPasswordEditText.setText("");
            checkPasswords();
        });
    }

    private void checkPasswords() {
        String newPassword = passwordEditText.getText().toString();
        String confirmPassword = confirmedPasswordEditText.getText().toString();

        if (newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Reset successful", Toast.LENGTH_SHORT).show();
            // Return to the sign-in page (assume it's MainActivity)
            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Finish this activity so the user can't go back to it
        } else {
            Toast.makeText(this, "The two passwords do not match", Toast.LENGTH_SHORT).show();
        }
    }
    // Method to validate password input in real-time
    private void validatePasswordInput(String password) {
        // Check if password is non-empty
        if (password.isEmpty()) {
            ShowError("Password cannot be empty", passwordEditText);
        }
        // Check if password length is at least 8 characters
        else if (password.length() < 8) {
            ShowError("Password must be at least 8 characters", passwordEditText);
        }
        // Check if password contains both letters and numbers
        else if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d).+$")) {
            ShowError("Password must contain both letters and numbers", passwordEditText);
        }
        // If all conditions are met, reset the error state
        else {
            ResetErrorState(passwordEditText);
        }
    }
    // Method to show error (Toast and change background to red)
    void ShowError(String message, EditText editText) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        editText.setBackgroundColor(Color.RED);
    }

    // Method to reset the error state (change background to white)
    void ResetErrorState(EditText editText) {
        editText.setBackgroundColor(Color.WHITE);
    }

}