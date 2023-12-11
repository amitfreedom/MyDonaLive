package com.stream.donalive;

import android.app.Application;
import android.content.Context;

import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
import com.omninos.util_data.AppPreferences;

public class ApplicationClass extends Application {
    private static final String TAG = "ApplicationClass";
    private Context context;
    private static AppPreferences appPreference;
    private static Singleton singleton;
    public static ApplicationClass appInstance;
    public static ApplicationClass getInstance()
    {
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        appPreference = AppPreferences.init(context, "MyDonaLiveDb");
        singleton=new Singleton();


    }

    public static AppPreferences getAppPreference() {
        return appPreference;
    }


    public static Singleton getSingleton() {
        return singleton;
    }

//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.app_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel channel = new NotificationChannel("2", name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//    }
    @Override
    public void onTerminate() {
        super.onTerminate();

    }

}
