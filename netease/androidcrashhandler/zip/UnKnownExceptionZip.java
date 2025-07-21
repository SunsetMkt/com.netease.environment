package com.netease.androidcrashhandler.zip;

import com.netease.androidcrashhandler.Const;
import com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager;
import com.netease.androidcrashhandler.unknownCrash.CheckNormalExitModel;
import java.io.File;
import java.util.Iterator;

/* loaded from: classes4.dex */
public class UnKnownExceptionZip extends BaseZip {
    String mErrorType;
    CheckNormalExitModel mExitModule;

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected boolean needExternalFile(String str) {
        return true;
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected void preOprate() {
    }

    public UnKnownExceptionZip(ZipCore zipCore, String str) {
        super(zipCore, str);
        this.mErrorType = "";
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected boolean checkEffective() {
        Iterator<String> it = this.mFileNameList.iterator();
        boolean z = false;
        while (it.hasNext()) {
            String next = it.next();
            if (next.equals(Const.FileNameTag.CHECK_NORMAL_EXIT_FILE)) {
                CheckNormalExitModel checkNormalExitModelBuildUndefinedException = CheckNormalExitManager.getInstance().buildUndefinedException(this.mTargetDir, next);
                this.mExitModule = checkNormalExitModelBuildUndefinedException;
                this.mErrorType = checkNormalExitModelBuildUndefinedException.errorType;
                z = true;
            }
        }
        return z;
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected long actionTime() {
        return this.mExitModule.lastTime;
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected void connectFile() {
        this.mFileNameList.add(CheckNormalExitManager.getInstance().buildMainFile(this.mTargetDir, this.mErrorType, this.mExitModule));
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected void afterOperate() {
        File file = new File(this.mTargetDir, Const.FileNameTag.APP_EXIT_FILE);
        file.delete();
        this.mFileNameList.remove(file.getName());
        File file2 = new File(this.mTargetDir, Const.FileNameTag.CHECK_NORMAL_EXIT_FILE);
        file2.delete();
        this.mFileNameList.remove(file2.getName());
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected String getErrorType() {
        return this.mErrorType;
    }
}