package com.netease.pushservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.push.utils.PushConstantsImpl;
import com.netease.push.utils.PushLog;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes4.dex */
public class PushServiceReceiver extends BroadcastReceiver {
    private static final String TAG = "NGPush_" + PushServiceReceiver.class.getSimpleName();

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.init(context);
        PushLog.e(TAG, "onReceive");
        PushLog.d(TAG, "intent:" + intent);
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        String packageName = context.getPackageName();
        PushLog.d(TAG, "action:" + action);
        if (PushService.pushServiceLive) {
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action) || "android.net.wifi.STATE_CHANGE".equals(action)) {
                NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                String str = PushConstantsImpl.SERVICE_METHOD_NETWORKDISCONNECT;
                if (activeNetworkInfo != null) {
                    PushLog.d(TAG, "Network Type:" + activeNetworkInfo.getTypeName());
                    PushLog.d(TAG, "Network State:" + activeNetworkInfo.getState());
                    if (activeNetworkInfo.isConnected()) {
                        PushLog.i(TAG, "Network change connected");
                        str = PushConstantsImpl.SERVICE_METHOD_NETWORKCONNECT;
                    } else {
                        PushLog.i(TAG, "Network change disconnected");
                    }
                } else {
                    PushLog.i(TAG, "Network change unavailable");
                }
                Intent intent2 = new Intent();
                intent2.putExtra("method", str);
                intent2.setPackage(packageName);
                PushLog.d(TAG, "CONNECTIVITY_CHANGE, startService");
                PushLog.d(TAG, "intentMethodName:" + str);
                intent2.putExtra(PushConstantsImpl.SERVICE_START_TYPE, PushConstantsImpl.SERVICE_START_TYPE_SELF);
                PushServiceHelper.startActivePushService(context, intent2);
            }
        }
    }
}