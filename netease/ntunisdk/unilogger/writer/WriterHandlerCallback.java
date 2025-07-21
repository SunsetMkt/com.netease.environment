package com.netease.ntunisdk.unilogger.writer;

import android.os.Handler;
import android.os.Message;
import com.netease.ntunisdk.okio.BufferedSink;
import com.netease.ntunisdk.okio.Okio;
import com.netease.ntunisdk.unilogger.configs.Config;
import com.netease.ntunisdk.unilogger.configs.ConfigProxy;
import com.netease.ntunisdk.unilogger.global.Const;
import com.netease.ntunisdk.unilogger.global.GlobalPrarm;
import com.netease.ntunisdk.unilogger.uploader.UploadCallBack;
import com.netease.ntunisdk.unilogger.uploader.UploadProxy;
import com.netease.ntunisdk.unilogger.utils.LogUtils;
import com.netease.ntunisdk.unilogger.utils.Utils;
import com.netease.ntunisdk.unilogger.zip.ZipCallBack;
import com.netease.ntunisdk.unilogger.zip.ZipProxy;
import java.io.File;
import java.util.ArrayList;

/* loaded from: classes5.dex */
public class WriterHandlerCallback implements Handler.Callback {
    private String unitName;
    private Config.UnitResult unitResult;
    private boolean has = false;
    private BufferedSink sink = null;
    private File file = null;
    private boolean start = true;

    public WriterHandlerCallback(String str, Config.UnitResult unitResult) {
        this.unitResult = unitResult;
        this.unitName = str;
        addNewFile();
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        File file;
        if (this.start) {
            try {
                synchronized (WriterHandlerCallback.class) {
                    if (this.sink == null && (file = this.file) != null) {
                        this.sink = Okio.buffer(Okio.sink(file));
                    }
                    if (this.sink == null) {
                        return true;
                    }
                    int i = message.what;
                    if (i == 0) {
                        this.sink.writeUtf8((String) message.obj);
                        this.sink.writeUtf8("\n");
                        if (!this.has) {
                            this.has = true;
                            LogUtils.i_inner("WriterHandlerCallback [handleMessage] [logger-trace] unitName=" + this.unitName + ", write to sink, filePath=" + this.file.getAbsolutePath());
                            WriterHandler.getInstance().sendMessageDelayed(Message.obtain(WriterHandler.getInstance(), 1, message.arg1, 0), 10000L);
                        }
                    } else if (i == 1) {
                        this.has = false;
                        this.sink.emit();
                        LogUtils.i_inner("WriterHandlerCallback [handleMessage] [logger-trace] unitName=" + this.unitName + ", sink write to file, filePath=" + this.file.getAbsolutePath() + ", length=" + this.file.length() + ", fileLinit * 9/10=" + (((ConfigProxy.getInstance().getFileLimit() * 1024) * 9) / 10));
                        if (this.file.length() > ((ConfigProxy.getInstance().getFileLimit() * 1024) * 9) / 10) {
                            LogUtils.i_inner("WriterHandlerCallback [handleMessage] unitName=" + this.unitName + ", file is full\uff0ccreate new file\uff0c ori filepath\uff1a" + this.file.getAbsolutePath());
                            this.sink.close();
                            this.sink = null;
                            zipAndUpload(this.file);
                            addNewFile();
                            this.sink = Okio.buffer(Okio.sink(this.file));
                            LogUtils.i_inner("WriterHandlerCallback [handleMessage] file is full\uff0ccreate new file\uff0c cur filePath=" + this.file.getAbsolutePath());
                        }
                    } else if (i == 2) {
                        LogUtils.i_inner("WriterHandlerCallback [handleMessage] unitName=" + this.unitName + ",  close sink");
                        this.sink.close();
                        this.sink = null;
                        this.start = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.w_inner("WriterHandlerCallback [handleMessage] unitName=" + this.unitName + ", Exception=" + e.toString());
            }
        }
        return false;
    }

    private void addNewFile() {
        this.file = Utils.createFile(GlobalPrarm.uniLoggerLogDirPath, createFileName());
        LogUtils.i_inner("WriterHandlerCallback [addNewFile] unitName=" + this.unitName + ", mFile=" + this.file.getAbsolutePath());
    }

    private String createUnitResultTag() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.unitName).append("_");
        try {
            Config.UnitResult unitResult = this.unitResult;
            if (unitResult != null) {
                if (unitResult.orMap != null) {
                    for (String str : this.unitResult.orMap.keySet()) {
                        if ("udid".equals(str)) {
                            stringBuffer.append(str).append("_").append(GlobalPrarm.udid).append("_");
                        } else if ("uid".equals(str)) {
                            stringBuffer.append(str).append("_").append(GlobalPrarm.uid).append("_");
                        } else if (Const.CONFIG_KEY.ROLEID.equals(str)) {
                            stringBuffer.append(str).append("_").append(GlobalPrarm.roleId).append("_");
                        } else if ("gameid".equals(str)) {
                            stringBuffer.append(str).append("_").append(GlobalPrarm.gameId).append("_");
                        } else if (Const.CONFIG_KEY.LOCAL_IP.equals(str)) {
                            stringBuffer.append(str).append("_").append(GlobalPrarm.localIp).append("_");
                        }
                    }
                }
                if (this.unitResult.andMap != null) {
                    for (String str2 : this.unitResult.andMap.keySet()) {
                        if ("package".equals(str2)) {
                            stringBuffer.append(str2).append("_").append(GlobalPrarm.packageName).append("_");
                        } else if ("model".equals(str2)) {
                            stringBuffer.append(str2).append("_").append(GlobalPrarm.model).append("_");
                        } else if ("os_ver".equals(str2)) {
                            stringBuffer.append(str2).append("_").append(GlobalPrarm.osVer).append("_");
                        } else if ("region".equals(str2)) {
                            stringBuffer.append(str2).append("_").append(GlobalPrarm.region).append("_");
                        } else if ("sdk_version".equals(str2)) {
                            stringBuffer.append(str2).append("_").append(GlobalPrarm.sdkVersion).append("_");
                        } else if (Const.CONFIG_KEY.UNISDK_VERSION.equals(str2)) {
                            stringBuffer.append(str2).append("_").append(GlobalPrarm.unisdkVersion).append("_");
                        } else if ("channel_id".equals(str2)) {
                            stringBuffer.append(str2).append("_").append(GlobalPrarm.channelId).append("_");
                        } else if (Const.CONFIG_KEY.CHANNEL_VERSION.equals(str2)) {
                            stringBuffer.append(str2).append("_").append(GlobalPrarm.channelVersion).append("_");
                        }
                    }
                }
            }
            LogUtils.i_inner("WriterHandlerCallback [createUnitResultTag] unitName=" + this.unitName + Utils.showUnitResult(this.unitResult) + ", fileName=" + ((Object) stringBuffer));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.w_inner("WriterHandlerCallback [createUnitResultTag] Exception=" + e.toString());
        }
        return stringBuffer.toString();
    }

    private String createFileName() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(createUnitResultTag());
        stringBuffer.append(System.currentTimeMillis());
        stringBuffer.append(".log");
        LogUtils.i_inner("WriterHandlerCallback [createFileName] unitName=" + this.unitName + Utils.showUnitResult(this.unitResult) + ", fileName=" + ((Object) stringBuffer));
        return stringBuffer.toString();
    }

    public void updateUnitResult(Config.UnitResult unitResult) {
        LogUtils.i_inner("WriterHandlerCallback [updateUnitResult] [logger-trace] start, unitName=" + this.unitName);
        if (this.unitResult == null) {
            this.unitResult = unitResult;
            return;
        }
        this.unitResult = unitResult;
        LogUtils.i_inner("WriterHandlerCallback [updateUnitResult] unitName=" + this.unitName + Utils.showUnitResult(unitResult));
        try {
            if (this.unitResult.uploadEnable) {
                File file = this.file;
                if (file != null && file.exists()) {
                    String strCreateUnitResultTag = createUnitResultTag();
                    LogUtils.i_inner("WriterHandlerCallback [updateUnitResult] unitName=" + this.unitName + ", filePath=" + this.file.getAbsolutePath() + ", new Tag =" + strCreateUnitResultTag);
                    if (!this.file.getName().startsWith(strCreateUnitResultTag)) {
                        LogUtils.i_inner("WriterHandlerCallback [updateUnitResult] unitName=" + this.unitName + ",  rename file");
                        File file2 = new File(GlobalPrarm.uniLoggerLogDirPath, createFileName());
                        this.file.renameTo(file2);
                        synchronized (WriterHandlerCallback.class) {
                            this.file = file2;
                            BufferedSink bufferedSink = this.sink;
                            if (bufferedSink != null) {
                                bufferedSink.emit();
                            }
                            this.sink = Okio.buffer(Okio.appendingSink(this.file));
                            this.start = true;
                        }
                        LogUtils.i_inner("WriterHandlerCallback [updateUnitResult] unitName=" + this.unitName + ",  rename file\uff0ccur filePath=" + this.file.getAbsolutePath());
                        return;
                    }
                    LogUtils.i_inner("WriterHandlerCallback [updateUnitResult] unitName=" + this.unitName + ",  same name, dont need to rename file\uff0c filePath=" + this.file.getAbsolutePath());
                    return;
                }
                synchronized (WriterHandlerCallback.class) {
                    addNewFile();
                    LogUtils.i_inner("WriterHandlerCallback [updateUnitResult] unitName=" + this.unitName + ",  close before, open now\uff0c filePath=" + this.file.getAbsolutePath());
                    this.start = true;
                    this.has = false;
                }
                return;
            }
            this.start = false;
            boolean zDeleteFile = Utils.deleteFile(this.file);
            StringBuilder sb = new StringBuilder("WriterHandlerCallback [updateUnitResult] unitName=");
            sb.append(this.unitName);
            sb.append(", delete file filePath=");
            File file3 = this.file;
            sb.append(file3 != null ? file3.getAbsolutePath() : "is not exist");
            sb.append(", delete result=");
            sb.append(zDeleteFile);
            LogUtils.i_inner(sb.toString());
            synchronized (WriterHandlerCallback.class) {
                BufferedSink bufferedSink2 = this.sink;
                if (bufferedSink2 != null) {
                    bufferedSink2.close();
                    this.sink = null;
                    this.file = null;
                }
            }
            return;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.w_inner("WriterHandlerCallback [updateUnitResult] unitName=" + this.unitName + ",  Exception=" + e.toString());
        }
        e.printStackTrace();
        LogUtils.w_inner("WriterHandlerCallback [updateUnitResult] unitName=" + this.unitName + ",  Exception=" + e.toString());
    }

    private void zipAndUpload(File file) {
        LogUtils.v_inner("WriterHandlerCallback [zipAndUpload] start");
        if (file == null || !file.exists()) {
            LogUtils.e_inner("WriterHandlerCallback [zipAndUpload] file is error");
            return;
        }
        if (!this.unitResult.uploadEnable) {
            LogUtils.e_inner("WriterHandlerCallback [zipAndUpload] unitResult.uploadEnable=" + this.unitResult.uploadEnable + " dont zip and upload");
            return;
        }
        final UploadCallBack uploadCallBack = new UploadCallBack() { // from class: com.netease.ntunisdk.unilogger.writer.WriterHandlerCallback.1
            @Override // com.netease.ntunisdk.unilogger.uploader.UploadCallBack
            public void onUploadResult(int i, String str) {
                LogUtils.i_inner("WriterHandlerCallback [zipAndUpload] [onUploadResult] unitName=" + WriterHandlerCallback.this.unitName + ", code=" + i + ", filePath=" + str);
                if (1 == i) {
                    Utils.deleteFile(str);
                }
            }
        };
        ZipProxy.getInstance().zipSingleFileInOtherThread(file, false, true, GlobalPrarm.uniLoggerLogDirPath, new ZipCallBack() { // from class: com.netease.ntunisdk.unilogger.writer.WriterHandlerCallback.2
            @Override // com.netease.ntunisdk.unilogger.zip.ZipCallBack
            public void onZipResult(boolean z, ArrayList<File> arrayList, String str) {
                LogUtils.i_inner("WriterHandlerCallback [zipAndUpload] [onZipResult] unitName=" + WriterHandlerCallback.this.unitName + ",  single file,  [onZipResult] suc=" + z + ", zipPath=" + str);
                if (z) {
                    UploadProxy.getInstance().uploadFile(str, uploadCallBack);
                    Utils.deleteAllFilesByFileList(arrayList);
                }
            }
        });
    }
}