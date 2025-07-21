package com.netease.cloud.nos.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.netease.cloud.nos.android.utils.Util;

/* loaded from: classes6.dex */
public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Util.netStateChange(context);
    }
}