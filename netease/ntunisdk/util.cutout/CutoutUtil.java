package com.netease.ntunisdk.util.cutout;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import com.netease.ntunisdk.util.cutout.SingleScreenFoldingUtil;

/* loaded from: classes.dex */
public class CutoutUtil {
    static int[] FALSE_WATERFALL_SAFETY = {0, 0, 0, 0};
    private static final String TAG = "CutoutUtil";
    private static CutoutInterface cutoutImpl;
    private static WaterfallInterface waterfallImpl;

    private static void initCutoutImpl() {
        reset();
        String str = Build.BRAND;
        String str2 = Build.MANUFACTURER;
        if (cutoutImpl == null) {
            if (Build.VERSION.SDK_INT >= 28) {
                cutoutImpl = new CutoutAndroidP();
            } else if (str.equalsIgnoreCase("samsung")) {
                cutoutImpl = new CutoutSamsung();
            } else if (str.equalsIgnoreCase("Xiaomi")) {
                cutoutImpl = new CutoutXiaomi();
            } else if (str.equalsIgnoreCase("OPPO")) {
                cutoutImpl = new CutoutOppo();
            } else if (str.equalsIgnoreCase("VIVO")) {
                cutoutImpl = new CutoutVivo();
            } else if (str.equalsIgnoreCase("HUAWEI") || str.equalsIgnoreCase("HONOR")) {
                cutoutImpl = new CutoutHuawei();
            } else if (str.equalsIgnoreCase("OnePlus")) {
                cutoutImpl = new CutoutOneplus();
            } else if (str2.equalsIgnoreCase("meizu")) {
                cutoutImpl = new CutoutMeizu();
            } else if (str.equalsIgnoreCase("nubia")) {
                cutoutImpl = new CutoutNubia();
            } else if (str.equalsIgnoreCase("smartisan")) {
                cutoutImpl = new CutoutSmartisan();
            } else if (str.equalsIgnoreCase("lge")) {
                cutoutImpl = new CutoutLg();
            } else if (str.equalsIgnoreCase("Lenovo")) {
                cutoutImpl = new CutoutLenovo();
            } else {
                cutoutImpl = new CutoutDefault();
            }
            if (waterfallImpl == null) {
                if (Build.VERSION.SDK_INT >= 30) {
                    waterfallImpl = new WaterfallAndroidR();
                } else if (str.equalsIgnoreCase("VIVO")) {
                    waterfallImpl = new CutoutVivo();
                } else if (str.equalsIgnoreCase("HUAWEI") || str.equalsIgnoreCase("HONOR")) {
                    waterfallImpl = new CutoutHuawei();
                }
            }
        }
        Log.i(TAG, "initCutoutImpl:" + cutoutImpl + ", " + waterfallImpl);
    }

    public static boolean hasCutout(Activity activity) {
        initCutoutImpl();
        return cutoutImpl.hasCutout(activity);
    }

    public static int[] getCutoutWidthHeight(Activity activity) {
        initCutoutImpl();
        return cutoutImpl.getCutoutWidthHeight(activity);
    }

    public static int[] getCutoutPosition(Activity activity) {
        initCutoutImpl();
        return cutoutImpl.getCutoutPosition(activity);
    }

    public static boolean hasWaterfall(Activity activity) {
        initCutoutImpl();
        WaterfallInterface waterfallInterface = waterfallImpl;
        if (waterfallInterface != null) {
            return waterfallInterface.hasWaterfall(activity);
        }
        return false;
    }

    public static int[] getSafeInset(Activity activity) {
        initCutoutImpl();
        int[] iArr = FALSE_WATERFALL_SAFETY;
        WaterfallInterface waterfallInterface = waterfallImpl;
        return waterfallInterface != null ? waterfallInterface.getSafeInset(activity) : iArr;
    }

    public static void reset() {
        cutoutImpl = null;
        waterfallImpl = null;
    }

    public static void initUtil(Activity activity, SingleScreenFoldingUtil.OnInitFinishLister onInitFinishLister) {
        SingleScreenFoldingUtil.init(activity, onInitFinishLister);
    }
}