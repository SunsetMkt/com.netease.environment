package com.netease.rnccplayer;

import com.CCMsgSdk.CCMsgSdk;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/* loaded from: classes4.dex */
public class VideoMsgModule extends ReactContextBaseJavaModule {
    @Override // com.facebook.react.bridge.NativeModule
    public String getName() {
        return "VideoMsgModule";
    }

    public VideoMsgModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    @ReactMethod
    public void fetchMessage(Promise promise) {
        promise.resolve(Arguments.fromList(CCMsgSdk.shareInstance().fetchMessage()));
    }

    @ReactMethod
    public void control(String str, int i, Promise promise) {
        if (CCMsgSdk.shareInstance().control(str, i) == 0) {
            promise.resolve(0);
        } else {
            promise.reject("0", "\u53d1\u9001\u5931\u8d25");
        }
    }
}