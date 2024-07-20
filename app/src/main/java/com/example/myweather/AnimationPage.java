package com.example.myweather;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AnimationPage extends AppCompatActivity {

    ImageView imgView;
    TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_page);

        imgView = findViewById(R.id.logo);
        txtView = findViewById(R.id.intro);

        // Initially set the alpha to 0 (invisible)
        imgView.setAlpha(0f);
        txtView.setAlpha(0f);

        // Animate the ImageView to fade in
        imgView.animate().alpha(1f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Once the ImageView animation ends, start the TextView animation
                txtView.animate().alpha(1f).setDuration(800).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // After both animations are complete, delay the transition to the next activity
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            Intent intent = new Intent(AnimationPage.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }, 3000); // 3 seconds delay
                    }
                });
            }
        });
    }
}