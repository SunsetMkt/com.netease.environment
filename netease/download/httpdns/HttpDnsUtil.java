package com.netease.download.httpdns;

import com.netease.download.Const;
import com.netease.download.downloader.DownloadInitInfo;

/* loaded from: classes5.dex */
public class HttpDnsUtil {
    public static String getHttpdnsServicesIp() {
        return Const.WS_HTTP_DNS_REQ_URL;
    }

    public static String getEdgeNodeIpUrl(String str) {
        StringBuffer stringBuffer = new StringBuffer("https://");
        stringBuffer.append(str).append("?ws_domain=edge.httpdns.com&ws_ret_type=json&ws_cli_IP=").append(DownloadInitInfo.getInstances().getmLocalIp());
        return stringBuffer.toString();
    }

    public static String getHttpdnsDomain2IpUrl(String str, String str2) {
        StringBuffer stringBuffer = new StringBuffer("https://");
        stringBuffer.append(str).append("/v1/?domain=").append(str2);
        return stringBuffer.toString();
    }

    public static String getHttpdnsFinalResUrl(String str) {
        StringBuffer stringBuffer = new StringBuffer("https://");
        stringBuffer.append(str).append("?ws_domain=").append(str).append("&ws_cli_IP=").append(DownloadInitInfo.getInstances().getmLocalIp());
        return stringBuffer.toString();
    }
}