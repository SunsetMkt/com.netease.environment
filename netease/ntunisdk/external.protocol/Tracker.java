package com.netease.ntunisdk.external.protocol;

import com.xiaomi.gamecenter.sdk.statistics.OneTrackParams;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class Tracker {
    public static final String EVENT_PROTOCOL_CLICK_AGREE = "protocol-click-agree";
    public static final String EVENT_PROTOCOL_CLICK_AGREE_ALL = "protocol-click-agree-all";
    public static final String EVENT_PROTOCOL_CLICK_CANCEL = "protocol-click-cancel";
    public static final String EVENT_PROTOCOL_CLICK_CONFIRM = "protocol-click-confirm";
    public static final String EVENT_PROTOCOL_CLICK_REFUSE = "protocol-click-refuse";
    public static final String EVENT_PROTOCOL_CLICK_REFUSE_RECONFIRM = "protocol-click-protocol_refuse_reconfirm";
    public static final String EVENT_PROTOCOL_FETCH_FAILED_SKIP = "protocol_fetch_failed_skip";
    public static final String EVENT_PROTOCOL_SHOW = "protocol-page-protocol";
    private static Tracker sInstance;
    private Callback<JSONObject> mEventCallback;
    private int publishArea = 0;

    private Tracker() {
    }

    public static Tracker getInstance() {
        if (sInstance == null) {
            synchronized (Tracker.class) {
                if (sInstance == null) {
                    sInstance = new Tracker();
                }
            }
        }
        return sInstance;
    }

    public void setEventCallback(Callback<JSONObject> callback) {
        this.mEventCallback = callback;
    }

    public void setPublishArea(int i) {
        this.publishArea = i;
    }

    public void onEvent(String str, String str2, String str3) throws JSONException {
        if (this.publishArea == 0 || this.mEventCallback == null) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(OneTrackParams.XMSdkParams.STEP, str);
            jSONObject.put("protocol_id", str2);
            jSONObject.put("protocol_version", str3);
            this.mEventCallback.onFinish(jSONObject);
        } catch (Exception unused) {
        }
    }
}