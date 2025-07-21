package com.netease.ntunisdk.zxing.utils;

import android.graphics.Bitmap;
import androidx.core.view.MotionEventCompat;
import org.xbill.DNS.WKSRecord;

/* loaded from: classes.dex */
public class BitmapUtil {
    public static byte[] getBitmapYUVBytes(Bitmap bitmap, boolean z) {
        if (bitmap != null) {
            try {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int i = width * height;
                int[] iArr = new int[i];
                bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
                byte[] bArr = new byte[i + (((width % 2 == 0 ? width : width + 1) * (height % 2 == 0 ? height : height + 1)) / 2)];
                encodeYUV420SP(bArr, iArr, width, height);
                if (z) {
                    bitmap.recycle();
                }
                return bArr;
            } catch (Exception | OutOfMemoryError unused) {
            }
        }
        return null;
    }

    private static void encodeYUV420SP(byte[] bArr, int[] iArr, int i, int i2) {
        int i3 = i * i2;
        int i4 = 0;
        int i5 = 0;
        for (int i6 = 0; i6 < i2; i6++) {
            int i7 = 0;
            while (i7 < i) {
                int i8 = iArr[i5];
                int i9 = (iArr[i5] & 16711680) >> 16;
                int i10 = (iArr[i5] & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                int i11 = iArr[i5] & 255;
                i5++;
                int i12 = (((((i9 * 66) + (i10 * WKSRecord.Service.PWDGEN)) + (i11 * 25)) + 128) >> 8) + 16;
                int i13 = (((((i9 * (-38)) - (i10 * 74)) + (i11 * 112)) + 128) >> 8) + 128;
                int i14 = (((((i9 * 112) - (i10 * 94)) - (i11 * 18)) + 128) >> 8) + 128;
                int iMax = Math.max(0, Math.min(i12, 255));
                int iMax2 = Math.max(0, Math.min(i13, 255));
                int iMax3 = Math.max(0, Math.min(i14, 255));
                int i15 = i4 + 1;
                bArr[i4] = (byte) iMax;
                if (i6 % 2 == 0 && i7 % 2 == 0) {
                    int i16 = i3 + 1;
                    bArr[i3] = (byte) iMax3;
                    i3 = i16 + 1;
                    bArr[i16] = (byte) iMax2;
                }
                i7++;
                i4 = i15;
            }
        }
    }
}