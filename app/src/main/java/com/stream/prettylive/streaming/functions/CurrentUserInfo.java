package com.stream.prettylive.streaming.functions;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stream.prettylive.ui.home.ui.profile.models.UserDetailsModel;
import com.stream.prettylive.ui.utill.Constant;

public class CurrentUserInfo {
    private static final String TAG = "UserInfo";
    private final CollectionReference usersRef = FirebaseFirestore.getInstance().collection(Constant.LOGIN_DETAILS);
    private UserDetailsModel userInfoByUserId;


    public CurrentUserInfo(UserInfo.Select select) {
        this.select = select;
    }

    private UserInfo.Select select;

    public interface Select{
        void UserDetailsByUserId(UserDetailsModel userInfoById);
    }
    public void getUserDetailsByUserId(String userId) {
        Log.i(TAG, "fetchUserDetails: "+userId);
        try {
            usersRef.whereEqualTo("userId", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                userInfoByUserId = document.toObject(UserDetailsModel.class);
                            }
                            select.UserDetailsByUserId(userInfoByUserId);
                        } else {
                            // Handle error
                            Log.e(TAG, "Error getting documents: ", task.getException());
                        }
                    });

        } catch (Exception e) {
            // Handle exceptions if needed
            Log.e(TAG, "Exception: " + e.getMessage());
        }

//        try{
//            usersRef.whereEqualTo("userId", userId)
//                    .addSnapshotListener((value, error) -> {
//                        if (error != null) {
//                            // Handle error
//                            Log.e(TAG, "Listen failed: " + error.getMessage());
//                            return;
//                        }
//
//                        assert value != null;
//                        for (DocumentSnapshot document : value) {
//                            userInfoByUserId = document.toObject(UserDetailsModel.class);
//                        }
//                        select.UserDetailsByUserId(userInfoByUserId);
//                    });
//
//        }catch (Exception e){
//
//        }


    }
}
