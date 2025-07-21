package com.netease.androidcrashhandler.zip;

import android.text.TextUtils;
import com.netease.androidcrashhandler.Const;
import com.netease.androidcrashhandler.MyTombstone;
import com.netease.androidcrashhandler.NTCrashHunterKit;
import com.netease.androidcrashhandler.util.LogUtils;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes4.dex */
public class AnrZip extends BaseZip {
    private String anrTraceName;
    private boolean hasWatchdogAnrFile;
    private long mAnrTime;
    private String mLogFile;
    private String mSystemTrace;
    private int pid;

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected String getErrorType() {
        return Const.ErrorTypeValue.ANR_TYPE;
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected boolean needExternalFile(String str) {
        return true;
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected void preOprate() {
    }

    public AnrZip(ZipCore zipCore, String str) {
        super(zipCore, str);
        this.pid = 0;
        this.anrTraceName = null;
        this.hasWatchdogAnrFile = false;
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected boolean checkEffective() {
        LogUtils.i(LogUtils.TAG, "ZipCore [checkEffective] start");
        if (this.mFileNameList == null || this.mFileNameList.size() < 2) {
            return false;
        }
        boolean z = false;
        boolean z2 = false;
        for (int i = 0; i < this.mFileNameList.size(); i++) {
            String str = this.mFileNameList.get(i);
            if (str.endsWith(Const.FileNameTag.TRACE_FILE)) {
                if (new File(this.mTargetDir, str).length() <= 1) {
                    return false;
                }
                this.anrTraceName = str;
                if (this.mAnrTime == 0) {
                    this.mAnrTime = new File(this.mTargetDir, str).lastModified();
                }
                z = true;
            } else if (str.endsWith(Const.FileNameTag.ANR_FILE)) {
                if (new File(this.mTargetDir, str).length() <= 1) {
                    return false;
                }
                this.hasWatchdogAnrFile = true;
                if (this.mAnrTime == 0) {
                    this.mAnrTime = new File(this.mTargetDir, str).lastModified();
                }
                z2 = true;
            } else if (Const.FileNameTag.NATIVE_LOG_FILE.equals(str)) {
                this.mLogFile = str;
            }
        }
        return z || (z2 && this.mParamJson.has("signal") && "3".equals(this.mParamJson.optString("signal")));
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected long actionTime() {
        return this.mAnrTime;
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected void connectFile() throws IOException {
        findPid();
        addAnrTombstoneFileToDesList(this.pid);
        addUUIDinTrace(this.anrTraceName);
    }

    @Override // com.netease.androidcrashhandler.zip.BaseZip
    protected void afterOperate() throws IOException {
        changeTraceToAnr();
        if (TextUtils.isEmpty(this.mLogFile)) {
            return;
        }
        String strMergeLogFile = ZipUtil.mergeLogFile(new File(this.mTargetDir, this.mLogFile), new File(this.mTargetDir, Const.FileNameTag.UNISDK_LOG_FILE));
        if (TextUtils.isEmpty(strMergeLogFile)) {
            return;
        }
        this.mFileNameList.remove(strMergeLogFile);
    }

    private void findPid() {
        if (TextUtils.isEmpty(this.anrTraceName)) {
            return;
        }
        try {
            Matcher matcher = Pattern.compile("\\d+").matcher(this.anrTraceName);
            if (matcher.find()) {
                this.pid = Integer.parseInt(matcher.group());
                LogUtils.i(LogUtils.TAG, "ZipCore [findPid] pid:" + this.pid);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void changeTraceToAnr() {
        if (this.hasWatchdogAnrFile || TextUtils.isEmpty(this.anrTraceName)) {
            return;
        }
        this.mFileNameList.remove(this.anrTraceName);
        File file = new File(this.mTargetDir, this.anrTraceName);
        File file2 = new File(this.mTargetDir, this.anrTraceName.replace(Const.FileNameTag.TRACE_FILE, Const.FileNameTag.ANR_FILE));
        file.renameTo(file2);
        this.mFileNameList.add(file2.getName());
    }

    private void addAnrTombstoneFileToDesList(int i) {
        String lastTimeAnrFile = MyTombstone.getInstance().getLastTimeAnrFile(NTCrashHunterKit.sharedKit().getContext(), this.mTargetDir, i);
        if (TextUtils.isEmpty(lastTimeAnrFile)) {
            return;
        }
        LogUtils.i(LogUtils.TAG, "ZipCore [appendFileToList] arrayList:" + this.mFileNameList.size());
        LogUtils.i(LogUtils.TAG, "ZipCore [appendFileToList] add fileName:" + lastTimeAnrFile);
        this.mSystemTrace = lastTimeAnrFile;
        this.mFileNameList.add(lastTimeAnrFile);
        LogUtils.i(LogUtils.TAG, "ZipCore [appendFileToList] arrayList:" + this.mFileNameList.size());
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:120:0x00f6
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:131)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:57)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:49)
        */
    private void addUUIDinTrace(java.lang.String r12) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 332
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.androidcrashhandler.zip.AnrZip.addUUIDinTrace(java.lang.String):void");
    }
}