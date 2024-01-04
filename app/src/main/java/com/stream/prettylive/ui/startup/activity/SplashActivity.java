package com.stream.prettylive.ui.startup.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.stream.prettylive.R;
import com.stream.prettylive.global.AppConstants;
import com.stream.prettylive.global.ApplicationClass;
import com.stream.prettylive.ui.home.HomeActivity;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_SCREEN_TIME_OUT = 2000;
    //Animations
    Animation topAnimantion,bottomAnimation,middleAnimation;
    private RelativeLayout relativeLayout;
    private TextView txt_logo;
    private FirebaseAuth mAuth;
    TextView marqueeTextView;
    FirebaseFirestore db;
    private String work="1";
    private String dev="0";
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
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("stop_app");

        collectionRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Iterate through the documents
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                        work = documentSnapshot.getString("working");
                        dev = documentSnapshot.getString("dev");

                    }
                })
                .addOnFailureListener(e -> {
                    work="0";
                    dev="0";
                    // Handle any errors
                    //
                    //
                });

        TextView marqueeText = findViewById(R.id.marqueeText);

        // Set up marquee animation
//        startMarqueeAnimation(marqueeText);

         marqueeTextView = findViewById(R.id.marqueeTextView);


        // Call the method to start the marquee animation
//        startMarqueeAnimation1();

        topAnimantion = AnimationUtils.loadAnimation(this, R.anim.top_animantion);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animantion);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animantion);
        relativeLayout.setAnimation(topAnimantion);
        txt_logo.setAnimation(bottomAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = mAuth.getCurrentUser();

                if (Objects.equals(work, "0")) {
                    Toast.makeText(SplashActivity.this, "Work in progress so you can't use this time try after somme time and contact with Admin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Objects.equals(dev, "0")) {
//                    Toast.makeText(SplashActivity.this, "Work in progress so you can't use this time try after somme time and contact with Admin", Toast.LENGTH_SHORT).show();
                    return;
                }

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