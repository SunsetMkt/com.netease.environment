package com.netease.pharos;

import android.text.TextUtils;

/* loaded from: classes2.dex */
public class ServerProxy {
    private static final String PRODUCTION_HOST_MAINLAND = "impression.update.netease.com";
    private static final String PRODUCTION_HOST_OVERSEA = "impression.update.easebar.com";
    private static final String TEST_HOST = "pharossdk.x.netease.com";
    private static ServerProxy sInstance;
    private String NET_AREA_URL;
    private String QOS_LIGHTEN_URL;
    private String REGION_CONFIG_URL;
    private boolean isTestMode = false;

    private ServerProxy() {
    }

    public static ServerProxy getInstance() {
        ServerProxy serverProxy;
        ServerProxy serverProxy2 = sInstance;
        if (serverProxy2 != null) {
            return serverProxy2;
        }
        synchronized (ServerProxy.class) {
            if (sInstance == null) {
                sInstance = new ServerProxy();
            }
            serverProxy = sInstance;
        }
        return serverProxy;
    }

    public void setTestMode(boolean z) {
        if (this.isTestMode != z) {
            this.isTestMode = z;
            this.REGION_CONFIG_URL = null;
            this.QOS_LIGHTEN_URL = null;
            this.NET_AREA_URL = null;
        }
    }

    public String getHost() {
        return this.isTestMode ? TEST_HOST : PharosProxy.getInstance().isOversea() ? PRODUCTION_HOST_OVERSEA : PRODUCTION_HOST_MAINLAND;
    }

    public String getRegionConfigUrl() {
        if (!TextUtils.isEmpty(this.REGION_CONFIG_URL)) {
            return this.REGION_CONFIG_URL;
        }
        return "https://" + getHost() + "/pharos/explore/%x_%s.txt";
    }

    public String getQosLightenUrl() {
        if (!TextUtils.isEmpty(this.QOS_LIGHTEN_URL)) {
            return this.QOS_LIGHTEN_URL;
        }
        return "https://" + getHost() + "/lighten/%s/%s.txt";
    }

    public String getNetAreaUrl() {
        if (!TextUtils.isEmpty(this.NET_AREA_URL)) {
            return this.NET_AREA_URL;
        }
        return "https://" + getHost() + "/pharos/v%s/%s.txt";
    }
}