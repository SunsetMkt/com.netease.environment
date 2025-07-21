package com.netease.rnwebview;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

/* loaded from: classes4.dex */
public class TopMessageEvent extends Event<TopMessageEvent> {
    public static final String EVENT_NAME = "topMessage";
    private final String mData;

    @Override // com.facebook.react.uimanager.events.Event
    public boolean canCoalesce() {
        return false;
    }

    @Override // com.facebook.react.uimanager.events.Event
    public short getCoalescingKey() {
        return (short) 0;
    }

    @Override // com.facebook.react.uimanager.events.Event
    public String getEventName() {
        return EVENT_NAME;
    }

    public TopMessageEvent(int i, String str) {
        super(i);
        this.mData = str;
    }

    @Override // com.facebook.react.uimanager.events.Event
    public void dispatch(RCTEventEmitter rCTEventEmitter) {
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putString("data", this.mData);
        rCTEventEmitter.receiveEvent(getViewTag(), EVENT_NAME, writableMapCreateMap);
    }
}