package com.netease.ntunisdk.unilogger;

import android.text.TextUtils;
import com.netease.ntunisdk.unilogger.configs.Config;
import com.netease.ntunisdk.unilogger.configs.ConfigProxy;
import com.netease.ntunisdk.unilogger.global.Const;
import com.netease.ntunisdk.unilogger.global.GlobalPrarm;
import com.netease.ntunisdk.unilogger.uploader.UploadCallBack;
import com.netease.ntunisdk.unilogger.uploader.UploadProxy;
import com.netease.ntunisdk.unilogger.utils.LogUtils;
import com.netease.ntunisdk.unilogger.utils.ThrowableString;
import com.netease.ntunisdk.unilogger.utils.Utils;
import com.netease.ntunisdk.unilogger.writer.WriterHandler;
import com.netease.ntunisdk.unilogger.zip.ZipCallBack;
import com.netease.ntunisdk.unilogger.zip.ZipProxy;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes3.dex */
public class UniLogger {
    private String unitName;
    private int writerHandlerCallbackIndex = -1;
    private boolean writeEnable = false;

    public UniLogger(String str) {
        this.unitName = str;
        init(str);
    }

    public void setUdid(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        GlobalPrarm.udid = str;
        ConfigProxy.getInstance().containValueFromConfigKey(str, "udid");
    }

    public void setUid(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        GlobalPrarm.uid = str;
        ConfigProxy.getInstance().containValueFromConfigKey(str, "uid");
    }

    public void setRoleId(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        GlobalPrarm.roleId = str;
        ConfigProxy.getInstance().containValueFromConfigKey(str, Const.CONFIG_KEY.ROLEID);
    }

    public void setGameid(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        GlobalPrarm.gameId = str;
        ConfigProxy.getInstance().containValueFromConfigKey(str, "gameid");
    }

    private void init(String str) {
        boolean z;
        String str2 = Const.CONFIG_KEY.ALL;
        LogUtils.i_inner("UniLogger [init] start, unitName=" + str);
        try {
            Config.UnitResult unitResult = ConfigProxy.getInstance().getUnitResult(Const.CONFIG_KEY.ALL);
            if (unitResult == null) {
                unitResult = ConfigProxy.getInstance().getUnitResult(str);
                z = false;
            } else {
                z = true;
            }
            if (unitResult == null) {
                LogUtils.e_inner("UniLogger [init] [logger-trace] unitResult is null, unitName=" + str);
                return;
            }
            LogUtils.i_inner("UniLogger [init] unitName=" + str + ", isAll=" + z + Utils.showUnitResult(unitResult));
            this.writerHandlerCallbackIndex = WriterHandler.getInstance().getCallBackIndex(z ? Const.CONFIG_KEY.ALL : str);
            WriterHandler writerHandler = WriterHandler.getInstance();
            if (!z) {
                str2 = str;
            }
            if (writerHandler.registerCallback(str2, unitResult)) {
                this.writeEnable = true;
            }
            if (unitResult.writeEnable == unitResult.uploadEnable) {
                update(unitResult, z);
            }
            LogUtils.i_inner("UniLogger [init] [logger-trace] unitName=" + str + ", writeEnable=" + this.writeEnable + ", isRemote=" + unitResult.isRemote);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.w_inner("UniLogger [init] Exception=" + e.toString());
        }
    }

    public void update(Config.UnitResult unitResult, boolean z) throws Exception {
        StringBuilder sb = new StringBuilder("UniLogger [update] start, ori unitName=");
        sb.append(this.unitName);
        sb.append(", fix unitName=");
        String str = Const.CONFIG_KEY.ALL;
        sb.append(z ? Const.CONFIG_KEY.ALL : this.unitName);
        sb.append(", writeEnable=");
        sb.append(unitResult.writeEnable);
        sb.append(", uploadEnable=");
        sb.append(unitResult.uploadEnable);
        sb.append(", isAll=");
        sb.append(z);
        sb.append(", isRemote=");
        sb.append(unitResult.isRemote);
        LogUtils.i_inner(sb.toString());
        if (!unitResult.writeEnable) {
            this.writeEnable = false;
            WriterHandler.getInstance().stop(this.writerHandlerCallbackIndex);
        } else {
            this.writeEnable = true;
        }
        if (-1 == this.writerHandlerCallbackIndex) {
            this.writerHandlerCallbackIndex = WriterHandler.getInstance().getCallBackIndex(z ? Const.CONFIG_KEY.ALL : this.unitName);
            if (!WriterHandler.getInstance().containCallback(z ? Const.CONFIG_KEY.ALL : this.unitName)) {
                WriterHandler.getInstance().registerCallback(z ? Const.CONFIG_KEY.ALL : this.unitName, unitResult);
            }
        }
        WriterHandler writerHandler = WriterHandler.getInstance();
        if (!z) {
            str = this.unitName;
        }
        writerHandler.updateCallBackUnitResult(str, unitResult);
    }

    public synchronized void v(String str, String str2) {
        LogUtils.v(str, str2);
        if (this.writeEnable) {
            WriterHandler.getInstance().write(this.writerHandlerCallbackIndex, Const.LEVEL.VERBOSE, str, str2);
        }
    }

    public synchronized void d(String str, String str2) {
        LogUtils.d(str, str2);
        if (this.writeEnable) {
            WriterHandler.getInstance().write(this.writerHandlerCallbackIndex, Const.LEVEL.DEBUG, str, str2);
        }
    }

    public synchronized void i(String str, String str2) {
        LogUtils.i(str, str2);
        if (this.writeEnable) {
            WriterHandler.getInstance().write(this.writerHandlerCallbackIndex, Const.LEVEL.INFO, str, str2);
        }
    }

    public synchronized void w(String str, String str2) {
        LogUtils.w(str, str2);
        if (this.writeEnable) {
            WriterHandler.getInstance().write(this.writerHandlerCallbackIndex, Const.LEVEL.WARN, str, str2);
        }
    }

    public synchronized void w(String str, Throwable th) {
        if (this.writeEnable) {
            WriterHandler.getInstance().write(this.writerHandlerCallbackIndex, Const.LEVEL.WARN, str, ThrowableString.get(th));
        }
    }

    public synchronized void e(String str, String str2) {
        LogUtils.e(str, str2);
        if (this.writeEnable) {
            WriterHandler.getInstance().write(this.writerHandlerCallbackIndex, Const.LEVEL.ERROR, str, str2);
        }
    }

    public void uploadLogFile(String str, final UploadCallBack uploadCallBack) {
        if (TextUtils.isEmpty(str)) {
            LogUtils.i_inner("UniLogger [uploadLogFile] single file,  filePath is null, filePath=" + str);
        } else if (str.endsWith(".zip")) {
            UploadProxy.getInstance().uploadFile(str, uploadCallBack);
        } else {
            ZipProxy.getInstance().zipSingleFileInOtherThread(new File(str), false, true, GlobalPrarm.uniLoggerLogDirPath, new ZipCallBack() { // from class: com.netease.ntunisdk.unilogger.UniLogger.1
                @Override // com.netease.ntunisdk.unilogger.zip.ZipCallBack
                public void onZipResult(boolean z, ArrayList<File> arrayList, String str2) {
                    LogUtils.i_inner("UniLogger [uploadLogFile] single file,  [onZipResult] suc=" + z + ", zipPath=" + str2);
                    if (z) {
                        UploadProxy.getInstance().uploadFile(str2, uploadCallBack);
                    } else {
                        uploadCallBack.onUploadResult(-1, str2);
                    }
                }
            });
        }
    }

    public void uploadLogFile(ArrayList<String> arrayList, final UploadCallBack uploadCallBack) {
        if (arrayList == null || arrayList.size() == 0) {
            LogUtils.e_inner("UniLogger [uploadLogFile] filelist,  filePathList is error");
            return;
        }
        if (arrayList.size() == 1 && arrayList.get(0).endsWith(".zip")) {
            UploadProxy.getInstance().uploadFile(arrayList.get(0), uploadCallBack);
            return;
        }
        ArrayList<File> arrayList2 = new ArrayList<>();
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (!TextUtils.isEmpty(next)) {
                arrayList2.add(new File(next));
            }
        }
        ZipProxy.getInstance().zipFileListInOtherThread(arrayList2, false, true, GlobalPrarm.uniLoggerLogDirPath, new ZipCallBack() { // from class: com.netease.ntunisdk.unilogger.UniLogger.2
            @Override // com.netease.ntunisdk.unilogger.zip.ZipCallBack
            public void onZipResult(boolean z, ArrayList<File> arrayList3, String str) {
                LogUtils.i_inner("UniLogger [uploadLogFile] filelist, [onZipResult] suc=" + z + ", files=" + arrayList3.toString() + ", zipPath=" + str);
                if (z) {
                    UploadProxy.getInstance().uploadFile(str, uploadCallBack);
                } else {
                    uploadCallBack.onUploadResult(-1, str);
                }
            }
        });
    }
}