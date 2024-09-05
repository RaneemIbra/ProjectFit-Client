package com.example.projectfit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectfit.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {
    private EditText emailEditText, phoneEditText, heightEditText, weightEditText, dateEditText, nameEditText;
    private Button editProfileButton;
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

        editProfileButton = findViewById(R.id.editProfileButton);
        bottomBar = findViewById(R.id.bottom_navigation);
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
                    navigateTo(WorkoutsListActivity.class);
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
