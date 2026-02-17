package com.mentalbook.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Splash Activity with animated loading screen
 * Shows logo animation, app name, and transitions to MainActivity
 */
public class SplashActivity extends AppCompatActivity {

    // Duration constants
    private static final int SPLASH_DURATION = 2500;
    private static final int LOGO_ANIMATION_DELAY = 300;
    private static final int APP_NAME_ANIMATION_DELAY = 800;
    private static final int TAGLINE_ANIMATION_DELAY = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize views
        ImageView ivLogo = findViewById(R.id.ivLogo);
        TextView tvAppName = findViewById(R.id.tvAppName);
        TextView tvTagline = findViewById(R.id.tvTagline);

        // Load animations
        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_in);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideFromBottom = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);

        // Start logo animation immediately
        ivLogo.startAnimation(scaleAnimation);

        // Delayed app name animation
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            tvAppName.startAnimation(fadeIn);
            tvAppName.setVisibility(TextView.VISIBLE);
        }, APP_NAME_ANIMATION_DELAY);

        // Delayed tagline animation
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            tvTagline.startAnimation(slideFromBottom);
            tvTagline.setVisibility(TextView.VISIBLE);
        }, TAGLINE_ANIMATION_DELAY);

        // Navigate to MainActivity after splash duration
        new Handler(Looper.getMainLooper()).postDelayed(this::navigateToMainActivity, SPLASH_DURATION);
    }

    private void navigateToMainActivity() {
        // Apply exit animation for splash
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        // Start main activity
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

