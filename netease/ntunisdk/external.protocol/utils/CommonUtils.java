package com.netease.ntunisdk.external.protocol.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.ntunisdk.external.protocol.data.SDKRuntime;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class CommonUtils {
    private static final String TAG = "CommonUtils";

    public static String getIpCountry() {
        Future futureSubmit;
        Callable<Response> callable = new Callable<Response>() { // from class: com.netease.ntunisdk.external.protocol.utils.CommonUtils.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Response call() throws Exception {
                return UrlConnectImpl.fetch(Const.WHO_AM_I_V2, null);
            }
        };
        try {
            if (AsyncTask.THREAD_POOL_EXECUTOR instanceof ThreadPoolExecutor) {
                futureSubmit = ((ThreadPoolExecutor) AsyncTask.THREAD_POOL_EXECUTOR).submit(callable);
                try {
                    try {
                        String contentStr = ((Response) futureSubmit.get(4000L, TimeUnit.MILLISECONDS)).getContentStr();
                        String strOptString = "";
                        if (TextUtils.isEmpty(contentStr)) {
                            strOptString = null;
                        } else {
                            try {
                                L.d(TAG, "request:https://who.easebar.com/v2, response:" + contentStr);
                                strOptString = new JSONObject(contentStr).optString("code", "");
                            } catch (Exception unused) {
                            }
                        }
                        L.d(TAG, "getCountryResult:" + strOptString);
                        return strOptString;
                    } catch (Exception e) {
                        e = e;
                        e.printStackTrace();
                        if (futureSubmit != null) {
                            futureSubmit.cancel(true);
                        }
                        return null;
                    }
                } catch (TimeoutException e2) {
                    e = e2;
                    e.printStackTrace();
                    if (futureSubmit != null) {
                        futureSubmit.cancel(true);
                    }
                    return null;
                }
            }
            return checkCountry();
        } catch (TimeoutException e3) {
            e = e3;
            futureSubmit = null;
        } catch (Exception e4) {
            e = e4;
            futureSubmit = null;
        }
    }

    public static String checkCountry() {
        String contentStr = UrlConnectImpl.fetch(Const.WHO_AM_I_V2, null, 1000, 1000).getContentStr();
        if (TextUtils.isEmpty(contentStr)) {
            return null;
        }
        try {
            L.d(TAG, contentStr);
            return new JSONObject(contentStr).optString("code", "");
        } catch (Exception unused) {
            return "";
        }
    }

    public static boolean isSouthAmerica() {
        boolean zContains = Arrays.asList(Const.SOUTH_AMERICA).contains(checkCountry());
        SDKRuntime.getInstance().setSouthAmericaAndJapan(zContains);
        return zContains;
    }

    public static String getAppName(Context context) {
        try {
            return context.getResources().getString(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.labelRes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getAppVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception unused) {
            return -1;
        }
    }

    public static boolean hasSetTaskAffinity(Activity activity) {
        if (activity == null) {
            return false;
        }
        try {
            return !activity.getPackageName().equals(getTaskAffinity(activity));
        } catch (Throwable unused) {
            return false;
        }
    }

    public static String getTaskAffinity(Activity activity) {
        try {
            return activity.getPackageManager().getActivityInfo(activity.getComponentName(), 128).taskAffinity;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setViewRtlLayout(View view) {
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 17) {
            view.setLayoutDirection(1);
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                setViewRtlLayout(viewGroup.getChildAt(i));
            }
            return;
        }
        if (Build.VERSION.SDK_INT >= 17) {
            view.setTextDirection(4);
        }
    }

    public static void changeToRTL(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        boolean zIsRTLSystem = isRTLSystem(activity);
        Configuration configuration = activity.getResources().getConfiguration();
        if (!zIsRTLSystem && SDKRuntime.getInstance().isPublishMiddleEast() && Build.VERSION.SDK_INT >= 17) {
            configuration.screenLayout = 128;
        }
        activity.getBaseContext().getResources().updateConfiguration(configuration, activity.getBaseContext().getResources().getDisplayMetrics());
        if (zIsRTLSystem || !SDKRuntime.getInstance().isPublishMiddleEast()) {
            return;
        }
        Configuration configuration2 = activity.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= 17) {
            configuration2.setLayoutDirection(new Locale("ar"));
        }
    }

    public static boolean isRTLSystem(Activity activity) {
        Locale locale;
        if (activity == null) {
            return false;
        }
        try {
            Configuration configuration = activity.getApplicationContext().getResources().getConfiguration();
            if (Build.VERSION.SDK_INT >= 24) {
                locale = configuration.getLocales().get(0);
            } else {
                locale = configuration.locale;
            }
            if (Build.VERSION.SDK_INT >= 17) {
                return TextUtils.getLayoutDirectionFromLocale(locale) == 1;
            }
            return locale.getLanguage().equalsIgnoreCase("ar");
        } catch (Throwable unused) {
            return false;
        }
    }

    public static void readGameLauncherActivity(Activity activity) {
        if (TextUtils.isEmpty(SDKRuntime.getInstance().getGameLauncherActivity())) {
            try {
                SDKRuntime.getInstance().setGameLauncherActivity(activity.getApplicationContext().getPackageManager().getActivityInfo(activity.getComponentName(), 128).metaData.getString(Const.PROTOCOL_LAUNCHER_ACTIVITY));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}