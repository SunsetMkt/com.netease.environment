package com.netease.ntunisdk.zxing.client.android.camera;

import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import com.facebook.react.uimanager.ViewProps;
import com.netease.ntunisdk.base.UniSdkUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public final class CameraConfigurationUtils {
    private static final int AREA_PER_1000 = 400;
    private static final double MAX_ASPECT_DISTORTION = 0.15d;
    private static final float MAX_EXPOSURE_COMPENSATION = 1.5f;
    private static final int MAX_FPS = 20;
    private static final float MIN_EXPOSURE_COMPENSATION = 0.0f;
    private static final int MIN_FPS = 10;
    private static final int MIN_PREVIEW_PIXELS = 153600;
    private static final Pattern SEMICOLON = Pattern.compile(";");
    private static final String TAG = "UniQR config util";

    private CameraConfigurationUtils() {
    }

    public static void setFocus(Camera.Parameters parameters, boolean z, boolean z2, boolean z3) {
        String strFindSettableValue;
        List<String> supportedFocusModes = parameters.getSupportedFocusModes();
        if (!z) {
            strFindSettableValue = null;
        } else if (z3 || z2) {
            strFindSettableValue = findSettableValue("focus mode", supportedFocusModes, "auto");
        } else {
            strFindSettableValue = findSettableValue("focus mode", supportedFocusModes, "continuous-picture", "continuous-video", "auto");
        }
        if (!z3 && strFindSettableValue == null) {
            strFindSettableValue = findSettableValue("focus mode", supportedFocusModes, "macro", "edof");
        }
        if (strFindSettableValue != null) {
            if (strFindSettableValue.equals(parameters.getFocusMode())) {
                UniSdkUtils.i(TAG, "Focus mode already set to " + strFindSettableValue);
                return;
            }
            parameters.setFocusMode(strFindSettableValue);
        }
    }

    public static void setTorch(Camera.Parameters parameters, boolean z) {
        String strFindSettableValue;
        List<String> supportedFlashModes = parameters.getSupportedFlashModes();
        if (z) {
            strFindSettableValue = findSettableValue("flash mode", supportedFlashModes, "torch", ViewProps.ON);
        } else {
            strFindSettableValue = findSettableValue("flash mode", supportedFlashModes, "off");
        }
        if (strFindSettableValue != null) {
            if (strFindSettableValue.equals(parameters.getFlashMode())) {
                UniSdkUtils.i(TAG, "Flash mode already set to " + strFindSettableValue);
                return;
            }
            UniSdkUtils.i(TAG, "Setting flash mode to " + strFindSettableValue);
            parameters.setFlashMode(strFindSettableValue);
        }
    }

    public static void setBestExposure(Camera.Parameters parameters, boolean z) {
        int minExposureCompensation = parameters.getMinExposureCompensation();
        int maxExposureCompensation = parameters.getMaxExposureCompensation();
        float exposureCompensationStep = parameters.getExposureCompensationStep();
        if (minExposureCompensation != 0 || maxExposureCompensation != 0) {
            if (exposureCompensationStep > 0.0f) {
                int iRound = Math.round((z ? 0.0f : MAX_EXPOSURE_COMPENSATION) / exposureCompensationStep);
                float f = exposureCompensationStep * iRound;
                int iMax = Math.max(Math.min(iRound, maxExposureCompensation), minExposureCompensation);
                if (parameters.getExposureCompensation() == iMax) {
                    UniSdkUtils.i(TAG, "Exposure compensation already set to " + iMax + " / " + f);
                    return;
                }
                UniSdkUtils.i(TAG, "Setting exposure compensation to " + iMax + " / " + f);
                parameters.setExposureCompensation(iMax);
                return;
            }
        }
        UniSdkUtils.i(TAG, "Camera does not support exposure compensation");
    }

    public static void setBestPreviewFPS(Camera.Parameters parameters) {
        setBestPreviewFPS(parameters, 10, 20);
    }

    public static void setBestPreviewFPS(Camera.Parameters parameters, int i, int i2) {
        List<int[]> supportedPreviewFpsRange = parameters.getSupportedPreviewFpsRange();
        UniSdkUtils.i(TAG, "Supported FPS ranges: " + toString((Collection<int[]>) supportedPreviewFpsRange));
        if (supportedPreviewFpsRange == null || supportedPreviewFpsRange.isEmpty()) {
            return;
        }
        int[] iArr = null;
        Iterator<int[]> it = supportedPreviewFpsRange.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            int[] next = it.next();
            int i3 = next[0];
            int i4 = next[1];
            if (i3 >= i * 1000 && i4 <= i2 * 1000) {
                iArr = next;
                break;
            }
        }
        if (iArr == null) {
            UniSdkUtils.i(TAG, "No suitable FPS range?");
            return;
        }
        int[] iArr2 = new int[2];
        parameters.getPreviewFpsRange(iArr2);
        if (Arrays.equals(iArr2, iArr)) {
            UniSdkUtils.i(TAG, "FPS range already set to " + Arrays.toString(iArr));
            return;
        }
        UniSdkUtils.i(TAG, "Setting FPS range to " + Arrays.toString(iArr));
        parameters.setPreviewFpsRange(iArr[0], iArr[1]);
    }

    public static void setFocusArea(Camera.Parameters parameters) {
        if (parameters.getMaxNumFocusAreas() > 0) {
            UniSdkUtils.i(TAG, "Old focus areas: " + toString((Iterable<Camera.Area>) parameters.getFocusAreas()));
            List<Camera.Area> listBuildMiddleArea = buildMiddleArea(400);
            UniSdkUtils.i(TAG, "Setting focus area to : " + toString((Iterable<Camera.Area>) listBuildMiddleArea));
            parameters.setFocusAreas(listBuildMiddleArea);
            return;
        }
        UniSdkUtils.i(TAG, "Device does not support focus areas");
    }

    public static void setMetering(Camera.Parameters parameters) {
        if (parameters.getMaxNumMeteringAreas() > 0) {
            UniSdkUtils.i(TAG, "Old metering areas: " + parameters.getMeteringAreas());
            List<Camera.Area> listBuildMiddleArea = buildMiddleArea(400);
            UniSdkUtils.i(TAG, "Setting metering area to : " + toString((Iterable<Camera.Area>) listBuildMiddleArea));
            parameters.setMeteringAreas(listBuildMiddleArea);
            return;
        }
        UniSdkUtils.i(TAG, "Device does not support metering areas");
    }

    private static List<Camera.Area> buildMiddleArea(int i) {
        int i2 = -i;
        return Collections.singletonList(new Camera.Area(new Rect(i2, i2, i, i), 1));
    }

    public static void setVideoStabilization(Camera.Parameters parameters) {
        if (parameters.isVideoStabilizationSupported()) {
            if (parameters.getVideoStabilization()) {
                UniSdkUtils.i(TAG, "Video stabilization already enabled");
                return;
            } else {
                UniSdkUtils.i(TAG, "Enabling video stabilization...");
                parameters.setVideoStabilization(true);
                return;
            }
        }
        UniSdkUtils.i(TAG, "This device does not support video stabilization");
    }

    public static void setBarcodeSceneMode(Camera.Parameters parameters) {
        if ("barcode".equals(parameters.getSceneMode())) {
            UniSdkUtils.i(TAG, "Barcode scene mode already set");
            return;
        }
        String strFindSettableValue = findSettableValue("scene mode", parameters.getSupportedSceneModes(), "barcode");
        UniSdkUtils.i(TAG, "Barcode scene mode not set yet, set scene mode " + strFindSettableValue);
        if (strFindSettableValue != null) {
            if ("torch".equals(strFindSettableValue)) {
                strFindSettableValue = ViewProps.ON;
            }
            parameters.setSceneMode(strFindSettableValue);
        }
    }

    public static void setZoom(Camera.Parameters parameters, double d) {
        if (parameters.isZoomSupported()) {
            Integer numIndexOfClosestZoom = indexOfClosestZoom(parameters, d);
            if (numIndexOfClosestZoom == null) {
                return;
            }
            if (parameters.getZoom() == numIndexOfClosestZoom.intValue()) {
                UniSdkUtils.i(TAG, "Zoom is already set to " + numIndexOfClosestZoom);
                return;
            }
            UniSdkUtils.i(TAG, "Setting zoom to " + numIndexOfClosestZoom);
            parameters.setZoom(numIndexOfClosestZoom.intValue());
            return;
        }
        UniSdkUtils.i(TAG, "Zoom is not supported");
    }

    private static Integer indexOfClosestZoom(Camera.Parameters parameters, double d) {
        List<Integer> zoomRatios = parameters.getZoomRatios();
        UniSdkUtils.i(TAG, "Zoom ratios: " + zoomRatios);
        int maxZoom = parameters.getMaxZoom();
        if (zoomRatios == null || zoomRatios.isEmpty() || zoomRatios.size() != maxZoom + 1) {
            UniSdkUtils.w(TAG, "Invalid zoom ratios!");
            return null;
        }
        double d2 = d * 100.0d;
        double d3 = Double.POSITIVE_INFINITY;
        int i = 0;
        for (int i2 = 0; i2 < zoomRatios.size(); i2++) {
            double dIntValue = zoomRatios.get(i2).intValue();
            Double.isNaN(dIntValue);
            double dAbs = Math.abs(dIntValue - d2);
            if (dAbs < d3) {
                i = i2;
                d3 = dAbs;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Chose zoom ratio of ");
        double dIntValue2 = zoomRatios.get(i).intValue();
        Double.isNaN(dIntValue2);
        sb.append(dIntValue2 / 100.0d);
        UniSdkUtils.i(TAG, sb.toString());
        return Integer.valueOf(i);
    }

    public static void setInvertColor(Camera.Parameters parameters) {
        if ("negative".equals(parameters.getColorEffect())) {
            UniSdkUtils.i(TAG, "Negative effect already set");
            return;
        }
        String strFindSettableValue = findSettableValue("color effect", parameters.getSupportedColorEffects(), "negative");
        if (strFindSettableValue != null) {
            parameters.setColorEffect(strFindSettableValue);
        }
    }

    public static Point findBestPreviewSizeValue(Camera.Parameters parameters, Point point) {
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        if (supportedPreviewSizes == null) {
            UniSdkUtils.w(TAG, "Device returned no supported preview sizes; using default");
            Camera.Size previewSize = parameters.getPreviewSize();
            if (previewSize == null) {
                throw new IllegalStateException("Parameters contained no preview size!");
            }
            return new Point(previewSize.width, previewSize.height);
        }
        ArrayList arrayList = new ArrayList(supportedPreviewSizes);
        Collections.sort(arrayList, new Comparator<Camera.Size>() { // from class: com.netease.ntunisdk.zxing.client.android.camera.CameraConfigurationUtils.1
            AnonymousClass1() {
            }

            @Override // java.util.Comparator
            public int compare(Camera.Size size, Camera.Size size2) {
                int i = size.height * size.width;
                int i2 = size2.height * size2.width;
                if (i2 < i) {
                    return -1;
                }
                return i2 > i ? 1 : 0;
            }
        });
        double d = point.x;
        double d2 = point.y;
        Double.isNaN(d);
        Double.isNaN(d2);
        double d3 = d / d2;
        Iterator it = arrayList.iterator();
        while (true) {
            if (it.hasNext()) {
                Camera.Size size = (Camera.Size) it.next();
                int i = size.width;
                int i2 = size.height;
                if (i * i2 < MIN_PREVIEW_PIXELS) {
                    it.remove();
                } else {
                    boolean z = point.x < point.y;
                    int i3 = z ? i2 : i;
                    int i4 = z ? i : i2;
                    double d4 = i3;
                    double d5 = i4;
                    Double.isNaN(d4);
                    Double.isNaN(d5);
                    if (Math.abs((d4 / d5) - d3) > MAX_ASPECT_DISTORTION) {
                        it.remove();
                    } else if (i3 == point.x && i4 == point.y) {
                        Point point2 = new Point(i, i2);
                        UniSdkUtils.i(TAG, "Found preview size exactly matching screen size: " + point2);
                        return point2;
                    }
                }
            } else {
                if (!arrayList.isEmpty()) {
                    Camera.Size size2 = (Camera.Size) arrayList.get(0);
                    Point point3 = new Point(size2.width, size2.height);
                    UniSdkUtils.i(TAG, "Using largest suitable preview size: " + point3);
                    return point3;
                }
                Camera.Size previewSize2 = parameters.getPreviewSize();
                if (previewSize2 == null) {
                    throw new IllegalStateException("Parameters contained no preview size!");
                }
                Point point4 = new Point(previewSize2.width, previewSize2.height);
                UniSdkUtils.i(TAG, "No suitable preview sizes, using default: " + point4);
                return point4;
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.zxing.client.android.camera.CameraConfigurationUtils$1 */
    class AnonymousClass1 implements Comparator<Camera.Size> {
        AnonymousClass1() {
        }

        @Override // java.util.Comparator
        public int compare(Camera.Size size, Camera.Size size2) {
            int i = size.height * size.width;
            int i2 = size2.height * size2.width;
            if (i2 < i) {
                return -1;
            }
            return i2 > i ? 1 : 0;
        }
    }

    private static String findSettableValue(String str, Collection<String> collection, String... strArr) {
        UniSdkUtils.i(TAG, "Requesting " + str + " value from among: " + Arrays.toString(strArr));
        UniSdkUtils.i(TAG, "Supported " + str + " values: " + collection);
        if (collection != null) {
            for (String str2 : strArr) {
                if (collection.contains(str2)) {
                    UniSdkUtils.i(TAG, "Can set " + str + " to: " + str2);
                    return str2;
                }
            }
        }
        UniSdkUtils.i(TAG, "No supported values match");
        return null;
    }

    private static String toString(Collection<int[]> collection) {
        if (collection == null || collection.isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Iterator<int[]> it = collection.iterator();
        while (it.hasNext()) {
            sb.append(Arrays.toString(it.next()));
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }

    private static String toString(Iterable<Camera.Area> iterable) {
        if (iterable == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Camera.Area area : iterable) {
            sb.append(area.rect);
            sb.append(':');
            sb.append(area.weight);
            sb.append(' ');
        }
        return sb.toString();
    }

    public static String collectStats(Camera.Parameters parameters) {
        return collectStats(parameters.flatten());
    }

    public static String collectStats(CharSequence charSequence) {
        StringBuilder sb = new StringBuilder(1000);
        sb.append("BOARD=");
        sb.append(Build.BOARD);
        sb.append('\n');
        sb.append("BRAND=");
        sb.append(Build.BRAND);
        sb.append('\n');
        sb.append("CPU_ABI=");
        sb.append(Build.CPU_ABI);
        sb.append('\n');
        sb.append("DEVICE=");
        sb.append(Build.DEVICE);
        sb.append('\n');
        sb.append("DISPLAY=");
        sb.append(Build.DISPLAY);
        sb.append('\n');
        sb.append("FINGERPRINT=");
        sb.append(Build.FINGERPRINT);
        sb.append('\n');
        sb.append("HOST=");
        sb.append(Build.HOST);
        sb.append('\n');
        sb.append("ID=");
        sb.append(Build.ID);
        sb.append('\n');
        sb.append("MANUFACTURER=");
        sb.append(Build.MANUFACTURER);
        sb.append('\n');
        sb.append("MODEL=");
        sb.append(Build.MODEL);
        sb.append('\n');
        sb.append("PRODUCT=");
        sb.append(Build.PRODUCT);
        sb.append('\n');
        sb.append("TAGS=");
        sb.append(Build.TAGS);
        sb.append('\n');
        sb.append("TIME=");
        sb.append(Build.TIME);
        sb.append('\n');
        sb.append("TYPE=");
        sb.append(Build.TYPE);
        sb.append('\n');
        sb.append("USER=");
        sb.append(Build.USER);
        sb.append('\n');
        sb.append("VERSION.CODENAME=");
        sb.append(Build.VERSION.CODENAME);
        sb.append('\n');
        sb.append("VERSION.INCREMENTAL=");
        sb.append(Build.VERSION.INCREMENTAL);
        sb.append('\n');
        sb.append("VERSION.RELEASE=");
        sb.append(Build.VERSION.RELEASE);
        sb.append('\n');
        sb.append("VERSION.SDK_INT=");
        sb.append(Build.VERSION.SDK_INT);
        sb.append('\n');
        if (charSequence != null) {
            String[] strArrSplit = SEMICOLON.split(charSequence);
            Arrays.sort(strArrSplit);
            for (String str : strArrSplit) {
                sb.append(str);
                sb.append('\n');
            }
        }
        return sb.toString();
    }
}