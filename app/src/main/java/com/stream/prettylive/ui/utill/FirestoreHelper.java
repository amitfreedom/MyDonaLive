package com.stream.prettylive.ui.utill;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreHelper {
    public static void fetchAndUpdateHostCount(String field) {
        // Access Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "all_counts" document in "total_counts" collection
        DocumentReference countsDocRef = db.collection(Constant.TOTAL_COUNTS).document(Constant.ALL_COUNT);

        // Fetch the document
        countsDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        try{
                            // Get the current host count
                            long currentHostCount = document.getLong(field);

                            // Increment the host count
                            long newHostCount = currentHostCount + 1;

                            // Update the document with the new host count
                            countsDocRef.update(field, newHostCount)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Host count updated successfully
                                                // Handle success if needed
                                            } else {
                                                // Failed to update host count
                                                // Handle failure if needed
                                            }
                                        }
                                    });

                        }catch (Exception e){
                            Log.i("kjhgfc", "onComplete: ");

                        }
                    } else {
                        // Document does not exist
                        // Handle error if needed
                    }
                } else {
                    // Error fetching document
                    // Handle error if needed
                }
            }
        });
    }
}

