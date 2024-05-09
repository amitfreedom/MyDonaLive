package com.stream.prettylive.ui.chat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.stream.prettylive.R;
import com.stream.prettylive.databinding.ActivityConversationBinding;
import com.stream.prettylive.ui.chat.adapter.ChatAdapter;
import com.stream.prettylive.ui.chat.model.Message;
import com.stream.prettylive.ui.utill.Constant;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ConversationActivity extends AppCompatActivity{
    private static final String TAG = "ConversationActivity";
    private ActivityConversationBinding binding;
    private static final int LIMIT = 50;
    private Query mQuery;
    String senderId,receiverId,username,image;
//    private MessageAdapter mAdapter;

    private FirebaseFirestore mFirestore;

    private DatabaseReference mFirebaseRef;
    private String mId;
    private List<Message> mChats;
    private ChatAdapter mAdapter;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConversationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mFirestore=FirebaseFirestore.getInstance();
        senderId=getIntent().getStringExtra("senderId");
        receiverId=getIntent().getStringExtra("receiverId");
        username=getIntent().getStringExtra("username");
        image=getIntent().getStringExtra("image");

        mChats = new ArrayList<>();

        /**
         * Firebase - Inicialize
         */
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mFirebaseRef = database.getReference("message");
        mId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        setUserData(image,username);


//        binding.rvMessage.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mRecyclerView));
        mAdapter = new ChatAdapter(mChats,senderId);
        binding.rvMessage.setAdapter(mAdapter);

        binding.ivSendMessage.setOnClickListener(v -> {
            if (!Objects.requireNonNull(binding.etInput.getText()).toString().trim().equals("")){
                sendMessage(senderId,receiverId,binding.etInput.getText().toString());
            }else {
                Toast.makeText(this, "Message input can't empty.", Toast.LENGTH_SHORT).show();
            }

        });
        mFirebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {

                        Message message = dataSnapshot.getValue(Message.class);

                        if (message.getReceiverId().equals(senderId) && message.getSenderId().equals(receiverId) ||
                                message.getReceiverId().equals(receiverId) && message.getSenderId().equals(senderId)){
                            mChats.add(message);
                        }
//

                        binding.rvMessage.scrollToPosition(mChats.size() - 1);
                        mAdapter.notifyItemInserted(mChats.size() - 1);
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    }
                }
                for (int i = 0; i < mChats.size(); i++) {
                    Log.i("klhdfghkjkdfhgdjkfg", "onChildAdded: message ="+mChats.get(i).getMessageText());

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });
    }

    private void setUserData(String image, String username) {
        if (Objects.equals(image, "")){
            // Load image
            Glide.with(binding.ivUserImage.getContext())
                    .load(Constant.USER_PLACEHOLDER_PATH)
                    .into(binding.ivUserImage);
        }else {
            // Load image
            Glide.with(binding.ivUserImage.getContext())
                    .load(image)
                    .into(binding.ivUserImage);
        }


        binding.titleText.setText(username);
    }

    public void sendMessage(String senderId, String receiverId, String messageText) {
//        long timestamp = System.currentTimeMillis();
        Date currentDate = new Date();
        long timestamp = currentDate.getTime();
        Map<String, Object> message = new HashMap<>();
        message.put("senderId", senderId);
        message.put("receiverId", receiverId);
        message.put("messageText", messageText);
        message.put("deviceId", mId);
        message.put("timestamp", timestamp);

        mFirebaseRef.push().setValue(message);
    }


}