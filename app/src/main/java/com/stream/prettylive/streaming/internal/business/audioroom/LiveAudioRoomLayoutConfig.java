package com.stream.prettylive.streaming.internal.business.audioroom;

import java.util.Arrays;
import java.util.List;

public class LiveAudioRoomLayoutConfig {

    public List<LiveAudioRoomLayoutRowConfig> rowConfigs = Arrays.asList(
        new LiveAudioRoomLayoutRowConfig(1, LiveAudioRoomLayoutAlignment.SPACE_AROUND),
        new LiveAudioRoomLayoutRowConfig(4, LiveAudioRoomLayoutAlignment.SPACE_AROUND),
        new LiveAudioRoomLayoutRowConfig(4, LiveAudioRoomLayoutAlignment.SPACE_AROUND),
        new LiveAudioRoomLayoutRowConfig(4, LiveAudioRoomLayoutAlignment.SPACE_AROUND));
    public int rowSpacing = 0;
}
