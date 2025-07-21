package com.netease.pushservice;

import android.content.Context;
import android.util.Log;
import com.netease.push.utils.NotifyMessage;
import com.netease.pushclient.PushClientReceiver;
import com.netease.pushclient.PushManager;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import org.json.JSONException;

/* loaded from: classes4.dex */
public class NgDefaultPushClientReceiver extends PushClientReceiver {
    public static final String TAG = "NGPush_" + NgDefaultPushClientReceiver.class.getSimpleName();
    private static final Random random = new Random(System.currentTimeMillis());

    @Override // com.netease.pushclient.PushClientReceiver
    public void onGetNewDevId(Context context, String str) throws JSONException {
        Log.d(TAG, "onGetNewDevId");
        Log.d(TAG, "regid:" + str);
        if (str.contains("oppo")) {
            PushManager.createPushChannel("oppo_group_id", "oppo_group_name", "oppo_channel_id", "oppo_channel_name", true, true, true, null);
        }
    }

    @Override // com.netease.pushclient.PushClientReceiver
    public void onGetNewToken(Context context, String str) {
        Log.d(TAG, "onGetNewToken");
        Log.d(TAG, "token:" + str);
    }

    @Override // com.netease.pushclient.PushClientReceiver
    public void onChannelNotiClickMessage(Context context, NotifyMessage notifyMessage) {
        Log.d(TAG, "onChannelNotiClickMessage");
        Log.d(TAG, "notifyMessage:" + notifyMessage);
    }

    @Override // com.netease.pushclient.PushClientReceiver
    public void onReceiveNotifyMessage(Context context, NotifyMessage notifyMessage) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Log.i(TAG, "onReceiveNotifyMessage");
        Log.d(TAG, "notifyMessage=" + notifyMessage.toString());
        Log.d(TAG, "isBackground:" + isBackground(context));
        Log.d(TAG, "notifyMessage passJsonString:" + notifyMessage.getPassJsonString());
        setForceShowMsgOnFront(PushManager.isForceShowMsgOnFront());
        super.onReceiveNotifyMessage(context, notifyMessage);
    }
}