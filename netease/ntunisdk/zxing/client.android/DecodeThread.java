package com.netease.ntunisdk.zxing.client.android;

import android.os.Handler;
import android.os.Looper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.netease.ntunisdk.base.UniSdkUtils;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/* loaded from: classes.dex */
final class DecodeThread extends Thread {
    public static final String BARCODE_BITMAP = "barcode_bitmap";
    public static final String BARCODE_SCALED_FACTOR = "barcode_scaled_factor";
    private static final String TAG = "UniQR decode t";
    private final CaptureActivity activity;
    private Handler handler;
    private final CountDownLatch handlerInitLatch = new CountDownLatch(1);
    private final Map<DecodeHintType, Object> hints;

    DecodeThread(CaptureActivity captureActivity, Collection<BarcodeFormat> collection, Map<DecodeHintType, ?> map, String str) {
        this.activity = captureActivity;
        EnumMap enumMap = new EnumMap(DecodeHintType.class);
        this.hints = enumMap;
        if (map != null) {
            enumMap.putAll(map);
        }
        if (collection == null || collection.isEmpty()) {
            collection = EnumSet.noneOf(BarcodeFormat.class);
            collection.addAll(DecodeFormatManager.PRODUCT_FORMATS);
            collection.addAll(DecodeFormatManager.INDUSTRIAL_FORMATS);
            collection.addAll(DecodeFormatManager.QR_CODE_FORMATS);
            collection.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
        }
        this.hints.put(DecodeHintType.POSSIBLE_FORMATS, collection);
        if (str != null) {
            this.hints.put(DecodeHintType.CHARACTER_SET, str);
        }
        UniSdkUtils.i(TAG, "Hints: " + this.hints);
    }

    Handler getHandler() throws InterruptedException {
        try {
            this.handlerInitLatch.await();
        } catch (InterruptedException unused) {
        }
        return this.handler;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Looper.prepare();
        this.handler = new DecodeHandler(this.activity, this.hints);
        this.handlerInitLatch.countDown();
        Looper.loop();
    }
}