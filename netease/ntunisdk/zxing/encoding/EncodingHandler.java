package com.netease.ntunisdk.zxing.encoding;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.netease.ntunisdk.base.UniSdkUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/* loaded from: classes.dex */
public final class EncodingHandler {
    private static final String TAG = "UniQR encode";
    private static final String UNI_QRCODE_FIXED_MARGIN = "UNI_QRCODE_FIXED_MARGIN";

    private static BitMatrix updateBit(BitMatrix bitMatrix, int i) {
        int i2 = i * 2;
        int[] enclosingRectangle = bitMatrix.getEnclosingRectangle();
        int i3 = enclosingRectangle[2] + i2;
        int i4 = enclosingRectangle[3] + i2;
        BitMatrix bitMatrix2 = new BitMatrix(i3, i4);
        bitMatrix2.clear();
        for (int i5 = i; i5 < i3 - i; i5++) {
            for (int i6 = i; i6 < i4 - i; i6++) {
                if (bitMatrix.get((i5 - i) + enclosingRectangle[0], (i6 - i) + enclosingRectangle[1])) {
                    bitMatrix2.set(i5, i6);
                }
            }
        }
        return bitMatrix2;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x004f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.graphics.Bitmap createQRCode(java.lang.String r15, int r16, int r17, java.lang.String r18) throws java.lang.NumberFormatException, com.google.zxing.WriterException {
        /*
            Method dump skipped, instructions count: 308
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.zxing.encoding.EncodingHandler.createQRCode(java.lang.String, int, int, java.lang.String):android.graphics.Bitmap");
    }

    private static int parseColorSafe(String str, int i) {
        if (TextUtils.isEmpty(str)) {
            return i;
        }
        if (!str.startsWith("#")) {
            str = "#" + str;
        }
        try {
            return Color.parseColor(str);
        } catch (Exception e) {
            UniSdkUtils.d(TAG, "parseColor Exception : " + e.getMessage());
            return i;
        }
    }

    public static String createQRCode(Context context, String str, Bitmap bitmap, int i, int i2, String str2, String str3) throws WriterException, FileNotFoundException {
        String absolutePath = context.getApplicationContext().getFilesDir().getAbsolutePath();
        File file = new File(absolutePath);
        if (!file.exists()) {
            UniSdkUtils.d(TAG, absolutePath + " not exists");
            file.mkdirs();
        }
        String str4 = absolutePath + File.separator + str2;
        createQRCode(str, i, i2, bitmap, str3).compress(str2.endsWith(".jpg") ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.PNG, 100, new FileOutputStream(str4));
        return str4;
    }

    public static Bitmap createQRCode(String str, int i, int i2, Bitmap bitmap, String str2) throws NumberFormatException, WriterException {
        Bitmap bitmapCreateQRCode = createQRCode(str, i, i2, str2);
        return bitmap != null ? addLogo(bitmapCreateQRCode, bitmap) : bitmapCreateQRCode;
    }

    private static Bitmap addLogo(Bitmap bitmap, Bitmap bitmap2) {
        if (bitmap == null) {
            return null;
        }
        if (bitmap2 == null) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int width2 = bitmap2.getWidth();
        int height2 = bitmap2.getHeight();
        if (width == 0 || height == 0) {
            return null;
        }
        if (width2 == 0 || height2 == 0) {
            return bitmap;
        }
        float f = ((width * 1.0f) / 5.0f) / width2;
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmapCreateBitmap);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
            canvas.scale(f, f, width / 2, height / 2);
            canvas.drawBitmap(bitmap2, (width - width2) / 2, (height - height2) / 2, (Paint) null);
            canvas.save();
            canvas.restore();
            return bitmapCreateBitmap;
        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }
    }
}