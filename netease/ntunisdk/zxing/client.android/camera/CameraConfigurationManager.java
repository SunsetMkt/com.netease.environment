package com.netease.ntunisdk.zxing.client.android.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.view.WindowManager;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.react.uimanager.ViewProps;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.zxing.client.android.camera.open.CameraFacing;
import com.netease.ntunisdk.zxing.client.android.camera.open.OpenCamera;
import com.netease.ntunisdk.zxing.utils.Util;

/* loaded from: classes.dex */
final class CameraConfigurationManager {
    private static final String TAG = "UniQR config";
    private Point bestPreviewSize;
    private Point cameraResolution;
    private final Context context;
    private int cwRotationFromDisplayToCamera;
    private Point screenResolution;

    CameraConfigurationManager(Context context) {
        this.context = context;
    }

    void initFromCameraParameters(OpenCamera openCamera, Point point) {
        int i;
        int i2;
        Camera.Parameters parameters = openCamera.getCamera().getParameters();
        int rotation = ((WindowManager) this.context.getSystemService("window")).getDefaultDisplay().getRotation();
        if (rotation == 0) {
            i = 0;
        } else if (rotation == 1) {
            i = 90;
        } else if (rotation == 2) {
            i = 180;
        } else if (rotation == 3) {
            i = RotationOptions.ROTATE_270;
        } else if (rotation % 90 == 0) {
            i = (rotation + 360) % 360;
        } else {
            throw new IllegalArgumentException("Bad rotation: " + rotation);
        }
        UniSdkUtils.i(TAG, "Display at: " + i);
        int orientation = openCamera.getOrientation();
        UniSdkUtils.i(TAG, "Camera at: " + orientation);
        if (openCamera.getFacing() == CameraFacing.FRONT) {
            orientation = (360 - orientation) % 360;
            UniSdkUtils.i(TAG, "Front camera overriden to: " + orientation);
        }
        this.cwRotationFromDisplayToCamera = ((orientation + 360) - i) % 360;
        UniSdkUtils.i(TAG, "Final display orientation: " + this.cwRotationFromDisplayToCamera);
        if (openCamera.getFacing() == CameraFacing.FRONT) {
            UniSdkUtils.i(TAG, "Compensating rotation for front camera");
            i2 = (360 - this.cwRotationFromDisplayToCamera) % 360;
        } else {
            i2 = this.cwRotationFromDisplayToCamera;
        }
        UniSdkUtils.i(TAG, "Clockwise rotation from display to camera: " + i2);
        this.screenResolution = point;
        if (point == null) {
            this.screenResolution = Util.getScreenSize(this.context);
        }
        UniSdkUtils.i(TAG, "Screen resolution in current orientation: " + this.screenResolution);
        this.cameraResolution = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, this.screenResolution);
        UniSdkUtils.i(TAG, "Camera resolution: " + this.cameraResolution);
        this.bestPreviewSize = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, this.screenResolution);
        UniSdkUtils.i(TAG, "Best available preview size: " + this.bestPreviewSize);
    }

    void setDesiredCameraParameters(OpenCamera openCamera, boolean z) {
        Camera camera = openCamera.getCamera();
        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null) {
            UniSdkUtils.w(TAG, "Device error: no camera parameters are available. Proceeding without configuration.");
            return;
        }
        UniSdkUtils.i(TAG, "Initial camera parameters: " + parameters.flatten());
        if (z) {
            UniSdkUtils.w(TAG, "In camera config safe mode -- most settings will not be honored");
        }
        CameraConfigurationUtils.setFocus(parameters, true, true, z);
        if (!z) {
            CameraConfigurationUtils.setVideoStabilization(parameters);
            CameraConfigurationUtils.setFocusArea(parameters);
            CameraConfigurationUtils.setMetering(parameters);
        }
        parameters.setPreviewSize(this.bestPreviewSize.x, this.bestPreviewSize.y);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(this.cwRotationFromDisplayToCamera);
        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        if (previewSize != null) {
            if (this.bestPreviewSize.x == previewSize.width && this.bestPreviewSize.y == previewSize.height) {
                return;
            }
            UniSdkUtils.w(TAG, "Camera said it supported preview size " + this.bestPreviewSize.x + 'x' + this.bestPreviewSize.y + ", but after setting it, preview size is " + previewSize.width + 'x' + previewSize.height);
            this.bestPreviewSize.x = previewSize.width;
            this.bestPreviewSize.y = previewSize.height;
        }
    }

    Point getCameraResolution() {
        return this.cameraResolution;
    }

    Point getScreenResolution() {
        return this.screenResolution;
    }

    boolean getTorchState(Camera camera) {
        Camera.Parameters parameters;
        if (camera == null || (parameters = camera.getParameters()) == null) {
            return false;
        }
        String flashMode = parameters.getFlashMode();
        return ViewProps.ON.equals(flashMode) || "torch".equals(flashMode);
    }

    void setTorch(Camera camera, boolean z) {
        Camera.Parameters parameters = camera.getParameters();
        CameraConfigurationUtils.setTorch(parameters, z);
        camera.setParameters(parameters);
    }

    public void updateScreenResolution(int i, int i2, OpenCamera openCamera) {
        if (this.screenResolution == null || openCamera == null || openCamera.getCamera() == null) {
            return;
        }
        Camera.Parameters parameters = openCamera.getCamera().getParameters();
        this.screenResolution.x = i;
        this.screenResolution.y = i2;
        UniSdkUtils.i(TAG, "Screen resolution in current orientation: " + this.screenResolution);
        this.cameraResolution = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, this.screenResolution);
        UniSdkUtils.i(TAG, "Camera resolution: " + this.cameraResolution);
        this.bestPreviewSize = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, this.screenResolution);
        UniSdkUtils.i(TAG, "Best available preview size: " + this.bestPreviewSize);
    }
}