package com.netease.cloud.nos.android.core;

import com.netease.cloud.nos.android.exception.InvalidChunkSizeException;
import com.netease.cloud.nos.android.exception.InvalidParameterException;
import com.netease.cloud.nos.android.utils.LogUtil;
import com.netease.cloud.nos.android.utils.Util;
import com.xiaomi.gamecenter.sdk.utils.RSASignature;
import com.xiaomi.mipush.sdk.Constants;
import org.apache.http.client.HttpClient;

/* loaded from: classes5.dex */
public class AcceleratorConf {
    private static final String LOGTAG = LogUtil.makeLogTag(AcceleratorConf.class);
    private String lbsHost = "http://wanproxy.127.net/lbs;http://wanproxy-hz.127.net/lbs;http://wanproxy-bj.127.net/lbs;http://wanproxy-oversea.127.net/lbs";
    private String lbsIP = "http://223.252.196.38/lbs";
    private String monitorHost = "http://wanproxy.127.net";
    private String charset = RSASignature.c;
    private int connectionTimeout = 10000;
    private int soTimeout = 30000;
    private int lbsConnectionTimeout = 10000;
    private int lbsSoTimeout = 10000;
    private int chunkSize = 32768;
    private int chunkRetryCount = 2;
    private int queryRetryCount = 2;
    private long refreshInterval = 7200000;
    private long monitorInterval = 120000;
    private boolean isPipelineEnabled = true;
    private long pipelineFailoverPeriod = Constants.ASSEMBLE_PUSH_NETWORK_INTERVAL;
    private int md5FileMaxSize = 1048576;
    private HttpClient httpClient = null;
    private boolean monitorThreadEnable = false;

    public String getCharset() {
        return this.charset;
    }

    public int getChunkRetryCount() {
        return this.chunkRetryCount;
    }

    public int getChunkSize() {
        return this.chunkSize;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    public int getLbsConnectionTimeout() {
        return this.lbsConnectionTimeout;
    }

    public String getLbsHost() {
        return this.lbsHost;
    }

    public String getLbsIP() {
        return this.lbsIP;
    }

    public int getLbsSoTimeout() {
        return this.lbsSoTimeout;
    }

    public int getMd5FileMaxSize() {
        return this.md5FileMaxSize;
    }

    public String getMonitorHost() {
        return this.monitorHost;
    }

    public long getMonitorInterval() {
        return this.monitorInterval;
    }

    public long getPipelineFailoverPeriod() {
        return this.pipelineFailoverPeriod;
    }

    public int getQueryRetryCount() {
        return this.queryRetryCount;
    }

    public long getRefreshInterval() {
        return this.refreshInterval;
    }

    public int getSoTimeout() {
        return this.soTimeout;
    }

    public boolean isMonitorThreadEnabled() {
        return this.monitorThreadEnable;
    }

    public boolean isPipelineEnabled() {
        return this.isPipelineEnabled;
    }

    public void setChunkRetryCount(int i) throws InvalidParameterException {
        if (i > 0) {
            this.chunkRetryCount = i;
        } else {
            throw new InvalidParameterException("Invalid chunkRetryCount:" + i);
        }
    }

    public void setChunkSize(int i) throws InvalidChunkSizeException {
        if (i > 4194304 || i < 4096) {
            throw new InvalidChunkSizeException();
        }
        this.chunkSize = i;
    }

    public void setConnectionTimeout(int i) throws InvalidParameterException {
        if (i > 0) {
            this.connectionTimeout = i;
        } else {
            throw new InvalidParameterException("Invalid ConnectionTimeout:" + i);
        }
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void setLbsConnectionTimeout(int i) throws InvalidParameterException {
        if (i > 0) {
            this.lbsConnectionTimeout = i;
        } else {
            throw new InvalidParameterException("Invalid lbsConnectionTimeout:" + i);
        }
    }

    public void setLbsHost(String str) {
        this.lbsHost = str;
    }

    public void setLbsIP(String str) throws InvalidParameterException {
        if (!Util.isValidLbsIP(str)) {
            throw new InvalidParameterException("Invalid LbsIP");
        }
        this.lbsIP = str;
    }

    public void setLbsSoTimeout(int i) throws InvalidParameterException {
        if (i > 0) {
            this.lbsSoTimeout = i;
        } else {
            throw new InvalidParameterException("Invalid lbsSoTimeout:" + i);
        }
    }

    public void setMd5FileMaxSize(int i) throws InvalidParameterException {
        if (i >= 0) {
            this.md5FileMaxSize = i;
        } else {
            throw new InvalidParameterException("Invalid md5FileMaxSize:" + i);
        }
    }

    public void setMonitorInterval(long j) {
        if (j >= 60000) {
            this.monitorInterval = j;
            return;
        }
        LogUtil.w(LOGTAG, "Invalid monitorInterval:" + j);
    }

    public void setMonitorThread(boolean z) {
        this.monitorThreadEnable = z;
    }

    public void setMontiroHost(String str) {
        this.monitorHost = str;
    }

    public void setPipelineEnabled(boolean z) {
        this.isPipelineEnabled = z;
    }

    public void setPipelineFailoverPeriod(long j) {
        if (j >= 0) {
            this.pipelineFailoverPeriod = j;
            return;
        }
        LogUtil.w(LOGTAG, "Invalid pipelineFailoverPeriod:" + j);
    }

    public void setQueryRetryCount(int i) throws InvalidParameterException {
        if (i > 0) {
            this.queryRetryCount = i;
        } else {
            throw new InvalidParameterException("Invalid queryRetryCount:" + i);
        }
    }

    public void setRefreshInterval(long j) {
        if (j >= 60000) {
            this.refreshInterval = j;
            return;
        }
        LogUtil.w(LOGTAG, "Invalid refreshInterval:" + j);
    }

    public void setSoTimeout(int i) throws InvalidParameterException {
        if (i > 0) {
            this.soTimeout = i;
        } else {
            throw new InvalidParameterException("Invalid soTimeout:" + i);
        }
    }
}