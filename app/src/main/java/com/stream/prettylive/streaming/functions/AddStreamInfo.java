package com.stream.prettylive.streaming.functions;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stream.prettylive.ui.utill.Constant;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class AddStreamInfo {
    private static final String TAG = "AddStreamInfo";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference streamCollection = db.collection("streams");
    private final CollectionReference usersRef = db.collection(Constant.LOGIN_DETAILS);

    public void addStreamInfo(String mainStreamID, String uid ,String userID, String userName, String image) {
        // Create a Map to hold the stream information
        Map<String, Object> streamInfo = new HashMap<>();
        streamInfo.put("mainStreamID", mainStreamID);
        streamInfo.put("uid", uid);
        streamInfo.put("userID", getUID(uid));
        streamInfo.put("userName", userName);
        streamInfo.put("image", image);

        // Add the stream information to Firestore
        DocumentReference streamDocRef = streamCollection.document(mainStreamID);
        streamDocRef.set(streamInfo)
                .addOnSuccessListener(aVoid -> {
                    // Handle success
                    Log.i("Firestore123", "Stream information added successfully for mainStreamID: " + mainStreamID);
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Log.e("Firestore123", "Error adding stream information for mainStreamID: " + mainStreamID, e);
                });
    }

    private String getUID(String uid) {
        Log.i("test2334", "fetchUserDetails: "+uid);
        final String[] userNAme = {""};
        usersRef.whereEqualTo("uid", Long.parseLong(uid))
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Handle error
                        Log.e("test2334", "Listen failed: " + error.getMessage());
                        return;
                    }

                    assert value != null;

                    for (DocumentSnapshot document : value) {
                         userNAme[0] =document.getString("userId");

                    }

                });

        return userNAme[0];


    }
}
