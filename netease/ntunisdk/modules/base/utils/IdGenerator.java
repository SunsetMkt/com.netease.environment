package com.netease.ntunisdk.modules.base.utils;

import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public class IdGenerator {
    private static AtomicInteger count = new AtomicInteger(100);

    public static int getUniqueId() {
        return count.incrementAndGet();
    }
}