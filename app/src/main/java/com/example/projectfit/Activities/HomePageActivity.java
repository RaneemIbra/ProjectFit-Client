package com.example.projectfit.Activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.projectfit.R;
import com.google.android.material.imageview.ShapeableImageView;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.r13x7x0kfrgcc));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rtejx37ax1fg));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.r4nmnnagzvip));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.raju1mwklbia));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rkfm73wy97fr));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rr72cr43mxo9));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rks7n0a9hm5l));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rrif4xi0ks2s));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rt6mp0emoe48));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rei0kw491b7u));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.r1ya0vac26d6));
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.image3));
    }
}