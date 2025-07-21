package com.netease.pharos.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.netease.pharos.util.LogUtil;

/* loaded from: classes5.dex */
public class ConnectionChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "ConnectionChangeReceiver";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        LogUtil.i(TAG, "onReceive intent.getAction()=" + intent.getAction());
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            NetworkStatus.getInstance().change();
        }
    }
}