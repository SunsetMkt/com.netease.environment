package com.netease.pharos.network;

import android.text.TextUtils;
import com.netease.ntunisdk.base.ConstProp;

/* loaded from: classes5.dex */
public enum NetworkType {
    INIT(-1, "init"),
    NONE(0, "none"),
    WIFI(1, "wifi"),
    MOBILE(2, ConstProp.NT_AUTH_NAME_MOBILE);

    private int flag;
    private String value;

    NetworkType(int i, String str) {
        this.flag = i;
        this.value = str;
    }

    public String value() {
        return this.value;
    }

    public int flag() {
        return this.flag;
    }

    public static NetworkType getNetwork(String str) {
        if (TextUtils.isEmpty(str)) {
            return NONE;
        }
        if (str.equalsIgnoreCase("init")) {
            return INIT;
        }
        if (str.equalsIgnoreCase(ConstProp.NT_AUTH_NAME_MOBILE)) {
            return MOBILE;
        }
        if (str.equalsIgnoreCase("wifi")) {
            return WIFI;
        }
        return NONE;
    }

    public static NetworkType getNetwork(int i) {
        if (i == -1) {
            return INIT;
        }
        if (i == 1) {
            return WIFI;
        }
        if (i == 2) {
            return MOBILE;
        }
        return NONE;
    }
}