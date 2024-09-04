package com.example.projectfit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectfit.Models.Workout;
import com.example.projectfit.Room.Repositories.UserRoomRepository;
import com.example.projectfit.R;
import com.example.projectfit.Models.User;
import com.example.projectfit.Room.Repositories.WorkoutRoomRepository;
import com.example.projectfit.Server.Repositories.UserServerRepository;
import com.example.projectfit.Server.Repositories.WorkoutServerRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button nav, nav2, nav3, nav4, nav5, nav6, nav7, nav8, nav9, nav10, nav11;
    private UserServerRepository userServerRepository;
    private UserRoomRepository userRoomRepository;
    private WorkoutRoomRepository workoutRoomRepository;
    private WorkoutServerRepository workoutServerRepository;

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
        userRoomRepository = new UserRoomRepository(this);
        workoutRoomRepository = new WorkoutRoomRepository(this);
        workoutServerRepository = new WorkoutServerRepository();
        nav = findViewById(R.id.HomePageBTN);
        nav2 = findViewById(R.id.MyPlanBTN);
        nav3 = findViewById(R.id.PlanQuestionsBTN);
        nav4 = findViewById(R.id.WorkoutBTN);
        nav5 = findViewById(R.id.WorkoutFilterBTN);
        nav6 = findViewById(R.id.ListBTN);
        nav7 = findViewById(R.id.resetPasswordPageBTN);
        nav8 = findViewById(R.id.ProfilePageBTN);
        nav9 = findViewById(R.id.LoginPageBTN);
        nav10 = findViewById(R.id.RegisterPageBTN);
        nav11 = findViewById(R.id.LoadingScreenBTN);
        LocalDate l1 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            l1 = LocalDate.ofYearDay(2024,1);
        }

        //Testing the backend
        User user1 = new User(null,"Temp1",5L,"Temp1","Temp1",l1,
        1.9,80,true,"Temp1","Temp1",null,null,null,null,null,null);
        userServerRepository.addUserInServer(user1);
        userRoomRepository.addUserLocally(user1);
        addWorkouts();
        setupNavigation();
    }

    private void addWorkouts() {
        List<String> muscles = new ArrayList<>();
        muscles.add("Chest");
        muscles.add("Triceps");

        List<Integer> setsReps = new ArrayList<>();
        setsReps.add(4);
        setsReps.add(12);
        Workout workout1 = new Workout("Bench Press",60,
                "Strength","A compound upper body exercise focusing on the chest, triceps, and shoulders.",
                muscles,300,null,null,null, setsReps,3);
        workoutRoomRepository.addWorkoutLocally(workout1);
        workoutServerRepository.addWorkoutInServer(workout1);
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
        nav7.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });
        nav8.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
        nav9.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        nav10.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        nav11.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, LoadingScreenActivity.class);
            startActivity(intent);
        });
    }
}