package com.netease.ntsharesdk.platform;

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
import com.tencent.mm.opensdk.modelbiz.SubscribeMiniProgramMsg;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class Weixin extends Platform {
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 31155;
    private static Weixin Weixininst = new Weixin();
    private IWXAPI api;
    private ShareArgs argsn;
    public String initContextString;

    @Override // com.netease.ntsharesdk.Platform
    protected String getPlatformName() {
        return Platform.WEIXIN;
    }

    @Override // com.netease.ntsharesdk.Platform
    public void handleIntent(Intent intent) {
    }

    public static Weixin getInst() {
        return Weixininst;
    }

    public Weixin() {
        this.initContextString = "";
    }

    public Weixin(Context context) {
        super(context);
        this.initContextString = "";
    }

    @Override // com.netease.ntsharesdk.Platform
    protected Object genMessage(ShareArgs shareArgs) {
        Bitmap bitmapScaleBitmap;
        WXMediaMessage.IMediaObject wXWebpageObject;
        dLog("imgPath:" + shareArgs.getValue(ShareArgs.IMG_PATH) + ",imgUrl:" + shareArgs.getValue(ShareArgs.IMG_URL) + ",imgData:" + shareArgs.getValue(ShareArgs.IMG_DATA));
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(shareArgs.toString());
        dLog(sb.toString());
        WXMediaMessage wXMediaMessage = new WXMediaMessage();
        if (shareArgs.getValue(ShareArgs.THUMB_DATA) != null) {
            dLog("args.getValue(ShareArgs.THUMB_DATA) != null");
            bitmapScaleBitmap = (Bitmap) shareArgs.getValue(ShareArgs.THUMB_DATA);
        } else {
            bitmapScaleBitmap = null;
        }
        if ("TYPE_MINI_PROGRAM".equals(shareArgs.getValue("type", ""))) {
            dLog("case mini-program");
            WXMiniProgramObject wXMiniProgramObject = new WXMiniProgramObject();
            Object value = shareArgs.getValue("url");
            if (value != null) {
                wXMiniProgramObject.webpageUrl = value.toString();
            }
            Object value2 = shareArgs.getValue("text");
            if (value2 != null) {
                wXMiniProgramObject.userName = value2.toString();
            }
            Object value3 = shareArgs.getValue("comment");
            wXWebpageObject = wXMiniProgramObject;
            if (value3 != null) {
                wXMiniProgramObject.path = value3.toString();
                wXWebpageObject = wXMiniProgramObject;
            }
        } else if (shareArgs.hasImage().booleanValue() || "TYPE_IMAGE".equals(shareArgs.getValue("type"))) {
            dLog("case image");
            WXImageObject wXImageObject = new WXImageObject();
            if (shareArgs.getValue(ShareArgs.IMG_DATA) != null) {
                WXImageObject wXImageObject2 = new WXImageObject((Bitmap) shareArgs.getValue(ShareArgs.IMG_DATA));
                if (bitmapScaleBitmap == null) {
                    bitmapScaleBitmap = scaleBitmap((Bitmap) shareArgs.getValue(ShareArgs.IMG_DATA));
                }
                dLog("shareInfo.getShareBitmap length:" + wXImageObject2.imageData.length);
                wXWebpageObject = wXImageObject2;
            } else {
                if (shareArgs.getValue(ShareArgs.IMG_PATH) != null) {
                    if (checkVersionValid(this.myCtx) && checkAndroidNotBelowN()) {
                        dLog("IWXAPI.getWXAppSupportAPI() \u63a5\u53e3\u83b7\u53d6\u5230\u7684\u503c >= 0x27000D00");
                        String string = shareArgs.getValue(ShareArgs.IMG_PATH).toString();
                        dLog("filePath:" + string);
                        String fileUri = getFileUri(this.myCtx, new File(string));
                        dLog("contentPath:" + fileUri);
                        wXImageObject.setImagePath(fileUri);
                    } else {
                        wXImageObject.setImagePath(shareArgs.getValue(ShareArgs.IMG_PATH).toString());
                    }
                } else {
                    dLog("do not support url image");
                }
                wXWebpageObject = wXImageObject;
            }
        } else if (shareArgs.getValue("url") != null || "TYPE_LINK".equals(shareArgs.getValue("type"))) {
            dLog("case link");
            wXWebpageObject = new WXWebpageObject(shareArgs.getValue("url").toString());
        } else if (shareArgs.getValue(ShareArgs.VIDEO_URL) != null || "TYPE_VIDEO".equals(shareArgs.getValue("type"))) {
            dLog("case video");
            WXVideoObject wXVideoObject = new WXVideoObject();
            wXVideoObject.videoUrl = shareArgs.getValue(ShareArgs.VIDEO_URL).toString();
            wXWebpageObject = wXVideoObject;
        } else {
            dLog("case text");
            wXWebpageObject = new WXTextObject(shareArgs.getValue("text").toString());
        }
        if (bitmapScaleBitmap != null) {
            dLog("thumb width:" + bitmapScaleBitmap.getWidth() + ", height:" + bitmapScaleBitmap.getHeight());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmapScaleBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            wXMediaMessage.thumbData = byteArrayOutputStream.toByteArray();
        } else {
            dLog("setShareThumb/THUMB_DATA null, please set value");
        }
        wXMediaMessage.mediaObject = wXWebpageObject;
        wXMediaMessage.title = shareArgs.getValue("title").toString();
        wXMediaMessage.description = shareArgs.getValue("text").toString();
        return wXMediaMessage;
    }

    public String getFileUri(Context context, File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        Uri uriForFile = UniNgshareFileProvider.getUriForFile(context, context.getPackageName() + ".uningshare.fileprovider", file);
        dLog("contentUri:" + uriForFile);
        context.grantUriPermission("com.tencent.mm", uriForFile, 1);
        return uriForFile.toString();
    }

    @Override // com.netease.ntsharesdk.Platform
    public void share(ShareArgs shareArgs) {
        this.argsn = shareArgs;
        if (!this.api.isWXAppInstalled()) {
            shareArgs.setFailMsg("App not installed");
            dLog("app not installed");
            if (this.shareEndListener != null) {
                this.shareEndListener.onShareEnd(getPlatformName(), 3, shareArgs);
                return;
            }
            return;
        }
        dLog("ShareArgs.TYPE:" + shareArgs.getValue("type"));
        if (ShareArgs.TYPE_MINI_PROGRAM_SUBSCRIBE.equals(shareArgs.getValue("type"))) {
            dLog("to mini program subscribe");
            SubscribeMiniProgramMsg.Req req = new SubscribeMiniProgramMsg.Req();
            req.miniProgramAppId = (String) shareArgs.getValue(ShareArgs.USER_NAME);
            boolean z = this.api.getWXAppSupportAPI() >= 620823808;
            dLog("supported:" + z);
            if (z) {
                dLog("sendReq:" + String.format("sendReq ret : %s", Boolean.valueOf(this.api.sendReq(req))));
                pushShareTranscation(req.transaction, shareArgs);
                return;
            }
            if (this.shareEndListener != null) {
                this.shareEndListener.onShareEnd(getPlatformName(), 2, shareArgs);
                return;
            }
            return;
        }
        if (shareArgs != null && ShareArgs.TYPE_TO_MINI_PROGRAM.equals(shareArgs.getValue("type"))) {
            WXLaunchMiniProgram.Req req2 = new WXLaunchMiniProgram.Req();
            req2.userName = (String) shareArgs.getValue(ShareArgs.USER_NAME);
            req2.path = (String) shareArgs.getValue(ShareArgs.PATH);
            req2.miniprogramType = Integer.valueOf((String) shareArgs.getValue(ShareArgs.MINI_PROGRAM_TYPE)).intValue();
            this.api.sendReq(req2);
            dLog("\u5f00\u59cb\u8c03\u7528\u5c0f\u7a0b\u5e8f ");
            dLog("\u8c03\u7528\u7248\u672c\u4e3a0\u6b63\u5f0f\u7248,1\u5f00\u53d1\u7248,2\u4f53\u9a8c\u7248\uff1a" + shareArgs.getValue(ShareArgs.MINI_PROGRAM_TYPE));
            pushShareTranscation(req2.transaction, shareArgs);
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
        WXMediaMessage wXMediaMessage = (WXMediaMessage) genMessage(shareArgs);
        SendMessageToWX.Req req3 = new SendMessageToWX.Req();
        req3.message = wXMediaMessage;
        req3.transaction = String.valueOf(System.currentTimeMillis());
        if (shareArgs.getValue(ShareArgs.TO_BLOG) != null && shareArgs.getValue(ShareArgs.TO_BLOG).toString().length() > 0) {
            dLog("SendMessageToWX.Req.WXSceneTimeline");
            req3.scene = 1;
        }
        dLog("share result " + Boolean.valueOf(this.api.sendReq(req3)));
        pushShareTranscation(req3.transaction, shareArgs);
    }

    @Override // com.netease.ntsharesdk.Platform
    public Boolean checkArgs(ShareArgs shareArgs) {
        WXMediaMessage wXMediaMessage = (WXMediaMessage) genMessage(shareArgs);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = wXMediaMessage;
        String str = !req.checkArgs() ? "ShareArgs wrong!" : "";
        if (shareArgs.hasImage().booleanValue() && shareArgs.getValue(ShareArgs.IMG_URL) != null) {
            str = "ShareArgs wrong! Not support img_url";
        }
        if (str.length() > 0) {
            dLog(str);
            shareArgs.setFailMsg(str);
            return false;
        }
        return true;
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
        this.api = WXAPIFactory.createWXAPI(this.myCtx, null);
        String config = getConfig("app_id");
        if (!TextUtils.isEmpty(SdkMgr.getInst().getPropStr("NG_SHARE_ALL_CHANNEL_CONFIGURATION"))) {
            try {
                JSONObject jSONObjectOptJSONObject = new JSONObject(SdkMgr.getInst().getPropStr("NG_SHARE_ALL_CHANNEL_CONFIGURATION")).optJSONObject(Platform.WEIXIN);
                if (jSONObjectOptJSONObject != null && !TextUtils.isEmpty(jSONObjectOptJSONObject.optString("app_id"))) {
                    config = jSONObjectOptJSONObject.optString("app_id");
                }
            } catch (Exception unused) {
                dLog(getPlatformName() + " CONFIGURATION error");
            }
        }
        dLog("platform: " + getPlatformName() + " init sdk app_id:" + config);
        this.api.registerApp(config);
        this.initContextString = context.toString();
        this.hasInit = true;
    }

    @Override // com.netease.ntsharesdk.Platform
    public Object getAPIInst() {
        return this.api;
    }

    @Override // com.netease.ntsharesdk.Platform
    public void handleRequest(Object obj) {
        dLog("handleRequest: " + obj);
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00a5  */
    /* JADX WARN: Removed duplicated region for block: B:55:? A[RETURN, SYNTHETIC] */
    @Override // com.netease.ntsharesdk.Platform
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void handleResponse(java.lang.Object r7) {
        /*
            r6 = this;
            com.tencent.mm.opensdk.modelbase.BaseResp r7 = (com.tencent.mm.opensdk.modelbase.BaseResp) r7
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "handleResponse:"
            r0.append(r1)
            java.lang.String r1 = r7.toString()
            r0.append(r1)
            java.lang.String r1 = ", errCode:"
            r0.append(r1)
            int r1 = r7.errCode
            r0.append(r1)
            java.lang.String r1 = ",errStr:"
            r0.append(r1)
            java.lang.String r1 = r7.errStr
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            dLog(r0)
            java.lang.String r0 = r7.transaction
            com.netease.ntsharesdk.ShareArgs r0 = r6.popShareTransaction(r0)
            if (r0 != 0) goto L37
            return
        L37:
            int r1 = r7.getType()
            r2 = 23
            r3 = 0
            if (r1 != r2) goto L60
            java.lang.String r1 = "ntsharesdk"
            java.lang.String r2 = "StartLaunchActivity"
            android.util.Log.d(r1, r2)
            java.lang.String r2 = r6.getPackageName()
            android.util.Log.d(r1, r2)
            android.content.pm.PackageManager r1 = r6.getPackageManager()
            java.lang.String r2 = r6.getPackageName()
            android.content.Intent r1 = r1.getLaunchIntentForPackage(r2)
            r6.startActivity(r1)
            r6.overridePendingTransition(r3, r3)
        L60:
            int r1 = r7.getType()
            r2 = 19
            if (r1 != r2) goto L72
            r1 = r7
            com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram$Resp r1 = (com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram.Resp) r1
            java.lang.String r1 = r1.extMsg
            java.lang.String r2 = "MINI_RESPONSE"
            r0.setValue(r2, r1)
        L72:
            r1 = 0
            int r2 = r7.errCode
            r4 = -4
            r5 = 2
            if (r2 == r4) goto L94
            r7 = -2
            if (r2 == r7) goto L8d
            if (r2 == 0) goto L87
            java.lang.String r7 = "OnShareEndListener.FAILED"
            dLog(r7)
            java.lang.String r1 = "unknown error"
        L85:
            r3 = r5
            goto L9c
        L87:
            java.lang.String r7 = "OnShareEndListener.OK"
            dLog(r7)
            goto L9c
        L8d:
            r3 = 1
            java.lang.String r7 = "OnShareEndListener.CANCEL"
            dLog(r7)
            goto L9c
        L94:
            java.lang.String r1 = "OnShareEndListener.FAILED, ErrCode.ERR_AUTH_DENIED"
            dLog(r1)
            java.lang.String r1 = r7.errStr
            goto L85
        L9c:
            if (r1 == 0) goto La1
            r0.setFailMsg(r1)
        La1:
            com.netease.ntsharesdk.OnShareEndListener r7 = r6.shareEndListener
            if (r7 == 0) goto Lae
            com.netease.ntsharesdk.OnShareEndListener r7 = r6.shareEndListener
            java.lang.String r1 = r6.getPlatformName()
            r7.onShareEnd(r1, r3, r0)
        Lae:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntsharesdk.platform.Weixin.handleResponse(java.lang.Object):void");
    }

    @Override // com.netease.ntsharesdk.Platform
    public boolean hasPlatformInstall(String str) {
        try {
            if (this.api != null) {
                dLog("hasPlatformInstall Weixin:" + this.api.isWXAppInstalled());
                return this.api.isWXAppInstalled();
            }
        } catch (Exception e) {
            dLog("hasPlatformInstall Exception:" + e.getMessage());
        }
        dLog("hasPlatformInstall api null:");
        return false;
    }

    @Override // com.netease.ntsharesdk.Platform
    public void updateApi(String str, Context context) {
        this.api.unregisterApp();
        this.api = WXAPIFactory.createWXAPI(this.myCtx, null);
        this.api.registerApp(str);
        this.myCtx = context;
        this.mConf = readConfig(this.myCtx, getPlatformName());
        this.hasInit = true;
        this.initContextString = context.toString();
    }

    public boolean checkVersionValid(Context context) {
        return this.api.getWXAppSupportAPI() >= 654314752;
    }

    public boolean checkAndroidNotBelowN() {
        return Build.VERSION.SDK_INT >= 24;
    }
}