package com.netease.ntsharesdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.netease.ntsharesdk.platform.QQ;
import com.netease.ntsharesdk.platform.Weibo;
import com.netease.ntsharesdk.platform.Weixin;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.sina.weibo.BuildConfig;
import java.util.HashMap;

/* loaded from: classes.dex */
public class ShareMgr {
    private static final String TAG = "UniSDK ngshare";
    private static ShareMgr inst = new ShareMgr();
    public boolean firstWeibo = true;
    public HashMap<String, Boolean> platformInstalled = new HashMap<>();
    private String shareViewTitle = "\u5206\u4eab";
    private OnShareEndListener shareEndListener = null;
    private Context myCtx = null;

    public void clearPlatformMap() {
    }

    public static ShareMgr getInst() {
        return inst;
    }

    private ShareMgr() {
    }

    public Boolean hasPlatform(String str) {
        if (str.equals("QQ")) {
            return Boolean.valueOf(isAppInstalled(this.myCtx, "com.tencent.mobileqq"));
        }
        if (str.equals(Platform.WEIBO)) {
            return Boolean.valueOf(isAppInstalled(this.myCtx, BuildConfig.APPLICATION_ID));
        }
        if (str.equals(Platform.WEIXIN)) {
            return Boolean.valueOf(isAppInstalled(this.myCtx, "com.tencent.mm"));
        }
        return false;
    }

    private boolean isAppInstalled(Context context, String str) throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo;
        if (!this.platformInstalled.containsKey(str)) {
            try {
                packageInfo = context.getPackageManager().getPackageInfo(str, 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                packageInfo = null;
            }
            if (packageInfo == null) {
                this.platformInstalled.put(str, false);
                return false;
            }
            this.platformInstalled.put(str, true);
            return true;
        }
        UniSdkUtils.d(TAG, "packagename:" + str + " has already getInstalled:" + this.platformInstalled.get(str));
        return this.platformInstalled.get(str).booleanValue();
    }

    public void share(ShareArgs shareArgs, String str, Activity activity) throws InterruptedException {
        Platform.dLog("ShareArgs:" + shareArgs + "pfName:" + str);
        StringBuilder sb = new StringBuilder();
        sb.append("myCtx:");
        sb.append(this.myCtx.toString());
        UniSdkUtils.d(TAG, sb.toString());
        Activity activity2 = (Activity) this.myCtx;
        if (Platform.WEIXIN.equals(str) && Weixin.getInst() != null) {
            Weixin.getInst().initSdk(this.myCtx);
            Weixin.getInst().setShareEndListener(this.shareEndListener);
            Weixin.getInst().share(shareArgs, activity2);
            UniSdkUtils.d(TAG, "sdk share to:Weixin");
            return;
        }
        if ("QQ".equals(str) && QQ.getInst() != null) {
            QQ.getInst().initSdk(this.myCtx);
            QQ.getInst().setShareEndListener(this.shareEndListener);
            QQ.getInst().callQQResult = true;
            QQ.getInst().share(shareArgs, activity2);
            UniSdkUtils.d(TAG, "sdk share to:QQ");
            return;
        }
        if (!Platform.WEIBO.equals(str) || Weibo.getInst() == null) {
            return;
        }
        Weibo.getInst().initSdk(this.myCtx);
        Weibo.getInst().setShareEndListener(this.shareEndListener);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Weibo.getInst().callWeiboResult = true;
        Weibo.getInst().share(shareArgs, activity2);
        UniSdkUtils.d(TAG, "sdk share to:Weibo");
    }

    public void share(ShareArgs shareArgs, String str) throws InterruptedException {
        share(shareArgs, str, (Activity) this.myCtx);
    }

    public void setShareViewTitle(String str) {
        this.shareViewTitle = str;
    }

    public void handleIntent(Intent intent) {
        if (Weixin.getInst() != null) {
            Weixin.getInst().handleIntent(intent);
        }
        if (Weibo.getInst() != null) {
            Weibo.getInst().handleIntent(intent);
        }
        if (QQ.getInst() != null) {
            QQ.getInst().handleIntent(intent);
        }
    }

    public void handleActivityResult(int i, int i2, Intent intent) {
        if (Weixin.getInst() != null) {
            Weixin.getInst().handleActivityResult(i, i2, intent);
        }
        if (Weibo.getInst() != null && Weibo.getInst().callWeiboResult) {
            Weibo.getInst().callWeiboResult = false;
            Weibo.getInst().handleActivityResult(i, i2, intent);
        }
        if (QQ.getInst() == null || !QQ.getInst().callQQResult) {
            return;
        }
        QQ.getInst().callQQResult = false;
        QQ.getInst().handleActivityResult(i, i2, intent);
    }

    public void setShareEndListener(OnShareEndListener onShareEndListener) {
        this.shareEndListener = onShareEndListener;
    }

    public void setContext(Context context) {
        this.myCtx = context;
    }

    public void updateApi(String str, String str2) {
        if (hasPlatform(str2).booleanValue()) {
            Platform.dLog("updateApi platform : " + str2 + ", api : " + str);
            if (Integer.toString(101).equals(str2) || Integer.toString(102).equals(str2) || Integer.toString(118).equals(str2)) {
                if (Weixin.getInst() != null) {
                    Weixin.getInst().updateApi(str, this.myCtx);
                }
            } else {
                if (Integer.toString(103).equals(str2) || Integer.toString(104).equals(str2)) {
                    return;
                }
                if (Integer.toString(105).equals(str2) || Integer.toString(106).equals(str2)) {
                    if (QQ.getInst() != null) {
                        QQ.getInst().updateApi(str, this.myCtx);
                    }
                } else if ((Integer.toString(100).equals(str2) || Integer.toString(117).equals(str2)) && Weibo.getInst() != null) {
                    Weibo.getInst().updateApi(str, this.myCtx);
                }
            }
        }
    }
}