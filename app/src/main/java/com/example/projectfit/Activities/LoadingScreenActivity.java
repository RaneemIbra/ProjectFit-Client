package com.example.projectfit.Activities;

import android.os.Bundle;
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

        gifImageView = findViewById(R.id.gifImageView);
        String gifUrl = "https://i.giphy.com/media/v1.Y2lkPTc5MGI3NjExODFocThmamZ0cGJ5Zm82aXJvYmQ0cWR2cXR1enJzOGM2YXZ4MnVwMiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/0BtrpaGo15NgvitihA/giphy.gif";
        Glide.with(this)
                .asGif()
                .load(gifUrl)
                .into(gifImageView);
    }
}
