package com.example.projectfit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.projectfit.R;
import com.google.android.material.imageview.ShapeableImageView;

public class LoginActivity extends AppCompatActivity {

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
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rikvg0t9izk));
        TextView password_restTxt = findViewById(R.id.password_restTxt);
        password_restTxt.setOnClickListener(view -> {
            // Create an Intent to start the new Activity
            Intent intent = new Intent(this, ResetPasswordActivity.class);
            // Start the new Activity
            startActivity(intent);
        });
        TextView SingUpTxt = findViewById(R.id.signUp_text);
        SingUpTxt.setOnClickListener(view ->
        {
                    // Create an Intent to start the new Activity
                    Intent intent = new Intent(this, RegisterActivity.class);
                    // Start the new Activity
                    startActivity(intent);
        });

        Button singIn_Button = findViewById(R.id.login_button);
        singIn_Button.setOnClickListener(v->{
            // missing checking validations + toasts if incorrect input
            Intent intent = new Intent(this, HomePageActivity.class);
            // Start the new Activity
            startActivity(intent);
        });
        

    }
}


