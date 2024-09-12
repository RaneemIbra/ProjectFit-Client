package com.example.projectfit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.bumptech.glide.Glide;
import com.example.projectfit.R;

public class LoadingScreenActivity extends AppCompatActivity {

    private ImageView gifImageView;
    Button registerBTN, loginBTN;
    private static final int DELAY_MILLIS = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loading_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        setUpClickListeners();
        String gifUrl = "https://i.giphy.com/media/v1.Y2lkPTc5MGI3NjExODFocThmamZ0cGJ5Zm82aXJvYmQ0cWR2cXR1enJzOGM2YXZ4MnVwMiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/0BtrpaGo15NgvitihA/giphy.gif";
        Glide.with(this)
                .asGif()
                .load(gifUrl)
                .into(gifImageView);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(LoadingScreenActivity.this, RegisterActivity.class); // Change MainActivity to your target activity
            startActivity(intent);
            finish();
        }, DELAY_MILLIS);
    }

    private void initViews(){
        gifImageView = findViewById(R.id.gifImageView);
        registerBTN = findViewById(R.id.registerButtonLoadingScreen);
        loginBTN = findViewById(R.id.LoginButtonLoadingScreen);
    }
    private void setUpClickListeners(){
        registerBTN.setOnClickListener(v -> {
            Intent intent = new Intent(LoadingScreenActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        loginBTN.setOnClickListener(v -> {
            Intent intent = new Intent(LoadingScreenActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
