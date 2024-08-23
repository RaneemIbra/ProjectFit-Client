package com.example.projectfit;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

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
        ImageView imageView1 = findViewById(R.id.image1);
        ImageView imageView2 = findViewById(R.id.image2);
        ImageView imageView3 = findViewById(R.id.image3);
        ImageView imageView4 = findViewById(R.id.image4);
        ImageView imageView5 = findViewById(R.id.image5);
        ImageView imageView6 = findViewById(R.id.image6);
        ImageView imageView7 = findViewById(R.id.image7);

        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into(imageView1);
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into(imageView2);
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into(imageView3);
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into(imageView4);
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into(imageView5);
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into(imageView6);
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into(imageView7);
    }
}