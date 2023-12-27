package com.stream.donalive.ui.chat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.stream.donalive.R;
import com.stream.donalive.databinding.ActivityConversationBinding;
import com.stream.donalive.ui.chat.adapter.MessageAdapter;
import com.stream.donalive.ui.chat.model.Message;
import com.stream.donalive.ui.home.ui.home.adapter.ActiveUserAdapter;
import com.stream.donalive.ui.utill.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ConversationActivity extends AppCompatActivity{
    private ActivityConversationBinding binding;
    private static final int LIMIT = 50;
    private Query mQuery;
    String senderId,receiverId,username,image;
    private MessageAdapter mAdapter;

    private FirebaseFirestore mFirestore;

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

        mQuery = mFirestore.collection(Constant.MESSAGE)
//                .whereEqualTo("senderId", senderId)
//                .whereEqualTo("receiverId", receiverId)
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .limit(LIMIT);

        setUserData(image,username);
        binding.ivSendMessage.setOnClickListener(v -> {
            if (!Objects.requireNonNull(binding.etInput.getText()).toString().trim().equals("")){
                sendMessage(senderId,receiverId,binding.etInput.getText().toString());
            }else {
                Toast.makeText(this, "Message input can't empty.", Toast.LENGTH_SHORT).show();
            }

        });

        mAdapter = new MessageAdapter(mQuery, new MessageAdapter.OnMessageUserSelectedListener() {
            @Override
            public void onMessageUserSelected(DocumentSnapshot user) {

            }
        }) {
            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    binding.rvMessage.setVisibility(View.GONE);
//                    binding.viewEmpty.setVisibility(View.VISIBLE);
                } else {
                    binding.rvMessage.setVisibility(View.VISIBLE);
//                    binding.viewEmpty.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.e("FirebaseFirestoreException", "onError: "+e );
            }


        };
        binding.rvMessage.setAdapter(mAdapter);

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
        Map<String, Object> message = new HashMap<>();
        message.put("senderId", senderId);
        message.put("receiverId", receiverId);
        message.put("messageText", messageText);
        message.put("timestamp", FieldValue.serverTimestamp());

        mFirestore.collection("messages")
                .add(message)
                .addOnSuccessListener(documentReference -> {
                    binding.etInput.setText("");
                    // Message sent successfully
                    // Handle success (if needed)
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    // Log or show an error message
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
        binding=null;
    }


}