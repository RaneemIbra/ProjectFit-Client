package com.example.projectfit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectfit.R;

public class ProfileActivity extends AppCompatActivity {
    private EditText emailEditText, phoneEditText, heightEditText, weightEditText, dateEditText, nameEditText;
    private Button editProfileButton, homePageBtn, planBtn;

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
        setupButtonListeners();
    }

    private void initViews() {
        heightEditText = findViewById(R.id.heightEditText);
        weightEditText = findViewById(R.id.weightEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        dateEditText = findViewById(R.id.dateEditText);
        emailEditText = findViewById(R.id.emailEditText);
        nameEditText = findViewById(R.id.nameEditText);

        editProfileButton = findViewById(R.id.editProfileButton);
        homePageBtn = findViewById(R.id.home_button);
        planBtn = findViewById(R.id.plan_button);
    }

    private void setupButtonListeners() {
        homePageBtn.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, HomePageActivity.class)));
        planBtn.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, MyPlanActivity.class)));
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
}
