package com.netease.ntunisdk.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Base64;
import com.netease.ntunisdk.base.utils.NtUniSDKConfigUtil;
import com.netease.ntunisdk.base.utils.StrUtil;
import com.netease.ntunisdk.base.utils.a;
import com.netease.ntunisdk.unilogger.global.Const;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/* loaded from: classes2.dex */
public abstract class SdkApplication {

    /* renamed from: a */
    private final Hashtable<String, String> f1631a = new Hashtable<>();
    protected Context myCtx;

    protected void doSepcialConfigVal(JSONObject jSONObject) {
    }

    public abstract String getChannel();

    public void handleOnApplicationAttachBaseContext(Context context) {
    }

    public void handleOnApplicationAttachBaseContext(Context context, Application application) {
    }

    public void handleOnApplicationConfigurationChanged(Context context, Application application, Configuration configuration) {
    }

    public void handleOnApplicationLowMemory(Context context, Application application) {
    }

    public void handleOnApplicationOnCreate(Context context) {
    }

    public void handleOnApplicationOnCreate(Context context, Application application) {
    }

    public void handleOnApplicationTerminate(Context context, Application application) {
    }

    public void handleOnApplicationTrimMemory(Context context, Application application, int i) {
    }

    public SdkApplication(Context context) throws IOException {
        String str;
        InputStream inputStreamOpen;
        UniSdkUtils.d("UniSDK SdkApplication", "SdkApplication construct");
        this.myCtx = context;
        UniSdkUtils.a();
        NtUniSDKConfigUtil.readLibraryConfig(context, new NtUniSDKConfigUtil.SdkBaseHandle() { // from class: com.netease.ntunisdk.base.SdkApplication.1
            AnonymousClass1() {
            }

            @Override // com.netease.ntunisdk.base.utils.NtUniSDKConfigUtil.SdkBaseHandle
            public final void setPropStr(String str2, String str3) {
                SdkApplication.this.setPropStr(str2, str3);
            }

            @Override // com.netease.ntunisdk.base.utils.NtUniSDKConfigUtil.SdkBaseHandle
            public final void doConfigVal(JSONObject jSONObject, String str2, boolean z) throws JSONException {
                SdkApplication.this.a(jSONObject, str2, z);
            }
        }, "UniSDK SdkApplication");
        try {
            inputStreamOpen = context.getAssets().open(Const.Common.NTUNISDK_COMMON_DATA, 3);
        } catch (IOException unused) {
            UniSdkUtils.i("UniSDK SdkApplication", "ntunisdk_common_data config not found");
            str = null;
        }
        if (inputStreamOpen == null) {
            UniSdkUtils.d("UniSDK SdkApplication", "ntunisdk_common_data null");
        } else {
            int iAvailable = inputStreamOpen.available();
            if (iAvailable != 0) {
                byte[] bArr = new byte[iAvailable];
                inputStreamOpen.read(bArr);
                str = new String(bArr, "UTF-8");
                if (str == null) {
                    UniSdkUtils.d("UniSDK SdkApplication", "ntunisdk_common_data is null");
                } else {
                    UniSdkUtils.d("UniSDK SdkApplication", str);
                    if (str.contains("\uff1a") || str.contains("\u201c") || str.contains("\u201d")) {
                        UniSdkUtils.e("UniSDK SdkApplication", "ntunisdk_common_data\u5305\u542b\u4e2d\u6587\u7279\u6b8a\u5b57\u7b26");
                    }
                    try {
                        JSONObject jSONObject = (JSONObject) new JSONTokener(str).nextValue();
                        if (TextUtils.isEmpty(getAppChannel())) {
                            String strA = a.a(context);
                            if (!TextUtils.isEmpty(strA)) {
                                this.f1631a.put(ConstProp.APP_CHANNEL, strA);
                            } else {
                                a(jSONObject, ConstProp.APP_CHANNEL, false);
                            }
                        }
                        doConfigVal(jSONObject, "JF_GAMEID");
                    } catch (JSONException unused2) {
                        UniSdkUtils.i("UniSDK SdkApplication", "ntunisdk_common_data config parse to json error");
                    }
                }
            }
        }
        readConfig(context);
    }

    public void setPropStr(String str, String str2) {
        UniSdkUtils.d("UniSDK SdkApplication", "key:" + str + "val:" + str2);
        this.f1631a.put(str, str2);
    }

    public String getPropStr(String str) {
        if (this.f1631a.containsKey(str)) {
            return this.f1631a.get(str);
        }
        return null;
    }

    public int getPropInt(String str, int i) {
        String propStr = getPropStr(str);
        if (propStr == null) {
            return i;
        }
        try {
            return Integer.parseInt(propStr);
        } catch (Exception unused) {
            return i;
        }
    }

    public String getAppChannel() {
        UniSdkUtils.i("UniSDK SdkApplication", "APP_CHANNEL:" + getPropStr(ConstProp.APP_CHANNEL));
        return getPropStr(ConstProp.APP_CHANNEL);
    }

    protected void doConfigVal(JSONObject jSONObject, String str) throws JSONException {
        a(jSONObject, str, true);
    }

    protected void a(JSONObject jSONObject, String str, boolean z) throws JSONException {
        String strValidate;
        if (jSONObject.has(str)) {
            try {
                strValidate = jSONObject.getString(str);
            } catch (JSONException unused) {
                UniSdkUtils.d("UniSDK SdkApplication", "no tag:".concat(String.valueOf(str)));
                strValidate = null;
            }
            if (strValidate == null || getPropStr(str) != null) {
                return;
            }
            UniSdkUtils.d("UniSDK SdkApplication", "doConfigVal: " + str + "--->" + strValidate);
            if (z) {
                strValidate = StrUtil.validate(strValidate);
            }
            setPropStr(str, strValidate);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkApplication$1 */
    final class AnonymousClass1 implements NtUniSDKConfigUtil.SdkBaseHandle {
        AnonymousClass1() {
        }

        @Override // com.netease.ntunisdk.base.utils.NtUniSDKConfigUtil.SdkBaseHandle
        public final void setPropStr(String str2, String str3) {
            SdkApplication.this.setPropStr(str2, str3);
        }

        @Override // com.netease.ntunisdk.base.utils.NtUniSDKConfigUtil.SdkBaseHandle
        public final void doConfigVal(JSONObject jSONObject, String str2, boolean z) throws JSONException {
            SdkApplication.this.a(jSONObject, str2, z);
        }
    }

    protected void readConfig(Context context) throws IOException {
        String str;
        InputStream inputStreamOpen;
        int iAvailable;
        String str2 = getChannel() + "_data";
        try {
            inputStreamOpen = context.getAssets().open(str2, 3);
            iAvailable = inputStreamOpen.available();
        } catch (IOException unused) {
            UniSdkUtils.i("UniSDK SdkApplication", str2 + " read exception");
            str = null;
        }
        if (iAvailable == 0) {
            UniSdkUtils.w("UniSDK SdkApplication", str2 + " is empty");
            return;
        }
        byte[] bArr = new byte[iAvailable];
        inputStreamOpen.read(bArr);
        str = new String(bArr, "UTF-8");
        if (TextUtils.isEmpty(str)) {
            UniSdkUtils.d("UniSDK SdkApplication", str2 + " is empty");
            return;
        }
        try {
            if (StrUtil.isBase64(str)) {
                str = new String(Base64.decode(str, 0), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (str == null) {
            UniSdkUtils.d("UniSDK SdkApplication", " null jsonStr");
            return;
        }
        if (str.contains("\uff1a") || str.contains("\u201c") || str.contains("\u201d")) {
            UniSdkUtils.e("UniSDK SdkApplication", str2 + "\u5305\u542b\u4e2d\u6587\u7279\u6b8a\u5b57\u7b26");
        }
        try {
            JSONObject jSONObject = (JSONObject) new JSONTokener(str).nextValue();
            a(jSONObject, ConstProp.UNISDK_SERVER_KEY, false);
            StrUtil.setKey(getPropStr(ConstProp.UNISDK_SERVER_KEY));
            a(jSONObject, ConstProp.GAMEID, false);
            doConfigVal(jSONObject, ConstProp.APP_KEY);
            doConfigVal(jSONObject, ConstProp.APP_SECRET);
            doConfigVal(jSONObject, ConstProp.APPID);
            a(jSONObject, ConstProp.APP_NAME, false);
            doConfigVal(jSONObject, ConstProp.APP_LOCATION);
            a(jSONObject, ConstProp.APP_VERSION, false);
            a(jSONObject, ConstProp.SCR_ORIENTATION, false);
            doConfigVal(jSONObject, ConstProp.CPID);
            doConfigVal(jSONObject, ConstProp.CP_KEY);
            doConfigVal(jSONObject, ConstProp.SERVER_ID);
            a(jSONObject, ConstProp.PAY_CB_URL, false);
            doConfigVal(jSONObject, ConstProp.RSA_PRIVATE);
            doConfigVal(jSONObject, ConstProp.RSA_PUBLIC);
            doConfigVal(jSONObject, ConstProp.SDK_UPDATE_CHECK_STRICT);
            doConfigVal(jSONObject, ConstProp.BUOY_PRIVATEKEY);
            doConfigVal(jSONObject, ConstProp.USER_ID);
            doConfigVal(jSONObject, ConstProp.PACKET_ID);
            doConfigVal(jSONObject, ConstProp.EXCHANGE_RATE);
            doConfigVal(jSONObject, ConstProp.EXCHANGE_UNIT);
            doConfigVal(jSONObject, ConstProp.CHANNEL_ID);
            doConfigVal(jSONObject, ConstProp.SPLASH);
            doConfigVal(jSONObject, ConstProp.SPLASH_TIME);
            doConfigVal(jSONObject, ConstProp.SPLASH_COLOR);
            doConfigVal(jSONObject, ConstProp.SPLASH_SECOND);
            doConfigVal(jSONObject, ConstProp.DEBUG_MODE);
            if (TextUtils.isEmpty(getAppChannel())) {
                String strA = a.a(context);
                if (!TextUtils.isEmpty(strA)) {
                    this.f1631a.put(ConstProp.APP_CHANNEL, strA);
                } else {
                    a(jSONObject, ConstProp.APP_CHANNEL, false);
                }
            }
            doConfigVal(jSONObject, ConstProp.LAUNCHER_NAME);
            doConfigVal(jSONObject, ConstProp.APPSFLYER_DEV_KEY);
            doConfigVal(jSONObject, ConstProp.ADVERTISER_APPID);
            doConfigVal(jSONObject, ConstProp.TIMELINE_KEY);
            doConfigVal(jSONObject, ConstProp.PLATFORM_KEY);
            doConfigVal(jSONObject, ConstProp.GAME_REGION);
            doConfigVal(jSONObject, ConstProp.GAME_REGION_CN);
            doConfigVal(jSONObject, ConstProp.GAME_REGION_AS);
            doConfigVal(jSONObject, ConstProp.GAME_REGION_US);
            doConfigVal(jSONObject, ConstProp.GAME_REGION_SA);
            doConfigVal(jSONObject, ConstProp.GAME_ENGINE);
            doConfigVal(jSONObject, ConstProp.CC_SHOW_FPS_SETTING);
            doConfigVal(jSONObject, ConstProp.CC_DEFAULT_FPS);
            doConfigVal(jSONObject, ConstProp.PAYTYPE);
            doConfigVal(jSONObject, ConstProp.PAYCODE);
            doConfigVal(jSONObject, ConstProp.MONTHTYPE);
            doConfigVal(jSONObject, ConstProp.LIANYUN);
            a(jSONObject, ConstProp.SINGLE_CB, false);
            doConfigVal(jSONObject, ConstProp.DK_APPID);
            doConfigVal(jSONObject, ConstProp.DK_APP_KEY);
            doConfigVal(jSONObject, ConstProp.SHARE_QQ_API);
            doConfigVal(jSONObject, ConstProp.SHARE_WEIBO_API);
            doConfigVal(jSONObject, ConstProp.SHARE_WEIXIN_API);
            doConfigVal(jSONObject, ConstProp.SHARE_YIXIN_API);
            doConfigVal(jSONObject, ConstProp.ENABLE_EXLOGIN_GUEST);
            doConfigVal(jSONObject, ConstProp.ENABLE_EXLOGIN_WEIBO);
            doConfigVal(jSONObject, ConstProp.ENABLE_EXLOGIN_MOBILE);
            doConfigVal(jSONObject, ConstProp.ENABLE_EXLOGIN_GOOGLEPLUS);
            doConfigVal(jSONObject, ConstProp.DATA_REPORT_MODE);
            a(jSONObject, ConstProp.GAME_NAME, false);
            doConfigVal(jSONObject, ConstProp.RETRIEVE_USER);
            doConfigVal(jSONObject, ConstProp.DOMAIN);
            doConfigVal(jSONObject, ConstProp.QQ_APPID);
            doConfigVal(jSONObject, ConstProp.QQ_APP_KEY);
            doConfigVal(jSONObject, ConstProp.WX_APPID);
            doConfigVal(jSONObject, ConstProp.WX_APP_KEY);
            doConfigVal(jSONObject, ConstProp.WEIBO_SSO_APP_KEY);
            a(jSONObject, ConstProp.WEIBO_SSO_URL, false);
            doConfigVal(jSONObject, ConstProp.OFFER_ID);
            doConfigVal(jSONObject, ConstProp.VERIFY_MODE);
            doConfigVal(jSONObject, ConstProp.REQUEST_UNISDK_SERVER);
            doConfigVal(jSONObject, ConstProp.UNISDK_CREATEORDER_URL);
            doConfigVal(jSONObject, ConstProp.UNISDK_QUERYORDER_URL);
            doConfigVal(jSONObject, ConstProp.UNISDK_CONSUMEORDER_URL);
            doConfigVal(jSONObject, ConstProp.LANGUAGE_CODE);
            doConfigVal(jSONObject, ConstProp.COUNTRY_CODE);
            doConfigVal(jSONObject, ConstProp.PURCHASE_REG_SERVER);
            doConfigVal(jSONObject, ConstProp.SPLASH_TYPE);
            doConfigVal(jSONObject, ConstProp.REQUEST_CMCC_PAYTYPE);
            doConfigVal(jSONObject, ConstProp.DEFAULT_CMCC_PAYTYPE);
            doConfigVal(jSONObject, ConstProp.GAME_VERSION);
            doConfigVal(jSONObject, ConstProp.DERIVE_CHANNEL);
            doConfigVal(jSONObject, ConstProp.CMCC_PAYTYPE_URL);
            doConfigVal(jSONObject, ConstProp.JF_LOG_KEY);
            doConfigVal(jSONObject, ConstProp.JF_OPEN_LOG_URL);
            doConfigVal(jSONObject, ConstProp.JF_PAY_LOG_URL);
            doConfigVal(jSONObject, "JF_GAMEID");
            doConfigVal(jSONObject, ConstProp.HAS_PAY_CB);
            doConfigVal(jSONObject, ConstProp.NEED_PLAY_GAME_SERVICE);
            a(jSONObject, ConstProp.UNISDK_SERVER_URL, false);
            doConfigVal(jSONObject, ConstProp.ENABLE_UNISDK_GUEST_DISCONNECT);
            doConfigVal(jSONObject, ConstProp.ENABLE_UNISDK_GUEST_UI);
            doConfigVal(jSONObject, ConstProp.FLOATBTN_CLOSED);
            doConfigVal(jSONObject, ConstProp.FLOAT_BTN_POS);
            a(jSONObject, ConstProp.UPDATE_CHECK_URL, false);
            a(jSONObject, ConstProp.UPDATE_DOWNLOAD_URL, false);
            doConfigVal(jSONObject, ConstProp.UNISDK_SERVER_MODE);
            doConfigVal(jSONObject, ConstProp.UNISDK_SERVER_EXTPARAM);
            doConfigVal(jSONObject, ConstProp.UNISDK_EXT_INFO);
            doConfigVal(jSONObject, ConstProp.CODE_SCANNER_PAY_URL);
            doConfigVal(jSONObject, ConstProp.ENABLE_TV);
            doConfigVal(jSONObject, ConstProp.EXTERNAL_OP_LIST);
            doConfigVal(jSONObject, ConstProp.UNISDK_JF_GAS3);
            doConfigVal(jSONObject, ConstProp.UNISDK_JF_GAS3_WEB);
            doConfigVal(jSONObject, ConstProp.UNISDK_JF_GAS3_URL);
            doConfigVal(jSONObject, ConstProp.SKIN_TYPE);
            doConfigVal(jSONObject, ConstProp.FLOW_CODE);
            doConfigVal(jSONObject, ConstProp.FLOW_KEY);
            doSepcialConfigVal(jSONObject);
        } catch (JSONException unused2) {
            UniSdkUtils.i("UniSDK SdkApplication", getChannel() + "_data config parse to json error");
        }
    }
}