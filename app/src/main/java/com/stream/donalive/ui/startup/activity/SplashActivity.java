package com.stream.donalive.ui.startup.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stream.donalive.R;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_SCREEN_TIME_OUT = 2000;
    //Animations
    Animation topAnimantion,bottomAnimation,middleAnimation;
    private RelativeLayout relativeLayout;
    private TextView txt_logo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        relativeLayout=findViewById(R.id.main_logo);
        txt_logo=findViewById(R.id.txt_logo);

        topAnimantion = AnimationUtils.loadAnimation(this, R.anim.top_animantion);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animantion);
        relativeLayout.setAnimation(topAnimantion);
        txt_logo.setAnimation(bottomAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (user == null) {
                    Intent intent = new Intent(SplashActivity.this, OnboardingActivity.class);
                    startActivity(intent);
                    finish();
//                }
//                else {
//                    Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
//                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(mainIntent);
//                    finish();
//                }
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}