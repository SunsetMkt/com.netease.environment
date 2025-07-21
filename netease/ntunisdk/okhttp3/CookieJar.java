package com.netease.ntunisdk.okhttp3;

import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public interface CookieJar {
    public static final CookieJar NO_COOKIES = new CookieJar() { // from class: com.netease.ntunisdk.okhttp3.CookieJar.1
        @Override // com.netease.ntunisdk.okhttp3.CookieJar
        public List<Cookie> loadForRequest(HttpUrl httpUrl) {
            return Collections.emptyList();
        }

        @Override // com.netease.ntunisdk.okhttp3.CookieJar
        public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
        }
    };

    List<Cookie> loadForRequest(HttpUrl httpUrl);

    void saveFromResponse(HttpUrl httpUrl, List<Cookie> list);
}