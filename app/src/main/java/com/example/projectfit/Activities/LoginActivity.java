package com.example.projectfit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectfit.Models.User;
import com.example.projectfit.R;
import com.example.projectfit.Room.Repositories.UserRoomRepository;
import com.example.projectfit.Server.Repositories.UserServerRepository;
import com.example.projectfit.Utils.Validation;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout emailLayout, passwordLayout;
    private Button loginButton;
    private TextView signUpText, forgotPasswordText;
    private CheckBox rememberMeCheckBox;
    private Validation validation;
    private UserRoomRepository userRoomRepository;
    private UserServerRepository userServerRepository;

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

        initializeRepositories();
        initializeViews();
        setupClickListeners();
    }

    private void initializeRepositories() {
        userRoomRepository = new UserRoomRepository(this);
        userServerRepository = new UserServerRepository();
        validation = new Validation();
    }

    private void initializeViews() {
        emailLayout = findViewById(R.id.emailLayoutLogin);
        passwordLayout = findViewById(R.id.passwordLayoutLogin);
        loginButton = findViewById(R.id.login_button);
        signUpText = findViewById(R.id.signUp_text);
        forgotPasswordText = findViewById(R.id.password_restTxt);
        rememberMeCheckBox = findViewById(R.id.remember_me_checkbox);
    }

    private void setupClickListeners() {
        signUpText.setOnClickListener(view -> navigateToActivity(RegisterActivity.class));
        forgotPasswordText.setOnClickListener(view -> navigateToActivity(ResetPasswordActivity.class));
        loginButton.setOnClickListener(view -> handleLogin());
    }

    private void navigateToActivity(Class<?> targetActivity) {
        Intent intent = new Intent(LoginActivity.this, targetActivity);
        startActivity(intent);
    }

    private void handleLogin() {
        String email = getTextFromInput(emailLayout);
        String password = getTextFromInput(passwordLayout);

        if (validation.emailValidate(email, emailLayout) && validation.passwordValidate(password, passwordLayout)) {
            userRoomRepository.validateUserLocal(email, password, new UserRoomRepository.OnUserValidationCallback() {
                @Override
                public void onSuccess(User user) {
                    showLoginSuccessMessage("Login successful (Local)");
                }

                @Override
                public void onFailure(String errorMessage) {
                    checkUserOnServer(email, password);
                }
            });
        }
    }

    private void checkUserOnServer(String email, String password) {
        userServerRepository.validateUserServer(email, password, new UserServerRepository.OnUserValidationCallback() {
            @Override
            public void onSuccess(User user) {
                showLoginSuccessMessage("Login successful (Server)");
            }

            @Override
            public void onFailure(String errorMessage) {
                showToastMessage(errorMessage);
            }
        });
    }

    private void showLoginSuccessMessage(String message) {
        runOnUiThread(() -> {
            showToastMessage(message);
            navigateToHomePage();
        });
    }

    private void showToastMessage(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToHomePage() {
        Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
        startActivity(intent);
        finish();
    }

    private String getTextFromInput(TextInputLayout textInputLayout) {
        EditText editText = textInputLayout.getEditText();
        return editText != null ? editText.getText().toString().trim() : "";
    }
}
