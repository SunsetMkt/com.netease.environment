package com.netease.download.reporter;

import android.text.TextUtils;
import com.netease.download.util.LogUtil;
import java.util.ArrayList;

/* loaded from: classes4.dex */
public class ReportUrlController {
    private static final String TAG = "ReportUrlController";
    private static ReportUrlController sReportUrlController;
    private String mReportUrl = null;
    private String[] mReportIP = null;
    private ArrayList<ReportUrlControllerUnit> mUrls = new ArrayList<>();
    private int mIndex = 0;

    private ReportUrlController() {
    }

    public static ReportUrlController getInstance() {
        if (sReportUrlController == null) {
            sReportUrlController = new ReportUrlController();
        }
        return sReportUrlController;
    }

    public synchronized void init(String str, String[] strArr) {
        LogUtil.i(TAG, "ReportUrlController [init] reportUrl=" + str);
        this.mUrls.clear();
        this.mReportUrl = str;
        this.mReportIP = strArr;
        this.mIndex = 0;
        parse();
    }

    public synchronized void parse() {
        LogUtil.i(TAG, "ReportUrlController [parse]");
        if (!TextUtils.isEmpty(this.mReportUrl)) {
            String domainFromUrl = ReportUtil.getInstances().getDomainFromUrl(this.mReportUrl);
            this.mUrls.add(new ReportUrlControllerUnit(domainFromUrl, this.mReportUrl));
            LogUtil.i(TAG, "ReportUrlController [parse] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u4e0a\u4f20\u65e5\u5fd7\uff0c\u94fe\u63a5\u57df\u540d= " + domainFromUrl);
        }
        LogUtil.i(TAG, "ReportUrlController [parse] urls=" + this.mUrls.toString());
    }

    public boolean hasNext() {
        LogUtil.i(TAG, "ReportUrlController [hasNext] mIndex=" + this.mIndex + ", mUrls.size()=" + this.mUrls.size());
        return this.mIndex < this.mUrls.size();
    }

    public synchronized ReportUrlControllerUnit next() {
        LogUtil.i(TAG, "ReportUrlController [next]");
        return (this.mIndex >= this.mUrls.size() || this.mUrls.size() == 0) ? null : this.mUrls.get(this.mIndex);
    }

    public void removeUnit() {
        LogUtil.i(TAG, "ReportUrlController [removeUnit]");
        this.mIndex++;
    }

    public ArrayList<ReportUrlControllerUnit> geturls() {
        return this.mUrls;
    }

    public class ReportUrlControllerUnit {
        public String mDomain;
        public String mUrl;

        public ReportUrlControllerUnit(String str, String str2) {
            this.mDomain = str;
            this.mUrl = str2;
        }

        public String toString() {
            return "mDomain=" + this.mDomain + ", mUrl=" + this.mUrl;
        }
    }

    public void clean() {
        this.mIndex = 0;
    }
}