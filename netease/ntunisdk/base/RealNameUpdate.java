package com.netease.ntunisdk.base;

import android.text.TextUtils;
import com.netease.ntunisdk.base.utils.NetUtil;
import com.netease.ntunisdk.base.utils.WgetDoneCallback;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.json.JSONObject;
import org.json.JSONTokener;

/* loaded from: classes2.dex */
public final class RealNameUpdate {

    public interface RealNameUpdateListener {
        void updateCallback();
    }

    private static String a() {
        String propStr = SdkMgr.getInst() == null ? null : SdkMgr.getInst().getPropStr(ConstProp.SDK_UNI_UPDATE_URL);
        if (TextUtils.isEmpty(propStr)) {
            return SdkMgr.getInst().getPropInt("EB", -1) == 1 ? "https://g0.gsf.easebar.com/feature/" : "https://g0.gsf.netease.com/feature/";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(propStr);
        sb.append(propStr.endsWith("/") ? "" : "/");
        sb.append("feature/");
        return sb.toString();
    }

    static void b(SdkBase sdkBase, RealNameUpdateListener realNameUpdateListener) {
        String strA = a();
        if (TextUtils.isEmpty(strA)) {
            UniSdkUtils.i("UniSDK RealNameUpdate", "null or empty url, update feature will not go on");
            return;
        }
        String str = strA + "all.json";
        String str2 = str + ".md5?gameid=" + SdkMgr.getInst().getPropStr("JF_GAMEID");
        UniSdkUtils.d("UniSDK RealNameUpdate", "feature md5 url:".concat(String.valueOf(str2)));
        NetUtil.wgetIncludeNewLine(str2, new b(sdkBase, str, true, realNameUpdateListener));
    }

    static class b implements WgetDoneCallback {

        /* renamed from: a, reason: collision with root package name */
        private SdkBase f1616a;
        private String b;
        private boolean c;
        private RealNameUpdateListener d;

        public b(SdkBase sdkBase, String str, boolean z, RealNameUpdateListener realNameUpdateListener) {
            this.f1616a = sdkBase;
            this.b = str;
            this.c = z;
            this.d = realNameUpdateListener;
        }

        @Override // com.netease.ntunisdk.base.utils.WgetDoneCallback
        public final void ProcessResult(String str) {
            UniSdkUtils.d("UniSDK RealNameUpdate", "features md5 api result:".concat(String.valueOf(str)));
            if (!TextUtils.isEmpty(str)) {
                UniSdkUtils.d("UniSDK RealNameUpdate", "feature url:" + this.b);
                NetUtil.wgetIncludeNewLine(this.b, new a(this.f1616a, str, this.c, this.d));
                return;
            }
            if (this.c) {
                RealNameUpdate.a(this.f1616a, this.d);
            } else {
                this.d.updateCallback();
            }
        }
    }

    static class a implements WgetDoneCallback {

        /* renamed from: a, reason: collision with root package name */
        private SdkBase f1615a;
        private String b;
        private boolean c;
        private RealNameUpdateListener d;

        public a(SdkBase sdkBase, String str, boolean z, RealNameUpdateListener realNameUpdateListener) {
            this.f1615a = sdkBase;
            this.b = str;
            this.c = z;
            this.d = realNameUpdateListener;
        }

        @Override // com.netease.ntunisdk.base.utils.WgetDoneCallback
        public final void ProcessResult(String str) throws NoSuchAlgorithmException {
            UniSdkUtils.d("UniSDK RealNameUpdate", "features result:".concat(String.valueOf(str)));
            if (!TextUtils.isEmpty(str)) {
                String strB = RealNameUpdate.b(str.getBytes());
                UniSdkUtils.d("UniSDK RealNameUpdate", "features content md5 result:".concat(String.valueOf(strB)));
                if (this.b.equalsIgnoreCase(strB)) {
                    try {
                        JSONObject jSONObjectOptJSONObject = ((JSONObject) new JSONTokener(str).nextValue()).optJSONObject(this.f1615a.getChannel());
                        if (jSONObjectOptJSONObject != null) {
                            JSONObject jSONObjectOptJSONObject2 = jSONObjectOptJSONObject.optJSONObject(this.f1615a.getSDKVersion());
                            if (jSONObjectOptJSONObject2 != null) {
                                String strOptString = jSONObjectOptJSONObject2.optString("is_real_name");
                                UniSdkUtils.d("UniSDK RealNameUpdate", "is_real_name result:".concat(String.valueOf(strOptString)));
                                if (!TextUtils.isEmpty(strOptString)) {
                                    this.f1615a.setPropStr(ConstProp.MODE_REAL_NAME, strOptString);
                                }
                            } else {
                                String strOptString2 = jSONObjectOptJSONObject.optString("is_real_name");
                                UniSdkUtils.d("UniSDK RealNameUpdate", "is_real_name result:".concat(String.valueOf(strOptString2)));
                                if (!TextUtils.isEmpty(strOptString2)) {
                                    this.f1615a.setPropStr(ConstProp.MODE_REAL_NAME, strOptString2);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        UniSdkUtils.d("UniSDK RealNameUpdate", "feature result json exception");
                    }
                } else {
                    UniSdkUtils.d("UniSDK RealNameUpdate", "features md5 is difference");
                }
            }
            if (this.c) {
                RealNameUpdate.a(this.f1615a, this.d);
            } else {
                this.d.updateCallback();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String b(byte[] bArr) throws NoSuchAlgorithmException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bArr);
            byte[] bArrDigest = messageDigest.digest();
            char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            char[] cArr2 = new char[bArrDigest.length * 2];
            int i = 0;
            for (byte b2 : bArrDigest) {
                int i2 = i + 1;
                cArr2[i] = cArr[(b2 >>> 4) & 15];
                i = i2 + 1;
                cArr2[i2] = cArr[b2 & 15];
            }
            return new String(cArr2);
        } catch (NoSuchAlgorithmException unused) {
            return "";
        }
    }

    static /* synthetic */ void a(SdkBase sdkBase, RealNameUpdateListener realNameUpdateListener) {
        String strA = a();
        if (TextUtils.isEmpty(strA)) {
            UniSdkUtils.i("UniSDK RealNameUpdate", "null or empty url, gameid feature will not go on");
            return;
        }
        String str = strA + String.format("%s.json", SdkMgr.getInst().getPropStr("JF_GAMEID"));
        String str2 = str + ".md5?gameid=" + SdkMgr.getInst().getPropStr("JF_GAMEID");
        UniSdkUtils.d("UniSDK RealNameUpdate", "feature md5 url:".concat(String.valueOf(str2)));
        NetUtil.wgetIncludeNewLine(str2, new b(sdkBase, str, false, realNameUpdateListener));
    }
}