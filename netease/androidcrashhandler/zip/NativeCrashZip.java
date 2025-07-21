package com.netease.androidcrashhandler.zip;

import android.text.TextUtils;
import com.netease.androidcrashhandler.Const;
import com.netease.androidcrashhandler.MyTombstone;
import com.netease.androidcrashhandler.NTCrashHunterKit;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes4.dex */
public class NativeCrashZip extends BaseZip {
    private long mCrashTime;
    private String mDmpFile;
    private String mLogFile;
    private String mMarkFile;

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected boolean needExternalFile(String str) {
        return true;
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected void preOprate() {
    }

    public NativeCrashZip(ZipCore zipCore, String str) {
        super(zipCore, str);
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    public boolean checkEffective() {
        LogUtils.i(LogUtils.TAG, "ZipCore [checkEffective] start");
        if (this.mFileNameList == null || this.mFileNameList.size() < 2) {
            return false;
        }
        boolean z = false;
        for (int i = 0; i < this.mFileNameList.size(); i++) {
            String str = this.mFileNameList.get(i);
            if (Const.FileNameTag.NATIVE_CRASH_MARK_FILE.equals(str) || Const.FileNameTag.NATIVE_CRASH_DMP_FILE.equals(str)) {
                this.mMarkFile = str;
                if (str.endsWith(Const.FileNameTag.DMP_FILE)) {
                    z = true;
                }
                if (this.mCrashTime == 0) {
                    this.mCrashTime = new File(this.mTargetDir, str).lastModified();
                }
            } else if (str.endsWith(Const.FileNameTag.DMP_FILE)) {
                this.mDmpFile = str;
                if (this.mCrashTime == 0) {
                    this.mCrashTime = new File(this.mTargetDir, str).lastModified();
                }
                z = true;
            } else if (Const.FileNameTag.NATIVE_LOG_FILE.equals(str)) {
                this.mLogFile = str;
            }
        }
        LogUtils.i(LogUtils.TAG, "ZipCore [checkEffective] ret:" + z);
        return z;
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    public long actionTime() {
        return this.mCrashTime;
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    public void connectFile() {
        addCrashTombstoneFileToDesList();
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    public void afterOperate() throws IOException {
        if (TextUtils.isEmpty(this.mDmpFile)) {
            changeErrorType();
        } else {
            ZipUtil.checkAndcopyUuidFile(this.mFileNameList, this.mTargetDir);
        }
        deleteMarkFile();
        if (TextUtils.isEmpty(this.mLogFile)) {
            return;
        }
        String strMergeLogFile = ZipUtil.mergeLogFile(new File(this.mTargetDir, this.mLogFile), new File(this.mTargetDir, Const.FileNameTag.UNISDK_LOG_FILE));
        if (TextUtils.isEmpty(strMergeLogFile)) {
            return;
        }
        this.mFileNameList.remove(strMergeLogFile);
    }

    private void deleteMarkFile() {
        if (TextUtils.isEmpty(this.mMarkFile)) {
            return;
        }
        this.mFileNameList.remove(this.mMarkFile);
        new File(this.mTargetDir, this.mMarkFile).delete();
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected String getErrorType() {
        return TextUtils.isEmpty(this.mDmpFile) ? Const.ErrorTypeValue.OTHER_TYPE : Const.ErrorTypeValue.JNI_TYPE;
    }

    private void changeErrorType() {
        try {
            this.mParamJson.put("error_type", Const.ErrorTypeValue.OTHER_TYPE);
            this.mParamJson.put("error_source", Const.ErrorTypeValue.NO_DUMP_FILE);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void addCrashTombstoneFileToDesList() {
        int i = 0;
        if (!TextUtils.isEmpty(this.mMarkFile)) {
            try {
                Matcher matcher = Pattern.compile("(?<=_)\\d+$").matcher(CUtil.file2Str(new File(this.mTargetDir, this.mMarkFile).getAbsolutePath()));
                if (matcher.find()) {
                    int i2 = Integer.parseInt(matcher.group());
                    try {
                        LogUtils.i(LogUtils.TAG, "ZipCore [findPid] pid:" + i2);
                        i = i2;
                    } catch (Throwable th) {
                        th = th;
                        i = i2;
                        th.printStackTrace();
                        addCrashTombstoneFileToDesList(i, this.mCrashTime);
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
        addCrashTombstoneFileToDesList(i, this.mCrashTime);
    }

    private void addCrashTombstoneFileToDesList(int i, long j) {
        String lastTimeCrashTombstone = MyTombstone.getInstance().getLastTimeCrashTombstone(NTCrashHunterKit.sharedKit().getContext(), this.mTargetDir, i, j);
        if (TextUtils.isEmpty(lastTimeCrashTombstone)) {
            return;
        }
        LogUtils.i(LogUtils.TAG, "ZipCore [addCrashTombstoneFileToDesList] arrayList:" + this.mFileNameList.size());
        LogUtils.i(LogUtils.TAG, "ZipCore [addCrashTombstoneFileToDesList] add fileName:" + lastTimeCrashTombstone);
        this.mFileNameList.add(lastTimeCrashTombstone);
        LogUtils.i(LogUtils.TAG, "ZipCore [addCrashTombstoneFileToDesList] arrayList:" + this.mFileNameList.size());
    }
}