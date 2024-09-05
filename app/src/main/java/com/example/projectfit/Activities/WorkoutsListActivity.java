package com.example.projectfit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.projectfit.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

public class WorkoutsListActivity extends AppCompatActivity {

    LinearLayout layout1,layout2,layout3,layout4,layout5,layout6;
    BottomNavigationView bottomBar;

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

        initViews();
        initClickListeners();
    }

    private void navigateTo(Class<?> targetActivity) {
        startActivity(new Intent(WorkoutsListActivity.this, targetActivity));
    }
    private void initClickListeners() {

        layout1.setOnClickListener(v -> navigateTo(WorkoutActivity.class));
        layout2.setOnClickListener(v -> navigateTo(WorkoutActivity.class));
        layout3.setOnClickListener(v -> navigateTo(WorkoutActivity.class));
        layout4.setOnClickListener(v -> navigateTo(WorkoutActivity.class));
        layout5.setOnClickListener(v -> navigateTo(WorkoutActivity.class));
        layout6.setOnClickListener(v -> navigateTo(WorkoutActivity.class));
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener (){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                int id_item=item.getItemId();
                if(id_item==R.id.home_BottomIcon)
                {
                    navigateTo(HomePageActivity.class);
                    return true;
                }
                else if (id_item == R.id.plan_BottomIcon)
                {
                    navigateTo(MyPlanActivity.class);
                    return true;
                }
                else if (id_item==R.id.workouts_BottomIcon)
                {
                    return true;
                }
                else if ( id_item==R.id.profile_BottomIcon)
                {
                    navigateTo(ProfileActivity.class);
                    return true;
                }
                else
                    return false;

            }
        });
    }
    private void initViews() {

        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
        layout4 = findViewById(R.id.layout4);
        layout5 = findViewById(R.id.layout5);
        layout6 = findViewById(R.id.layout6);
        bottomBar=findViewById(R.id.bottom_navigation);
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image1));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image2));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image3));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image4));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image5));
        Glide.with(this).load("https://i.imgur.com/kyjOOcy.jpeg").into((ShapeableImageView) findViewById(R.id.image6));
    }
}