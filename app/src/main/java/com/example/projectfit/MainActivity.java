package com.example.projectfit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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