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

import com.bumptech.glide.Glide;
import com.example.projectfit.R;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfileActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText heightEditText;
    private EditText weightEditText;
    private EditText dateEditText;
    private EditText nameEditText;
    private Button editProfileButton;
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


        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rew4om8nwfp9));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.r1dt24381yha));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.r9ytoxqfuc0m));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rjaltsn56sbf));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rmw7gh2fx21g));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rmwt9q7s677o));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.raltse61lt9h));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rltnfh9vdac9));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.r9hnczojci8l));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rihtyk0bypl));
        // Find and set up EditText views
         heightEditText = findViewById(R.id.heightEditText);
         weightEditText = findViewById(R.id.weightEditText);
         phoneEditText = findViewById(R.id.phoneEditText);
         dateEditText = findViewById(R.id.dateEditText);
         emailEditText = findViewById(R.id.emailEditText);
         nameEditText = findViewById(R.id.nameEditText);


         editProfileButton = findViewById(R.id.editProfileButton);
        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HomePageActivity.class);
            startActivity(intent);
        });
        Button planButton = findViewById(R.id.planButton);
        planButton.setOnClickListener(v -> {
            Intent intent2= new Intent(ProfileActivity.this, MyPlanActivity.class);
            startActivity(intent2);
        });
        // Prevent re-navigating to the same activity
        if (!(ProfileActivity.this instanceof ProfileActivity)) {
            Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        //activity transition animations  to enhance the user experience further when navigating
        // between pages.
        Intent intent = new Intent(ProfileActivity.this, HomePageActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        //activity transition animations  to enhance the user experience further when
        // navigating between pages.
        Intent intent2 = new Intent(ProfileActivity.this, MyPlanActivity.class);
        startActivity(intent2);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


    }

    private void validateEmail() {
        String emailInput = emailEditText.getText().toString();

        if (emailInput.isEmpty()) {
            emailEditText.setError("Email cannot be empty");
        } else if (!emailInput.contains("@")) {
            emailEditText.setError("Missing @ symbol");
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailEditText.setError("Invalid email format");
        }

    }
}