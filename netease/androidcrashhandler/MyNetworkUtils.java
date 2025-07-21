package com.netease.androidcrashhandler;

/* loaded from: classes2.dex */
public class MyNetworkUtils {
    private static final String TAG = "MyNetworkUtils";
    private static MyNetworkUtils sMyNetworkUtils;

    public void post(MyPostEntity myPostEntity) {
    }

    public void postOtherError(MyPostEntity myPostEntity) {
    }

    public void postScriptError(MyPostEntity myPostEntity) {
    }

    public void postUserInfo(String str, String str2, String str3) {
    }

    public void postUserInfo(String str, String str2, String str3, String str4) {
    }

    private MyNetworkUtils() {
    }

    public static MyNetworkUtils getInstance() {
        if (sMyNetworkUtils == null) {
            sMyNetworkUtils = new MyNetworkUtils();
        }
        return sMyNetworkUtils;
    }

    public MyPostEntity getDefaultPostEntity() {
        return MyPostEntity.getInstance();
    }
}