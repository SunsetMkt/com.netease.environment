package com.netease.ntunisdk.modules.deviceinfo;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.provider.Settings;
import android.text.TextUtils;
import com.netease.ntunisdk.modules.base.utils.LogModule;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes.dex */
public class GaidUtils implements Runnable {
    private static final String KEY_GAID_CACHED = "gaid_cached";
    private static final String KEY_GAID_CACHED_FAKE = "gaid_cached_fake";
    private static final String TAG = "GaidUtils";
    private static DeviceInfoModule deviceInfoModule;
    private static SharedPreferences sharedPreferences;
    private WeakReference<Context> mRef;
    private static HashSet<Callback> sCallbackSet = new HashSet<>();
    private static boolean sHasCheckedGms = false;
    private static boolean sHasInstalledGms = false;
    private static boolean sHasCheckedAmazonAd = false;
    private static boolean sHasInstalledAmazonAd = false;

    public interface Callback {
        void done(String str);
    }

    private GaidUtils(Context context) {
        this.mRef = new WeakReference<>(context);
    }

    public static void requestGaid(Context context, DeviceInfoModule deviceInfoModule2) {
        deviceInfoModule = deviceInfoModule2;
        Thread thread = new Thread(new GaidUtils(context));
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(TAG, 0);
        }
        thread.start();
    }

    public static void cacheGaid(String str) {
        SharedPreferences sharedPreferences2 = sharedPreferences;
        if (sharedPreferences2 != null) {
            SharedPreferences.Editor editorEdit = sharedPreferences2.edit();
            editorEdit.putString(KEY_GAID_CACHED, str);
            editorEdit.commit();
        }
    }

    private static void cacheFakeGaid(Context context, String str) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(TAG, 0);
        }
        SharedPreferences.Editor editorEdit = sharedPreferences.edit();
        editorEdit.putString(KEY_GAID_CACHED_FAKE, str);
        editorEdit.commit();
    }

    public static void doWhenGaidReady(Context context, String str) {
        cacheGaid(str);
        Iterator<Callback> it = sCallbackSet.iterator();
        while (it.hasNext()) {
            Callback next = it.next();
            if (next != null) {
                next.done(str);
            }
        }
        DeviceInfoModule deviceInfoModule2 = deviceInfoModule;
        if (deviceInfoModule2 != null) {
            deviceInfoModule2.gaidCallback("{\"methodId\": \"gaidCallback\", \"gaid\": \"" + str + "\"}");
        }
        sCallbackSet.clear();
    }

    public static String getCachedGaid() {
        SharedPreferences sharedPreferences2 = sharedPreferences;
        if (sharedPreferences2 != null) {
            return sharedPreferences2.getString(KEY_GAID_CACHED, null);
        }
        return null;
    }

    public static String getCachedFakeGaid(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(TAG, 0);
        }
        return sharedPreferences.getString(KEY_GAID_CACHED_FAKE, null);
    }

    public static String getCachedGaid(Callback callback) {
        if (callback != null) {
            sCallbackSet.add(callback);
        }
        return getCachedGaid();
    }

    public static String getCachedFakeGaid(Context context, Callback callback) {
        if (callback != null) {
            sCallbackSet.add(callback);
        }
        return getCachedFakeGaid(context);
    }

    public static boolean hasInstalledAmazonAdvertisingId(Context context) {
        if (!sHasCheckedAmazonAd) {
            try {
                PackageManager packageManager = context.getPackageManager();
                LogModule.i(TAG, "Amazon advertisingidsettings pkgInfo: " + packageManager.getPackageInfo("com.amazon.advertisingidsettings", 0));
                LogModule.i(TAG, "Amazon advertisingidsettings appInfo: " + packageManager.getApplicationInfo("com.amazon.advertisingidsettings", 0));
                sHasInstalledAmazonAd = true;
            } catch (Throwable unused) {
                LogModule.w(TAG, "Amazon advertisingidsettings is missing.");
            }
            sHasCheckedAmazonAd = true;
        }
        return sHasInstalledAmazonAd;
    }

    public static boolean hasInstalledGooglePlayServices(Context context) {
        if (!sHasCheckedGms) {
            try {
                PackageManager packageManager = context.getPackageManager();
                LogModule.i(TAG, "gms pkgInfo: " + packageManager.getPackageInfo("com.google.android.gms", 0));
                LogModule.i(TAG, "gms appInfo: " + packageManager.getApplicationInfo("com.google.android.gms", 0));
                LogModule.i(TAG, "store pkgInfo: " + packageManager.getPackageInfo("com.android.vending", 0));
                LogModule.i(TAG, "store appInfo: " + packageManager.getApplicationInfo("com.android.vending", 0));
                sHasInstalledGms = true;
            } catch (Throwable unused) {
                LogModule.w(TAG, "Google Play services is missing.");
            }
            sHasCheckedGms = true;
        }
        return sHasInstalledGms;
    }

    private String getFromPlayServiceClient(Context context) {
        String strEncode = null;
        try {
            Object objInvoke = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient").getDeclaredMethod("getAdvertisingIdInfo", Context.class).invoke(null, context);
            Class<?> cls = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient$Info");
            boolean zBooleanValue = ((Boolean) cls.getDeclaredMethod("isLimitAdTrackingEnabled", new Class[0]).invoke(objInvoke, new Object[0])).booleanValue();
            LogModule.i(TAG, "before get id:" + ((String) null) + " / limit:" + zBooleanValue);
            if (zBooleanValue) {
                LogModule.w(TAG, "gaid limited");
                strEncode = "";
            } else {
                String str = (String) cls.getDeclaredMethod("getId", new Class[0]).invoke(objInvoke, new Object[0]);
                try {
                    strEncode = URLEncoder.encode(str, "UTF-8");
                    if (!TextUtils.isEmpty(strEncode)) {
                        doWhenGaidReady(context, strEncode);
                    }
                } catch (Throwable th) {
                    th = th;
                    strEncode = str;
                    getCachedGaid();
                    LogModule.i(TAG, "" + th.getMessage());
                    return strEncode;
                }
            }
            LogModule.i(TAG, "after get id:" + strEncode + " / limit:" + zBooleanValue);
        } catch (Throwable th2) {
            th = th2;
        }
        return strEncode;
    }

    private void getFromBindService(Context context) {
        boolean zBindService;
        AdvertisingConnection advertisingConnection = new AdvertisingConnection();
        Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
        intent.setPackage("com.google.android.gms");
        try {
            try {
                zBindService = context.bindService(intent, advertisingConnection, 1);
            } catch (Throwable th) {
                th = th;
                zBindService = false;
            }
            try {
                if (zBindService) {
                    try {
                        new AdvertisingInterface(advertisingConnection.getBinder()).getId(context);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    cacheGaid("");
                }
            } catch (Throwable th2) {
                th = th2;
                try {
                    th.printStackTrace();
                    if (zBindService) {
                        context.unbindService(advertisingConnection);
                    }
                    return;
                } catch (Throwable th3) {
                    if (zBindService) {
                        try {
                            context.unbindService(advertisingConnection);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    throw th3;
                }
            }
            if (zBindService) {
                context.unbindService(advertisingConnection);
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.mRef.get() != null) {
            DeviceInfoModule deviceInfoModule2 = deviceInfoModule;
            if (DeviceInfoModule.disableAccessData) {
                return;
            }
            if (!deviceInfoModule.isOversea) {
                cacheGaid("");
            } else if (TextUtils.isEmpty(getAmazonAdId(this.mRef.get())) && TextUtils.isEmpty(getFromPlayServiceClient(this.mRef.get()))) {
                getFromBindService(this.mRef.get());
            }
        }
    }

    private String getAmazonAdId(Context context) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            if (!(Settings.Secure.getInt(contentResolver, "limit_ad_tracking") != 0)) {
                String string = Settings.Secure.getString(contentResolver, "advertising_id");
                LogModule.i(TAG, "Amazon advertisingID: " + string);
                doWhenGaidReady(context, string);
                return string;
            }
        } catch (Settings.SettingNotFoundException unused) {
        }
        return null;
    }

    private static final class AdvertisingConnection implements ServiceConnection {
        private final LinkedBlockingQueue<IBinder> queue;
        boolean retrieved;

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }

        private AdvertisingConnection() {
            this.retrieved = false;
            this.queue = new LinkedBlockingQueue<>(1);
        }

        /* synthetic */ AdvertisingConnection(AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                this.queue.put(iBinder);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }

        IBinder getBinder() throws InterruptedException {
            if (this.retrieved) {
                throw new IllegalStateException();
            }
            this.retrieved = true;
            return this.queue.take();
        }
    }

    private static final class AdvertisingInterface implements IInterface {
        private IBinder binder;

        AdvertisingInterface(IBinder iBinder) {
            this.binder = iBinder;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.binder;
        }

        public void getId(Context context) {
            Parcel parcelObtain = Parcel.obtain();
            Parcel parcelObtain2 = Parcel.obtain();
            boolean z = false;
            String string = null;
            try {
                parcelObtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                this.binder.transact(1, parcelObtain, parcelObtain2, 0);
                parcelObtain2.readException();
                string = parcelObtain2.readString();
                parcelObtain.writeInt(0);
                this.binder.transact(2, parcelObtain, parcelObtain2, 0);
                parcelObtain2.readException();
                if (parcelObtain2.readInt() != 0) {
                    z = true;
                }
            } finally {
                try {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    LogModule.i(GaidUtils.TAG, "id:" + string + " / limit:" + z);
                    if (!TextUtils.isEmpty(string)) {
                    }
                    GaidUtils.cacheGaid("");
                } catch (Throwable th) {
                }
            }
            parcelObtain2.recycle();
            parcelObtain.recycle();
            LogModule.i(GaidUtils.TAG, "id:" + string + " / limit:" + z);
            if (!TextUtils.isEmpty(string) || z) {
                GaidUtils.cacheGaid("");
            } else {
                GaidUtils.doWhenGaidReady(context, string);
            }
        }
    }

    public static synchronized String getRandomUDID(Context context) {
        String string;
        try {
            Random random = new Random();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                int iNextInt = random.nextInt(2);
                if (iNextInt == 0) {
                    sb.append((char) Math.round((Math.random() * 25.0d) + 97.0d));
                } else if (iNextInt == 1) {
                    sb.append(new Random().nextInt(10));
                }
            }
            string = sb.toString();
            cacheFakeGaid(context, string);
            LogModule.i(TAG, "create Fake udid: " + string);
        } catch (Exception e) {
            LogModule.i(TAG, "create Fake udid throw Exception: " + e);
            return UUID.randomUUID().toString().replace("-", "");
        }
        return string;
    }
}