package com.netease.androidcrashhandler;

import com.netease.androidcrashhandler.Const;
import com.netease.androidcrashhandler.other.OtherCore;
import com.netease.androidcrashhandler.other.OtherProxy;
import com.netease.androidcrashhandler.util.LogUtils;
import java.io.File;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class MyPostEntity {
    private static final String TAG = "MyPostEntity";
    private static MyPostEntity sMyNetworkUtils;
    private HashMap<String, String> mParamsMap = new HashMap<>();
    private boolean isMain = false;

    private MyPostEntity() {
    }

    public static MyPostEntity getInstance() {
        if (sMyNetworkUtils == null) {
            sMyNetworkUtils = new MyPostEntity();
        }
        return sMyNetworkUtils;
    }

    public void setParam(String str, String str2) throws NumberFormatException {
        LogUtils.i(LogUtils.TAG, "AndroidCrashHandler MyPostEntity [setParam] NTCrashHunterKit is init =  " + NTCrashHunterKit.sharedKit().isInit());
        LogUtils.i(LogUtils.TAG, "AndroidCrashHandler MyPostEntity [setParam] key =  " + str + ", value=" + str2);
        if (NTCrashHunterKit.sharedKit().isInit()) {
            if (Const.ParamKey.CALLBACK_SO_PATH.equals(str) || Const.ParamKey.CALLBACK_METHOD_NAME.equals(str)) {
                LogUtils.i(LogUtils.TAG, "AndroidCrashHandler [setParam] setSoParam");
                NTCrashHunterKit.sharedKit().setSoParam(str, str2);
                return;
            } else {
                NTCrashHunterKit.sharedKit().setParam(str, str2);
                return;
            }
        }
        this.mParamsMap.put(str, str2);
    }

    public HashMap<String, String> getParams() {
        return this.mParamsMap;
    }

    public void setURL(String str) {
        NTCrashHunterKit.sharedKit().setUrl(str);
    }

    public void setFile(String str, String str2, String str3) {
        LogUtils.i(LogUtils.TAG, "AndroidCrashHandler MyPostEntity [setFile] \u4ece\u65e7\u63a5\u53e3\u542f\u52a8\u4e0a\u4f20 " + str2 + ", \u5b57\u7b26\u4e32\u5f62\u5f0f");
        if (NTCrashHunterKit.sharedKit().getContext() != null) {
            if (this.isMain) {
                OtherCore otherCore = new OtherCore();
                otherCore.addMainInfo(str, str2);
                OtherProxy.getInstance().put(otherCore);
            } else {
                OtherCore otherCore2 = new OtherCore();
                otherCore2.addInfo(str, str2);
                OtherProxy.getInstance().put(otherCore2);
            }
        }
    }

    public MyPostEntity(MyPostEntity myPostEntity) {
    }

    public void setFile(File file, String str, String str2) {
        LogUtils.i(LogUtils.TAG, "AndroidCrashHandler MyPostEntity [setFile] \u4ece\u65e7\u63a5\u53e3\u542f\u52a8\u4e0a\u4f20 " + str + ", \u6587\u4ef6\u5f62\u5f0f");
        if (NTCrashHunterKit.sharedKit().getContext() != null) {
            if (this.isMain) {
                OtherCore otherCore = new OtherCore();
                otherCore.addMainFile(file.getAbsolutePath(), str);
                OtherProxy.getInstance().put(otherCore);
            } else {
                OtherCore otherCore2 = new OtherCore();
                otherCore2.addMinorFile(file.getAbsolutePath(), str);
                OtherProxy.getInstance().put(otherCore2);
            }
        }
    }
}