package com.netease.ntunisdk.external.protocol.data;

import com.netease.ntunisdk.external.protocol.utils.TextCompat;
import java.io.File;

/* loaded from: classes.dex */
public class ProtocolFile {
    public File cacheDir;
    public String key;
    public String url;

    public ProtocolFile(String str) {
        this.url = str;
        this.key = TextCompat.md5(str);
        this.cacheDir = new File(SDKRuntime.getInstance().getCacheDir(), this.key);
    }

    public boolean checkProtocolExist(String str) {
        String strMd5 = TextCompat.md5(str);
        if (this.cacheDir.exists()) {
            return new File(this.cacheDir, strMd5).exists();
        }
        return false;
    }

    public ProtocolInfo getLocalMainProtocol() {
        return getLocalProtocol(this.url);
    }

    public ProtocolInfo getLocalProtocol(String str) {
        ProtocolInfo protocol = SDKRuntime.getInstance().getProtocol(str);
        if (protocol != null || !checkProtocolExist(str)) {
            return protocol;
        }
        ProtocolInfo localProtocol = ProtocolParser.readLocalProtocol(this, str);
        SDKRuntime.getInstance().addProtocolIntoMemory(localProtocol);
        return localProtocol;
    }
}