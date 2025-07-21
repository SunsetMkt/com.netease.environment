package com.netease.pharos.report;

import com.netease.pharos.Const;
import com.netease.pharos.PharosProxy;
import com.netease.pharos.locationCheck.NetAreaInfo;
import com.netease.pharos.util.LogUtil;

/* loaded from: classes5.dex */
public class ReportProxy {
    public static final String TAG = "ReportProxy";
    public static ReportProxy sReportProxy;

    private ReportProxy() {
    }

    public static ReportProxy getInstance() {
        if (sReportProxy == null) {
            sReportProxy = new ReportProxy();
        }
        return sReportProxy;
    }

    public int report(final String str) {
        boolean zIsmLogUpload = NetAreaInfo.getInstances().ismLogUpload();
        LogUtil.i(TAG, "ReportProxy [report] \u662f\u5426\u4e0a\u4f20\u65e5\u5fd7=" + zIsmLogUpload);
        if (!zIsmLogUpload) {
            return 11;
        }
        new Thread(new Runnable() { // from class: com.netease.pharos.report.ReportProxy.1
            @Override // java.lang.Runnable
            public void run() {
                String str2 = PharosProxy.getInstance().isOversea() ? Const.REPORT_URL_OVERSEA : Const.REPORT_URL_MAINLAND;
                ReportCore reportCore = new ReportCore();
                reportCore.init(str2);
                reportCore.start(str, null);
            }
        }).start();
        return 11;
    }
}