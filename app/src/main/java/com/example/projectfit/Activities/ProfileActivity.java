package com.example.projectfit.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.projectfit.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {
    private EditText emailEditText, phoneEditText, heightEditText, weightEditText, dateEditText, nameEditText;
    private Button UpdateProfileButton, homePageBtn, planBtn,editDetailsButton,logoutButton;
    private TextView userNameTextView , userMailTextView , userAgeTextView , userHeightTextView ,
            userWeightTextView , userBirthdateTextView , userPhoneTextView ;
    private boolean isEditing = false; // To track whether we are in edit mode

    BottomNavigationView bottomBar;

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
         editDetailsButton = findViewById(R.id.editDetailsButton);
        UpdateProfileButton = findViewById(R.id.UpdateProfileButton);
        homePageBtn = findViewById(R.id.home_button);
        planBtn = findViewById(R.id.plan_button);
        logoutButton = findViewById(R.id.logoutButton);
        bottomBar = findViewById(R.id.bottom_navigation);


        // setting the current user name
         userNameTextView = findViewById(R.id.userNameTextView);
        // Get the current user's name
        String currentUserName = HomePageActivity.user.getFullName();
       // Set the name to the TextView
        userNameTextView.setText(currentUserName);


        //setting the current user mail
        userMailTextView = findViewById(R.id.r9t1dlwyyn1);
        // Get the current user's mail
        String currentUserMail = HomePageActivity.user.getEmailAddress();
        // Set the mail to the TextView
        userMailTextView.setText(currentUserMail);



        //setting the current user birthdayDate
        userBirthdateTextView = findViewById(R.id.rbd44z6os1qv);
        // Get the current user's birthdayDate
        String currentUserBirthdate = String.valueOf(HomePageActivity.user.getBirthday());
        // Set the birthday date to the TextView
        userBirthdateTextView.setText("Birthday: " + currentUserBirthdate);


      // setting the current user height
        userHeightTextView = findViewById(R.id.rdmhc6s0vgl4);
        // Get the current user's height
        String currentUserHeight = String.valueOf(HomePageActivity.user.getHeight());
        // Set the height to the TextView
        userHeightTextView.setText("Height: " + currentUserHeight);



        // setting the current user weight
        userWeightTextView = findViewById(R.id.ritmf488dqb);
        // Get the current user's weight
        String currentUserWeight = String.valueOf(HomePageActivity.user.getWeight());
        // Set the weight to the TextView
        userWeightTextView.setText("Weight: " + currentUserWeight);



        //setting the current user age
        userAgeTextView = findViewById(R.id.r9ox48lkbrn);
        // Ensure that HomePageActivity.user is not null and getBirthday() returns a valid Date
        if (HomePageActivity.user != null && HomePageActivity.user.getBirthday() != null) {
            LocalDate birthDate = HomePageActivity.user.getBirthday();

            // Calculate the age from the birthday
            int age = calculateAge(birthDate);

            // Set the age to the TextView
            userAgeTextView.setText("Age: " + age);
        } else {
            // Handle the case where user or birthDate is null
            userAgeTextView.setText("Age: Not Available");
        }



     // Find the TextView for the phone number
        userPhoneTextView = findViewById(R.id.userPhoneTextView);

       // Ensure that HomePageActivity.user is not null and getPhoneNumber() returns a valid phone number
        if (HomePageActivity.user != null && HomePageActivity.user.getPhoneNum() != null) {
            String phoneNumber = String.valueOf(HomePageActivity.user.getPhoneNum());

            // Set the phone number to the TextView
            userPhoneTextView.setText("Phone: " + phoneNumber);
        } else {
            // Handle the case where the phone number is not available
            userPhoneTextView.setText("Phone: Not Available");
        }

        // In your activity or fragment

          // Find the ShapeableImageView for the profile picture
        ShapeableImageView profileImageView = findViewById(R.id.rew4om8nwfp9);

       // Check if the user object and profile picture URL exist
        if (HomePageActivity.user != null && HomePageActivity.user.getProfilePicture() != null) {
            String profilePictureUrl = Arrays.toString(HomePageActivity.user.getProfilePicture());

            // Load the profile picture using Glide
            Glide.with(this)
                    .load(profilePictureUrl)  // URL or drawable
                    .placeholder(R.drawable.user)  // Placeholder while loading
                    .error(R.drawable.user)        // Error image if loading fails
                    .into(profileImageView);
        } else {
            // Handle the case where profile picture URL is not available
            profileImageView.setImageResource(R.drawable.user);


        }





    }

    private int calculateAge(LocalDate birthDate) {
        Calendar birth = Calendar.getInstance();

        int year = birth.get(Calendar.YEAR);
        int month = birth.get(Calendar.MONTH);
        int day = birth.get(Calendar.DAY_OF_MONTH);

        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR);

        // Check if the birthday has occurred this year or not
        if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }

    private void setupButtonListeners() {
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener (){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                int id_item=item.getItemId();
                if(id_item==R.id.home_BottomIcon)
                {
                    navigateTo(HomePageActivity.class);
                    return true;
                }
                else if (id_item == R.id.plan_BottomIcon)
                {
                    navigateTo(MyPlanActivity.class);
                    return true;
                }
                else if (id_item==R.id.workouts_BottomIcon)
                {
                    navigateTo(WorkoutsFilterActivity.class);
                    return true;
                }
                else if ( id_item==R.id.profile_BottomIcon)
                {

                    return true;
                }
                else
                    return false;

            }
        });
    }
    private void navigateTo(Class<?> targetActivity) {
        startActivity(new Intent(ProfileActivity.this, targetActivity));
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
