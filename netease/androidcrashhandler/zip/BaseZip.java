package com.netease.androidcrashhandler.zip;

import android.text.TextUtils;
import com.netease.androidcrashhandler.Const;
import com.netease.androidcrashhandler.entity.di.DiInfo;
import com.netease.androidcrashhandler.init.InitProxy;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public abstract class BaseZip {
    protected ZipCore mCore;
    protected boolean mIsLaunchMode;
    protected String mTargetDir;
    protected ArrayList<String> mFileNameList = new ArrayList<>();
    protected String mDiFileName = null;
    protected JSONObject mParamJson = null;

    protected abstract long actionTime();

    protected abstract void afterOperate();

    protected abstract boolean checkEffective();

    protected abstract void connectFile();

    protected abstract String getErrorType();

    protected abstract boolean needExternalFile(String str);

    protected abstract void preOprate();

    public BaseZip(ZipCore zipCore, String str) {
        this.mIsLaunchMode = false;
        this.mCore = zipCore;
        this.mTargetDir = str;
        this.mIsLaunchMode = zipCore.isIsAppLaunch();
    }

    public void addDirFile(String str) {
        File[] fileArrListFiles;
        if (TextUtils.isEmpty(str) || (fileArrListFiles = new File(str).listFiles()) == null) {
            return;
        }
        for (File file : fileArrListFiles) {
            addFile(file.getName());
        }
    }

    public void copyExternalFile(List<File> list) {
        for (File file : list) {
            if (needExternalFile(file.getName())) {
                File file2 = new File(this.mTargetDir, file.getName());
                LogUtils.i(LogUtils.TAG, "ZipCore [copyExternalFile] file:" + file.getAbsolutePath());
                if (CUtil.copyFile(file.getAbsolutePath(), file2.getAbsolutePath())) {
                    addFile(file2.getName());
                }
            }
        }
    }

    public void addFile(String str) {
        Iterator<String> it = this.mFileNameList.iterator();
        while (it.hasNext()) {
            if (it.next().equals(str)) {
                return;
            }
        }
        this.mFileNameList.add(str);
    }

    public void addFileList(ArrayList<String> arrayList) {
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            addFile(it.next());
        }
    }

    public ArrayList<String> getFileNameList() {
        return this.mFileNameList;
    }

    public void checkAndRebuildDiFile(String str) {
        this.mDiFileName = str;
    }

    public void setParamJson(JSONObject jSONObject) {
        this.mParamJson = jSONObject;
    }

    public JSONObject getParamJson() {
        return this.mParamJson;
    }

    public void updateCrashTimeToParamJson(long j) throws JSONException {
        try {
            JSONObject jSONObject = this.mParamJson;
            if (jSONObject != null) {
                jSONObject.put("crash_time", j + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i(LogUtils.TAG, "ZipCore [updateCrashTimeToParamJson] Exception =" + e.toString());
        }
    }

    private boolean preZip() throws Throwable {
        if (this.mParamJson == null) {
            return false;
        }
        checkAndRebuildDiFile();
        preOprate();
        if (checkEffective()) {
            connectFile();
            connectTime();
            afterOperate();
            return true;
        }
        cleanRelatedFile();
        return false;
    }

    private void connectTime() throws JSONException {
        if (0 != actionTime()) {
            updateCrashTimeToParamJson(actionTime());
            ZipUtil.updateCrashTimeToDi(actionTime(), this.mTargetDir, this.mDiFileName);
        }
    }

    public String zipFile() {
        String str = "";
        try {
            if (preZip()) {
                String str2 = System.currentTimeMillis() + ".zip";
                if (ZipUtil.zip(this.mTargetDir, this.mFileNameList, InitProxy.sUploadFilePath, str2) == 3) {
                    createUploadParamFile(str2, this.mParamJson);
                    str = str2;
                }
                cleanRelatedFile();
            }
        } catch (Throwable th) {
            LogUtils.e(LogUtils.TAG, "ZipCore [zipFile] " + th.toString());
            th.printStackTrace();
        }
        return str;
    }

    private void cleanRelatedFile() {
        LogUtils.i(LogUtils.TAG, "ZipCore [cleanRelatedFile] not match");
        if (InitProxy.sOldUploadFilePath.equals(this.mTargetDir)) {
            ZipUtil.deleteOldFileUploadPathFile(this.mFileNameList, true);
        } else {
            CUtil.deleteDir(this.mTargetDir);
        }
    }

    private void createUploadParamFile(String str, JSONObject jSONObject) {
        if (jSONObject != null) {
            if (jSONObject.has("signal")) {
                jSONObject.remove("signal");
            }
            try {
                jSONObject.put("error_type", getErrorType());
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        if (jSONObject != null && jSONObject.length() > 0) {
            CUtil.str2File(jSONObject.toString(), InitProxy.sUploadFilePath, str + Const.FileNameTag.CFG_FILE);
        }
        LogUtils.i(LogUtils.TAG, "ZipCore [createUploadParamFile] paramJson: " + jSONObject.toString());
        LogUtils.i(LogUtils.TAG, "ZipCore [createUploadParamFile] cfg file=" + str + Const.FileNameTag.CFG_FILE);
        LogUtils.i(LogUtils.TAG, "ZipCore [createUploadParamFile] finish");
    }

    private void checkAndRebuildDiFile() throws Throwable {
        String str;
        LogUtils.i(LogUtils.TAG, "ZipCore [checkAndRebuildDiFile] start");
        for (int i = 0; i < this.mFileNameList.size(); i++) {
            String str2 = this.mFileNameList.get(i);
            if (str2.endsWith(Const.FileNameTag.DI_FILE)) {
                this.mDiFileName = str2;
            }
        }
        if (TextUtils.isEmpty(this.mDiFileName)) {
            if (this.mIsLaunchMode) {
                str = DiInfo.sPreFileName;
            } else {
                str = DiInfo.sCurFileName;
            }
            CUtil.addInfoToDiFile(InitProxy.sUploadFilePath, str, "filter_pipe", this.mCore.getExtensionInfos().toString());
            CUtil.copyFile(InitProxy.sUploadFilePath + "/" + str, this.mTargetDir + "/" + str);
            StringBuilder sb = new StringBuilder("ZipCore [checkAndRebuildDiFile] back up di file :");
            sb.append(str);
            LogUtils.i(LogUtils.TAG, sb.toString());
            this.mFileNameList.add(str);
            this.mDiFileName = str;
        }
    }

    public String toString() {
        return "BaseZip mFileNameList" + this.mFileNameList.toString();
    }
}