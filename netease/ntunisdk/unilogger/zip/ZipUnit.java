package com.netease.ntunisdk.unilogger.zip;

import com.netease.ntunisdk.unilogger.zip.ZipProxy;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/* loaded from: classes5.dex */
public class ZipUnit {
    public boolean fileDatelimit;
    public boolean fileSizelimit;
    public ArrayList<File> files;
    public String zipDirPath = null;
    public ZipCallBack zipCallBack = null;

    public ZipUnit(ArrayList<File> arrayList, boolean z, boolean z2) {
        this.files = arrayList;
        this.fileDatelimit = z;
        this.fileSizelimit = z2;
    }

    public void setZipDirPath(String str) {
        this.zipDirPath = str;
    }

    public void setZipCallBack(ZipCallBack zipCallBack) {
        this.zipCallBack = zipCallBack;
    }

    public void zip() throws IOException {
        ZipProxy.ZipResult zipResultZipFileListInSameList = ZipProxy.getInstance().zipFileListInSameList(this.files, this.fileDatelimit, this.fileSizelimit, this.zipDirPath);
        this.zipCallBack.onZipResult(zipResultZipFileListInSameList.zipSucess, zipResultZipFileListInSameList.filePathList, zipResultZipFileListInSameList.zipFileName);
    }
}