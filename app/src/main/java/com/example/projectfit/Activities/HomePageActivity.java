package com.example.projectfit.Activities;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.projectfit.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {
    BarChart stepChart, waterChart;

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
        ProgressBar circularProgressBar = findViewById(R.id.r13x7x0kfrgcc);
        circularProgressBar.setIndeterminate(false);
        circularProgressBar.setProgress(75);
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
        stepChart = findViewById(R.id.stepChart);
        waterChart = findViewById(R.id.WaterChart);

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 1f));
        entries.add(new BarEntry(1f, 2f));
        entries.add(new BarEntry(2f, 3f));
        entries.add(new BarEntry(3f, 4f));
        entries.add(new BarEntry(4f, 5f));

        BarDataSet dataSet = new BarDataSet(entries, "Sample Data");
        BarData barData = new BarData(dataSet);

        stepChart.setData(barData);
        waterChart.setData(barData);
        stepChart.invalidate();
        waterChart.invalidate();
    }
}