package com.stream.donalive.ui.home.ui.explore;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stream.donalive.R;
import com.stream.donalive.databinding.FragmentExploreBinding;
import com.stream.donalive.databinding.FragmentLiveBinding;
import com.stream.donalive.streaming.activity.LiveStreamingActivity;
import com.stream.donalive.streaming.internal.ZEGOCallInvitationManager;
import com.stream.donalive.streaming.internal.ZEGOLiveStreamingManager;
import com.stream.donalive.streaming.internal.sdk.ZEGOSDKManager;

public class ExploreFragment extends Fragment {

    private FragmentExploreBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        binding.watchLiveStreaming.setOnClickListener(v -> {

            String liveID = "123456";
            if (TextUtils.isEmpty(liveID)) {
                return;
            }
            Intent intent = new Intent(getActivity().getApplication(), LiveStreamingActivity.class);
            intent.putExtra("host", false);
            intent.putExtra("liveID", liveID);
            startActivity(intent);
        });

        // if LiveStreaming,init after user login,may receive pk request.
        ZEGOLiveStreamingManager.getInstance().init();
        // if Call invitation,init after user login,may receive call request.
        ZEGOCallInvitationManager.getInstance().init();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



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
//            Intent intent = new Intent(this, CallBackgroundService.class);
//            stopService(intent);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("checkmethod", "onDestroyView: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("checkmethod", "onDetach: ");
    }
}