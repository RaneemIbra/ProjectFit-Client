package com.example.projectfit.Activities;

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

import com.example.projectfit.Server.API.AppAPI;
import com.example.projectfit.Server.API.UserAPI;
import com.example.projectfit.R;
import com.example.projectfit.Server.Models.User;
import com.example.projectfit.Server.Repositories.UserServerRepository;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {
    Button nav, nav2, nav3, nav4, nav5, nav6;
    private UserServerRepository userServerRepository;

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
        userServerRepository = new UserServerRepository();
        nav = findViewById(R.id.HomePageBTN);
        nav2 = findViewById(R.id.MyPlanBTN);
        nav3 = findViewById(R.id.PlanQuestionsBTN);
        nav4 = findViewById(R.id.WorkoutBTN);
        nav5 = findViewById(R.id.WorkoutFilterBTN);
        nav6 = findViewById(R.id.ListBTN);
        LocalDate l1 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            l1 = LocalDate.ofYearDay(2024,1);
        }
        User user1 = new User(1L,"Temp1",5L,"Temp1","Temp1",l1,
        1.9,80,true,"Temp1","Temp1",null,null,null,null,null,null);
        userServerRepository.addUser(user1);
        setupNavigation();
    }
    private void setupNavigation() {
        nav.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
            startActivity(intent);
        });
        nav2.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MyPlanActivity.class);
            startActivity(intent);
        });
        nav3.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PlanQuestionsActivity.class);
            startActivity(intent);
        });
        nav4.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, WorkoutActivity.class);
            startActivity(intent);
        });
        nav5.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, WorkoutsFilterActivity.class);
            startActivity(intent);
        });
        nav6.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, WorkoutsListActivity.class);
            startActivity(intent);
        });
    }
}