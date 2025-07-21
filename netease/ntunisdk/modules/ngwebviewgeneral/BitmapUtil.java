package com.netease.ntunisdk.modules.ngwebviewgeneral;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import com.netease.ntunisdk.modules.ngwebviewgeneral.log.NgWebviewLog;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes.dex */
public class BitmapUtil {
    private static final int DEFAULT_DECODE_MEMORY_LIMIT = 2097152;
    private static final String TAG = "gm_bridge BitmapUtil";

    public static Bitmap decodeResource(Context context, String str) {
        return BitmapFactory.decodeResource(context.getResources(), ResIdReader.getDrawableId(context, str));
    }

    public static Bitmap decodeFile(String str, int i, int i2) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        options.inSampleSize = calculateInSampleSize(options, i, i2);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(str, options);
    }

    public static Bitmap decodeFile(String str) {
        return BitmapFactory.decodeFile(str);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        int i5 = 1;
        if (i3 > i2 || i4 > i) {
            int i6 = i3 / 2;
            int i7 = i4 / 2;
            while (i6 / i5 > i2 && i7 / i5 > i) {
                i5 *= 2;
            }
        }
        return i5;
    }

    public static Bitmap createBitmap(Context context, Object obj) throws FileNotFoundException {
        if (obj == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        int i = 1;
        options.inJustDecodeBounds = true;
        if (obj instanceof Uri) {
            decodeStream(context, (Uri) obj, options);
        } else if (obj instanceof String) {
            decodeFile((String) obj, options);
        }
        options.inJustDecodeBounds = false;
        NgWebviewLog.d(TAG, "JustDecodeBounds : outWidth=" + options.outWidth + ", outHeight=" + options.outHeight, new Object[0]);
        if (options.outWidth <= 0 || options.outHeight <= 0) {
            return getBitmap(context, obj, options);
        }
        int i2 = options.outWidth * 2 * options.outHeight;
        NgWebviewLog.d(TAG, "original bitmap size= " + i2, new Object[0]);
        if (i2 > 2097152) {
            int i3 = i2 / 2097152;
            while (i < i3) {
                i *= 4;
            }
            int iSqrt = (int) Math.sqrt(i);
            NgWebviewLog.d(TAG, "scale= " + i3 + ", inSampleSize= " + iSqrt, new Object[0]);
            options.inSampleSize = iSqrt;
        }
        return getBitmap(context, obj, options);
    }

    private static Bitmap getBitmap(Context context, Object obj, BitmapFactory.Options options) {
        if (obj instanceof Uri) {
            return decodeStream(context, (Uri) obj, options);
        }
        if (obj instanceof String) {
            return decodeFile((String) obj, options);
        }
        return null;
    }

    private static Bitmap decodeFile(String str, BitmapFactory.Options options) {
        return BitmapFactory.decodeFile(str, options);
    }

    private static Bitmap decodeStream(Context context, Uri uri, BitmapFactory.Options options) throws FileNotFoundException {
        try {
            InputStream inputStreamOpenInputStream = context.getContentResolver().openInputStream(uri);
            if (inputStreamOpenInputStream != null) {
                return BitmapFactory.decodeStream(inputStreamOpenInputStream, null, options);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean saveBitmap(Bitmap bitmap, File file, int i) {
        boolean z;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int iMin = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap bitmap2 = bitmap;
        double d = 1.0d;
        int i2 = 75;
        while (true) {
            NgWebviewLog.d(TAG, "start compress ...", new Object[0]);
            bitmap2.compress(Bitmap.CompressFormat.JPEG, i2, byteArrayOutputStream);
            if (byteArrayOutputStream.size() < i) {
                NgWebviewLog.d(TAG, "compress finish,size = " + byteArrayOutputStream.size(), new Object[0]);
                if (bitmap2 != bitmap) {
                    bitmap2.recycle();
                }
                z = true;
            } else {
                double d2 = iMin;
                Double.isNaN(d2);
                if (d2 * d < 100.0d) {
                    i2 -= 15;
                    NgWebviewLog.d(TAG, "reduce quality to " + i2, new Object[0]);
                    if (i2 < 30) {
                        if (bitmap2 != bitmap) {
                            bitmap2.recycle();
                        }
                        NgWebviewLog.e(TAG, "can't reduce quality any more");
                        z = false;
                    }
                } else {
                    d /= 2.0d;
                    NgWebviewLog.d(TAG, "scale bitmap to " + d, new Object[0]);
                    double width = (double) bitmap.getWidth();
                    Double.isNaN(width);
                    double height = (double) bitmap.getHeight();
                    Double.isNaN(height);
                    Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (width * d), (int) (height * d), false);
                    if (bitmap2 != bitmap) {
                        bitmap2.recycle();
                    }
                    bitmap2 = bitmapCreateScaledBitmap;
                }
                byteArrayOutputStream.reset();
            }
        }
        if (z) {
            return writeFile(byteArrayOutputStream, file);
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0033 A[Catch: IOException -> 0x003e, PHI: r3
  0x0033: PHI (r3v5 java.io.FileOutputStream) = (r3v4 java.io.FileOutputStream), (r3v6 java.io.FileOutputStream) binds: [B:21:0x0031, B:25:0x003b] A[DONT_GENERATE, DONT_INLINE], TryCatch #5 {IOException -> 0x003e, blocks: (B:9:0x0018, B:20:0x002e, B:22:0x0033, B:24:0x0038), top: B:33:0x0008 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean writeFile(java.io.ByteArrayOutputStream r4, java.io.File r5) throws java.lang.Throwable {
        /*
            r0 = 0
            if (r4 == 0) goto L3e
            if (r5 != 0) goto L6
            goto L3e
        L6:
            r1 = 1
            r2 = 0
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L22 java.io.IOException -> L2d java.io.FileNotFoundException -> L37
            r3.<init>(r5)     // Catch: java.lang.Throwable -> L22 java.io.IOException -> L2d java.io.FileNotFoundException -> L37
            byte[] r5 = r4.toByteArray()     // Catch: java.lang.Throwable -> L20 java.io.IOException -> L2e java.io.FileNotFoundException -> L38
            int r2 = r4.size()     // Catch: java.lang.Throwable -> L20 java.io.IOException -> L2e java.io.FileNotFoundException -> L38
            r3.write(r5, r0, r2)     // Catch: java.lang.Throwable -> L20 java.io.IOException -> L2e java.io.FileNotFoundException -> L38
            r4.close()     // Catch: java.io.IOException -> L3e
            r3.close()     // Catch: java.io.IOException -> L3e
            r0 = 1
            goto L3e
        L20:
            r5 = move-exception
            goto L24
        L22:
            r5 = move-exception
            r3 = r2
        L24:
            r4.close()     // Catch: java.io.IOException -> L2c
            if (r3 == 0) goto L2c
            r3.close()     // Catch: java.io.IOException -> L2c
        L2c:
            throw r5
        L2d:
            r3 = r2
        L2e:
            r4.close()     // Catch: java.io.IOException -> L3e
            if (r3 == 0) goto L3e
        L33:
            r3.close()     // Catch: java.io.IOException -> L3e
            goto L3e
        L37:
            r3 = r2
        L38:
            r4.close()     // Catch: java.io.IOException -> L3e
            if (r3 == 0) goto L3e
            goto L33
        L3e:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.modules.ngwebviewgeneral.BitmapUtil.writeFile(java.io.ByteArrayOutputStream, java.io.File):boolean");
    }

    public static void deleteFile(String str) {
        try {
            new File(str).delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getImgSavePath(Context context) throws IOException {
        File filesDir;
        String str = String.format("unisdk_%s.jpg", new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS", Locale.US).format(new Date()));
        if (isSDCardAvailable()) {
            filesDir = getExternalFileDir(context);
        } else {
            filesDir = context.getFilesDir();
        }
        File file = new File(filesDir, str);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException unused) {
                return null;
            }
        }
        return file.getAbsolutePath();
    }

    public static boolean isSDCardAvailable() {
        String externalStorageState = Environment.getExternalStorageState();
        return externalStorageState != null && externalStorageState.equals("mounted");
    }

    public static File getExternalFileDir(Context context) {
        File externalFilesDir;
        if (Build.VERSION.SDK_INT >= 8 && (externalFilesDir = context.getExternalFilesDir("")) != null) {
            return externalFilesDir;
        }
        return new File(Environment.getExternalStorageDirectory().getPath() + ("/Android/data/" + context.getPackageName() + "/files/"));
    }
}