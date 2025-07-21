package com.netease.ntunisdk.base.utils;

import android.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Window;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import java.lang.ref.WeakReference;

/* loaded from: classes3.dex */
public class LoadingDialog {
    private static final String TAG = "LoadingDialog";
    private static LoadingDialog instance;
    private Handler handler;
    private WeakReference<Activity> ref;
    private Dialog dialog = null;
    private long loadingInterval = 180000;
    private final int MSG_ORDER_LOADING_CLOSE = 1;

    private LoadingDialog(Activity activity) {
        this.handler = null;
        this.ref = new WeakReference<>(activity);
        if (this.handler == null) {
            this.handler = new Handler(Looper.getMainLooper()) { // from class: com.netease.ntunisdk.base.utils.LoadingDialog.1
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    if (message.what == 1) {
                        LoadingDialog.this.dismiss();
                    }
                }
            };
        }
    }

    public void notifyVisibilityChanged(boolean z) {
        UniSdkUtils.d(TAG, "notifyVisibilityChanged visible=".concat(String.valueOf(z)));
        if (this.dialog != null) {
            updateVisibility(z);
        }
    }

    public static LoadingDialog getInstance(Activity activity) {
        LoadingDialog loadingDialog = instance;
        if (loadingDialog == null) {
            instance = new LoadingDialog(activity);
        } else {
            loadingDialog.resetContext(activity);
        }
        return instance;
    }

    private void resetContext(Activity activity) {
        WeakReference<Activity> weakReference = this.ref;
        if (weakReference == null || weakReference.get() == null || this.ref.get() != activity) {
            this.ref = new WeakReference<>(activity);
        }
    }

    public void show() {
        if (this.ref.get() != null) {
            this.ref.get().runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.utils.LoadingDialog.2
                @Override // java.lang.Runnable
                public void run() {
                    if (LoadingDialog.this.dialog != null) {
                        LoadingDialog.this.dismiss();
                    }
                    LoadingDialog.this.dialog = new BaseDialog((Context) LoadingDialog.this.ref.get(), (byte) 0);
                    LoadingDialog.this.dialog.setCanceledOnTouchOutside(false);
                    LoadingDialog.this.dialog.setCancelable(true);
                    try {
                        LoadingDialog.this.dialog.setContentView(((Activity) LoadingDialog.this.ref.get()).getResources().getIdentifier("unisdk_login_loading_progressdialog", ResIdReader.RES_TYPE_LAYOUT, ((Activity) LoadingDialog.this.ref.get()).getPackageName()));
                        LoadingDialog.this.updateVisibility("1".equals(SdkMgr.getInst().getPropStr(ConstProp.HIDE_LOADING_UI, "0")) ? false : true);
                        LoadingDialog.this.dialog.show();
                    } catch (Throwable th) {
                        UniSdkUtils.e(LoadingDialog.TAG, th.getMessage());
                    }
                }
            });
        }
    }

    public void show(String str) {
        long jDoubleValue;
        try {
            jDoubleValue = (long) (Double.valueOf(str).doubleValue() * 1000.0d);
        } catch (Throwable th) {
            UniSdkUtils.e(TAG, th.getMessage());
            jDoubleValue = 0L;
        }
        try {
            Handler handler = this.handler;
            if (handler != null) {
                handler.removeMessages(1);
            }
            if (jDoubleValue > 0 && this.loadingInterval != jDoubleValue) {
                this.loadingInterval = jDoubleValue;
            }
            show();
            Handler handler2 = this.handler;
            if (handler2 != null) {
                handler2.sendEmptyMessageDelayed(1, this.loadingInterval);
            }
        } catch (Throwable th2) {
            UniSdkUtils.e(TAG, th2.getMessage());
        }
    }

    public void dismiss() {
        if (this.ref.get() != null) {
            this.ref.get().runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.utils.LoadingDialog.3
                @Override // java.lang.Runnable
                public void run() {
                    if (LoadingDialog.this.dialog != null) {
                        LoadingDialog.this.dialog.dismiss();
                        LoadingDialog.this.dialog = null;
                    }
                }
            });
        }
    }

    public void dismissAndRemoveMessage() {
        try {
            dismiss();
            Handler handler = this.handler;
            if (handler != null) {
                handler.removeMessages(1);
            }
        } catch (Throwable th) {
            UniSdkUtils.e(TAG, th.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateVisibility(boolean z) {
        Window window;
        Dialog dialog = this.dialog;
        if (dialog == null || (window = dialog.getWindow()) == null) {
            return;
        }
        window.getDecorView().setVisibility(z ? 0 : 4);
    }

    private static class BaseDialog extends Dialog {
        public BaseDialog(Context context) {
            super(context);
        }

        public BaseDialog(Context context, byte b) {
            super(context, R.style.Theme.Translucent.NoTitleBar.Fullscreen);
        }

        @Override // android.app.Dialog, android.view.Window.Callback
        public void onWindowFocusChanged(boolean z) {
            super.onWindowFocusChanged(z);
            UniSdkUtils.d(LoadingDialog.TAG, "BaseDialog onWindowFocusChanged: ".concat(String.valueOf(z)));
            LifeCycleChecker.getInst().onGameFocusChanged(z);
        }

        @Override // android.app.Dialog, android.content.DialogInterface
        public void dismiss() {
            super.dismiss();
            UniSdkUtils.d(LoadingDialog.TAG, "BaseDialog dismiss");
            LifeCycleChecker.getInst().onGameFocusChanged(false);
        }
    }
}