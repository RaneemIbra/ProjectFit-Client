package com.example.projectfit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

public class WorkoutsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workouts_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;  
        });
//        Button homePageBtn = findViewById(R.id.homePageBtn);
//        homePageBtn.setOnClickListener(view -> {
//            Intent intent = new Intent(WorkoutsListActivity.this, MainActivity.class);
//            startActivity(intent);
//        });
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image1));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image2));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image3));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image4));
    }
}