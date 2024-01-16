package com.stream.prettylive.streaming.functions;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.stream.prettylive.ui.utill.Constant;

import java.util.Objects;

public class EndLiveStatus {

    public EndLiveStatus(Select select) {
        this.select = select;
    }

    private Select select;

    public interface Select{
        void LiveEndStatus(int endStatus);
    }

    public void realTimeLiveEnd(String mainStreamID, String userId) {
        try{
            DocumentReference documentRef = FirebaseFirestore.getInstance()
                    .collection(Constant.ROOM_STATUS)
                    .document(mainStreamID)
                    .collection("current_room_user")
                    .document(userId);

            // Add a snapshot listener to listen for real-time updates
            documentRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.e("room_users", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        if (Objects.equals(documentSnapshot.getString("active"), "no")){
                            select.LiveEndStatus(1);
                        }else {
                            select.LiveEndStatus(0);
                        }

                        Log.i("room_users", "Document with userId already exists");
                    }
                }
            });


        }catch (Exception e){

        }
    }
}
