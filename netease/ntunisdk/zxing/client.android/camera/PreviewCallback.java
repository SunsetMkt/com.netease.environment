package com.netease.ntunisdk.zxing.client.android.camera;

import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;

/* loaded from: classes.dex */
final class PreviewCallback implements Camera.PreviewCallback {
    private final CameraConfigurationManager configManager;
    private Handler previewHandler;
    private int previewMessage;

    PreviewCallback(CameraConfigurationManager cameraConfigurationManager) {
        this.configManager = cameraConfigurationManager;
    }

    void setHandler(Handler handler, int i) {
        this.previewHandler = handler;
        this.previewMessage = i;
    }

    @Override // android.hardware.Camera.PreviewCallback
    public void onPreviewFrame(byte[] bArr, Camera camera) {
        Message messageObtainMessage;
        Point cameraResolution = this.configManager.getCameraResolution();
        Handler handler = this.previewHandler;
        if (cameraResolution == null || handler == null) {
            return;
        }
        Point screenResolution = this.configManager.getScreenResolution();
        if (screenResolution.x < screenResolution.y) {
            messageObtainMessage = handler.obtainMessage(this.previewMessage, cameraResolution.y, cameraResolution.x, bArr);
        } else {
            messageObtainMessage = handler.obtainMessage(this.previewMessage, cameraResolution.x, cameraResolution.y, bArr);
        }
        messageObtainMessage.sendToTarget();
        this.previewHandler = null;
    }
}