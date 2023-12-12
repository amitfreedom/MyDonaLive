package com.stream.donalive.ui.home.ui.live;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.stream.donalive.R;
import com.stream.donalive.databinding.FragmentLiveBinding;
import com.stream.donalive.global.AppConstants;
import com.stream.donalive.global.ApplicationClass;
import com.stream.donalive.streaming.ZEGOSDKKeyCenter;
import com.stream.donalive.streaming.activity.LiveAudioRoomActivity;
import com.stream.donalive.streaming.activity.LiveStreamingActivity;
import com.stream.donalive.streaming.components.call.CallBackgroundService;
import com.stream.donalive.streaming.internal.ZEGOCallInvitationManager;
import com.stream.donalive.streaming.internal.ZEGOLiveStreamingManager;
import com.stream.donalive.streaming.internal.sdk.ZEGOSDKManager;
import com.stream.donalive.streaming.internal.sdk.basic.ZEGOSDKCallBack;
import com.stream.donalive.streaming.internal.sdk.basic.ZEGOSDKUser;
import com.stream.donalive.ui.activity.MainActivity;
import com.stream.donalive.ui.home.ui.profile.models.UserDetailsModel;
import com.stream.donalive.ui.utill.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LiveFragment extends Fragment {

    private FragmentLiveBinding binding;
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private FirebaseAuth mAuth;
    private UserDetailsModel userDetails;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLiveBinding.inflate(inflater, container, false);
//        initZEGOSDK();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(Constant.LOGIN_DETAILS);
        fetchUserDetails(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));


        binding.liveLoginBtn.setOnClickListener(v -> {
            String userID = userDetails.getUserId();
            String userName = String.valueOf(userDetails.getUsername());
            if (TextUtils.isEmpty(userID) || TextUtils.isEmpty(userName)) {
                Toast.makeText(getActivity(), "User details not found", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
//            signInZEGOSDK(userID, userName, (errorCode, message) -> {
//                if (errorCode == 0) {
//                    Intent intent = new Intent(getActivity(), MainActivity.class);
//                    startActivity(intent);
//                }
//            });

        });

//        initZEGOSDK();



    }

    private void initZEGOSDK() {

        ZEGOSDKManager.getInstance().initSDK(getActivity().getApplication(), ZEGOSDKKeyCenter.appID, ZEGOSDKKeyCenter.appSign);
        ZEGOSDKManager.getInstance().enableZEGOEffects(true);
    }

    private void fetchUserDetails(String userId) {

        usersRef.whereEqualTo("userId", userId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Handle error
                        Log.e("FirestoreListener", "Listen failed: " + error.getMessage());
                        return;
                    }

                    for (DocumentSnapshot document : value) {
                        userDetails = document.toObject(UserDetailsModel.class);
                        updateUI(userDetails);
                    }

                    // Now userDetailsList contains UserDetails objects from Firestore
                    // Use the list as needed (e.g., display in UI, perform operations)
                });

//
    }

    private void updateUI(UserDetailsModel userDetails) {
//        signInZEGOSDK(userDetails.getUserId(), String.valueOf(userDetails.getUid()), (errorCode, message) -> {
//            if (errorCode == 0) {
//                Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void signInZEGOSDK(String userID, String userName, ZEGOSDKCallBack callback) {
        ZEGOSDKManager.getInstance().connectUser(userID, userName, callback);
    }

    @Override
    public void onPause() {
        super.onPause();

//            ZEGOSDKManager.getInstance().disconnectUser();
//            ZEGOLiveStreamingManager.getInstance().removeUserData();
//            ZEGOLiveStreamingManager.getInstance().removeUserListeners();
//            // if Call invitation,init after user login,may receive call request.
//            ZEGOCallInvitationManager.getInstance().removeUserData();
//            ZEGOCallInvitationManager.getInstance().removeUserListeners();
//            Intent intent = new Intent(getActivity(), CallBackgroundService.class);
//            getActivity().stopService(intent);

    }

    private void requestPermissionIfNeeded(List<String> permissions, RequestCallback requestCallback) {
        boolean allGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
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
                if (deniedList.contains(Manifest.permission.CAMERA)) {
                    message = this.getString(R.string.permission_explain_camera);
                } else if (deniedList.contains(Manifest.permission.RECORD_AUDIO)) {
                    message = this.getString(R.string.permission_explain_mic);
                }
            } else {
                if (deniedList.size() == 1) {
                    if (deniedList.contains(Manifest.permission.CAMERA)) {
                        message = this.getString(R.string.permission_explain_camera);
                    } else if (deniedList.contains(Manifest.permission.RECORD_AUDIO)) {
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
                if (deniedList.contains(Manifest.permission.CAMERA)) {
                    message = this.getString(R.string.settings_camera);
                } else if (deniedList.contains(Manifest.permission.RECORD_AUDIO)) {
                    message = this.getString(R.string.settings_mic);
                }
            } else {
                if (deniedList.size() == 1) {
                    if (deniedList.contains(Manifest.permission.CAMERA)) {
                        message = this.getString(R.string.settings_camera);
                    } else if (deniedList.contains(Manifest.permission.RECORD_AUDIO)) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        Log.i("testFun", "onDestroyView:========= ");

    }
}