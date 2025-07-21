package com.netease.ntunisdk.external.protocol.data;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class Config {
    public String defaultLanguage;
    public ProtocolConfig defaultProtocolConfig;
    public ArrayList<ProtocolConfig> protocolConfigs = new ArrayList<>();

    public static class ProtocolConfig {
        public int id;
        public String language;
        public String tag;
        public String url;
        public boolean isLauncherShow = false;
        public ArrayList<String> countries = new ArrayList<>();
    }

    public ProtocolConfig getProtocolConfig(String str, String str2, String str3) {
        if (!TextUtils.isEmpty(str)) {
            Iterator<ProtocolConfig> it = this.protocolConfigs.iterator();
            while (it.hasNext()) {
                ProtocolConfig next = it.next();
                if (next.countries.contains(str)) {
                    return next;
                }
            }
        } else {
            Iterator<ProtocolConfig> it2 = this.protocolConfigs.iterator();
            while (it2.hasNext()) {
                ProtocolConfig next2 = it2.next();
                if (next2.language.equals(str2)) {
                    return next2;
                }
            }
        }
        if (TextUtils.isEmpty(str3)) {
            return this.defaultProtocolConfig;
        }
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.url = str3;
        return protocolConfig;
    }
}