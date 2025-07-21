package com.netease.androidcrashhandler.unknownCrash;

import com.netease.androidcrashhandler.util.LimitSizeMap;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes5.dex */
public class CheckNormalExitModel {
    public static final int ANR_EXIT_TYPE = 2;
    public static final int APP_UPDATE_TYPE = 10;
    public static final int CRASH_EXIT_TYPE = 3;
    public static final String JSON_APP_VERSION = "app_version";
    public static final String JSON_DYNAMICHARDWARE = "dynamicHardware";
    public static final String JSON_ERRORTYPE = "errorType";
    public static final String JSON_EXITTIME = "exitTime";
    public static final String JSON_EXITTYPE = "exitType";
    public static final String JSON_ISAPPFOREGROUND = "isAppForeground";
    public static final String JSON_ISCHARGE = "isCharge";
    public static final String JSON_LASTTIME = "lasttime";
    public static final String JSON_MEMORYCRITICALCOUNT = "memoryCriticalCount";
    public static final String JSON_MEMORYCRITICALTIME = "memoryCriticalTime";
    public static final String JSON_MEMORYLIST = "memoryList";
    public static final String JSON_MEMORYSTATE = "memoryState";
    public static final String JSON_PID = "pid";
    public static final String JSON_POWER = "power";
    public static final String JSON_STARTTIME = "startTime";
    public static final String JSON_SYSTEMTOTALMEMORY = "systemTotalMemory";
    public static final String JSON_SYSTEM_STATE = "system_state";
    public static final String JSON_USERINFO = "userInfo";
    public static final int MEMORYADVICE_MEMORYSTATE_STATE_APPROACHING_LIMIT = 2;
    public static final int MEMORYADVICE_MEMORYSTATE_STATE_CRITICAL = 3;
    public static final int MEMORYADVICE_MEMORYSTATE_STATE_OK = 1;
    public static final int MEMORY_WARN_TYPE = 9;
    public static final int REBOOT_EXIT_BY_POWER_TYPE = 5;
    public static final String REPORT_ERROR_TYPE_NATIVE_OOM = "NATIVE_OOM";
    public static final String REPORT_ERROR_TYPE_UNDEFINED_EXCEPTION = "UNDEFINED_EXCEPTION";
    public static final int SYSTEM_UNKNOWN_TYPE = 6;
    public static final int UNKNOWN_EXIT_TYPE = 7;
    public static final int USER_EXIT_BY_BACKGROUND_TYPE = 8;
    public static final int USER_EXIT_TYPE = 1;
    public int exitType;
    public boolean isAppForeground;
    public boolean isCharge;
    public long lastTime;
    public int memoryCriticalCount;
    public long memoryCriticalTime;
    public int memoryState;
    public long pid;
    public double power;
    public int systemState;
    public long systemTotalMemory;
    public String startTime = "";
    public String exitTime = "";
    public String errorType = "";
    public String versionName = "";
    public LimitSizeMap<Long, ArrayList<Long>> memoryList = new LimitSizeMap<>(20);

    public static class MemoryInfo {
        public long pidPss;
        public long systemFreePss;
        public long time;
    }

    public String setTime(long j) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ClientLogConstant.DATA_FORMAT, Locale.ENGLISH);
        Date date = new Date(j);
        return simpleDateFormat.format(date) + " " + new SimpleDateFormat("Z", Locale.ENGLISH).format(date);
    }

    public String toString() {
        return "CheckNormalExitModel{startTime='" + this.startTime + "', isAppForeground=" + this.isAppForeground + ", exitTime='" + this.exitTime + "', exitType=" + this.exitType + ", errorType='" + this.errorType + "', systemTotalMemory=" + this.systemTotalMemory + ", memoryList=" + this.memoryList.toString() + ", lastTime=" + this.lastTime + ", power=" + this.power + ", isCharge=" + this.isCharge + ", memoryState=" + this.memoryState + ", memoryCriticalCount=" + this.memoryCriticalCount + ", memoryCriticalTime=" + this.memoryCriticalTime + ", pid=" + this.pid + ", systemState=" + this.systemState + '}';
    }
}