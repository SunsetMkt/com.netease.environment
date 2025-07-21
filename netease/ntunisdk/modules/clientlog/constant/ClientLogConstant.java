package com.netease.ntunisdk.modules.clientlog.constant;

/* loaded from: classes6.dex */
public class ClientLogConstant {
    public static final int BATCH_SUBMIT_COUNT = 10;
    public static final int CAPACITY = 50;
    public static final int CLOSE_CLIENT_LOG = 1;
    public static final int CORE_POOL_SIZE = 2;
    public static final int DATABASE_VERSION = 1;
    public static final String DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DB_NAME = "client_log_db";
    public static final String EB = "EB";
    public static int EB_TAG = 0;
    public static final int EB_TAG_INTERNAL = 0;
    public static final int EB_TAG_OVERSEA = 1;
    public static final String ID = "ID";
    public static final String INTERNAL_URL = "https://sigma-adclientlog-g0.proxima.nie.netease.com";
    public static final long KEEP_ALIVE_TIME = 1;
    public static final String LOG = "log";
    public static final int MAXIMUM_POOL_SIZE = 3;
    public static final int MAX_ROW_SIZE = 5000;
    public static final String MODULE_NAME = "clientLog";
    public static final String MODULE_TYPE = "adclientlog";
    public static final String MODULE_VERSIONS = "1.1.0";
    public static final String NORMAL_TYPE_KEY = "Content-Type";
    public static final String NORMAL_TYPE_VALUE = "application/json";
    public static final int NO_SUBMIT_STATUS_TAG_TAG = 0;
    public static final int ONCE_SUBMIT_COUNT = 20;
    public static final String OVERSEA_URL = "https://sigma-adclientlog-g0.proxima.nie.easebar.com";
    public static final String PATCH_TYPE_KEY = "X-Content-Type";
    public static final String PATCH_TYPE_VALUE = "application/list";
    public static final String PLATFORM = "platform";
    public static final String SDK = "sdk";
    public static final int SEND_CLIENT_LOG = 0;
    public static final String STATUS = "status";
    public static SubmitModel SUBMIT_MODULE = SubmitModel.MODEL_PATCH;
    public static final int SUBMIT_STATUS_TAG = 1;
    public static final String TABLE_NAME = "client_log_table";
    public static final String TAG = "ClientLogModule";
    public static final String TIMESTAMP = "timestamp";
    public static final String TRANSID = "transid";
    public static final String TYPE = "type";
    public static final String UDID = "udid";

    public enum SubmitModel {
        MODEL_PATCH,
        MODEL_SINGLE
    }
}