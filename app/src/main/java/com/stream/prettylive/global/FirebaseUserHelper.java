package com.stream.prettylive.global;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.stream.prettylive.ui.home.ui.profile.models.UserDetailsModel;
import com.stream.prettylive.ui.utill.Constant;

import java.util.HashMap;
import java.util.Map;

public class FirebaseUserHelper {
private Context context;
    public FirebaseUserHelper(Context context) {
        this.context=context;
    }

    public interface UserDetailsListener {
        void onUserDetailsFetched(UserDetailsModel userDetails);
        void onUserDetailsNotFetched(String message);
    }


    // Function to enable or disable a user in Firestore
    //kjfgjgfj
    public static void setDisabledStatus(FragmentActivity fragmentActivity, String userId, boolean disabled) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference loginDetails = db.collection(Constant.LOGIN_DETAILS);
        // Create a query to find the document with the given userId
        Query query = loginDetails.whereEqualTo("userId", userId);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the document ID for the matched document
                    String documentId = document.getId();

                    Map<String, Object> updateDetails = new HashMap<>();
                    updateDetails.put("disabled", !disabled);

                    // Update the liveType field from 0 to 1
                    loginDetails.document(documentId)
                            .update(updateDetails)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(fragmentActivity, "User status changed", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Log.i("sdfdf", "setDisabledStatus: "+e.getMessage());

                            });
                }
            } else {
                Log.e("UpdateLiveType", "Error getting documents: ", task.getException());
            }
        });

    }

    public static void fetchUserDetails(long uid,Context context, UserDetailsListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constant.LOGIN_DETAILS)
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (DocumentSnapshot document : querySnapshot) {
                                UserDetailsModel userDetails = document.toObject(UserDetailsModel.class);
                                // Pass the fetched user details to the listener
                                listener.onUserDetailsFetched(userDetails);

                            }
                        } else {
                            String errorMessage = "No user details found";
//                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                            listener.onUserDetailsNotFetched(errorMessage);
                        }
                    } else {
//                        Toast.makeText(context, "Error fetching user details", Toast.LENGTH_SHORT).show();
                        // Handle error
                        Exception exception = task.getException();
                        listener.onUserDetailsNotFetched(exception.getMessage());
                        Log.e("FirebaseUserHelper", "Error fetching user details: " + exception.getMessage());
                    }
                });
    }


}