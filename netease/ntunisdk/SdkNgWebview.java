package com.netease.ntunisdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import com.alipay.sdk.m.u.i;
import com.facebook.react.uimanager.ViewProps;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.OnFinishInitListener;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.base.utils.NetConnectivity;
import com.netease.ntunisdk.cutout.RespUtil;
import com.netease.ntunisdk.modules.api.ModulesManager;
import com.netease.ntunisdk.modules.ngwebviewgeneral.WebviewParams;
import com.netease.ntunisdk.modules.ngwebviewgeneral.aidl.IPCWebViewManager;
import com.netease.ntunisdk.modules.ngwebviewgeneral.log.NgWebviewLog;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivityEx;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivityEx2;
import com.netease.ntunisdk.modules.personalinfolist.HookManager;
import com.netease.ntunisdk.util.cutout.CutoutUtil;
import com.netease.ntunisdk.util.cutout.SingleScreenFoldingUtil;
import com.netease.rnccplayer.VideoView;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class SdkNgWebview extends SdkBase {
    private static final String SDK_VERSION = "3.0";
    private static final String TAG = "UniSDK ngwebview";
    private static IPCWebViewManager mIPCManager;
    private int cutoutHeight;
    private int cutoutWidth;
    private boolean isHasPdfView;
    private String isSingleInstance;
    private String isSingleProcess;
    private Context mContext;
    private String openJson;

    @Override // com.netease.ntunisdk.base.SdkBase
    public void checkOrder(OrderInfo orderInfo) {
    }

    @Override // com.netease.ntunisdk.base.SdkBase, com.netease.ntunisdk.base.GamerInterface
    public String getChannel() {
        return "ngwebview";
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getSDKVersion() {
        return "3.0";
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    protected String getUniSDKVersion() {
        return "3.0";
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void logout() {
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void openManager() {
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void upLoadUserInfo() {
    }

    public SdkNgWebview(Context context) {
        super(context);
        this.openJson = "";
        this.isHasPdfView = true;
        this.mContext = context;
        setPropInt(ConstProp.INNER_MODE_NO_PAY, 1);
        setPropInt(ConstProp.INNER_MODE_SECOND_CHANNEL, 1);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getLoginSession() {
        return hasLogin() ? getPropStr(ConstProp.SESSION) : ConstProp.S_NOT_LOGIN_SESSION;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getLoginUid() {
        return hasLogin() ? getPropStr(ConstProp.UID) : "";
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void init(OnFinishInitListener onFinishInitListener) {
        NgWebviewLog.isDebug = SdkMgr.getInst().getPropInt(ConstProp.DEBUG_MODE, 0) == 1;
        NgWebviewLog.d(TAG, "init...", new Object[0]);
        CutoutUtil.initUtil((Activity) this.myCtx, new SingleScreenFoldingUtil.OnInitFinishLister() { // from class: com.netease.ntunisdk.SdkNgWebview.1
            @Override // com.netease.ntunisdk.util.cutout.SingleScreenFoldingUtil.OnInitFinishLister
            public void onFinish(boolean z) {
            }
        });
        onFinishInitListener.finishInit(0);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void login() {
        setPropStr(ConstProp.UID, "NGWebViewUid");
        setPropStr(ConstProp.SESSION, "NGWebViewSession");
        setPropInt(ConstProp.LOGIN_STAT, 1);
        loginDone(0);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void extendFunc(String str) throws JSONException, ClassNotFoundException, UnsupportedEncodingException {
        JSONObject jSONObject;
        NgWebviewLog.d(TAG, "CutoutWidth: " + CutoutUtil.getCutoutWidthHeight((Activity) this.mContext)[0], new Object[0]);
        NgWebviewLog.d(TAG, "CutoutHeight: " + CutoutUtil.getCutoutWidthHeight((Activity) this.mContext)[1], new Object[0]);
        NgWebviewLog.d(TAG, "CutoutUtil.getCutoutPosition[0]: " + CutoutUtil.getCutoutPosition((Activity) this.mContext)[0], new Object[0]);
        NgWebviewLog.d(TAG, "CutoutUtil.getCutoutPosition[1]: " + CutoutUtil.getCutoutPosition((Activity) this.mContext)[1], new Object[0]);
        NgWebviewLog.d(TAG, "CutoutUtil.getCutoutPosition[2]: " + CutoutUtil.getCutoutPosition((Activity) this.mContext)[2], new Object[0]);
        NgWebviewLog.d(TAG, "CutoutUtil.getCutoutPosition[3]: " + CutoutUtil.getCutoutPosition((Activity) this.mContext)[3], new Object[0]);
        NgWebviewLog.d(TAG, "SdkNgWebview-mContext: " + this.mContext, new Object[0]);
        NgWebviewLog.d(TAG, "SdkNgWebview-myctx: " + this.myCtx, new Object[0]);
        this.cutoutWidth = CutoutUtil.getCutoutWidthHeight((Activity) this.mContext)[0];
        this.cutoutHeight = CutoutUtil.getCutoutWidthHeight((Activity) this.mContext)[1];
        try {
            Class.forName("com.github.barteksc.pdfviewer.PDFView");
        } catch (ClassNotFoundException unused) {
            NgWebviewLog.i(TAG, "Didn't find pdfViewClass , Please check if this feature is required !");
            this.isHasPdfView = false;
        }
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject = new JSONObject(str);
        } catch (JSONException e) {
            e = e;
            jSONObject = jSONObject2;
        }
        try {
            String string = jSONObject.getString("methodId");
            UniSdkUtils.d(TAG, "extendFunc:" + string);
            String strOptString = jSONObject.optString("channel");
            if (TextUtils.isEmpty(strOptString) || getChannel().equalsIgnoreCase(strOptString)) {
                if ("NGWebViewOpenURL".equalsIgnoreCase(string)) {
                    openNgWebview(jSONObject, false);
                    return;
                }
                if ("NGWebViewClose".equalsIgnoreCase(string)) {
                    if (mIPCManager != null && !"1".equals(this.isSingleProcess)) {
                        mIPCManager.send(str);
                        return;
                    }
                    NgWebviewActivity ngWebviewActivity = NgWebviewActivity.getInstance();
                    if (ngWebviewActivity == null) {
                        NgWebviewLog.d(TAG, "NgWebviewActivity is null", new Object[0]);
                        return;
                    } else {
                        ngWebviewActivity.closeWebview("ntExtendFunc");
                        return;
                    }
                }
                if ("NGWebViewCallbackToWeb".equalsIgnoreCase(string)) {
                    if (mIPCManager != null && !"1".equals(this.isSingleProcess)) {
                        mIPCManager.send(str);
                        return;
                    }
                    String strOptString2 = jSONObject.optString("callback_id");
                    NgWebviewActivity ngWebviewActivity2 = NgWebviewActivity.getInstance();
                    if (ngWebviewActivity2 == null) {
                        NgWebviewLog.d(TAG, "NgWebviewActivity is null", new Object[0]);
                        return;
                    } else {
                        ngWebviewActivity2.onJsCallback(strOptString2, str);
                        return;
                    }
                }
                if ("ModuleBaseReInit".equalsIgnoreCase(string)) {
                    ModulesManager.getInst().reInit(this.mContext);
                    return;
                }
                if ("NGWebViewControl".equalsIgnoreCase(string) && "1".equals(this.isSingleInstance)) {
                    String strOptString3 = jSONObject.optString("action");
                    NgWebviewActivity ngWebviewActivity3 = NgWebviewActivity.getInstance();
                    if (ngWebviewActivity3 == null) {
                        NgWebviewLog.d(TAG, "NgWebviewActivity is null", new Object[0]);
                        return;
                    } else if (ViewProps.HIDDEN.equals(strOptString3)) {
                        ngWebviewActivity3.moveTaskToBack(true);
                        return;
                    } else {
                        if ("show".equals(strOptString3)) {
                            openNgWebview(new JSONObject(this.openJson), true);
                            return;
                        }
                        return;
                    }
                }
                if (TextUtils.isEmpty(strOptString)) {
                    return;
                }
                jSONObject.put(RespUtil.UniSdkField.RESP_CODE, 1);
                jSONObject.put(RespUtil.UniSdkField.RESP_MSG, "methodId not exist");
                extendFuncCall(jSONObject.toString());
            }
        } catch (JSONException e2) {
            e = e2;
            NgWebviewLog.d(TAG, "extendFunc json parse error", new Object[0]);
            e.printStackTrace();
            if (TextUtils.isEmpty("")) {
                return;
            }
            try {
                jSONObject.put(RespUtil.UniSdkField.RESP_CODE, 10000);
                jSONObject.put(RespUtil.UniSdkField.RESP_MSG, "\u672a\u77e5\u9519\u8bef");
                extendFuncCall(jSONObject.toString());
            } catch (JSONException unused2) {
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x0092  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00ae  */
    /* JADX WARN: Removed duplicated region for block: B:50:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String catUidRoleidServer(java.lang.String r10) throws java.io.UnsupportedEncodingException {
        /*
            r9 = this;
            java.lang.String r0 = "UTF-8"
            java.lang.String r1 = ""
            com.netease.ntunisdk.base.GamerInterface r2 = com.netease.ntunisdk.base.SdkMgr.getInst()     // Catch: java.io.UnsupportedEncodingException -> L34
            java.lang.String r3 = "FULL_UIN"
            java.lang.String r2 = r2.getPropStr(r3, r1)     // Catch: java.io.UnsupportedEncodingException -> L34
            java.lang.String r2 = java.net.URLEncoder.encode(r2, r0)     // Catch: java.io.UnsupportedEncodingException -> L34
            com.netease.ntunisdk.base.GamerInterface r3 = com.netease.ntunisdk.base.SdkMgr.getInst()     // Catch: java.io.UnsupportedEncodingException -> L31
            java.lang.String r4 = "USERINFO_UID"
            java.lang.String r3 = r3.getPropStr(r4, r1)     // Catch: java.io.UnsupportedEncodingException -> L31
            java.lang.String r3 = java.net.URLEncoder.encode(r3, r0)     // Catch: java.io.UnsupportedEncodingException -> L31
            com.netease.ntunisdk.base.GamerInterface r4 = com.netease.ntunisdk.base.SdkMgr.getInst()     // Catch: java.io.UnsupportedEncodingException -> L2f
            java.lang.String r5 = "USERINFO_HOSTID"
            java.lang.String r4 = r4.getPropStr(r5, r1)     // Catch: java.io.UnsupportedEncodingException -> L2f
            java.lang.String r1 = java.net.URLEncoder.encode(r4, r0)     // Catch: java.io.UnsupportedEncodingException -> L2f
            goto L3a
        L2f:
            r0 = move-exception
            goto L37
        L31:
            r0 = move-exception
            r3 = r1
            goto L37
        L34:
            r0 = move-exception
            r2 = r1
            r3 = r2
        L37:
            r0.printStackTrace()
        L3a:
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r0.<init>()
            android.net.Uri r4 = android.net.Uri.parse(r10)
            java.util.Set r4 = r4.getQueryParameterNames()
            java.lang.String r5 = "&server="
            java.lang.String r6 = "&role_id="
            java.lang.String r7 = "&uid="
            if (r4 == 0) goto L92
            int r8 = r4.size()
            if (r8 <= 0) goto L92
            java.lang.String r8 = "uid"
            boolean r8 = r4.contains(r8)
            if (r8 != 0) goto L69
            boolean r8 = android.text.TextUtils.isEmpty(r2)
            if (r8 != 0) goto L69
            r0.append(r7)
            r0.append(r2)
        L69:
            java.lang.String r2 = "role_id"
            boolean r2 = r4.contains(r2)
            if (r2 != 0) goto L7d
            boolean r2 = android.text.TextUtils.isEmpty(r3)
            if (r2 != 0) goto L7d
            r0.append(r6)
            r0.append(r3)
        L7d:
            java.lang.String r2 = "server"
            boolean r2 = r4.contains(r2)
            if (r2 != 0) goto La4
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 != 0) goto La4
            r0.append(r5)
            r0.append(r1)
            goto La4
        L92:
            r0.append(r7)
            r0.append(r2)
            r0.append(r6)
            r0.append(r3)
            r0.append(r5)
            r0.append(r1)
        La4:
            java.lang.String r0 = r0.toString()
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 != 0) goto Lfc
            java.lang.String r1 = "?"
            boolean r2 = r10.contains(r1)
            r3 = 1
            if (r2 == 0) goto Le6
            int r1 = r10.indexOf(r1)
            int r2 = r10.length()
            int r2 = r2 - r3
            if (r1 != r2) goto Ld6
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r10)
            java.lang.String r10 = r0.substring(r3)
            r1.append(r10)
            java.lang.String r10 = r1.toString()
            goto Lfc
        Ld6:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r10)
            r1.append(r0)
            java.lang.String r10 = r1.toString()
            goto Lfc
        Le6:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r10)
            r2.append(r1)
            java.lang.String r10 = r0.substring(r3)
            r2.append(r10)
            java.lang.String r10 = r2.toString()
        Lfc:
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.SdkNgWebview.catUidRoleidServer(java.lang.String):java.lang.String");
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

    public static IPCWebViewManager getmIPCManager() {
        NgWebviewLog.d(TAG, "getmIPCManager\u3002\u3002\u3002", new Object[0]);
        return mIPCManager;
    }

    public void setmIPCManager(IPCWebViewManager iPCWebViewManager) {
        mIPCManager = iPCWebViewManager;
    }

    private void openNgWebview(JSONObject jSONObject, boolean z) throws JSONException, ClassNotFoundException, UnsupportedEncodingException {
        int propInt;
        String str;
        int i;
        int propInt2;
        int i2;
        String str2;
        String str3;
        int i3;
        String str4;
        int i4;
        int i5;
        int i6;
        String str5;
        int i7;
        String str6;
        Intent intent;
        NgWebviewLog.d(TAG, "NGWebViewOpenURL isWebviewShow: " + z, new Object[0]);
        this.isSingleProcess = jSONObject.optString("isSingleProcess");
        this.isSingleInstance = jSONObject.optString("isSingleInstance");
        if (!z && "1".equals(this.isSingleInstance)) {
            NgWebviewActivity ngWebviewActivity = NgWebviewActivity.getInstance();
            if (ngWebviewActivity == null) {
                NgWebviewLog.d(TAG, "NgWebviewActivity is null", new Object[0]);
            } else {
                ngWebviewActivity.closeWebview("OverrideClose");
            }
        }
        this.openJson = jSONObject.toString();
        String strOptString = jSONObject.optString("URLString");
        NgWebviewLog.d(TAG, "URLString=" + strOptString, new Object[0]);
        WebviewParams webviewParams = new WebviewParams();
        if (TextUtils.isEmpty(strOptString)) {
            NgWebviewLog.d(TAG, "URLString is empty", new Object[0]);
            try {
                jSONObject.put(RespUtil.UniSdkField.RESP_CODE, 2);
                jSONObject.put(RespUtil.UniSdkField.RESP_MSG, "URLString required");
                extendFuncCall(jSONObject.toString());
                return;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        String strOptString2 = jSONObject.optString("scriptVersion");
        String strOptString3 = jSONObject.optString("isFullScreen", "1");
        String strOptString4 = jSONObject.optString("navigationBarVisible");
        int iOptInt = jSONObject.optInt("origin_x");
        int iOptInt2 = jSONObject.optInt("origin_y");
        int iOptInt3 = jSONObject.optInt("cls_btn_origin_x");
        int iOptInt4 = jSONObject.optInt("cls_btn_origin_y");
        int iOptInt5 = jSONObject.optInt("cls_btn_width");
        int iOptInt6 = jSONObject.optInt("cls_btn_height");
        int iOptInt7 = jSONObject.optInt("width");
        int iOptInt8 = jSONObject.optInt("height");
        int propInt3 = iOptInt;
        if (iOptInt7 <= 0 || iOptInt8 <= 0) {
            propInt = iOptInt7;
            str = strOptString3;
            i = iOptInt8;
        } else {
            propInt = iOptInt7;
            i = iOptInt8;
            str = "0";
        }
        int iOptInt9 = jSONObject.optInt(VideoView.EVENT_PROP_ORIENTATION);
        if (str.equals("1") && strOptString4.equals("1")) {
            jSONObject.optString("blackBorderVisible", "1");
        } else {
            jSONObject.optString("blackBorderVisible");
        }
        String strOptString5 = jSONObject.optString("blackBorderColor");
        String strOptString6 = jSONObject.optString("qstn_close_btn");
        String strOptString7 = jSONObject.optString("closeButtonVisible");
        String strOptString8 = jSONObject.optString("supportBackKey");
        String strOptString9 = jSONObject.optString("secureVerify");
        String strOptString10 = jSONObject.optString("additionalUserAgent");
        String strOptString11 = jSONObject.optString("h5_padding");
        String propStr = SdkMgr.getInst().getPropStr("WEBVIEW_CONTENT_TYPE");
        if (strOptString10.contains("Unisdk/2.0")) {
            strOptString10 = strOptString10.replace("Unisdk/2.0", "Unisdk/2.1");
        } else if (!strOptString10.contains("Unisdk/2.1")) {
            strOptString10 = " Unisdk/2.1 NetType/" + NetConnectivity.getNetworkType(this.myCtx) + " os/android" + Build.VERSION.SDK_INT + " ngwebview/" + getSDKVersion() + " package_name/" + this.myCtx.getApplicationContext().getPackageName() + " udid/" + SdkMgr.getInst().getUdid() + " " + strOptString10;
        }
        Boolean bool = false;
        if (strOptString.contains("survey.163.com") || strOptString.contains("survey.netease.com") || strOptString.contains("survey.easebar.com") || strOptString.contains("research.163.com") || strOptString.contains("research.easebar.com") || strOptString.contains("survey-game.163.com") || strOptString.contains("research-game.163.com") || strOptString.contains("research-game.easebar.com") || "SURVEY".equals(propStr)) {
            NgWebviewLog.d(TAG, "questionnaire handle.", new Object[0]);
            String strCatUidRoleidServer = catUidRoleidServer(strOptString);
            StringBuilder sb = new StringBuilder();
            bool = true;
            sb.append("catUidRoleidServer.URLString=");
            sb.append(strCatUidRoleidServer);
            NgWebviewLog.d(TAG, sb.toString(), new Object[0]);
            int propInt4 = SdkMgr.getInst().getPropInt("WEBVIEW_ORIENTATION", 0);
            String propStr2 = !TextUtils.isEmpty(SdkMgr.getInst().getPropStr("WEBVIEW_H5_PADDING")) ? SdkMgr.getInst().getPropStr("WEBVIEW_H5_PADDING") : strOptString11;
            if (!TextUtils.isEmpty(SdkMgr.getInst().getPropStr("WEBVIEW_BLACKBORDERVISIBLE"))) {
                SdkMgr.getInst().getPropStr("WEBVIEW_BLACKBORDERVISIBLE");
            }
            strOptString5 = !TextUtils.isEmpty(SdkMgr.getInst().getPropStr("WEBVIEW_BLACKBORDERCOLOR")) ? SdkMgr.getInst().getPropStr("WEBVIEW_BLACKBORDERCOLOR") : strOptString5;
            if (SdkMgr.getInst().getPropInt("WEBVIEW_ORIGIN_X", -1) >= 0) {
                webviewParams.setSetSurveyXY(true);
                propInt3 = SdkMgr.getInst().getPropInt("WEBVIEW_ORIGIN_X", -1);
            }
            if (SdkMgr.getInst().getPropInt("WEBVIEW_ORIGIN_Y", -1) >= 0) {
                webviewParams.setSetSurveyXY(true);
                propInt2 = SdkMgr.getInst().getPropInt("WEBVIEW_ORIGIN_Y", -1);
            } else {
                propInt2 = iOptInt2;
            }
            int i8 = propInt2;
            if (SdkMgr.getInst().getPropInt("WEBVIEW_WIDTH", -1) >= 0) {
                propInt = SdkMgr.getInst().getPropInt("WEBVIEW_WIDTH", -1);
            }
            if (SdkMgr.getInst().getPropInt("WEBVIEW_HEIGHT", -1) >= 0) {
                int propInt5 = SdkMgr.getInst().getPropInt("WEBVIEW_HEIGHT", -1);
                str5 = "WEBVIEW_H5_PADDING";
                i2 = propInt3;
                i7 = i8;
                str2 = propStr2;
                i3 = propInt4;
                str4 = TAG;
                i4 = propInt;
                i5 = iOptInt5;
                i6 = propInt5;
                str3 = strCatUidRoleidServer;
            } else {
                i2 = propInt3;
                str2 = propStr2;
                str3 = strCatUidRoleidServer;
                i3 = propInt4;
                str4 = TAG;
                i4 = propInt;
                i5 = iOptInt5;
                i6 = i;
                str5 = "WEBVIEW_H5_PADDING";
                i7 = i8;
            }
        } else {
            str3 = strOptString;
            i2 = propInt3;
            i3 = iOptInt9;
            str2 = strOptString11;
            str4 = TAG;
            i4 = propInt;
            i5 = iOptInt5;
            i6 = i;
            str5 = "WEBVIEW_H5_PADDING";
            i7 = iOptInt2;
        }
        String str7 = strOptString5;
        if (!TextUtils.isEmpty(str2) && "1".equals(str) && handleSpecialModel()) {
            str6 = str;
            strOptString10 = strOptString10 + " uni_padding/" + paddingPx2dip(this.mContext, str2);
        } else {
            str6 = str;
        }
        String strOptString12 = jSONObject.optString("intercept_schemes");
        if (TextUtils.isEmpty(strOptString12)) {
            strOptString12 = jSONObject.optString("handle_schemes");
        }
        String strOptString13 = jSONObject.optString("webviewCtx", "");
        webviewParams.setBlackBorderColor(str7);
        webviewParams.setSingleProcess("1".equals(this.isSingleProcess));
        webviewParams.setSingleInstance("1".equals(this.isSingleInstance));
        webviewParams.setHasPdfView(this.isHasPdfView);
        webviewParams.setWebviewCtx(strOptString13);
        webviewParams.setSurvey(bool.booleanValue());
        webviewParams.setSecureVerify("1".equals(strOptString9));
        webviewParams.setUrl(str3);
        webviewParams.setScriptVersion(strOptString2);
        webviewParams.setOriginX(i2);
        webviewParams.setOriginY(i7);
        webviewParams.setWidth(i4);
        webviewParams.setHeight(i6);
        webviewParams.setCloseBtnOriginX(iOptInt3);
        webviewParams.setCloseBtnOriginY(iOptInt4);
        webviewParams.setCloseBtnWidth(i5);
        webviewParams.setCloseBtnHeight(iOptInt6);
        webviewParams.setOrientation(i3);
        webviewParams.setAdditionalUserAgent(strOptString10);
        webviewParams.setNavigationBarVisible("1".equals(strOptString4));
        webviewParams.setQstnCloseBtnVisible("1".equals(strOptString6));
        webviewParams.setCloseButtonVisible("1".equals(strOptString7));
        webviewParams.setSupportBackKey("1".equals(strOptString8));
        webviewParams.setIntercept_schemes(strOptString12);
        String str8 = str6;
        webviewParams.setFullScreen("1".equals(str8));
        webviewParams.setFullScreenNoAdaptive("2".equals(str8));
        webviewParams.setIsModule(false);
        webviewParams.setHasCutout(CutoutUtil.hasCutout((Activity) this.myCtx));
        webviewParams.setWebviewBackgroundColor(SdkMgr.getInst().getPropStr(ConstProp.WEBVIEW_BKCOLOR));
        webviewParams.setYYGameID(SdkMgr.getInst().getPropStr(ConstProp.YY_GAMEID));
        webviewParams.setChannelID(SdkMgr.getInst().getChannel());
        webviewParams.setAppVersionName(UniSdkUtils.getAppVersionName(this.myCtx));
        webviewParams.setCutoutHeight(this.cutoutHeight);
        webviewParams.setCutoutWidth(this.cutoutWidth);
        if ("1".equals(this.isSingleInstance)) {
            NgWebviewLog.d(str4, "singleInstance mode", new Object[0]);
            intent = new Intent(this.myCtx, (Class<?>) NgWebviewActivityEx2.class);
        } else {
            String str9 = str4;
            if (!"1".equals(this.isSingleProcess)) {
                NgWebviewLog.d(str9, "isSingleProcess mode", new Object[0]);
                intent = new Intent(this.myCtx, (Class<?>) NgWebviewActivityEx.class);
                IPCWebViewManager iPCWebViewManager = mIPCManager;
                if (iPCWebViewManager != null) {
                    iPCWebViewManager.unbindService(this.myCtx);
                    mIPCManager = null;
                }
                mIPCManager = new IPCWebViewManager();
                mIPCManager.bindService(this.myCtx);
            } else {
                NgWebviewLog.d(str9, "default mode", new Object[0]);
                intent = new Intent(this.myCtx, (Class<?>) NgWebviewActivity.class);
            }
        }
        intent.setFlags(268435456);
        intent.putExtra("WebviewParams", webviewParams);
        HookManager.startActivity(this.myCtx, intent);
        SdkMgr.getInst().setPropStr("WEBVIEW_CONTENT_TYPE", "");
        SdkMgr.getInst().setPropStr(ConstProp.WEBVIEW_FULLFIT, "0");
        SdkMgr.getInst().setPropStr(ConstProp.WEBVIEW_CLBTN, "0");
        SdkMgr.getInst().setPropStr(str5, "");
    }
}