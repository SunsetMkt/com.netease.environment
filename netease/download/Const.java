package com.netease.download;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class Const {
    public static final long ALARM_REPEAT_INTERVAL = 15000;
    public static final String ALL_DOWNLOADID = "ALL_DOWNLOADID";
    public static final int CODE_FINISH = 3;
    public static final int CODE_FINISH_ALL = 4;
    public static final int CODE_FINISH_PART = 5;
    public static final int CODE_FORCE_FINISH = 9;
    public static final int CODE_NETWORK_CHANGE = 10;
    public static final int CODE_PROGRESS = 2;
    public static final int CODE_RESTART = 8;
    public static final int CODE_START = 1;
    public static final int CODE_START_LISTFILE = 11;
    public static final int CODE_STOP = 6;
    public static final int CODE_STOP_WIFI_ONLY = 7;
    public static final int COMMON_FAIL = 4;
    public static final int COMMON_FINISH = 3;
    public static final int COMMON_INIT = 0;
    public static final int COMMON_PROGRESS = 2;
    public static final int COMMON_RESTART = 1;
    public static final int COMMON_STOP = 5;
    public static final int CONNECT_TIMEOUT_TIME = 30000;
    public static final int COPY_BUFFER_SIZE = 32768;
    public static final int DOWNLOAD_BUFFER_SIZE = 8192;
    public static final long DOWNLOAD_CACHE_MIN_SIZE = 2097152;
    public static final long DOWNLOAD_MAX_INTERVAL = 2592000000L;
    public static final int DOWNLOAD_REPORT_MIN_SIZE = 6291456;
    public static final int DOWNLOAD_REPORT_THRESHOLD = 500;
    public static final int DOWNLOAD_SEGMENT_THRESTHOD = 33554432;
    public static final int DOWNLOAD_SPEED_LIMIT = 10485760;
    public static final boolean FORCE_HTTPS = false;
    public static final int HANDSHAKE_TIMEOUT_TIME = 30000;
    public static final String HEADER_HEADER_HOST = "Host";
    public static final String HEADER_RANGE = "Range";
    public static final String HEADER_RANGE_BYTES_PREF = "bytes=";
    public static final String HEADER_RANGE_BYTES_SUFF = "-";
    public static final String HEADER_RANGE_END = "END";
    public static final String HEADER_RANGE_START = "START";
    public static final String HTTPDNS_CONFIG_CND = "httpdns_config_cnd";
    public static final String HTTPDNS_CONFIG_MODULE = "httpdns_report_module";
    public static final int HTTP_REQ_MAX_RETRY = 3;
    public static final int KEEPALIVE_TIMEOUT = 30;
    public static final String KEY_CONNECT_TIMEOUT_TIME = "KEY_CONNECT_TIMEOUT_TIME";
    public static final String KEY_MD5 = "md5";
    public static final String KEY_READ_TIMEOUT_TIME = "READ_TIMEOUT_TIME";
    public static final String KEY_SIZE = "size";
    public static final String KEY_TIME = "time";
    public static final String KEY_TOTAL_PART = "total_part";
    public static final int LOG_TYPE_CONFIG_FILE = 4;
    public static final int LOG_TYPE_HTTPDNS = 3;
    public static final int LOG_TYPE_LVSIP = 5;
    public static final int LOG_TYPE_PATCH = 2;
    public static final int LOG_TYPE_STATE = 1;
    public static final String LOG_TYPE_STATE_ERROR = "error";
    public static final String LOG_TYPE_STATE_FINISH = "finish";
    public static final String LOG_TYPE_STATE_START = "start";
    public static final int MD5_FAIL_RETRY_DOWNLOAD_COUNT = 2;
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String NT_HTTP_DNS_REQ_URL = "106.2.42.106";
    public static final String NT_PARAM_CLIP = "cli_IP";
    public static final String NT_PARAM_DOMAIN = "domain";
    public static final String Not_MD5_BUT_LVSIP = "Not_MD5_BUT_LVSIP";
    public static final int ON_PROGRESS_CALLBACK_TIME = 25;
    public static final int PINGINTERVAL = 30;
    public static final String PREFERENCES_FILE_NAME = "download_downloadid_file";
    public static final int READ_TIMEOUT_TIME = 30000;
    public static final int REPORT_ALL_INFO = 2;
    public static final int REPORT_BASE_INFO = 1;
    public static final int REQ_CONFIG = 1;
    public static final int REQ_DOWNLOAD = 4;
    public static final int REQ_DOWNLOAD_PART = 5;
    public static final int REQ_HTTPDNS_EDGE_NODE = 2;
    public static final int REQ_HTTPDNS_TARGET_NODE = 3;
    public static final String REQ_PREFIX_FOR_WS = "httpdnsip";
    public static final String REQ_URL_FOR_WS = "mbdl.update.netease.com";
    public static final String RESP_CONTENT_SPIT1 = ",";
    public static final String RESP_CONTENT_SPIT2 = ":";
    public static final String RESP_LINE_SPIT = "</br>";
    public static final int STATUS_IDLE = 0;
    public static final int STATUS_ONGOING = 1;
    public static final int STATUS_STOPPED = 2;
    public static final String TYPE_TARGET_ERROR = "error";
    public static final String TYPE_TARGET_NORMAL = "list";
    public static final String TYPE_TARGET_OTHER = "other";
    public static final String TYPE_TARGET_PATCH = "patch";
    public static final String URL_CONFIG_FORMAT = "https://mbdl.update.netease.com/%s.mbdl";
    public static final String VERSION = "2.8.2";
    public static final int WRITE_TIMEOUT_TIME = 30000;
    public static final String WS_HTTP_DNS_REQ_URL = "https://mbdl.update.netease.com/httpdns.mbdl";
    public static final String WS_PARAM_CLIP = "ws_cli_IP";
    public static final String WS_PARAM_DOMAIN = "ws_domain";
    public static String[] REQ_IPS_WS_CHINA = {"42.186.111.125", "42.186.225.81", "42.186.225.81"};
    public static String[] REQ_IPS_WS_OVERSEA = {"34.49.124.186", "34.49.124.186", "34.49.124.186"};
    public static String[] REQ_IPS_WS = {"42.186.111.125", "42.186.225.81", "42.186.225.81", "34.49.124.186", "34.49.124.186", "34.49.124.186"};
    public static List<String[]> REQ_IPS_FOR_WS = new ArrayList<String[]>() { // from class: com.netease.download.Const.1
        {
            add(Const.REQ_IPS_WS);
        }
    };
    public static String URL_LOG = "sigma-orbit-impression.proxima.nie.netease.com";
    public static String[] REQ_IPS_FOR_LOG_CHINA = {"223.252.201.28"};
    public static String[] REQ_IPS_FOR_LOG_OVERSEA = {"3.113.36.58", "54.150.206.79"};
    public static String[] REQ_IPS_FOR_LOG_GLOBAL = {"223.252.201.28", "52.196.13.86", "52.68.80.180"};
    public static String[] REQ_IPS_FOR_LOG = {"123.58.166.46", "123.58.170.147", "13.112.202.90", "52.221.84.180", "13.54.11.65", "34.199.187.221", "52.52.82.138"};

    public enum Stage {
        NORMAL,
        OTHER_SEG_USED,
        OTHER_IP_USED,
        RE_DOWNLOAD
    }

    public static void setReqIpsForWs(String[] strArr) {
        REQ_IPS_FOR_WS.clear();
        REQ_IPS_FOR_WS.add(strArr);
    }
}