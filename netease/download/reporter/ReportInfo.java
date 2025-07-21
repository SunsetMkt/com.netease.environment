package com.netease.download.reporter;

/* loaded from: classes4.dex */
public class ReportInfo {
    private static final String TAG = "ReportInfo_new";
    private static ReportInfo sReportInfo_new;

    private ReportInfo() {
    }

    public static ReportInfo getInstance() {
        if (sReportInfo_new == null) {
            sReportInfo_new = new ReportInfo();
        }
        return sReportInfo_new;
    }

    /*  JADX ERROR: Type inference failed
        jadx.core.utils.exceptions.JadxOverflowException: Type inference error: updates count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:77)
        */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 3 */
    public java.lang.String getInfo(boolean r27) {
        /*
            Method dump skipped, instructions count: 2743
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.download.reporter.ReportInfo.getInfo(boolean):java.lang.String");
    }
}