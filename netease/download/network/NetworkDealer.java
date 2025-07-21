package com.netease.download.network;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/* loaded from: classes5.dex */
public interface NetworkDealer<T> {
    T processContent(HttpURLConnection httpURLConnection, int i, String str, Map<String, String> map) throws Exception;

    int processHeader(Map<String, List<String>> map, int i, String str);
}