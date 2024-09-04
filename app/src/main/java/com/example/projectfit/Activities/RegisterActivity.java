package com.example.projectfit.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextWatcher;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.projectfit.R;
import com.google.android.material.imageview.ShapeableImageView;

public class RegisterActivity extends AppCompatActivity {
    EditText nameEditText = findViewById(R.id.name_txt);
    EditText emailEditText = findViewById(R.id.email_txt);
    EditText passwordEditText = findViewById(R.id.password_txt);
    EditText weightEditText = findViewById(R.id.weight_txt);
    EditText heightEditText = findViewById(R.id.height_txt);
    Spinner spinner = findViewById(R.id.spinner);
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_items, android.R.layout.simple_spinner_item);
    Button login_button = findViewById(R.id.login_button);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView) findViewById(R.id.name_icon));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView) findViewById(R.id.email_icon));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView) findViewById(R.id.password_icon));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView) findViewById(R.id.password_visibility));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView) findViewById(R.id.weight_icon));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView) findViewById(R.id.height_icon));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView) findViewById(R.id.question_icon));

        // Set up username real-time feedback
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateNameInput(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Set up email real-time feedback
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmailInput(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
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

        // Set up weight real-time feedback
        weightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateWeightInput(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set up height real-time feedback
        heightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateHeightInput(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // security question spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // if The prompt item is selected, do nothing
                if (position != 0) {
                    // Handle selection of other items
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    Toast.makeText(RegisterActivity.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // login button
        login_button.setOnClickListener(view -> {
            // Create an Intent to start the new Activity
            nameEditText.setText("");
            emailEditText.setText("");
            passwordEditText.setText("");
            weightEditText.setText("");
            heightEditText.setText("");

            Intent intent = new Intent(this, LoadingScreenActivity.class);
            // Start the new Activity
            startActivity(intent);
        });

        // sing in text
        TextView SingIpTxt = findViewById(R.id.singIn_txt);
        SingIpTxt.setOnClickListener(view ->
        {
            // Create an Intent to start the new Activity
            Intent intent = new Intent(this, LoginActivity.class);
            // Start the new Activity
            startActivity(intent);
        });
    }

    // Method to validate name input in real-time
    void validateNameInput(String name) {
        // Check if name is non-empty
        if (name.isEmpty()) {
            ShowError("Name cannot be empty", nameEditText);
        }
        // Check if name contains only letters
        else if (!name.matches("[a-zA-Z]+")) {
            ShowError("Name must contain only letters", nameEditText);
        }
        // Check if name length is within the limit
        else if (name.length() > 20) {
            ShowError("Name cannot exceed 20 characters", nameEditText);
        }
        // If all conditions are met, reset the error state
        else {
            ResetErrorState(nameEditText);
        }
    }

    // Method to validate email input in real-time
    private void validateEmailInput(String email) {
        // Check if email is non-empty
        if (email.isEmpty()) {
            ShowError("Email cannot be empty", emailEditText);
        }
        // Check if email format is valid
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ShowError("Invalid email format", emailEditText);
        }
        // If all conditions are met, reset the error state
        else {
            ResetErrorState(emailEditText);
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

    // Method to validate weight input in real-time
    private void validateWeightInput(String weightStr) {
        // Check if weight is non-empty
        if (weightStr.isEmpty()) {
            ShowError("Weight cannot be empty", weightEditText);
        }
        // Check if weight is a valid number
        else {
            try {
                double weight = Double.parseDouble(weightStr);
                // Check if weight is within a reasonable range
                if (weight < 30 || weight > 300) {
                    ShowError("Weight must be between 30 and 300 kg", weightEditText);
                } else {
                    ResetErrorState(weightEditText);
                }
            } catch (NumberFormatException e) {
                ShowError("Please enter a valid number", weightEditText);
            }
        }
    }

    // Method to validate height input in real-time
    private void validateHeightInput(String heightStr) {
        // Check if height is non-empty
        if (heightStr.isEmpty()) {
            ShowError("Height cannot be empty", heightEditText);
        }
        // Check if height is a valid number
        else {
            try {
                double height = Double.parseDouble(heightStr);
                // Check if height is within a reasonable range
                if (height < 50 || height > 250) {
                    ShowError("Height must be between 50 and 250 cm", heightEditText);
                } else {
                    ResetErrorState(heightEditText);
                }
            } catch (NumberFormatException e) {
                ShowError("Please enter a valid number", heightEditText);
            }
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
