package com.netease.mpay.auth.plugins;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
/* loaded from: classes.dex */
public @interface PluginStatus {
    public static final int FAILED = 1;
    public static final int SUCCESS = 0;
}