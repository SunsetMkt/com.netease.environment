package com.netease.ntunisdk;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.modules.personalinfolist.HookManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
class WifiListUtil {
    private static final String DEVICE_JSON = "{\"methodId\":\"getWifiListJson\",\"reacquire\":true}";
    private static final String FALSE_MAC = "02:00:00:00:00:00";
    private static final String TAG = "WifiListUtil";
    private static Map<String, Unit> sCacheResults = new HashMap();
    private static int sGlobalIndex;

    WifiListUtil() {
    }

    static class Unit {
        int connected;
        int frequency;
        String mac;
        String ssid;
        int currentIndex = 1;
        List<Integer> levels = new LinkedList();

        Unit() {
        }

        void append(int i, int i2, String str, String str2, int i3) {
            if (this.currentIndex < i - 1) {
                for (int i4 = 0; i4 < (i - this.currentIndex) - 1; i4++) {
                    this.levels.add(-100);
                }
            }
            this.levels.add(Integer.valueOf(i2));
            this.currentIndex = i;
            this.ssid = str;
            this.mac = str2;
            this.frequency = i3;
        }
    }

    static void clear() {
        sGlobalIndex = 0;
        sCacheResults.clear();
    }

    static JSONArray getArray() throws JSONException {
        JSONArray jSONArray = new JSONArray();
        try {
            for (Map.Entry<String, Unit> entry : sCacheResults.entrySet()) {
                JSONArray jSONArray2 = new JSONArray();
                Unit value = entry.getValue();
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("ssid", value.ssid);
                jSONObject.put("bssid", value.mac);
                jSONObject.put("frequency", value.frequency);
                if (1 == value.connected) {
                    jSONObject.put("connected", 1);
                }
                Iterator<Integer> it = value.levels.iterator();
                while (it.hasNext()) {
                    jSONArray2.put(it.next());
                }
                jSONObject.put("rss", jSONArray2);
                jSONArray.put(jSONObject);
            }
        } catch (Exception unused) {
        }
        return jSONArray;
    }

    static int appendWifiList(Context context) {
        WifiManager wifiManager;
        Unit unit;
        UniSdkUtils.d(TAG, "appendWifiList");
        try {
            wifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (wifiManager == null) {
            return sGlobalIndex;
        }
        List<ScanResult> scanResults = wifiManager.getScanResults();
        sGlobalIndex++;
        if (scanResults != null && !scanResults.isEmpty()) {
            WifiInfo connectionInfo = HookManager.getConnectionInfo(wifiManager);
            for (ScanResult scanResult : scanResults) {
                if (sCacheResults.containsKey(scanResult.BSSID)) {
                    unit = sCacheResults.get(scanResult.BSSID);
                } else {
                    unit = new Unit();
                    sCacheResults.put(scanResult.BSSID, unit);
                }
                Unit unit2 = unit;
                unit2.connected = (connectionInfo != null && isValidBssid(connectionInfo.getBSSID()) && connectionInfo.getBSSID().equals(scanResult.BSSID)) ? 1 : 0;
                unit2.append(sGlobalIndex, scanResult.level, scanResult.SSID, scanResult.BSSID, scanResult.frequency);
            }
        }
        return sGlobalIndex;
    }

    private static boolean isValidBssid(String str) {
        return (TextUtils.isEmpty(str) || FALSE_MAC.equals(str)) ? false : true;
    }

    private static JSONArray acquireSingleWifiList(Context context) throws JSONException {
        List<ScanResult> scanResults;
        UniSdkUtils.d(TAG, "acquireSingleWifiList");
        JSONArray jSONArray = new JSONArray();
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi");
            if (wifiManager != null && (scanResults = wifiManager.getScanResults()) != null && !scanResults.isEmpty()) {
                WifiInfo connectionInfo = HookManager.getConnectionInfo(wifiManager);
                for (ScanResult scanResult : scanResults) {
                    JSONObject jSONObject = new JSONObject();
                    try {
                        jSONObject.put("ssid", scanResult.SSID);
                        jSONObject.put("bssid", scanResult.BSSID);
                        jSONObject.put("frequency", scanResult.frequency);
                        if (connectionInfo != null && isValidBssid(connectionInfo.getBSSID()) && connectionInfo.getBSSID().equals(scanResult.BSSID)) {
                            jSONObject.put("connected", 1);
                        }
                        JSONArray jSONArray2 = new JSONArray();
                        jSONArray2.put(scanResult.level);
                        jSONObject.put("rss", jSONArray2);
                        jSONArray.put(jSONObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return jSONArray;
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x0097  */
    /* JADX WARN: Removed duplicated region for block: B:47:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static org.json.JSONArray acquireSingleWifiList2(android.content.Context r8) throws org.json.JSONException {
        /*
            java.lang.String r0 = "connected"
            java.lang.String r1 = "rss"
            com.netease.ntunisdk.modules.api.ModulesManager r2 = com.netease.ntunisdk.modules.api.ModulesManager.getInst()     // Catch: java.lang.Throwable -> L9d
            r2.init(r8)     // Catch: java.lang.Throwable -> L9d
            com.netease.ntunisdk.modules.api.ModulesManager r8 = com.netease.ntunisdk.modules.api.ModulesManager.getInst()
            java.lang.String r2 = "WifiListUtil"
            java.lang.String r3 = "deviceInfo"
            java.lang.String r4 = "{\"methodId\":\"getWifiListJson\",\"reacquire\":true}"
            java.lang.String r8 = r8.extendFunc(r2, r3, r4)
            r3 = 0
            org.json.JSONObject r4 = new org.json.JSONObject     // Catch: java.lang.Exception -> L91
            r4.<init>(r8)     // Catch: java.lang.Exception -> L91
            java.lang.String r5 = "respCode"
            r6 = -1
            int r5 = r4.optInt(r5, r6)     // Catch: java.lang.Exception -> L91
            if (r5 != 0) goto L7c
            java.lang.String r8 = "wifi"
            org.json.JSONArray r8 = r4.optJSONArray(r8)     // Catch: java.lang.Exception -> L91
            if (r8 == 0) goto L77
            r4 = 0
        L31:
            int r5 = r8.length()     // Catch: java.lang.Exception -> L79
            if (r4 == r5) goto L77
            org.json.JSONObject r5 = r8.optJSONObject(r4)     // Catch: java.lang.Exception -> L79
            int r6 = r5.optInt(r0)     // Catch: java.lang.Exception -> L79
            if (r6 != 0) goto L44
            r5.remove(r0)     // Catch: java.lang.Exception -> L79
        L44:
            java.lang.Object r6 = r5.opt(r1)     // Catch: java.lang.Exception -> L79
            boolean r7 = r6 instanceof org.json.JSONArray     // Catch: java.lang.Exception -> L79
            if (r7 != 0) goto L74
            boolean r7 = r6 instanceof java.lang.Number     // Catch: java.lang.Exception -> L79
            if (r7 == 0) goto L5f
            r5.remove(r1)     // Catch: java.lang.Exception -> L79
            org.json.JSONArray r7 = new org.json.JSONArray     // Catch: java.lang.Exception -> L79
            r7.<init>()     // Catch: java.lang.Exception -> L79
            r7.put(r6)     // Catch: java.lang.Exception -> L79
            r5.put(r1, r7)     // Catch: java.lang.Exception -> L79
            goto L74
        L5f:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L79
            r0.<init>()     // Catch: java.lang.Exception -> L79
            java.lang.String r1 = "invalid item: "
            r0.append(r1)     // Catch: java.lang.Exception -> L79
            r0.append(r5)     // Catch: java.lang.Exception -> L79
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Exception -> L79
            com.netease.ntunisdk.base.UniSdkUtils.e(r2, r0)     // Catch: java.lang.Exception -> L79
            goto L95
        L74:
            int r4 = r4 + 1
            goto L31
        L77:
            r3 = r8
            goto L95
        L79:
            r0 = move-exception
            r3 = r8
            goto L92
        L7c:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L91
            r0.<init>()     // Catch: java.lang.Exception -> L91
            java.lang.String r1 = "invalid result: "
            r0.append(r1)     // Catch: java.lang.Exception -> L91
            r0.append(r8)     // Catch: java.lang.Exception -> L91
            java.lang.String r8 = r0.toString()     // Catch: java.lang.Exception -> L91
            com.netease.ntunisdk.base.UniSdkUtils.e(r2, r8)     // Catch: java.lang.Exception -> L91
            goto L95
        L91:
            r0 = move-exception
        L92:
            r0.printStackTrace()
        L95:
            if (r3 != 0) goto L9c
            org.json.JSONArray r3 = new org.json.JSONArray
            r3.<init>()
        L9c:
            return r3
        L9d:
            r0 = move-exception
            r0.printStackTrace()
            org.json.JSONArray r8 = acquireSingleWifiList(r8)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.WifiListUtil.acquireSingleWifiList2(android.content.Context):org.json.JSONArray");
    }
}