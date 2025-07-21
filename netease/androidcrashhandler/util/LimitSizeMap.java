package com.netease.androidcrashhandler.util;

import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: classes4.dex */
public class LimitSizeMap<K, V> extends LinkedHashMap<K, V> {
    private int QUEUE_MAX_SIZE;

    public LimitSizeMap(int i) {
        super(i + 1, 1.0f);
        this.QUEUE_MAX_SIZE = i;
    }

    @Override // java.util.LinkedHashMap
    protected boolean removeEldestEntry(Map.Entry<K, V> entry) {
        return size() > this.QUEUE_MAX_SIZE;
    }
}