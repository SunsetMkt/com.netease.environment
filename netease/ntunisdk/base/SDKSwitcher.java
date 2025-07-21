package com.netease.ntunisdk.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.netease.ntunisdk.base.function.k;
import com.netease.ntunisdk.base.utils.NetUtil;
import com.netease.ntunisdk.base.utils.WgetDoneCallback;
import com.netease.ntunisdk.unilogger.global.Const;
import com.xiaomi.onetrack.OneTrack;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class SDKSwitcher {
    private static SDKSwitcher c;

    /* renamed from: a */
    private HashMap<String, String> f1629a = new HashMap<>();
    private HashMap<String, Boolean> b = new HashMap<>();

    public interface ParseDoneCallback {
        void onResult(HashMap<String, Boolean> map);
    }

    private SDKSwitcher() {
        if (SdkMgr.getInst() != null) {
            String channel = SdkMgr.getInst().getChannel();
            String sDKVersion = SdkMgr.getInst().getSDKVersion(channel);
            String baseVersion = SdkMgr.getBaseVersion();
            UniSdkUtils.i("SDKSwitcher", "mOs=android, mChannel=" + channel + ", mChannelVersion=" + sDKVersion + ", mCommonVersion=" + baseVersion);
            this.f1629a.put("os", "android");
            this.f1629a.put("channel", channel);
            this.f1629a.put(Const.CONFIG_KEY.CHANNEL_VERSION, sDKVersion);
            this.f1629a.put("common_version", baseVersion);
        }
    }

    public static SDKSwitcher getInstance() {
        if (c == null) {
            c = new SDKSwitcher();
        }
        return c;
    }

    public HashMap<String, Boolean> getSDKSwitcherMap() {
        return this.b;
    }

    public void start(ParseDoneCallback parseDoneCallback, Context context) {
        UniSdkUtils.i("SDKSwitcher", "SDKSwitcher [start] start");
        if (this.f1629a.size() == 0) {
            UniSdkUtils.i("SDKSwitcher", "SDKSwitcher [start] param error");
        } else {
            a(context, parseDoneCallback, false);
            k.a();
        }
    }

    private static String a(boolean z) {
        String propStr;
        String propStr2;
        String string;
        if (!z) {
            UniSdkUtils.i("SDKSwitcher", "getUrl request project");
            propStr2 = SdkMgr.getInst() != null ? SdkMgr.getInst().getPropStr(ConstProp.SDK_SWITCHER_URL_PROJECT) : null;
            if (TextUtils.isEmpty(propStr2)) {
                UniSdkUtils.i("SDKSwitcher", "requestSwitchInfo4SDK use default url to request project sdk switch info");
                String propStr3 = SdkMgr.getInst().getPropStr(ConstProp.YY_GAMEID);
                if (TextUtils.isEmpty(propStr3)) {
                    propStr3 = SdkMgr.getInst().getPropStr("JF_GAMEID");
                }
                String propStr4 = SdkMgr.getInst().getPropStr(ConstProp.SDK_UNI_UPDATE_URL);
                if (TextUtils.isEmpty(propStr4)) {
                    string = SdkMgr.getInst().hasFeature("EB") ? ConstProp.SDK_SWITCHER_PROJECT_OVERSEA_URL : ConstProp.SDK_SWITCHER_PROJECT_URL;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(propStr4);
                    sb.append(propStr4.endsWith("/") ? "" : "/");
                    sb.append("feature2/");
                    string = sb.toString();
                }
                if (TextUtils.isEmpty(string)) {
                    UniSdkUtils.i("SDKSwitcher", "null or empty url, request switch info will not go on");
                    propStr2 = string;
                } else {
                    propStr = string + propStr3 + ".query.json";
                    propStr2 = propStr;
                }
            }
        } else {
            UniSdkUtils.i("SDKSwitcher", "getUrl request all");
            propStr = SdkMgr.getInst() != null ? SdkMgr.getInst().getPropStr(ConstProp.SDK_SWITCHER_URL) : null;
            if (TextUtils.isEmpty(propStr)) {
                propStr2 = SdkMgr.getInst() != null ? SdkMgr.getInst().getPropStr(ConstProp.SDK_UNI_UPDATE_URL) : null;
                if (!TextUtils.isEmpty(propStr2)) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(propStr2);
                    sb2.append(propStr2.endsWith("/") ? "" : "/");
                    sb2.append("feature/query.json");
                    propStr2 = sb2.toString();
                }
                if (TextUtils.isEmpty(propStr2)) {
                    UniSdkUtils.i("SDKSwitcher", "requestSwitchInfo4SDK use default url to request all switch info");
                    propStr = SdkMgr.getInst().hasFeature("EB") ? ConstProp.SDK_SWITCHER_DEFAULT_OVERSEA_URL : ConstProp.SDK_SWITCHER_DEFAULT_URL;
                    propStr2 = propStr;
                }
            } else {
                propStr2 = propStr;
            }
        }
        if (TextUtils.isEmpty(propStr2)) {
            UniSdkUtils.i("SDKSwitcher", "getUrl, null or empty url");
            return propStr2;
        }
        String str = propStr2 + "?gameid=" + SdkMgr.getInst().getPropStr("JF_GAMEID");
        UniSdkUtils.i("SDKSwitcher", "getUrl url=".concat(String.valueOf(str)));
        return str;
    }

    public void a(Context context, ParseDoneCallback parseDoneCallback, boolean z) {
        UniSdkUtils.i("SDKSwitcher", "requestSwitchInfo4SDK start requestAll=".concat(String.valueOf(z)));
        String strA = a(z);
        if (TextUtils.isEmpty(strA)) {
            UniSdkUtils.i("SDKSwitcher", "null or empty url, request switch info will not go on");
        } else {
            NetUtil.wget(strA, new WgetDoneCallback() { // from class: com.netease.ntunisdk.base.SDKSwitcher.1
                @Override // com.netease.ntunisdk.base.utils.WgetDoneCallback
                public void ProcessResult(String str) {
                }

                AnonymousClass1() {
                }
            }, new NetUtil.HttpURLConnectionListener() { // from class: com.netease.ntunisdk.base.SDKSwitcher.2

                /* renamed from: a */
                final /* synthetic */ Context f1630a;
                final /* synthetic */ boolean b;
                final /* synthetic */ ParseDoneCallback c;

                AnonymousClass2(Context context2, boolean z2, ParseDoneCallback parseDoneCallback2) {
                    context = context2;
                    z = z2;
                    parseDoneCallback = parseDoneCallback2;
                }

                @Override // com.netease.ntunisdk.base.utils.NetUtil.HttpURLConnectionListener
                public void preConnect(HttpURLConnection httpURLConnection) {
                    SDKSwitcher.a(context, httpURLConnection);
                }

                @Override // com.netease.ntunisdk.base.utils.NetUtil.HttpURLConnectionListener
                public void afterConnect(String str, HttpURLConnection httpURLConnection) throws IOException {
                    Context context2 = context;
                    if (context2 == null || httpURLConnection == null) {
                        return;
                    }
                    try {
                        SharedPreferences sharedPreferences = context2.getSharedPreferences("UniSDK", 0);
                        if (sharedPreferences == null) {
                            return;
                        }
                        int responseCode = httpURLConnection.getResponseCode();
                        UniSdkUtils.i("SDKSwitcher", "requestSwitchInfo4SDK connection  code=".concat(String.valueOf(responseCode)));
                        if (304 == responseCode) {
                            str = sharedPreferences.getString("switcher_sdk_request_result_cache", "");
                        }
                        if (304 == responseCode) {
                            UniSdkUtils.i("SDKSwitcher", "requestSwitchInfo4SDK get result from cache");
                        } else {
                            SDKSwitcher.a(sharedPreferences, httpURLConnection, str);
                        }
                        if (TextUtils.isEmpty(str) && !z) {
                            UniSdkUtils.i("SDKSwitcher", "need to request all SDKSwitcher info");
                            SDKSwitcher.this.a(context, parseDoneCallback, true);
                        } else {
                            SDKSwitcher.a(SDKSwitcher.this, str, parseDoneCallback);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        UniSdkUtils.w("SDKSwitcher", "requestSwitchInfo4SDK Exception=" + e.toString());
                    }
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SDKSwitcher$1 */
    class AnonymousClass1 implements WgetDoneCallback {
        @Override // com.netease.ntunisdk.base.utils.WgetDoneCallback
        public void ProcessResult(String str) {
        }

        AnonymousClass1() {
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SDKSwitcher$2 */
    class AnonymousClass2 implements NetUtil.HttpURLConnectionListener {

        /* renamed from: a */
        final /* synthetic */ Context f1630a;
        final /* synthetic */ boolean b;
        final /* synthetic */ ParseDoneCallback c;

        AnonymousClass2(Context context2, boolean z2, ParseDoneCallback parseDoneCallback2) {
            context = context2;
            z = z2;
            parseDoneCallback = parseDoneCallback2;
        }

        @Override // com.netease.ntunisdk.base.utils.NetUtil.HttpURLConnectionListener
        public void preConnect(HttpURLConnection httpURLConnection) {
            SDKSwitcher.a(context, httpURLConnection);
        }

        @Override // com.netease.ntunisdk.base.utils.NetUtil.HttpURLConnectionListener
        public void afterConnect(String str, HttpURLConnection httpURLConnection) throws IOException {
            Context context2 = context;
            if (context2 == null || httpURLConnection == null) {
                return;
            }
            try {
                SharedPreferences sharedPreferences = context2.getSharedPreferences("UniSDK", 0);
                if (sharedPreferences == null) {
                    return;
                }
                int responseCode = httpURLConnection.getResponseCode();
                UniSdkUtils.i("SDKSwitcher", "requestSwitchInfo4SDK connection  code=".concat(String.valueOf(responseCode)));
                if (304 == responseCode) {
                    str = sharedPreferences.getString("switcher_sdk_request_result_cache", "");
                }
                if (304 == responseCode) {
                    UniSdkUtils.i("SDKSwitcher", "requestSwitchInfo4SDK get result from cache");
                } else {
                    SDKSwitcher.a(sharedPreferences, httpURLConnection, str);
                }
                if (TextUtils.isEmpty(str) && !z) {
                    UniSdkUtils.i("SDKSwitcher", "need to request all SDKSwitcher info");
                    SDKSwitcher.this.a(context, parseDoneCallback, true);
                } else {
                    SDKSwitcher.a(SDKSwitcher.this, str, parseDoneCallback);
                }
            } catch (Exception e) {
                e.printStackTrace();
                UniSdkUtils.w("SDKSwitcher", "requestSwitchInfo4SDK Exception=" + e.toString());
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:148:0x013a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:150:0x013d A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean a(java.lang.String r10, org.json.JSONObject r11) throws org.json.JSONException, java.lang.NoSuchMethodException, java.lang.SecurityException {
        /*
            Method dump skipped, instructions count: 346
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.SDKSwitcher.a(java.lang.String, org.json.JSONObject):boolean");
    }

    private boolean b(String str, JSONObject jSONObject) throws NoSuchMethodException, SecurityException {
        if (jSONObject == null || jSONObject.length() <= 0) {
            UniSdkUtils.e("SDKSwitcher", "unitJson is error");
            return false;
        }
        Iterator<String> itKeys = jSONObject.keys();
        boolean zA = false;
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            if ("$or".equals(next)) {
                JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("$or");
                if (jSONArrayOptJSONArray != null && jSONArrayOptJSONArray.length() > 0) {
                    for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                        JSONObject jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(i);
                        Iterator<String> itKeys2 = jSONObjectOptJSONObject.keys();
                        while (itKeys2.hasNext()) {
                            String next2 = itKeys2.next();
                            JSONObject jSONObjectOptJSONObject2 = jSONObjectOptJSONObject.optJSONObject(next2);
                            UniSdkUtils.i("SDKSwitcher", "key=" + next2 + ", data=" + jSONObjectOptJSONObject2);
                            zA = a(str, next2, jSONObjectOptJSONObject2);
                            if (!zA) {
                                break;
                            }
                        }
                        if (zA) {
                            break;
                        }
                    }
                }
            } else {
                JSONObject jSONObjectOptJSONObject3 = jSONObject.optJSONObject(next);
                UniSdkUtils.i("SDKSwitcher", "key=" + next + ", data=" + jSONObjectOptJSONObject3);
                zA = a(str, next, jSONObjectOptJSONObject3);
            }
            if (!zA) {
                break;
            }
        }
        return zA;
    }

    private boolean a(String str, String str2, JSONObject jSONObject) throws NoSuchMethodException, SecurityException {
        String downloadSDKVersion;
        int iA;
        int iA2;
        if (TextUtils.isEmpty(str2)) {
            UniSdkUtils.e("SDKSwitcher", "key is error");
            return false;
        }
        if (jSONObject == null || jSONObject.length() <= 0) {
            UniSdkUtils.e("SDKSwitcher", "data is error");
            return false;
        }
        if ("pharos".equals(str) && "sdk_version".equals(str2)) {
            downloadSDKVersion = SDKPharos.getInstance().getPharosSDKVersion();
            UniSdkUtils.i("SDKSwitcher", "sdk=" + str + ", sdk_version=" + downloadSDKVersion);
        } else {
            downloadSDKVersion = null;
        }
        if (OneTrack.Event.DOWNLOAD.equals(str) && "sdk_version".equals(str2)) {
            downloadSDKVersion = SdkMgr.getDLInst().getDownloadSDKVersion();
            UniSdkUtils.i("SDKSwitcher", "sdk=" + str + ", sdk_version=" + downloadSDKVersion);
        }
        if (this.f1629a.containsKey(str2)) {
            downloadSDKVersion = this.f1629a.get(str2);
        }
        Iterator<String> itKeys = jSONObject.keys();
        while (true) {
            boolean z = false;
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                if (jSONObject.toString().contains("$gte") && jSONObject.toString().contains("$lte")) {
                    String strOptString = jSONObject.optString("$gte");
                    String strOptString2 = jSONObject.optString("$lte");
                    if (!TextUtils.isEmpty(downloadSDKVersion)) {
                        int iA3 = a(downloadSDKVersion, strOptString);
                        if (iA3 == 0 || iA3 == 1) {
                            z = true;
                        }
                        if (z) {
                            int iA4 = a(downloadSDKVersion, strOptString2);
                            if (iA4 != 0 && iA4 != -1) {
                                break;
                            }
                            z = true;
                            break;
                            break;
                        }
                        continue;
                    } else {
                        continue;
                    }
                } else if ("$or".equals(next)) {
                    JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("$or");
                    if (jSONArrayOptJSONArray != null && jSONArrayOptJSONArray.length() > 0) {
                        for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                            String strOptString3 = jSONArrayOptJSONArray.optString(i);
                            if (!TextUtils.isEmpty(downloadSDKVersion) && downloadSDKVersion.equals(strOptString3)) {
                                z = true;
                                break;
                            }
                        }
                    }
                } else if ("$eq".equals(next)) {
                    String strOptString4 = jSONObject.optString("$eq");
                    if (!TextUtils.isEmpty(downloadSDKVersion) && downloadSDKVersion.equals(strOptString4)) {
                        z = true;
                        break;
                        break;
                    }
                } else if ("$in".equals(next)) {
                    JSONArray jSONArrayOptJSONArray2 = jSONObject.optJSONArray("$in");
                    if (jSONArrayOptJSONArray2 != null && jSONArrayOptJSONArray2.length() > 0) {
                        for (int i2 = 0; i2 < jSONArrayOptJSONArray2.length(); i2++) {
                            String strOptString5 = jSONArrayOptJSONArray2.optString(i2);
                            if (!TextUtils.isEmpty(downloadSDKVersion) && downloadSDKVersion.equals(strOptString5)) {
                                z = true;
                                break;
                                break;
                            }
                        }
                    }
                } else if ("$gte".equals(next)) {
                    String strOptString6 = jSONObject.optString("$gte");
                    if (!TextUtils.isEmpty(downloadSDKVersion) && ((iA = a(downloadSDKVersion, strOptString6)) == 0 || iA == 1)) {
                        z = true;
                        break;
                        break;
                    }
                } else if ("$lte".equals(next)) {
                    String strOptString7 = jSONObject.optString("$lte");
                    if (!TextUtils.isEmpty(downloadSDKVersion) && ((iA2 = a(downloadSDKVersion, strOptString7)) == 0 || iA2 == -1)) {
                        z = true;
                        break;
                        break;
                    }
                }
            }
            UniSdkUtils.i("SDKSwitcher", "key=" + str2 + ", data=" + jSONObject + ", result=" + z);
            return z;
        }
    }

    private static int a(String str, String str2) {
        int i = -2;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            UniSdkUtils.e("SDKSwitcher", "compareTo param is error");
            return -2;
        }
        String[] strArrSplit = str.split("\\.|\\(|\\)");
        String[] strArrSplit2 = str2.split("\\.|\\(|\\)");
        int iMin = (strArrSplit == null || strArrSplit.length <= 0 || strArrSplit2 == null || strArrSplit2.length <= 0) ? 0 : Math.min(strArrSplit.length, strArrSplit2.length);
        if (iMin != 0) {
            int i2 = 0;
            while (true) {
                if (i2 >= iMin) {
                    break;
                }
                int length = strArrSplit[i2].length() - strArrSplit2[i2].length();
                UniSdkUtils.i("SDKSwitcher", "diff =".concat(String.valueOf(length)));
                if (length == 0) {
                    if (strArrSplit[i2].compareTo(strArrSplit2[i2]) > 0) {
                        i = 1;
                        break;
                    }
                    if (strArrSplit[i2].compareTo(strArrSplit2[i2]) < 0) {
                        i = -1;
                        break;
                    }
                    if (strArrSplit[i2].compareTo(strArrSplit2[i2]) == 0) {
                        i = 0;
                    }
                    if (i2 == iMin - 1) {
                        if (strArrSplit.length - strArrSplit2.length > 0) {
                            i = 1;
                        } else {
                            i = strArrSplit.length - strArrSplit2.length < 0 ? -1 : 0;
                        }
                    }
                    i2++;
                } else {
                    if (length != 0) {
                        i = length;
                        break;
                    }
                    i2++;
                }
            }
        }
        UniSdkUtils.i("SDKSwitcher", "compareTo version1 =" + str + ", version2 =" + str2 + ", result=" + i);
        return i;
    }

    static /* synthetic */ void a(Context context, HttpURLConnection httpURLConnection) {
        if (context == null || httpURLConnection == null) {
            return;
        }
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("UniSDK", 0);
            if (sharedPreferences == null) {
                return;
            }
            String string = sharedPreferences.getString("etag", "");
            if (!TextUtils.isEmpty(string)) {
                UniSdkUtils.i("SDKSwitcher", "setRequestProperty set If-None-Match, etag=".concat(String.valueOf(string)));
                httpURLConnection.setRequestProperty("If-None-Match", string);
            }
            String string2 = sharedPreferences.getString("last_modified", "");
            if (TextUtils.isEmpty(string2)) {
                return;
            }
            UniSdkUtils.i("SDKSwitcher", "setRequestProperty set If-Modified-Since, lastModified=".concat(String.valueOf(string2)));
            httpURLConnection.setRequestProperty(com.netease.ntunisdk.external.protocol.Const.IF_MODIFIED_SINCE, string2);
        } catch (Exception e) {
            e.printStackTrace();
            UniSdkUtils.w("SDKSwitcher", "setRequestProperty Exception=" + e.toString());
        }
    }

    static /* synthetic */ void a(SharedPreferences sharedPreferences, HttpURLConnection httpURLConnection, String str) throws Exception {
        String headerField = httpURLConnection.getHeaderField(com.netease.ntunisdk.external.protocol.Const.ETAG);
        UniSdkUtils.i("SDKSwitcher", "cacheResponsePropertyAndRequestResult connection get ETag from server, etag=".concat(String.valueOf(headerField)));
        if (!TextUtils.isEmpty(headerField)) {
            sharedPreferences.edit().putString("etag", headerField).commit();
        }
        String headerField2 = httpURLConnection.getHeaderField(com.netease.ntunisdk.external.protocol.Const.LAST_MODIFIED);
        UniSdkUtils.i("SDKSwitcher", "cacheResponsePropertyAndRequestResult connection get Last-Modified from server, lastModified=".concat(String.valueOf(headerField2)));
        if (!TextUtils.isEmpty(headerField2)) {
            sharedPreferences.edit().putString("last_modified", headerField2).commit();
        }
        if (TextUtils.isEmpty(str)) {
            return;
        }
        sharedPreferences.edit().putString("switcher_sdk_request_result_cache", str).commit();
    }

    static /* synthetic */ void a(SDKSwitcher sDKSwitcher, String str, ParseDoneCallback parseDoneCallback) {
        UniSdkUtils.i("SDKSwitcher", "dont need to request all SDKSwitcher info");
        UniSdkUtils.i("SDKSwitcher", "requestSwitchInfo4SDK request result info = ".concat(String.valueOf(str)));
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.length() > 0) {
                    Iterator<String> itKeys = jSONObject.keys();
                    while (itKeys.hasNext()) {
                        String next = itKeys.next();
                        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject(next);
                        if (jSONObjectOptJSONObject != null && jSONObjectOptJSONObject.length() > 0) {
                            UniSdkUtils.i("SDKSwitcher", "key = " + next + ", temp = " + jSONObjectOptJSONObject.toString());
                            if (!TextUtils.isEmpty(next) && jSONObjectOptJSONObject != null) {
                                String propStr = !"pharos".equals(next) ? SdkMgr.getInst().getPropStr(ConstProp.YY_GAMEID) : null;
                                if (TextUtils.isEmpty(propStr)) {
                                    propStr = SdkMgr.getInst().getPropStr("JF_GAMEID");
                                }
                                sDKSwitcher.f1629a.put("gameid", propStr);
                                UniSdkUtils.i("SDKSwitcher", "requestSwitchInfo4SDK param mInfo=" + sDKSwitcher.f1629a.toString());
                                sDKSwitcher.b.put(next, Boolean.valueOf(sDKSwitcher.a(next, jSONObjectOptJSONObject)));
                            } else {
                                UniSdkUtils.w("SDKSwitcher", "requestSwitchInfo4SDK param error1");
                            }
                        } else {
                            UniSdkUtils.w("SDKSwitcher", "requestSwitchInfo4SDK param error2");
                        }
                    }
                }
            } catch (JSONException e) {
                UniSdkUtils.w("SDKSwitcher", "requestSwitchInfo4SDK JSONException =".concat(String.valueOf(e)));
                e.printStackTrace();
            }
        }
        parseDoneCallback.onResult(sDKSwitcher.b);
    }
}