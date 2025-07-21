package com.netease.androidcrashhandler.zip;

import com.netease.androidcrashhandler.Const;
import java.io.File;
import java.util.Iterator;

/* loaded from: classes4.dex */
public class JavaCrashZip extends BaseZip {
    private long mCrashTime;

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected void afterOperate() {
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected void connectFile() {
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected String getErrorType() {
        return Const.ErrorTypeValue.JAVA_TYPE;
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected boolean needExternalFile(String str) {
        return true;
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected void preOprate() {
    }

    public JavaCrashZip(ZipCore zipCore, String str) {
        super(zipCore, str);
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected boolean checkEffective() {
        Iterator<String> it = this.mFileNameList.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (next.endsWith(Const.FileNameTag.ACI_FILE)) {
                this.mCrashTime = new File(this.mTargetDir, next).lastModified();
                return true;
            }
        }
        return false;
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected long actionTime() {
        return this.mCrashTime;
    }
}