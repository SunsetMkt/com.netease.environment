package com.netease.ntunisdk.shortcuts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/* loaded from: classes.dex */
public class ShortcutRouteActivity extends Activity {
    private static final String TAG = "QR quickqr_route";

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d(TAG, "onCreate " + getTaskId() + " " + hashCode());
        Intent intent = new Intent(this, (Class<?>) ShortcutMainActivity.class);
        intent.addFlags(335544320);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}