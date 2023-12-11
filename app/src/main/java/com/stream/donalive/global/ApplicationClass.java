package com.stream.donalive.global;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.stream.donalive.streaming.ZEGOSDKKeyCenter;
import com.stream.donalive.streaming.internal.sdk.ZEGOSDKManager;

public class ApplicationClass extends Application {
    private static final String TAG = "ApplicationClass";
    private Context context;
    private static Sharedpref sharedpref;
    private static Singleton singleton;
    private SharedPreferences mPref;
    private static ApplicationClass instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getApplicationContext();
        sharedpref = new Sharedpref(getApplicationContext());
        singleton = new Singleton();

        initZEGOSDK();

//        mPref = getSharedPreferences(AppConstants.DB, Context.MODE_PRIVATE);


    }

    private void initZEGOSDK() {

        ZEGOSDKManager.getInstance().initSDK(instance, ZEGOSDKKeyCenter.appID, ZEGOSDKKeyCenter.appSign);
        ZEGOSDKManager.getInstance().enableZEGOEffects(true);
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

        Log.i("1234560", "onTerminate() method called. Application is being terminated.");

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            System.out.println("Call onTerminate456 TRIM_MEMORY_UI_HIDDEN");
            // The application's UI is no longer visible
            // Perform cleanup tasks here
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


}
