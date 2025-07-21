package com.netease.androidcrashhandler.unknownCrash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/* loaded from: classes5.dex */
public class ShutdownBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN";
    private static final String TAG = "Shutdown";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_SHUTDOWN)) {
            CheckNormalExitManager.getInstance().setNormalExit();
            Log.i(TAG, "ShutdownBroadcastReceiver onReceive(), Do thing!");
        }
    }
}