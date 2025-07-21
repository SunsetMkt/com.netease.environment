package com.netease.pharos.report;

import android.os.Handler;
import android.os.Message;

/* loaded from: classes5.dex */
public class ReportCenter {
    private static NetmonReport mNetmonReport;
    private static Handler sHandler;

    public static Handler getHandler() {
        if (sHandler == null) {
            sHandler = new Handler() { // from class: com.netease.pharos.report.ReportCenter.1
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    int i = message.what;
                }
            };
        }
        return sHandler;
    }

    public static NetmonReport getNetmonReport() {
        if (mNetmonReport == null) {
            mNetmonReport = new NetmonReport();
        }
        return mNetmonReport;
    }
}