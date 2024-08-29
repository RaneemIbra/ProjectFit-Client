package com.example.projectfit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {
    Button nav, nav2, nav3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nav = findViewById(R.id.button);
        nav2 = findViewById(R.id.button3);
        nav3 = findViewById(R.id.nav3);
        LocalDate l1 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            l1 = LocalDate.ofYearDay(2024,1);
        }
        User user1 = new User(1L,"Temp",5L,"Temp","Temp",l1,
        1.9,80,true,"Temp","Temp",null,null,null,null,null,null);
        UserAPI userAPI = AppAPI.getClient().create(UserAPI.class);
        Call<User> call =userAPI.createUser(user1);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "User submitted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to submit user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        nav.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, WorkoutsListActivity.class);
            startActivity(intent);
        });
        nav2.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, WorkoutActivity.class);
            startActivity(intent);
        });
        nav3.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, WorkoutsFilterActivity.class);
            startActivity(intent);
        });
    }
}