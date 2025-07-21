package com.netease.download.downloadpart;

import com.netease.download.Const;
import com.netease.download.downloader.DownloadParams;
import com.netease.download.util.LogUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/* loaded from: classes3.dex */
public class DonwonloadPartProxy {
    private static final String TAG = "DonwonloadPartProxy";
    private DownloadParams[] mParamsList;
    private int mType = 0;
    private Const.Stage mState = null;
    private ExecutorService mExs = null;
    private ArrayList<Future<Integer>> mAl = null;

    public void init(DownloadParams[] downloadParamsArr, Const.Stage stage, int i) {
        this.mParamsList = downloadParamsArr;
        this.mType = i;
        this.mState = stage;
    }

    public int start() {
        LogUtil.stepLog("DonwonloadPartProxy [start] \u5206\u7247\u4e0b\u8f7d\u6a21\u5757");
        LogUtil.i(TAG, "DonwonloadPartProxy [start]  \u5206\u7247\u6570=" + this.mParamsList.length);
        if (this.mExs == null) {
            this.mExs = Executors.newFixedThreadPool(5);
        }
        this.mAl = new ArrayList<>();
        int iIntValue = 0;
        for (DownloadParams downloadParams : this.mParamsList) {
            DownloadPartCore downloadPartCore = new DownloadPartCore();
            downloadParams.getPartIndex();
            downloadPartCore.init(downloadParams);
            this.mAl.add(this.mExs.submit(downloadPartCore));
        }
        Iterator<Future<Integer>> it = this.mAl.iterator();
        while (it.hasNext()) {
            Future<Integer> next = it.next();
            try {
                if (next.get().intValue() != 0) {
                    iIntValue = next.get().intValue();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                LogUtil.i(TAG, "DonwonloadPartProxy InterruptedException e=" + e);
                iIntValue = 11;
            } catch (ExecutionException e2) {
                e2.printStackTrace();
                LogUtil.i(TAG, "DonwonloadPartProxy ExecutionException e=" + e2);
                iIntValue = 11;
            } catch (Exception e3) {
                e3.printStackTrace();
                LogUtil.i(TAG, "DonwonloadPartProxy Exception e=" + e3);
                iIntValue = 11;
            }
        }
        LogUtil.i(TAG, "DonwonloadPartProxy [start]  \u5206\u7247\u603b\u4e0b\u8f7d\u7ed3\u679c=" + iIntValue);
        ExecutorService executorService = this.mExs;
        if (executorService != null && !executorService.isShutdown()) {
            this.mExs.shutdown();
            this.mExs = null;
        }
        return iIntValue;
    }
}