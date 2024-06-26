package com.stream.prettylive.streaming.components.audioroom;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.stream.prettylive.R;
import com.stream.prettylive.streaming.components.RoomRequestListDialog;
import com.stream.prettylive.streaming.components.message.barrage.BottomInputDialog;
import com.stream.prettylive.streaming.internal.ZEGOLiveAudioRoomManager;
import com.stream.prettylive.streaming.internal.ZEGOLiveAudioRoomManager.LiveAudioRoomListener;
import com.stream.prettylive.streaming.internal.business.RoomRequestType;
import com.stream.prettylive.streaming.internal.business.audioroom.LiveAudioRoomSeat;
import com.stream.prettylive.streaming.internal.sdk.ZEGOSDKManager;
import com.stream.prettylive.streaming.internal.sdk.basic.ZEGOSDKUser;
import com.stream.prettylive.streaming.internal.sdk.components.express.AudioOutputButton;
import com.stream.prettylive.streaming.internal.sdk.components.express.GiftButton;
import com.stream.prettylive.streaming.internal.sdk.components.express.ToggleMicrophoneButton;
import com.stream.prettylive.streaming.internal.sdk.express.IExpressEngineEventHandler;
import com.stream.prettylive.streaming.internal.utils.Utils;

import java.util.List;

public class BottomMenuBar extends LinearLayout {


    private LinearLayout childLinearLayout;
    private ToggleMicrophoneButton microphoneButton;
    private LockSeatButton lockSeatButton;
    private TakeSeatButton takeSeatButton;
    private RoomRequestButton takeSeatRequestListButton;
    private AudioOutputButton audioOutputButton;
    private GiftButton giftButton;

    public BottomMenuBar(Context context) {
        super(context);
        initView();
    }

    public BottomMenuBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BottomMenuBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public BottomMenuBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
//        setOrientation(LinearLayout.HORIZONTAL);
//        setLayoutParams(new LayoutParams(-1, -2));
//        setGravity(Gravity.END);
//
//        ImageView messageButton = new ImageView(getContext());
//        messageButton.setImageResource(R.drawable.audioroom_icon_im);
//        messageButton.setScaleType(ImageView.ScaleType.FIT_XY);
//        addView(messageButton, generateChildImageLayoutParams());
//        messageButton.setOnClickListener(v -> {
//            if (getContext() instanceof Activity) {
//                BottomInputDialog bottomInputDialog = new BottomInputDialog(getContext());
//                bottomInputDialog.show();
//            }
//        });

        childLinearLayout = new LinearLayout(getContext());
        childLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        childLinearLayout.setGravity(Gravity.END);
        LayoutParams params = new LayoutParams(0, -2, 1);
        addView(childLinearLayout, params);
        int paddingEnd = Utils.dp2px(8, getResources().getDisplayMetrics());
        childLinearLayout.setPadding(0, 0, paddingEnd, 0);

        microphoneButton = new ToggleMicrophoneButton(getContext());
        boolean microphoneOpen = ZEGOSDKManager.getInstance().expressService.isMicrophoneOpen();
        microphoneButton.updateState(microphoneOpen);
        childLinearLayout.addView(microphoneButton, generateChildImageLayoutParams());
        ZEGOSDKManager.getInstance().expressService.addEventHandler(new IExpressEngineEventHandler() {
            @Override
            public void onMicrophoneOpen(String userID, boolean open) {
                ZEGOSDKUser localUser = ZEGOSDKManager.getInstance().expressService.getCurrentUser();
                if (userID.equals(localUser.userID)) {
                    microphoneButton.updateState(open);
                }
            }
        });

        lockSeatButton = new LockSeatButton(getContext());
        childLinearLayout.addView(lockSeatButton, generateChildImageLayoutParams());

        takeSeatButton = new TakeSeatButton(getContext());
        childLinearLayout.addView(takeSeatButton, generateChildTextLayoutParams());

        takeSeatRequestListButton = new RoomRequestButton(getContext());
        takeSeatRequestListButton.setRoomRequestType(RoomRequestType.REQUEST_TAKE_SEAT);
        takeSeatRequestListButton.setOnClickListener(v -> {
            RoomRequestListDialog dialog = new RoomRequestListDialog(getContext());
            dialog.setRoomRequestType(RoomRequestType.REQUEST_TAKE_SEAT);
            dialog.show();
        });
        childLinearLayout.addView(takeSeatRequestListButton, generateChildImageLayoutParams());

        audioOutputButton = new AudioOutputButton(getContext());
        audioOutputButton.open();
        childLinearLayout.addView(audioOutputButton, generateChildImageLayoutParams());

//        giftButton = new GiftButton(getContext());
//        giftButton.open();
//        childLinearLayout.addView(giftButton, generateChildImageLayoutParams());
//
//        giftButton.setOnClickListener(view -> {
//            Toast.makeText(getContext(), "okkkk", Toast.LENGTH_SHORT).show();
//        });

        ZEGOLiveAudioRoomManager.getInstance().addLiveAudioRoomListener(new LiveAudioRoomListener() {
            @Override
            public void onHostChanged(ZEGOSDKUser hostUser) {
                updateWidgets();
            }

            @Override
            public void onLockSeatStatusChanged(boolean lock) {
                updateWidgets();
                takeSeatRequestListButton.checkRedPoint();
                takeSeatButton.updateUI();
            }

            @Override
            public void onSeatChanged(List<LiveAudioRoomSeat> changedSeats) {
                updateWidgets();
            }
        });

        microphoneButton.setVisibility(GONE);
        lockSeatButton.setVisibility(GONE);
        takeSeatButton.setVisibility(GONE);
        takeSeatRequestListButton.setVisibility(GONE);
        audioOutputButton.setVisibility(GONE);
//        giftButton.setVisibility(GONE);
    }

    private void updateWidgets() {

        int myRoomSeatIndex = ZEGOLiveAudioRoomManager.getInstance().findMyRoomSeatIndex();
        ZEGOSDKUser localUser = ZEGOSDKManager.getInstance().expressService.getCurrentUser();
        ZEGOSDKUser hostUser = ZEGOLiveAudioRoomManager.getInstance().getHostUser();

        if (localUser.equals(hostUser)) {
            // host widget
            microphoneButton.setVisibility(VISIBLE);
            lockSeatButton.setVisibility(VISIBLE);
            takeSeatButton.setVisibility(GONE);
            takeSeatRequestListButton.setVisibility(VISIBLE);
            audioOutputButton.setVisibility(VISIBLE);
        } else {
            if (myRoomSeatIndex >= 0) {
                // speaker widget
                microphoneButton.setVisibility(VISIBLE);
                lockSeatButton.setVisibility(GONE);
                takeSeatButton.setVisibility(GONE);
                takeSeatRequestListButton.setVisibility(GONE);
                audioOutputButton.setVisibility(VISIBLE);
            } else {
                // audience widget
                microphoneButton.setVisibility(GONE);
                lockSeatButton.setVisibility(GONE);
                if (ZEGOLiveAudioRoomManager.getInstance().isSeatLocked()) {
                    takeSeatButton.setVisibility(VISIBLE);
                } else {
                    takeSeatButton.setVisibility(GONE);
                }
                takeSeatRequestListButton.setVisibility(GONE);
                audioOutputButton.setVisibility(GONE);
            }
        }

    }

    private LayoutParams generateChildImageLayoutParams() {
        int size = Utils.dp2px(36f, getResources().getDisplayMetrics());
        int marginTop = Utils.dp2px(10f, getResources().getDisplayMetrics());
        int marginBottom = Utils.dp2px(16f, getResources().getDisplayMetrics());
        int marginEnd = Utils.dp2px(8, getResources().getDisplayMetrics());
        LayoutParams layoutParams = new LayoutParams(size, size);
        layoutParams.topMargin = marginTop;
        layoutParams.bottomMargin = marginBottom;
        layoutParams.rightMargin = marginEnd;
        return layoutParams;
    }

    private LayoutParams generateChildTextLayoutParams() {
        int size = Utils.dp2px(36f, getResources().getDisplayMetrics());
        int marginTop = Utils.dp2px(10f, getResources().getDisplayMetrics());
        int marginBottom = Utils.dp2px(16f, getResources().getDisplayMetrics());
        int marginEnd = Utils.dp2px(8, getResources().getDisplayMetrics());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, size);
        layoutParams.topMargin = marginTop;
        layoutParams.bottomMargin = marginBottom;
        layoutParams.rightMargin = marginEnd;
        return layoutParams;
    }
}
