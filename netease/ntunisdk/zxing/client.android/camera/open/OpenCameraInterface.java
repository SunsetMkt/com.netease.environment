package com.netease.ntunisdk.zxing.client.android.camera.open;

import android.hardware.Camera;
import com.netease.ntunisdk.base.UniSdkUtils;

/* loaded from: classes.dex */
public final class OpenCameraInterface {
    public static final int NO_REQUESTED_CAMERA = -1;
    private static final String TAG = "UniQR camera";

    private OpenCameraInterface() {
    }

    public static OpenCamera open(int i) {
        int i2;
        Camera.CameraInfo cameraInfo;
        Camera cameraOpen;
        int numberOfCameras = Camera.getNumberOfCameras();
        if (numberOfCameras == 0) {
            UniSdkUtils.e(TAG, "No cameras!");
            return null;
        }
        boolean z = i >= 0;
        if (!z) {
            i2 = 0;
            while (true) {
                if (i2 >= numberOfCameras) {
                    cameraInfo = null;
                    break;
                }
                cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(i2, cameraInfo);
                if (CameraFacing.values()[cameraInfo.facing] == CameraFacing.BACK) {
                    break;
                }
                i2++;
            }
        } else {
            Camera.CameraInfo cameraInfo2 = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo2);
            cameraInfo = cameraInfo2;
            i2 = i;
        }
        if (i2 < numberOfCameras) {
            UniSdkUtils.i(TAG, "Opening camera #" + i2);
            cameraOpen = Camera.open(i2);
        } else if (z) {
            UniSdkUtils.e(TAG, "Requested camera does not exist: " + i);
            cameraOpen = null;
        } else {
            UniSdkUtils.i(TAG, "No camera facing " + CameraFacing.BACK + "; returning camera #0");
            cameraOpen = Camera.open(0);
            cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(0, cameraInfo);
        }
        if (cameraOpen == null) {
            return null;
        }
        return new OpenCamera(i2, cameraOpen, CameraFacing.values()[cameraInfo.facing], cameraInfo.orientation);
    }
}