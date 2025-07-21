package com.netease.pushservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.push.utils.PushConstantsImpl;
import com.netease.push.utils.PushLog;
import com.netease.push.utils.PushSetting;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes4.dex */
public class PushService extends Service {
    private int clientCount = 0;
    private boolean initRes = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;
    private static final String TAG = "NGPush_" + PushService.class.getSimpleName();
    public static boolean pushServiceLive = false;
    public static int hasOnCreate = 0;

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    @Override // android.app.Service
    public void onCreate() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        super.onCreate();
        pushServiceLive = true;
        hasOnCreate = 1;
        PushLog.e(TAG, "hasOnCreate: " + hasOnCreate);
        PushLog.init(this);
        PushLog.e(TAG, "PushService onCreate, package: " + getPackageName() + ", this:" + this);
        this.clientCount = 0;
        this.phoneStateListener = new PhoneStateChangeListener();
        this.telephonyManager = (TelephonyManager) getSystemService("phone");
        boolean curUseNiepush = PushSetting.getCurUseNiepush(this, false);
        PushLog.i(TAG, "useNiepush: " + curUseNiepush);
        if (curUseNiepush) {
            this.initRes = PushServiceHelper.getInstance().init(this);
            registerConnectivityReceiver();
        }
    }

    @Override // android.app.Service
    public void onDestroy() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        pushServiceLive = false;
        unregisterConnectivityReceiver();
        stopSelf();
        PushLog.i(TAG, "onDestroy, this:" + this);
        super.onDestroy();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String stringExtra = intent.getStringExtra(IdCache.androidIdTag);
        String stringExtra2 = intent.getStringExtra(IdCache.transIdTag);
        Log.d(TAG, "onStartCommand -> androidId: " + stringExtra);
        Log.d(TAG, "onStartCommand -> transId: " + stringExtra2);
        if (!TextUtils.isEmpty(stringExtra)) {
            IdCache.androidId = stringExtra;
        }
        if (!TextUtils.isEmpty(stringExtra2)) {
            IdCache.transId = stringExtra2;
        }
        boolean curUseNiepush = PushSetting.getCurUseNiepush(this, false);
        String string = (intent == null || intent.getExtras() == null) ? "" : intent.getExtras().getString(PushConstantsImpl.SERVICE_START_TYPE);
        PushLog.d(TAG, "startType:" + string);
        PushLog.d(TAG, "hasOnCreate:" + hasOnCreate);
        PushLog.d(TAG, "intent:" + intent);
        if (PushConstantsImpl.SERVICE_START_TYPE_SELF.equals(string) || curUseNiepush) {
            if (hasOnCreate == 1) {
                if (!this.initRes) {
                    PushLog.d(TAG, "repeat PushServiceHelper.getInstance().init");
                    PushServiceHelper.getInstance().init(this);
                }
                PushLog.e(TAG, "onStartCommand");
                PushLog.d(TAG, "intent:" + intent);
                PushLog.d(TAG, "flags:" + i);
                PushLog.d(TAG, "startId:" + i2);
                PushLog.d(TAG, "package name:" + getApplicationContext().getPackageName());
                if (intent != null) {
                    PushLog.d(TAG, "processCommand:");
                    PushServiceHelper.getInstance().processCommand(this, intent);
                }
                hasOnCreate = 0;
            }
            return 2;
        }
        PushLog.e(TAG, "need not niepush");
        stopSelf();
        return 2;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "onUnbind");
        PushLog.d(TAG, "intent:" + intent);
        PushLog.d(TAG, "package:" + getPackageName());
        this.clientCount = this.clientCount + (-1);
        PushLog.d(TAG, "clientcount:" + this.clientCount);
        return super.onUnbind(intent);
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "onBind");
        PushLog.d(TAG, "intent:" + intent);
        PushLog.d(TAG, "package:" + getPackageName());
        this.clientCount = this.clientCount + 1;
        PushLog.d(TAG, "clientcount:" + this.clientCount);
        return null;
    }

    private void registerConnectivityReceiver() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "registerConnectivityReceiver");
        try {
            this.telephonyManager.listen(this.phoneStateListener, 64);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterConnectivityReceiver() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (this.phoneStateListener == null) {
            return;
        }
        PushLog.d(TAG, "unregisterConnectivityReceiver");
        try {
            this.telephonyManager.listen(this.phoneStateListener, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.e(TAG, "start...");
        registerConnectivityReceiver();
    }

    public void restart(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.e(TAG, PushConstantsImpl.SERVICE_METHOD_RESTART);
        PushLog.d(TAG, "packageName:" + str);
        PushLog.d(TAG, "this.getPackageName():" + getPackageName());
        if (str.equals(getPackageName())) {
            return;
        }
        Intent intentCreateMethodIntent = PushServiceHelper.createMethodIntent();
        intentCreateMethodIntent.setPackage(str);
        ((AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM)).set(3, SystemClock.elapsedRealtime() + 1000, PendingIntent.getBroadcast(this, 0, intentCreateMethodIntent, 268435456));
        stop();
    }

    public void stop() {
        new Handler().postDelayed(new Runnable() { // from class: com.netease.pushservice.PushService.1
            @Override // java.lang.Runnable
            public void run() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                PushLog.e(PushService.TAG, "stop..., this:" + this);
                try {
                    PushService.this.unregisterConnectivityReceiver();
                } catch (IllegalArgumentException unused) {
                }
                PushServiceHelper.getInstance().stop();
                PushService.this.stopSelf();
            }
        }, 1500L);
    }
}