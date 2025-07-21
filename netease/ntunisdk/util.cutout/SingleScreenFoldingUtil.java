package com.netease.ntunisdk.util.cutout;

import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.View;

/* loaded from: classes.dex */
public class SingleScreenFoldingUtil {
    private static final String TAG = "SingleScreenFoldingUtil";
    static String[] TARGET_MODEL = {"PAL-AL00"};
    static int dis_left = Integer.MIN_VALUE;
    static int dis_right = Integer.MIN_VALUE;
    static int dis_top = Integer.MIN_VALUE;

    public enum LocationType {
        BASE_LEFT,
        BASE_RIGHT
    }

    public interface OnInitFinishLister {
        void onFinish(boolean z);
    }

    public static void init(final Activity activity, final OnInitFinishLister onInitFinishLister) {
        String[] strArr = TARGET_MODEL;
        int length = strArr.length;
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            if (strArr[i].equalsIgnoreCase(Build.MODEL)) {
                z = true;
                break;
            }
            i++;
        }
        if (!z) {
            if (onInitFinishLister != null) {
                onInitFinishLister.onFinish(true);
            }
        } else if (Build.VERSION.SDK_INT >= 28) {
            final View decorView = activity.getWindow().getDecorView();
            decorView.post(new Runnable() { // from class: com.netease.ntunisdk.util.cutout.SingleScreenFoldingUtil.1
                /* JADX WARN: Removed duplicated region for block: B:31:0x00c8  */
                /* JADX WARN: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
                @Override // java.lang.Runnable
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct code enable 'Show inconsistent code' option in preferences
                */
                public void run() {
                    /*
                        r10 = this;
                        java.lang.String r0 = "SingleScreenFoldingUtil"
                        r1 = 1
                        r2 = 0
                        android.view.View r3 = r1     // Catch: java.lang.Exception -> Laa
                        android.view.WindowInsets r3 = r3.getRootWindowInsets()     // Catch: java.lang.Exception -> Laa
                        if (r3 == 0) goto La8
                        android.view.DisplayCutout r3 = r3.getDisplayCutout()     // Catch: java.lang.Exception -> Laa
                        if (r3 == 0) goto La8
                        java.util.List r3 = r3.getBoundingRects()     // Catch: java.lang.Exception -> Laa
                        if (r3 == 0) goto La8
                        int r4 = r3.size()     // Catch: java.lang.Exception -> Laa
                        if (r4 <= 0) goto La8
                        java.lang.Object r3 = r3.get(r2)     // Catch: java.lang.Exception -> Laa
                        android.graphics.Rect r3 = (android.graphics.Rect) r3     // Catch: java.lang.Exception -> Laa
                        int r4 = r3.left     // Catch: java.lang.Exception -> Laa
                        int r5 = r3.top     // Catch: java.lang.Exception -> Laa
                        int r6 = r3.right     // Catch: java.lang.Exception -> Laa
                        int r3 = r3.bottom     // Catch: java.lang.Exception -> Laa
                        int r7 = android.os.Build.VERSION.SDK_INT     // Catch: java.lang.Exception -> Laa
                        r8 = 30
                        if (r7 < r8) goto L39
                        android.app.Activity r7 = r2     // Catch: java.lang.Exception -> Laa
                        android.view.Display r7 = r7.getDisplay()     // Catch: java.lang.Exception -> Laa
                        goto L43
                    L39:
                        android.app.Activity r7 = r2     // Catch: java.lang.Exception -> Laa
                        android.view.WindowManager r7 = r7.getWindowManager()     // Catch: java.lang.Exception -> Laa
                        android.view.Display r7 = r7.getDefaultDisplay()     // Catch: java.lang.Exception -> Laa
                    L43:
                        android.graphics.Point r8 = new android.graphics.Point     // Catch: java.lang.Exception -> Laa
                        r8.<init>()     // Catch: java.lang.Exception -> Laa
                        r7.getRealSize(r8)     // Catch: java.lang.Exception -> Laa
                        int r7 = r8.x     // Catch: java.lang.Exception -> Laa
                        int r8 = r8.y     // Catch: java.lang.Exception -> Laa
                        android.app.Activity r9 = r2     // Catch: java.lang.Exception -> Laa
                        android.content.res.Resources r9 = r9.getResources()     // Catch: java.lang.Exception -> Laa
                        android.content.res.Configuration r9 = r9.getConfiguration()     // Catch: java.lang.Exception -> Laa
                        int r9 = r9.orientation     // Catch: java.lang.Exception -> Laa
                        if (r9 != r1) goto L65
                        com.netease.ntunisdk.util.cutout.SingleScreenFoldingUtil.dis_top = r5     // Catch: java.lang.Exception -> Laa
                        int r7 = r7 - r6
                        com.netease.ntunisdk.util.cutout.SingleScreenFoldingUtil.dis_right = r7     // Catch: java.lang.Exception -> Laa
                        com.netease.ntunisdk.util.cutout.SingleScreenFoldingUtil.dis_left = r4     // Catch: java.lang.Exception -> Laa
                        goto L79
                    L65:
                        int r9 = r7 / 3
                        if (r6 >= r9) goto L71
                        com.netease.ntunisdk.util.cutout.SingleScreenFoldingUtil.dis_top = r4     // Catch: java.lang.Exception -> Laa
                        com.netease.ntunisdk.util.cutout.SingleScreenFoldingUtil.dis_right = r5     // Catch: java.lang.Exception -> Laa
                        int r8 = r8 - r3
                        com.netease.ntunisdk.util.cutout.SingleScreenFoldingUtil.dis_left = r8     // Catch: java.lang.Exception -> Laa
                        goto L79
                    L71:
                        int r7 = r7 - r6
                        com.netease.ntunisdk.util.cutout.SingleScreenFoldingUtil.dis_top = r7     // Catch: java.lang.Exception -> Laa
                        int r8 = r8 - r3
                        com.netease.ntunisdk.util.cutout.SingleScreenFoldingUtil.dis_right = r8     // Catch: java.lang.Exception -> Laa
                        com.netease.ntunisdk.util.cutout.SingleScreenFoldingUtil.dis_left = r5     // Catch: java.lang.Exception -> Laa
                    L79:
                        java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> La4
                        r2.<init>()     // Catch: java.lang.Exception -> La4
                        java.lang.String r3 = "init -> info -> dis_top: "
                        r2.append(r3)     // Catch: java.lang.Exception -> La4
                        int r3 = com.netease.ntunisdk.util.cutout.SingleScreenFoldingUtil.dis_top     // Catch: java.lang.Exception -> La4
                        r2.append(r3)     // Catch: java.lang.Exception -> La4
                        java.lang.String r3 = " ;dis_right: "
                        r2.append(r3)     // Catch: java.lang.Exception -> La4
                        int r3 = com.netease.ntunisdk.util.cutout.SingleScreenFoldingUtil.dis_right     // Catch: java.lang.Exception -> La4
                        r2.append(r3)     // Catch: java.lang.Exception -> La4
                        java.lang.String r3 = " ;dis_left: "
                        r2.append(r3)     // Catch: java.lang.Exception -> La4
                        int r3 = com.netease.ntunisdk.util.cutout.SingleScreenFoldingUtil.dis_left     // Catch: java.lang.Exception -> La4
                        r2.append(r3)     // Catch: java.lang.Exception -> La4
                        java.lang.String r2 = r2.toString()     // Catch: java.lang.Exception -> La4
                        android.util.Log.d(r0, r2)     // Catch: java.lang.Exception -> La4
                        goto Lc4
                    La4:
                        r2 = move-exception
                        r1 = r2
                        r2 = 1
                        goto Lab
                    La8:
                        r1 = 0
                        goto Lc4
                    Laa:
                        r1 = move-exception
                    Lab:
                        java.lang.StringBuilder r3 = new java.lang.StringBuilder
                        r3.<init>()
                        java.lang.String r4 = "init -> e: "
                        r3.append(r4)
                        java.lang.String r1 = r1.getMessage()
                        r3.append(r1)
                        java.lang.String r1 = r3.toString()
                        android.util.Log.e(r0, r1)
                        r1 = r2
                    Lc4:
                        com.netease.ntunisdk.util.cutout.SingleScreenFoldingUtil$OnInitFinishLister r0 = r3
                        if (r0 == 0) goto Lcb
                        r0.onFinish(r1)
                    Lcb:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.util.cutout.SingleScreenFoldingUtil.AnonymousClass1.run():void");
                }
            });
        } else if (onInitFinishLister != null) {
            onInitFinishLister.onFinish(true);
        }
    }

    public static int[] getCutoutLocation(Activity activity, int[] iArr, LocationType locationType) {
        Display defaultDisplay;
        int i;
        int i2;
        int i3;
        if (Build.VERSION.SDK_INT < 28 || dis_top == Integer.MIN_VALUE || dis_right == Integer.MIN_VALUE) {
            return iArr;
        }
        int i4 = iArr[0];
        int i5 = iArr[1];
        int i6 = iArr[2];
        int i7 = iArr[3];
        int i8 = activity.getResources().getConfiguration().orientation;
        if (Build.VERSION.SDK_INT >= 30) {
            defaultDisplay = activity.getDisplay();
        } else {
            defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        }
        Point point = new Point();
        defaultDisplay.getRealSize(point);
        int i9 = point.x;
        int i10 = point.y;
        if (i8 == 1) {
            if (locationType == LocationType.BASE_RIGHT) {
                i = i9 - dis_right;
                i3 = i - (i6 - i4);
            } else {
                i3 = dis_left;
                i = i3 + (i6 - i4);
            }
        } else if (i6 < i9 / 3) {
            if (locationType == LocationType.BASE_RIGHT) {
                i3 = dis_top;
                int i11 = dis_right;
                i7 = (i7 - i5) + i11;
                i5 = i11;
                i = i3 + (i6 - i4);
            } else {
                i3 = dis_top;
                int i12 = i10 - dis_left;
                i5 = i12 - (i7 - i5);
                i7 = i12;
                i = i3 + (i6 - i4);
            }
        } else {
            if (locationType == LocationType.BASE_RIGHT) {
                i = i9 - dis_top;
                int i13 = i10 - dis_right;
                i2 = i - (i6 - i4);
                i5 = i13 - (i7 - i5);
                i7 = i13;
            } else {
                int i14 = dis_left;
                i = i9 - dis_top;
                i7 = (i7 - i5) + i14;
                i2 = i - (i6 - i4);
                i5 = i14;
            }
            i3 = i2;
        }
        return new int[]{i3, i5, i, i7};
    }
}