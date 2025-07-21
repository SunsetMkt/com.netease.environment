package com.netease.unisdk.biometric;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;

/* loaded from: classes.dex */
class KeyguardUtils {
    private KeyguardUtils() {
    }

    static KeyguardManager getKeyguardManager(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Api23Impl.getKeyguardManager(context);
        }
        Object systemService = context.getSystemService("keyguard");
        if (systemService instanceof KeyguardManager) {
            return (KeyguardManager) systemService;
        }
        return null;
    }

    static boolean isDeviceSecuredWithCredential(Context context) {
        KeyguardManager keyguardManager = getKeyguardManager(context);
        if (keyguardManager == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            return Api23Impl.isDeviceSecure(keyguardManager);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return Api16Impl.isKeyguardSecure(keyguardManager);
        }
        return false;
    }

    private static class Api23Impl {
        private Api23Impl() {
        }

        static KeyguardManager getKeyguardManager(Context context) {
            return (KeyguardManager) context.getSystemService(KeyguardManager.class);
        }

        static boolean isDeviceSecure(KeyguardManager keyguardManager) {
            return keyguardManager.isDeviceSecure();
        }
    }

    private static class Api16Impl {
        private Api16Impl() {
        }

        static boolean isKeyguardSecure(KeyguardManager keyguardManager) {
            return keyguardManager.isKeyguardSecure();
        }
    }
}