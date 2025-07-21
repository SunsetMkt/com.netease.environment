package com.netease.androidcrashhandler.other;

import android.os.Process;
import android.os.SystemClock;
import android.text.TextUtils;
import com.netease.androidcrashhandler.AndroidCrashHandler;
import com.netease.androidcrashhandler.Const;
import com.netease.androidcrashhandler.config.ConfigCore;
import com.netease.androidcrashhandler.entity.di.DiInfo;
import com.netease.androidcrashhandler.entity.di.DiProxy;
import com.netease.androidcrashhandler.init.InitProxy;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.androidcrashhandler.zip.ZipProxy;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class OtherCore implements Callable<Integer> {
    private static final String MAIN_FILE = "NTMAIN_";
    private static final String MINOR_FILE = "";
    private static final String TAG = "OtherCore";
    private String mErrorType = null;
    private String mPackageDir = "";
    private HashMap<String, String> mMainInfoMap = new HashMap<>();
    private HashMap<String, String> mMainFilePathMap = new HashMap<>();
    private HashMap<String, String> mMinorInfoMap = new HashMap<>();
    private HashMap<String, String> mMinorFilePathMap = new HashMap<>();

    public interface CopyListener {
        void onFinish(boolean z, String str);
    }

    interface StorageListener {
        void onFinish(String str);
    }

    private boolean str2file(String str, String str2, String str3) {
        LogUtils.i(LogUtils.TAG, "OtherCore [str2file] start");
        boolean zStr2File = CUtil.str2File(str, str2, str3);
        LogUtils.i(LogUtils.TAG, "OtherCore [str2file] result = " + zStr2File);
        return zStr2File;
    }

    private boolean copy(String str, String str2, String str3) {
        LogUtils.i(LogUtils.TAG, "OtherCore [copy] start");
        boolean zCopyFile = CUtil.copyFile(str, new File(str2, str3).getAbsolutePath());
        LogUtils.i(LogUtils.TAG, "OtherCore [copy] result = " + zCopyFile);
        return zCopyFile;
    }

    public void setErrorType(String str) {
        this.mErrorType = str;
    }

    private String checkAndCutInfoToFile(String str) {
        LogUtils.i(LogUtils.TAG, "OtherCore [checkAndCutInfoToFile] start");
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        long length = str.getBytes().length;
        long j = ConfigCore.getInstance().getmFileLimit() * 1024;
        LogUtils.i(LogUtils.TAG, "OtherCore [checkAndCutInfoToFile] fileByteLength=" + length + ", fileLimitSize=" + j);
        if (length <= j) {
            return str;
        }
        try {
            long j2 = j / 6;
            long j3 = j - j2;
            byte[] bArr = new byte[(int) j];
            int i = (int) j2;
            System.arraycopy(str.getBytes(), 0, bArr, 0, i);
            System.arraycopy(str.getBytes(), (int) (length - j3), bArr, i, (int) j3);
            return new String(bArr);
        } catch (Exception e) {
            LogUtils.i(LogUtils.TAG, "OtherCore [getSuitableFileInfo] Exception=" + e.toString());
            e.printStackTrace();
            return str;
        }
    }

    public void copyFileFromMapToUploadFileDir(String str, HashMap<String, String> map, CopyListener copyListener) {
        if (map == null || map.size() <= 0) {
            return;
        }
        LogUtils.i(LogUtils.TAG, "OtherCore [copyFileFromMapToUploadFileDir] mFilePathMap=" + map.toString());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            if (!copy(key, str, entry.getValue())) {
                copyListener.onFinish(false, key);
            } else {
                copyListener.onFinish(true, key);
            }
        }
    }

    public void copyFileInfoFromMapToUploadFileDir(String str, HashMap<String, String> map, CopyListener copyListener) {
        if (map == null || map.size() <= 0) {
            return;
        }
        LogUtils.i(LogUtils.TAG, "OtherCore [copyFileInfoFromMapToUploadFileDir] fileInfoMap=" + map.toString());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            try {
                if (!str2file(checkAndCutInfoToFile(key), str, value)) {
                    copyListener.onFinish(false, value);
                } else {
                    copyListener.onFinish(true, value);
                }
            } catch (Throwable th) {
                th.printStackTrace();
                copyListener.onFinish(false, value);
            }
        }
    }

    public boolean stroageFile(String str, HashMap<String, String> map, HashMap<String, String> map2, CopyListener copyListener) {
        try {
            copyFileInfoFromMapToUploadFileDir(str, map, copyListener);
            copyFileFromMapToUploadFileDir(str, map2, copyListener);
            return true;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    public boolean stroageAssistFile(String str, CopyListener copyListener) {
        return stroageFile(str, this.mMinorInfoMap, this.mMinorFilePathMap, copyListener);
    }

    public boolean stroageMainFile(String str, CopyListener copyListener) {
        return stroageFile(str, this.mMainInfoMap, this.mMainFilePathMap, copyListener);
    }

    private void storageAllRelatedFileToUploadFileDir(StorageListener storageListener) throws JSONException {
        LogUtils.i(LogUtils.TAG, "OtherCore [storageAllRelatedFileToUploadFileDir] start");
        File file = new File(InitProxy.sUploadFilePath, Const.FileNameTag.DIR_OTHER + Process.myPid() + "_" + SystemClock.elapsedRealtime());
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
            JSONArray jSONArray = new JSONArray();
            JSONArray jSONArray2 = new JSONArray();
            stroageMainFile(file.getAbsolutePath(), new CopyListener() { // from class: com.netease.androidcrashhandler.other.OtherCore.1
                final /* synthetic */ JSONArray val$copyFailFileNameArray;
                final /* synthetic */ JSONArray val$copySuccessFileNameArray;

                AnonymousClass1(JSONArray jSONArray3, JSONArray jSONArray22) {
                    jSONArray = jSONArray3;
                    jSONArray = jSONArray22;
                }

                @Override // com.netease.androidcrashhandler.other.OtherCore.CopyListener
                public void onFinish(boolean z, String str) {
                    if (z) {
                        jSONArray.put(str);
                    } else {
                        jSONArray.put(str);
                    }
                }
            });
            stroageAssistFile(file.getAbsolutePath(), new CopyListener() { // from class: com.netease.androidcrashhandler.other.OtherCore.2
                final /* synthetic */ JSONArray val$copyFailFileNameArray;
                final /* synthetic */ JSONArray val$copySuccessFileNameArray;

                AnonymousClass2(JSONArray jSONArray3, JSONArray jSONArray22) {
                    jSONArray = jSONArray3;
                    jSONArray = jSONArray22;
                }

                @Override // com.netease.androidcrashhandler.other.OtherCore.CopyListener
                public void onFinish(boolean z, String str) {
                    if (z) {
                        jSONArray.put(str);
                    } else {
                        jSONArray.put(str);
                    }
                }
            });
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("copySuccessFilePath", jSONArray3);
            jSONObject.put("copyFailFilePath", jSONArray22);
            AndroidCrashHandler.callbackToUser(3, jSONObject);
            storageListener.onFinish(file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i(LogUtils.TAG, "OtherCore [storageFileOrFileInfoToUploadFileDir] Exception=" + e.toString());
        }
    }

    /* renamed from: com.netease.androidcrashhandler.other.OtherCore$1 */
    class AnonymousClass1 implements CopyListener {
        final /* synthetic */ JSONArray val$copyFailFileNameArray;
        final /* synthetic */ JSONArray val$copySuccessFileNameArray;

        AnonymousClass1(JSONArray jSONArray3, JSONArray jSONArray22) {
            jSONArray = jSONArray3;
            jSONArray = jSONArray22;
        }

        @Override // com.netease.androidcrashhandler.other.OtherCore.CopyListener
        public void onFinish(boolean z, String str) {
            if (z) {
                jSONArray.put(str);
            } else {
                jSONArray.put(str);
            }
        }
    }

    /* renamed from: com.netease.androidcrashhandler.other.OtherCore$2 */
    class AnonymousClass2 implements CopyListener {
        final /* synthetic */ JSONArray val$copyFailFileNameArray;
        final /* synthetic */ JSONArray val$copySuccessFileNameArray;

        AnonymousClass2(JSONArray jSONArray3, JSONArray jSONArray22) {
            jSONArray = jSONArray3;
            jSONArray = jSONArray22;
        }

        @Override // com.netease.androidcrashhandler.other.OtherCore.CopyListener
        public void onFinish(boolean z, String str) {
            if (z) {
                jSONArray.put(str);
            } else {
                jSONArray.put(str);
            }
        }
    }

    public void addMainInfo(String str, String str2) {
        LogUtils.i(LogUtils.TAG, "OtherCore [addMainInfo] start");
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return;
        }
        String str3 = "NTMAIN_" + str2;
        LogUtils.i(LogUtils.TAG, "OtherCore [addMainInfo] main file name=" + str3);
        this.mMainInfoMap.put(str, str3);
    }

    public void addInfo(String str, String str2) {
        LogUtils.i(LogUtils.TAG, "OtherCore [addInfo] start");
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return;
        }
        String str3 = "" + str2;
        LogUtils.i(LogUtils.TAG, "OtherCore [addInfo] minor file name=" + str3);
        this.mMinorInfoMap.put(str, str3);
    }

    public void addMainFile(String str, String str2) {
        LogUtils.i(LogUtils.TAG, "OtherCore [addMainFile] start");
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return;
        }
        String str3 = "NTMAIN_" + str2;
        LogUtils.i(LogUtils.TAG, "OtherCore [addMainFile] main file name=" + str3);
        this.mMainFilePathMap.put(str, str3);
    }

    public void addMinorFile(String str, String str2) {
        LogUtils.i(LogUtils.TAG, "OtherCore [addMinorFile] start");
        LogUtils.i(LogUtils.TAG, "OtherCore [addMinorFile] start srcFilePath=" + str + ", fileName=" + str2);
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return;
        }
        this.mMinorFilePathMap.put(str, str2);
    }

    /* renamed from: com.netease.androidcrashhandler.other.OtherCore$3 */
    class AnonymousClass3 implements StorageListener {
        AnonymousClass3() {
        }

        @Override // com.netease.androidcrashhandler.other.OtherCore.StorageListener
        public void onFinish(String str) {
            OtherCore.this.mPackageDir = str;
            CUtil.copyFile(InitProxy.sUploadFilePath + "/" + DiInfo.sCurFileName, OtherCore.this.mPackageDir + "/" + DiInfo.sCurFileName);
        }
    }

    @Override // java.util.concurrent.Callable
    public Integer call() throws Exception {
        DiProxy.getInstance().updateDiInfoToLocalFile();
        storageAllRelatedFileToUploadFileDir(new StorageListener() { // from class: com.netease.androidcrashhandler.other.OtherCore.3
            AnonymousClass3() {
            }

            @Override // com.netease.androidcrashhandler.other.OtherCore.StorageListener
            public void onFinish(String str) {
                OtherCore.this.mPackageDir = str;
                CUtil.copyFile(InitProxy.sUploadFilePath + "/" + DiInfo.sCurFileName, OtherCore.this.mPackageDir + "/" + DiInfo.sCurFileName);
            }
        });
        if (this.mMainInfoMap.isEmpty() && (this.mMainFilePathMap.isEmpty() || TextUtils.isEmpty(this.mPackageDir))) {
            return null;
        }
        ZipProxy.getInstance().zipAndUploadOtherFiles(this.mErrorType, this.mPackageDir);
        return null;
    }
}