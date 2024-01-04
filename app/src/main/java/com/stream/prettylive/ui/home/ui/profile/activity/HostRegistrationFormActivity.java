package com.stream.prettylive.ui.home.ui.profile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.stream.prettylive.R;

public class HostRegistrationFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_registration_form);
    }

    public void onBackPress(View view) {
        onBackPressed();
    }
}