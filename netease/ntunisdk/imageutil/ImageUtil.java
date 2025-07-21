package com.netease.ntunisdk.imageutil;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import com.facebook.imagepipeline.common.RotationOptions;
import com.netease.mc.mi.R;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.modules.personalinfolist.HookManager;
import java.io.IOException;

/* loaded from: classes.dex */
public class ImageUtil {
    private static final String TAG = "QR imageutil";

    public static String getRecentImagePath(Context context, int i) {
        Cursor cursorQueryContentResolver;
        String str = null;
        try {
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver contentResolver = context.getContentResolver();
            String str2 = String.format("(%s=? or %s=?) and %s>%s", "mime_type", "mime_type", "date_modified", Long.valueOf((System.currentTimeMillis() / 1000) - (i * 60)));
            UniSdkUtils.d(TAG, str2);
            String[] strArr = {"image/jpeg", "image/png"};
            if (Build.VERSION.SDK_INT >= 26) {
                Bundle bundle = new Bundle();
                bundle.putString("android:query-arg-sql-selection", str2);
                bundle.putStringArray("android:query-arg-sql-selection-args", strArr);
                bundle.putString("android:query-arg-limit", "1");
                bundle.putString("android:query-arg-offset", "0");
                bundle.putInt("android:query-arg-sort-direction", 1);
                cursorQueryContentResolver = HookManager.queryContentResolver(contentResolver, uri, null, bundle, null);
            } else {
                cursorQueryContentResolver = HookManager.queryContentResolver(contentResolver, uri, null, str2, strArr, "date_modified desc limit 0,1");
            }
        } catch (Throwable th) {
            th = th;
        }
        if (cursorQueryContentResolver == null) {
            return null;
        }
        if (cursorQueryContentResolver.moveToNext()) {
            int columnIndex = cursorQueryContentResolver.getColumnIndex("_data");
            int columnIndex2 = cursorQueryContentResolver.getColumnIndex("date_modified");
            int columnIndex3 = cursorQueryContentResolver.getColumnIndex("mime_type");
            String string = columnIndex != -1 ? cursorQueryContentResolver.getString(columnIndex) : "";
            try {
                UniSdkUtils.d(TAG, String.format("path = %s, dateModified = %s, mimeType = %s", string, columnIndex2 != -1 ? cursorQueryContentResolver.getString(columnIndex2) : "", columnIndex3 != -1 ? cursorQueryContentResolver.getString(columnIndex3) : ""));
                str = string;
            } catch (Throwable th2) {
                th = th2;
                str = string;
                UniSdkUtils.d(TAG, "getRecentImagePath Exception:" + th.getMessage());
                return str;
            }
        }
        cursorQueryContentResolver.close();
        return str;
    }

    public static void showImagePopupWindow(Activity activity, String str, final View view, final View.OnClickListener onClickListener) {
        if (isFinishing(activity)) {
            UniSdkUtils.i(TAG, activity + " is finishing");
            return;
        }
        View viewInflate = LayoutInflater.from(activity).inflate(R.layout.ntunisdk_scanner_popup_image, (ViewGroup) null);
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.iv_latest_image);
        Bitmap bitmap = getBitmap(str, activity.getResources().getDimensionPixelSize(R.dimen.ntunisdk_scanner__popup_image_w), activity.getResources().getDimensionPixelSize(R.dimen.ntunisdk_scanner__popup_image_h));
        if (bitmap == null) {
            UniSdkUtils.d(TAG, "bitmap is null, showImagePopupWindow ignore");
            return;
        }
        imageView.setImageBitmap(bitmap);
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.imageutil.ImageUtil.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                View.OnClickListener onClickListener2 = onClickListener;
                if (onClickListener2 != null) {
                    onClickListener2.onClick(view2);
                }
            }
        });
        viewInflate.measure(0, 0);
        UniSdkUtils.d(TAG, viewInflate.getHeight() + " " + viewInflate.getMeasuredHeight());
        final PopupWindow popupWindow = new PopupWindow(viewInflate, viewInflate.getMeasuredWidth(), viewInflate.getMeasuredHeight(), false);
        popupWindow.setContentView(viewInflate);
        try {
            final int width = view.getWidth() + activity.getResources().getDimensionPixelSize(R.dimen.ntunisdk_scanner__popup_image_left_offset);
            final int i = (-(view.getHeight() + viewInflate.getMeasuredHeight())) / 2;
            if (Build.VERSION.SDK_INT >= 17 && (activity.getResources().getConfiguration().getLayoutDirection() == 1 || "1".equals(SdkMgr.getInst().getPropStr(ConstProp.ENABLE_RTL)))) {
                UniSdkUtils.i(TAG, "set xOffset");
                width = (-view.getWidth()) - activity.getResources().getDimensionPixelSize(R.dimen.ntunisdk_scanner__popup_image_left_offset);
            }
            view.post(new Runnable() { // from class: com.netease.ntunisdk.imageutil.ImageUtil.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        popupWindow.showAsDropDown(view, width, i);
                    } catch (Exception e) {
                        UniSdkUtils.d(ImageUtil.TAG, "showAsDropDown exception:" + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            UniSdkUtils.d(TAG, "showImagePopupWindow exception:" + e.getMessage());
        }
    }

    public static Bitmap getBitmap(String str, int i, int i2) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        int rotateDegree = getRotateDegree(str);
        UniSdkUtils.d(TAG, "getRotateDegree:" + rotateDegree);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        options.inSampleSize = calculateInSampleSize(options, i, i2);
        options.inJustDecodeBounds = false;
        Bitmap bitmapDecodeFile = BitmapFactory.decodeFile(str, options);
        return rotateDegree > 0 ? rotate(bitmapDecodeFile, rotateDegree, 0.0f, 0.0f, true) : bitmapDecodeFile;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        int i5 = 1;
        while (true) {
            if (i3 <= i2 && i4 <= i) {
                return i5;
            }
            i3 >>= 1;
            i4 >>= 1;
            i5 <<= 1;
        }
    }

    public static int dip2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static boolean isFinishing(Activity activity) {
        return activity == null || activity.isFinishing();
    }

    public static Bitmap rotate(Bitmap bitmap, int i, float f, float f2, boolean z) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        if (i == 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(i, f, f2);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (z && !bitmap.isRecycled() && bitmapCreateBitmap != bitmap) {
            bitmap.recycle();
        }
        return bitmapCreateBitmap;
    }

    private static boolean isEmptyBitmap(Bitmap bitmap) {
        return bitmap == null || bitmap.getWidth() == 0 || bitmap.getHeight() == 0;
    }

    public static int getRotateDegree(String str) {
        try {
            int attributeInt = new ExifInterface(str).getAttributeInt("Orientation", 1);
            if (attributeInt == 3) {
                return 180;
            }
            if (attributeInt == 6) {
                return 90;
            }
            if (attributeInt != 8) {
                return 0;
            }
            return RotationOptions.ROTATE_270;
        } catch (IOException e) {
            UniSdkUtils.d(TAG, "getRotateDegree exception:" + e.getMessage());
            return -1;
        }
    }
}