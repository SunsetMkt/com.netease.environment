package com.netease.unisdk.biometric;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class BiometricImpl implements Biometric {
    private static final String HARMONY_OS = "harmony";
    private Callback mCallback;
    private int status = -1;
    private boolean isFocused = false;
    private int isSupportBiometricApi = -1;

    private int getErrorCode(int i) {
        return (i == 3 || i == 5 || i == 7 || i == 10) ? 1 : 3;
    }

    @Override // com.netease.unisdk.biometric.Biometric
    public void onActivityResult(int i, int i2, Intent intent) {
    }

    @Override // com.netease.unisdk.biometric.Biometric
    public int isDeviceSecured(Context context) {
        try {
            BiometricManager biometricManagerFrom = BiometricManager.from(context);
            int i = 1;
            if (biometricManagerFrom.canAuthenticate(33023) == 0) {
                if (biometricManagerFrom.canAuthenticate(255) != 0 || !isDeviceSupportBiometricByMTL()) {
                    i = 0;
                }
                if (isDeviceSupportPinByMTL()) {
                    return 2;
                }
                return i;
            }
        } catch (Throwable unused) {
        }
        return 0;
    }

    @Override // com.netease.unisdk.biometric.Biometric
    public void verify(Activity activity, String str, String str2, Callback callback) {
        this.mCallback = callback;
        Executor mainExecutor = ContextCompat.getMainExecutor(activity);
        this.status = -1;
        new BiometricPrompt((FragmentActivity) activity, mainExecutor, new BiometricPrompt.AuthenticationCallback() { // from class: com.netease.unisdk.biometric.BiometricImpl.1
            @Override // androidx.biometric.BiometricPrompt.AuthenticationCallback
            public void onAuthenticationError(int i, CharSequence charSequence) {
                super.onAuthenticationError(i, charSequence);
                Log.d("NtBiometric", "onAuthenticationError:" + i + ",msg:" + ((Object) charSequence));
                BiometricImpl.this.status = i;
                if (!BiometricImpl.this.isFocused || BiometricImpl.this.mCallback == null) {
                    return;
                }
                BiometricImpl.this.mCallback.onFinish(BiometricImpl.this.status);
            }

            @Override // androidx.biometric.BiometricPrompt.AuthenticationCallback
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult authenticationResult) {
                super.onAuthenticationSucceeded(authenticationResult);
                Log.d("NtBiometric", "onAuthenticationSucceeded," + BiometricImpl.this.isFocused);
                BiometricImpl.this.status = 0;
                if (!BiometricImpl.this.isFocused || BiometricImpl.this.mCallback == null) {
                    return;
                }
                BiometricImpl.this.mCallback.onFinish(BiometricImpl.this.status);
            }

            @Override // androidx.biometric.BiometricPrompt.AuthenticationCallback
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                BiometricImpl.this.status = 1;
                if (!BiometricImpl.this.isFocused || BiometricImpl.this.mCallback == null) {
                    return;
                }
                BiometricImpl.this.mCallback.onFinish(BiometricImpl.this.status);
            }
        }).authenticate(new BiometricPrompt.PromptInfo.Builder().setTitle(str).setSubtitle(str2).setNegativeButtonText("\u53d6\u6d88").setAllowedAuthenticators(255).build());
    }

    @Override // com.netease.unisdk.biometric.Biometric
    public void onWindowFocusChanged(boolean z) {
        Callback callback;
        int i;
        Log.d("NtBiometric", "onWindowFocusChanged[BiometricImpl]:" + z);
        this.isFocused = z;
        if (!this.isFocused || (callback = this.mCallback) == null || (i = this.status) == -1) {
            return;
        }
        callback.onFinish(i);
    }

    private boolean isDeviceSupportBiometricByMTL() {
        int i = this.isSupportBiometricApi;
        if (i != -1) {
            return i == 1;
        }
        if (!isHarmonyOS() && !"Hera-BD00".equals(Build.MODEL) && !"M2004J19C".equals(Build.MODEL)) {
            this.isSupportBiometricApi = 1;
        } else {
            this.isSupportBiometricApi = 0;
        }
        return this.isSupportBiometricApi == 1;
    }

    private boolean isDeviceSupportPinByMTL() {
        return !"Mi 10".equals(Build.MODEL);
    }

    public static boolean isHarmonyOS() throws NoSuchMethodException, ClassNotFoundException, SecurityException {
        try {
            Class<?> cls = Class.forName("com.huawei.system.BuildEx");
            Method method = cls.getMethod("getOsBrand", new Class[0]);
            ClassLoader classLoader = cls.getClassLoader();
            if (classLoader == null || classLoader.getParent() != null) {
                return false;
            }
            return HARMONY_OS.equals(method.invoke(cls, new Object[0]));
        } catch (ClassNotFoundException | NoSuchMethodException | Exception unused) {
            return false;
        }
    }
}