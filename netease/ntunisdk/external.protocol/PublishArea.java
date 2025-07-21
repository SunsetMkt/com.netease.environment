package com.netease.ntunisdk.external.protocol;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
/* loaded from: classes.dex */
public @interface PublishArea {
    public static final int PUBLISH_KOREA = 4;
    public static final int PUBLISH_MAIN_LAND = 0;
    public static final int PUBLISH_MIDDLE_EAST = 3;
    public static final int PUBLISH_NONE = -1;
    public static final int PUBLISH_OVERSEA = 1;
    public static final int PUBLISH_OVERSEA_TEST = 99;
    public static final int PUBLISH_TAIWAN = 2;
}