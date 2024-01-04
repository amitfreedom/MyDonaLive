package com.stream.prettylive.streaming.internal.business.audioroom;

import java.util.List;

public interface RoomSeatServiceListener {

    default void onSeatChanged(List<LiveAudioRoomSeat> changedSeats){}
}
