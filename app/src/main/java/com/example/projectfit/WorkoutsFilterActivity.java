package com.example.projectfit;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

public class WorkoutsFilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workouts_filter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ShapeableImageView photo=findViewById(R.id.legs_image);
        photo.setImageResource(R.drawable.img);

       // Glide.with(this).load("drawable/img.png").into((ShapeableImageView)findViewById(R.id.r4en2wcm8cu));
        //Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rj2eqf4csatn));
        //Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rf7hmijhd478));
        //Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rmkb1m5uwabg));
        //Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into((ShapeableImageView)findViewById(R.id.rrj38y1qpc3));
    }
}