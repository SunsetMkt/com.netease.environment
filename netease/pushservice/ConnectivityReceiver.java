package com.netease.pushservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.push.utils.PushLog;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes4.dex */
public class ConnectivityReceiver extends BroadcastReceiver {
    private static final String TAG = "NGPush_" + ConnectivityReceiver.class.getSimpleName();

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        intent.getAction();
        PushLog.i(TAG, "onReceive");
        PushLog.d(TAG, "intent=" + intent);
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            PushLog.d(TAG, "Network Type = " + activeNetworkInfo.getTypeName());
            PushLog.d(TAG, "Network State = " + activeNetworkInfo.getState());
            if (activeNetworkInfo.isConnected()) {
                PushLog.i(TAG, "Network connected");
                PushServiceHelper.getInstance().connect(false);
                return;
            }
            return;
        }
        PushLog.e(TAG, "Network unavailable");
        PushServiceHelper.getInstance().disconnect();
    }
}