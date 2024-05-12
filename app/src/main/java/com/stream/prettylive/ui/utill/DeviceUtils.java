package com.stream.prettylive.ui.utill;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class DeviceUtils {

    public interface TokenFetchListener {
        void onTokenFetchSuccess(String token);
        void onTokenFetchFailure(Exception e);
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        // Get the Android device ID

        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void getDeviceToken(TokenFetchListener listener) {
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if (listener != null) {
                            listener.onTokenFetchSuccess(s);
                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (listener != null) {
                            listener.onTokenFetchFailure(e);
                        }
                    }
                });
    }
}

