package com.netease.ntunisdk.zxing.client.android;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import com.netease.ntunisdk.zxing.client.android.camera.CameraManager;
import java.util.Collection;
import java.util.Map;

/* loaded from: classes.dex */
public final class CaptureActivityHandler extends Handler {
    private static final String TAG = "UniQR act handler";
    private final CaptureActivity activity;
    private final CameraManager cameraManager;
    private final DecodeThread decodeThread;
    private State state;

    private enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    CaptureActivityHandler(CaptureActivity captureActivity, Collection<BarcodeFormat> collection, Map<DecodeHintType, ?> map, String str, CameraManager cameraManager) {
        this.activity = captureActivity;
        DecodeThread decodeThread = new DecodeThread(captureActivity, collection, map, str);
        this.decodeThread = decodeThread;
        decodeThread.start();
        this.state = State.SUCCESS;
        this.cameraManager = cameraManager;
        cameraManager.startPreview();
        restartPreviewAndDecode();
    }

    private final int getResId(String str, String str2) {
        return this.activity.getResources().getIdentifier("ntunisdk_" + str, str2, this.activity.getPackageName());
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) throws IllegalStateException {
        if (message.what == getResId("restart_preview", ResIdReader.RES_TYPE_ID)) {
            restartPreviewAndDecode();
            return;
        }
        if (message.what == getResId("decode_succeeded", ResIdReader.RES_TYPE_ID)) {
            this.state = State.SUCCESS;
            this.activity.handleDecode((Result) message.obj);
            return;
        }
        if (message.what == getResId("decode_failed", ResIdReader.RES_TYPE_ID)) {
            this.state = State.PREVIEW;
            this.cameraManager.requestPreviewFrame(this.decodeThread.getHandler(), getResId("decode", ResIdReader.RES_TYPE_ID));
            return;
        }
        if (message.what == getResId("return_scan_result", ResIdReader.RES_TYPE_ID)) {
            this.activity.setResult(-1, (Intent) message.obj);
            this.activity.finish();
            return;
        }
        if (message.what == getResId("launch_product_query", ResIdReader.RES_TYPE_ID)) {
            String str = (String) message.obj;
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(524288);
            intent.setData(Uri.parse(str));
            ResolveInfo resolveInfoResolveActivity = this.activity.getPackageManager().resolveActivity(intent, 65536);
            String str2 = null;
            if (resolveInfoResolveActivity != null && resolveInfoResolveActivity.activityInfo != null) {
                str2 = resolveInfoResolveActivity.activityInfo.packageName;
                UniSdkUtils.i(TAG, "Using browser in package " + str2);
            }
            if ("com.android.browser".equals(str2) || "com.android.chrome".equals(str2)) {
                intent.setPackage(str2);
                intent.addFlags(268435456);
                intent.putExtra("com.android.browser.application_id", str2);
            }
            try {
                this.activity.startActivity(intent);
            } catch (ActivityNotFoundException unused) {
                UniSdkUtils.w(TAG, "Can't find anything to handle VIEW of URI " + str);
            }
        }
    }

    public void quitSynchronously() {
        this.state = State.DONE;
        this.cameraManager.stopPreview();
        Message.obtain(this.decodeThread.getHandler(), getResId("quit", ResIdReader.RES_TYPE_ID)).sendToTarget();
        try {
            this.decodeThread.join(500L);
        } catch (InterruptedException unused) {
        }
        removeMessages(getResId("decode_succeeded", ResIdReader.RES_TYPE_ID));
        removeMessages(getResId("decode_failed", ResIdReader.RES_TYPE_ID));
    }

    private void restartPreviewAndDecode() {
        if (this.state == State.SUCCESS) {
            this.state = State.PREVIEW;
            this.cameraManager.requestPreviewFrame(this.decodeThread.getHandler(), getResId("decode", ResIdReader.RES_TYPE_ID));
            this.activity.drawViewfinder();
        }
    }
}