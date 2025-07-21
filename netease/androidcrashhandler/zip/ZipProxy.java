package com.netease.androidcrashhandler.zip;

import android.text.TextUtils;
import com.netease.androidcrashhandler.AndroidCrashHandler;
import com.netease.androidcrashhandler.entity.Extension.ExtensionInfo;
import com.netease.androidcrashhandler.init.InitProxy;
import com.netease.androidcrashhandler.net.RequestCallback;
import com.netease.androidcrashhandler.net.UploadZipRequest;
import com.netease.androidcrashhandler.processCenter.RetryHandler;
import com.netease.androidcrashhandler.processCenter.TaskProxy;
import com.netease.androidcrashhandler.thirdparty.clientLogModule.ClientLog;
import com.netease.androidcrashhandler.unknownCrash.CheckNormalExitModel;
import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class ZipProxy {
    private static final byte[] mLock = new byte[0];
    private static int sReTryUploadTime = 5;
    private static int sSleepTime = 600000;
    private static ZipProxy sZipProxy;
    private HashMap<String, Integer> mUploadIngMap = new HashMap<>();
    private List<String> mNotUploadZip = new ArrayList();

    private ZipProxy() {
    }

    public static ZipProxy getInstance() {
        if (sZipProxy == null) {
            sZipProxy = new ZipProxy();
        }
        return sZipProxy;
    }

    private String zipOtherFilesSync(String str, String str2) {
        ZipCore zipCore = new ZipCore();
        zipCore.setErrorType(str);
        zipCore.setIsAppLaunch(false);
        zipCore.setExtensionInfos(ExtensionInfo.getInstance().getResult());
        zipCore.setPackagePath(str2);
        return zipCore.zipOtherTask(str2);
    }

    private boolean checkFileValid(String str) {
        File file = new File(InitProxy.sUploadFilePath, str);
        if (ZipUtil.checkFileTimeValid(file) && ZipUtil.checkFileSizeValid(file)) {
            return true;
        }
        file.delete();
        LogUtils.i(LogUtils.TAG, "ZipProxy [zipAndUploadOtherFilesSync] zip not pass");
        return false;
    }

    private void upload(String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        sendZipToUpload(arrayList);
    }

    public void zipAndUploadOtherFiles(String str, String str2) {
        LogUtils.i(LogUtils.TAG, "ZipProxy [zipAndUploadOtherFilesSync] start");
        String strZipOtherFilesSync = zipOtherFilesSync(str, str2);
        if (TextUtils.isEmpty(strZipOtherFilesSync) || !checkFileValid(strZipOtherFilesSync)) {
            return;
        }
        upload(strZipOtherFilesSync);
        ExtensionInfo.getInstance().clean();
    }

    public void lunchZipAsync() {
        LogUtils.i(LogUtils.TAG, "ZipProxy [lunchZipAndDispatch] start");
        ZipCore zipCore = new ZipCore();
        zipCore.setErrorType(null);
        zipCore.setIsAppLaunch(true);
        zipCore.setAutoUpload(false);
        zipCore.setExtensionInfos(ExtensionInfo.getInstance().getResult());
        TaskProxy.getInstances().put(zipCore);
    }

    public void sendZipToUpload(List<String> list) {
        if (ZipUtil.checkNetAndAgreement()) {
            synchronized (mLock) {
                if (this.mNotUploadZip.size() > 0) {
                    Iterator<String> it = this.mNotUploadZip.iterator();
                    while (it.hasNext()) {
                        list.add(new File(it.next()).getName());
                    }
                    this.mNotUploadZip.clear();
                }
            }
            Iterator<String> it2 = list.iterator();
            while (it2.hasNext()) {
                File file = new File(InitProxy.sUploadFilePath, it2.next());
                JSONObject cfgFileContent = ZipUtil.getCfgFileContent(file.getAbsolutePath());
                if (cfgFileContent != null && cfgFileContent.length() > 0) {
                    submitUploadRequest(file.getAbsolutePath(), cfgFileContent, 1);
                } else {
                    LogUtils.i(LogUtils.TAG, "ZipProxy [dispatch] param\u6587\u4ef6\u4e3a\u7a7a\uff0c\u65e0\u6cd5\u751f\u6210config\u6587\u4ef6\uff0c\u76f4\u63a5\u5220\u9664\u8be5zip\u5305");
                    file.delete();
                }
            }
            return;
        }
        synchronized (mLock) {
            this.mNotUploadZip.addAll(list);
        }
    }

    public synchronized void dispatch() {
        LogUtils.i(LogUtils.TAG, "ZipProxy [dispatch] start");
        TaskProxy.getInstances().put(new Callable<Integer>() { // from class: com.netease.androidcrashhandler.zip.ZipProxy.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                ArrayList arrayList = new ArrayList();
                arrayList.addAll(ZipUtil.getValidZipFileList(InitProxy.sOldUploadFilePath));
                arrayList.addAll(ZipUtil.getValidZipFileList(InitProxy.sUploadFilePath));
                int i = 0;
                while (!ZipUtil.checkNetAndAgreement()) {
                    try {
                        Thread.sleep(1000L);
                        i++;
                        if (i == 5) {
                            synchronized (ZipProxy.mLock) {
                                ZipProxy.this.mNotUploadZip.addAll(arrayList);
                            }
                            return 1;
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    String str = (String) it.next();
                    JSONObject cfgFileContent = ZipUtil.getCfgFileContent(str);
                    if (cfgFileContent != null && cfgFileContent.length() > 0) {
                        ZipProxy.this.submitUploadRequest(str, cfgFileContent, 1);
                    } else {
                        LogUtils.i(LogUtils.TAG, "ZipProxy [dispatch] param\u6587\u4ef6\u4e3a\u7a7a\uff0c\u65e0\u6cd5\u751f\u6210config\u6587\u4ef6\uff0c\u76f4\u63a5\u5220\u9664\u8be5zip\u5305");
                        new File(str).delete();
                    }
                }
                return 1;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void submitUploadRequest(final String str, final JSONObject jSONObject, int i) {
        UploadZipRequest uploadZipRequest = new UploadZipRequest(str, jSONObject);
        uploadZipRequest.registerRequestCallback(new RequestCallback() { // from class: com.netease.androidcrashhandler.zip.ZipProxy.2
            @Override // com.netease.androidcrashhandler.net.RequestCallback
            public void onResponse(int i2, String str2) throws Throwable {
                ZipProxy.this.changeZipUploadStatus(200 == i2, str);
                ZipProxy.this.callBack(jSONObject, i2, str2);
            }
        });
        synchronized (this.mUploadIngMap) {
            if (!this.mUploadIngMap.containsKey(str)) {
                LogUtils.i(LogUtils.TAG, "ZipProxy [submitUploadRequest] zipfileName=" + str + ", \u8be5zip\u5305\u8fd8\u6ca1\u4e0a\u4f20\uff0c\u8fdb\u5165\u4e0a\u4f20\u73af\u8282");
                if (TaskProxy.getInstances().put(uploadZipRequest)) {
                    this.mUploadIngMap.put(str, Integer.valueOf(i));
                }
            } else {
                LogUtils.i(LogUtils.TAG, "ZipProxy [submitUploadRequest] zipfileName=" + str + ", \u8be5zip\u6b63\u5728\u4e0a\u4f20\u4e2d\uff0c\u65e0\u6cd5\u540c\u4e00\u65f6\u523b\u91cd\u590d\u4e0a\u4f20");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callBack(JSONObject jSONObject, int i, String str) throws JSONException {
        try {
            JSONObject jSONObjectCreateCallbackJson = createCallbackJson(i, str, jSONObject.getString(ClientLogConstant.TRANSID), jSONObject.getString("error_type"));
            AndroidCrashHandler.callbackToUser(7, jSONObjectCreateCallbackJson);
            ClientLog.getInstence().sendClientLog(jSONObjectCreateCallbackJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JSONObject createCallbackJson(int i, String str, String str2, String str3) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("code", i);
            jSONObject.put("zipPath", str);
            jSONObject.put(ClientLogConstant.TRANSID, str2);
            jSONObject.put(CheckNormalExitModel.JSON_ERRORTYPE, str3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeZipUploadStatus(boolean z, String str) throws Throwable {
        int iIntValue;
        LogUtils.i(LogUtils.TAG, "ZipProxy [changeZipUploadStatus] start");
        LogUtils.i(LogUtils.TAG, "ZipProxy [changeZipUploadStatus] zipFileName=" + str);
        synchronized (this.mUploadIngMap) {
            Integer num = this.mUploadIngMap.get(str);
            iIntValue = num == null ? 0 : num.intValue();
            if (this.mUploadIngMap != null && !TextUtils.isEmpty(str) && this.mUploadIngMap.containsKey(str)) {
                LogUtils.i(LogUtils.TAG, "ZipProxy [removeUploadingTag] mUploadIngMap=" + this.mUploadIngMap.toString());
                this.mUploadIngMap.remove(str);
            }
        }
        retryUpload(z, iIntValue, str);
    }

    private void retryUpload(boolean z, int i, final String str) throws Throwable {
        if (!z && i < sReTryUploadTime) {
            LogUtils.i(LogUtils.TAG, "ZipProxy [retryUpload] reTry");
            final JSONObject cfgFileContent = ZipUtil.getCfgFileContent(str);
            if (cfgFileContent == null || cfgFileContent.length() <= 0) {
                return;
            }
            final int i2 = i + 1;
            LogUtils.i(LogUtils.TAG, "ZipProxy [retryUpload] reTry time:" + i2);
            LogUtils.i(LogUtils.TAG, "ZipProxy [retryUpload] sleep :" + sSleepTime + "Thread:" + Thread.currentThread().getName());
            RetryHandler.getInstance().sendRetryTaskDelay(new RetryHandler.RetryTask() { // from class: com.netease.androidcrashhandler.zip.ZipProxy.3
                @Override // com.netease.androidcrashhandler.processCenter.RetryHandler.RetryTask
                public void run() {
                    ZipProxy.this.submitUploadRequest(str, cfgFileContent, i2);
                }
            }, (long) sSleepTime);
            return;
        }
        LogUtils.i(LogUtils.TAG, "ZipProxy [retryUpload] cancel reTry");
    }
}