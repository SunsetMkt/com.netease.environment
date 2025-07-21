package com.netease.androidcrashhandler;

/* loaded from: classes2.dex */
public class Const {
    public static final String VERSION = "3.12.4";

    public class ModuleStatus {
        public static final int COMMON_FAIL = 4;
        public static final int COMMON_FINISH = 3;
        public static final int COMMON_INIT = 0;
        public static final int COMMON_PROGRESS = 2;
        public static final int COMMON_RESTART = 1;
        public static final int COMMON_STOP = 5;
        public static final int EXCEPTION_ERROR = -2;
        public static final int UNKNOWNERROR = -1;

        public ModuleStatus() {
        }
    }

    public class Net {
        public static final int CONNECT_TIMEOUT_TIME = 30000;
        public static final String METHOD_GET = "GET";
        public static final String METHOD_POST = "POST";
        public static final int READ_TIMEOUT_TIME = 30000;
        public static final int WRITE_TIMEOUT_TIME = 30000;

        public Net() {
        }
    }

    public class URL {
        public static final String DEFAULT_CONFIG_URL = "https://appdump.nie.netease.com/config";
        public static final String DEFAULT_SYSTEM_SO_TOKEN_URL = "https://appdump.nie.netease.com/sys_so/prepare";
        public static final String DEFAULT_SYSTEM_SO_UPLOAD_URL = "https://appdump.nie.netease.com/sys_so/upload";
        public static final String DEFAULT_UPLOAD_URL = "https://appdump.nie.netease.com/upload";

        public URL() {
        }
    }

    public class UrlTypeKey {
        public static final String CONFIG_URL_TYPE = "config_url_type";
        public static final String UPLOAD_URL_TYPE = "upload_url_type";

        public UrlTypeKey() {
        }
    }

    public class ErrorTypeValue {
        public static final String ANR_TYPE = "ANDROID_ANR";
        public static final String JAVA_TYPE = "ANDROID_JAVA_EXCEPTION";
        public static final String JNI_TYPE = "ANDROID_NATIVE_ERROR";
        public static final String MEMORY_TYPE = "MEMORY_WARN";
        public static final String NO_DUMP_FILE = "crash_without_dump_file";
        public static final String OTHER_TYPE = "OTHER";
        public static final String SCRIPT_TYPE = "SCRIPT_ERROR";
        public static final String SCRIPT_WARN_TYPE = "SCRIPT_WARN";
        public static final String U3D_TYPE = "U3D_ERROR";
        public static final String UE_FATAL_TYPE = "UE_FATAL";

        public ErrorTypeValue() {
        }
    }

    public class EventTypeValue {
        public static final int ANR = 6;
        public static final int CRASHHUNTER_START_EVENT = 9;
        public static final int FILE_COPY_FINISH = 3;
        public static final int HAVE_NATIVE_CRASH = 10;
        public static final int JAVA_CRASH = 4;
        public static final int JAVA_CRASH_NEW = 11;
        public static final int NATIVE_CRASH = 8;
        public static final int UNKNOWN_EXCEPTION = 12;
        public static final int UPLOAD_CALLBACK = 7;

        public EventTypeValue() {
        }
    }

    public class ParamKey {
        public static final String APPKEY = "appkey";
        public static final String CALLBACK_METHOD_NAME = "callback_method_name";
        public static final String CALLBACK_SO_PATH = "callback_so_path";
        public static final String CLIENT_V = "client_v";
        public static final String CRASH_THROWABLE = "crash_throwable";
        public static final String DUMP_MODULE = "dump_module";
        public static final String ENGINE_VERSION = "engine_version";
        public static final String ERROR_TYPE = "error_type";
        public static final String INFO = "info";
        public static final String PROCOTOL_STATE = "procotol_state";
        public static final String PROJECT = "project";
        public static final String RES_VERSION = "res_version";
        public static final String SERVER_NAME = "server_name";
        public static final String TARGET_THREAD = "target_thread";
        public static final String UID = "uid";
        public static final String USERNAME = "username";

        public ParamKey() {
        }
    }

    public class ProtocolState {
        public static final String ACCEPT = "1";
        public static final String INIT = "0";
        public static final String REFUSE = "2";

        public ProtocolState() {
        }
    }

    public class FileNameTag {
        public static final String ACI_FILE = ".aci";
        public static final String ANR_FILE = ".anr";
        public static final String APP_EXIT_FILE = "app_exit.temp";
        public static final String CFG_FILE = ".cfg";
        public static final String CHECK_NORMAL_EXIT_FILE = "CheckNormalExit.exc";
        public static final String CHECK_NORMAL_EXIT_FILE_TEMP = "CheckNormalExit.temp";
        public static final String DIR_ANR = "ANR_TRACE_";
        public static final String DIR_JAVA_CRASH = "JAVA_DUMP_";
        public static final String DIR_NATIVE_CRASH = "NATIVE_DUMP_";
        public static final String DIR_OTHER = "OTHER_";
        public static final String DIR_UNDEFINED_EXCEPTION = "UNDEFINED_EXCEPTION_";
        public static final String DI_FILE = ".di";
        public static final String DMP_FILE = ".dmp";
        public static final String EXC_FILE = "undefined_exception.exc";
        public static final String FEATURE_MODULE_INFO = "attach_main_module_info";
        public static final String MAIN_FILE = "NTMAIN_";
        public static final String MESSAGE_FILE = ".message";
        public static final String NATIVE_CRASH_DMP_FILE = "native_crash.dmp";
        public static final String NATIVE_CRASH_MARK_FILE = "native_crash.mark";
        public static final String NATIVE_LOG_FILE = "logcat.log";
        public static final String OTHER_FILE = ".other";
        public static final String PARAM_FILE = ".param";
        public static final String RUNTIME_FILE = ".runtime";
        public static final String SCRIPT_FILE = ".script";
        public static final String STACK_FILE = ".stack";
        public static final String TEMP_FILE = ".temp";
        public static final String TRACE_FILE = ".trace";
        public static final String U3D_FILE = ".u3d";
        public static final String UNISDK_LOG_FILE = "UniTrace.log";
        public static final String UNISDK_LOG_FILE_TEMP = "UniTrace.log_temp";
        public static final String UUID_FILE = "ntunisdk_so_uuids";
        public static final String ZIP_FILE = ".zip";
        public static final String ZIP_TEMP_FILE = ".ziptemp";

        public FileNameTag() {
        }
    }

    public class UnKnownCrashModel {
        public static final String ERROR_TYPE = "error_type";
        public static final String TIME = "time";

        public UnKnownCrashModel() {
        }
    }
}