package com.example.projectfit.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ResetPasswordActivity extends AppCompatActivity {
    private TextInputLayout emailLayout, answerLayout, passwordLayout, confirmPasswordLayout;
    private Spinner securityQuestionSpinner;
    private Button resetButton;
    private Validation validation;
    private UserRoomRepository userRoomRepository;
    private UserServerRepository userServerRepository;
    private String selectedSecurityQuestion;
    private ExecutorService executorService;

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

        initializeViews();
        initializeRepositories();

        resetButton.setOnClickListener(view -> handleResetPassword());
    }

    private void initializeViews() {
        emailLayout = findViewById(R.id.emailLayout);
        answerLayout = findViewById(R.id.AnswerLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);
        securityQuestionSpinner = findViewById(R.id.spinner);
        resetButton = findViewById(R.id.Reset_button);
    }

    private void initializeRepositories() {
        userRoomRepository = new UserRoomRepository(this);
        userServerRepository = new UserServerRepository();
        validation = new Validation();
        executorService = Executors.newSingleThreadExecutor();
    }

    private void handleResetPassword() {
        String email = getTextFromInput(emailLayout);
        String answer = getTextFromInput(answerLayout);
        String newPassword = getTextFromInput(passwordLayout);
        String confirmPassword = getTextFromInput(confirmPasswordLayout);
        selectedSecurityQuestion = securityQuestionSpinner.getSelectedItem().toString();

        if (!validateInput(email, answer, newPassword, confirmPassword)) {
            return;
        }

        resetButton.setEnabled(false);

        executorService.execute(() -> {
            userRoomRepository.validateUserLocalByAnswer(email, answer, new UserRoomRepository.OnUserValidationCallback() {
                @Override
                public void onSuccess(User user) {
                    user.setPassword(newPassword);
                    userRoomRepository.updateUserLocally(user);
                    updateUserOnServer(user, "Password reset successfully (Local)");
                }

                @Override
                public void onFailure(String errorMessage) {
                    checkUserOnServer(email, answer, newPassword);
                }
            });
        });
    }

    private boolean validateInput(String email, String answer, String newPassword, String confirmPassword) {
        boolean isValid = true;

        if (!validation.emailValidate(email, emailLayout)) {
            isValid = false;
        }
        if (!validation.ValidateText(answer, answerLayout)) {
            isValid = false;
        }
        if (!validation.passwordValidate(newPassword, passwordLayout)) {
            isValid = false;
        }
        if (!newPassword.equals(confirmPassword)) {
            confirmPasswordLayout.setError("Passwords do not match");
            isValid = false;
        }

        return isValid;
    }

    private void checkUserOnServer(String email, String answer, String newPassword) {
        userServerRepository.validateUserServerByAnswer(email, answer, new UserServerRepository.OnUserValidationCallback() {
            @Override
            public void onSuccess(User user) {
                user.setPassword(newPassword);
                updateUserOnServer(user, "Password reset successfully (Server)");
            }

            @Override
            public void onFailure(String errorMessage) {
                showError(errorMessage);
            }
        });
    }

    private void updateUserOnServer(User user, String successMessage) {
        userServerRepository.updateUser(user, new UserServerRepository.OnUserUpdateCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(ResetPasswordActivity.this, successMessage, Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                showError("Server update failed: " + errorMessage);
            }
        });
    }

    private void showError(String errorMessage) {
        runOnUiThread(() -> {
            Toast.makeText(ResetPasswordActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            resetButton.setEnabled(true);
        });
    }

    private String getTextFromInput(TextInputLayout textInputLayout) {
        return textInputLayout.getEditText() != null ? textInputLayout.getEditText().getText().toString().trim() : "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
