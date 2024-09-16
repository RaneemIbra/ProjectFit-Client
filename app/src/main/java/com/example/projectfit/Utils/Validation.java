package com.example.projectfit.Utils;

import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;

public class Validation {
    public boolean ValidateText(String Text, TextInputLayout input) {
        if (Text.isEmpty()) {
            input.setError("Field can't be empty");
            return false;
        }else{
            input.setErrorEnabled(false);
            return true;
        }
    }

    public boolean passwordValidate(String passwordText, TextInputLayout password) {
        if (passwordText.isEmpty()) {
            password.setError("Field can't be empty");
            return false;
        }else if(passwordText.length()<8){
            password.setError("password can't be less than 8 characters");
            return false;
        }else{
            password.setErrorEnabled(false);
            return true;
        }
    }

    public boolean heightValidate(String heightText, TextInputLayout heightLayout) {
        if (heightText.isEmpty()) {
            heightLayout.setError("Field can't be empty");
            return false;
        }
        try {
            double heightValue = Double.parseDouble(heightText);
            if (heightValue < 60 || heightValue > 270) {
                heightLayout.setError("Height should be between 60 and 270 cm");
                return false;
            } else {
                heightLayout.setErrorEnabled(false);
                return true;
            }
        } catch (NumberFormatException e) {
            heightLayout.setError("Invalid height");
            return false;
        }
    }

    public boolean weightValidate(String weightText, TextInputLayout weightLayout) {
        if (weightText.isEmpty()) {
            weightLayout.setError("Field can't be empty");
            return false;
        }
        try {
            double weightValue = Double.parseDouble(weightText);
            if (weightValue < 30 || weightValue > 130) {
                weightLayout.setError("Weight should be between 30 and 130 kg");
                return false;
            } else {
                weightLayout.setErrorEnabled(false);
                return true;
            }
        } catch (NumberFormatException e) {
            weightLayout.setError("Invalid weight");
            return false;
        }
    }

    public boolean emailValidate(String emailText, TextInputLayout email) {
        if (emailText.isEmpty()) {
            email.setError("Field can't be empty");
            return false;
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            email.setError("Invalid Email");
            return false;
        }
        else{
            email.setErrorEnabled(false);
            return true;
        }
    }

    public boolean isNameValid(String fullName, TextInputLayout fullNameLayout) {
        if(!fullName.matches("[a-zA-Z\\s]+")){
            fullNameLayout.setError("Invalid name");
            return false;
        }
        return true;
    }

    public boolean isAgeValid(LocalDate birthDate, TextInputLayout birthDateLayout) {
        if (birthDate == null) {
            birthDateLayout.setError("Invalid birthday");
            return false;
        }

        LocalDate currentDate = null;
        int age = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();

            age = currentDate.getYear() - birthDate.getYear();

            if ((birthDate.getMonthValue() > currentDate.getMonthValue()) ||
                    (birthDate.getMonthValue() == currentDate.getMonthValue() && birthDate.getDayOfMonth() > currentDate.getDayOfMonth())) {
                age--;
            }
        }
        if(!(age >= 8 && age <= 70)){
            birthDateLayout.setError("Age should be between 8 and 70");
            return false;
        }
        return true;
    }
}
