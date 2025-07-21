package com.netease.pushclient;

import android.content.Context;
import android.util.Log;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import com.netease.push.utils.NotifyMessage;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import org.json.JSONException;

/* loaded from: classes3.dex */
public class MyPushClientReceiver extends PushClientReceiver {
    public static final String TAG = "NGPush_MyPushClientReceiver";
    private static final Random random = new Random(System.currentTimeMillis());

    @Override // com.netease.pushclient.PushClientReceiver
    public void onGetNewDevId(Context context, String str) throws JSONException {
        String str2 = TAG;
        Log.d(str2, "onGetNewDevId");
        Log.d(str2, "regid:" + str);
        if (str.contains("oppo")) {
            PushManager.createPushChannel("oppo_group_id", "oppo_group_name", "oppo_channel_id", "oppo_channel_name", true, true, true, null);
        }
    }

    @Override // com.netease.pushclient.PushClientReceiver
    public void onChannelNotiClickMessage(Context context, NotifyMessage notifyMessage) {
        String str = TAG;
        Log.d(str, "onChannelNotiClickMessage");
        Log.d(str, "notifyMessage:" + notifyMessage);
    }

    @Override // com.netease.pushclient.PushClientReceiver
    public void onReceiveNotifyMessage(Context context, NotifyMessage notifyMessage) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String str = TAG;
        Log.i(str, "onReceiveNotifyMessage");
        Log.d(str, "notifyMessage=" + notifyMessage.toString());
        notifyMessage.setIcon(context.getResources().getIdentifier("ic_alarm_on_black_24dp", ResIdReader.RES_TYPE_DRAWABLE, context.getPackageName()));
        Log.d(str, "isBackground:" + isBackground(context));
        Log.d(str, "notifyMessage passJsonString:" + notifyMessage.getPassJsonString());
        setForceShowMsgOnFront(PushManager.isForceShowMsgOnFront());
        super.onReceiveNotifyMessage(context, notifyMessage);
    }
}