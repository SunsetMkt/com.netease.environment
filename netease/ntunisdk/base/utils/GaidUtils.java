package com.netease.ntunisdk.base.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.text.TextUtils;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes3.dex */
public class GaidUtils implements Runnable {
    private static final String KEY_GAID_CACHED = "gaid_cached";
    private static final String TAG = "GaidUtils";
    private static HashSet<Callback> sCallbackSet = new HashSet<>();
    private static boolean sHasCheckedGms = false;
    private static boolean sHasInstalledGms = false;
    private static SharedPreferences sharedPreferences;
    private WeakReference<Context> mRef;

    public interface Callback {
        void done(String str);
    }

    private GaidUtils(Context context) {
        this.mRef = new WeakReference<>(context);
    }

    public static void requestGaid(Context context) {
        Thread thread = new Thread(new GaidUtils(context));
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(TAG, 0);
        }
        thread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void cacheGaid(String str) {
        SharedPreferences sharedPreferences2 = sharedPreferences;
        if (sharedPreferences2 != null) {
            SharedPreferences.Editor editorEdit = sharedPreferences2.edit();
            editorEdit.putString(KEY_GAID_CACHED, str);
            editorEdit.commit();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void doWhenGaidReady(Context context, final String str) {
        cacheGaid(str);
        Iterator<Callback> it = sCallbackSet.iterator();
        while (it.hasNext()) {
            Callback next = it.next();
            if (next != null) {
                next.done(str);
            }
        }
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.utils.GaidUtils.1
                @Override // java.lang.Runnable
                public final void run() {
                    if (SdkMgr.getInst() != null) {
                        ((SdkBase) SdkMgr.getInst()).extendFuncCall("{\"methodId\": \"gaidCallback\", \"gaid\": \"" + str + "\"}");
                    }
                }
            });
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

    public static String getCachedGaid(Callback callback) {
        if (callback != null) {
            sCallbackSet.add(callback);
        }
        return getCachedGaid();
    }

    public static boolean hasInstalledGooglePlayServices(Context context) {
        if (!sHasCheckedGms) {
            try {
                PackageManager packageManager = context.getPackageManager();
                UniSdkUtils.i(TAG, "gms pkgInfo: ".concat(String.valueOf(packageManager.getPackageInfo("com.google.android.gms", 0))));
                UniSdkUtils.i(TAG, "gms appInfo: ".concat(String.valueOf(packageManager.getApplicationInfo("com.google.android.gms", 0))));
                UniSdkUtils.i(TAG, "store pkgInfo: ".concat(String.valueOf(packageManager.getPackageInfo("com.android.vending", 0))));
                UniSdkUtils.i(TAG, "store appInfo: ".concat(String.valueOf(packageManager.getApplicationInfo("com.android.vending", 0))));
                sHasInstalledGms = true;
            } catch (Throwable unused) {
                UniSdkUtils.w(TAG, "Google Play services is missing.");
            }
            sHasCheckedGms = true;
        }
        return sHasInstalledGms;
    }

    private String getFromPlayServiceClient(Context context) {
        String strEncode;
        Object objInvoke;
        Class<?> cls;
        boolean zBooleanValue;
        try {
            objInvoke = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient").getDeclaredMethod("getAdvertisingIdInfo", Context.class).invoke(null, context);
            cls = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient$Info");
            strEncode = (String) cls.getDeclaredMethod("getId", new Class[0]).invoke(objInvoke, new Object[0]);
        } catch (Throwable th) {
            th = th;
            strEncode = null;
        }
        try {
            strEncode = URLEncoder.encode(strEncode, "UTF-8");
            zBooleanValue = ((Boolean) cls.getDeclaredMethod("isLimitAdTrackingEnabled", new Class[0]).invoke(objInvoke, new Object[0])).booleanValue();
            UniSdkUtils.i(TAG, "id:" + strEncode + " / limit:" + zBooleanValue);
        } catch (Throwable th2) {
            th = th2;
            if (getCachedGaid() == null) {
                cacheGaid(hasInstalledGooglePlayServices(context) ? null : "");
            }
            UniSdkUtils.i(TAG, th.getMessage());
            return strEncode;
        }
        if (zBooleanValue) {
            UniSdkUtils.w(TAG, "gaid limited");
            return "";
        }
        if (!TextUtils.isEmpty(strEncode)) {
            doWhenGaidReady(context, strEncode);
        }
        return strEncode;
    }

    private void getFromBindService(Context context) {
        AdvertisingConnection advertisingConnection = new AdvertisingConnection();
        Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
        intent.setPackage("com.google.android.gms");
        try {
            if (context.bindService(intent, advertisingConnection, 1)) {
                try {
                    try {
                        new AdvertisingInterface(advertisingConnection.getBinder()).getId(context);
                    } finally {
                        context.unbindService(advertisingConnection);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.mRef.get() == null || !TextUtils.isEmpty(getFromPlayServiceClient(this.mRef.get()))) {
            return;
        }
        getFromBindService(this.mRef.get());
    }

    private static final class AdvertisingConnection implements ServiceConnection {
        private final LinkedBlockingQueue<IBinder> queue;
        boolean retrieved;

        @Override // android.content.ServiceConnection
        public final void onServiceDisconnected(ComponentName componentName) {
        }

        private AdvertisingConnection() {
            this.retrieved = false;
            this.queue = new LinkedBlockingQueue<>(1);
        }

        @Override // android.content.ServiceConnection
        public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                this.queue.put(iBinder);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }

        final IBinder getBinder() throws InterruptedException {
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
        public final IBinder asBinder() {
            return this.binder;
        }

        public final void getId(Context context) {
            Parcel parcelObtain = Parcel.obtain();
            Parcel parcelObtain2 = Parcel.obtain();
            String string = null;
            boolean z = false;
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
                    UniSdkUtils.i(GaidUtils.TAG, "id:" + string + " / limit:" + z);
                    if (!TextUtils.isEmpty(string)) {
                    }
                    GaidUtils.cacheGaid("");
                } catch (Throwable th) {
                }
            }
            parcelObtain2.recycle();
            parcelObtain.recycle();
            UniSdkUtils.i(GaidUtils.TAG, "id:" + string + " / limit:" + z);
            if (!TextUtils.isEmpty(string) || z) {
                GaidUtils.cacheGaid("");
            } else {
                GaidUtils.doWhenGaidReady(context, string);
            }
        }
    }
}