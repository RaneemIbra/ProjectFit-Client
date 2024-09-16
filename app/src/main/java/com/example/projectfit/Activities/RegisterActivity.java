package com.example.projectfit.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectfit.Models.User;
import com.example.projectfit.Room.Repositories.UserRoomRepository;
import com.example.projectfit.Server.Repositories.UserServerRepository;
import com.example.projectfit.Utils.Validation;
import com.example.projectfit.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout fullNameLayout, emailLayout, passwordLayout, birthDateLayout, heightLayout, weightLayout, answerLayout;
    private TextInputEditText birthDateEditText;
    private Button registerButton;
    private Validation validation;
    private String selectedQuestion;
    private TextView signInText;
    private Spinner securityQuestionSpinner;
    private UserRoomRepository userRoomRepository;
    private UserServerRepository userServerRepository;
    private LocalDate selectedBirthDate;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeRepositories();
        initializeViews();
        setupListeners();
    }

    private void initializeRepositories() {
        userRoomRepository = new UserRoomRepository(this);
        userServerRepository = new UserServerRepository();
        validation = new Validation();
        executorService = Executors.newSingleThreadExecutor();
    }

    private void initializeViews() {
        fullNameLayout = findViewById(R.id.nameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        heightLayout = findViewById(R.id.HeightLayout);
        weightLayout = findViewById(R.id.WeightLayout);
        answerLayout = findViewById(R.id.AnswerLayout);
        securityQuestionSpinner = findViewById(R.id.spinner);
        birthDateLayout = findViewById(R.id.birthDateLayout);
        birthDateEditText = findViewById(R.id.birthDate);
        registerButton = findViewById(R.id.register_button);
        signInText = findViewById(R.id.signInText);
    }

    private void setupListeners() {
        setupSpinnerListener();
        birthDateEditText.setOnClickListener(v -> showDatePickerDialog());
        registerButton.setOnClickListener(view -> handleRegisterClick());
        signInText.setOnClickListener(view -> navigateToActivity(LoginActivity.class));
    }

    private void setupSpinnerListener() {
        securityQuestionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedQuestion = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedQuestion = null;
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedMonth++;
                    String birthDateString = selectedDay + "/" + selectedMonth + "/" + selectedYear;
                    birthDateEditText.setText(birthDateString);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        selectedBirthDate = LocalDate.of(selectedYear, selectedMonth, selectedDay);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void handleRegisterClick() {
        String fullNameText = getTextFromInput(fullNameLayout);
        String emailText = getTextFromInput(emailLayout);
        String passwordText = getTextFromInput(passwordLayout);
        String heightText = getTextFromInput(heightLayout);
        String weightText = getTextFromInput(weightLayout);
        String answerText = getTextFromInput(answerLayout);

        if (isValidInput(fullNameText, emailText, passwordText, heightText, weightText, answerText)) {
            executorService.execute(() -> {
                userServerRepository.getUserByEmail(emailText, new retrofit2.Callback<User>() {
                    @Override
                    public void onResponse(retrofit2.Call<User> call, retrofit2.Response<User> response) {
                        runOnUiThread(() -> {
                            if (response.isSuccessful() && response.body() != null) {
                                emailLayout.setError("This email is already registered.");
                                Toast.makeText(RegisterActivity.this, "User already exists with this email on the server.", Toast.LENGTH_SHORT).show();
                            } else {
                                User newUser = new User(
                                        null,
                                        fullNameText,
                                        null,
                                        emailText,
                                        passwordText,
                                        selectedBirthDate,
                                        Double.parseDouble(heightText),
                                        Double.parseDouble(weightText),
                                        true,
                                        selectedQuestion,
                                        answerText,
                                        null,
                                        null,
                                        "",
                                        null,
                                        null,
                                        null,
                                        null
                                );

                                userServerRepository.addUserInServer(newUser);
                                userRoomRepository.addUserLocally(newUser);

                                Toast.makeText(RegisterActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                navigateToActivity(LoginActivity.class);
                            }
                        });
                    }

                    @Override
                    public void onFailure(retrofit2.Call<User> call, Throwable t) {
                        runOnUiThread(() -> {
                            Toast.makeText(RegisterActivity.this, "Server error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            });
        }
    }


    private boolean isValidInput(String fullNameText, String emailText, String passwordText, String heightText, String weightText, String answerText) {
        boolean isFullNameValid = validation.isNameValid(fullNameText, fullNameLayout);
        boolean isEmailValid = validation.emailValidate(emailText, emailLayout);
        boolean isPasswordValid = validation.passwordValidate(passwordText, passwordLayout);
        boolean isHeightValid = validation.heightValidate(heightText, heightLayout);
        boolean isWeightValid = validation.weightValidate(weightText, weightLayout);
        boolean isAnswerValid = validation.ValidateText(answerText, answerLayout);
        boolean isQuestionSelected = validateSpinnerSelection();
        boolean isBirthDateValid = selectedBirthDate != null;

        if (!isBirthDateValid) {
            birthDateLayout.setError("Please select a birth date");
        } else {
            birthDateLayout.setErrorEnabled(false);
        }
        isBirthDateValid = validation.isAgeValid(selectedBirthDate, birthDateLayout);

        return isFullNameValid && isEmailValid && isPasswordValid && isHeightValid && isWeightValid && isAnswerValid && isQuestionSelected && isBirthDateValid;
    }

    private String getTextFromInput(TextInputLayout textInputLayout) {
        TextInputEditText editText = (TextInputEditText) textInputLayout.getEditText();
        return editText != null ? editText.getText().toString() : "";
    }

    private boolean validateSpinnerSelection() {
        if (securityQuestionSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a security question", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void navigateToActivity(Class<?> targetActivity) {
        Intent intent = new Intent(RegisterActivity.this, targetActivity);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
