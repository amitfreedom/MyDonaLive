package com.stream.prettylive.streaming.global;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.stream.prettylive.R;

public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_layout);

        // Customize your dialog components
        // For example:
        // TextView dialogTitle = findViewById(R.id.dialog_title);
        // dialogTitle.setText("Your Custom Title");

        // Other dialog customization and functionalities
    }
}
