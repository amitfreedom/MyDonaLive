package com.stream.donalive.ui.follow.activity;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stream.donalive.R;
import com.stream.donalive.databinding.ActivityUserInfoBinding;
import com.stream.donalive.global.AppConstants;
import com.stream.donalive.global.ApplicationClass;
import com.stream.donalive.ui.chat.activity.ConversationActivity;
import com.stream.donalive.ui.follow.TestUser;
import com.stream.donalive.ui.follow.methods.FirestoreUtils;
import com.stream.donalive.ui.follow.methods.FollowUnfollowManager;
import com.stream.donalive.ui.utill.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserInfoActivity extends AppCompatActivity {
    private static final String TAG = "UserInfoActivity";

    private ActivityUserInfoBinding binding;
    private String userId;
    private String level;
    private String username;
    private String image;
    private String uid;
    private FirebaseFirestore mFirestore;
    private boolean isFollowingUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mFirestore = FirebaseFirestore.getInstance();

        userId = getIntent().getStringExtra("userId");
        username = getIntent().getStringExtra("username");
        image = getIntent().getStringExtra("image");
        level = getIntent().getStringExtra("level");
        uid = getIntent().getStringExtra("uid");

//        createDocuments(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID),userId);
        checkFollowFollowingStatus();

        updateUI();

        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFollowingUser){
                    new FollowUnfollowManager(new FollowUnfollowManager.Select() {
                        @Override
                        public void onSuccess(String status) {
                            checkFollowFollowingStatus();
                            Toast.makeText(UserInfoActivity.this, status, Toast.LENGTH_SHORT).show();
                        }
                    }).unfollowUser(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID),userId);
                }else {
                    new FollowUnfollowManager(new FollowUnfollowManager.Select() {
                        @Override
                        public void onSuccess(String status) {
                            checkFollowFollowingStatus();
                            Toast.makeText(UserInfoActivity.this, status, Toast.LENGTH_SHORT).show();
                        }
                    }).followUser(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID),userId);
                }
            }
        });
        binding.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, ConversationActivity.class);
                intent.putExtra("senderId",ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));
                intent.putExtra("receiverId",userId);
                intent.putExtra("image",image);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

    }

    private void updateUI() {
        binding.txtUsername.setText(username);
        binding.txtUid.setText("ID : "+uid);
        binding.txtLevel.setText("Lv"+level);
        if (Objects.equals(image, "")){
            Glide.with(this).load(Constant.USER_PLACEHOLDER_PATH).into(binding.ivProfileImage);
        }else {
            Glide.with(this).load(image).into(binding.ivProfileImage);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void checkFollowFollowingStatus() {
        FirestoreUtils.checkFollowStatus(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID),userId, new FirestoreUtils.FollowStatusCallback() {
            @Override
            public void onFollowStatus(boolean isFollowing) {
                if (isFollowing) {
                    isFollowingUser= true;
                    binding.txtFollow.setText("Following");
                } else {
                    isFollowingUser= false;
                    binding.txtFollow.setText("+ Follow");
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}