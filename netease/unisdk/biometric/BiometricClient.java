package com.netease.unisdk.biometric;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
public class BiometricClient implements Callback {
    public static final int REQUEST_ENROLL = 81;
    public static final String VERSION = "0.9.2";
    private static BiometricClient instance;
    private Request mCurrentRequest;
    private Callback mEnrollCallback;
    AtomicBoolean isRunning = new AtomicBoolean(false);
    private CopyOnWriteArrayList<Request> mRequests = new CopyOnWriteArrayList<>();
    private Biometric mBiometric = new BiometricImpl();
    private Biometric mKeyguard = new KeyGuardImpl();

    public String getVersion() {
        return VERSION;
    }

    public static BiometricClient getInstance() {
        if (instance == null) {
            synchronized (BiometricClient.class) {
                if (instance == null) {
                    instance = new BiometricClient();
                }
            }
        }
        return instance;
    }

    private BiometricClient() {
    }

    public synchronized void verify(Activity activity, String str, String str2, Callback callback) {
        Log.d("NtBiometric", "version:0.9.2");
        Log.d("NtBiometric", "onVerify:" + str + ", subTitle:" + str2);
        if (callback != null) {
            this.mRequests.add(new Request(str, str2, callback));
        }
        if (this.isRunning.get()) {
            return;
        }
        this.isRunning.set(true);
        int iIsDeviceSecured = this.mBiometric.isDeviceSecured(activity);
        if (iIsDeviceSecured == 1) {
            Log.d("NtBiometric", "device secured: true");
            Log.d("NtBiometric", "start verify by keyguard");
            this.mCurrentRequest = new Request(str, str2, this);
            Intent intent = new Intent();
            intent.putExtra("USE_KEYGUARD", false);
            intent.setClass(activity, AuthActivity.class);
            activity.startActivity(intent);
        } else if (iIsDeviceSecured == 2) {
            Log.d("NtBiometric", "start verify by keyguard");
            this.mCurrentRequest = new Request(str, str2, this);
            Intent intent2 = new Intent();
            intent2.putExtra("USE_KEYGUARD", true);
            intent2.setClass(activity, AuthActivity.class);
            activity.startActivity(intent2);
        } else {
            Log.d("NtBiometric", "device secured: false");
            onFinish(3);
        }
    }

    public synchronized void verifyByKeyGuard(Activity activity, String str, String str2, Callback callback) {
        Log.d("NtBiometric", "version:0.9.2");
        Log.d("NtBiometric", "onVerify:" + str + ", subTitle:" + str2);
        if (callback != null) {
            this.mRequests.add(new Request(str, str2, callback));
        }
        if (this.isRunning.get()) {
            return;
        }
        this.isRunning.set(true);
        if (this.mKeyguard.isDeviceSecured(activity) == 2) {
            Log.d("NtBiometric", "device secured: true");
            this.mCurrentRequest = new Request(str, str2, this);
            Intent intent = new Intent();
            intent.putExtra("USE_KEYGUARD", true);
            intent.setClass(activity, AuthActivity.class);
            activity.startActivity(intent);
        } else {
            Log.d("NtBiometric", "device secured: false");
            onFinish(3);
        }
    }

    void verify(AuthActivity authActivity, Callback callback) {
        Log.d("NtBiometric", "version:0.9.2");
        Log.d("NtBiometric", "verify[AuthActivity]");
        Request request = this.mCurrentRequest;
        if (request == null) {
            callback.onFinish(3);
        } else {
            this.mBiometric.verify(authActivity, request.title, this.mCurrentRequest.msg, callback);
        }
    }

    void verifyByKeyGuard(AuthActivity authActivity, Callback callback) {
        Log.d("NtBiometric", "version:0.9.2");
        Log.d("NtBiometric", "verifyByKeyGuard[AuthActivity]");
        if (this.mCurrentRequest != null) {
            Log.d("NtBiometric", "start verify by keyguard");
            this.mKeyguard.verify(authActivity, this.mCurrentRequest.title, this.mCurrentRequest.msg, callback);
        } else {
            callback.onFinish(3);
        }
    }

    private synchronized void dispatchCallback(int i) {
        Log.d("NtBiometric", "dispatchCallback:" + i);
        Iterator<Request> it = this.mRequests.iterator();
        while (it.hasNext()) {
            it.next().mCallback.onFinish(i);
        }
        this.mRequests.clear();
        this.isRunning.set(false);
    }

    public void onWindowFocusChanged(boolean z) {
        this.mBiometric.onWindowFocusChanged(z);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        Log.d("NtBiometric", "onActivityResult");
        if (i == 81) {
            Callback callback = this.mEnrollCallback;
            if (callback != null) {
                callback.onFinish(0);
                return;
            }
            return;
        }
        if (i == 80) {
            this.mKeyguard.onActivityResult(i, i2, intent);
        } else {
            this.mBiometric.onActivityResult(i, i2, intent);
        }
    }

    @Override // com.netease.unisdk.biometric.Callback
    public void onFinish(int i) {
        Log.d("NtBiometric", "onFinish[client]:" + i);
        dispatchCallback(i);
    }

    public static class Request {
        private Callback mCallback;
        private String msg;
        private String title;

        public Request(String str, String str2, Callback callback) {
            this.title = str;
            this.msg = str2;
            this.mCallback = callback;
        }

        public String getTitle() {
            return this.title;
        }

        public String getMsg() {
            return this.msg;
        }

        public Callback getCallback() {
            return this.mCallback;
        }
    }

    public void requestNewBiometricEnroll(Activity activity, Callback callback) {
        Log.d("NtBiometric", "requestNewBiometricEnroll");
        this.mEnrollCallback = callback;
        try {
            Intent intent = new Intent("android.settings.BIOMETRIC_ENROLL");
            intent.putExtra("android.provider.extra.BIOMETRIC_AUTHENTICATORS_ALLOWED", 32768);
            activity.startActivityForResult(intent, 81);
        } catch (Throwable th) {
            th.printStackTrace();
            Log.d("NtBiometric", "not support");
            this.mEnrollCallback.onFinish(3);
        }
    }

    public int isDeviceSecured(Context context) {
        return this.mBiometric.isDeviceSecured(context);
    }
}