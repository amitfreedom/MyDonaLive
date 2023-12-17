package com.stream.donalive.ui.startup.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stream.donalive.R;
import com.stream.donalive.global.AppConstants;
import com.stream.donalive.global.ApplicationClass;
import com.stream.donalive.ui.home.HomeActivity;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_SCREEN_TIME_OUT = 2000;
    //Animations
    Animation topAnimantion,bottomAnimation,middleAnimation;
    private RelativeLayout relativeLayout;
    private TextView txt_logo;
    private FirebaseAuth mAuth;
    TextView marqueeTextView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        relativeLayout=findViewById(R.id.main_logo);
        txt_logo=findViewById(R.id.txt_logo);

        TextView marqueeText = findViewById(R.id.marqueeText);

        // Set up marquee animation
//        startMarqueeAnimation(marqueeText);

         marqueeTextView = findViewById(R.id.marqueeTextView);

        // Call the method to start the marquee animation
//        startMarqueeAnimation1();

        topAnimantion = AnimationUtils.loadAnimation(this, R.anim.top_animantion);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animantion);
        relativeLayout.setAnimation(topAnimantion);
        txt_logo.setAnimation(bottomAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = mAuth.getCurrentUser();

                if (user == null) {
                    Intent intent = new Intent(SplashActivity.this, OnboardingActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    ApplicationClass.getSharedpref().saveString(AppConstants.USER_ID, user.getUid());
                    Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }

    private void startMarqueeAnimation1() {
        // Calculate the duration of the animation based on the length of the text
        int textLength = marqueeTextView.getText().length();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        long duration = (textLength * 1000) / screenWidth * 100; // Adjust the factor as needed

        // Create a translate animation
        TranslateAnimation animation = new TranslateAnimation(
                screenWidth,          // fromXDelta
                -screenWidth,         // toXDelta
                0,                    // fromYDelta
                0                     // toYDelta
        );

        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(duration);
        animation.setRepeatCount(TranslateAnimation.INFINITE);
        animation.setFillAfter(true);

        // Set animation listener if needed
        animation.setAnimationListener(new TranslateAnimation.AnimationListener() {
            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {
            }

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                // Handle animation end if needed
            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {
                // Handle animation repeat if needed
            }
        });

        // Start the animation
        marqueeTextView.startAnimation(animation);
    }

    private void startMarqueeAnimation(TextView textView) {
        // Measure the text width
        textView.measure(0, 0);

        // Calculate the duration based on text width and screen width
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int textWidth = textView.getMeasuredWidth();
        long duration = (textWidth + screenWidth) * 3; // Adjust the multiplier as needed

        // Set up marquee animation
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(textView, "translationX", -screenWidth, textWidth);
        animatorX.setInterpolator(new LinearInterpolator());
        animatorX.setDuration(duration);
        animatorX.setRepeatMode(ValueAnimator.RESTART);
        animatorX.setRepeatCount(ValueAnimator.INFINITE);

        // Start the animation
        animatorX.start();
    }
}