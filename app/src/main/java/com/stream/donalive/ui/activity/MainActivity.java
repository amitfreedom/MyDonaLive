package com.stream.donalive.ui.activity;

import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.stream.donalive.R;
import com.stream.donalive.databinding.ActivityMainBinding;
import com.stream.donalive.global.AppConstants;
import com.stream.donalive.global.ApplicationClass;
import com.stream.donalive.streaming.activity.CallWaitActivity;
import com.stream.donalive.streaming.activity.LiveAudioRoomActivity;
import com.stream.donalive.streaming.activity.LiveStreamingActivity;
import com.stream.donalive.streaming.components.call.CallBackgroundService;
import com.stream.donalive.streaming.internal.ZEGOLiveStreamingManager;
import com.stream.donalive.streaming.internal.ZEGOCallInvitationManager;
import com.stream.donalive.streaming.internal.business.call.CallExtendedData;
import com.stream.donalive.streaming.internal.business.call.FullCallInfo;
import com.stream.donalive.streaming.internal.sdk.ZEGOSDKManager;
import com.stream.donalive.streaming.internal.sdk.basic.ZEGOSDKUser;
import com.stream.donalive.ui.home.ui.profile.models.UserDetailsModel;
import com.stream.donalive.ui.utill.Constant;

import im.zego.zim.callback.ZIMCallInvitationSentCallback;
import im.zego.zim.entity.ZIMCallInvitationSentInfo;
import im.zego.zim.entity.ZIMError;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private CollectionReference usersRef;
    private UserDetailsModel userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        usersRef = firestore.collection(Constant.LOGIN_DETAILS);

        try {
            ZEGOSDKUser localUser = ZEGOSDKManager.getInstance().expressService.getCurrentUser();
            if (localUser!=null){
                binding.liveUserinfoUserid.setText(localUser.userID);
                binding.liveUserinfoUsername.setText(localUser.userName);
            }
        }catch (Exception e){

        }

        binding.startLiveStreaming.setOnClickListener(v -> {
//            String liveID = binding.liveIdStreaming.getEditText().getText().toString();
//            if (TextUtils.isEmpty(liveID)) {
//                binding.liveIdStreaming.setError("please input liveID");
//                return;
//            }
            List<String> permissions = Arrays.asList(permission.CAMERA, permission.RECORD_AUDIO);
            requestPermissionIfNeeded(permissions, new RequestCallback() {
                @Override
                public void onResult(boolean allGranted, @NonNull List<String> grantedList,
                                     @NonNull List<String> deniedList) {
                    if (allGranted) {
                        Intent intent = new Intent(MainActivity.this, LiveStreamingActivity.class);
                        intent.putExtra("host", true);
                        intent.putExtra("liveID", getSaltString(userDetails.getUserId()));
                        intent.putExtra("userId", userDetails.getUserId());
                        intent.putExtra("username", userDetails.getUsername());
                        intent.putExtra("uid", userDetails.getUid());
                        intent.putExtra("country_name", userDetails.getCountry_name());
                        intent.putExtra("image", userDetails.getImage());
                        intent.putExtra("level", userDetails.getLevel());
                        startActivity(intent);
                    }
                }
            });
        });

        binding.watchLiveStreaming.setOnClickListener(v -> {
            String liveID = binding.liveIdStreaming.getEditText().getText().toString();
            if (TextUtils.isEmpty(liveID)) {
                binding.liveIdStreaming.setError("please input liveID");
                return;
            }
            Intent intent = new Intent(MainActivity.this, LiveStreamingActivity.class);
            intent.putExtra("host", false);
            intent.putExtra("liveID", liveID);
            startActivity(intent);
        });

        binding.startLiveAudioroom.setOnClickListener(v -> {
//            String liveID = binding.liveIdAudioRoom.getEditText().getText().toString();
//            if (TextUtils.isEmpty(liveID)) {
//                binding.liveIdAudioRoom.setError("please input liveID");
//                return;
//            }
            List<String> permissions = Arrays.asList(permission.RECORD_AUDIO);
            requestPermissionIfNeeded(permissions, new RequestCallback() {
                @Override
                public void onResult(boolean allGranted, @NonNull List<String> grantedList,
                                     @NonNull List<String> deniedList) {
                    if (allGranted) {
                        Intent intent = new Intent(MainActivity.this, LiveAudioRoomActivity.class);
                        intent.putExtra("host", true);
                        intent.putExtra("liveID", getSaltString(userDetails.getUserId()));
                        intent.putExtra("userId", userDetails.getUserId());
                        intent.putExtra("username", userDetails.getUsername());
                        intent.putExtra("uid", userDetails.getUid());
                        intent.putExtra("country_name", userDetails.getCountry_name());
                        intent.putExtra("image", userDetails.getImage());
                        intent.putExtra("level", userDetails.getLevel());
                        startActivity(intent);
                    }
                }
            });
        });

        binding.watchLiveAudioroom.setOnClickListener(v -> {
            String liveID = binding.liveIdAudioRoom.getEditText().getText().toString();
            if (TextUtils.isEmpty(liveID)) {
                binding.liveIdAudioRoom.setError("please input liveID");
                return;
            }
            Intent intent = new Intent(MainActivity.this, LiveAudioRoomActivity.class);
            intent.putExtra("host", false);
            intent.putExtra("liveID", liveID);
            startActivity(intent);
        });

        binding.callUserVideo.setOnClickListener(v -> {
            String targetUserID = binding.callUserId.getEditText().getText().toString();
            if (TextUtils.isEmpty(targetUserID)) {
                binding.callUserId.setError("please input targetUserID");
                return;
            }
            List<String> permissions = Arrays.asList(permission.CAMERA, permission.RECORD_AUDIO);
            requestPermissionIfNeeded(permissions, new RequestCallback() {
                @Override
                public void onResult(boolean allGranted, @NonNull List<String> grantedList,
                                     @NonNull List<String> deniedList) {
                    if (allGranted) {
                        ZEGOCallInvitationManager.getInstance()
                                .sendVideoCall(targetUserID, new ZIMCallInvitationSentCallback() {
                                    @Override
                                    public void onCallInvitationSent(String requestID, ZIMCallInvitationSentInfo info,
                                                                     ZIMError errorInfo) {
                                        if (errorInfo.code.value() == 0) {
                                            ZEGOSDKUser localUser = ZEGOSDKManager.getInstance().expressService.getCurrentUser();
                                            FullCallInfo fullCallInfo = new FullCallInfo();
                                            fullCallInfo.callID = requestID;
                                            fullCallInfo.callType = CallExtendedData.VIDEO_CALL;
                                            fullCallInfo.callerUserID = localUser.userID;
                                            fullCallInfo.callerUserName = localUser.userName;
                                            fullCallInfo.calleeUserID = targetUserID;
                                            fullCallInfo.isOutgoingCall = true;
                                            CallWaitActivity.startActivity(MainActivity.this, fullCallInfo);
                                        }
                                    }
                                });
                    }
                }
            });
        });

        binding.callUserAudio.setOnClickListener(v -> {
            String targetUserID = binding.callUserId.getEditText().getText().toString();
            if (TextUtils.isEmpty(targetUserID)) {
                binding.callUserId.setError("please input targetUserID");
                return;
            }
            List<String> permissions = Collections.singletonList(permission.RECORD_AUDIO);
            requestPermissionIfNeeded(permissions, new RequestCallback() {
                @Override
                public void onResult(boolean allGranted, @NonNull List<String> grantedList,
                                     @NonNull List<String> deniedList) {
                    if (allGranted) {
                        ZEGOCallInvitationManager.getInstance()
                                .sendVoiceCall(targetUserID, new ZIMCallInvitationSentCallback() {
                                    @Override
                                    public void onCallInvitationSent(String requestID, ZIMCallInvitationSentInfo info,
                                                                     ZIMError errorInfo) {
                                        if (errorInfo.code.value() == 0) {
                                            ZEGOSDKUser localUser = ZEGOSDKManager.getInstance().expressService.getCurrentUser();
                                            FullCallInfo fullCallInfo = new FullCallInfo();
                                            fullCallInfo.callID = requestID;
                                            fullCallInfo.callType = CallExtendedData.VOICE_CALL;
                                            fullCallInfo.callerUserID = localUser.userID;
                                            fullCallInfo.callerUserName = localUser.userName;
                                            fullCallInfo.calleeUserID = targetUserID;
                                            fullCallInfo.isOutgoingCall = true;
                                            CallWaitActivity.startActivity(MainActivity.this, fullCallInfo);
                                        }
                                    }
                                });
                    }
                }
            });
        });

        // if LiveStreaming,init after user login,may receive pk request.
        ZEGOLiveStreamingManager.getInstance().init();
        // if Call invitation,init after user login,may receive call request.
        ZEGOCallInvitationManager.getInstance().init();
        Intent intent = new Intent(this, CallBackgroundService.class);
        startService(intent);

//        fetchUserDetails(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));
        fetchUserDetails(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));

    }


    protected String getSaltString(String SALTCHARS) {
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 20) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();

        Log.i("saltStr", "getSaltString: "+saltStr);
        return saltStr;

    }

    private void fetchUserDetails(String userId) {

        usersRef.whereEqualTo("userId", userId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Handle error
                        Log.e("test2334", "Listen failed: " + error.getMessage());
                        return;
                    }

                    for (DocumentSnapshot document : value) {
                        userDetails = document.toObject(UserDetailsModel.class);
//
                    }
                });


    }

    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            ZEGOSDKManager.getInstance().disconnectUser();
            ZEGOLiveStreamingManager.getInstance().removeUserData();
            ZEGOLiveStreamingManager.getInstance().removeUserListeners();
            // if Call invitation,init after user login,may receive call request.
            ZEGOCallInvitationManager.getInstance().removeUserData();
            ZEGOCallInvitationManager.getInstance().removeUserListeners();
            Intent intent = new Intent(this, CallBackgroundService.class);
            stopService(intent);
        }
    }

    private void requestPermissionIfNeeded(List<String> permissions, RequestCallback requestCallback) {
        boolean allGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
            }
        }
        if (allGranted) {
            requestCallback.onResult(true, permissions, new ArrayList<>());
            return;
        }

        PermissionX.init(this).permissions(permissions).onExplainRequestReason((scope, deniedList) -> {
            String message = "";
            if (permissions.size() == 1) {
                if (deniedList.contains(permission.CAMERA)) {
                    message = this.getString(R.string.permission_explain_camera);
                } else if (deniedList.contains(permission.RECORD_AUDIO)) {
                    message = this.getString(R.string.permission_explain_mic);
                }
            } else {
                if (deniedList.size() == 1) {
                    if (deniedList.contains(permission.CAMERA)) {
                        message = this.getString(R.string.permission_explain_camera);
                    } else if (deniedList.contains(permission.RECORD_AUDIO)) {
                        message = this.getString(R.string.permission_explain_mic);
                    }
                } else {
                    message = this.getString(R.string.permission_explain_camera_mic);
                }
            }
            scope.showRequestReasonDialog(deniedList, message, getString(R.string.ok));
        }).onForwardToSettings((scope, deniedList) -> {
            String message = "";
            if (permissions.size() == 1) {
                if (deniedList.contains(permission.CAMERA)) {
                    message = this.getString(R.string.settings_camera);
                } else if (deniedList.contains(permission.RECORD_AUDIO)) {
                    message = this.getString(R.string.settings_mic);
                }
            } else {
                if (deniedList.size() == 1) {
                    if (deniedList.contains(permission.CAMERA)) {
                        message = this.getString(R.string.settings_camera);
                    } else if (deniedList.contains(permission.RECORD_AUDIO)) {
                        message = this.getString(R.string.settings_mic);
                    }
                } else {
                    message = this.getString(R.string.settings_camera_mic);
                }
            }
            scope.showForwardToSettingsDialog(deniedList, message, getString(R.string.settings),
                    getString(R.string.cancel));
        }).request(new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, @NonNull List<String> grantedList,
                                 @NonNull List<String> deniedList) {
                if (requestCallback != null) {
                    requestCallback.onResult(allGranted, grantedList, deniedList);
                }
            }
        });
    }
}