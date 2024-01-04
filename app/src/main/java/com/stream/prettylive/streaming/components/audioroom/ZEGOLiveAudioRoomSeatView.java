package com.stream.prettylive.streaming.components.audioroom;

import android.content.Context;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.stream.prettylive.R;
import com.stream.prettylive.streaming.components.LetterIconView;
import com.stream.prettylive.streaming.internal.ZEGOLiveAudioRoomManager;
import com.stream.prettylive.streaming.internal.sdk.basic.ZEGOSDKUser;
import com.stream.prettylive.streaming.internal.utils.Utils;

public class ZEGOLiveAudioRoomSeatView extends LinearLayout {

    private ImageView seatIconBackground;
    private ImageView userCustomAvatarView;
    private LetterIconView letterIconView;
    private ImageView hostTagView;
    private TextView userNameView;

    public ZEGOLiveAudioRoomSeatView(@NonNull Context context) {
        super(context);
        initView();
    }

    public ZEGOLiveAudioRoomSeatView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ZEGOLiveAudioRoomSeatView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();

        // background: lock/unlock
        // letterIcon,customIcon
        // hostTag
        FrameLayout contentLayout = new FrameLayout(getContext());
        LayoutParams contentLayoutParams = new LayoutParams(Utils.dp2px(66, displayMetrics),
            Utils.dp2px(66, displayMetrics));
        addView(contentLayout, contentLayoutParams);

        FrameLayout.LayoutParams avatarParams = new FrameLayout.LayoutParams(Utils.dp2px(54, displayMetrics),
            Utils.dp2px(54, displayMetrics));
        seatIconBackground = new ImageView(getContext());
        seatIconBackground.setBackgroundResource(R.drawable.audioroom_icon_seat);
        contentLayout.addView(seatIconBackground, avatarParams);

        avatarParams.gravity = Gravity.CENTER;
        letterIconView = new LetterIconView(getContext());
        letterIconView.setCircleBackgroundRadius(avatarParams.width / 2);
        letterIconView.setLetter("");
        contentLayout.addView(letterIconView, avatarParams);

        userCustomAvatarView = new ImageView(getContext());
        userCustomAvatarView.setLayoutParams(avatarParams);
        contentLayout.addView(userCustomAvatarView);

        hostTagView = new ImageView(getContext());
        hostTagView.setImageResource(R.drawable.audioroom_icon_seat_host);
        hostTagView.setVisibility(GONE);
        FrameLayout.LayoutParams hostTagViewParams = new FrameLayout.LayoutParams(-2, -2);
        hostTagViewParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        hostTagViewParams.bottomMargin = Utils.dp2px(4, getResources().getDisplayMetrics());
        contentLayout.addView(hostTagView, hostTagViewParams);

        // username below
        userNameView = new TextView(getContext());
        userNameView.setEllipsize(TruncateAt.END);
        userNameView.setGravity(Gravity.CENTER);
        userNameView.setSingleLine();
        userNameView.setPadding(Utils.dp2px(4, getResources().getDisplayMetrics()), 0,
            Utils.dp2px(4, getResources().getDisplayMetrics()), 0);
        userNameView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        LayoutParams userNameParams = new LayoutParams(Utils.dp2px(66, displayMetrics),
            Utils.dp2px(14, displayMetrics));
        addView(userNameView, userNameParams);
    }

    public void onUserUpdate(ZEGOSDKUser audioRoomUser) {
        if (audioRoomUser != null) {
            addUserToSeat(audioRoomUser);
        } else {
            removeUserFromSeat();
        }
    }

    private void removeUserFromSeat() {
        letterIconView.setLetter("");
        userNameView.setText("");
        userCustomAvatarView.setImageDrawable(null);
    }

    private void addUserToSeat(ZEGOSDKUser audioRoomUser) {
        letterIconView.setLetter(audioRoomUser.userName.toUpperCase());
        userNameView.setText(audioRoomUser.userName);
        String userAvatarUrl = ZEGOLiveAudioRoomManager.getInstance().getUserAvatar(audioRoomUser.userID);
        onUserAvatarUpdated(userAvatarUrl);
    }

    public void onLockChanged(boolean lock) {
        if (lock) {
            seatIconBackground.setBackgroundResource(R.drawable.audioroom_icon_lock_seat);
        } else {
            seatIconBackground.setBackgroundResource(R.drawable.audioroom_icon_seat);
        }
    }

    public void onUserAvatarUpdated(String url) {
        if (TextUtils.isEmpty(url)) {
            userCustomAvatarView.setImageDrawable(null);
        } else {
            RequestOptions requestOptions = new RequestOptions().circleCrop();
            Glide.with(getContext()).load(url).apply(requestOptions).into(userCustomAvatarView);
        }
    }

    public void showHostTag() {
        hostTagView.setVisibility(VISIBLE);
    }

    public void hideHostTag() {
        hostTagView.setVisibility(GONE);
    }
}
