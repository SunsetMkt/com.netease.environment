package com.netease.ntunisdk.unilogger.global;

/* loaded from: classes6.dex */
public class Const {
    public static final String VERSION = "1.0.0";

    public class InitState {
        public static final int DEFAULT = 0;
        public static final int FINISH = 2;
        public static final int INIT = 1;

        public InitState() {
        }
    }

    public class Network {
        public static final String CONFIG_URL_MAINLAND = "https://impression.update.netease.com/logcatch/v1/";
        public static final String CONFIG_URL_OVERSEA = "https://impression.update.easebar.com/logcatch/v1/";
        public static final long CONNECT_TIMEOUT_TIME = 5000;
        public static final long KEEPALIVE_TIMEOUT = 30;
        public static final String METHOD_GET = "GET";
        public static final String METHOD_POST = "POST";
        public static final long PINGINTERVAL = 30;
        public static final long READ_TIMEOUT_TIME = 5000;
        public static final int REQUEST_CODE_ERROR = -1;
        public static final int REQUEST_CODE_PATH_NOT_EXIST = -2;
        public static final int REQUEST_CODE_SUC = 1;
        public static final String UPLOAD_URL_MAINLAND = "https://filecatch.nie.netease.com/api/v1/log?project=";
        public static final String UPLOAD_URL_OVERSEA = "https://filecatch.nie.easebar.com/api/v1/log?project=";
        public static final String WHOAMI_URL_MAINLAND = "https://whoami.nie.netease.com/v2";
        public static final String WHOAMI_URL_OVERSEA = "https://whoami.nie.easebar.com/v2";
        public static final long WRITE_TIMEOUT_TIME = 5000;

        public Network() {
        }
    }

    public class Hit {
        public static final int DEFAULT = 0;
        public static final int HIT = 1;
        public static final int UNHIT = 2;

        public Hit() {
        }
    }

    public class LEVEL {
        public static final String DEBUG = "D";
        public static final String ERROR = "E";
        public static final String INFO = "I";
        public static final String VERBOSE = "V";
        public static final String WARN = "W";

        public LEVEL() {
        }
    }

    public class FILE {
        public static final long CARRIER_LIMIT = 10485760;
        public static final int EXPIRE = 30;
        public static final long FILE_LIMIT = 10485760;
        public static final String LOCAL_CONFIG_FILE_NAME = "unilogger_local_config_file";
        public static final int QUEUE_SIZE = 20;
        public static final String UNIT_RESULT_FILE_NAME = "unilogger_unit_result";
        public static final String ZIP_FILE_SUFFIX = ".zip";

        public FILE() {
        }
    }

    public class CONFIG_KEY {
        public static final String ALL = "all";
        public static final String CARRIER_LIMIT = "carrier_limit";
        public static final String CHANNEL_ID = "channel_id";
        public static final String CHANNEL_VERSION = "channel_version";
        public static final String EXPIRE = "expire";
        public static final String FILE_LIMIT = "file_limit";
        public static final String GAMEID = "gameid";
        public static final String LOCAL_IP = "local_ip";
        public static final String MODEL = "model";
        public static final String OS_VER = "os_ver";
        public static final String PACKAGE_NAME = "package";
        public static final String QUEUE_SIZE = "queue_size";
        public static final String REGION = "region";
        public static final String ROLEID = "roleid";
        public static final String SDK_VERSION = "sdk_version";
        public static final String UDID = "udid";
        public static final String UID = "uid";
        public static final String UNISDK_VERSION = "unisdk_version";
        public static final String UPLOAD_URL = "upload_url";
        public static final String WIFI_ONLY = "wifi_only";

        public CONFIG_KEY() {
        }
    }

    public class Common {
        public static final String JF_GAMEID = "JF_GAMEID";
        public static final String NTUNISDK_COMMON_DATA = "ntunisdk_common_data";

        public Common() {
        }
    }
}