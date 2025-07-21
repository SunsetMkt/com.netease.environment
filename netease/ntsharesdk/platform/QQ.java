package com.netease.ntsharesdk.platform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.netease.ntsharesdk.Platform;
import com.netease.ntsharesdk.ShareArgs;
import com.netease.ntunisdk.base.SdkMgr;
import com.tencent.open.miniapp.MiniApp;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class QQ extends Platform {
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 31155;
    private static QQ QQinst = new QQ();
    private Tencent api;
    private ShareArgs argsn;
    public String initContextString;
    IUiListener qqShareListener;

    @Override // com.netease.ntsharesdk.Platform
    protected String getPlatformName() {
        return "QQ";
    }

    @Override // com.netease.ntsharesdk.Platform
    public void handleIntent(Intent intent) {
    }

    @Override // com.netease.ntsharesdk.Platform
    public void handleResponse(Object obj) {
    }

    public static QQ getInst() {
        return QQinst;
    }

    public QQ() {
        this.initContextString = "";
        this.qqShareListener = new IUiListener() { // from class: com.netease.ntsharesdk.platform.QQ.1
            @Override // com.tencent.tauth.IUiListener
            public void onWarning(int i) {
            }

            @Override // com.tencent.tauth.IUiListener
            public void onCancel() {
                Platform.dLog("QQShare.onCancel");
                ShareArgs shareArgs = new ShareArgs();
                shareArgs.setFailMsg("onCancel");
                if (QQ.this.shareEndListener != null) {
                    QQ.this.shareEndListener.onShareEnd(QQ.this.getPlatformName(), 1, shareArgs);
                }
            }

            @Override // com.tencent.tauth.IUiListener
            public void onComplete(Object obj) {
                Platform.dLog("QQShare.onComplete");
                Platform.dLog("qq share ok");
                if (QQ.this.shareEndListener != null) {
                    QQ.this.shareEndListener.onShareEnd(QQ.this.getPlatformName(), 0, null);
                }
            }

            @Override // com.tencent.tauth.IUiListener
            public void onError(UiError uiError) {
                Platform.dLog("QQShare.onError");
                ShareArgs shareArgs = new ShareArgs();
                shareArgs.setFailMsg(uiError.errorMessage);
                Platform.dLog("error:" + uiError.errorMessage);
                if (QQ.this.shareEndListener != null) {
                    QQ.this.shareEndListener.onShareEnd(QQ.this.getPlatformName(), 2, shareArgs);
                }
            }
        };
    }

    public QQ(Context context) {
        super(context);
        this.initContextString = "";
        this.qqShareListener = new IUiListener() { // from class: com.netease.ntsharesdk.platform.QQ.1
            @Override // com.tencent.tauth.IUiListener
            public void onWarning(int i) {
            }

            @Override // com.tencent.tauth.IUiListener
            public void onCancel() {
                Platform.dLog("QQShare.onCancel");
                ShareArgs shareArgs = new ShareArgs();
                shareArgs.setFailMsg("onCancel");
                if (QQ.this.shareEndListener != null) {
                    QQ.this.shareEndListener.onShareEnd(QQ.this.getPlatformName(), 1, shareArgs);
                }
            }

            @Override // com.tencent.tauth.IUiListener
            public void onComplete(Object obj) {
                Platform.dLog("QQShare.onComplete");
                Platform.dLog("qq share ok");
                if (QQ.this.shareEndListener != null) {
                    QQ.this.shareEndListener.onShareEnd(QQ.this.getPlatformName(), 0, null);
                }
            }

            @Override // com.tencent.tauth.IUiListener
            public void onError(UiError uiError) {
                Platform.dLog("QQShare.onError");
                ShareArgs shareArgs = new ShareArgs();
                shareArgs.setFailMsg(uiError.errorMessage);
                Platform.dLog("error:" + uiError.errorMessage);
                if (QQ.this.shareEndListener != null) {
                    QQ.this.shareEndListener.onShareEnd(QQ.this.getPlatformName(), 2, shareArgs);
                }
            }
        };
    }

    @Override // com.netease.ntsharesdk.Platform
    protected Object genMessage(ShareArgs shareArgs) {
        Bundle bundle = new Bundle();
        if ("TYPE_IMAGE".equals(shareArgs.getValue("type"))) {
            dLog("QQShare.SHARE_TO_QQ_TYPE_IMAGE");
            bundle.putInt("req_type", 5);
            bundle.putString("imageLocalUrl", shareArgs.getValue(ShareArgs.IMG_PATH).toString());
        } else {
            dLog("QQShare.SHARE_TO_QQ_TYPE_DEFAULT");
            bundle.putInt("req_type", 1);
            bundle.putString("title", shareArgs.getValue("title").toString());
            bundle.putString("targetUrl", shareArgs.getValue("url").toString());
            if (shareArgs.getValue("text") != null) {
                bundle.putString("summary", shareArgs.getValue("text").toString());
            }
            if (shareArgs.getValue(ShareArgs.IMG_PATH) != null) {
                bundle.putString("imageUrl", shareArgs.getValue(ShareArgs.IMG_PATH).toString());
            }
        }
        if (shareArgs.getValue(ShareArgs.TO_BLOG) != null && shareArgs.getValue(ShareArgs.TO_BLOG).toString().length() > 0) {
            dLog("args.getValue(ShareArgs.TO_BLOG) is not empty");
            bundle.putInt("cflag", 1);
        } else if (SdkMgr.getInst().getPropInt("NGSHARE_QQSHARE_QZONE_FORBID", 0) == 1) {
            bundle.putInt("cflag", 2);
        }
        return bundle;
    }

    @Override // com.netease.ntsharesdk.Platform
    public void share(ShareArgs shareArgs, Activity activity) {
        this.argsn = shareArgs;
        if (activity == null) {
            shareArgs.setFailMsg("Activity null!");
            dLog("Activity null!");
            if (this.shareEndListener != null) {
                this.shareEndListener.onShareEnd(getPlatformName(), 2, shareArgs);
                return;
            }
            return;
        }
        if (shareArgs != null && ShareArgs.TYPE_TO_MINI_PROGRAM.equals(shareArgs.getValue("type"))) {
            String str = (String) shareArgs.getValue(ShareArgs.USER_NAME);
            String str2 = (String) shareArgs.getValue(ShareArgs.PATH);
            String str3 = (String) shareArgs.getValue(ShareArgs.MINI_PROGRAM_TYPE);
            String str4 = "release";
            if (!"3".equals(str3) && !"release".equals(str3)) {
                if ("1".equals(str3) || MiniApp.MINIAPP_VERSION_TRIAL.equals(str3)) {
                    str4 = MiniApp.MINIAPP_VERSION_TRIAL;
                } else if ("0".equals(str3) || MiniApp.MINIAPP_VERSION_DEVELOP.equals(str3)) {
                    str4 = MiniApp.MINIAPP_VERSION_DEVELOP;
                }
            }
            dLog("miniAppVersion:" + str4);
            dLog("\u8c03\u7528\u7248\u672c\u4e3arelease\u6b63\u5f0f\u7248,develop\u5f00\u53d1\u7248,trial\u4f53\u9a8c\u7248\uff1a" + shareArgs.getValue(ShareArgs.MINI_PROGRAM_TYPE));
            this.api.startMiniApp((Activity) this.myCtx, str, str2, str4);
            return;
        }
        if (!checkArgs(shareArgs).booleanValue()) {
            dLog("checkArgs(args) false");
            if (this.shareEndListener != null) {
                this.shareEndListener.onShareEnd(getPlatformName(), 2, shareArgs);
                return;
            }
            return;
        }
        Bundle bundle = (Bundle) genMessage(shareArgs);
        dLog("QQ share act:" + activity.toString());
        dLog("QQ share bundle:" + bundle.toString());
        this.api.shareToQQ(activity, bundle, this.qqShareListener);
    }

    @Override // com.netease.ntsharesdk.Platform
    public void share(ShareArgs shareArgs) {
        share(shareArgs, null);
    }

    @Override // com.netease.ntsharesdk.Platform
    public Boolean checkArgs(ShareArgs shareArgs) {
        return true;
    }

    @Override // com.netease.ntsharesdk.Platform
    public void handleActivityResult(int i, int i2, Intent intent) {
        dLog("QQ handleActivityResult");
        super.handleActivityResult(i, i2, intent);
        if (i != 10103 || this.shareEndListener == null) {
            return;
        }
        Tencent.onActivityResultData(i, i2, intent, this.qqShareListener);
    }

    @Override // com.netease.ntsharesdk.Platform
    public void initSdk(Context context) {
        dLog("initContextString:" + this.initContextString);
        dLog("ctx:" + context.toString());
        if (this.hasInit && this.initContextString.equals(context.toString())) {
            return;
        }
        this.myCtx = context;
        this.mConf = readConfig(this.myCtx, getPlatformName());
        String config = getConfig("app_id");
        if (!TextUtils.isEmpty(SdkMgr.getInst().getPropStr("NG_SHARE_ALL_CHANNEL_CONFIGURATION"))) {
            try {
                JSONObject jSONObjectOptJSONObject = new JSONObject(SdkMgr.getInst().getPropStr("NG_SHARE_ALL_CHANNEL_CONFIGURATION")).optJSONObject("QQ");
                if (jSONObjectOptJSONObject != null && !TextUtils.isEmpty(jSONObjectOptJSONObject.optString("app_id"))) {
                    config = jSONObjectOptJSONObject.optString("app_id");
                }
            } catch (Exception unused) {
                dLog(getPlatformName() + " CONFIGURATION error");
            }
        }
        dLog("platform: " + getPlatformName() + " init sdk app_id:" + config);
        Context context2 = this.myCtx;
        StringBuilder sb = new StringBuilder();
        sb.append(this.myCtx.getPackageName());
        sb.append(".uningshare.fileprovider");
        this.api = Tencent.createInstance(config, context2, sb.toString());
        this.hasInit = true;
        this.initContextString = context.toString();
        Tencent.setIsPermissionGranted(true);
    }

    @Override // com.netease.ntsharesdk.Platform
    public Object getAPIInst() {
        return this.api;
    }

    @Override // com.netease.ntsharesdk.Platform
    public boolean hasPlatformInstall(String str) {
        try {
            if (this.api != null) {
                dLog("hasPlatformInstall QQ:" + this.api.isQQInstalled(this.myCtx));
                return this.api.isQQInstalled(this.myCtx);
            }
        } catch (Exception e) {
            dLog("hasPlatformInstall Exception:" + e.getMessage());
        }
        dLog("hasPlatformInstall api null");
        return false;
    }

    @Override // com.netease.ntsharesdk.Platform
    public void updateApi(String str, Context context) {
        this.myCtx = context;
        this.mConf = readConfig(this.myCtx, getPlatformName());
        this.api = Tencent.createInstance(str, this.myCtx);
        this.hasInit = true;
        this.initContextString = context.toString();
        Tencent.setIsPermissionGranted(true);
    }
}