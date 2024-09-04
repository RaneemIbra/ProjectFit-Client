package com.example.projectfit.Utils;

import com.google.android.material.textfield.TextInputLayout;

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

    public boolean Height_Weight_Validate(String value, TextInputLayout Height_Weight) {
        if (value.isEmpty()) {
            Height_Weight.setError("Field can't be empty");
            return false;
        }else if(value.length()>3){
            Height_Weight.setError("value can't be more than 10 characters");
            return false;
        }
        else{
            Height_Weight.setErrorEnabled(false);
            return true;
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
}
