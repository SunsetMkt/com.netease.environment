package com.netease.rnccplayer;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.netease.cc.ccplayerwrapper.Constants;

/* loaded from: classes4.dex */
class VideoVbrEvent extends Event<VideoVbrEvent> {
    public static final String EVENT_NAME = "onVbrData";
    private final WritableArray mArray;
    private final String mCurrentVbr;

    @Override // com.facebook.react.uimanager.events.Event
    public String getEventName() {
        return EVENT_NAME;
    }

    protected VideoVbrEvent(int i, String str, WritableArray writableArray) {
        super(i);
        this.mCurrentVbr = str;
        this.mArray = writableArray;
    }

    @Override // com.facebook.react.uimanager.events.Event
    public void dispatch(RCTEventEmitter rCTEventEmitter) {
        rCTEventEmitter.receiveEvent(getViewTag(), getEventName(), serializeEventData());
    }

    private WritableMap serializeEventData() {
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putString("vbr", this.mCurrentVbr);
        writableMapCreateMap.putArray(Constants.KEY_VBR_LIST, this.mArray);
        return writableMapCreateMap;
    }
}