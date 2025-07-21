package com.netease.cc.ccplayerwrapper;

import java.util.HashMap;
import java.util.Map;

/* loaded from: classes4.dex */
public class VideoConfig {
    private String anchorCCid;
    private String cdn;
    private int clientType;
    private String coPlatform;
    private boolean devMode;
    private boolean enableLog;
    private boolean enableMediaCodec;
    private boolean enableStat;
    private Map<String, String> headers;
    private long maxCacheMs;
    private String mobileurl;
    private String osName;
    private String platform;
    private float playbackSpeed;
    private String playurl;
    private int radicalSpeedLevel;
    private boolean resetWhenComplete;
    private int scaleMode;
    private long seekAtStartMs;
    private String src;
    private VIDEO_TYPE type;
    private UserInfo userInfo;
    private String vbr;
    private int vbrChooseByUser;
    private int vbrValue;

    public enum VIDEO_TYPE {
        LIVE_CCID,
        LIVE_MOBILE_URL,
        LIVE_URL,
        VOD_URL,
        VOD_RECORDID
    }

    public void copy(VideoConfig videoConfig) {
        this.type = videoConfig.type;
        this.anchorCCid = videoConfig.anchorCCid;
        this.playurl = videoConfig.playurl;
        copyHeaders(videoConfig.headers);
        this.mobileurl = videoConfig.mobileurl;
        this.vbr = videoConfig.vbr;
        this.vbrValue = videoConfig.vbrValue;
        this.vbrChooseByUser = videoConfig.vbrChooseByUser;
        this.cdn = videoConfig.cdn;
        this.devMode = videoConfig.devMode;
        this.enableLog = videoConfig.enableLog;
        this.enableMediaCodec = videoConfig.enableMediaCodec;
        this.radicalSpeedLevel = videoConfig.radicalSpeedLevel;
        this.scaleMode = videoConfig.scaleMode;
        this.seekAtStartMs = videoConfig.seekAtStartMs;
        setPlaybackSpeed(videoConfig.playbackSpeed);
        this.src = videoConfig.src;
        this.coPlatform = videoConfig.coPlatform;
        this.platform = videoConfig.platform;
        this.osName = videoConfig.osName;
        this.clientType = videoConfig.clientType;
        this.enableStat = videoConfig.enableStat;
        this.resetWhenComplete = videoConfig.resetWhenComplete;
        this.maxCacheMs = videoConfig.maxCacheMs;
        this.userInfo.copy(videoConfig.userInfo);
    }

    public void copyHeaders(Map<String, String> map) {
        if (map != null) {
            this.headers = new HashMap();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                this.headers.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public String getAnchorCCid() {
        return this.anchorCCid;
    }

    public String getCdn() {
        return this.cdn;
    }

    public int getClientType() {
        return this.clientType;
    }

    public String getCoPlatform() {
        return this.coPlatform;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public long getMaxCacheMs() {
        return this.maxCacheMs;
    }

    public String getMobileurl() {
        String str = this.mobileurl;
        if (str != null && !str.isEmpty()) {
            return this.mobileurl;
        }
        if (isDevMode()) {
            return "http://vapi.dev.cc.163.com/video_play_url_mobile/" + this.anchorCCid;
        }
        return "http://cgi.v.cc.163.com/video_play_url_mobile/" + this.anchorCCid;
    }

    public String getOsName() {
        return this.osName;
    }

    public String getPlatform() {
        return this.platform;
    }

    public float getPlaybackSpeed() {
        return this.playbackSpeed;
    }

    public String getPlayurl() {
        return this.playurl;
    }

    public int getRadicalSpeedLevel() {
        return this.radicalSpeedLevel;
    }

    public int getScaleMode() {
        return this.scaleMode;
    }

    public long getSeekAtStartMs() {
        return this.seekAtStartMs;
    }

    public String getSrc() {
        return this.src;
    }

    public VIDEO_TYPE getType() {
        return this.type;
    }

    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    public String getVbr() {
        return this.vbr;
    }

    public int getVbrChooseByUser() {
        return this.vbrChooseByUser;
    }

    public int getVbrValue() {
        return this.vbrValue;
    }

    public boolean isDevMode() {
        return this.devMode;
    }

    public boolean isEnableLog() {
        return this.enableLog;
    }

    public boolean isEnableMediaCodec() {
        return this.enableMediaCodec;
    }

    public boolean isResetWhenComplete() {
        return this.resetWhenComplete;
    }

    public boolean isStatEnable() {
        return this.enableStat;
    }

    public void setAnchorCCid(String str) {
        this.anchorCCid = str;
    }

    public void setCdn(String str) {
        this.cdn = str;
    }

    public void setClientType(int i) {
        this.clientType = i;
    }

    public void setDevMode(boolean z) {
        this.devMode = z;
    }

    public void setEnableLog(boolean z) {
        this.enableLog = z;
    }

    public void setEnableMediaCodec(boolean z) {
        this.enableMediaCodec = z;
    }

    public void setMaxCacheMs(long j) {
        this.maxCacheMs = j;
    }

    public void setMobileurl(String str) {
        this.mobileurl = str;
    }

    public void setPlatform(String str) {
        this.platform = str;
    }

    public void setPlaybackSpeed(float f) {
        if (f <= 0.0f) {
            f = 1.0f;
        }
        this.playbackSpeed = f;
    }

    public void setPlayurl(String str) {
        this.playurl = str;
    }

    public void setRadicalSpeedLevel(int i) {
        this.radicalSpeedLevel = i;
    }

    public void setScaleMode(int i) {
        this.scaleMode = i;
    }

    public void setSrc(String str) {
        this.src = str;
    }

    public void setType(VIDEO_TYPE video_type) {
        this.type = video_type;
    }

    public void setVbr(String str) {
        this.vbr = str;
    }

    public void setVbrChooseByUser(int i) {
        this.vbrChooseByUser = i;
    }

    public void setVbrValue(int i) {
        this.vbrValue = i;
    }

    public String toString() {
        return "VideoConfig{type=" + this.type + ", mobileurl='" + this.mobileurl + "', anchorCCid='" + this.anchorCCid + "', playurl='" + this.playurl + "', vbr='" + this.vbr + "', cdn='" + this.cdn + "', devMode=" + this.devMode + ", enableLog=" + this.enableLog + ", enableMediaCodec=" + this.enableMediaCodec + ", resetWhenComplete=" + this.resetWhenComplete + ", scaleMode=" + this.scaleMode + ", radicalSpeedLevel=" + this.radicalSpeedLevel + ", seekAtStartMs=" + this.seekAtStartMs + ", src='" + this.src + "', coPlatform='" + this.coPlatform + "', platform='" + this.platform + "', clientType=" + this.clientType + ", userInfo=" + this.userInfo + '}';
    }

    private VideoConfig(Builder builder) {
        this.resetWhenComplete = true;
        this.maxCacheMs = 0L;
        this.type = builder.f1528a;
        this.mobileurl = builder.b;
        this.anchorCCid = builder.c;
        this.playurl = builder.d;
        copyHeaders(builder.e);
        this.vbr = builder.f;
        this.vbrValue = builder.g;
        this.vbrChooseByUser = builder.h;
        this.cdn = builder.i;
        this.devMode = builder.j;
        this.enableLog = builder.k;
        this.enableMediaCodec = builder.l;
        this.radicalSpeedLevel = builder.m;
        this.scaleMode = builder.n;
        this.seekAtStartMs = builder.o;
        setPlaybackSpeed(builder.p);
        this.enableStat = builder.q;
        this.resetWhenComplete = builder.r;
        this.src = builder.t;
        this.coPlatform = builder.u;
        this.platform = builder.v;
        this.osName = builder.w;
        this.clientType = builder.x;
        this.maxCacheMs = builder.s;
        this.userInfo = new UserInfo(builder.y);
    }

    public static final class Builder {

        /* renamed from: a, reason: collision with root package name */
        private VIDEO_TYPE f1528a;
        private String b;
        private String c;
        private String d;
        private String f;
        private String i;
        private long o;
        private String t;
        private String u;
        private String v;
        private int x;
        private String w = "android";
        private UserInfo y = null;
        private boolean j = false;
        private boolean k = false;
        private boolean l = true;
        private Map<String, String> e = null;
        private float p = 1.0f;
        private boolean q = true;
        private boolean r = true;
        private int g = 0;
        private int h = 0;
        private long s = 0;
        private int m = 0;
        private int n = 2;

        public Builder anchorccid(String str) {
            this.c = str;
            return this;
        }

        public VideoConfig build() {
            return new VideoConfig(this);
        }

        public Builder devMode(boolean z) {
            this.j = z;
            return this;
        }

        public Builder enableLog(boolean z) {
            this.k = z;
            return this;
        }

        public Builder enableMediaCodec(boolean z) {
            this.l = z;
            return this;
        }

        public Builder enableStat(boolean z) {
            this.q = z;
            return this;
        }

        public Builder maxCacheMs(long j) {
            this.s = j;
            return this;
        }

        public Builder mobileurl(String str, String str2) {
            this.b = str;
            this.c = str2;
            return this;
        }

        public Builder playbackSpeed(float f) {
            this.p = f;
            return this;
        }

        public Builder playurl(String str) {
            this.d = str;
            return this;
        }

        public Builder radicalSpeedLevel(int i) {
            this.m = i;
            return this;
        }

        public Builder resetWhenComplete(boolean z) {
            this.r = z;
            return this;
        }

        public Builder scaleMode(int i) {
            this.n = i;
            return this;
        }

        public Builder seekAtStartMs(long j) {
            this.o = j;
            return this;
        }

        public Builder type(VIDEO_TYPE video_type) {
            this.f1528a = video_type;
            return this;
        }

        public Builder vbr(String str) {
            this.f = str;
            return this;
        }

        public Builder vbrChooseByUser(int i) {
            this.h = i;
            return this;
        }

        public Builder vbrValue(int i) {
            this.g = i;
            return this;
        }

        public Builder withHeaders(Map<String, String> map) {
            this.e = map;
            return this;
        }

        public Builder withPlatform(String str, String str2, String str3, String str4, int i) {
            this.t = str;
            this.u = str2;
            this.v = str3;
            this.w = str4;
            this.x = i;
            return this;
        }

        public Builder withTVPlatform() {
            return withPlatform("ccandroidtv", "", "cc", "androidtv", 1291);
        }

        public Builder withThirdPlatform(String str, int i) {
            this.t = str;
            this.u = str;
            this.v = str;
            this.w = "android";
            this.x = i;
            return this;
        }

        public Builder withUserInfo(String str, String str2, long j, String str3) {
            UserInfo userInfo = new UserInfo();
            userInfo.urs = str;
            userInfo.sid = str2;
            userInfo.uid = j;
            userInfo.extraLog = str3;
            return withUserInfo(userInfo);
        }

        public Builder withUserInfo(UserInfo userInfo) {
            this.y = userInfo;
            return this;
        }
    }
}