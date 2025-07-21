package com.netease.ntunisdk.base.constant;

import com.alipay.sdk.m.x.d;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.external.protocol.data.User;

/* compiled from: Features.java */
/* loaded from: classes6.dex */
public enum b {
    Logout(User.USER_NAME_LOGOUT, ConstProp.MODE_HAS_LOGOUT),
    Manager("manager", ConstProp.MODE_HAS_MANAGER),
    Exit(d.z, ConstProp.MODE_EXIT_VIEW),
    Share("share", ConstProp.MODE_HAS_SHARE);

    public String e;
    public String f;

    b(String str, String str2) {
        this.e = str;
        this.f = str2;
    }
}