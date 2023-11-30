package com.stream.donalive.ui.startup.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;

import com.stream.donalive.R;
import com.stream.donalive.databinding.ActivityOnboardingBinding;

public class OnboardingActivity extends AppCompatActivity {
    private ActivityOnboardingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spannableString();

    }

    private void spannableString() {
        String fullText = "Login means you agree to our Terms & Privacy Policy";

        // Create a SpannableString to underline the specific text
        SpannableString spannableString = new SpannableString(fullText);

        // Find the starting and ending indices of the text to be underlined
        int startUnderline = fullText.indexOf("Terms");
        int endUnderline = fullText.indexOf("Policy") + "Policy".length();

        // Apply underline to the specified portion of text
        if (startUnderline != -1 && endUnderline != -1) {
            spannableString.setSpan(new UnderlineSpan(), startUnderline, endUnderline, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // You can also change the color of the underlined text if needed
            spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), startUnderline, endUnderline, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        binding.txtTerms.setText(spannableString);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}