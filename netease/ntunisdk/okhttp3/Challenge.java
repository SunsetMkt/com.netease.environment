package com.netease.ntunisdk.okhttp3;

import com.netease.cloud.nos.android.constants.Code;
import com.netease.ntunisdk.okhttp3.internal.Util;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.james.mime4j.field.ContentTypeField;

/* loaded from: classes2.dex */
public final class Challenge {
    private final Map<String, String> authParams;
    private final String scheme;

    public Challenge(String str, String str2) {
        if (str == null) {
            throw new NullPointerException("scheme == null");
        }
        if (str2 == null) {
            throw new NullPointerException("realm == null");
        }
        this.scheme = str;
        this.authParams = Collections.singletonMap("realm", str2);
    }

    public Challenge(String str, Map<String, String> map) {
        if (str == null) {
            throw new NullPointerException("scheme == null");
        }
        if (map == null) {
            throw new NullPointerException("authParams == null");
        }
        this.scheme = str;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            linkedHashMap.put(entry.getKey() == null ? null : entry.getKey().toLowerCase(Locale.US), entry.getValue());
        }
        this.authParams = Collections.unmodifiableMap(linkedHashMap);
    }

    public Map<String, String> authParams() {
        return this.authParams;
    }

    public Charset charset() {
        String str = this.authParams.get(ContentTypeField.PARAM_CHARSET);
        if (str != null) {
            try {
                return Charset.forName(str);
            } catch (Exception unused) {
            }
        }
        return Util.ISO_8859_1;
    }

    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Challenge) {
            Challenge challenge = (Challenge) obj;
            if (challenge.scheme.equals(this.scheme) && challenge.authParams.equals(this.authParams)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return ((Code.HTTP_NO_RESPONSE + this.scheme.hashCode()) * 31) + this.authParams.hashCode();
    }

    public String realm() {
        return this.authParams.get("realm");
    }

    public String scheme() {
        return this.scheme;
    }

    public String toString() {
        return this.scheme + " authParams=" + this.authParams;
    }

    public Challenge withCharset(Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset == null");
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap(this.authParams);
        linkedHashMap.put(ContentTypeField.PARAM_CHARSET, charset.name());
        return new Challenge(this.scheme, linkedHashMap);
    }
}