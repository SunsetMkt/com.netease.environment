package com.netease.ntunisdk.modules.personalinfolist;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.bun.supplier.IdSupplier;
import com.netease.ntunisdk.modules.base.utils.LogModule;
import com.netease.rnccplayer.VideoView;
import com.xiaomi.gamecenter.sdk.statistics.OneTrackParams;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class HookManager {
    private static Context ctx;
    private static final String TAG = HookManager.class.getSimpleName();
    private static List<String> PROJECTION = new ArrayList(Arrays.asList("_id", "_data", "mime_type", "width", "height", "duration", "_size", "bucket_display_name", "_display_name", "bucket_id", "date_added", VideoView.EVENT_PROP_ORIENTATION, "_data", "_display_name", "_size", "date_added", "_data", "duration", "_display_name", "date_added"));

    private static String getSensorType(int i) {
        return (1 == i || 10 == i) ? "Accelerometer" : 9 == i ? "Gravity-Sensor" : (11 == i || 15 == i || 20 == i) ? "Rotation-Vector-Sensor" : (2 == i || 14 == i) ? "Magnetic-Field-Sensor" : (4 == i || 16 == i) ? "Gyroscope-Sensor" : "";
    }

    static void test() {
    }

    public static void init(Context context) {
        ctx = context;
    }

    public static String getOAID(IdSupplier idSupplier) {
        LogModule.i(TAG, "getOAID");
        return idSupplier.getOAID();
    }

    public static String getOAID(com.bun.miitmdid.interfaces.IdSupplier idSupplier) {
        LogModule.i(TAG, "getOAID");
        return idSupplier.getOAID();
    }

    public static String getMobileModel() {
        LogModule.i(TAG, "getMobileModel");
        return Build.MODEL;
    }

    public static String getMobileVersion() {
        LogModule.i(TAG, "getMobileVersion");
        return Build.VERSION.RELEASE;
    }

    public static String getMobileBrand() {
        LogModule.i(TAG, "getMobileBrand");
        return Build.BRAND;
    }

    public static String getSettingsString(ContentResolver contentResolver, String str) {
        LogModule.i(TAG, "getSettingsString");
        return Settings.Secure.getString(contentResolver, str);
    }

    public static String getMacAddress(WifiInfo wifiInfo) {
        LogModule.i(TAG, "getMacAddress");
        return wifiInfo.getMacAddress();
    }

    public static String getBSSID(WifiInfo wifiInfo) throws JSONException {
        LogModule.i(TAG, "getBSSID");
        sendWifiClientLog("getBSSID");
        return wifiInfo.getBSSID();
    }

    public static String getSSID(WifiInfo wifiInfo) throws JSONException {
        LogModule.i(TAG, "getSSID");
        sendWifiClientLog("getSSID");
        return wifiInfo.getSSID();
    }

    public static int getFrequency(WifiInfo wifiInfo) throws JSONException {
        LogModule.i(TAG, "getFrequency");
        sendWifiClientLog("getFrequency");
        if (Build.VERSION.SDK_INT >= 21) {
            return wifiInfo.getFrequency();
        }
        return -1;
    }

    public static int getRssi(WifiInfo wifiInfo) throws JSONException {
        LogModule.i(TAG, "getRssi");
        sendWifiClientLog("getRssi");
        return wifiInfo.getRssi();
    }

    public static int getIpAddress(WifiInfo wifiInfo) {
        LogModule.i(TAG, "getIpAddress");
        return wifiInfo.getIpAddress();
    }

    public static List<ScanResult> getScanResults(WifiManager wifiManager) throws JSONException {
        LogModule.i(TAG, "getScanResults");
        sendWifiClientLog("getScanResults");
        return wifiManager.getScanResults();
    }

    public static WifiInfo getConnectionInfo(WifiManager wifiManager) {
        LogModule.i(TAG, "getConnectionInfo");
        sendWifiClientLog("getConnectionInfo");
        return wifiManager.getConnectionInfo();
    }

    private static void sendWifiClientLog(String str) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("name", "wifi");
            jSONObject.putOpt("spec", new JSONObject());
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.putOpt("event", jSONObject);
            jSONObject2.put(OneTrackParams.XMSdkParams.STEP, str);
            jSONObject2.put("func", str);
            jSONObject2.put("upload_type", "hook");
            ClientLogReporter.report(jSONObject2.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDeviceId(TelephonyManager telephonyManager) {
        LogModule.i(TAG, "getDeviceId");
        return telephonyManager.getDeviceId();
    }

    public static String getSubscriberId(TelephonyManager telephonyManager) {
        LogModule.i(TAG, "getSubscriberId");
        return telephonyManager.getSubscriberId();
    }

    public static String getSystemTimeZone(TimeZone timeZone) {
        LogModule.i(TAG, "getSystemTimeZone");
        return timeZone.getID();
    }

    public static int getBatteryIntProperty(BatteryManager batteryManager, int i) {
        LogModule.i(TAG, "getBatteryIntProperty");
        if (Build.VERSION.SDK_INT >= 21) {
            return batteryManager.getIntProperty(i);
        }
        return Integer.MIN_VALUE;
    }

    public static boolean registerSensorListener(SensorManager sensorManager, SensorEventListener sensorEventListener, Sensor sensor, int i) throws JSONException {
        LogModule.i(TAG, "registerSensorListener");
        sendSensorClientLog(sensor);
        return sensorManager.registerListener(sensorEventListener, sensor, i);
    }

    public static boolean registerSensorListener(SensorManager sensorManager, SensorEventListener sensorEventListener, Sensor sensor, int i, Handler handler) throws JSONException {
        LogModule.i(TAG, "registerSensorListener");
        sendSensorClientLog(sensor);
        return sensorManager.registerListener(sensorEventListener, sensor, i, handler);
    }

    public static boolean registerSensorListener(SensorManager sensorManager, SensorEventListener sensorEventListener, Sensor sensor, int i, int i2) throws JSONException {
        LogModule.i(TAG, "registerSensorListener");
        sendSensorClientLog(sensor);
        if (Build.VERSION.SDK_INT >= 19) {
            return sensorManager.registerListener(sensorEventListener, sensor, i, i2);
        }
        return false;
    }

    public static boolean registerSensorListener(SensorManager sensorManager, SensorEventListener sensorEventListener, Sensor sensor, int i, int i2, Handler handler) throws JSONException {
        LogModule.i(TAG, "registerSensorListener");
        sendSensorClientLog(sensor);
        if (Build.VERSION.SDK_INT >= 19) {
            return sensorManager.registerListener(sensorEventListener, sensor, i, i2, handler);
        }
        return false;
    }

    private static void sendSensorClientLog(Sensor sensor) throws JSONException {
        if (sensor != null) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("name", "sensor");
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("type", getSensorType(sensor.getType()));
                jSONObject.putOpt("spec", jSONObject2);
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.putOpt("event", jSONObject);
                jSONObject3.put(OneTrackParams.XMSdkParams.STEP, "registerSensorListener");
                jSONObject3.put("func", "registerSensorListener");
                jSONObject3.put("upload_type", "hook");
                ClientLogReporter.report(jSONObject3.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void startActivity(Activity activity, Intent intent) {
        LogModule.i(TAG, "startActivity");
        handleStartActivityClientLog(intent, "startActivity");
        activity.startActivity(intent);
    }

    public static void startActivity(Context context, Intent intent) {
        LogModule.i(TAG, "startActivity");
        handleStartActivityClientLog(intent, "startActivity");
        context.startActivity(intent);
    }

    public static void startActivity(Activity activity, Intent intent, Bundle bundle) throws JSONException {
        LogModule.i(TAG, "startActivity");
        handleStartActivityClientLog(intent, "startActivity");
        activity.startActivity(intent, bundle);
    }

    public static void startActivity(Context context, Intent intent, Bundle bundle) throws JSONException {
        LogModule.i(TAG, "startActivity");
        handleStartActivityClientLog(intent, "startActivity");
        context.startActivity(intent, bundle);
    }

    public static void startActivityForResult(Activity activity, Intent intent, int i) {
        LogModule.i(TAG, "startActivityForResult");
        handleStartActivityClientLog(intent, "startActivityForResult");
        activity.startActivityForResult(intent, i);
    }

    public static void startActivityForResult(Activity activity, Intent intent, int i, Bundle bundle) throws JSONException {
        LogModule.i(TAG, "startActivityForResult");
        handleStartActivityClientLog(intent, "startActivityForResult");
        activity.startActivityForResult(intent, i, bundle);
    }

    private static boolean containsMIME(String[] strArr) {
        if (strArr != null) {
            for (String str : strArr) {
                if (str.contains("image") || str.contains("video")) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void handleStartActivityClientLog(Intent intent, String str) throws JSONException {
        if (intent != null) {
            try {
                String action = intent.getAction();
                String type = intent.getType();
                String[] stringArrayExtra = intent.getStringArrayExtra("android.intent.extra.MIME_TYPES");
                if (!"android.intent.action.PICK".equalsIgnoreCase(action) || TextUtils.isEmpty(type) || !type.contains("image")) {
                    if (!"android.intent.action.GET_CONTENT".equals(action)) {
                        return;
                    }
                    if ((TextUtils.isEmpty(type) || !type.contains("image")) && !containsMIME(stringArrayExtra)) {
                        return;
                    }
                }
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("name", "photos");
                jSONObject.putOpt("spec", new JSONObject());
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.putOpt("event", jSONObject);
                jSONObject2.put(OneTrackParams.XMSdkParams.STEP, str);
                jSONObject2.put("func", str);
                jSONObject2.put("upload_type", "hook");
                ClientLogReporter.report(jSONObject2.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Cursor queryContentResolver(ContentResolver contentResolver, Uri uri, String[] strArr, Bundle bundle, CancellationSignal cancellationSignal) throws JSONException {
        LogModule.i(TAG, "queryContentResolver");
        handleQueryContentResolverClientLog(uri, strArr, null);
        if (Build.VERSION.SDK_INT >= 26) {
            return contentResolver.query(uri, strArr, bundle, cancellationSignal);
        }
        return null;
    }

    public static Cursor queryContentResolver(ContentResolver contentResolver, Uri uri, String[] strArr, String str, String[] strArr2, String str2) throws JSONException {
        LogModule.i(TAG, "queryContentResolver");
        handleQueryContentResolverClientLog(uri, strArr, strArr2);
        return contentResolver.query(uri, strArr, str, strArr2, str2);
    }

    public static Cursor queryContentResolver(ContentResolver contentResolver, Uri uri, String[] strArr, String str, String[] strArr2, String str2, CancellationSignal cancellationSignal) throws JSONException {
        LogModule.i(TAG, "queryContentResolver");
        handleQueryContentResolverClientLog(uri, strArr, strArr2);
        return contentResolver.query(uri, strArr, str, strArr2, str2, cancellationSignal);
    }

    /* JADX WARN: Removed duplicated region for block: B:81:0x0053 A[Catch: Exception -> 0x00ab, TryCatch #0 {Exception -> 0x00ab, blocks: (B:58:0x0004, B:61:0x0012, B:64:0x001b, B:66:0x0027, B:68:0x002f, B:93:0x0074, B:73:0x003c, B:75:0x0040, B:81:0x0053, B:83:0x0056, B:85:0x0060, B:88:0x0069, B:78:0x004d), top: B:98:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x006e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void handleQueryContentResolverClientLog(android.net.Uri r8, java.lang.String[] r9, java.lang.String[] r10) throws org.json.JSONException {
        /*
            java.lang.String r0 = "queryContentResolver"
            if (r8 == 0) goto Laf
            java.lang.String r1 = ""
            android.net.Uri r2 = android.provider.ContactsContract.Contacts.CONTENT_URI     // Catch: java.lang.Exception -> Lab
            boolean r2 = r2.equals(r8)     // Catch: java.lang.Exception -> Lab
            java.lang.String r3 = "photos"
            r4 = 0
            r5 = 1
            if (r2 != 0) goto L70
            android.net.Uri r2 = android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI     // Catch: java.lang.Exception -> Lab
            boolean r2 = r2.equals(r8)     // Catch: java.lang.Exception -> Lab
            if (r2 == 0) goto L1b
            goto L70
        L1b:
            java.lang.String r2 = "external"
            android.net.Uri r2 = android.provider.MediaStore.Files.getContentUri(r2)     // Catch: java.lang.Exception -> Lab
            boolean r2 = r2.equals(r8)     // Catch: java.lang.Exception -> Lab
            if (r2 != 0) goto L3a
            android.net.Uri r2 = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI     // Catch: java.lang.Exception -> Lab
            boolean r2 = r2.equals(r8)     // Catch: java.lang.Exception -> Lab
            if (r2 != 0) goto L3a
            android.net.Uri r2 = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI     // Catch: java.lang.Exception -> Lab
            boolean r8 = r2.equals(r8)     // Catch: java.lang.Exception -> Lab
            if (r8 == 0) goto L38
            goto L3a
        L38:
            r5 = 0
            goto L72
        L3a:
            if (r9 == 0) goto L50
            int r8 = r9.length     // Catch: java.lang.Exception -> Lab
            r2 = 0
        L3e:
            if (r2 >= r8) goto L50
            r6 = r9[r2]     // Catch: java.lang.Exception -> Lab
            java.util.List<java.lang.String> r7 = com.netease.ntunisdk.modules.personalinfolist.HookManager.PROJECTION     // Catch: java.lang.Exception -> Lab
            boolean r6 = r7.contains(r6)     // Catch: java.lang.Exception -> Lab
            if (r6 == 0) goto L4d
            r1 = r3
            r8 = 1
            goto L51
        L4d:
            int r2 = r2 + 1
            goto L3e
        L50:
            r8 = 0
        L51:
            if (r10 == 0) goto L6e
            int r9 = r10.length     // Catch: java.lang.Exception -> Lab
        L54:
            if (r4 >= r9) goto L6e
            r2 = r10[r4]     // Catch: java.lang.Exception -> Lab
            java.lang.String r6 = "image"
            boolean r6 = r2.contains(r6)     // Catch: java.lang.Exception -> Lab
            if (r6 != 0) goto L6c
            java.lang.String r6 = "video"
            boolean r2 = r2.contains(r6)     // Catch: java.lang.Exception -> Lab
            if (r2 == 0) goto L69
            goto L6c
        L69:
            int r4 = r4 + 1
            goto L54
        L6c:
            r1 = r3
            goto L72
        L6e:
            r5 = r8
            goto L72
        L70:
            java.lang.String r1 = "contacts"
        L72:
            if (r5 == 0) goto Laf
            org.json.JSONObject r8 = new org.json.JSONObject     // Catch: java.lang.Exception -> Lab
            r8.<init>()     // Catch: java.lang.Exception -> Lab
            java.lang.String r9 = "name"
            r8.put(r9, r1)     // Catch: java.lang.Exception -> Lab
            java.lang.String r9 = "spec"
            org.json.JSONObject r10 = new org.json.JSONObject     // Catch: java.lang.Exception -> Lab
            r10.<init>()     // Catch: java.lang.Exception -> Lab
            r8.putOpt(r9, r10)     // Catch: java.lang.Exception -> Lab
            org.json.JSONObject r9 = new org.json.JSONObject     // Catch: java.lang.Exception -> Lab
            r9.<init>()     // Catch: java.lang.Exception -> Lab
            java.lang.String r10 = "event"
            r9.putOpt(r10, r8)     // Catch: java.lang.Exception -> Lab
            java.lang.String r8 = "step"
            r9.put(r8, r0)     // Catch: java.lang.Exception -> Lab
            java.lang.String r8 = "func"
            r9.put(r8, r0)     // Catch: java.lang.Exception -> Lab
            java.lang.String r8 = "upload_type"
            java.lang.String r10 = "hook"
            r9.put(r8, r10)     // Catch: java.lang.Exception -> Lab
            java.lang.String r8 = r9.toString()     // Catch: java.lang.Exception -> Lab
            com.netease.ntunisdk.modules.personalinfolist.ClientLogReporter.report(r8)     // Catch: java.lang.Exception -> Lab
            goto Laf
        Lab:
            r8 = move-exception
            r8.printStackTrace()
        Laf:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.modules.personalinfolist.HookManager.handleQueryContentResolverClientLog(android.net.Uri, java.lang.String[], java.lang.String[]):void");
    }

    public static CharSequence getClipDataText(ClipData.Item item) throws JSONException {
        LogModule.i(TAG, "getClipDataText");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("name", "clipboard");
            jSONObject.putOpt("spec", new JSONObject());
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.putOpt("event", jSONObject);
            jSONObject2.put(OneTrackParams.XMSdkParams.STEP, "getClipDataText");
            jSONObject2.put("func", "getClipDataText");
            jSONObject2.put("upload_type", "hook");
            ClientLogReporter.report(jSONObject2.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item.getText();
    }
}