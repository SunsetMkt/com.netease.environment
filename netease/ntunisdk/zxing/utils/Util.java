package com.netease.ntunisdk.zxing.utils;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.core.content.PermissionChecker;
import com.facebook.common.util.UriUtil;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.netease.ntunisdk.SdkQRCode;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.imageutil.ImageUtil;
import com.netease.ntunisdk.langutil.LanguageUtil;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import com.netease.ntunisdk.modules.personalinfolist.HookManager;
import com.netease.ntunisdk.zxing.client.android.DecodeFormatManager;
import com.netease.ntunisdk.zxing.client.android.DecodeHandler;
import com.netease.ntunisdk.zxing.notch.NotchDevice;
import com.xiaomi.gamecenter.sdk.utils.RSASignature;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.jose4j.jwk.EllipticCurveJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;

/* loaded from: classes.dex */
public class Util {
    private static final int MAX_DECODE_PICTURE_SIZE = 2764800;
    private static final String PATH_DOCUMENT = "document";
    private static final String TAG = "UniQR util";
    private static String hexString = "0123456789ABCDEF";
    private static Dialog mProgressDialog;
    private static Toast mToast;

    public interface ScanImageCallback {
        void onFailed();

        void onSuccess(String str);
    }

    public static String bytesToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder("");
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        for (byte b : bArr) {
            String hexString2 = Integer.toHexString(b & 255);
            if (hexString2.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString2);
        }
        return sb.toString();
    }

    public static byte[] hexStringToBytes(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        String upperCase = str.toUpperCase();
        int length = upperCase.length() / 2;
        char[] charArray = upperCase.toCharArray();
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) (charToByte(charArray[i2 + 1]) | (charToByte(charArray[i2]) << 4));
        }
        return bArr;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String toHexString(String str) throws UnsupportedEncodingException {
        byte[] bytes;
        try {
            bytes = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            bytes = null;
        }
        if (bytes == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append(hexString.charAt((bytes[i] & 240) >> 4));
            sb.append(hexString.charAt((bytes[i] & 15) >> 0));
        }
        return sb.toString();
    }

    public static String hexToString(String str) {
        if ("0x".equals(str.substring(0, 2))) {
            str = str.substring(2);
        }
        int length = str.length() / 2;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            try {
                bArr[i] = (byte) (Integer.parseInt(str.substring(i2, i2 + 2), 16) & 255);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            return new String(bArr, RSASignature.c);
        } catch (Exception e2) {
            e2.printStackTrace();
            return str;
        }
    }

    public static byte[] bmpToByteArray(Bitmap bitmap, boolean z) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        if (z) {
            bitmap.recycle();
        }
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        try {
            byteArrayOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteArray;
    }

    public static byte[] getHtmlByteArray(String str) {
        InputStream inputStream = null;
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return inputStreamToByte(inputStream);
    }

    public static byte[] inputStreamToByte(InputStream inputStream) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (true) {
                int i = inputStream.read();
                if (i != -1) {
                    byteArrayOutputStream.write(i);
                } else {
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.close();
                    return byteArray;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] readFromFile(String str, int i, int i2) throws IOException {
        byte[] bArr = null;
        if (str == null) {
            return null;
        }
        File file = new File(str);
        if (!file.exists()) {
            Log.d(TAG, "readFromFile: file not found");
            return null;
        }
        if (i2 == -1) {
            i2 = (int) file.length();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("readFromFile : offset = ");
        sb.append(i);
        sb.append(" len = ");
        sb.append(i2);
        sb.append(" offset + len = ");
        int i3 = i + i2;
        sb.append(i3);
        Log.d(TAG, sb.toString());
        if (i < 0) {
            Log.e(TAG, "readFromFile invalid offset:" + i);
            return null;
        }
        if (i2 <= 0) {
            Log.e(TAG, "readFromFile invalid len:" + i2);
            return null;
        }
        if (i3 > ((int) file.length())) {
            Log.e(TAG, "readFromFile invalid file len:" + file.length());
            return null;
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(str, RsaJsonWebKey.PRIME_FACTOR_OTHER_MEMBER_NAME);
            bArr = new byte[i2];
            randomAccessFile.seek(i);
            randomAccessFile.readFully(bArr);
            randomAccessFile.close();
            return bArr;
        } catch (Exception e) {
            Log.e(TAG, "readFromFile : errMsg = " + e.getMessage());
            e.printStackTrace();
            return bArr;
        }
    }

    public static int computeSampleSize(BitmapFactory.Options options, int i, int i2) {
        int iComputeInitialSampleSize = computeInitialSampleSize(options, i, i2);
        if (iComputeInitialSampleSize > 8) {
            return ((iComputeInitialSampleSize + 7) / 8) * 8;
        }
        int i3 = 1;
        while (i3 < iComputeInitialSampleSize) {
            i3 <<= 1;
        }
        return i3;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int i, int i2) {
        int iCeil;
        int iMin;
        double d = options.outWidth;
        double d2 = options.outHeight;
        if (i2 == -1) {
            iCeil = 1;
        } else {
            Double.isNaN(d);
            Double.isNaN(d2);
            double d3 = i2;
            Double.isNaN(d3);
            iCeil = (int) Math.ceil(Math.sqrt((d * d2) / d3));
        }
        if (i == -1) {
            iMin = 128;
        } else {
            double d4 = i;
            Double.isNaN(d);
            Double.isNaN(d4);
            double dFloor = Math.floor(d / d4);
            Double.isNaN(d2);
            Double.isNaN(d4);
            iMin = (int) Math.min(dFloor, Math.floor(d2 / d4));
        }
        if (iMin < iCeil) {
            return iCeil;
        }
        if (i2 == -1 && i == -1) {
            return 1;
        }
        return i == -1 ? iCeil : iMin;
    }

    public static Bitmap readBitmap(String str) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(str + "test.jpg"));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            options.inPurgeable = true;
            options.inInputShareable = true;
            return BitmapFactory.decodeStream(fileInputStream, null, options);
        } catch (Exception | OutOfMemoryError unused) {
            return null;
        }
    }

    public static Bitmap extractThumbNail(String str, int i, int i2, boolean z) {
        double d;
        double d2;
        double d3;
        double d4;
        double d5;
        int i3;
        int i4;
        Bitmap bitmapCreateBitmap;
        if (TextUtils.isEmpty(str) || i <= 0 || i2 <= 0) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            options.inJustDecodeBounds = true;
            Bitmap bitmapDecodeFile = BitmapFactory.decodeFile(str, options);
            if (bitmapDecodeFile != null) {
                bitmapDecodeFile.recycle();
            }
            Log.d(TAG, "extractThumbNail: round=" + i2 + EllipticCurveJsonWebKey.X_MEMBER_NAME + i + ", crop=" + z);
            double d6 = options.outHeight;
            Double.isNaN(d6);
            double d7 = i;
            Double.isNaN(d7);
            double d8 = (d6 * 1.0d) / d7;
            double d9 = options.outWidth;
            Double.isNaN(d9);
            double d10 = i2;
            Double.isNaN(d10);
            double d11 = (d9 * 1.0d) / d10;
            Log.d(TAG, "extractThumbNail: extract beX = " + d11 + ", beY = " + d8);
            if (z) {
                if (d8 > d11) {
                }
            } else {
                d = d8 < d11 ? d11 : d8;
            }
            options.inSampleSize = (int) d;
            if (options.inSampleSize <= 1) {
                options.inSampleSize = 1;
            }
            while ((options.outHeight * options.outWidth) / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
                options.inSampleSize++;
            }
            if (z) {
                if (d8 > d11) {
                    Double.isNaN(d10);
                    double d12 = d10 * 1.0d;
                    double d13 = options.outHeight;
                    Double.isNaN(d13);
                    d4 = d12 * d13;
                    d5 = options.outWidth;
                    Double.isNaN(d5);
                    i4 = (int) (d4 / d5);
                    i3 = i2;
                } else {
                    Double.isNaN(d7);
                    double d14 = d7 * 1.0d;
                    double d15 = options.outWidth;
                    Double.isNaN(d15);
                    d2 = d14 * d15;
                    d3 = options.outHeight;
                    Double.isNaN(d3);
                    i3 = (int) (d2 / d3);
                    i4 = i;
                }
            } else if (d8 < d11) {
                Double.isNaN(d10);
                double d16 = d10 * 1.0d;
                double d17 = options.outHeight;
                Double.isNaN(d17);
                d4 = d16 * d17;
                d5 = options.outWidth;
                Double.isNaN(d5);
                i4 = (int) (d4 / d5);
                i3 = i2;
            } else {
                Double.isNaN(d7);
                double d18 = d7 * 1.0d;
                double d19 = options.outWidth;
                Double.isNaN(d19);
                d2 = d18 * d19;
                d3 = options.outHeight;
                Double.isNaN(d3);
                i3 = (int) (d2 / d3);
                i4 = i;
            }
            options.inJustDecodeBounds = false;
            Log.d(TAG, "bitmap required size=" + i3 + EllipticCurveJsonWebKey.X_MEMBER_NAME + i4 + ", orig=" + options.outWidth + EllipticCurveJsonWebKey.X_MEMBER_NAME + options.outHeight + ", sample=" + options.inSampleSize);
            Bitmap bitmapDecodeFile2 = BitmapFactory.decodeFile(str, options);
            if (bitmapDecodeFile2 == null) {
                Log.e(TAG, "bitmap decode failed");
                return null;
            }
            Log.d(TAG, "bitmap decoded size=" + bitmapDecodeFile2.getWidth() + EllipticCurveJsonWebKey.X_MEMBER_NAME + bitmapDecodeFile2.getHeight());
            Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmapDecodeFile2, i3, i4, true);
            if (bitmapCreateScaledBitmap != null) {
                bitmapDecodeFile2.recycle();
                bitmapDecodeFile2 = bitmapCreateScaledBitmap;
            }
            if (!z || (bitmapCreateBitmap = Bitmap.createBitmap(bitmapDecodeFile2, (bitmapDecodeFile2.getWidth() - i2) >> 1, (bitmapDecodeFile2.getHeight() - i) >> 1, i2, i)) == null) {
                return bitmapDecodeFile2;
            }
            bitmapDecodeFile2.recycle();
            Log.d(TAG, "bitmap cropped size=" + bitmapCreateBitmap.getWidth() + EllipticCurveJsonWebKey.X_MEMBER_NAME + bitmapCreateBitmap.getHeight());
            return bitmapCreateBitmap;
        } catch (OutOfMemoryError e) {
            Log.e(TAG, "decode bitmap failed: " + e.getMessage());
            return null;
        }
    }

    public static void showResultDialog(Context context, String str, String str2) {
        if (str == null) {
            return;
        }
        String strReplace = str.replace(",", "\n");
        Log.d("Util", strReplace);
        new AlertDialog.Builder(context).setTitle(str2).setMessage(strReplace).setNegativeButton("\u77e5\u9053\u4e86", (DialogInterface.OnClickListener) null).create().show();
    }

    public static void showProgressDialog(Context context, String str, String str2) {
        dismissDialog();
        if (TextUtils.isEmpty(str)) {
            str = "\u8bf7\u7a0d\u5019";
        }
        if (TextUtils.isEmpty(str2)) {
            str2 = "\u6b63\u5728\u52a0\u8f7d...";
        }
        mProgressDialog = ProgressDialog.show(context, str, str2);
    }

    public static AlertDialog showConfirmCancelDialog(Context context, String str, String str2, DialogInterface.OnClickListener onClickListener) {
        AlertDialog alertDialogCreate = new AlertDialog.Builder(context).setMessage(str2).setPositiveButton("\u786e\u8ba4", onClickListener).setNegativeButton("\u53d6\u6d88", (DialogInterface.OnClickListener) null).create();
        alertDialogCreate.setCanceledOnTouchOutside(false);
        alertDialogCreate.show();
        return alertDialogCreate;
    }

    public static void dismissDialog() {
        Dialog dialog = mProgressDialog;
        if (dialog != null) {
            dialog.dismiss();
            mProgressDialog = null;
        }
    }

    public static void toastMessage(Activity activity, String str) {
        toastMessage(activity, str, null);
    }

    public static void toastMessage(Activity activity, String str, String str2) {
        if ("w".equals(str2)) {
            Log.w("sdkDemo", str);
        } else if (RsaJsonWebKey.EXPONENT_MEMBER_NAME.equals(str2)) {
            Log.e("sdkDemo", str);
        } else {
            Log.d("sdkDemo", str);
        }
        activity.runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.zxing.utils.Util.1
            final /* synthetic */ Activity val$activity;
            final /* synthetic */ String val$message;

            AnonymousClass1(Activity activity2, String str3) {
                activity = activity2;
                str = str3;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (Util.mToast != null) {
                    Util.mToast.cancel();
                    Toast unused = Util.mToast = null;
                }
                Toast unused2 = Util.mToast = Toast.makeText(activity, str, 0);
                Util.mToast.show();
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.zxing.utils.Util$1 */
    class AnonymousClass1 implements Runnable {
        final /* synthetic */ Activity val$activity;
        final /* synthetic */ String val$message;

        AnonymousClass1(Activity activity2, String str3) {
            activity = activity2;
            str = str3;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (Util.mToast != null) {
                Util.mToast.cancel();
                Toast unused = Util.mToast = null;
            }
            Toast unused2 = Util.mToast = Toast.makeText(activity, str, 0);
            Util.mToast.show();
        }
    }

    public static Bitmap getBitmap(String str) throws IOException {
        Log.v(TAG, "getBitmap:" + str);
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            Log.v(TAG, "image download finished." + str);
            return bitmapDecodeStream;
        } catch (IOException e) {
            e.printStackTrace();
            Log.v(TAG, "getBitmap bmp fail---");
            return null;
        } catch (OutOfMemoryError e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static void release() {
        mProgressDialog = null;
        mToast = null;
    }

    public static String getPath(Context context, Uri uri) {
        Uri uri2 = null;
        if ((Build.VERSION.SDK_INT >= 19) && isDocumentUri(uri)) {
            if (isExternalStorageDocument(uri)) {
                String[] strArrSplit = getDocumentId(uri).split(":");
                if ("primary".equalsIgnoreCase(strArrSplit[0])) {
                    return Environment.getExternalStorageDirectory() + "/" + strArrSplit[1];
                }
            } else {
                if (isDownloadsDocument(uri)) {
                    String documentId = getDocumentId(uri);
                    if (TextUtils.isEmpty(documentId)) {
                        Log.d(TAG, uri.toString() + " parse failed(id is null). -> 1_1");
                        return null;
                    }
                    if (documentId.startsWith("raw:")) {
                        return documentId.substring(4);
                    }
                    try {
                        return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(documentId)), null, null);
                    } catch (NumberFormatException e) {
                        UniSdkUtils.d(SdkQRCode.TAG, "Zxing decodeUseZxing Exception :" + e.getMessage());
                        return null;
                    }
                }
                if (isMediaDocument(uri)) {
                    String[] strArrSplit2 = getDocumentId(uri).split(":");
                    String str = strArrSplit2[0];
                    if ("image".equals(str)) {
                        uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(str)) {
                        uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(str)) {
                        uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    return getDataColumn(context, uri2, "_id=?", new String[]{strArrSplit2[1]});
                }
            }
        } else {
            if (UriUtil.LOCAL_CONTENT_SCHEME.equalsIgnoreCase(uri.getScheme())) {
                if (isGooglePhotosUri(uri)) {
                    return uri.getLastPathSegment();
                }
                return getDataColumn(context, uri, null, null);
            }
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        return null;
    }

    private static boolean isDocumentUri(Uri uri) {
        List<String> pathSegments = uri.getPathSegments();
        return pathSegments.size() >= 2 && PATH_DOCUMENT.equals(pathSegments.get(0));
    }

    private static String getDocumentId(Uri uri) {
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() < 2) {
            throw new IllegalArgumentException("Not a document: " + uri);
        }
        if (!PATH_DOCUMENT.equals(pathSegments.get(0))) {
            throw new IllegalArgumentException("Not a document: " + uri);
        }
        return pathSegments.get(1);
    }

    public static String getDataColumn(Context context, Uri uri, String str, String[] strArr) throws Throwable {
        Cursor cursor = null;
        try {
            Cursor cursorQueryContentResolver = HookManager.queryContentResolver(context.getContentResolver(), uri, new String[]{"_data"}, str, strArr, null);
            if (cursorQueryContentResolver != null) {
                try {
                    if (cursorQueryContentResolver.moveToFirst()) {
                        String string = cursorQueryContentResolver.getString(cursorQueryContentResolver.getColumnIndexOrThrow("_data"));
                        if (cursorQueryContentResolver != null) {
                            cursorQueryContentResolver.close();
                        }
                        return string;
                    }
                } catch (Throwable th) {
                    th = th;
                    cursor = cursorQueryContentResolver;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            if (cursorQueryContentResolver != null) {
                cursorQueryContentResolver.close();
            }
            return null;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static Point getScreenSize(Context context) {
        Point point = new Point();
        if (context == null) {
            return point;
        }
        try {
            WindowManager windowManager = (WindowManager) context.getSystemService("window");
            if (Build.VERSION.SDK_INT >= 30) {
                Rect bounds = windowManager.getCurrentWindowMetrics().getBounds();
                point.x = bounds.width();
                point.y = bounds.height();
            } else if (Build.VERSION.SDK_INT >= 17) {
                windowManager.getDefaultDisplay().getRealSize(point);
            } else {
                windowManager.getDefaultDisplay().getSize(point);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return point;
    }

    public static void hideSystemUI(Window window) {
        if (window == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 28) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.layoutInDisplayCutoutMode = 1;
            window.setAttributes(attributes);
        }
        try {
            View decorView = window.getDecorView();
            if (Build.VERSION.SDK_INT < 19) {
                decorView.setSystemUiVisibility(8);
            } else {
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | 256 | 4096 | 2 | 512 | 4 | 1024);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static void hideNavigationBar(Window window) {
        if (window == null) {
            return;
        }
        try {
            View decorView = window.getDecorView();
            if (Build.VERSION.SDK_INT < 19) {
                decorView.setSystemUiVisibility(8);
            } else {
                decorView.setSystemUiVisibility(5894);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static boolean selfPermissionGranted(Context context, String str) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        if (getTargetSdkVersion(context) >= 23) {
            if (context.checkSelfPermission(str) == 0) {
                return true;
            }
        } else if (PermissionChecker.checkSelfPermission(context, str) == 0) {
            return true;
        }
        return false;
    }

    public static int getTargetSdkVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.targetSdkVersion;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void adaptNotch(Activity activity, boolean z, View[] viewArr) {
        if (ImageUtil.isFinishing(activity)) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) activity.findViewById(R.id.content);
        ViewGroup viewGroup2 = viewGroup != null ? (ViewGroup) viewGroup.getChildAt(0) : null;
        if (viewGroup2 == null || viewGroup2.getChildCount() <= 0) {
            return;
        }
        int length = viewArr.length;
        NotchDevice.NotchAffectView[] notchAffectViewArr = new NotchDevice.NotchAffectView[length];
        for (int i = 0; i < length; i++) {
            notchAffectViewArr[i] = new NotchDevice.NotchAffectView(viewArr[i], true, true, z, 2);
        }
        NotchDevice.getInstance(activity).justify(activity, activity.getWindow(), notchAffectViewArr);
    }

    public static void setViewTextDirection(View view, int i) {
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 17) {
            view.setLayoutDirection(1);
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                setViewTextDirection(viewGroup.getChildAt(i2), i);
            }
            return;
        }
        if (Build.VERSION.SDK_INT >= 17) {
            view.setTextDirection(i);
        }
    }

    /* renamed from: com.netease.ntunisdk.zxing.utils.Util$2 */
    class AnonymousClass2 implements Runnable {
        final /* synthetic */ ScanImageCallback val$callback;
        final /* synthetic */ String val$path;

        AnonymousClass2(String str, ScanImageCallback scanImageCallback) {
            str = str;
            scanImageCallback = scanImageCallback;
        }

        @Override // java.lang.Runnable
        public void run() {
            int i = 1;
            String strDecodeBitmap = null;
            for (int i2 = 0; i2 < 5; i2++) {
                strDecodeBitmap = Util.decodeBitmap(str, i);
                if (!TextUtils.isEmpty(strDecodeBitmap)) {
                    break;
                }
                i *= 2;
            }
            if (TextUtils.isEmpty(strDecodeBitmap)) {
                scanImageCallback.onFailed();
            } else {
                scanImageCallback.onSuccess(strDecodeBitmap);
            }
        }
    }

    public static void scanningImage(String str, ScanImageCallback scanImageCallback) {
        UniSdkUtils.d(SdkQRCode.TAG, "scanningImage... : " + str);
        new Thread(new Runnable() { // from class: com.netease.ntunisdk.zxing.utils.Util.2
            final /* synthetic */ ScanImageCallback val$callback;
            final /* synthetic */ String val$path;

            AnonymousClass2(String str2, ScanImageCallback scanImageCallback2) {
                str = str2;
                scanImageCallback = scanImageCallback2;
            }

            @Override // java.lang.Runnable
            public void run() {
                int i = 1;
                String strDecodeBitmap = null;
                for (int i2 = 0; i2 < 5; i2++) {
                    strDecodeBitmap = Util.decodeBitmap(str, i);
                    if (!TextUtils.isEmpty(strDecodeBitmap)) {
                        break;
                    }
                    i *= 2;
                }
                if (TextUtils.isEmpty(strDecodeBitmap)) {
                    scanImageCallback.onFailed();
                } else {
                    scanImageCallback.onSuccess(strDecodeBitmap);
                }
            }
        }).start();
    }

    public static String decodeBitmap(String str, int i) {
        Bitmap bitmapDecodeFile;
        BitmapFactory.Options options = new BitmapFactory.Options();
        UniSdkUtils.d(SdkQRCode.TAG, "sampleSize : " + i);
        options.inSampleSize = i;
        try {
            bitmapDecodeFile = BitmapFactory.decodeFile(str, options);
        } catch (Throwable th) {
            UniSdkUtils.e(SdkQRCode.TAG, "decodeBitmap exception:" + th.getMessage());
            bitmapDecodeFile = null;
        }
        if (bitmapDecodeFile == null) {
            return null;
        }
        int width = bitmapDecodeFile.getWidth();
        int height = bitmapDecodeFile.getHeight();
        UniSdkUtils.d(SdkQRCode.TAG, "bmpWidth : " + width);
        UniSdkUtils.d(SdkQRCode.TAG, "bmpHeight : " + height);
        String scanResult = getScanResult(bitmapDecodeFile, width, height);
        bitmapDecodeFile.recycle();
        return scanResult;
    }

    private static String getScanResult(Bitmap bitmap, int i, int i2) {
        int[] iArr = new int[i * i2];
        bitmap.getPixels(iArr, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        if (i > 0 && i2 > 0) {
            String strDecodeUseZxing = decodeUseZxing(iArr, i, i2);
            UniSdkUtils.d(SdkQRCode.TAG, "scanResult:" + strDecodeUseZxing);
            return strDecodeUseZxing;
        }
        UniSdkUtils.e(SdkQRCode.TAG, "get YUV error");
        return null;
    }

    private static String decodeUseZxing(int[] iArr, int i, int i2) {
        UniSdkUtils.d(SdkQRCode.TAG, "decodeUseZxing");
        long jCurrentTimeMillis = System.currentTimeMillis();
        String text = null;
        try {
            MultiFormatReader multiFormatReader = new MultiFormatReader();
            multiFormatReader.setHints(DecodeFormatManager.TWO_DIMENSIONAL_HINTS);
            Result resultDecodeResultWithRGBSource = DecodeHandler.decodeResultWithRGBSource(multiFormatReader, new RGBLuminanceSource(i, i2, iArr));
            if (resultDecodeResultWithRGBSource != null) {
                text = resultDecodeResultWithRGBSource.getText();
            }
        } catch (Exception e) {
            UniSdkUtils.d(SdkQRCode.TAG, "Zxing decodeUseZxing Exception :" + e.getMessage());
        }
        UniSdkUtils.d(SdkQRCode.TAG, "Zxing time : " + (System.currentTimeMillis() - jCurrentTimeMillis));
        return text;
    }

    public static int getResId(Context context, String str, String str2) {
        return context.getResources().getIdentifier(str, str2, context.getPackageName());
    }

    public static String getLocalString(Context context, String str) {
        return LanguageUtil.getString(context, getResId(context, str, ResIdReader.RES_TYPE_STRING));
    }

    public static void requestMediaPermission(Activity activity, int i) {
        if (Build.VERSION.SDK_INT >= 23) {
            activity.requestPermissions(new String[]{getMediaPermission(activity)}, i);
        }
    }

    public static String getMediaPermission(Context context) {
        return (Build.VERSION.SDK_INT < 33 || getTargetSdkVersion(context) < 33) ? "android.permission.READ_EXTERNAL_STORAGE" : "android.permission.READ_MEDIA_IMAGES";
    }

    public static boolean isMediaPermissionGranted(Context context) {
        return selfPermissionGranted(context, getMediaPermission(context));
    }
}