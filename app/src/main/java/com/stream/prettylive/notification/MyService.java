package com.stream.prettylive.notification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.stream.prettylive.global.AppConstants;
import com.stream.prettylive.global.ApplicationClass;
import com.stream.prettylive.ui.utill.Constant;

import java.util.HashMap;
import java.util.Map;

public class MyService extends Service {

    private static final String TAG = "MyService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");
        return START_STICKY; // or other appropriate flag
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed");
        updateLiveStatus(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(TAG, "App is removed from recent apps list");
        updateLiveStatus(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));

    }

    private void updateLiveStatus(String userId) {
        // Reference to the Firestore collection
        CollectionReference liveDetailsRef = FirebaseFirestore.getInstance().collection(Constant.LIVE_DETAILS);

        // Create a query to find the document with the given userId
        Query query = liveDetailsRef.whereEqualTo("userId", userId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
               try{
                   for (QueryDocumentSnapshot document : task.getResult()) {
                       // Get the document ID for the matched document
                       String documentId = document.getId();
                       String liveStatus = document.getString("liveStatus");

                       long timestamp = System.currentTimeMillis();
                       Map<String, Object> updateDetails = new HashMap<>();
                       updateDetails.put("liveStatus", "offline");
                       updateDetails.put("endTime", timestamp);

                       // Update the liveType field from 0 to 1
                       liveDetailsRef.document(documentId)
                               .update(updateDetails)
                               .addOnSuccessListener(aVoid -> {
                                   stopSelf();
                                   Log.i("UpdateLiveType", "liveType updated successfully for user with ID: " + userId);
                               })
                               .addOnFailureListener(e -> {
                                   Log.e("UpdateLiveType", "Error updating liveType for user with ID: " + userId, e);
                               });
                   }
               }catch (Exception e){

               }
            } else {
                Log.e("UpdateLiveType", "Error getting documents: ", task.getException());
            }
        });
    }
}

