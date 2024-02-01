package com.stream.prettylive.ui.toplist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.stream.prettylive.databinding.ActivityTopListBinding;
import com.stream.prettylive.ui.home.ui.profile.models.UserDetailsModel;
import com.stream.prettylive.ui.toplist.activity.TopUsersSendReceiveCoinsActivity;

import java.util.ArrayList;
import java.util.List;

public class TopListActivity extends AppCompatActivity {
    private ActivityTopListBinding binding;
    private List<UserDetailsModel>topReceiver= new ArrayList();
    private List<UserDetailsModel>topSender= new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTopListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        topSenderList();
        topReceiverList();

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnTopReceiver.setOnClickListener(v -> {
            Intent intent = new Intent(this, TopUsersSendReceiveCoinsActivity.class);
            intent.putExtra("listType","receiveCoin");
            startActivity(intent);
        });
        binding.btnTopSender.setOnClickListener(v -> {
            Intent intent = new Intent(this, TopUsersSendReceiveCoinsActivity.class);
            intent.putExtra("listType","senderCoin");
            startActivity(intent);
        });

//        updateUI(topSender,topReceiver);


    }

    private void receiverUser(List<UserDetailsModel> topReceiver) {
        try {
            if (topReceiver.size()>0){
                Glide.with(this).load(topReceiver.get(0).getImage()).into(binding.receiverImage1);
                Glide.with(this).load(topReceiver.get(1).getImage()).into(binding.receiverImage2);
                Glide.with(this).load(topReceiver.get(2).getImage()).into(binding.receiverImage3);
            }


        }catch (Exception e){

        }

    }

    private void topReceiverList() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("login_details")
                .orderBy("receiveCoin", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                // Access data from the document
                                String diamond = document.getString("diamond");
                                String username = document.getString("username");

                                UserDetailsModel userDetailsModel = document.toObject(UserDetailsModel.class);
                                topReceiver.add(userDetailsModel);
                                receiverUser(topReceiver);
                                Log.i("kjhdfjkhjkhdf", "Username: "+username+"\n"+"diamond :"+diamond);
                            }
                        } else {
                            // Handle the case where the query snapshot is null
                        }
                    } else {
                        // Handle the error
                        Exception exception = task.getException();
                        if (exception != null) {
                            exception.printStackTrace();
                        }
                    }
                });
    }
    private void topSenderList() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("login_details")
                .orderBy("senderCoin", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                // Access data from the document
                                String diamond = document.getString("diamond");
                                String username = document.getString("username");

                                UserDetailsModel userDetailsModel = document.toObject(UserDetailsModel.class);
                                topSender.add(userDetailsModel);
                                senderUser(topSender);
                                Log.i("kjhdfjkhjkhdf", "Username: "+username+"\n"+"diamond :"+diamond);
                            }
                        } else {
                            // Handle the case where the query snapshot is null
                        }
                    } else {
                        // Handle the error
                        Exception exception = task.getException();
                        if (exception != null) {
                            exception.printStackTrace();
                        }
                    }
                });
    }

    private void senderUser(List<UserDetailsModel> topSender) {
        try {
            if (topSender.size()>0){
                Glide.with(this).load(topSender.get(0).getImage()).into(binding.senderImage1);
                Glide.with(this).load(topSender.get(1).getImage()).into(binding.senderImage2);
                Glide.with(this).load(topSender.get(2).getImage()).into(binding.senderImage3);
            }


        }catch (Exception e){

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}