package com.netease.ntsharesdk.platform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import com.netease.ntsharesdk.Platform;
import com.netease.ntsharesdk.ShareArgs;
import com.netease.ntsharesdk.UniNgshareFileProvider;
import com.netease.ntunisdk.base.SdkMgr;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MultiImageObject;
import com.sina.weibo.sdk.api.SuperGroupObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.sina.weibo.sdk.openapi.WBAPIFactory;
import com.sina.weibo.sdk.share.WbShareCallback;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class Weibo extends Platform implements WbShareCallback {
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 31155;
    private static final int TYPE_ATTENTION = 2;
    private static final int TYPE_SHARE = 1;
    private static Weibo Weiboinst = new Weibo();
    private static boolean hasOauth;
    private ShareArgs argsn;
    private boolean authorize;
    private boolean hasPermission;
    public String initContextString;
    private boolean isFirstAuthorizeSuccess;
    private IWBAPI mWBAPI;

    @Override // com.netease.ntsharesdk.Platform
    public Object getAPIInst() {
        return null;
    }

    @Override // com.netease.ntsharesdk.Platform
    protected String getPlatformName() {
        return Platform.WEIBO;
    }

    public static Weibo getInst() {
        return Weiboinst;
    }

    public Weibo() {
        this.authorize = false;
        this.isFirstAuthorizeSuccess = false;
        this.hasPermission = true;
        this.initContextString = "";
    }

    public Weibo(Context context) {
        super(context);
        this.authorize = false;
        this.isFirstAuthorizeSuccess = false;
        this.hasPermission = true;
        this.initContextString = "";
    }

    @Override // com.netease.ntsharesdk.Platform
    public void initSdk(Context context) {
        dLog("\u5fae\u535a\u5206\u4eabhasInit:" + this.hasInit);
        dLog("initContextString:" + this.initContextString);
        dLog("ctx:" + context.toString());
        if (this.hasInit && this.initContextString.equals(context.toString())) {
            return;
        }
        this.myCtx = context;
        this.mConf = readConfig(this.myCtx, getPlatformName());
        String config = getConfig("app_id");
        String config2 = getConfig("app_url", "http://www.sina.com");
        if (!TextUtils.isEmpty(SdkMgr.getInst().getPropStr("NG_SHARE_ALL_CHANNEL_CONFIGURATION"))) {
            try {
                JSONObject jSONObjectOptJSONObject = new JSONObject(SdkMgr.getInst().getPropStr("NG_SHARE_ALL_CHANNEL_CONFIGURATION")).optJSONObject(Platform.WEIBO);
                if (jSONObjectOptJSONObject != null && !TextUtils.isEmpty(jSONObjectOptJSONObject.optString("app_id"))) {
                    String strOptString = jSONObjectOptJSONObject.optString("app_id");
                    try {
                        config2 = jSONObjectOptJSONObject.optString("universal_link");
                        config = strOptString;
                    } catch (Exception unused) {
                        config = strOptString;
                        dLog(getPlatformName() + " CONFIGURATION error");
                        AuthInfo authInfo = new AuthInfo(this.myCtx, config, config2, "");
                        dLog("platform: " + getPlatformName() + " init sdk app_id:" + config);
                        this.mWBAPI = WBAPIFactory.createWBAPI(this.myCtx);
                        this.mWBAPI.registerApp(this.myCtx, authInfo);
                        this.initContextString = context.toString();
                        this.hasInit = true;
                    }
                }
            } catch (Exception unused2) {
            }
        }
        AuthInfo authInfo2 = new AuthInfo(this.myCtx, config, config2, "");
        dLog("platform: " + getPlatformName() + " init sdk app_id:" + config);
        this.mWBAPI = WBAPIFactory.createWBAPI(this.myCtx);
        this.mWBAPI.registerApp(this.myCtx, authInfo2);
        this.initContextString = context.toString();
        this.hasInit = true;
    }

    public Context getCtx() {
        return this.myCtx;
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override // com.sina.weibo.sdk.share.WbShareCallback
    public void onComplete() {
        if (this.shareEndListener != null) {
            this.shareEndListener.onShareEnd(getPlatformName(), 0, this.argsn);
        }
    }

    @Override // com.sina.weibo.sdk.share.WbShareCallback
    public void onError(UiError uiError) {
        ShareArgs shareArgs = this.argsn;
        if (shareArgs != null) {
            shareArgs.setFailMsg("\u5206\u4eab\u5931\u8d25");
        }
        dLog("\u5fae\u535a\u5206\u4eab\u5931\u8d25:" + uiError.errorMessage);
        if (this.shareEndListener != null) {
            this.shareEndListener.onShareEnd(getPlatformName(), 2, this.argsn);
        }
    }

    @Override // com.sina.weibo.sdk.share.WbShareCallback
    public void onCancel() {
        ShareArgs shareArgs = this.argsn;
        if (shareArgs != null) {
            shareArgs.setFailMsg("\u53d6\u6d88\u5206\u4eab");
        }
        if (this.shareEndListener != null) {
            this.shareEndListener.onShareEnd(getPlatformName(), 1, this.argsn);
        }
    }

    private void sendMessage(ShareArgs shareArgs) {
        sendMultiMessage(shareArgs);
    }

    private void sendMultiMessage(ShareArgs shareArgs) {
        new WeiboMultiMessage();
        WeiboMultiMessage weiboMultiMessage = (WeiboMultiMessage) genMessage(shareArgs);
        dLog("\u53d1\u9001\u5fae\u535a\u5206\u4eab\u8bf7\u6c42\u53c2\u6570:" + weiboMultiMessage.toString());
        this.mWBAPI.shareMessage((Activity) this.myCtx, weiboMultiMessage, true);
    }

    @Override // com.netease.ntsharesdk.Platform
    protected Object genMessage(ShareArgs shareArgs) {
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.textObject = getTextObj(shareArgs);
        if (shareArgs.hasImage().booleanValue() || "TYPE_IMAGE".equals(shareArgs.getValue("type"))) {
            dLog("args.hasImage() true");
            if (shareArgs.getValue(ShareArgs.IMG_DATA) != null) {
                weiboMultiMessage.imageObject = getImageObj(shareArgs);
            } else if (shareArgs.getValue(ShareArgs.IMG_PATH) != null) {
                weiboMultiMessage.multiImageObject = getMultiImageObj(shareArgs);
            }
        } else if (shareArgs.getValue("url") != null || "TYPE_LINK".equals(shareArgs.getValue("type"))) {
            dLog("args.getValue(ShareArgs.URL) not null");
            weiboMultiMessage.mediaObject = getWebpageObj(shareArgs);
        } else if (shareArgs.getValue(ShareArgs.VIDEO_URL) != null || "TYPE_VIDEO".equals(shareArgs.getValue("type"))) {
            dLog("args.getValue(ShareArgs.VIDEO_URL) not null");
            weiboMultiMessage.videoSourceObject = getVideoObj(shareArgs);
        }
        if (shareArgs.getValue("ExtJson") != null) {
            try {
                JSONObject jSONObject = new JSONObject((String) shareArgs.getValue("ExtJson"));
                if ("superGroup".equals(jSONObject.optString("type"))) {
                    dLog("share superGroup");
                    String strOptString = jSONObject.optString("superGroup");
                    String strOptString2 = jSONObject.optString("section");
                    String strOptString3 = jSONObject.optString("extData");
                    SuperGroupObject superGroupObject = new SuperGroupObject();
                    superGroupObject.sgName = strOptString;
                    superGroupObject.secName = strOptString2;
                    superGroupObject.sgExtParam = strOptString3;
                    weiboMultiMessage.superGroupObject = superGroupObject;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return weiboMultiMessage;
    }

    private int getShareType(ShareArgs shareArgs) {
        return shareArgs.getValue(ShareArgs.TO_BLOG, null) == null ? 1 : 2;
    }

    @Override // com.netease.ntsharesdk.Platform
    public void share(ShareArgs shareArgs) {
        if (!checkArgs(shareArgs).booleanValue()) {
            dLog("checkArgs(args) false");
            if (this.shareEndListener != null) {
                this.shareEndListener.onShareEnd(getPlatformName(), 2, shareArgs);
                return;
            }
            return;
        }
        try {
            this.argsn = shareArgs;
            if (getShareType(shareArgs) != 1) {
                return;
            }
            doShare(shareArgs);
        } catch (Exception e) {
            e.printStackTrace();
            if (this.shareEndListener != null) {
                this.shareEndListener.onShareEnd(getPlatformName(), 2, shareArgs);
            }
        }
    }

    private void doShare(ShareArgs shareArgs) {
        if (this.mWBAPI.isWBAppInstalled()) {
            sendMessage(shareArgs);
            return;
        }
        if (shareArgs != null) {
            shareArgs.setFailMsg("\u5206\u4eab\u5931\u8d25");
        }
        if (this.shareEndListener != null) {
            this.shareEndListener.onShareEnd(getPlatformName(), 2, shareArgs);
        }
    }

    private void appShare(ShareArgs shareArgs, Oauth2AccessToken oauth2AccessToken) {
        dLog("\u5f00\u59cb\u5fae\u535a\u5206\u4eab");
        hasOauth = true;
        sendMessage(shareArgs);
    }

    private TextObject getTextObj(ShareArgs shareArgs) {
        TextObject textObject = new TextObject();
        textObject.text = shareArgs.getValue("text").toString();
        textObject.title = shareArgs.getValue("title").toString();
        return textObject;
    }

    private ImageObject getImageObj(ShareArgs shareArgs) throws IOException {
        ImageObject imageObject = new ImageObject();
        if (shareArgs.getValue(ShareArgs.IMG_DATA) != null) {
            imageObject.setImageData((Bitmap) shareArgs.getValue(ShareArgs.IMG_DATA));
        }
        return imageObject;
    }

    private MultiImageObject getMultiImageObj(ShareArgs shareArgs) {
        dLog("getMultiImageObj");
        MultiImageObject multiImageObject = new MultiImageObject();
        if (shareArgs.getValue(ShareArgs.IMG_PATH) != null) {
            ArrayList<Uri> arrayList = new ArrayList<>();
            if (Build.VERSION.SDK_INT >= 24) {
                dLog("SDK_INT >= N");
                String str = this.myCtx.getPackageName() + ".uningshare.fileprovider";
                dLog("authority:" + str);
                Uri uriForFile = UniNgshareFileProvider.getUriForFile(this.myCtx, str, new File(shareArgs.getValue(ShareArgs.IMG_PATH).toString()));
                dLog("uri:" + uriForFile);
                arrayList.add(uriForFile);
            } else {
                dLog("SDK_INT < N");
                arrayList.add(Uri.fromFile(new File(shareArgs.getValue(ShareArgs.IMG_PATH).toString())));
            }
            multiImageObject.imageList = arrayList;
        }
        return multiImageObject;
    }

    private WebpageObject getWebpageObj(ShareArgs shareArgs) throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream;
        WebpageObject webpageObject = new WebpageObject();
        webpageObject.identify = UUID.randomUUID().toString();
        webpageObject.title = shareArgs.getValue("title").toString();
        if (shareArgs.getValue("comment") != null) {
            dLog("args.getValue(ShareArgs.COMMENT) not null");
            webpageObject.description = shareArgs.getValue("comment").toString();
        } else {
            dLog("args.getValue(ShareArgs.COMMENT) null, please set value");
        }
        Bitmap bitmap = (Bitmap) shareArgs.getValue(ShareArgs.THUMB_DATA);
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        try {
            try {
                try {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    try {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, byteArrayOutputStream);
                        webpageObject.thumbData = byteArrayOutputStream.toByteArray();
                        byteArrayOutputStream.close();
                    } catch (Exception e) {
                        e = e;
                        byteArrayOutputStream2 = byteArrayOutputStream;
                        e.printStackTrace();
                        if (byteArrayOutputStream2 != null) {
                            byteArrayOutputStream2.close();
                        }
                        webpageObject.actionUrl = shareArgs.getValue("url").toString();
                        webpageObject.defaultText = shareArgs.getValue("text").toString();
                        return webpageObject;
                    } catch (Throwable th) {
                        th = th;
                        if (byteArrayOutputStream != null) {
                            try {
                                byteArrayOutputStream.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    byteArrayOutputStream = byteArrayOutputStream2;
                }
            } catch (Exception e3) {
                e = e3;
            }
        } catch (IOException e4) {
            e4.printStackTrace();
        }
        webpageObject.actionUrl = shareArgs.getValue("url").toString();
        webpageObject.defaultText = shareArgs.getValue("text").toString();
        return webpageObject;
    }

    private VideoSourceObject getVideoObj(ShareArgs shareArgs) {
        VideoSourceObject videoSourceObject = new VideoSourceObject();
        if (Build.VERSION.SDK_INT >= 24) {
            File file = new File(shareArgs.getValue(ShareArgs.VIDEO_URL).toString());
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }
            videoSourceObject.videoPath = UniNgshareFileProvider.getUriForFile(this.myCtx, this.myCtx.getPackageName() + ".uningshare.fileprovider", file);
        } else {
            videoSourceObject.videoPath = Uri.fromFile(new File(shareArgs.getValue(ShareArgs.VIDEO_URL).toString()));
        }
        return videoSourceObject;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0031  */
    @Override // com.netease.ntsharesdk.Platform
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Boolean checkArgs(com.netease.ntsharesdk.ShareArgs r4) {
        /*
            r3 = this;
            int r0 = r3.getShareType(r4)
            r1 = 1
            if (r0 == r1) goto L1c
            r2 = 2
            if (r0 == r2) goto Lb
            goto L31
        Lb:
            java.lang.String r0 = "title"
            java.lang.Object r0 = r4.getValue(r0)
            java.lang.String r0 = (java.lang.String) r0
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L31
            java.lang.String r0 = "ShareArgs wrong! WeiboAttention should has title(userId)"
            goto L33
        L1c:
            java.lang.Boolean r0 = r4.hasImage()
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L31
            java.lang.String r0 = "img_url"
            java.lang.Object r0 = r4.getValue(r0)
            if (r0 == 0) goto L31
            java.lang.String r0 = "ShareArgs wrong! Weibo app share doesn`t support img_url"
            goto L33
        L31:
            java.lang.String r0 = ""
        L33:
            int r2 = r0.length()
            if (r2 <= 0) goto L45
            dLog(r0)
            r4.setFailMsg(r0)
            r4 = 0
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r4)
            return r4
        L45:
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r1)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntsharesdk.platform.Weibo.checkArgs(com.netease.ntsharesdk.ShareArgs):java.lang.Boolean");
    }

    @Override // com.netease.ntsharesdk.Platform
    public void handleIntent(Intent intent) {
        dLog("handleIntent");
        IWBAPI iwbapi = this.mWBAPI;
        if (iwbapi != null) {
            iwbapi.doResultIntent(intent, this);
        }
    }

    @Override // com.netease.ntsharesdk.Platform
    public void handleActivityResult(int i, int i2, Intent intent) {
        dLog("Weibo handleActivityResult, requestCode:" + i + ", resultCode:" + i2 + ", hasOauth:" + hasOauth);
        super.handleActivityResult(i, i2, intent);
        StringBuilder sb = new StringBuilder();
        sb.append("mWBAPI:");
        sb.append(this.mWBAPI);
        dLog(sb.toString());
        if (this.mWBAPI == null || i != 10001) {
            return;
        }
        dLog("mWBAPI CallBack");
        this.mWBAPI.doResultIntent(intent, this);
    }

    @Override // com.netease.ntsharesdk.Platform
    public boolean hasPlatformInstall(String str) {
        try {
            if (this.mWBAPI != null) {
                dLog("hasPlatformInstall Weibo:" + this.mWBAPI.isWBAppInstalled());
                return this.mWBAPI.isWBAppInstalled();
            }
        } catch (Exception e) {
            dLog("hasPlatformInstall Exception:" + e.getMessage());
        }
        dLog("hasPlatformInstall shareHandler null");
        return false;
    }

    @Override // com.netease.ntsharesdk.Platform
    public void updateApi(String str, Context context) {
        AuthInfo authInfo = new AuthInfo(this.myCtx, str, getConfig("app_url", "http://www.sina.com"), "");
        this.mWBAPI = WBAPIFactory.createWBAPI(this.myCtx);
        this.mWBAPI.registerApp(this.myCtx, authInfo);
        this.myCtx = context;
        this.mConf = readConfig(this.myCtx, getPlatformName());
        this.hasInit = true;
        this.initContextString = context.toString();
    }
}