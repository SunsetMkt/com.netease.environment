package com.netease.ntunisdk.unilogger.writer;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import com.netease.ntunisdk.unilogger.configs.Config;
import com.netease.ntunisdk.unilogger.global.Const;
import com.netease.ntunisdk.unilogger.global.GlobalPrarm;
import com.netease.ntunisdk.unilogger.utils.LogUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

/* loaded from: classes5.dex */
public class WriterHandler extends Handler {
    private static SimpleDateFormat FORMAT;
    private static WriterHandler writerHandler;
    private HashMap<Integer, WriterHandlerCallback> callBackMap;

    private WriterHandler(Looper looper) {
        super(looper);
        this.callBackMap = new HashMap<>();
    }

    public static WriterHandler getInstance() {
        if (writerHandler == null) {
            HandlerThread handlerThread = new HandlerThread("UniLoggerWriter");
            handlerThread.start();
            writerHandler = new WriterHandler(handlerThread.getLooper());
            FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        }
        return writerHandler;
    }

    public synchronized int getCallBackIndex(String str) {
        return str.hashCode();
    }

    public boolean registerCallback(String str, Config.UnitResult unitResult) throws Exception {
        if (!this.callBackMap.containsKey(Integer.valueOf(str.hashCode()))) {
            this.callBackMap.put(Integer.valueOf(str.hashCode()), new WriterHandlerCallback(str, unitResult));
        }
        return this.callBackMap.containsKey(Integer.valueOf(str.hashCode()));
    }

    public boolean containCallback(String str) {
        return this.callBackMap.containsKey(Integer.valueOf(str.hashCode()));
    }

    public void updateCallBackUnitResult(String str, Config.UnitResult unitResult) {
        if (Const.CONFIG_KEY.ALL.equals(str)) {
            Iterator<Integer> it = this.callBackMap.keySet().iterator();
            while (it.hasNext()) {
                this.callBackMap.get(it.next()).updateUnitResult(unitResult);
            }
            return;
        }
        if (this.callBackMap.containsKey(Integer.valueOf(str.hashCode()))) {
            this.callBackMap.get(Integer.valueOf(str.hashCode())).updateUnitResult(unitResult);
            return;
        }
        LogUtils.i_inner("WriterHandler [updateCallBackUnitResult] [logger-trace] WriterHandlerCallback dont have " + str);
    }

    public void write(int i, String str, String str2, String str3) {
        Message.obtain(this, 0, i, 0, FORMAT.format(new Date()) + " " + Process.myPid() + "-" + Process.myTid() + "/" + GlobalPrarm.packageName + " " + str + "/" + str2 + ": " + str3).sendToTarget();
    }

    public void stop(int i) {
        Message.obtain(this, 2, i, 0).sendToTarget();
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if ((i == 0 || i == 1 || i == 2) && this.callBackMap.containsKey(Integer.valueOf(message.arg1))) {
            this.callBackMap.get(Integer.valueOf(message.arg1)).handleMessage(message);
        }
    }
}