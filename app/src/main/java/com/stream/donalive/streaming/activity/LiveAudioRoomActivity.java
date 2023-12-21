package com.stream.donalive.streaming.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.stream.donalive.R;
import com.stream.donalive.databinding.ActivityLiveAudioRoomBinding;
import com.stream.donalive.global.AppConstants;
import com.stream.donalive.global.ApplicationClass;
import com.stream.donalive.streaming.ZEGOSDKKeyCenter;
import com.stream.donalive.streaming.gift.GiftHelper;
import com.stream.donalive.streaming.internal.ZEGOLiveAudioRoomManager;
import com.stream.donalive.streaming.internal.ZEGOLiveStreamingManager;
import com.stream.donalive.streaming.internal.business.RoomRequestExtendedData;
import com.stream.donalive.streaming.internal.business.RoomRequestType;
import com.stream.donalive.streaming.internal.business.audioroom.LiveAudioRoomLayoutConfig;
import com.stream.donalive.streaming.internal.sdk.ZEGOSDKManager;
import com.stream.donalive.streaming.internal.sdk.basic.ZEGOSDKCallBack;
import com.stream.donalive.streaming.internal.sdk.express.IExpressEngineEventHandler;
import com.stream.donalive.streaming.internal.sdk.zim.IZIMEventHandler;
import com.stream.donalive.streaming.internal.utils.ToastUtil;
import com.stream.donalive.streaming.internal.utils.Utils;
import com.stream.donalive.ui.home.ui.profile.models.UserDetailsModel;
import com.stream.donalive.ui.utill.Constant;
import com.zegocloud.uikit.prebuilt.liveaudioroom.ZegoUIKitPrebuiltLiveAudioRoomConfig;
import com.zegocloud.uikit.prebuilt.liveaudioroom.ZegoUIKitPrebuiltLiveAudioRoomFragment;

import im.zego.zegoexpress.constants.ZegoScenario;
import im.zego.zegoexpress.constants.ZegoStreamResourceMode;
import im.zego.zegoexpress.constants.ZegoUpdateType;
import im.zego.zegoexpress.entity.ZegoPlayerConfig;
import im.zego.zegoexpress.entity.ZegoStream;
import im.zego.zim.callback.ZIMRoomAttributesOperatedCallback;
import im.zego.zim.entity.ZIMError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class LiveAudioRoomActivity extends AppCompatActivity {

    private ActivityLiveAudioRoomBinding binding;
    private String roomID;
    private String userId;
    private String username;
    private String country;
    private String image;
    private long uid;
    private LiveAudioRoomLayoutConfig seatLayoutConfig;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private CollectionReference usersRef;
    private UserDetailsModel userDetails;
    private String level;
    View giftButton;

    String TAG = "LiveAudioRoomActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLiveAudioRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        usersRef = firestore.collection(Constant.LOGIN_DETAILS);


        boolean isHost = getIntent().getBooleanExtra("host", true);
        userId = getIntent().getStringExtra("userId");
        roomID = getIntent().getStringExtra("liveID");
        username = getIntent().getStringExtra("username");
        country = getIntent().getStringExtra("country_name");
        image = getIntent().getStringExtra("image");
        level = getIntent().getStringExtra("level");
        uid = getIntent().getLongExtra("uid",0);
        if (TextUtils.isEmpty(roomID)) {
            finish();
            return;
        }


        fetchUserDetails(userId);
        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exitDialog();
            }
        });




        binding.liveAudioroomTopbar.setRoomID(roomID);
        // two rows, four columns
        seatLayoutConfig = new LiveAudioRoomLayoutConfig();
        seatLayoutConfig.rowSpacing = Utils.dp2px(8, getResources().getDisplayMetrics());
        ZEGOLiveAudioRoomManager.getInstance().init(seatLayoutConfig);
        binding.seatContainer.setLayoutConfig(seatLayoutConfig);

        ZEGOSDKManager.getInstance().expressService.openCamera(false);
        ZEGOSDKManager.getInstance().expressService.addEventHandler(new IExpressEngineEventHandler() {
            @Override
            public void onRoomStreamUpdate(String roomID, ZegoUpdateType updateType, ArrayList<ZegoStream> streamList,
                                           JSONObject extendedData) {
                super.onRoomStreamUpdate(roomID, updateType, streamList, extendedData);
                for (ZegoStream zegoStream : streamList) {
                    if (updateType == ZegoUpdateType.ADD) {
                        ZegoPlayerConfig config = new ZegoPlayerConfig();
                        config.resourceMode = ZegoStreamResourceMode.ONLY_RTC;
                        ZEGOSDKManager.getInstance().expressService.startPlayingStream(zegoStream.streamID, config);
                    } else {
                        ZEGOSDKManager.getInstance().expressService.stopPlayingStream(zegoStream.streamID);
                    }
                }
            }
        });
        ZegoScenario chatRoom = ZegoScenario.HIGH_QUALITY_CHATROOM;
        binding.topView.setVisibility(View.VISIBLE);
        ZEGOSDKManager.getInstance().loginRoom(roomID, chatRoom, new ZEGOSDKCallBack() {
            @Override
            public void onResult(int errorCode, String message) {
                if (errorCode != 0) {
                    Log.e(TAG, "onRoomLoginResult: error: " + errorCode);
                    finish();
                } else {



                    if (isHost) {
                        // save live data
                        saveLiveData(userId,uid,username,true,roomID,"1",country,image);

                        ZEGOLiveAudioRoomManager.getInstance().setHostAndLockSeat();
                        ZEGOLiveAudioRoomManager.getInstance().takeSeat(0, new ZIMRoomAttributesOperatedCallback() {
                            @Override
                            public void onRoomAttributesOperated(String roomID, ArrayList<String> errorKeys,
                                                                 ZIMError errorInfo) {

                            }
                        });
                    }
                    initListenerAfterLoginRoom();
                }
            }
        });
        // add a gift button to liveAudioRoom audience
        GiftHelper giftHelper = new GiftHelper(findViewById(R.id.layout), String.valueOf(uid), username);
        giftButton = giftHelper.getGiftButton(this, ZEGOSDKKeyCenter.appID, ZEGOSDKKeyCenter.serverSecret, roomID);

        // Get reference to the giftButtonContainer
        FrameLayout giftButtonContainer = findViewById(R.id.giftButtonContainer);
        giftButtonContainer.addView(giftButton);

        giftButton.post(new Runnable() {
            @Override
            public void run() {
                giftButton.performClick();
            }
        });


    }

    private long pressedTime;
    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
//            finish();
        } else {
//            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            exitDialog();
        }
        pressedTime = System.currentTimeMillis();
    }

    private void exitDialog() {

        new MaterialAlertDialogBuilder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to leave ?")
                .setCancelable(false)
                .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ZEGOLiveAudioRoomManager.getInstance().leave();
                        boolean isHost = getIntent().getBooleanExtra("host", true);
                        if (isHost){
                            updateLiveStatus(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));

                        }
                        onBackPressed();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
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
        if(userDetails.getImage()!="") {
            Glide.with(this).load(userDetails.getImage()).into(binding.ivUserImage);
        }else {
            Glide.with(this).load(Constant.USER_PLACEHOLDER_PATH).into(binding.ivUserImage);
        }
        binding.txtUsername.setText(userDetails.getUsername());
        binding.txtUid.setText("ID : "+String.valueOf(userDetails.getUid()));
        binding.txtLevel.setText("Lv"+userDetails.getLevel());
        binding.txtCoin.setText(userDetails.getCoins());
    }

    private void saveLiveData(String userId, long uid, String userName, boolean isHost, String liveID, String liveType, String country,String image) {

        long timestamp = System.currentTimeMillis();
        Map<String, Object> liveDetails = new HashMap<>();
        liveDetails.put("userId", userId);
        liveDetails.put("uid", uid);
        liveDetails.put("username", userName);
        liveDetails.put("photo", image!=""?image:Constant.USER_PLACEHOLDER_PATH);
        liveDetails.put("tag", "");
        liveDetails.put("host", isHost);
        liveDetails.put("liveID", liveID);
        liveDetails.put("liveType", liveType);
        liveDetails.put("liveStatus", "online");
        liveDetails.put("startTime", timestamp);
        liveDetails.put("endTime", timestamp);
        liveDetails.put("country", country);

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
    private void initListenerAfterLoginRoom() {
        ZEGOSDKManager.getInstance().zimService.addEventHandler(new IZIMEventHandler() {
            @Override
            public void onOutgoingRoomRequestAccepted(String requestID, String extendedData) {
                RoomRequestExtendedData data = RoomRequestExtendedData.parse(extendedData);
                if (data != null && data.roomRequestType == RoomRequestType.REQUEST_TAKE_SEAT) {
                    int seatIndex = ZEGOLiveAudioRoomManager.getInstance().findFirstAvailableSeatIndex();
                    if (seatIndex != -1) {
                        ZEGOLiveAudioRoomManager.getInstance()
                                .takeSeat(seatIndex, new ZIMRoomAttributesOperatedCallback() {
                                    @Override
                                    public void onRoomAttributesOperated(String roomID, ArrayList<String> errorKeys,
                                                                         ZIMError errorInfo) {

                                    }
                                });
                    } else {
                        ToastUtil.show(LiveAudioRoomActivity.this, "Cannot find available seat");
                    }
                }
            }

            @Override
            public void onUserAvatarUpdated(String userID, String url) {
                super.onUserAvatarUpdated(userID, url);
                binding.seatContainer.onUserAvatarUpdated(userID, url);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            ZEGOLiveAudioRoomManager.getInstance().leave();
            boolean isHost = getIntent().getBooleanExtra("host", true);
            if (isHost){
                updateLiveStatus(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));

            }
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
}