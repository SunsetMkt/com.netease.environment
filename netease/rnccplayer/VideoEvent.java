package com.netease.rnccplayer;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

/* loaded from: classes4.dex */
class VideoEvent extends Event<VideoEvent> {
    public static final String EVENT_NAME = "onVideoData";
    private final String mData;
    private final boolean mIsPlaying;
    private final int mType;

    @Override // com.facebook.react.uimanager.events.Event
    public String getEventName() {
        return EVENT_NAME;
    }

    protected VideoEvent(int i, int i2, String str, boolean z) {
        super(i);
        this.mType = i2;
        this.mData = str;
        this.mIsPlaying = z;
    }

    @Override // com.facebook.react.uimanager.events.Event
    public void dispatch(RCTEventEmitter rCTEventEmitter) {
        rCTEventEmitter.receiveEvent(getViewTag(), getEventName(), serializeEventData());
    }

    private WritableMap serializeEventData() {
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putString("type", String.valueOf(this.mType));
        writableMapCreateMap.putString("data", this.mData);
        writableMapCreateMap.putString("isPlaying", String.valueOf(this.mIsPlaying));
        return writableMapCreateMap;
    }
}