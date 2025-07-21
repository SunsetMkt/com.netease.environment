package com.netease.download.reporter;

import android.content.Context;
import android.text.TextUtils;
import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.util.LogUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/* loaded from: classes4.dex */
public class ReportFile {
    private static final String TAG = "ReportFile";
    private static ReportFile sReportFile;
    private ExecutorService mExs = Executors.newSingleThreadExecutor();
    private ArrayList<Future<Integer>> mAl = new ArrayList<>();
    private BlockingQueue<String> mQueue = new ArrayBlockingQueue(2000);
    private File mFile = null;
    private BufferedWriter mOut = null;
    public FileCallBack mFileCallBack = null;
    private boolean mIsStart = false;

    interface FileCallBack {
        void finish(String str);
    }

    private ReportFile() {
    }

    public static ReportFile getInstances() {
        if (sReportFile == null) {
            sReportFile = new ReportFile();
        }
        return sReportFile;
    }

    public void init(Context context, FileCallBack fileCallBack) throws IOException {
        try {
            LogUtil.i(TAG, "ReportFile [init] \u65e5\u5fd7\u5b58\u653e\u8def\u5f84=" + context.getCacheDir());
            File file = new File(context.getCacheDir() + "/orbitlog/" + TaskHandleOp.getInstance().getTaskHandle().getSessionid() + "_report_info.txt");
            this.mFile = file;
            if (!file.getParentFile().exists()) {
                this.mFile.getParentFile().mkdirs();
            }
            if (!this.mFile.exists()) {
                try {
                    this.mFile.createNewFile();
                    LogUtil.i(TAG, "ReportFile [init] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u65e5\u5fd7\u6587\u4ef6\u751f\u6210\u6210\u529f\uff0c\u6587\u4ef6\u8def\u5f84=" + this.mFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.mIsStart = false;
            this.mFileCallBack = fileCallBack;
        } catch (Exception e2) {
            LogUtil.i(TAG, "ReportFile [init] Exception=" + e2);
        }
    }

    public void add(String str) {
        BlockingQueue<String> blockingQueue = this.mQueue;
        if (blockingQueue != null) {
            blockingQueue.add(str);
        }
    }

    public void cleanAndAdd(String str) {
        LogUtil.i(TAG, "ReportFile [cleanAndAdd] start");
        if (this.mQueue != null) {
            LogUtil.i(TAG, "ReportFile [cleanAndAdd] start mQueue=" + this.mQueue.size());
            this.mQueue.clear();
            this.mQueue.add(ReportInfo.getInstance().getInfo(false));
            this.mQueue.add(str);
            return;
        }
        LogUtil.w(TAG, "ReportFile [cleanAndAdd] mQueue is null");
    }

    public void start() {
        LogUtil.i(TAG, "ReportFile [init] Thread mIsStart=" + this.mIsStart);
        if (this.mIsStart) {
            return;
        }
        this.mIsStart = true;
        Thread thread = new Thread(new Runnable() { // from class: com.netease.download.reporter.ReportFile.1
            @Override // java.lang.Runnable
            public void run() {
                StringBuilder sb;
                LogUtil.i(ReportFile.TAG, "ReportFile [init] Thread start");
                while (true) {
                    try {
                        try {
                            try {
                                String str = (String) ReportFile.this.mQueue.take();
                                if (str.equals("finish_over")) {
                                    break;
                                }
                                try {
                                    ReportFile.this.mOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ReportFile.this.mFile.getAbsoluteFile()), "UTF-8"), 1024);
                                    ReportFile.this.mOut.write(str);
                                } catch (FileNotFoundException e) {
                                    LogUtil.i(ReportFile.TAG, "ReportFile [init] \u65e5\u5fd7\u6587\u4ef6\u843d\u5730 FileNotFoundException = " + e);
                                } catch (IOException e2) {
                                    LogUtil.i(ReportFile.TAG, "ReportFile [init] \u65e5\u5fd7\u6587\u4ef6\u843d\u5730 IOException = " + e2);
                                } catch (Exception e3) {
                                    LogUtil.i(ReportFile.TAG, "ReportFile [init] \u65e5\u5fd7\u6587\u4ef6\u843d\u5730 Exception = " + e3);
                                }
                                ReportFile.this.mOut.flush();
                                ReportFile.this.mOut.close();
                            } catch (IOException e4) {
                                LogUtil.i(ReportFile.TAG, "ReportFile [init] IOException = " + e4.toString());
                                e4.printStackTrace();
                                sb = new StringBuilder("ReportFile [init] \u65e5\u5fd7\u6587\u4ef6\u843d\u5730\u5b8c\u6210, file size = ");
                            } catch (Exception e5) {
                                LogUtil.i(ReportFile.TAG, "ReportFile [init] Exception = " + e5.toString());
                                e5.printStackTrace();
                                sb = new StringBuilder("ReportFile [init] \u65e5\u5fd7\u6587\u4ef6\u843d\u5730\u5b8c\u6210, file size = ");
                            }
                        } catch (InterruptedException e6) {
                            LogUtil.i(ReportFile.TAG, "ReportFile [init] InterruptedException = " + e6.toString());
                            e6.printStackTrace();
                            sb = new StringBuilder("ReportFile [init] \u65e5\u5fd7\u6587\u4ef6\u843d\u5730\u5b8c\u6210, file size = ");
                        }
                    } catch (Throwable th) {
                        LogUtil.i(ReportFile.TAG, "ReportFile [init] \u65e5\u5fd7\u6587\u4ef6\u843d\u5730\u5b8c\u6210, file size = " + ReportFile.this.mFile.length());
                        ReportFile.this.mFileCallBack.finish(ReportFile.this.mFile.getAbsolutePath());
                        throw th;
                    }
                }
                LogUtil.i(ReportFile.TAG, "ReportFile [init] \u65e5\u5fd7\u6587\u4ef6\u843d\u5730 break");
                sb = new StringBuilder("ReportFile [init] \u65e5\u5fd7\u6587\u4ef6\u843d\u5730\u5b8c\u6210, file size = ");
                sb.append(ReportFile.this.mFile.length());
                LogUtil.i(ReportFile.TAG, sb.toString());
                ReportFile.this.mFileCallBack.finish(ReportFile.this.mFile.getAbsolutePath());
            }
        });
        thread.setName("download_report_thread");
        thread.start();
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00c3 A[Catch: Exception -> 0x00e5, TryCatch #2 {Exception -> 0x00e5, blocks: (B:7:0x002c, B:9:0x0037, B:11:0x003d, B:13:0x0047, B:21:0x0093, B:29:0x00bd, B:31:0x00c3, B:33:0x00cc, B:28:0x00a9, B:17:0x0072, B:19:0x0083, B:32:0x00c7, B:23:0x009a, B:25:0x00a0, B:14:0x0066), top: B:40:0x002c, inners: #1, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x009a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String readFile(java.lang.String r11) {
        /*
            r10 = this;
            java.lang.String r0 = "ReportFile [readFile] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---Exception = "
            java.lang.String r1 = "ReportFile [readFile] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---FileNotFoundException = "
            java.lang.String r2 = "ReportFile [readFile] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u6587\u4ef6\u5b58\u5728\uff0c\u8def\u5f84="
            java.lang.String r3 = "ReportFile [readFile] start"
            java.lang.String r4 = "ReportFile"
            com.netease.download.util.LogUtil.i(r4, r3)
            boolean r3 = android.text.TextUtils.isEmpty(r11)
            java.lang.String r5 = ""
            if (r3 == 0) goto L1b
            java.lang.String r11 = "ReportFile [readFile] \u53c2\u6570\u9519\u8bef"
            com.netease.download.util.LogUtil.i(r4, r11)
            return r5
        L1b:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r6 = "ReportFile [readFile] filePath="
            r3.<init>(r6)
            r3.append(r11)
            java.lang.String r3 = r3.toString()
            com.netease.download.util.LogUtil.i(r4, r3)
            java.io.File r3 = new java.io.File     // Catch: java.lang.Exception -> Le5
            r3.<init>(r11)     // Catch: java.lang.Exception -> Le5
            boolean r11 = r3.exists()     // Catch: java.lang.Exception -> Le5
            if (r11 != 0) goto L3d
            java.lang.String r11 = "ReportFile [readFile] \u6587\u4ef6\u4e0d\u5b58\u5728"
            com.netease.download.util.LogUtil.i(r4, r11)     // Catch: java.lang.Exception -> Le5
            return r5
        L3d:
            long r6 = r3.length()     // Catch: java.lang.Exception -> Le5
            r8 = 0
            int r11 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r11 <= 0) goto Lc7
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Le5
            r11.<init>(r2)     // Catch: java.lang.Exception -> Le5
            java.lang.String r2 = r3.getAbsolutePath()     // Catch: java.lang.Exception -> Le5
            r11.append(r2)     // Catch: java.lang.Exception -> Le5
            java.lang.String r2 = ", \u6587\u4ef6\u5927\u5c0f="
            r11.append(r2)     // Catch: java.lang.Exception -> Le5
            long r6 = r3.length()     // Catch: java.lang.Exception -> Le5
            r11.append(r6)     // Catch: java.lang.Exception -> Le5
            java.lang.String r11 = r11.toString()     // Catch: java.lang.Exception -> Le5
            com.netease.download.util.LogUtil.i(r4, r11)     // Catch: java.lang.Exception -> Le5
            java.util.Scanner r11 = new java.util.Scanner     // Catch: java.lang.Exception -> L71 java.io.FileNotFoundException -> L82
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch: java.lang.Exception -> L71 java.io.FileNotFoundException -> L82
            r2.<init>(r3)     // Catch: java.lang.Exception -> L71 java.io.FileNotFoundException -> L82
            r11.<init>(r2)     // Catch: java.lang.Exception -> L71 java.io.FileNotFoundException -> L82
            goto L93
        L71:
            r11 = move-exception
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Le5
            r1.<init>(r0)     // Catch: java.lang.Exception -> Le5
            r1.append(r11)     // Catch: java.lang.Exception -> Le5
            java.lang.String r11 = r1.toString()     // Catch: java.lang.Exception -> Le5
            com.netease.download.util.LogUtil.w(r4, r11)     // Catch: java.lang.Exception -> Le5
            goto L92
        L82:
            r11 = move-exception
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Le5
            r0.<init>(r1)     // Catch: java.lang.Exception -> Le5
            r0.append(r11)     // Catch: java.lang.Exception -> Le5
            java.lang.String r11 = r0.toString()     // Catch: java.lang.Exception -> Le5
            com.netease.download.util.LogUtil.w(r4, r11)     // Catch: java.lang.Exception -> Le5
        L92:
            r11 = 0
        L93:
            java.lang.StringBuffer r0 = new java.lang.StringBuffer     // Catch: java.lang.Exception -> Le5
            r0.<init>()     // Catch: java.lang.Exception -> Le5
        L98:
            if (r11 == 0) goto Lbd
            boolean r1 = r11.hasNextLine()     // Catch: java.lang.Exception -> La8
            if (r1 == 0) goto Lbd
            java.lang.String r1 = r11.nextLine()     // Catch: java.lang.Exception -> La8
            r0.append(r1)     // Catch: java.lang.Exception -> La8
            goto L98
        La8:
            r1 = move-exception
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Le5
            r2.<init>()     // Catch: java.lang.Exception -> Le5
            java.lang.String r3 = "ReportFile [readFile] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u6587\u4ef6\u8bfb\u53d6\u5f02\u5e38 Exception = "
            r2.append(r3)     // Catch: java.lang.Exception -> Le5
            r2.append(r1)     // Catch: java.lang.Exception -> Le5
            java.lang.String r1 = r2.toString()     // Catch: java.lang.Exception -> Le5
            com.netease.download.util.LogUtil.w(r4, r1)     // Catch: java.lang.Exception -> Le5
        Lbd:
            java.lang.String r5 = r0.toString()     // Catch: java.lang.Exception -> Le5
            if (r11 == 0) goto Lcc
            r11.close()     // Catch: java.lang.Exception -> Le5
            goto Lcc
        Lc7:
            java.lang.String r11 = "ReportFile [readFile] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u6587\u4ef6\u5185\u5bb9\u5f02\u5e38"
            com.netease.download.util.LogUtil.i(r4, r11)     // Catch: java.lang.Exception -> Le5
        Lcc:
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Le5
            r11.<init>()     // Catch: java.lang.Exception -> Le5
            java.lang.String r0 = "ReportFile [readFile] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u6587\u4ef6\u8bfb\u53d6\u5185\u5bb9="
            r11.append(r0)     // Catch: java.lang.Exception -> Le5
            java.lang.String r0 = r5.toString()     // Catch: java.lang.Exception -> Le5
            r11.append(r0)     // Catch: java.lang.Exception -> Le5
            java.lang.String r11 = r11.toString()     // Catch: java.lang.Exception -> Le5
            com.netease.download.util.LogUtil.i(r4, r11)     // Catch: java.lang.Exception -> Le5
            goto Lf7
        Le5:
            r11 = move-exception
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "ReportFile [readFile] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u6587\u4ef6\u8bfb\u53d6\u5185\u5bb9 Exception="
            r0.<init>(r1)
            r0.append(r11)
            java.lang.String r11 = r0.toString()
            com.netease.download.util.LogUtil.w(r4, r11)
        Lf7:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.download.reporter.ReportFile.readFile(java.lang.String):java.lang.String");
    }

    public void clean() {
        LogUtil.i(TAG, "ReportFile [clean] start");
        this.mIsStart = false;
        this.mQueue.clear();
    }

    public void deleteFile(String str) {
        if (TextUtils.isEmpty(str)) {
            LogUtil.i(TAG, "ReportFile [deleteFile] filePath error");
            return;
        }
        try {
            File file = new File(str);
            if (file.exists()) {
                file.delete();
                LogUtil.i(TAG, "ReportFile [deleteFile] \u65e5\u5fd7\u6587\u4ef6\u5220\u9664\u6210\u529f\uff0c filePath=" + str);
            } else {
                LogUtil.i(TAG, "ReportFile [deleteFile] \u6587\u4ef6\u4e0d\u5b58\u5728\uff0c filePath=" + str);
            }
        } catch (Exception e) {
            LogUtil.i(TAG, "ReportFile [deleteFile] \u65e5\u5fd7\u6587\u4ef6\u5220\u9664\u5f02\u5e38\uff0c filePath=" + str);
            LogUtil.w(TAG, "ReportFile [deleteFile] Exception = " + e.toString());
            e.printStackTrace();
        }
    }
}