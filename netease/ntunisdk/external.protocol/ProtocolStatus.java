package com.netease.ntunisdk.external.protocol;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
/* loaded from: classes.dex */
public @interface ProtocolStatus {
    public static final int HAS_ACCEPT = 1;
    public static final int HAS_CHECK_UPDATE = 2;
    public static final int NEED_ACCEPT_UPDATE = 3;
    public static final int NEW_PROTOCOL = 4;
    public static final int NO_ACCEPT = 0;
}