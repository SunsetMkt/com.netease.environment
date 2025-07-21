package com.netease.ntunisdk.unilogger.uploader;

import android.text.TextUtils;
import com.netease.ntunisdk.unilogger.configs.ConfigProxy;
import com.netease.ntunisdk.unilogger.global.GlobalPrarm;
import com.netease.ntunisdk.unilogger.network.NetCallback;
import com.netease.ntunisdk.unilogger.network.NetworkProxy;
import com.netease.ntunisdk.unilogger.network.UploadRequest;
import com.netease.ntunisdk.unilogger.utils.LogUtils;
import com.netease.ntunisdk.unilogger.utils.Utils;
import com.netease.ntunisdk.unilogger.utils.WifiUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes6.dex */
public class UploadProxy {
    private static UploadProxy uploadProxy;
    private int expire = 30;
    private long carrierLimit = 10485760;

    private UploadProxy() {
    }

    public static UploadProxy getInstance() {
        if (uploadProxy == null) {
            uploadProxy = new UploadProxy();
        }
        return uploadProxy;
    }

    public void init(int i, long j) {
        this.expire = i;
        this.carrierLimit = j * 1024;
    }

    public void uploadHistoryLogZips() {
        LogUtils.v_inner("UploadProxy [uploadHistoryLogZips] start");
        ArrayList<String> arrayListSearchFilesBySuffix = Utils.searchFilesBySuffix(GlobalPrarm.uniLoggerLogDirPath, ".zip");
        if (arrayListSearchFilesBySuffix == null || arrayListSearchFilesBySuffix.size() == 0) {
            LogUtils.i_inner("UploadProxy [uploadHistoryLogZips] file count: 0");
            return;
        }
        LogUtils.i_inner("UploadProxy [uploadHistoryLogZips] fileList=" + arrayListSearchFilesBySuffix.toString());
        Iterator<String> it = arrayListSearchFilesBySuffix.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (fileMatch(new File(next))) {
                LogUtils.i_inner("UploadProxy [uploadHistoryLogZips] upload file:" + next);
                upLoadFileInternal(next);
            } else {
                LogUtils.i_inner("UploadProxy [uploadHistoryLogZips] delete file:" + next);
                Utils.deleteFile(next);
            }
        }
    }

    private void upLoadFileInternal(String str) {
        if (!TextUtils.isEmpty(str) && new File(str).exists() && checkNetStatus()) {
            UploadRequest uploadRequest = new UploadRequest();
            uploadRequest.setUploadFilePath(str);
            uploadRequest.registerNetCallback(new NetCallback() { // from class: com.netease.ntunisdk.unilogger.uploader.UploadProxy.1
                AnonymousClass1() {
                }

                @Override // com.netease.ntunisdk.unilogger.network.NetCallback
                public void onNetCallback(int i, String str2) {
                    LogUtils.i_inner("UploadProxy [upLoadFileInternal] [onNetCallback] code=" + i + ", filePath=" + str2);
                    if (1 == i) {
                        LogUtils.i_inner("UploadProxy [upLoadFileInternal] [onNetCallback] upload suc, delete filePath:" + str2);
                        Utils.deleteFile(str2);
                    }
                }
            });
            NetworkProxy.addToUploadQueue(uploadRequest);
        }
    }

    /* renamed from: com.netease.ntunisdk.unilogger.uploader.UploadProxy$1 */
    class AnonymousClass1 implements NetCallback {
        AnonymousClass1() {
        }

        @Override // com.netease.ntunisdk.unilogger.network.NetCallback
        public void onNetCallback(int i, String str2) {
            LogUtils.i_inner("UploadProxy [upLoadFileInternal] [onNetCallback] code=" + i + ", filePath=" + str2);
            if (1 == i) {
                LogUtils.i_inner("UploadProxy [upLoadFileInternal] [onNetCallback] upload suc, delete filePath:" + str2);
                Utils.deleteFile(str2);
            }
        }
    }

    private boolean fileMatch(File file) {
        boolean z = Utils.fileDateMatch(file, this.expire) && Utils.fileSizeMatch(file, this.carrierLimit);
        LogUtils.i_inner("UploadProxy [fileMatch] result=" + z + ", fileName=" + file.getName());
        return z;
    }

    public boolean uploadFile(String str, UploadCallBack uploadCallBack) {
        if (!TextUtils.isEmpty(str)) {
            File file = new File(str);
            if (file.exists() && checkNetStatus() && fileMatch(file)) {
                UploadRequest uploadRequest = new UploadRequest();
                uploadRequest.setUploadFilePath(str);
                uploadRequest.registerNetCallback(new NetCallback() { // from class: com.netease.ntunisdk.unilogger.uploader.UploadProxy.2
                    final /* synthetic */ UploadCallBack val$uploadCallBack;

                    AnonymousClass2(UploadCallBack uploadCallBack2) {
                        uploadCallBack = uploadCallBack2;
                    }

                    @Override // com.netease.ntunisdk.unilogger.network.NetCallback
                    public void onNetCallback(int i, String str2) {
                        LogUtils.i_inner("UploadProxy [upLoadFile] [onNetCallback] code=" + i + ", filePath=" + str2);
                        uploadCallBack.onUploadResult(i, str2);
                    }
                });
                return NetworkProxy.addToUploadQueue(uploadRequest);
            }
            LogUtils.i_inner("UploadProxy [upLoadFile] [onNetCallback] file is not exist, filePath=" + str);
            return false;
        }
        LogUtils.i_inner("UploadProxy [upLoadFile] [onNetCallback] filePath is null, filePath=" + str);
        return false;
    }

    /* renamed from: com.netease.ntunisdk.unilogger.uploader.UploadProxy$2 */
    class AnonymousClass2 implements NetCallback {
        final /* synthetic */ UploadCallBack val$uploadCallBack;

        AnonymousClass2(UploadCallBack uploadCallBack2) {
            uploadCallBack = uploadCallBack2;
        }

        @Override // com.netease.ntunisdk.unilogger.network.NetCallback
        public void onNetCallback(int i, String str2) {
            LogUtils.i_inner("UploadProxy [upLoadFile] [onNetCallback] code=" + i + ", filePath=" + str2);
            uploadCallBack.onUploadResult(i, str2);
        }
    }

    private boolean checkNetStatus() {
        return WifiUtil.isConnectNet() && !(ConfigProxy.getInstance().getWifiOnly() && WifiUtil.isConnectedMobile());
    }
}