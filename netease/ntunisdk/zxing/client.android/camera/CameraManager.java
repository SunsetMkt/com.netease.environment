package com.netease.ntunisdk.zxing.client.android.camera;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.view.SurfaceHolder;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.zxing.client.android.CaptureActivity;
import com.netease.ntunisdk.zxing.client.android.ZoomHandler;
import com.netease.ntunisdk.zxing.client.android.camera.open.OpenCamera;
import com.netease.ntunisdk.zxing.client.android.camera.open.OpenCameraInterface;
import java.io.IOException;

/* loaded from: classes.dex */
public final class CameraManager {
    private static final int MAX_FRAME_HEIGHT = 900;
    private static final int MAX_FRAME_WIDTH = 1200;
    private static final int MIN_FRAME_HEIGHT = 350;
    private static final int MIN_FRAME_WIDTH = 350;
    private static final String TAG = "UniQR manager";
    private final CaptureActivity activity;
    private AutoFocusManager autoFocusManager;
    private OpenCamera camera;
    private int cameraDisplayOrientation;
    private final CameraConfigurationManager configManager;
    private final Context context;
    private Rect framingRect;
    private Rect framingRectInPreview;
    private boolean initialized;
    private final PreviewCallback previewCallback;
    private boolean previewing;
    private int requestedFramingRectHeight;
    private int requestedFramingRectWidth;
    private boolean forceInitFromCameraParameters = false;
    private int requestedCameraId = -1;

    public CameraManager(CaptureActivity captureActivity) {
        this.activity = captureActivity;
        this.context = captureActivity;
        CameraConfigurationManager cameraConfigurationManager = new CameraConfigurationManager(captureActivity);
        this.configManager = cameraConfigurationManager;
        this.previewCallback = new PreviewCallback(cameraConfigurationManager);
    }

    public synchronized void openDriver(SurfaceHolder surfaceHolder, Point point, boolean z) throws IOException {
        this.forceInitFromCameraParameters = z;
        OpenCamera openCameraOpen = this.camera;
        if (openCameraOpen == null) {
            openCameraOpen = OpenCameraInterface.open(this.requestedCameraId);
            if (openCameraOpen == null) {
                throw new IOException("Camera.open() failed to return object from driver");
            }
            this.camera = openCameraOpen;
        }
        openCameraOpen.getCamera().setPreviewDisplay(surfaceHolder);
        if (!this.initialized || z) {
            this.initialized = true;
            this.configManager.initFromCameraParameters(openCameraOpen, point);
            if (this.requestedFramingRectWidth > 0 && this.requestedFramingRectHeight > 0) {
                setManualFramingRect(this.requestedFramingRectWidth, this.requestedFramingRectHeight);
                this.requestedFramingRectWidth = 0;
                this.requestedFramingRectHeight = 0;
            }
        }
        Camera.Parameters parameters = openCameraOpen.getCamera().getParameters();
        String strFlatten = parameters == null ? null : parameters.flatten();
        try {
            this.configManager.setDesiredCameraParameters(openCameraOpen, false);
        } catch (RuntimeException unused) {
            UniSdkUtils.i(TAG, "Camera rejected parameters. Setting only minimal safe-mode parameters");
            UniSdkUtils.i(TAG, "Resetting to saved camera params: " + strFlatten);
            if (strFlatten != null) {
                Camera.Parameters parameters2 = openCameraOpen.getCamera().getParameters();
                parameters2.unflatten(strFlatten);
                try {
                    openCameraOpen.getCamera().setParameters(parameters2);
                    this.configManager.setDesiredCameraParameters(openCameraOpen, true);
                } catch (RuntimeException unused2) {
                    UniSdkUtils.e(TAG, "Camera rejected even safe-mode parameters! No configuration");
                }
            }
        }
        ZoomHandler.init(openCameraOpen.getCamera());
    }

    public synchronized void openDriver(SurfaceHolder surfaceHolder, Point point) throws IOException {
        openDriver(surfaceHolder, point, false);
    }

    public synchronized boolean isOpen() {
        return this.camera != null;
    }

    public synchronized void closeDriver() {
        if (this.camera != null) {
            this.camera.release();
            this.camera = null;
            this.framingRect = null;
            this.framingRectInPreview = null;
            ZoomHandler.destroy();
        }
    }

    public synchronized void startPreview() {
        OpenCamera openCamera = this.camera;
        if (openCamera != null && !this.previewing) {
            openCamera.getCamera().startPreview();
            this.previewing = true;
            this.autoFocusManager = new AutoFocusManager(openCamera.getCamera());
        }
    }

    public synchronized void stopPreview() {
        if (this.autoFocusManager != null) {
            this.autoFocusManager.stop();
            this.autoFocusManager = null;
        }
        if (this.camera != null && this.previewing) {
            this.camera.getCamera().stopPreview();
            this.previewCallback.setHandler(null, 0);
            this.previewing = false;
        }
    }

    public synchronized void setTorch(boolean z) {
        if (this.camera == null) {
            return;
        }
        if (z != this.configManager.getTorchState(this.camera.getCamera()) && this.camera != null) {
            if (this.autoFocusManager != null) {
                this.autoFocusManager.stop();
            }
            this.configManager.setTorch(this.camera.getCamera(), z);
            if (this.autoFocusManager != null) {
                AutoFocusManager autoFocusManager = new AutoFocusManager(this.camera.getCamera());
                this.autoFocusManager = autoFocusManager;
                autoFocusManager.start();
            }
        }
    }

    public synchronized void autoFocus() {
        if (this.camera == null) {
            return;
        }
        if (this.autoFocusManager != null) {
            this.autoFocusManager.start();
        }
    }

    public synchronized void requestPreviewFrame(Handler handler, int i) {
        OpenCamera openCamera = this.camera;
        if (openCamera != null && this.previewing) {
            this.previewCallback.setHandler(handler, i);
            openCamera.getCamera().setOneShotPreviewCallback(this.previewCallback);
        }
    }

    public synchronized Rect getFramingRect() {
        int iFindDesiredDimensionInRange;
        if (this.framingRect == null || this.forceInitFromCameraParameters) {
            if (this.camera == null) {
                return null;
            }
            int i = 0;
            this.forceInitFromCameraParameters = false;
            int headHeight = this.activity.getHeadHeight();
            Point screenResolution = this.configManager.getScreenResolution();
            if (screenResolution == null) {
                return null;
            }
            UniSdkUtils.i(TAG, "screenResolution: " + screenResolution);
            if (screenResolution.x < screenResolution.y) {
                iFindDesiredDimensionInRange = findDesiredDimensionInRange(screenResolution.x, 350, MAX_FRAME_WIDTH);
            } else {
                iFindDesiredDimensionInRange = findDesiredDimensionInRange(screenResolution.y, 350, 900);
            }
            UniSdkUtils.i(TAG, "width,height = " + iFindDesiredDimensionInRange);
            int i2 = (screenResolution.x - iFindDesiredDimensionInRange) / 2;
            if (!this.activity.isPortrait()) {
                i = headHeight;
            }
            int i3 = ((screenResolution.y - iFindDesiredDimensionInRange) - i) / 2;
            this.framingRect = new Rect(i2, i3, i2 + iFindDesiredDimensionInRange, iFindDesiredDimensionInRange + i3);
            UniSdkUtils.i(TAG, "Calculated framing rect: " + this.framingRect);
        }
        return this.framingRect;
    }

    public Point getScreenResolution() {
        return this.configManager.getScreenResolution();
    }

    private static int findDesiredDimensionInRange(int i, int i2, int i3) {
        int i4 = (i * 5) / 8;
        return i4 < i2 ? i2 : i4 > i3 ? i3 : i4;
    }

    public synchronized Rect getFramingRectInPreview() {
        if (this.framingRectInPreview == null) {
            Rect framingRect = getFramingRect();
            if (framingRect == null) {
                return null;
            }
            Rect rect = new Rect(framingRect);
            Point cameraResolution = this.configManager.getCameraResolution();
            Point screenResolution = this.configManager.getScreenResolution();
            if (cameraResolution != null && screenResolution != null) {
                if (screenResolution.x < screenResolution.y) {
                    rect.left = (rect.left * cameraResolution.y) / screenResolution.x;
                    rect.right = (rect.right * cameraResolution.y) / screenResolution.x;
                    rect.top = (rect.top * cameraResolution.x) / screenResolution.y;
                    rect.bottom = (rect.bottom * cameraResolution.x) / screenResolution.y;
                } else {
                    rect.left = (rect.left * cameraResolution.x) / screenResolution.x;
                    rect.right = (rect.right * cameraResolution.x) / screenResolution.x;
                    rect.top = (rect.top * cameraResolution.y) / screenResolution.y;
                    rect.bottom = (rect.bottom * cameraResolution.y) / screenResolution.y;
                }
                this.framingRectInPreview = rect;
            }
            return null;
        }
        return this.framingRectInPreview;
    }

    public synchronized void setManualCameraId(int i) {
        this.requestedCameraId = i;
    }

    public synchronized void setManualFramingRect(int i, int i2) {
        if (this.initialized) {
            Point screenResolution = this.configManager.getScreenResolution();
            if (i > screenResolution.x) {
                i = screenResolution.x;
            }
            if (i2 > screenResolution.y) {
                i2 = screenResolution.y;
            }
            int i3 = (screenResolution.x - i) / 2;
            int i4 = (screenResolution.y - i2) / 2;
            this.framingRect = new Rect(i3, i4, i + i3, i2 + i4);
            UniSdkUtils.i(TAG, "Calculated manual framing rect: " + this.framingRect);
            this.framingRectInPreview = null;
        } else {
            this.requestedFramingRectWidth = i;
            this.requestedFramingRectHeight = i2;
        }
    }

    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] bArr, int i, int i2) {
        Rect framingRectInPreview = getFramingRectInPreview();
        if (framingRectInPreview == null || this.autoFocusManager == null) {
            return null;
        }
        try {
            return new PlanarYUVLuminanceSource(bArr, i, i2, framingRectInPreview.left, framingRectInPreview.top, framingRectInPreview.width(), framingRectInPreview.height(), false);
        } catch (Exception e) {
            UniSdkUtils.e(TAG, "new PlanarYUVLuminanceSource exception:" + e.getMessage());
            return null;
        }
    }

    public void setCameraDisplayOrientation(int i) {
        Camera camera;
        OpenCamera openCamera = this.camera;
        if (openCamera == null || (camera = openCamera.getCamera()) == null) {
            return;
        }
        UniSdkUtils.i(TAG, "setCameraDisplayOrientation: " + i);
        if (i == 90) {
            setDisplayOrientation(camera, 180);
        } else if (i == 270) {
            setDisplayOrientation(camera, 0);
        }
    }

    private void setDisplayOrientation(Camera camera, int i) {
        UniSdkUtils.i(TAG, "setCameraDisplayOrientation: " + i + " " + this.cameraDisplayOrientation);
        camera.setDisplayOrientation(i);
        this.cameraDisplayOrientation = i;
    }

    public Point getCameraResolution() {
        return this.configManager.getCameraResolution();
    }
}