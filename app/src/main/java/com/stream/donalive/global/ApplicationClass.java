package com.stream.donalive.global;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.stream.donalive.streaming.ZEGOSDKKeyCenter;
import com.stream.donalive.streaming.internal.sdk.ZEGOSDKManager;
import com.zegocloud.zimkit.services.ZIMKit;
import com.zegocloud.zimkit.services.config.InputConfig;

import java.util.HashMap;
import java.util.Map;

public class ApplicationClass extends Application {
    private static final String TAG = "ApplicationClass";
    FirebaseFirestore db;
    private Context context;
    private static Sharedpref sharedpref;
    private static Singleton singleton;
    private SharedPreferences mPref;
    private static ApplicationClass instance;

    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseFirestore.getInstance();
        instance = this;
        context = getApplicationContext();
        sharedpref = new Sharedpref(getApplicationContext());
        singleton = new Singleton();

        initZEGOSDK();
        ZIMKit.initWith(this, ZEGOSDKKeyCenter.appID, ZEGOSDKKeyCenter.appSign);
        ZIMKit.initNotifications();

        InputConfig inputConfig = new InputConfig();
        inputConfig.showVoiceButton = true;
        inputConfig.showEmojiButton = true;
        inputConfig.showAddButton = true;
        ZIMKit.setInputConfig(inputConfig);


    }

    private void initZEGOSDK() {

        ZEGOSDKManager.getInstance().initSDK(instance, ZEGOSDKKeyCenter.appID, ZEGOSDKKeyCenter.appSign);
//        ZEGOSDKManager.getInstance().enableZEGOEffects(true);
    }

    public static ApplicationClass getInstance() {
        return instance;
    }

    public static Sharedpref getSharedpref() {
        return sharedpref;
    }

    public static Singleton getSingleton() {
        return singleton;
    }

    public static ApplicationClass getAppContext() {
        return instance;
    }
    public SharedPreferences preferences() {
        return mPref;
    }




    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            System.out.println("Call onTerminate456 TRIM_MEMORY_UI_HIDDEN");
            // The application's UI is no longer visible
            // Perform cleanup tasks here
//            saveData();
        }

        if (level == ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            System.out.println("Call onTerminate456 TRIM_MEMORY_BACKGROUND");
            // The application's UI is no longer visible
            // Perform cleanup tasks here
        }

        if (level == ComponentCallbacks2.TRIM_MEMORY_COMPLETE) {
            System.out.println("Call onTerminate456 TRIM_MEMORY_COMPLETE");
            // The application's UI is no longer visible
            // Perform cleanup tasks here
        }if (level == ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
            System.out.println("Call onTerminate456 TRIM_MEMORY_MODERATE");
            // The application's UI is no longer visible
            // Perform cleanup tasks here
        }

        if (level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL) {
            System.out.println("Call onTerminate456 TRIM_MEMORY_RUNNING_CRITICAL");
            // The application's UI is no longer visible
            // Perform cleanup tasks here
        }if (level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW) {
            System.out.println("Call onTerminate456 TRIM_MEMORY_RUNNING_LOW");
            // The application's UI is no longer visible
            // Perform cleanup tasks here
        }if (level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE) {
            System.out.println("Call onTerminate456 TRIM_MEMORY_RUNNING_MODERATE");
            // The application's UI is no longer visible
            // Perform cleanup tasks here
        }
    }

    private void saveData() {
        Log.i("checkmethod", "onDestroy:======123== ");
        // Create a new user data map
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Dave");
        user.put("email", "dave@example.com");

// Add a new document with a generated ID
        db.collection("Test145")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Log.i("checkmethod", "DocumentSnapshot added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.i("checkmethod", "Error adding document", e);
                });

        Log.i("checkmethod", "onDestroy:======1234== ");

    }


}
