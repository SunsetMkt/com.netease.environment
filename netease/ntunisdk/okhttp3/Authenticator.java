package com.netease.ntunisdk.okhttp3;

import java.io.IOException;
import javax.annotation.Nullable;

/* loaded from: classes2.dex */
public interface Authenticator {
    public static final Authenticator NONE = new Authenticator() { // from class: com.netease.ntunisdk.okhttp3.Authenticator.1
        @Override // com.netease.ntunisdk.okhttp3.Authenticator
        public Request authenticate(@Nullable Route route, Response response) {
            return null;
        }
    };

    @Nullable
    Request authenticate(@Nullable Route route, Response response) throws IOException;
}