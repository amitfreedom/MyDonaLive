package com.stream.donalive.streaming.activity;

import android.Manifest.permission;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.stream.donalive.R;
import com.stream.donalive.databinding.ActivityLiveStreamingBinding;
import com.stream.donalive.global.AppConstants;
import com.stream.donalive.global.ApplicationClass;
import com.stream.donalive.streaming.internal.ZEGOLiveStreamingManager;
import com.stream.donalive.streaming.internal.ZEGOLiveStreamingManager.LiveStreamingListener;
import com.stream.donalive.streaming.internal.business.RoomRequestExtendedData;
import com.stream.donalive.streaming.internal.business.RoomRequestType;
import com.stream.donalive.streaming.internal.business.pk.PKService.PKInfo;
import com.stream.donalive.streaming.internal.sdk.ZEGOSDKManager;
import com.stream.donalive.streaming.internal.sdk.basic.ZEGOSDKCallBack;
import com.stream.donalive.streaming.internal.sdk.basic.ZEGOSDKUser;
import com.stream.donalive.streaming.internal.sdk.components.effect.BeautyDialog;
import com.stream.donalive.streaming.internal.sdk.express.ExpressService;
import com.stream.donalive.streaming.internal.sdk.express.IExpressEngineEventHandler;
import com.stream.donalive.streaming.internal.sdk.zim.IZIMEventHandler;
import com.stream.donalive.streaming.internal.utils.ToastUtil;
import com.stream.donalive.ui.home.ui.profile.models.UserDetailsModel;
import com.stream.donalive.ui.utill.Constant;

import im.zego.zegoexpress.callback.IZegoMixerStartCallback;
import im.zego.zegoexpress.callback.IZegoRoomLoginCallback;
import im.zego.zegoexpress.constants.ZegoPublisherState;
import im.zego.zegoexpress.constants.ZegoRoomStateChangedReason;
import im.zego.zegoexpress.constants.ZegoScenario;
import im.zego.zim.ZIM;
import im.zego.zim.callback.ZIMLoggedInCallback;
import im.zego.zim.callback.ZIMRoomEnteredCallback;
import im.zego.zim.entity.ZIMError;
import im.zego.zim.entity.ZIMRoomFullInfo;
import im.zego.zim.enums.ZIMConnectionEvent;
import im.zego.zim.enums.ZIMConnectionState;
import im.zego.zim.enums.ZIMErrorCode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.json.JSONObject;

public class LiveStreamingActivity extends AppCompatActivity {

    private ActivityLiveStreamingBinding binding;
    private String liveID;
    private String userId;
    private String username;
    private long uid;
    //    private AlertDialog inviteCoHostDialog;
    private AlertDialog zimReconnectDialog;
    private AlertDialog startPKDialog;
    private BeautyDialog beautyDialog;
    private SignInClient signInClient;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private CollectionReference usersRef;
    private UserDetailsModel userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLiveStreamingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        usersRef = firestore.collection(Constant.LOGIN_DETAILS);


        boolean isHost = getIntent().getBooleanExtra("host", true);
        userId = getIntent().getStringExtra("userId");
        liveID = getIntent().getStringExtra("liveID");
        username = getIntent().getStringExtra("username");
        uid = getIntent().getLongExtra("uid",0);

        binding.liveAudioroomTopbar.setRoomID(liveID);

        ZEGOLiveStreamingManager.getInstance().addRoomListeners();

        listenSDKEvent();

        binding.previewStart.setOnClickListener(v -> {
            loginRoom();
        });

//        fetchUserDetails(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));

        binding.previewBeauty.setOnClickListener(v -> {
            if (beautyDialog == null) {
                beautyDialog = new BeautyDialog(LiveStreamingActivity.this);
            }
            beautyDialog.show();
        });
        binding.liveBottomMenuBar.setBeautyButtonClickListener(v -> {
            if (beautyDialog == null) {
                beautyDialog = new BeautyDialog(LiveStreamingActivity.this);
            }
            beautyDialog.show();
        });

        if (isHost) {
            // join when click start
            ZEGOSDKManager.getInstance().expressService.openCamera(true);
            ZEGOSDKManager.getInstance().expressService.openMicrophone(true);
            binding.previewStart.setVisibility(View.VISIBLE);
            if (ZEGOSDKManager.getInstance().effectsService.isEffectSDKInit()) {
                binding.previewBeauty.setVisibility(View.VISIBLE);
            }
            binding.mainHostVideo.startPreviewOnly();

            ZEGOSDKUser currentUser = ZEGOSDKManager.getInstance().expressService.getCurrentUser();
            ZEGOLiveStreamingManager.getInstance().setHostUser(currentUser);
        } else {
            // join right now
            ZEGOSDKManager.getInstance().expressService.openCamera(false);
            ZEGOSDKManager.getInstance().expressService.openMicrophone(false);
            binding.previewStart.setVisibility(View.GONE);
            loginRoom();
        }
    }

    private void fetchUserDetails(String userId) {
        Log.i("test2334", "fetchUserDetails: "+userId);

        usersRef.whereEqualTo("userId", userId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Handle error
                        Log.e("test2334", "Listen failed: " + error.getMessage());
                        return;
                    }

                    for (DocumentSnapshot document : value) {
                        userDetails = document.toObject(UserDetailsModel.class);
                        updateUI(userDetails);
                        Log.e("test2334", "Listen UserDetailsModel: ");
//
                    }
                });


    }

    private void updateUI(UserDetailsModel userDetails) {
        Log.i("test2334", "updateUI: "+userDetails.getUserId());
        binding.liveAudioroomTopbar.setRoomID(userDetails.getUserId());
    }

    private void saveLiveData(String userId,long uid,String userName,boolean isHost,String liveID,String liveType) {

        long timestamp = System.currentTimeMillis();
        Map<String, Object> liveDetails = new HashMap<>();
        liveDetails.put("userId", userId);
        liveDetails.put("uid", uid);
        liveDetails.put("username", userName);
        liveDetails.put("photo", "https://restream.io/blog/content/images/size/w2000/2023/06/how-to-stream-live-video-on-your-website.JPG");
        liveDetails.put("tag", "");
        liveDetails.put("host", isHost);
        liveDetails.put("liveID", liveID);
        liveDetails.put("liveType", liveType);
        liveDetails.put("liveStatus", "online");
        liveDetails.put("startTime", timestamp);
        liveDetails.put("endTime", timestamp);

        // Add the login details to Firestore
        firestore.collection(Constant.LIVE_DETAILS)
                .add(liveDetails)
                .addOnSuccessListener(documentReference -> {
                    Log.i("documentReference", "documentReference: created ");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error adding details"+e,Toast.LENGTH_SHORT).show();
                    // Handle failure
                    Log.e("MainActivity", "Error adding  details", e);
                });


    }

    private void loginRoom() {

        ZEGOSDKManager.getInstance().loginRoom(liveID, ZegoScenario.BROADCAST, new ZEGOSDKCallBack() {
            @Override
            public void onResult(int errorCode, String message) {
                if (errorCode != 0) {
                    onJoinRoomFailed();
                } else {
                    onJoinRoomSuccess();

                    // save live data
                    saveLiveData(userId,uid,username,true,userId,"0");

                }
            }
        });
    }

    private void onJoinRoomFailed() {
        finish();
    }

    private void onJoinRoomSuccess() {
        binding.previewStart.setVisibility(View.GONE);
        binding.previewBeauty.setVisibility(View.GONE);
        binding.liveBottomMenuBar.setVisibility(View.VISIBLE);

        boolean isHost = getIntent().getBooleanExtra("host", true);
        if (isHost) {
            ZEGOLiveStreamingManager.getInstance().startPublishingStream();
        }

        ZEGOSDKManager.getInstance().expressService.startSoundLevelMonitor();

        int width = binding.getRoot().getWidth() / 4;
        binding.pkOtherVideoIcon.setCircleBackgroundRadius(width / 2);
        binding.pkSelfVideoIcon.setCircleBackgroundRadius(width / 2);
        binding.audienceMixSelfIcon.setCircleBackgroundRadius(width / 2);
        binding.audienceMixOtherIcon.setCircleBackgroundRadius(width / 2);
        binding.mainHostVideoIcon.setCircleBackgroundRadius(width);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            ZEGOLiveStreamingManager.getInstance().leave();
            updateLiveStatus(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));
        }
    }

    private void updateLiveStatus(String userId) {
        // Reference to the Firestore collection
        CollectionReference liveDetailsRef = firestore.collection(Constant.LIVE_DETAILS);

        // Create a query to find the document with the given userId
        Query query = liveDetailsRef.whereEqualTo("userId", userId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the document ID for the matched document
                    String documentId = document.getId();

                    long timestamp = System.currentTimeMillis();
                    Map<String, Object> updateDetails = new HashMap<>();
                    updateDetails.put("liveStatus", "offline");
                    updateDetails.put("endTime", timestamp);

                    // Update the liveType field from 0 to 1
                    liveDetailsRef.document(documentId)
                            .update(updateDetails)
                            .addOnSuccessListener(aVoid -> {
                                Log.i("UpdateLiveType", "liveType updated successfully for user with ID: " + userId);
                            })
                            .addOnFailureListener(e -> {
                                Log.e("UpdateLiveType", "Error updating liveType for user with ID: " + userId, e);
                            });
                }
            } else {
                Log.e("UpdateLiveType", "Error getting documents: ", task.getException());
            }
        });
    }


    public void listenSDKEvent() {
        ZEGOSDKManager.getInstance().expressService.addEventHandler(new IExpressEngineEventHandler() {
            @Override
            public void onCameraOpen(String userID, boolean open) {
                onRoomUserCameraOpen(userID, open);
            }

            @Override
            public void onReceiveStreamAdd(List<ZEGOSDKUser> userList) {
                List<ZEGOSDKUser> coHostUserList = new ArrayList<>();
                for (ZEGOSDKUser zegosdkUser : userList) {
                    Log.i("checkmethod", "onReceiveStreamAdd: "+zegosdkUser.userName);
                    if (ZEGOLiveStreamingManager.getInstance().isHost(zegosdkUser.userID)) {
                        binding.mainHostVideo.setUserID(zegosdkUser.userID);
                        binding.mainHostVideoIcon.setLetter(zegosdkUser.userName);
                        binding.mainHostVideo.setStreamID(zegosdkUser.getMainStreamID());
                        if (ZEGOLiveStreamingManager.getInstance().getPKInfo() == null) {
                            binding.mainHostVideo.setVisibility(View.VISIBLE);
                            binding.mainHostVideoLayout.setVisibility(View.VISIBLE);
                            binding.mainHostVideo.startPlayRemoteAudioVideo();
                        }
                    } else {
                        if (ZEGOLiveStreamingManager.getInstance().getPKInfo() == null) {
                            coHostUserList.add(zegosdkUser);
                        }
                    }
                }
                if (ZEGOLiveStreamingManager.getInstance().getPKInfo() == null) {
                    binding.liveCohostView.addUser(coHostUserList);
                }
            }

            @Override
            public void onReceiveStreamRemove(List<ZEGOSDKUser> userList) {
                List<ZEGOSDKUser> coHostUserList = new ArrayList<>();
                for (ZEGOSDKUser ZEGOSDKUser : userList) {
                    if (Objects.equals(binding.mainHostVideo.getUserID(), ZEGOSDKUser.userID)) {
                        binding.mainHostVideo.stopPlayRemoteAudioVideo();
                        binding.mainHostVideo.setStreamID("");
                        binding.mainHostVideo.setUserID("");
                        binding.mainHostVideoIcon.setLetter("");
                        binding.mainHostVideoLayout.setVisibility(View.GONE);

                    } else {
                        coHostUserList.add(ZEGOSDKUser);
                    }
                }
                binding.liveCohostView.removeUser(coHostUserList);
            }

            @Override
            public void onPublisherStateUpdate(String streamID, ZegoPublisherState state, int errorCode,
                                               JSONObject extendedData) {
                super.onPublisherStateUpdate(streamID, state, errorCode, extendedData);
                ZEGOSDKUser currentUser = ZEGOSDKManager.getInstance().expressService.getCurrentUser();
                if (state == ZegoPublisherState.PUBLISHING) {
                    if (ZEGOLiveStreamingManager.getInstance().isCoHost(currentUser.userID)) {
                        binding.liveCohostView.addUser(currentUser);
                    } else if (ZEGOLiveStreamingManager.getInstance().isCurrentUserHost()) {
                        binding.mainHostVideo.setUserID(currentUser.userID);
                        binding.mainHostVideoIcon.setLetter(currentUser.userName);
                        binding.mainHostVideo.setStreamID(streamID);
                        if (ZEGOLiveStreamingManager.getInstance().getPKInfo() == null) {
                            binding.mainHostVideoLayout.setVisibility(View.VISIBLE);
                        }
                    }
                } else if (state == ZegoPublisherState.NO_PUBLISH) {
                    if (streamID.endsWith("_host")) {
                        binding.mainHostVideo.setUserID("");
                        binding.mainHostVideoIcon.setLetter("");
                        binding.mainHostVideo.setStreamID("");
                        binding.mainHostVideo.stopPublishAudioVideo();
                        binding.mainHostVideoLayout.setVisibility(View.GONE);
                    } else {
                        binding.liveCohostView.removeUser(currentUser);
                    }
                }
            }

            @Override
            public void onUserLeft(List<ZEGOSDKUser> userList) {
                if (!ZEGOLiveStreamingManager.getInstance().isCurrentUserHost()) {
                    PKInfo pkInfo = ZEGOLiveStreamingManager.getInstance().getPKInfo();
                    if (pkInfo != null) {
                        for (ZEGOSDKUser zegosdkUser : userList) {
                            if (zegosdkUser.userID.equals(pkInfo.hostUserID)) {
                                ZEGOLiveStreamingManager.getInstance().stopPKBattle();
                            }
                        }
                    }
                }
            }

            @Override
            public void onRoomStateChanged(String roomID, ZegoRoomStateChangedReason reason, int errorCode,
                                           JSONObject extendedData) {
                if (reason == ZegoRoomStateChangedReason.RECONNECT_FAILED) {
                    if (zimReconnectDialog != null && zimReconnectDialog.isShowing()) {
                        zimReconnectDialog.dismiss();
                    }
                    AlertDialog.Builder builder = new Builder(LiveStreamingActivity.this);
                    builder.setTitle("ZEGO SDK Disconnected");
                    builder.setMessage("Reconnected ?");
                    builder.setPositiveButton(R.string.ok, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ZEGOSDKUser currentUser = ZEGOSDKManager.getInstance().expressService.getCurrentUser();

                            ZEGOSDKManager.getInstance().expressService.removeRoomData();
                            ZEGOSDKManager.getInstance().expressService.removeUserData();
                            ZEGOSDKManager.getInstance().zimService.removeRoomData();
                            ZEGOSDKManager.getInstance().zimService.removeUserData();
                            ZEGOLiveStreamingManager.getInstance().removeUserData();
                            ZEGOLiveStreamingManager.getInstance().removeRoomData();

                            ZEGOSDKManager.getInstance().zimService.connectUser(currentUser.userID,
                                    currentUser.userName, errorInfo -> {
                                        if (errorInfo.code == ZIMErrorCode.SUCCESS) {
                                            ZEGOSDKManager.getInstance().zimService.loginRoom(roomID,
                                                    new ZIMRoomEnteredCallback() {
                                                        @Override
                                                        public void onRoomEntered(ZIMRoomFullInfo roomInfo,
                                                                                  ZIMError errorInfo) {
                                                            ZEGOSDKManager.getInstance().expressService.loginRoom(roomID,
                                                                    new IZegoRoomLoginCallback() {
                                                                        @Override
                                                                        public void onRoomLoginResult(int errorCode1,
                                                                                                      JSONObject extendedData1) {

                                                                        }
                                                                    });
                                                        }
                                                    });
                                        }
                                    });
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    builder.create().show();
                }
            }
        });
        ZEGOSDKManager.getInstance().zimService.addEventHandler(new IZIMEventHandler() {

            @Override
            public void onOutgoingRoomRequestAccepted(String requestID, String extendedData) {
                RoomRequestExtendedData data = RoomRequestExtendedData.parse(extendedData);
                if (data != null && data.roomRequestType == RoomRequestType.REQUEST_COHOST) {
                    ExpressService expressService = ZEGOSDKManager.getInstance().expressService;
                    ZEGOSDKUser currentUser = expressService.getCurrentUser();
                    if (ZEGOLiveStreamingManager.getInstance().isAudience(currentUser.userID)) {
                        List<String> permissions = Arrays.asList(permission.CAMERA, permission.RECORD_AUDIO);
                        requestPermissionIfNeeded(permissions, new RequestCallback() {

                            @Override
                            public void onResult(boolean allGranted, @NonNull List<String> grantedList,
                                                 @NonNull List<String> deniedList) {
                                ZEGOLiveStreamingManager.getInstance().startCoHost();
                            }
                        });
                    }
                }
            }

            @Override
            public void onConnectionStateChanged(ZIM zim, ZIMConnectionState state, ZIMConnectionEvent event,
                                                 JSONObject extendedData) {
                if (state == ZIMConnectionState.DISCONNECTED) {
                    AlertDialog.Builder builder = new Builder(LiveStreamingActivity.this);
                    builder.setTitle("ZIM DisConnected");
                    builder.setMessage("Reconnected ?");
                    builder.setPositiveButton(R.string.ok, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ZEGOSDKUser currentUser = ZEGOSDKManager.getInstance().expressService.getCurrentUser();
                            String roomID = ZEGOSDKManager.getInstance().expressService.getCurrentRoomID();

                            ZEGOSDKManager.getInstance().zimService.removeRoomData();
                            ZEGOSDKManager.getInstance().zimService.removeUserData();
                            ZEGOLiveStreamingManager.getInstance().removeUserData();
                            ZEGOLiveStreamingManager.getInstance().removeRoomData();
                            ZEGOSDKManager.getInstance().zimService.connectUser(currentUser.userID,
                                    currentUser.userName, new ZIMLoggedInCallback() {
                                        @Override
                                        public void onLoggedIn(ZIMError errorInfo) {
                                            ZEGOSDKManager.getInstance().zimService.loginRoom(roomID,
                                                    new ZIMRoomEnteredCallback() {
                                                        @Override
                                                        public void onRoomEntered(ZIMRoomFullInfo roomInfo,
                                                                                  ZIMError errorInfo) {

                                                        }
                                                    });
                                        }
                                    });
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    zimReconnectDialog = builder.create();
                    zimReconnectDialog.show();
                }
            }
        });

        //        ZEGOSDKManager.getInstance().zimService.addIncomingRoomRequestListener(new IncomingRoomRequestListener() {
        //            @Override
        //            public void onInComingRoomRequestReceived(RoomRequest request) {
        //                ZEGOSDKUser currentUser = ZEGOSDKManager.getInstance().expressService.getcurrentUser();
        //                if (ZEGOLiveStreamingManager.getInstance().isAudience(currentUser.userID)) {
        //                    if (inviteCoHostDialog == null) {
        //                        Builder builder = new Builder(LiveStreamingActivity.this);
        //                        builder.setTitle("you received a new invitation");
        //
        //                        ZEGOSDKUser inviter = ZEGOSDKManager.getInstance().expressService.getUser(request.sender);
        //                        if (inviter != null) {
        //                            builder.setMessage(inviter.userName + " invite you to CoHost");
        //                        }
        //                        builder.setPositiveButton(R.string.ok, new OnClickListener() {
        //                            @Override
        //                            public void onClick(DialogInterface dialog, int which) {
        //                                ExpressService expressService = ZEGOSDKManager.getInstance().expressService;
        //                                expressService.openMicrophone(true);
        //                                expressService.enableCamera(true);
        //                                expressService.startPublishLocalAudioVideo();
        //                                dialog.dismiss();
        //                            }
        //                        });
        //                        builder.setNegativeButton(R.string.cancel, new OnClickListener() {
        //                            @Override
        //                            public void onClick(DialogInterface dialog, int which) {
        //                                dialog.dismiss();
        //                            }
        //                        });
        //                        inviteCoHostDialog = builder.create();
        //                    }
        //                    if (!inviteCoHostDialog.isShowing()) {
        //                        inviteCoHostDialog.show();
        //                    }
        //                }
        //            }
        //
        //            @Override
        //            public void onInComingRoomRequestCancelled(RoomRequest request) {
        //                if (inviteCoHostDialog != null && inviteCoHostDialog.isShowing()) {
        //                    inviteCoHostDialog.dismiss();
        //                }
        //            }
        //
        //            @Override
        //            public void onActionAcceptIncomingRoomRequest(int errorCode, RoomRequest request) {
        //            }
        //
        //            @Override
        //            public void onActionRejectIncomingRoomRequest(int errorCode, RoomRequest request) {
        //            }
        //        });

        ZEGOLiveStreamingManager.getInstance().addLiveStreamingListener(new LiveStreamingListener() {

            @Override
            public void onReceiveStartPKRequest(String requestID, String inviter, String inviterName, String roomId) {
                if (startPKDialog != null && startPKDialog.isShowing()) {
                    return;
                }
                AlertDialog.Builder startPKBuilder = new AlertDialog.Builder(LiveStreamingActivity.this);
                startPKBuilder.setTitle(inviterName + " invite you pkbattle");
                startPKBuilder.setPositiveButton(R.string.ok, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PKInfo pkInfo = new PKInfo(new ZEGOSDKUser(inviter, inviterName), roomId);
                        ZEGOLiveStreamingManager.getInstance().setCurrentPKInfo(pkInfo);
                        ZEGOLiveStreamingManager.getInstance().acceptPKBattleStartRequest(requestID);
                    }
                });
                startPKBuilder.setNegativeButton(R.string.reject, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ZEGOLiveStreamingManager.getInstance().rejectPKBattleStartRequest(requestID);
                    }
                });
                startPKDialog = startPKBuilder.create();
                startPKDialog.setCanceledOnTouchOutside(false);
                startPKDialog.show();
            }

            @Override
            public void onReceiveStopPKRequest(String requestID) {
                PKInfo pkInfo = ZEGOLiveStreamingManager.getInstance().getPKInfo();
                ToastUtil.show(LiveStreamingActivity.this, pkInfo.pkUser.userName + " has Stopped PK");
            }

            @Override
            public void onInComingStartPKRequestTimeout(String requestID) {
                if (startPKDialog != null && startPKDialog.isShowing()) {
                    startPKDialog.dismiss();
                }
            }

            @Override
            public void onInComingStartPKRequestCancelled(String requestID) {
                if (startPKDialog != null && startPKDialog.isShowing()) {
                    startPKDialog.dismiss();
                }
            }

            @Override
            public void onPKStarted() {
                PKInfo pkInfo = ZEGOLiveStreamingManager.getInstance().getPKInfo();
                onRoomPKStarted(pkInfo);
            }

            @Override
            public void onPKEnded() {
                onRoomPKEnded();
            }

            @Override
            public void onPKSEITimeOut(String userID, boolean timeout) {
                if (ZEGOLiveStreamingManager.getInstance().isCurrentUserHost()) {
                    if (userID.equals(binding.pkOtherVideo.getUserID())) {
                        if (timeout) {
                            binding.pkOtherVideoTips.setVisibility(View.VISIBLE);
                            binding.pkOtherVideoMute.setVisibility(View.GONE);
                            binding.pkOtherVideo.mutePlayAudio(true);
                            boolean pkUserMuted = ZEGOLiveStreamingManager.getInstance().isPKUserMuted();
                            if (!pkUserMuted) {
                                ZEGOLiveStreamingManager.getInstance().mutePKUser(true, new IZegoMixerStartCallback() {
                                    @Override
                                    public void onMixerStartResult(int errorCode, JSONObject extendedData) {
                                        if (errorCode == 0) {
                                            binding.pkOtherVideoMute.setText("Unmute user");
                                        }
                                    }
                                });
                            }
                        } else {
                            binding.pkOtherVideoTips.setVisibility(View.GONE);
                            binding.pkOtherVideoMute.setVisibility(View.VISIBLE);
                            binding.pkOtherVideo.mutePlayAudio(false);
                            boolean pkUserMuted = ZEGOLiveStreamingManager.getInstance().isPKUserMuted();
                            if (pkUserMuted) {
                                ZEGOLiveStreamingManager.getInstance().mutePKUser(false, new IZegoMixerStartCallback() {
                                    @Override
                                    public void onMixerStartResult(int errorCode, JSONObject extendedData) {
                                        if (errorCode == 0) {
                                            binding.pkOtherVideoMute.setText("Mute user");
                                        }
                                    }
                                });
                            }
                        }
                    }
                } else {
                    if (ZEGOLiveStreamingManager.getInstance().isHost(userID)) {
                        if (timeout) {
                            if (binding.audienceMixSelfTips.getVisibility() != View.VISIBLE) {
                                binding.audienceMixSelfTips.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (binding.audienceMixSelfTips.getVisibility() != View.GONE) {
                                binding.audienceMixSelfTips.setVisibility(View.GONE);
                            }
                        }
                    } else if (ZEGOLiveStreamingManager.getInstance().isPKUser(userID)) {
                        if (timeout) {
                            if (binding.audienceMixOtherTips.getVisibility() != View.VISIBLE) {
                                binding.audienceMixOtherTips.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (binding.audienceMixOtherTips.getVisibility() != View.GONE) {
                                binding.audienceMixOtherTips.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onPKCameraOpen(String userID, boolean open) {
                if (ZEGOLiveStreamingManager.getInstance().isPKUser(userID)) {
                    onPKUserCameraUpdate(userID, open);
                } else if (ZEGOLiveStreamingManager.getInstance().isHost(userID)) {
                    onHostCameraUpdate(open);
                }
            }

            @Override
            public void onPKMicrophoneOpen(String userID, boolean open) {
            }
        });
    }

    private void onRoomUserCameraOpen(String userID, boolean open) {
        if (ZEGOLiveStreamingManager.getInstance().getPKInfo() == null) {
            if (ZEGOLiveStreamingManager.getInstance().isHost(userID)) {
                if (open) {
                    binding.mainHostVideo.setVisibility(View.VISIBLE);
                    binding.mainHostVideoIcon.setVisibility(View.GONE);
                } else {
                    binding.mainHostVideo.setVisibility(View.INVISIBLE);
                    binding.mainHostVideoIcon.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (ZEGOLiveStreamingManager.getInstance().isHost(userID)) {
                onHostCameraUpdate(open);
            }
        }
    }

    private void onRoomPKStarted(PKInfo pkInfo) {
        ZEGOSDKUser currentUser = ZEGOSDKManager.getInstance().expressService.getCurrentUser();

        binding.liveCohostView.setVisibility(View.GONE);
        binding.pkVideoLayout.setVisibility(View.VISIBLE);
        binding.mainHostVideoLayout.setVisibility(View.GONE);

        ZEGOSDKUser hostUser = ZEGOLiveStreamingManager.getInstance().getHostUser();
        if (ZEGOLiveStreamingManager.getInstance().isCurrentUserHost()) {
            binding.pkOtherVideoLayout.setVisibility(View.VISIBLE);
            binding.pkSelfVideoLayout.setVisibility(View.VISIBLE);
            binding.pkOtherVideo.setUserID(pkInfo.pkUser.userID);
            binding.pkOtherVideoIcon.setLetter(pkInfo.pkUser.userName);
            binding.pkOtherVideo.setStreamID(pkInfo.getPKStream());
            binding.pkOtherVideo.startPlayRemoteAudioVideo();

            binding.pkSelfVideo.setUserID(currentUser.userID);
            binding.pkSelfVideoIcon.setLetter(currentUser.userName);
            binding.pkSelfVideo.startPreviewOnly();
        } else {

            binding.pkOtherVideoLayout.setVisibility(View.INVISIBLE);
            binding.pkSelfVideoLayout.setVisibility(View.INVISIBLE);

            binding.audienceMixSelfIcon.setLetter(hostUser.userName);
            binding.audienceMixOtherIcon.setLetter(pkInfo.pkUser.userName);

            String streamID = ZEGOSDKManager.getInstance().expressService.getCurrentRoomID() + "_mix";
            binding.audienceMixVideo.setStreamID(streamID);
            binding.audienceMixVideo.startPlayRemoteAudioVideo();


        }

        onHostCameraUpdate(hostUser.isCameraOpen());

        binding.pkOtherVideoMute.setOnClickListener(v -> {
            boolean pkUserMuted = ZEGOLiveStreamingManager.getInstance().isPKUserMuted();
            ZEGOLiveStreamingManager.getInstance().mutePKUser(!pkUserMuted, new IZegoMixerStartCallback() {
                @Override
                public void onMixerStartResult(int errorCode, JSONObject extendedData) {
                    if (errorCode == 0) {
                        if (pkUserMuted) {
                            binding.pkOtherVideoMute.setText("Mute user");
                        } else {
                            binding.pkOtherVideoMute.setText("Unmute user");
                        }
                    }
                }
            });
        });
    }

    private void onRoomPKEnded() {
        ZEGOSDKUser hostUser = ZEGOLiveStreamingManager.getInstance().getHostUser();
        binding.pkVideoLayout.setVisibility(View.INVISIBLE);
        if (hostUser != null) {
            binding.mainHostVideoLayout.setVisibility(View.VISIBLE);
        }
        binding.liveCohostView.setVisibility(View.VISIBLE);

        if (ZEGOLiveStreamingManager.getInstance().isCurrentUserHost()) {
            binding.pkOtherVideo.stopPlayRemoteAudioVideo();
            binding.pkOtherVideo.setUserID("");
            binding.pkOtherVideoIcon.setLetter("");
            binding.pkOtherVideo.setStreamID("");

            binding.mainHostVideo.startPreviewOnly();
        } else {
            binding.audienceMixVideo.stopPlayRemoteAudioVideo();
            binding.audienceMixVideo.setStreamID("");

            if (hostUser != null) {
                String hostMainStreamID = hostUser.getMainStreamID();
                if (hostMainStreamID != null) {
                    binding.mainHostVideo.startPlayRemoteAudioVideo();
                }
            }
        }

        if (hostUser != null) {
            onRoomUserCameraOpen(hostUser.userID, hostUser.isCameraOpen());
        }
    }

    private void onPKUserCameraUpdate(String userID, boolean open) {
        if (ZEGOLiveStreamingManager.getInstance().isCurrentUserHost()) {
            if (open) {
                binding.pkOtherVideoIcon.setVisibility(View.INVISIBLE);
                binding.pkOtherVideo.setVisibility(View.VISIBLE);
            } else {
                binding.pkOtherVideoIcon.setVisibility(View.VISIBLE);
                binding.pkOtherVideo.setVisibility(View.INVISIBLE);
            }
        } else {
            if (open) {
                binding.audienceMixOtherIcon.setVisibility(View.INVISIBLE);
            } else {
                binding.audienceMixOtherIcon.setVisibility(View.VISIBLE);
            }
        }
    }

    private void onHostCameraUpdate(boolean open) {
        if (ZEGOLiveStreamingManager.getInstance().isCurrentUserHost()) {
            if (open) {
                binding.pkSelfVideoIcon.setVisibility(View.INVISIBLE);
                binding.pkSelfVideo.setVisibility(View.VISIBLE);
            } else {
                binding.pkSelfVideoIcon.setVisibility(View.VISIBLE);
                binding.pkSelfVideo.setVisibility(View.INVISIBLE);
            }
        } else {
            if (open) {
                binding.audienceMixSelfIcon.setVisibility(View.INVISIBLE);
            } else {
                binding.audienceMixSelfIcon.setVisibility(View.VISIBLE);
            }
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