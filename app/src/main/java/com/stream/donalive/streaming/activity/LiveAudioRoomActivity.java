package com.stream.donalive.streaming.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.stream.donalive.databinding.ActivityLiveAudioRoomBinding;
import com.stream.donalive.global.AppConstants;
import com.stream.donalive.global.ApplicationClass;
import com.stream.donalive.streaming.internal.ZEGOLiveAudioRoomManager;
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

public class LiveAudioRoomActivity extends AppCompatActivity {

    private ActivityLiveAudioRoomBinding binding;
    private String roomID;
    private String userId;
    private String username;
    private long uid;
    private LiveAudioRoomLayoutConfig seatLayoutConfig;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private CollectionReference usersRef;
    private UserDetailsModel userDetails;

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
        uid = getIntent().getLongExtra("uid",0);
//        String roomID = getIntent().getStringExtra("liveID");
        if (TextUtils.isEmpty(roomID)) {
            finish();
            return;
        }

//        getSupportActionBar().setTitle("Live Audio Room");

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
        ZEGOSDKManager.getInstance().loginRoom(roomID, chatRoom, new ZEGOSDKCallBack() {
            @Override
            public void onResult(int errorCode, String message) {
                if (errorCode != 0) {
                    Log.e(TAG, "onRoomLoginResult: error: " + errorCode);
                    finish();
                } else {

                    if (isHost) {
                        // save live data
                        saveLiveData(userId,uid,username,true,roomID,"1");

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
}