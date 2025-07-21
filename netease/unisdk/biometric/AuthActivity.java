package com.netease.unisdk.biometric;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.fragment.app.FragmentActivity;

/* loaded from: classes.dex */
public class AuthActivity extends FragmentActivity {
    private int status = -1;
    private boolean isNeedReturnGame = false;
    private boolean mHasResume = false;
    private boolean mHasReceiverResult = false;
    private boolean isCloseBySelf = false;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        boolean booleanExtra = getIntent().getBooleanExtra("USE_KEYGUARD", false);
        try {
            hidSysNavigation();
            Window window = getWindow();
            window.setBackgroundDrawable(new ColorDrawable(0));
            window.setDimAmount(0.1f);
            hideSystemUI(window);
            this.status = -1;
            if (booleanExtra) {
                BiometricClient.getInstance().verifyByKeyGuard(this, new Callback() { // from class: com.netease.unisdk.biometric.AuthActivity.1
                    @Override // com.netease.unisdk.biometric.Callback
                    public void onFinish(int i) {
                        if (AuthActivity.this.isCloseBySelf) {
                            i = 0;
                        }
                        Log.d("NtBiometric", "onFinish:" + i + ", isCloseBySelf:" + AuthActivity.this.isCloseBySelf);
                        AuthActivity.this.mHasReceiverResult = true;
                        AuthActivity.this.finish();
                        BiometricClient.getInstance().onFinish(i);
                    }
                });
            } else {
                this.isNeedReturnGame = true;
                BiometricClient.getInstance().verify(this, new Callback() { // from class: com.netease.unisdk.biometric.AuthActivity.2
                    @Override // com.netease.unisdk.biometric.Callback
                    public void onFinish(int i) {
                        Log.d("NtBiometric", "onFinish:" + i + ", hasResume:" + AuthActivity.this.mHasResume);
                        AuthActivity.this.mHasReceiverResult = true;
                        AuthActivity.this.status = i;
                        if (AuthActivity.this.mHasResume) {
                            BiometricClient.getInstance().onFinish(AuthActivity.this.status);
                            AuthActivity.this.isNeedReturnGame = false;
                            AuthActivity.this.overridePendingTransition(0, 0);
                            AuthActivity.this.finish();
                        }
                    }
                });
            }
        } catch (Throwable th) {
            th.printStackTrace();
            if (!booleanExtra) {
                BiometricClient.getInstance().verifyByKeyGuard(this, new Callback() { // from class: com.netease.unisdk.biometric.AuthActivity.3
                    @Override // com.netease.unisdk.biometric.Callback
                    public void onFinish(int i) {
                        AuthActivity.this.finish();
                        BiometricClient.getInstance().onFinish(i);
                    }
                });
            } else {
                finish();
                BiometricClient.getInstance().onFinish(3);
            }
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        BiometricClient.getInstance().onActivityResult(i, i2, intent);
        this.mHasReceiverResult = true;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("NtBiometric", "onNewIntent");
        if (!this.mHasReceiverResult) {
            BiometricClient.getInstance().onFinish(this.status);
        }
        overridePendingTransition(0, 0);
        finish();
    }

    @Override // android.app.Activity
    protected void onRestart() {
        super.onRestart();
        Log.d("NtBiometric", "onRestart");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.netease.unisdk.biometric.AuthActivity.4
            @Override // java.lang.Runnable
            public void run() {
                if (AuthActivity.this.mHasReceiverResult) {
                    return;
                }
                Intent intent = new Intent(AuthActivity.this, (Class<?>) AuthActivity.class);
                intent.addFlags(603979776);
                AuthActivity.this.startActivity(intent);
                AuthActivity.this.isCloseBySelf = true;
                Log.d("NtBiometric", "push top activity");
            }
        }, 600L);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.mHasResume = true;
        synchronized (BiometricClient.getInstance()) {
            Log.d("NtBiometric", "onResume:" + this.mHasResume);
            BiometricClient.getInstance().onWindowFocusChanged(true);
            if (this.isNeedReturnGame && this.mHasResume && this.status != -1) {
                this.isNeedReturnGame = false;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.netease.unisdk.biometric.AuthActivity.5
                    @Override // java.lang.Runnable
                    public synchronized void run() {
                        Log.d("NtBiometric", "postResult:" + AuthActivity.this.status + ", onResume:" + AuthActivity.this.mHasResume);
                        if (AuthActivity.this.mHasResume) {
                            BiometricClient.getInstance().onFinish(AuthActivity.this.status);
                            AuthActivity.this.overridePendingTransition(0, 0);
                            AuthActivity.this.finish();
                        } else {
                            AuthActivity.this.isNeedReturnGame = true;
                        }
                    }
                }, 200L);
            }
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        this.mHasResume = false;
        BiometricClient.getInstance().onWindowFocusChanged(false);
        Log.d("NtBiometric", "onPause:" + this.mHasResume);
    }

    private void hidSysNavigation() {
        try {
            View.class.getMethod("setSystemUiVisibility", Integer.TYPE).invoke(getWindow().getDecorView(), 2);
        } catch (Throwable unused) {
        }
    }

    public static void hideSystemUI(Window window) {
        if (window == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 28) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.layoutInDisplayCutoutMode = 1;
            window.setAttributes(attributes);
        }
        try {
            View decorView = window.getDecorView();
            if (Build.VERSION.SDK_INT > 14 && Build.VERSION.SDK_INT < 19) {
                decorView.setSystemUiVisibility(8);
            } else if (Build.VERSION.SDK_INT >= 19) {
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | 512 | 256 | 1024 | 2 | 4096 | 4);
            }
        } catch (Throwable unused) {
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        BiometricClient.getInstance().isRunning.set(false);
    }
}