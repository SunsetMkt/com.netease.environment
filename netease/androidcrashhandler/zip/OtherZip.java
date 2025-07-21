package com.netease.androidcrashhandler.zip;

import android.text.TextUtils;
import com.netease.androidcrashhandler.Const;
import java.io.File;
import java.util.Iterator;

/* loaded from: classes4.dex */
public class OtherZip extends BaseZip {
    private long mCrashTime;
    private String mMainFileName;

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected void afterOperate() {
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected void connectFile() {
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected void preOprate() {
    }

    public OtherZip(ZipCore zipCore, String str) {
        super(zipCore, str);
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected boolean checkEffective() {
        Iterator<String> it = this.mFileNameList.iterator();
        boolean z = false;
        while (it.hasNext()) {
            String next = it.next();
            if (next.startsWith(Const.FileNameTag.MAIN_FILE)) {
                this.mCrashTime = new File(this.mTargetDir, next).lastModified();
                this.mMainFileName = next;
                z = true;
            }
        }
        return z;
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected long actionTime() {
        return this.mCrashTime;
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected boolean needExternalFile(String str) {
        return !Const.FileNameTag.UNISDK_LOG_FILE.equals(str);
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected String getErrorType() {
        String errorType = this.mCore.getErrorType();
        if (!TextUtils.isEmpty(errorType)) {
            return errorType;
        }
        if (!this.mMainFileName.endsWith(Const.FileNameTag.OTHER_FILE)) {
            if (this.mMainFileName.endsWith(Const.FileNameTag.SCRIPT_FILE) && TextUtils.isEmpty(errorType)) {
                return Const.ErrorTypeValue.SCRIPT_TYPE;
            }
            if (this.mMainFileName.endsWith(Const.FileNameTag.U3D_FILE) && TextUtils.isEmpty(errorType)) {
                return Const.ErrorTypeValue.U3D_TYPE;
            }
        }
        return Const.ErrorTypeValue.OTHER_TYPE;
    }
}