package com.stream.donalive.streaming.internal.business.audioroom;

import java.util.List;

public interface RoomSeatServiceListener {

    default void onSeatChanged(List<LiveAudioRoomSeat> changedSeats){}
}
