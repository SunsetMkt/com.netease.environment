package com.netease.ntunisdk.modules.ngwebviewgeneral;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import com.alipay.sdk.m.u.i;
import com.facebook.react.uimanager.ViewProps;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.modules.api.ModulesManager;
import com.netease.ntunisdk.modules.base.BaseModules;
import com.netease.ntunisdk.modules.base.call.IModulesCall;
import com.netease.ntunisdk.modules.ngwebviewgeneral.aidl.IPCWebViewManager;
import com.netease.ntunisdk.modules.ngwebviewgeneral.log.NgWebviewLog;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivityEx;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivityEx2;
import com.netease.ntunisdk.modules.personalinfolist.HookManager;
import com.netease.ntunisdk.util.cutout.CutoutUtil;
import com.netease.rnccplayer.VideoView;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class NgWebViewGeneralModule extends BaseModules {
    private static final String SDK_VERSION = "3.0";
    private static final String TAG = "UniSDK NgWebViewGeneral";
    private int cutoutHeight;
    private int cutoutWidth;
    private boolean isHasPdfView;
    private String isSingleInstance;
    private String isSingleProcess;
    private Context mContext;
    private IPCWebViewManager mIPCManager;
    private String openJson;

    @Override // com.netease.ntunisdk.modules.base.BaseModules
    public String getModuleName() {
        return "ngWebViewGeneral";
    }

    @Override // com.netease.ntunisdk.modules.base.BaseModules
    public String getVersion() {
        return "3.0";
    }

    public NgWebViewGeneralModule(Context context, IModulesCall iModulesCall) throws ClassNotFoundException {
        super(context, iModulesCall);
        this.isHasPdfView = true;
        this.openJson = "";
        this.mContext = context;
        StringBuilder sb = new StringBuilder();
        sb.append("isHasCutout: ");
        Activity activity = (Activity) context;
        sb.append(CutoutUtil.hasCutout(activity));
        NgWebviewLog.d(TAG, sb.toString(), new Object[0]);
        NgWebviewLog.d(TAG, "CutoutWidth: " + CutoutUtil.getCutoutWidthHeight(activity)[0], new Object[0]);
        NgWebviewLog.d(TAG, "CutoutHeight: " + CutoutUtil.getCutoutWidthHeight(activity)[1], new Object[0]);
        NgWebviewLog.d(TAG, "CutoutUtil.getCutoutPosition[0]: " + CutoutUtil.getCutoutPosition(activity)[0], new Object[0]);
        NgWebviewLog.d(TAG, "CutoutUtil.getCutoutPosition[1]: " + CutoutUtil.getCutoutPosition(activity)[1], new Object[0]);
        NgWebviewLog.d(TAG, "CutoutUtil.getCutoutPosition[2]: " + CutoutUtil.getCutoutPosition(activity)[2], new Object[0]);
        NgWebviewLog.d(TAG, "CutoutUtil.getCutoutPosition[3]: " + CutoutUtil.getCutoutPosition(activity)[3], new Object[0]);
        NgWebviewLog.d(TAG, "NgWebViewGeneralModule-context: " + context, new Object[0]);
        NgWebviewLog.d(TAG, "Build.MODEL: " + Build.MODEL, new Object[0]);
        this.cutoutWidth = CutoutUtil.getCutoutWidthHeight(activity)[0];
        this.cutoutHeight = CutoutUtil.getCutoutWidthHeight(activity)[1];
        try {
            Class.forName("com.github.barteksc.pdfviewer.PDFView");
        } catch (ClassNotFoundException unused) {
            NgWebviewLog.e(TAG, "Didn't find pdfViewClass , Please check if this feature is required");
            this.isHasPdfView = false;
        }
    }

    @Override // com.netease.ntunisdk.modules.base.BaseModules
    public String extendFunc(String str, String str2, String str3, Object... objArr) throws ClassNotFoundException {
        NgWebviewLog.d(TAG, "BaseModules extendFunc: " + str3, new Object[0]);
        try {
            JSONObject jSONObject = new JSONObject(str3);
            String strOptString = jSONObject.optString("methodId");
            if ("NGWebViewOpenURL".equalsIgnoreCase(strOptString)) {
                openNgWebview(jSONObject, false, str2);
            } else if ("NGWebViewClose".equalsIgnoreCase(strOptString)) {
                if (this.mIPCManager != null && !"1".equals(this.isSingleProcess)) {
                    this.mIPCManager.send(str3);
                } else {
                    NgWebviewActivity ngWebviewActivity = NgWebviewActivity.getInstance();
                    if (ngWebviewActivity == null) {
                        NgWebviewLog.d(TAG, "NgWebviewActivity is null", new Object[0]);
                        return "";
                    }
                    ngWebviewActivity.closeWebview("ntExtendFunc");
                }
            } else if ("NGWebViewCallbackToWeb".equalsIgnoreCase(strOptString)) {
                if (this.mIPCManager != null && !"1".equals(this.isSingleProcess)) {
                    this.mIPCManager.send(str3);
                } else {
                    String strOptString2 = jSONObject.optString("callback_id");
                    NgWebviewActivity ngWebviewActivity2 = NgWebviewActivity.getInstance();
                    if (ngWebviewActivity2 == null) {
                        NgWebviewLog.d(TAG, "NgWebviewActivity is null", new Object[0]);
                        return "";
                    }
                    ngWebviewActivity2.onJsCallback(strOptString2, str3);
                }
            } else if ("NGWebViewControl".equalsIgnoreCase(strOptString) && "1".equals(this.isSingleInstance)) {
                String strOptString3 = jSONObject.optString("action");
                NgWebviewActivity ngWebviewActivity3 = NgWebviewActivity.getInstance();
                if (ngWebviewActivity3 == null) {
                    NgWebviewLog.d(TAG, "NgWebviewActivity is null", new Object[0]);
                    return "";
                }
                if (ViewProps.HIDDEN.equals(strOptString3)) {
                    ngWebviewActivity3.moveTaskToBack(true);
                } else if ("show".equals(strOptString3)) {
                    openNgWebview(new JSONObject(this.openJson), true, str2);
                }
            }
        } catch (JSONException e) {
            NgWebviewLog.d(TAG, "extendFunc json parse error", new Object[0]);
            e.printStackTrace();
        }
        return "";
    }

    public static String getAppVersionName(Context context) {
        if (context != null) {
            try {
                return context.getPackageManager().getPackageInfo(getAppPackageName(context), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getAppPackageName(Context context) {
        return context != null ? context.getPackageName() : "";
    }

    public static String paddingPx2dip(Context context, String str) {
        float f = context.getResources().getDisplayMetrics().density;
        String[] strArrSplit = str.substring(1, str.length() - 1).split(",");
        String str2 = "{";
        for (int i = 0; i < strArrSplit.length; i++) {
            str2 = i == strArrSplit.length - 1 ? str2 + ((int) ((Float.valueOf(strArrSplit[i]).floatValue() / f) + 0.5f)) + i.d : str2 + ((int) ((Float.valueOf(strArrSplit[i]).floatValue() / f) + 0.5f)) + ",";
        }
        NgWebviewLog.d(TAG, "paddingPx2dip res: " + str2, new Object[0]);
        return str2;
    }

    private boolean handleSpecialModel() {
        NgWebviewLog.d(TAG, "Build.MODEL: " + Build.MODEL, new Object[0]);
        if ("MRR-W29".equals(Build.MODEL)) {
            return true;
        }
        return CutoutUtil.hasCutout((Activity) this.mContext);
    }

    @Override // com.netease.ntunisdk.modules.base.BaseModules
    public void receiveOthersModulesMsg(String str, String str2) {
        super.receiveOthersModulesMsg(str, str2);
    }

    public static String getNetworkType() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", "ntGetNetworktype");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ModulesManager.getInst().extendFunc("ngWebViewGeneral", "deviceInfo", jSONObject.toString());
    }

    private void openNgWebview(JSONObject jSONObject, boolean z, String str) throws JSONException, ClassNotFoundException {
        int i;
        String str2;
        int i2;
        String strOptString;
        Intent intent;
        NgWebviewLog.d(TAG, "isWebviewShow: " + z, new Object[0]);
        this.isSingleProcess = jSONObject.optString("isSingleProcess");
        this.isSingleInstance = jSONObject.optString("isSingleInstance");
        if (!z && this.isSingleInstance.equals("1")) {
            NgWebviewActivity ngWebviewActivity = NgWebviewActivity.getInstance();
            if (ngWebviewActivity == null) {
                NgWebviewLog.d(TAG, "NgWebviewActivity is null", new Object[0]);
            } else {
                ngWebviewActivity.closeWebview("OverrideClose");
            }
        }
        this.openJson = jSONObject.toString();
        String strOptString2 = jSONObject.optString("URLString");
        NgWebviewLog.d(TAG, "URLString=" + strOptString2, new Object[0]);
        if (TextUtils.isEmpty(strOptString2)) {
            NgWebviewLog.d(TAG, "URLString is empty", new Object[0]);
        }
        NgWebviewLog.d(TAG, "CutoutUtil.hasCutout() :" + CutoutUtil.hasCutout((Activity) this.context), new Object[0]);
        String strOptString3 = jSONObject.optString("scriptVersion");
        String strOptString4 = jSONObject.optString("isFullScreen", "1");
        String strOptString5 = jSONObject.optString("navigationBarVisible");
        int iOptInt = jSONObject.optInt("origin_x");
        int iOptInt2 = jSONObject.optInt("origin_y");
        int iOptInt3 = jSONObject.optInt("cls_btn_origin_x");
        int iOptInt4 = jSONObject.optInt("cls_btn_origin_y");
        int iOptInt5 = jSONObject.optInt("cls_btn_width");
        int iOptInt6 = jSONObject.optInt("cls_btn_height");
        int iOptInt7 = jSONObject.optInt("width");
        int iOptInt8 = jSONObject.optInt("height");
        String strOptString6 = jSONObject.optString(ConstProp.WEBVIEW_BKCOLOR);
        String strOptString7 = jSONObject.optString(ConstProp.YY_GAMEID);
        String strOptString8 = jSONObject.optString("blackBorderColor");
        if (iOptInt7 <= 0 || iOptInt8 <= 0) {
            i = iOptInt5;
            str2 = strOptString4;
            i2 = iOptInt4;
        } else {
            i2 = iOptInt4;
            i = iOptInt5;
            str2 = "0";
        }
        int iOptInt9 = jSONObject.optInt(VideoView.EVENT_PROP_ORIENTATION);
        if (str2.equals("1") && strOptString5.equals("1")) {
            jSONObject.optString("blackBorderVisible", "1");
        } else {
            jSONObject.optString("blackBorderVisible");
        }
        String strOptString9 = jSONObject.optString("qstn_close_btn");
        String strOptString10 = jSONObject.optString("closeButtonVisible");
        String strOptString11 = jSONObject.optString("supportBackKey");
        String strOptString12 = jSONObject.optString("secureVerify");
        String strOptString13 = jSONObject.optString("additionalUserAgent");
        String strOptString14 = jSONObject.optString("h5_padding");
        String strOptString15 = jSONObject.optString("WEBVIEW_CONTENT_TYPE");
        if (strOptString13.contains("Unisdk/2.0")) {
            strOptString13 = strOptString13.replace("Unisdk/2.0", "Unisdk/2.1");
        } else if (!strOptString13.contains("Unisdk/2.1")) {
            strOptString13 = " Unisdk/2.1 NetType/" + getNetworkType() + " os/android" + Build.VERSION.SDK_INT + " ngwebview/3.0 package_name/" + this.mContext.getPackageName() + " " + strOptString13;
        }
        NgWebviewLog.d(TAG, "ngWebviewUserAgent: " + strOptString13, new Object[0]);
        Boolean bool = false;
        if (strOptString2.contains("survey.163.com") || strOptString2.contains("survey.netease.com") || strOptString2.contains("survey.easebar.com") || strOptString2.contains("research.163.com") || strOptString2.contains("research.easebar.com") || strOptString2.contains("survey-game.163.com") || strOptString2.contains("research-game.163.com") || strOptString2.contains("research-game.easebar.com") || "SURVEY".equals(strOptString15)) {
            NgWebviewLog.d(TAG, "questionnaire handle.", new Object[0]);
            strOptString = jSONObject.optString("URLString");
            NgWebviewLog.d(TAG, "catUidRoleidServer.URLString=" + strOptString, new Object[0]);
            bool = true;
        } else {
            strOptString = strOptString2;
        }
        if (!TextUtils.isEmpty(strOptString14) && "1".equals(str2) && handleSpecialModel()) {
            strOptString13 = strOptString13 + " uni_padding/" + paddingPx2dip(this.mContext, strOptString14);
        }
        String strOptString16 = jSONObject.optString("intercept_schemes");
        if (TextUtils.isEmpty(strOptString16)) {
            strOptString16 = jSONObject.optString("handle_schemes");
        }
        WebviewParams webviewParams = new WebviewParams();
        webviewParams.setSingleProcess("1".equals(this.isSingleProcess));
        webviewParams.setSingleInstance("1".equals(this.isSingleInstance));
        webviewParams.setBlackBorderColor(strOptString8);
        webviewParams.setSurvey(bool.booleanValue());
        webviewParams.setHasPdfView(this.isHasPdfView);
        webviewParams.setSecureVerify("1".equals(strOptString12));
        webviewParams.setUrl(strOptString);
        webviewParams.setScriptVersion(strOptString3);
        webviewParams.setOriginX(iOptInt);
        webviewParams.setOriginY(iOptInt2);
        webviewParams.setWidth(iOptInt7);
        webviewParams.setHeight(iOptInt8);
        webviewParams.setCloseBtnOriginX(iOptInt3);
        webviewParams.setCloseBtnOriginY(i2);
        webviewParams.setCloseBtnWidth(i);
        webviewParams.setCloseBtnHeight(iOptInt6);
        webviewParams.setOrientation(iOptInt9);
        webviewParams.setAdditionalUserAgent(strOptString13);
        webviewParams.setNavigationBarVisible("1".equals(strOptString5));
        webviewParams.setQstnCloseBtnVisible("1".equals(strOptString9));
        webviewParams.setCloseButtonVisible("1".equals(strOptString10));
        webviewParams.setSupportBackKey("1".equals(strOptString11));
        webviewParams.setIntercept_schemes(strOptString16);
        webviewParams.setFullScreen("1".equals(str2));
        webviewParams.setFullScreenNoAdaptive("2".equals(str2));
        webviewParams.setIsModule(true);
        webviewParams.setSource(str);
        webviewParams.setHasCutout(CutoutUtil.hasCutout((Activity) this.context));
        webviewParams.setWebviewBackgroundColor(strOptString6);
        webviewParams.setYYGameID(strOptString7);
        webviewParams.setChannelID(getModuleName());
        webviewParams.setAppVersionName(getAppVersionName(this.context));
        webviewParams.setCutoutHeight(this.cutoutHeight);
        webviewParams.setCutoutWidth(this.cutoutWidth);
        if ("1".equals(this.isSingleInstance)) {
            NgWebviewLog.d(TAG, "singleInstance mode", new Object[0]);
            intent = new Intent(this.context, (Class<?>) NgWebviewActivityEx2.class);
        } else if (!"1".equals(this.isSingleProcess)) {
            NgWebviewLog.d(TAG, "isSingleProcess mode", new Object[0]);
            intent = new Intent(this.context, (Class<?>) NgWebviewActivityEx.class);
            IPCWebViewManager iPCWebViewManager = this.mIPCManager;
            if (iPCWebViewManager != null) {
                iPCWebViewManager.unbindService(this.context);
                this.mIPCManager = null;
            }
            this.mIPCManager = new IPCWebViewManager();
            this.mIPCManager.bindService(this.context);
        } else {
            NgWebviewLog.d(TAG, "default mode", new Object[0]);
            intent = new Intent(this.context, (Class<?>) NgWebviewActivity.class);
        }
        intent.putExtra("WebviewParams", webviewParams);
        HookManager.startActivity(this.context, intent);
    }
}