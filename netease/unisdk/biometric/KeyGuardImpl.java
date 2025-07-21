package com.netease.unisdk.biometric;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

/* loaded from: classes.dex */
public class KeyGuardImpl implements Biometric {
    public static final int REQUEST_KEYGUARD_UNLOCK = 80;
    private Callback mCallback;

    @Override // com.netease.unisdk.biometric.Biometric
    public void onWindowFocusChanged(boolean z) {
    }

    @Override // com.netease.unisdk.biometric.Biometric
    public int isDeviceSecured(Context context) {
        return KeyguardUtils.isDeviceSecuredWithCredential(context) ? 2 : 0;
    }

    @Override // com.netease.unisdk.biometric.Biometric
    public void verify(Activity activity, String str, String str2, Callback callback) {
        this.mCallback = callback;
        KeyguardManager keyguardManager = KeyguardUtils.getKeyguardManager(activity);
        if (keyguardManager != null) {
            if (Build.VERSION.SDK_INT >= 21) {
                activity.startActivityForResult(keyguardManager.createConfirmDeviceCredentialIntent(str, str2), 80);
                return;
            }
            Log.d("NtBiometric", "not support");
            Callback callback2 = this.mCallback;
            if (callback2 != null) {
                callback2.onFinish(3);
                return;
            }
            return;
        }
        Log.d("NtBiometric", "not support");
        Callback callback3 = this.mCallback;
        if (callback3 != null) {
            callback3.onFinish(3);
        }
    }

    @Override // com.netease.unisdk.biometric.Biometric
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 80) {
            int i3 = i2 == -1 ? 0 : 1;
            Log.d("NtBiometric", "key guard result:" + i3);
            Callback callback = this.mCallback;
            if (callback != null) {
                callback.onFinish(i3);
            }
        }
    }
}