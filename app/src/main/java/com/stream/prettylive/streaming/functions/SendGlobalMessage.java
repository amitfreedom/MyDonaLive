package com.stream.prettylive.streaming.functions;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.stream.prettylive.streaming.activity.model.ChatMessageModel;
import com.stream.prettylive.ui.home.ui.profile.models.UserDetailsModel;
import com.stream.prettylive.ui.utill.Constant;

public class SendGlobalMessage {
    private static final String TAG = "SendGlobalMessage";
    private final CollectionReference usersRef = FirebaseFirestore.getInstance().collection(Constant.LOGIN_DETAILS);

    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private  UserDetailsModel userDetails;
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("userInfo");
    private String roomId ="";
    private String message ="";

    public  SendGlobalMessage(String roomId,String message) {
        this.roomId=roomId;
        this.message=message;
        Log.i(TAG, "sendCustomMessage: 3444555");
        if (mAuth.getCurrentUser() != null) {
            userInfo(mAuth.getCurrentUser().getUid());
            Log.i(TAG, "sendCustomMessage: 3444555");
        }
    }

    private void userInfo(String userId) {
        usersRef.whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.exists()) {
                                UserDetailsModel userDetails = document.toObject(UserDetailsModel.class);
                                sendCustomMessage(message, userDetails);
                            }
                        }
                    } else {
                        Log.e("FirestoreListener", "Error getting documents: ", task.getException());
                    }
                });
    }


    public void sendCustomMessage(String message,UserDetailsModel userDetails) {
        Log.i(TAG, "sendCustomMessage: "+message);
        ChatMessageModel chatMessageModel = new ChatMessageModel();
        chatMessageModel.setGift("");
        chatMessageModel.setImage(userDetails.getImage());
        chatMessageModel.setKey(ref.push().getKey());
        chatMessageModel.setMessage(message);
        chatMessageModel.setName(userDetails.getUsername());
        chatMessageModel.setLevel(userDetails.getLevel());
        chatMessageModel.setUserId(userDetails.getUserId());
        sendMessage(chatMessageModel, chatMessageModel.getKey());
    }

    private void sendMessage(ChatMessageModel chatMessageModel, String key) {
        ref.child(roomId).child("audio").child(roomId).child("chat_comments").child(key).setValue(chatMessageModel);

    }

}
