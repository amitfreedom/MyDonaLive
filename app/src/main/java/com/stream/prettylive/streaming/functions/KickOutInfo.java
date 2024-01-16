package com.stream.prettylive.streaming.functions;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.stream.prettylive.ui.utill.Constant;

public class KickOutInfo {

    public KickOutInfo(Select select) {
        this.select = select;
    }

    private Select select;

    public interface Select{
        void KickOutStatus(int kickValue);
    }
    public void checkKickOut(String mainStreamID,String userId) {
        DocumentReference documentRef = FirebaseFirestore.getInstance()
                .collection(Constant.KICK_OUT)
                .document(mainStreamID)
                .collection("current_room_user")
                .document(userId);

// Check if the document already exists
        documentRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Document with userId already exists
                                select.KickOutStatus(1);
                                Log.i("room_users", "Document with userId already exists");
                            }
                            else {
                                select.KickOutStatus(0);

                            }
                        } else {
                            Log.e("room_users", "Error getting document", task.getException());
                        }
                    }
                });
    }

    public void realTimeKickOut(String mainStreamID, String userId) {
        DocumentReference documentRef = FirebaseFirestore.getInstance()
                .collection(Constant.KICK_OUT)
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
                    // Document with userId already exists
                    select.KickOutStatus(1);
                    Log.i("room_users", "Document with userId already exists");
                } else {
                    // Document with userId doesn't exist
                    select.KickOutStatus(0);
                    Log.i("room_users", "Document with userId does not exist");
                }
            }
        });
    }

}
