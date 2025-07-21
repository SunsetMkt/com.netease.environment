package com.netease.download.reporter;

import android.content.Context;
import android.text.TextUtils;
import com.netease.download.Const;
import com.netease.download.config.ConfigParams;
import com.netease.download.config.ConfigProxy;
import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.reporter.ReportFile;
import com.netease.download.util.LogUtil;
import java.io.File;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class ReportProxy {
    private static final String TAG = "ReportProxy";
    private static ReportProxy sReportProxy;
    private boolean mHasInit = false;
    private ReportCallBack mReportCallBack = null;
    private Context mContext = null;

    private ReportProxy() {
    }

    public static ReportProxy getInstance() {
        if (sReportProxy == null) {
            sReportProxy = new ReportProxy();
        }
        return sReportProxy;
    }

    public ReportCallBack getmReportCallBack() {
        return this.mReportCallBack;
    }

    public void setmReportCallBack(ReportCallBack reportCallBack) {
        this.mReportCallBack = reportCallBack;
    }

    public void init(Context context) {
        LogUtil.i("ReportProxy", "ReportProxy [init] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u65e5\u5fd7\u6a21\u5757\u4ee3\u7406\u7c7b\u521d\u59cb\u5316");
        this.mContext = context;
        ReportFile.getInstances().init(this.mContext, new ReportFile.FileCallBack() { // from class: com.netease.download.reporter.ReportProxy.1
            AnonymousClass1() {
            }

            @Override // com.netease.download.reporter.ReportFile.FileCallBack
            public void finish(String str) {
                LogUtil.i("ReportProxy", "ReportProxy [FileCallBack] [finish] \u6587\u4ef6\u843d\u5730\u5b8c\u6210\uff0c\u4e0a\u4f20\u65e5\u5fd7\u6587\u4ef6");
                ReporetCore.getInstance().setOpen(false);
                ReportNet.getInstances().reportFile(str, 0);
            }
        });
        ReportFile.getInstances().start();
        ReporetCore.getInstance().init();
    }

    /* renamed from: com.netease.download.reporter.ReportProxy$1 */
    class AnonymousClass1 implements ReportFile.FileCallBack {
        AnonymousClass1() {
        }

        @Override // com.netease.download.reporter.ReportFile.FileCallBack
        public void finish(String str) {
            LogUtil.i("ReportProxy", "ReportProxy [FileCallBack] [finish] \u6587\u4ef6\u843d\u5730\u5b8c\u6210\uff0c\u4e0a\u4f20\u65e5\u5fd7\u6587\u4ef6");
            ReporetCore.getInstance().setOpen(false);
            ReportNet.getInstances().reportFile(str, 0);
        }
    }

    public void clean() {
        ReportFile.getInstances().clean();
    }

    public void close(long j) {
        ReporetCore.getInstance().close(j);
    }

    /* renamed from: com.netease.download.reporter.ReportProxy$2 */
    class AnonymousClass2 implements Runnable {
        final /* synthetic */ Context val$context;
        final /* synthetic */ int val$logDelay;

        AnonymousClass2(Context context, int i) {
            context = context;
            i = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            String reportUrl;
            String[] reportIpArray;
            ConfigParams configParams = ConfigProxy.getInstances().getmConfigParams();
            if (configParams != null) {
                LogUtil.i("ReportProxy", "ReportProxy [reportFile] \u91c7\u7528\u914d\u7f6e\u6587\u4ef6 ip");
                reportUrl = configParams.getReportUrl();
                reportIpArray = configParams.getReportIpArray();
            } else {
                reportUrl = null;
                reportIpArray = null;
            }
            if (TextUtils.isEmpty(reportUrl) || reportIpArray == null || reportIpArray.length <= 0) {
                LogUtil.i("ReportProxy", "ReportProxy [reportFile] \u91c7\u7528hardcode ip");
                reportIpArray = Const.REQ_IPS_FOR_LOG;
                String overSea = TaskHandleOp.getInstance().getTaskHandle().getOverSea();
                LogUtil.i("ReportProxy", "ReportProxy [report] \u6d77\u5916=" + overSea);
                String str = "https://sigma-orbit-impression.proxima.nie.easebar.com/sdk";
                if ("1".equals(overSea) || "2".equals(overSea)) {
                    reportIpArray = Const.REQ_IPS_FOR_LOG_OVERSEA;
                } else {
                    str = "https://sigma-orbit-impression.proxima.nie.netease.com/sdk";
                    if ("0".equals(overSea)) {
                        reportIpArray = Const.REQ_IPS_FOR_LOG_CHINA;
                    } else if ("-1".equals(overSea)) {
                        reportIpArray = Const.REQ_IPS_FOR_LOG_GLOBAL;
                    }
                }
                reportUrl = str;
            }
            LogUtil.i("ReportProxy", "ReportProxy [report] url=" + reportUrl);
            ReportUrlController.getInstance().init(reportUrl, reportIpArray);
            LogUtil.i("ReportProxy", "ReportProxy [report] ReportUrlController=" + ReportUrlController.getInstance().toString());
            try {
                String str2 = context.getCacheDir() + "/orbitlog";
                LogUtil.i("ReportProxy", "ReportProxy [report] dirPath=" + str2);
                if (!TextUtils.isEmpty(str2)) {
                    File file = new File(str2);
                    if (file.exists() && file.isDirectory()) {
                        String[] list = file.list();
                        if (list != null && list.length > 0) {
                            if (list == null || list.length <= 0) {
                                return;
                            }
                            LogUtil.i("ReportProxy", "ReportProxy [report] file size = " + list.length);
                            for (String str3 : list) {
                                String str4 = str2 + "/" + str3;
                                if (str4.contains("report_info.txt")) {
                                    LogUtil.i("ReportProxy", "ReportProxy [report] tFilePath=" + str4);
                                    ReportNet.getInstances().reportFile(str4, i);
                                }
                            }
                            return;
                        }
                        LogUtil.i("ReportProxy", "ReportProxy [report] \u6ca1\u6709\u9057\u7559\u6587\u4ef6\u9700\u8981\u4e0a\u4f20");
                        return;
                    }
                    LogUtil.i("ReportProxy", "ReportProxy [report] dirFile is null");
                    return;
                }
                LogUtil.i("ReportProxy", "ReportProxy [report] dirPath is null");
            } catch (Exception e) {
                LogUtil.i("ReportProxy", "ReportProxy [report] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u65e5\u5fd7\u4e0a\u4f20\u5b8c\u6210\uff0c\u662f\u5426\u9700\u8981\u5220\u9664\u6587\u4ef6 Exception=" + e);
            }
        }
    }

    public void report(Context context, int i) {
        Thread thread = new Thread(new Runnable() { // from class: com.netease.download.reporter.ReportProxy.2
            final /* synthetic */ Context val$context;
            final /* synthetic */ int val$logDelay;

            AnonymousClass2(Context context2, int i2) {
                context = context2;
                i = i2;
            }

            @Override // java.lang.Runnable
            public void run() {
                String reportUrl;
                String[] reportIpArray;
                ConfigParams configParams = ConfigProxy.getInstances().getmConfigParams();
                if (configParams != null) {
                    LogUtil.i("ReportProxy", "ReportProxy [reportFile] \u91c7\u7528\u914d\u7f6e\u6587\u4ef6 ip");
                    reportUrl = configParams.getReportUrl();
                    reportIpArray = configParams.getReportIpArray();
                } else {
                    reportUrl = null;
                    reportIpArray = null;
                }
                if (TextUtils.isEmpty(reportUrl) || reportIpArray == null || reportIpArray.length <= 0) {
                    LogUtil.i("ReportProxy", "ReportProxy [reportFile] \u91c7\u7528hardcode ip");
                    reportIpArray = Const.REQ_IPS_FOR_LOG;
                    String overSea = TaskHandleOp.getInstance().getTaskHandle().getOverSea();
                    LogUtil.i("ReportProxy", "ReportProxy [report] \u6d77\u5916=" + overSea);
                    String str = "https://sigma-orbit-impression.proxima.nie.easebar.com/sdk";
                    if ("1".equals(overSea) || "2".equals(overSea)) {
                        reportIpArray = Const.REQ_IPS_FOR_LOG_OVERSEA;
                    } else {
                        str = "https://sigma-orbit-impression.proxima.nie.netease.com/sdk";
                        if ("0".equals(overSea)) {
                            reportIpArray = Const.REQ_IPS_FOR_LOG_CHINA;
                        } else if ("-1".equals(overSea)) {
                            reportIpArray = Const.REQ_IPS_FOR_LOG_GLOBAL;
                        }
                    }
                    reportUrl = str;
                }
                LogUtil.i("ReportProxy", "ReportProxy [report] url=" + reportUrl);
                ReportUrlController.getInstance().init(reportUrl, reportIpArray);
                LogUtil.i("ReportProxy", "ReportProxy [report] ReportUrlController=" + ReportUrlController.getInstance().toString());
                try {
                    String str2 = context.getCacheDir() + "/orbitlog";
                    LogUtil.i("ReportProxy", "ReportProxy [report] dirPath=" + str2);
                    if (!TextUtils.isEmpty(str2)) {
                        File file = new File(str2);
                        if (file.exists() && file.isDirectory()) {
                            String[] list = file.list();
                            if (list != null && list.length > 0) {
                                if (list == null || list.length <= 0) {
                                    return;
                                }
                                LogUtil.i("ReportProxy", "ReportProxy [report] file size = " + list.length);
                                for (String str3 : list) {
                                    String str4 = str2 + "/" + str3;
                                    if (str4.contains("report_info.txt")) {
                                        LogUtil.i("ReportProxy", "ReportProxy [report] tFilePath=" + str4);
                                        ReportNet.getInstances().reportFile(str4, i);
                                    }
                                }
                                return;
                            }
                            LogUtil.i("ReportProxy", "ReportProxy [report] \u6ca1\u6709\u9057\u7559\u6587\u4ef6\u9700\u8981\u4e0a\u4f20");
                            return;
                        }
                        LogUtil.i("ReportProxy", "ReportProxy [report] dirFile is null");
                        return;
                    }
                    LogUtil.i("ReportProxy", "ReportProxy [report] dirPath is null");
                } catch (Exception e) {
                    LogUtil.i("ReportProxy", "ReportProxy [report] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u65e5\u5fd7\u4e0a\u4f20\u5b8c\u6210\uff0c\u662f\u5426\u9700\u8981\u5220\u9664\u6587\u4ef6 Exception=" + e);
                }
            }
        });
        thread.setName("download_report");
        thread.start();
    }

    public void report(Context context, String str, int i) {
        String reportUrl;
        String[] reportIpArray;
        ConfigParams configParams = ConfigProxy.getInstances().getmConfigParams();
        if (configParams != null) {
            LogUtil.i("ReportProxy", "ReportProxy [report] \u91c7\u7528\u914d\u7f6e\u6587\u4ef6 ip");
            reportUrl = configParams.getReportUrl();
            reportIpArray = configParams.getReportIpArray();
        } else {
            reportUrl = null;
            reportIpArray = null;
        }
        if (TextUtils.isEmpty(reportUrl) || reportIpArray == null || reportIpArray.length <= 0) {
            LogUtil.i("ReportProxy", "ReportProxy [report] \u91c7\u7528hardcode ip");
            reportIpArray = Const.REQ_IPS_FOR_LOG;
            String overSea = TaskHandleOp.getInstance().getTaskHandle().getOverSea();
            LogUtil.i("ReportProxy", "ReportProxy [report] \u6d77\u5916=" + overSea);
            String str2 = "https://sigma-orbit-impression.proxima.nie.easebar.com/sdk";
            if ("1".equals(overSea) || "2".equals(overSea)) {
                reportIpArray = Const.REQ_IPS_FOR_LOG_OVERSEA;
            } else {
                str2 = "https://sigma-orbit-impression.proxima.nie.netease.com/sdk";
                if ("0".equals(overSea)) {
                    reportIpArray = Const.REQ_IPS_FOR_LOG_CHINA;
                } else if ("-1".equals(overSea)) {
                    reportIpArray = Const.REQ_IPS_FOR_LOG_GLOBAL;
                }
            }
            reportUrl = str2;
        }
        LogUtil.i("ReportProxy", "ReportProxy [report] url=" + reportUrl);
        ReportUrlController.getInstance().init(reportUrl, reportIpArray);
        LogUtil.i("ReportProxy", "ReportProxy [report] ReportUrlController=" + ReportUrlController.getInstance().toString());
        if (!TextUtils.isEmpty(str)) {
            LogUtil.i("ReportProxy", "ReportProxy [report] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u4e0a\u4f20\u4fe1\u606f---\u4e0a\u4f20\u65e5\u5fd7\u5185\u5bb9=" + str);
            ReportNet.getInstances().report(str, i);
            return;
        }
        LogUtil.i("ReportProxy", "ReportProxy [report] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u4e0a\u4f20\u4fe1\u606f\uff0c\u4e0d\u9700\u8981\u4e0a\u4f20");
    }

    public void reportInfo(Context context, int i, int i2) {
        if (i != 1) {
            if (i == 2) {
                LogUtil.i("ReportProxy", "ReportProxy [report] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u4e0a\u4f20\u5168\u90e8\u4fe1\u606f");
                LogUtil.i("ReportProxy", "ReportProxy [report] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u4e0a\u4f20\u5168\u90e8\u4fe1\u606f = " + ReportInfo.getInstance().getInfo(false));
                report(context, ReportInfo.getInstance().getInfo(false), i2);
                return;
            }
            return;
        }
        LogUtil.i("ReportProxy", "ReportProxy [report] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u4e0a\u4f20\u57fa\u7840\u4fe1\u606f");
        String info = ReportInfo.getInstance().getInfo(true);
        LogUtil.i("ReportProxy", "ReportProxy [report] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u4e0a\u4f20\u57fa\u7840\u4fe1\u606f = " + info);
        try {
            JSONObject jSONObject = new JSONObject(info);
            if (14 != TaskHandleOp.getInstance().getTaskHandle().getStatus()) {
                jSONObject.put("status", -1);
            }
            info = jSONObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        report(context, info, i2);
    }

    public void setOpen(boolean z) {
        ReporetCore.getInstance().setOpen(z);
    }
}