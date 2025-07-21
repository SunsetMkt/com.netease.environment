package com.netease.ntunisdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;
import com.netease.ntsharesdk.OnShareEndListener;
import com.netease.ntsharesdk.Platform;
import com.netease.ntsharesdk.ShareArgs;
import com.netease.ntsharesdk.ShareMgr;
import com.netease.ntsharesdk.platform.Weibo;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.OnFinishInitListener;
import com.netease.ntunisdk.base.OnShareListener;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.ShareInfo;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.cutout.RespUtil;
import com.netease.ntunisdk.unilogger.global.Const;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.tencent.connect.common.Constants;
import com.tencent.open.SocialConstants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.xiaomi.gamecenter.sdk.report.SDefine;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class SdkNGShare extends SdkBase {
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 31155;
    public static final String QQ_AUTH_TYPE = "getAuthInfo";
    public static final String QQ_CALLAPI_TYPE = "qqCallCommonApi";
    public static final String QQ_GOTO_SETTING_TYPE = "qqGotoSetting";
    private static final String TAG = "UniSDK ngshare";
    private static Bitmap bmp;
    private JSONObject QQAuthObj;
    private String QQFuncType;
    private Tencent api;
    private ShareArgs args;
    private IWBAPI mWBAPI;
    private OnShareListener originalListener;
    private String platform;
    IUiListener qqListener;
    private String qq_appid;
    private String weibo_appid;
    private String weibo_url;

    public static String getChannelSts() {
        return "ngshare";
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void checkOrder(OrderInfo orderInfo) {
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getSDKVersion() {
        return Platform.Version;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    protected String getUniSDKVersion() {
        return Platform.Version;
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

    public SdkNGShare(Context context) {
        super(context);
        this.originalListener = null;
        this.qqListener = new IUiListener() { // from class: com.netease.ntunisdk.SdkNGShare.2
            @Override // com.tencent.tauth.IUiListener
            public void onComplete(Object obj) throws JSONException {
                try {
                    UniSdkUtils.d(SdkNGShare.TAG, "onComplete:");
                    UniSdkUtils.d(SdkNGShare.TAG, "onComplete:" + obj.toString());
                    UniSdkUtils.d(SdkNGShare.TAG, "QQFuncType:" + SdkNGShare.this.QQFuncType);
                    if (SdkNGShare.QQ_AUTH_TYPE.equals(SdkNGShare.this.QQFuncType)) {
                        if (obj == null) {
                            SdkNGShare.this.QQAuthObj.put("errorMsg", "QQ authorize result error");
                            SdkNGShare.this.QQAuthObj.put(RespUtil.UniSdkField.RESP_CODE, 0);
                            SdkNGShare.this.QQAuthObj.put(RespUtil.UniSdkField.RESP_MSG, "success");
                            SdkNGShare.this.extendFuncCall(SdkNGShare.this.QQAuthObj.toString());
                        } else {
                            JSONObject jSONObject = (JSONObject) obj;
                            UniSdkUtils.d(SdkNGShare.TAG, "AccessToken:" + jSONObject.optString(Constants.PARAM_ACCESS_TOKEN));
                            UniSdkUtils.d(SdkNGShare.TAG, "OpenId:" + jSONObject.optString("openid"));
                            SdkNGShare.this.QQAuthObj.put("uid", jSONObject.optString("openid"));
                            SdkNGShare.this.QQAuthObj.put("accessToken", jSONObject.optString(Constants.PARAM_ACCESS_TOKEN));
                            SdkNGShare.this.QQAuthObj.put("expiresTime", jSONObject.optLong(Constants.PARAM_EXPIRES_TIME));
                            SdkNGShare.this.QQAuthObj.put(RespUtil.UniSdkField.RESP_CODE, 0);
                            SdkNGShare.this.QQAuthObj.put(RespUtil.UniSdkField.RESP_MSG, "success");
                            SdkNGShare.this.api.setAccessToken(jSONObject.optString(Constants.PARAM_ACCESS_TOKEN), jSONObject.optString(Constants.PARAM_EXPIRES_IN));
                            SdkNGShare.this.api.setOpenId(jSONObject.optString("openid"));
                            SdkNGShare.this.extendFuncCall(SdkNGShare.this.QQAuthObj.toString());
                        }
                    } else if (SdkNGShare.QQ_GOTO_SETTING_TYPE.equals(SdkNGShare.this.QQFuncType)) {
                        if (obj == null) {
                            SdkNGShare.this.QQAuthObj.put("result", 1);
                            SdkNGShare.this.QQAuthObj.put(RespUtil.UniSdkField.RESP_CODE, 0);
                            SdkNGShare.this.QQAuthObj.put(RespUtil.UniSdkField.RESP_MSG, "success");
                            SdkNGShare.this.extendFuncCall(SdkNGShare.this.QQAuthObj.toString());
                        } else {
                            JSONObject jSONObject2 = (JSONObject) obj;
                            if (!TextUtils.isEmpty(jSONObject2.optString(Constants.PARAM_ACCESS_TOKEN)) && !TextUtils.isEmpty(jSONObject2.optString(Constants.PARAM_EXPIRES_IN)) && !TextUtils.isEmpty(jSONObject2.optString("openid"))) {
                                SdkNGShare.this.api.setAccessToken(jSONObject2.optString(Constants.PARAM_ACCESS_TOKEN), jSONObject2.optString(Constants.PARAM_EXPIRES_IN));
                                SdkNGShare.this.api.setOpenId(jSONObject2.optString("openid"));
                                SdkNGShare.this.QQAuthObj.put("result", 0);
                                SdkNGShare.this.QQAuthObj.put(RespUtil.UniSdkField.RESP_CODE, 0);
                                SdkNGShare.this.QQAuthObj.put(RespUtil.UniSdkField.RESP_MSG, "success");
                                SdkNGShare.this.extendFuncCall(SdkNGShare.this.QQAuthObj.toString());
                            }
                        }
                    } else if (SdkNGShare.QQ_CALLAPI_TYPE.equals(SdkNGShare.this.QQFuncType)) {
                        if (obj == null) {
                            SdkNGShare.this.QQAuthObj.put("errorMsg", "QQ call api failed");
                            SdkNGShare.this.QQAuthObj.put(RespUtil.UniSdkField.RESP_CODE, 0);
                            SdkNGShare.this.QQAuthObj.put(RespUtil.UniSdkField.RESP_MSG, "success");
                            SdkNGShare.this.extendFuncCall(SdkNGShare.this.QQAuthObj.toString());
                        } else {
                            SdkNGShare.this.QQAuthObj.put("result", ((JSONObject) obj).toString());
                            SdkNGShare.this.QQAuthObj.put(RespUtil.UniSdkField.RESP_CODE, 0);
                            SdkNGShare.this.QQAuthObj.put(RespUtil.UniSdkField.RESP_MSG, "success");
                            SdkNGShare.this.extendFuncCall(SdkNGShare.this.QQAuthObj.toString());
                        }
                    }
                    SdkNGShare.this.QQAuthObj = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override // com.tencent.tauth.IUiListener
            public void onError(UiError uiError) throws JSONException {
                try {
                    UniSdkUtils.d(SdkNGShare.TAG, "onError:" + uiError.errorCode + ";" + uiError.errorMessage);
                    SdkNGShare.this.QQAuthObj.put("errorMsg", "QQ authorize failed");
                    SdkNGShare.this.QQAuthObj.put(RespUtil.UniSdkField.RESP_CODE, 0);
                    SdkNGShare.this.QQAuthObj.put(RespUtil.UniSdkField.RESP_MSG, "success");
                    SdkNGShare.this.extendFuncCall(SdkNGShare.this.QQAuthObj.toString());
                    SdkNGShare.this.QQAuthObj = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override // com.tencent.tauth.IUiListener
            public void onCancel() throws JSONException {
                UniSdkUtils.d(SdkNGShare.TAG, "onCancel:");
                try {
                    SdkNGShare.this.QQAuthObj.put("errorMsg", "QQ authorize cancel");
                    SdkNGShare.this.QQAuthObj.put(RespUtil.UniSdkField.RESP_CODE, 0);
                    SdkNGShare.this.QQAuthObj.put(RespUtil.UniSdkField.RESP_MSG, "success");
                    SdkNGShare.this.extendFuncCall(SdkNGShare.this.QQAuthObj.toString());
                    SdkNGShare.this.QQAuthObj = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override // com.tencent.tauth.IUiListener
            public void onWarning(int i) throws JSONException {
                try {
                    SdkNGShare.this.QQAuthObj.put("errorMsg", "QQ authorize warning");
                    SdkNGShare.this.QQAuthObj.put(RespUtil.UniSdkField.RESP_CODE, 0);
                    SdkNGShare.this.QQAuthObj.put(RespUtil.UniSdkField.RESP_MSG, "success");
                    SdkNGShare.this.extendFuncCall(SdkNGShare.this.QQAuthObj.toString());
                    SdkNGShare.this.QQAuthObj = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        setPropInt(ConstProp.INNER_MODE_SECOND_CHANNEL, 1);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void init(OnFinishInitListener onFinishInitListener) {
        UniSdkUtils.d(TAG, "init...");
        setPropInt(ConstProp.INNER_MODE_NO_PAY, 1);
        setPropInt(ConstProp.MODE_HAS_SHARE, 1);
        setPropInt(SdkQRCode.ENABLE_UNISDK_PERMISSION_TIPS, 0);
        ShareMgr.getInst().setContext(this.myCtx);
        ShareMgr.getInst().setShareEndListener(new OnShareEndListener() { // from class: com.netease.ntunisdk.SdkNGShare.1
            @Override // com.netease.ntsharesdk.OnShareEndListener
            public void onShareEnd(String str, int i, ShareArgs shareArgs) {
                Object[] objArr = new Object[3];
                objArr[0] = str;
                objArr[1] = Integer.valueOf(i);
                objArr[2] = shareArgs == null ? "" : shareArgs.getFailMsg();
                UniSdkUtils.d(SdkNGShare.TAG, String.format("pf:%s,result:%s, failmsg:%s", objArr));
                SdkMgr.getInst().setPropStr(ConstProp.CHANNEL_ID, str);
                if (shareArgs != null && shareArgs.getValue("MINI_RESPONSE") != null) {
                    SdkMgr.getInst().setPropStr("MINI_RESPONSE", (String) shareArgs.getValue("MINI_RESPONSE"));
                }
                if (shareArgs != null && shareArgs.getFailMsg() != null) {
                    SdkMgr.getInst().setPropStr(ConstProp.NT_CALLBACK_MESSAGE, shareArgs.getFailMsg());
                }
                if (i == 0) {
                    SdkNGShare.this.shareFinished(true);
                } else {
                    SdkNGShare.this.shareFinished(false);
                }
            }
        });
        onFinishInitListener.finishInit(0);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void login() {
        setPropStr(ConstProp.UID, "NGSshareUid");
        setPropStr(ConstProp.SESSION, "NGSshareSession");
        setPropInt(ConstProp.LOGIN_STAT, 1);
        loginDone(0);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getLoginSession() {
        return hasLogin() ? getPropStr(ConstProp.SESSION) : ConstProp.S_NOT_LOGIN_SESSION;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getLoginUid() {
        return hasLogin() ? getPropStr(ConstProp.UID) : "";
    }

    @Override // com.netease.ntunisdk.base.SdkBase, com.netease.ntunisdk.base.GamerInterface
    public void exit() {
        super.exit();
    }

    @Override // com.netease.ntunisdk.base.SdkBase, com.netease.ntunisdk.base.GamerInterface
    public String getChannel() {
        return getChannelSts();
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnNewIntent(Intent intent) {
        ShareMgr.getInst().handleIntent(intent);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnActivityResult(int i, int i2, Intent intent) {
        UniSdkUtils.d(TAG, "sdkOnActivityResult...");
        try {
            ShareMgr.getInst().handleActivityResult(i, i2, intent);
            UniSdkUtils.i(TAG, "Weibo handleActivityResult, requestCode:" + i + ", resultCode:" + i2);
            if (this.mWBAPI != null && i == 32973 && this.myCtx != null && Weibo.getInst().callWeiboOauthResult) {
                UniSdkUtils.i(TAG, "mSsoHandler.authorizeCallBack");
                this.mWBAPI.authorizeCallback((Activity) this.myCtx, i, i2, intent);
                Weibo.getInst().callWeiboOauthResult = false;
            }
            if (i == 11101 && this.QQAuthObj != null) {
                UniSdkUtils.d(TAG, "REQUEST_LOGIN");
                Tencent.onActivityResultData(i, i2, intent, this.qqListener);
            }
            if (i != 10114 || this.QQAuthObj == null) {
                return;
            }
            UniSdkUtils.d(TAG, "REQUEST_COMMON_CHANNEL");
            Tencent.onActivityResultData(i, i2, intent, this.qqListener);
        } catch (Exception e) {
            UniSdkUtils.d(TAG, "share Exception:" + e.toString());
            e.printStackTrace();
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    protected String specialShareChannel(ShareInfo shareInfo) {
        return 301 == shareInfo.getShareChannel() ? getChannel() : "";
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void share(ShareInfo shareInfo) {
        UniSdkUtils.d(TAG, String.format("scope:%s, shareChannle:%s, title:%s, text:%s, comment:%s, imgPath:%s, url:%s, bitmap:%s, shareThumb:%s, type:%s", shareInfo.getScope(), Integer.valueOf(shareInfo.getShareChannel()), shareInfo.getTitle(), shareInfo.getText(), shareInfo.getDesc(), shareInfo.getImage(), shareInfo.getLink(), shareInfo.getShareBitmap(), shareInfo.getShareThumb(), shareInfo.getType()));
        if ("1".equals(SdkMgr.getInst().getPropStr("IS_RUNNING_CLOUD"))) {
            Toast.makeText(this.myCtx, !TextUtils.isEmpty(SdkMgr.getInst().getPropStr("NGSHARE_RUNNING_CLOUD_TOAST_TIPS")) ? SdkMgr.getInst().getPropStr("NGSHARE_RUNNING_CLOUD_TOAST_TIPS") : "\u4e91\u6e38\u620f\u6682\u4e0d\u652f\u6301\u8be5\u529f\u80fd", 0).show();
            return;
        }
        try {
            UniSdkUtils.i(TAG, "shareInfo has webURL:" + DownLoadUtil.hasWebUrl(shareInfo));
            if (DownLoadUtil.hasWebUrl(shareInfo)) {
                DownLoadUtil.webShare(this.myCtx, shareInfo);
                return;
            }
            this.platform = Platform.OTHER;
            if (301 == shareInfo.getShareChannel() || 101 == shareInfo.getShareChannel() || 102 == shareInfo.getShareChannel() || 118 == shareInfo.getShareChannel()) {
                this.platform = Platform.WEIXIN;
            } else if (105 == shareInfo.getShareChannel() || 106 == shareInfo.getShareChannel()) {
                this.platform = "QQ";
            } else {
                if (103 != shareInfo.getShareChannel() && 104 != shareInfo.getShareChannel()) {
                    if (100 == shareInfo.getShareChannel() || 117 == shareInfo.getShareChannel()) {
                        this.platform = Platform.WEIBO;
                    }
                }
                return;
            }
            this.args = genShareArgs(shareInfo);
            ShareMgr.getInst().share(this.args, this.platform, (Activity) this.myCtx);
        } catch (Exception e) {
            UniSdkUtils.d(TAG, "share Exception:" + e.toString());
            e.printStackTrace();
            shareFinished(false);
        }
    }

    private ShareArgs genShareArgs(ShareInfo shareInfo) {
        ShareArgs shareArgs = new ShareArgs();
        shareArgs.setValue("title", shareInfo.getTitle());
        shareArgs.setValue("text", shareInfo.getText());
        if ("TYPE_IMAGE".equalsIgnoreCase(shareInfo.getType()) && TextUtils.isEmpty(shareInfo.getImage()) && shareInfo.getShareBitmap() == null) {
            UniSdkUtils.d(TAG, "ShareInfo IMAGE TYPE not Match!!!");
        }
        if ("TYPE_LINK".equalsIgnoreCase(shareInfo.getType()) && TextUtils.isEmpty(shareInfo.getLink())) {
            UniSdkUtils.d(TAG, "ShareInfo LINK TYPE not Match!!!");
        }
        if ("TYPE_VIDEO".equalsIgnoreCase(shareInfo.getType()) && TextUtils.isEmpty(shareInfo.getVideoUrl())) {
            UniSdkUtils.d(TAG, "ShareInfo VIDEO TYPE not Match!!!");
        }
        if (ShareArgs.TYPE_TO_MINI_PROGRAM.equalsIgnoreCase(shareInfo.getType()) && TextUtils.isEmpty(shareInfo.getTemplateId())) {
            UniSdkUtils.d(TAG, "ShareInfo MINI_PROGRAM TYPE not Match!!!");
        }
        if ("TYPE_IMAGE".equals(shareInfo.getType()) || "TYPE_LINK".equals(shareInfo.getType()) || "TYPE_VIDEO".equals(shareInfo.getType()) || ShareArgs.TYPE_TO_MINI_PROGRAM.equals(shareInfo.getType()) || "TYPE_MINI_PROGRAM".equals(shareInfo.getType())) {
            shareArgs.setValue("type", shareInfo.getType());
        }
        if (!TextUtils.isEmpty(shareInfo.getDesc())) {
            shareArgs.setValue("comment", shareInfo.getDesc());
        }
        if (!TextUtils.isEmpty(shareInfo.getImage())) {
            UniSdkUtils.d(TAG, "!TextUtils.isEmpty(shareInfo.getImage())");
            if (shareInfo.getImage().startsWith("http")) {
                shareArgs.setValue(ShareArgs.IMG_URL, shareInfo.getImage());
            } else {
                shareArgs.setValue(ShareArgs.IMG_PATH, shareInfo.getImage());
            }
        }
        if (!TextUtils.isEmpty(shareInfo.getLink())) {
            UniSdkUtils.d(TAG, "!TextUtils.isEmpty(shareInfo.getLink())");
            shareArgs.setValue("url", shareInfo.getLink());
        }
        if (!TextUtils.isEmpty(shareInfo.getVideoUrl())) {
            UniSdkUtils.d(TAG, "shareInfo.getVideoUrl() not empty");
            shareArgs.setValue(ShareArgs.VIDEO_URL, shareInfo.getVideoUrl());
        }
        if (102 == shareInfo.getShareChannel() || 104 == shareInfo.getShareChannel() || 106 == shareInfo.getShareChannel()) {
            shareArgs.setValue(ShareArgs.TO_BLOG, "1");
        }
        if (117 == shareInfo.getShareChannel()) {
            if (shareInfo.isShowShareDialog()) {
                shareArgs.setValue("comment", "show");
            } else {
                shareArgs.setValue("comment", null);
            }
            shareArgs.setValue("title", shareInfo.getToUser());
            shareArgs.setValue(ShareArgs.TO_BLOG, "2");
        }
        if (118 == shareInfo.getShareChannel()) {
            shareArgs.setValue("title", shareInfo.getToUser());
            shareArgs.setValue(ShareArgs.TO_BLOG, "2");
        }
        if (shareInfo.getShareThumb() != null) {
            UniSdkUtils.d(TAG, "null != shareInfo.getShareThumb()");
            shareArgs.setValue(ShareArgs.THUMB_DATA, shareInfo.getShareThumb());
        }
        if (shareInfo.getShareBitmap() != null) {
            UniSdkUtils.d(TAG, "null != shareInfo.getShareBitmap()");
            shareArgs.setValue(ShareArgs.IMG_DATA, shareInfo.getShareBitmap());
        }
        if (301 == shareInfo.getShareChannel()) {
            if (ShareInfo.TYPE_SUBSCRIBE.equals(shareInfo.getType())) {
                shareArgs.setValue("type", ShareArgs.TYPE_MINI_PROGRAM_SUBSCRIBE);
                shareArgs.setValue(ShareArgs.USER_NAME, shareInfo.getMiniProgramID());
            } else {
                try {
                    shareArgs.setValue("type", ShareArgs.TYPE_TO_MINI_PROGRAM);
                    shareArgs.setValue(ShareArgs.PATH, shareInfo.getPath());
                    shareArgs.setValue(ShareArgs.USER_NAME, shareInfo.getUserName());
                    shareArgs.setValue(ShareArgs.MINI_PROGRAM_TYPE, shareInfo.getMiniProgramType());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "ngshare MiniProgram Error:" + e.getMessage());
                }
            }
        }
        if (105 == shareInfo.getShareChannel() && "TYPE_MINI_PROGRAM".equals(shareInfo.getType())) {
            try {
                shareArgs.setValue("type", ShareArgs.TYPE_TO_MINI_PROGRAM);
                shareArgs.setValue(ShareArgs.PATH, shareInfo.getPath());
                shareArgs.setValue(ShareArgs.USER_NAME, shareInfo.getUserName());
                shareArgs.setValue(ShareArgs.MINI_PROGRAM_TYPE, shareInfo.getMiniProgramType());
            } catch (Exception e2) {
                e2.printStackTrace();
                Log.e(TAG, "ngshare MiniProgram Error:" + e2.getMessage());
            }
        }
        if (!TextUtils.isEmpty(shareInfo.getExtJson())) {
            UniSdkUtils.d(TAG, "shareInfo.getExtJson:" + shareInfo.getExtJson());
            shareArgs.setValue("ExtJson", shareInfo.getExtJson());
        }
        return shareArgs;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void updateApi(String str, String str2) {
        UniSdkUtils.d(TAG, "call updateApi key:" + str + ",platform:" + str2);
        if (Integer.toString(101).equals(str2) || Integer.toString(102).equals(str2) || Integer.toString(118).equals(str2)) {
            str2 = Platform.WEIXIN;
        } else if (Integer.toString(103).equals(str2) || Integer.toString(104).equals(str2)) {
            str2 = Platform.YIXIN;
        } else if (Integer.toString(105).equals(str2) || Integer.toString(106).equals(str2)) {
            str2 = "QQ";
        } else if (Integer.toString(100).equals(str2) || Integer.toString(117).equals(str2)) {
            str2 = Platform.WEIBO;
        }
        ShareMgr.getInst().updateApi(str, str2);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public boolean hasPlatform(String str) {
        UniSdkUtils.d(TAG, "call hasPlatform platform:" + str);
        if (Integer.toString(101).equals(str) || Integer.toString(102).equals(str) || Integer.toString(118).equals(str)) {
            str = Platform.WEIXIN;
        } else {
            if (Integer.toString(103).equals(str) || Integer.toString(104).equals(str)) {
                return false;
            }
            if (Integer.toString(105).equals(str) || Integer.toString(106).equals(str)) {
                str = "QQ";
            } else if (Integer.toString(100).equals(str) || Integer.toString(117).equals(str)) {
                str = Platform.WEIBO;
            }
        }
        return ShareMgr.getInst().hasPlatform(str).booleanValue();
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void extendFunc(String str, final Object... objArr) throws JSONException {
        String strOptString;
        final JSONObject jSONObject;
        String strOptString2;
        int i;
        int i2;
        Field[] fieldArr;
        UniSdkUtils.i(TAG, "extendFunc: " + str + ", " + objArr.toString());
        try {
            jSONObject = new JSONObject(str);
            strOptString2 = jSONObject.optString("methodId");
            UniSdkUtils.d(TAG, "extendFunc:" + strOptString2);
            strOptString = jSONObject.optString("channel");
        } catch (JSONException e) {
            e = e;
            strOptString = "";
        }
        try {
            if (TextUtils.isEmpty(strOptString) || getChannel().equalsIgnoreCase(strOptString)) {
                boolean z = true;
                if ("ngshareExtend".equals(strOptString2)) {
                    UniSdkUtils.d(TAG, "ngshareExtend:" + str);
                    if (objArr.length > 0 && objArr[0] != null) {
                        UniSdkUtils.d(TAG, "ngshareExtend Context:" + objArr[0]);
                        if (objArr[0] instanceof Activity) {
                            ShareMgr.getInst().setContext((Activity) objArr[0]);
                        } else {
                            UniSdkUtils.d(TAG, "objects[0] not Activity:" + objArr[0].getClass());
                        }
                    }
                    final String strOptString3 = jSONObject.optString(SocialConstants.PARAM_SOURCE);
                    try {
                        Field[] declaredFields = Class.forName("com.netease.ntunisdk.base.SdkBase").getDeclaredFields();
                        int length = declaredFields.length;
                        int i3 = 0;
                        while (i3 < length) {
                            Field field = declaredFields[i3];
                            if (field.getGenericType().toString().equals("interface com.netease.ntunisdk.base.OnShareListener")) {
                                UniSdkUtils.d(TAG, "set shareListener:");
                                field.setAccessible(z);
                                this.originalListener = (OnShareListener) field.get(SdkMgr.getInst());
                                final int propInt = SdkMgr.getInst().getPropInt(ConstProp.SHARE_CALLER_THREAD, 2);
                                i = i3;
                                i2 = length;
                                fieldArr = declaredFields;
                                SdkMgr.getInst().setShareListener(new OnShareListener() { // from class: com.netease.ntunisdk.SdkNGShare.3
                                    @Override // com.netease.ntunisdk.base.OnShareListener
                                    public void onShareFinished(boolean z2) throws JSONException {
                                        try {
                                            UniSdkUtils.d(SdkNGShare.TAG, "extend share finish:" + z2);
                                            String strOptString4 = jSONObject.optString("platform");
                                            String str2 = "";
                                            if (SDefine.l.equals(strOptString3)) {
                                                UniSdkUtils.d(SdkNGShare.TAG, "ngshareExtend platform:" + strOptString4);
                                                jSONObject.put("methodId", "NGWebViewCallbackToWeb");
                                                jSONObject.put("result", z2);
                                                jSONObject.put("hasPlatform", SdkMgr.getInst().ntHasPlatform(strOptString4));
                                                jSONObject.put("channel", "ngwebview");
                                                jSONObject.put("code", z2 ? 0 : 2);
                                                if (!TextUtils.isEmpty(strOptString4) && !SdkMgr.getInst().ntHasPlatform(strOptString4)) {
                                                    str2 = "\u5f53\u524d\u8bbe\u5907\u6ca1\u6709\u5b89\u88c5" + strOptString4 + "\uff0c\u7ec8\u6b62\u5206\u4eab";
                                                }
                                                jSONObject.put("errorDescription", str2);
                                                SdkMgr.getInst().ntExtendFunc(jSONObject.toString());
                                            } else {
                                                jSONObject.put("result", z2);
                                                jSONObject.put(RespUtil.UniSdkField.RESP_CODE, 0);
                                                jSONObject.put(RespUtil.UniSdkField.RESP_MSG, "success");
                                                jSONObject.put("code", z2 ? 0 : 2);
                                                if (!TextUtils.isEmpty(strOptString4) && !SdkMgr.getInst().ntHasPlatform(strOptString4)) {
                                                    str2 = "\u5f53\u524d\u8bbe\u5907\u6ca1\u6709\u5b89\u88c5" + strOptString4 + "\uff0c\u7ec8\u6b62\u5206\u4eab";
                                                }
                                                jSONObject.put("errorDescription", str2);
                                                SdkNGShare.this.extendFuncCall(jSONObject.toString());
                                            }
                                            SdkMgr.getInst().setShareListener(SdkNGShare.this.originalListener, propInt);
                                            if (objArr.length <= 0 || objArr[0] == null) {
                                                return;
                                            }
                                            UniSdkUtils.d(SdkNGShare.TAG, "sdk base myCtx:" + SdkNGShare.this.myCtx);
                                            ShareMgr.getInst().setContext(SdkNGShare.this.myCtx);
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                            SdkMgr.getInst().setShareListener(SdkNGShare.this.originalListener, propInt);
                                            Object[] objArr2 = objArr;
                                            if (objArr2.length <= 0 || objArr2[0] == null) {
                                                return;
                                            }
                                            UniSdkUtils.d(SdkNGShare.TAG, "sdk base myCtx:" + SdkNGShare.this.myCtx);
                                            ShareMgr.getInst().setContext(SdkNGShare.this.myCtx);
                                        }
                                    }
                                }, 1);
                            } else {
                                i = i3;
                                i2 = length;
                                fieldArr = declaredFields;
                            }
                            i3 = i + 1;
                            declaredFields = fieldArr;
                            length = i2;
                            z = true;
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    DownLoadUtil.webShare(this.myCtx, DownLoadUtil.jsonStr2Obj(str));
                    return;
                }
                if (TextUtils.isEmpty(strOptString)) {
                    return;
                }
                jSONObject.put(RespUtil.UniSdkField.RESP_CODE, 1);
                jSONObject.put(RespUtil.UniSdkField.RESP_MSG, "methodId not exist");
                extendFuncCall(jSONObject.toString());
            }
        } catch (JSONException e3) {
            e = e3;
            UniSdkUtils.d(TAG, "extendFunc JSONException:" + e.getMessage());
            if (TextUtils.isEmpty(strOptString)) {
                return;
            }
            try {
                JSONObject jSONObject2 = new JSONObject(str);
                jSONObject2.put(RespUtil.UniSdkField.RESP_CODE, 10000);
                jSONObject2.put(RespUtil.UniSdkField.RESP_MSG, "\u672a\u77e5\u9519\u8bef");
                extendFuncCall(jSONObject2.toString());
            } catch (JSONException unused) {
            }
        }
    }

    public void getWeiboConfig(Context context) throws IOException {
        String str;
        try {
            InputStream inputStreamOpen = context.getAssets().open("ntshare_data", 3);
            byte[] bArr = new byte[inputStreamOpen.available()];
            inputStreamOpen.read(bArr);
            str = new String(bArr, "UTF-8");
        } catch (IOException e) {
            UniSdkUtils.i(TAG, "read ntshare_data error :" + e.getMessage());
            str = null;
        }
        UniSdkUtils.i(TAG, "ntshare_data json:" + str);
        if (str == null) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject(Platform.WEIBO);
            this.weibo_appid = jSONObjectOptJSONObject.optString("app_id");
            this.weibo_url = jSONObjectOptJSONObject.optString("app_url", "http://www.sina.com");
            UniSdkUtils.i(TAG, "weibo_appid:" + this.weibo_appid);
            UniSdkUtils.i(TAG, "weibo_url:" + this.weibo_url);
            this.qq_appid = jSONObject.optJSONObject("QQ").optString("app_id");
            UniSdkUtils.i(TAG, "qq_appid:" + this.qq_appid);
        } catch (JSONException e2) {
            UniSdkUtils.i(TAG, "ntshare_data config parse to json error: " + e2.getMessage());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:77:0x0272 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:93:? A[RETURN, SYNTHETIC] */
    @Override // com.netease.ntunisdk.base.SdkBase
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void extendFunc(java.lang.String r14) throws org.json.JSONException, java.lang.InterruptedException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 649
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.SdkNGShare.extendFunc(java.lang.String):void");
    }

    public void QQLogin() throws IOException {
        if (TextUtils.isEmpty(this.qq_appid)) {
            getWeiboConfig(this.myCtx);
        }
        this.api = Tencent.createInstance(this.qq_appid, this.myCtx, this.myCtx.getPackageName() + ".uningshare.fileprovider");
        Tencent.setIsPermissionGranted(true);
        UniSdkUtils.d(TAG, "Tencent.createInstance:" + this.api.hashCode());
        this.api.login((Activity) this.myCtx, Const.CONFIG_KEY.ALL, this.qqListener);
    }

    public boolean QQReady() {
        boolean z = false;
        if (this.api == null) {
            UniSdkUtils.d(TAG, "api null!");
            return false;
        }
        UniSdkUtils.d(TAG, "api.isSessionValid():" + this.api.isSessionValid());
        UniSdkUtils.d(TAG, "api.getQQToken().getOpenId():" + this.api.getQQToken().getOpenId());
        if (this.api.isSessionValid() && this.api.getQQToken().getOpenId() != null) {
            z = true;
        }
        if (!z) {
            UniSdkUtils.d(TAG, "QQ login and get openId first!");
        }
        return z;
    }

    /* JADX WARN: Type inference failed for: r2v4, types: [com.netease.ntunisdk.SdkNGShare$7] */
    private void handlerUniQrcode(JSONObject jSONObject) {
        UniSdkUtils.d(TAG, "handlerUniQrcode:" + jSONObject.toString());
        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("params");
        if (jSONObjectOptJSONObject != null) {
            if ("share".equalsIgnoreCase(jSONObjectOptJSONObject.optString("action"))) {
                final ShareInfo shareInfo = new ShareInfo();
                shareInfo.setShareChannel(jSONObjectOptJSONObject.optInt("shareChannel", 102));
                shareInfo.setTitle(jSONObjectOptJSONObject.optString("title"));
                String strOptString = jSONObjectOptJSONObject.optString("text");
                shareInfo.setText(strOptString);
                shareInfo.setDesc(jSONObjectOptJSONObject.optString(SocialConstants.PARAM_APP_DESC, strOptString));
                shareInfo.setLink(jSONObjectOptJSONObject.optString("link"));
                final String strOptString2 = jSONObjectOptJSONObject.optString("type");
                final String strOptString3 = jSONObjectOptJSONObject.optString("image_url");
                if (!TextUtils.isEmpty(strOptString3)) {
                    new Thread() { // from class: com.netease.ntunisdk.SdkNGShare.7
                        @Override // java.lang.Thread, java.lang.Runnable
                        public void run() throws IOException {
                            if ("TYPE_LINK".equals(strOptString2)) {
                                Bitmap bitmapLoadImage = SdkNGShare.this.loadImage(strOptString3);
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmapLoadImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                UniSdkUtils.d(SdkNGShare.TAG, "ShareThumb before length:" + byteArrayOutputStream.toByteArray().length);
                                Bitmap bitmapScaleBitmap = SdkNGShare.this.scaleBitmap(bitmapLoadImage);
                                ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                                bitmapScaleBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream2);
                                UniSdkUtils.d(SdkNGShare.TAG, "ShareThumb after length:" + byteArrayOutputStream2.toByteArray().length);
                                shareInfo.setShareThumb(bitmapScaleBitmap);
                            } else {
                                ShareInfo shareInfo2 = shareInfo;
                                SdkNGShare sdkNGShare = SdkNGShare.this;
                                shareInfo2.setImage(sdkNGShare.savePhotoToGallery(sdkNGShare.myCtx, strOptString3));
                            }
                            SdkNGShare.this.qrcodeShare(shareInfo);
                        }
                    }.start();
                    return;
                } else {
                    qrcodeShare(shareInfo);
                    return;
                }
            }
            return;
        }
        UniSdkUtils.e(TAG, "handlerUniQrcode params null");
    }

    public String savePhotoToGallery(Context context, String str) throws IOException {
        try {
            UniSdkUtils.d(TAG, "getBitmap bmp url\uff1a" + str);
            if (!TextUtils.isEmpty(str)) {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                httpURLConnection.setConnectTimeout(6000);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                bmp = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            }
            UniSdkUtils.d(TAG, "getBitmap bmp\uff1a" + bmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saveBitmap(context, bmp);
    }

    public String saveBitmap(Context context, Bitmap bitmap) throws IOException {
        String str = context.getExternalFilesDir(null) + File.separator + "UniQrcodeShare.jpg";
        UniSdkUtils.e(TAG, "\u4fdd\u5b58\u56fe\u7247:" + str);
        File file = new File(str);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            UniSdkUtils.i(TAG, "\u5df2\u7ecf\u4fdd\u5b58");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    protected Bitmap scaleBitmap(Bitmap bitmap) {
        UniSdkUtils.d(TAG, "scale ShareArgs.IMG_DATA to thumb");
        float fMin = Math.min(80.0f / bitmap.getWidth(), 80.0f / bitmap.getHeight());
        Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * fMin), (int) (bitmap.getHeight() * fMin), true);
        UniSdkUtils.d(TAG, "thumb_data w:" + bitmapCreateScaledBitmap.getWidth() + ",h:" + bitmapCreateScaledBitmap.getHeight());
        return bitmapCreateScaledBitmap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bitmap loadImage(String str) throws IOException {
        UniSdkUtils.d(TAG, "loadImage");
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            if (httpURLConnection.getResponseCode() != 200) {
                return null;
            }
            InputStream inputStream = httpURLConnection.getInputStream();
            DisplayMetrics displayMetrics = this.myCtx.getResources().getDisplayMetrics();
            UniSdkUtils.d(TAG, "metrics.widthPixels:" + displayMetrics.widthPixels + ",metrics.heightPixels:" + displayMetrics.heightPixels);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[1024];
            while (true) {
                int i = inputStream.read(bArr, 0, bArr.length);
                if (i != -1) {
                    byteArrayOutputStream.write(bArr, 0, i);
                } else {
                    byteArrayOutputStream.flush();
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    UniSdkUtils.d(TAG, "imageData.length:" + byteArray.length);
                    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
                    int iCalculateInSampleSize = calculateInSampleSize(options, displayMetrics.widthPixels, displayMetrics.heightPixels);
                    options.inSampleSize = iCalculateInSampleSize;
                    UniSdkUtils.d(TAG, "options.inSampleSize:" + iCalculateInSampleSize);
                    options.inJustDecodeBounds = false;
                    Bitmap bitmapDecodeByteArray = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
                    inputStream.close();
                    byteArrayOutputStream.close();
                    return bitmapDecodeByteArray;
                }
            }
        } catch (IOException e) {
            UniSdkUtils.d(TAG, "getBitmap exception:" + e.getMessage());
            return null;
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        UniSdkUtils.d(TAG, "options.outWidth:" + i4 + ",options.outHeight:" + i3);
        if (i3 <= i2 && i4 <= i) {
            return 1;
        }
        int iRound = Math.round(i3 / i2);
        int iRound2 = Math.round(i4 / i);
        return iRound > iRound2 ? iRound : iRound2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void qrcodeShare(final ShareInfo shareInfo) {
        UniSdkUtils.d(TAG, "qrcodeShare");
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.SdkNGShare.8
            @Override // java.lang.Runnable
            public void run() {
                SdkNGShare.this.share(shareInfo);
            }
        });
    }
}