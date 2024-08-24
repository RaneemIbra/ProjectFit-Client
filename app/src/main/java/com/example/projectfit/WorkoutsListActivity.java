package com.example.projectfit;

import android.os.Bundle;

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
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image3));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image4));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image5));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image6));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image7));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image8));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image18));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image19));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image20));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image21));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image22));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image23));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image24));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image25));
    }
}