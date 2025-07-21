package com.netease.ntunisdk.zxing.client.android;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import java.io.ByteArrayOutputStream;
import java.util.Map;

/* loaded from: classes.dex */
public final class DecodeHandler extends Handler {
    private static final String TAG = "UniQR decode";
    private final CaptureActivity activity;
    private final MultiFormatReader multiFormatReader;
    private boolean running = true;

    DecodeHandler(CaptureActivity captureActivity, Map<DecodeHintType, Object> map) {
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        this.multiFormatReader = multiFormatReader;
        multiFormatReader.setHints(map);
        this.activity = captureActivity;
    }

    private int getResId(String str, String str2) {
        return this.activity.getResources().getIdentifier("ntunisdk_" + str, str2, this.activity.getPackageName());
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        if (this.running) {
            if (message.what == getResId("decode", ResIdReader.RES_TYPE_ID) && message.obj != null) {
                try {
                    decode((byte[]) message.obj, message.arg1, message.arg2);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (message.what == getResId("quit", ResIdReader.RES_TYPE_ID)) {
                this.running = false;
                Looper looperMyLooper = Looper.myLooper();
                if (looperMyLooper != null) {
                    looperMyLooper.quit();
                }
            }
        }
    }

    private void decode(byte[] bArr, int i, int i2) {
        byte[] bArr2;
        long jCurrentTimeMillis = System.currentTimeMillis();
        Rect framingRectInPreview = this.activity.getCameraManager().getFramingRectInPreview();
        if (framingRectInPreview == null) {
            return;
        }
        if (framingRectInPreview.left + framingRectInPreview.width() > i || framingRectInPreview.top + framingRectInPreview.height() > i2) {
            UniSdkUtils.d(TAG, "decode (rect.left + rect.width() > width) || (rect.top + rect.height() > height)");
            return;
        }
        boolean z = false;
        if (i < i2) {
            bArr2 = new byte[bArr.length];
            for (int i3 = 0; i3 < i; i3++) {
                for (int i4 = 0; i4 < i2; i4++) {
                    bArr2[(((i4 * i) + i) - i3) - 1] = bArr[(i3 * i2) + i4];
                }
            }
            z = true;
        } else {
            bArr2 = bArr;
        }
        PlanarYUVLuminanceSource planarYUVLuminanceSourceBuildLuminanceSource = this.activity.getCameraManager().buildLuminanceSource(bArr2, i, i2);
        Result resultDecodeResultWithSource = decodeResultWithSource(this.multiFormatReader, planarYUVLuminanceSourceBuildLuminanceSource);
        if (z && resultDecodeResultWithSource == null) {
            planarYUVLuminanceSourceBuildLuminanceSource = this.activity.getCameraManager().buildLuminanceSource(bArr, i, i2);
            resultDecodeResultWithSource = decodeResultWithSource(this.multiFormatReader, planarYUVLuminanceSourceBuildLuminanceSource);
        }
        this.multiFormatReader.reset();
        sendDecodeResult(jCurrentTimeMillis, resultDecodeResultWithSource, planarYUVLuminanceSourceBuildLuminanceSource);
    }

    public static Result decodeResultWithSource(MultiFormatReader multiFormatReader, PlanarYUVLuminanceSource planarYUVLuminanceSource) {
        if (planarYUVLuminanceSource == null) {
            return null;
        }
        Result rawResult = getRawResult(multiFormatReader, new BinaryBitmap(new HybridBinarizer(planarYUVLuminanceSource)));
        if (rawResult == null) {
            rawResult = getRawResult(multiFormatReader, new BinaryBitmap(new HybridBinarizer(planarYUVLuminanceSource.invert())));
        }
        return rawResult == null ? getRawResult(multiFormatReader, new BinaryBitmap(new GlobalHistogramBinarizer(planarYUVLuminanceSource))) : rawResult;
    }

    public static Result decodeResultWithRGBSource(MultiFormatReader multiFormatReader, RGBLuminanceSource rGBLuminanceSource) {
        if (rGBLuminanceSource == null) {
            return null;
        }
        Result rawResult = getRawResult(multiFormatReader, new BinaryBitmap(new HybridBinarizer(rGBLuminanceSource)));
        if (rawResult == null) {
            rawResult = getRawResult(multiFormatReader, new BinaryBitmap(new HybridBinarizer(rGBLuminanceSource.invert())));
        }
        return rawResult == null ? getRawResult(multiFormatReader, new BinaryBitmap(new GlobalHistogramBinarizer(rGBLuminanceSource))) : rawResult;
    }

    private static Result getRawResult(MultiFormatReader multiFormatReader, BinaryBitmap binaryBitmap) {
        try {
            return multiFormatReader.decodeWithState(binaryBitmap);
        } catch (ReaderException unused) {
            return null;
        }
    }

    private void sendDecodeResult(long j, Result result, PlanarYUVLuminanceSource planarYUVLuminanceSource) {
        Handler handler = this.activity.getHandler();
        if (result == null) {
            if (handler != null) {
                Message.obtain(handler, getResId("decode_failed", ResIdReader.RES_TYPE_ID)).sendToTarget();
                return;
            }
            return;
        }
        UniSdkUtils.i(TAG, "Found barcode in " + (System.currentTimeMillis() - j) + " ms");
        if (handler != null) {
            Message messageObtain = Message.obtain(handler, getResId("decode_succeeded", ResIdReader.RES_TYPE_ID), result);
            Bundle bundle = new Bundle();
            bundleThumbnail(planarYUVLuminanceSource, bundle);
            messageObtain.setData(bundle);
            messageObtain.sendToTarget();
        }
    }

    private static void bundleThumbnail(PlanarYUVLuminanceSource planarYUVLuminanceSource, Bundle bundle) {
        int[] iArrRenderThumbnail = planarYUVLuminanceSource.renderThumbnail();
        int thumbnailWidth = planarYUVLuminanceSource.getThumbnailWidth();
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(iArrRenderThumbnail, 0, thumbnailWidth, thumbnailWidth, planarYUVLuminanceSource.getThumbnailHeight(), Bitmap.Config.ARGB_8888);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapCreateBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        bundle.putByteArray(DecodeThread.BARCODE_BITMAP, byteArrayOutputStream.toByteArray());
        bundle.putFloat(DecodeThread.BARCODE_SCALED_FACTOR, thumbnailWidth / planarYUVLuminanceSource.getWidth());
    }
}