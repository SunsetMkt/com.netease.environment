package com.netease.androidcrashhandler.zip;

import android.os.Process;
import android.text.TextUtils;
import com.netease.androidcrashhandler.Const;
import com.netease.androidcrashhandler.NTCrashHunterKit;
import com.netease.androidcrashhandler.entity.di.DiInfo;
import com.netease.androidcrashhandler.entity.param.ParamsInfo;
import com.netease.androidcrashhandler.init.InitProxy;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class ZipCore implements Callable<Integer> {
    private String mErrorType = null;
    private boolean mIsAppLaunch = false;
    private boolean mAutoUpload = false;
    private JSONObject mExtensionInfos = null;
    private String mPackagePath = null;

    interface FileNameChecker {
        boolean checkFileName(String str);

        String getFileType(String str);
    }

    interface ZipListener {
        void onZipFinish(String str);
    }

    public boolean isAutoUpload() {
        return this.mAutoUpload;
    }

    public void setAutoUpload(boolean z) {
        this.mAutoUpload = z;
    }

    public void setExtensionInfos(JSONObject jSONObject) {
        this.mExtensionInfos = jSONObject;
    }

    public JSONObject getExtensionInfos() {
        return this.mExtensionInfos;
    }

    public void setIsAppLaunch(boolean z) {
        this.mIsAppLaunch = z;
    }

    public void setErrorType(String str) {
        this.mErrorType = str;
    }

    public String getErrorType() {
        return this.mErrorType;
    }

    public boolean isIsAppLaunch() {
        return this.mIsAppLaunch;
    }

    public void setPackagePath(String str) {
        this.mPackagePath = str;
    }

    @Override // java.util.concurrent.Callable
    public Integer call() throws Exception {
        LogUtils.i(LogUtils.TAG, "ZipCore [call] start auto upload:" + this.mAutoUpload + " is launch mode:" + this.mIsAppLaunch);
        List<String> listZipAllType = zipAllType();
        if (this.mAutoUpload && !listZipAllType.isEmpty()) {
            ZipProxy.getInstance().sendZipToUpload(listZipAllType);
        }
        return 1;
    }

    public String zipOtherTask(String str) {
        OtherZip otherZip = new OtherZip(this, str);
        otherZip.addDirFile(str);
        otherZip.setParamJson(createValidParamJson());
        return otherZip.zipFile();
    }

    private boolean identifySdkFile(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.endsWith(Const.FileNameTag.ACI_FILE) || str.endsWith(".zip") || str.endsWith(Const.FileNameTag.ZIP_TEMP_FILE) || str.endsWith(Const.FileNameTag.DI_FILE) || str.endsWith(Const.FileNameTag.ANR_FILE) || str.endsWith(Const.FileNameTag.TRACE_FILE) || str.endsWith(Const.FileNameTag.MESSAGE_FILE) || str.endsWith(Const.FileNameTag.DMP_FILE) || str.endsWith(Const.FileNameTag.STACK_FILE) || str.startsWith(Const.FileNameTag.MAIN_FILE) || str.endsWith(Const.FileNameTag.PARAM_FILE) || str.endsWith(Const.FileNameTag.CFG_FILE) || str.contains("ntunisdk_so_uuids") || str.contains(Const.FileNameTag.NATIVE_CRASH_MARK_FILE) || str.contains(Const.FileNameTag.NATIVE_CRASH_DMP_FILE) || str.contains(Const.FileNameTag.UNISDK_LOG_FILE_TEMP);
    }

    private File[] listFiles(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return new File(str).listFiles();
    }

    private List<File> getAllMinorFileByDir(String str) {
        File[] fileArrListFiles = listFiles(str);
        ArrayList arrayList = new ArrayList();
        if (fileArrListFiles != null) {
            for (File file : fileArrListFiles) {
                if (file.isFile() && !identifySdkFile(file.getName())) {
                    arrayList.add(file);
                }
            }
        }
        return arrayList;
    }

    public boolean checkDirValid(File file) {
        if (ZipUtil.checkFileTimeValid(file)) {
            return true;
        }
        LogUtils.i(LogUtils.TAG, "ZipCore [distinguishZip] file too old, file delete");
        CUtil.deleteDir(file.getAbsolutePath());
        return false;
    }

    private void zipDir(File file, List<File> list, ZipListener zipListener) {
        BaseZip baseZipCreateValidZipTask = createValidZipTask(file);
        if (baseZipCreateValidZipTask != null) {
            baseZipCreateValidZipTask.addDirFile(file.getAbsolutePath());
            baseZipCreateValidZipTask.copyExternalFile(list);
            baseZipCreateValidZipTask.setParamJson(createValidParamJson());
            String strZipFile = baseZipCreateValidZipTask.zipFile();
            if (TextUtils.isEmpty(strZipFile)) {
                return;
            }
            zipListener.onZipFinish(strZipFile);
        }
    }

    private void deleteMinorFiles(List<File> list) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            LogUtils.i(LogUtils.TAG, "ZipCore [delete other file] finish:" + list.get(i).getName());
            list.get(i).delete();
        }
    }

    public List<String> zipCurDir() {
        File[] fileArrListFiles;
        LogUtils.i(LogUtils.TAG, "ZipCore [zipHistory] start");
        ArrayList arrayList = new ArrayList();
        try {
            fileArrListFiles = listFiles(InitProxy.sUploadFilePath);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        if (fileArrListFiles == null) {
            return arrayList;
        }
        ArrayList arrayList2 = new ArrayList();
        arrayList2.addAll(getAllMinorFileByDir(InitProxy.sOldUploadFilePath));
        ArrayList<File> arrayList3 = new ArrayList();
        for (File file : fileArrListFiles) {
            if (file.isDirectory()) {
                arrayList3.add(file);
            } else if (file.isFile() && !identifySdkFile(file.getName())) {
                arrayList2.add(file);
            }
        }
        for (File file2 : arrayList3) {
            if (checkDirValid(file2)) {
                File[] fileArrListFiles2 = file2.listFiles();
                if (!file2.getName().contains("_" + Process.myPid() + "_") && (fileArrListFiles2 == null || fileArrListFiles2.length == 0)) {
                    CUtil.deleteDir(file2.getAbsolutePath());
                    LogUtils.i(LogUtils.TAG, "distinguishZip [checkDirValid] dir_have_no_file_delete_and_pass:" + file2.getName());
                } else if (fileArrListFiles2 == null || fileArrListFiles2.length == 0) {
                    LogUtils.i(LogUtils.TAG, "distinguishZip [zipHistory] this_process_mkdir_pass:" + file2.getName());
                } else {
                    zipDir(file2, arrayList2, new ZipListener() { // from class: com.netease.androidcrashhandler.zip.ZipCore.1
                        final /* synthetic */ List val$result;

                        AnonymousClass1(List arrayList4) {
                            list = arrayList4;
                        }

                        @Override // com.netease.androidcrashhandler.zip.ZipCore.ZipListener
                        public void onZipFinish(String str) {
                            list.add(str);
                        }
                    });
                }
            }
        }
        if (arrayList4.size() > 0) {
            deleteMinorFiles(arrayList2);
        }
        LogUtils.i(LogUtils.TAG, "ZipCore [distinguishZip] finish:" + arrayList4.toString());
        return arrayList4;
    }

    /* renamed from: com.netease.androidcrashhandler.zip.ZipCore$1 */
    class AnonymousClass1 implements ZipListener {
        final /* synthetic */ List val$result;

        AnonymousClass1(List arrayList4) {
            list = arrayList4;
        }

        @Override // com.netease.androidcrashhandler.zip.ZipCore.ZipListener
        public void onZipFinish(String str) {
            list.add(str);
        }
    }

    private BaseZip createValidZipTask(File file) {
        if (file.getName().startsWith(Const.FileNameTag.DIR_NATIVE_CRASH)) {
            LogUtils.i(LogUtils.TAG, "ZipCore [distinguishZip] zipNativeCrash");
            return new NativeCrashZip(this, file.getAbsolutePath());
        }
        if (file.getName().startsWith(Const.FileNameTag.DIR_JAVA_CRASH)) {
            LogUtils.i(LogUtils.TAG, "ZipCore [distinguishZip] zipJavaCrash");
            return new JavaCrashZip(this, file.getAbsolutePath());
        }
        if (file.getName().startsWith(Const.FileNameTag.DIR_ANR)) {
            LogUtils.i(LogUtils.TAG, "ZipCore [distinguishZip] zipAnr");
            return new AnrZip(this, file.getAbsolutePath());
        }
        if (file.getName().startsWith(Const.FileNameTag.DIR_OTHER)) {
            LogUtils.i(LogUtils.TAG, "ZipCore [distinguishZip] zipOther");
            return new OtherZip(this, file.getAbsolutePath());
        }
        if (!file.getName().startsWith(Const.FileNameTag.DIR_UNDEFINED_EXCEPTION)) {
            return null;
        }
        LogUtils.i(LogUtils.TAG, "ZipCore [distinguishZip] zipOther");
        return new UnKnownExceptionZip(this, file.getAbsolutePath());
    }

    private void zipOldDir(ZipListener zipListener) throws Throwable {
        ArrayList<String> allValidFileFromUploadDir = getAllValidFileFromUploadDir();
        allValidFileFromUploadDir.add(DiInfo.sCurFileName);
        addExtensionInfoToDiFile(DiInfo.sCurFileName);
        LogUtils.i(LogUtils.TAG, "ZipCore [zipAllType] filesList = " + allValidFileFromUploadDir.toString());
        HashMap<String, BaseZip> mapFilterDifferentTypes = filterDifferentTypes(allValidFileFromUploadDir);
        Iterator<String> it = mapFilterDifferentTypes.keySet().iterator();
        while (it.hasNext()) {
            BaseZip baseZip = mapFilterDifferentTypes.get(it.next());
            if (baseZip != null) {
                baseZip.setParamJson(crateOldFileParamJson());
                String strZipFile = baseZip.zipFile();
                if (!TextUtils.isEmpty(strZipFile)) {
                    zipListener.onZipFinish(strZipFile);
                }
            }
        }
    }

    public List<String> zipAllType() throws Throwable {
        LogUtils.i(LogUtils.TAG, "ZipCore [zipAllType] start");
        ArrayList arrayList = new ArrayList();
        if (TextUtils.isEmpty(InitProxy.sUploadFilePath)) {
            LogUtils.i(LogUtils.TAG, "ZipCore [zipAllType] param error");
            return arrayList;
        }
        arrayList.addAll(zipCurDir());
        zipOldDir(new ZipListener() { // from class: com.netease.androidcrashhandler.zip.ZipCore.2
            final /* synthetic */ List val$zipFileList;

            AnonymousClass2(List arrayList2) {
                list = arrayList2;
            }

            @Override // com.netease.androidcrashhandler.zip.ZipCore.ZipListener
            public void onZipFinish(String str) {
                LogUtils.i(LogUtils.TAG, "ZipCore [zipAllType] add to list, file name = " + str);
                list.add(str);
            }
        });
        LogUtils.i(LogUtils.TAG, "ZipCore [zipAllType] finish list:" + arrayList2.toString());
        return arrayList2;
    }

    /* renamed from: com.netease.androidcrashhandler.zip.ZipCore$2 */
    class AnonymousClass2 implements ZipListener {
        final /* synthetic */ List val$zipFileList;

        AnonymousClass2(List arrayList2) {
            list = arrayList2;
        }

        @Override // com.netease.androidcrashhandler.zip.ZipCore.ZipListener
        public void onZipFinish(String str) {
            LogUtils.i(LogUtils.TAG, "ZipCore [zipAllType] add to list, file name = " + str);
            list.add(str);
        }
    }

    private ArrayList<String> getAllValidFileFromUploadDir() {
        LogUtils.i(LogUtils.TAG, "ZipCore [getAllValidFileFromUploadDir] start");
        ArrayList<String> arrayList = new ArrayList<>();
        if (TextUtils.isEmpty(InitProxy.sOldUploadFilePath)) {
            LogUtils.i(LogUtils.TAG, "ZipCore [getAllValidFileFromUploadDir] mUploadFilePath error");
            return arrayList;
        }
        new File(InitProxy.sOldUploadFilePath).list(new FilenameFilter() { // from class: com.netease.androidcrashhandler.zip.ZipCore.3
            final /* synthetic */ ArrayList val$fileList;

            AnonymousClass3(ArrayList arrayList2) {
                arrayList = arrayList2;
            }

            @Override // java.io.FilenameFilter
            public boolean accept(File file, String str) {
                LogUtils.i(LogUtils.TAG, "ZipCore [getAllValidFileFromUploadDir] fileName = " + str);
                try {
                    File file2 = new File(file.getAbsolutePath() + "/" + str);
                    if (file2.isFile() && ZipCore.this.isCommonFile(str)) {
                        if (ZipUtil.checkFileTimeValid(file2)) {
                            LogUtils.i(LogUtils.TAG, "ZipCore [getAllValidFileFromUploadDir] need to zip");
                            arrayList.add(str);
                            return true;
                        }
                        LogUtils.i(LogUtils.TAG, "ZipCore [getAllValidFileFromUploadDir] file too old, file delete");
                        file2.delete();
                        return false;
                    }
                    LogUtils.i(LogUtils.TAG, "ZipCore [getAllValidFileFromUploadDir] do not zip");
                    return false;
                } catch (Exception e) {
                    LogUtils.i(LogUtils.TAG, "ZipCore [getAllValidFileFromUploadDir] Exception =" + e.toString());
                    e.printStackTrace();
                    return false;
                }
            }
        });
        LogUtils.i(LogUtils.TAG, "ZipCore [getAllValidFileFromUploadDir] need to zip , fileList=" + arrayList2.toString());
        return arrayList2;
    }

    /* renamed from: com.netease.androidcrashhandler.zip.ZipCore$3 */
    class AnonymousClass3 implements FilenameFilter {
        final /* synthetic */ ArrayList val$fileList;

        AnonymousClass3(ArrayList arrayList2) {
            arrayList = arrayList2;
        }

        @Override // java.io.FilenameFilter
        public boolean accept(File file, String str) {
            LogUtils.i(LogUtils.TAG, "ZipCore [getAllValidFileFromUploadDir] fileName = " + str);
            try {
                File file2 = new File(file.getAbsolutePath() + "/" + str);
                if (file2.isFile() && ZipCore.this.isCommonFile(str)) {
                    if (ZipUtil.checkFileTimeValid(file2)) {
                        LogUtils.i(LogUtils.TAG, "ZipCore [getAllValidFileFromUploadDir] need to zip");
                        arrayList.add(str);
                        return true;
                    }
                    LogUtils.i(LogUtils.TAG, "ZipCore [getAllValidFileFromUploadDir] file too old, file delete");
                    file2.delete();
                    return false;
                }
                LogUtils.i(LogUtils.TAG, "ZipCore [getAllValidFileFromUploadDir] do not zip");
                return false;
            } catch (Exception e) {
                LogUtils.i(LogUtils.TAG, "ZipCore [getAllValidFileFromUploadDir] Exception =" + e.toString());
                e.printStackTrace();
                return false;
            }
        }
    }

    public boolean isCommonFile(String str) {
        return (str.endsWith(".zip") || str.endsWith(Const.FileNameTag.ZIP_TEMP_FILE) || str.endsWith(Const.FileNameTag.PARAM_FILE) || str.endsWith(Const.FileNameTag.TEMP_FILE) || str.endsWith(Const.FileNameTag.CFG_FILE) || str.endsWith(Const.FileNameTag.DI_FILE)) ? false : true;
    }

    private BaseZip createSubZip(String str) {
        if (Const.ErrorTypeValue.JAVA_TYPE.equals(str)) {
            return new JavaCrashZip(this, InitProxy.sOldUploadFilePath);
        }
        if (Const.ErrorTypeValue.JNI_TYPE.equals(str)) {
            return new NativeCrashZip(this, InitProxy.sOldUploadFilePath);
        }
        if (Const.ErrorTypeValue.ANR_TYPE.equals(str)) {
            return new AnrZip(this, InitProxy.sOldUploadFilePath);
        }
        if (Const.ErrorTypeValue.OTHER_TYPE.equals(str) || Const.ErrorTypeValue.SCRIPT_TYPE.equals(str) || Const.ErrorTypeValue.U3D_TYPE.equals(str)) {
            return new AnrZip(this, InitProxy.sOldUploadFilePath);
        }
        return null;
    }

    private boolean addFileToDesMap(HashMap<String, BaseZip> map, String str, FileNameChecker fileNameChecker) {
        BaseZip baseZipCreateSubZip;
        if (!fileNameChecker.checkFileName(str)) {
            return false;
        }
        LogUtils.i(LogUtils.TAG, "ZipCore [addFileToDesMap] add to aciList, fileName = " + str);
        if (map.containsKey(fileNameChecker.getFileType(str))) {
            baseZipCreateSubZip = map.get(fileNameChecker.getFileType(str));
        } else {
            baseZipCreateSubZip = createSubZip(fileNameChecker.getFileType(str));
        }
        baseZipCreateSubZip.addFile(str);
        map.put(fileNameChecker.getFileType(str), baseZipCreateSubZip);
        return true;
    }

    private HashMap<String, BaseZip> filterDifferentTypes(ArrayList<String> arrayList) {
        LogUtils.i(LogUtils.TAG, "ZipCore [filterDifferentTypes] start");
        HashMap<String, BaseZip> map = new HashMap<>();
        if (arrayList == null || arrayList.size() <= 0) {
            LogUtils.i(LogUtils.TAG, "ZipCore [filterDifferentTypes] params error");
            return map;
        }
        ArrayList<String> arrayList2 = new ArrayList<>();
        LogUtils.i(LogUtils.TAG, "ZipCore [filterDifferentTypes] mIsAppLaunch = " + this.mIsAppLaunch);
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            String next = it.next();
            LogUtils.i(LogUtils.TAG, "ZipCore [filterDifferentTypes] fileName = " + next);
            if (!addFileToDesMap(map, next, new FileNameChecker() { // from class: com.netease.androidcrashhandler.zip.ZipCore.4
                @Override // com.netease.androidcrashhandler.zip.ZipCore.FileNameChecker
                public String getFileType(String str) {
                    return Const.ErrorTypeValue.JAVA_TYPE;
                }

                AnonymousClass4() {
                }

                @Override // com.netease.androidcrashhandler.zip.ZipCore.FileNameChecker
                public boolean checkFileName(String str) {
                    return str.endsWith(Const.FileNameTag.ACI_FILE);
                }
            }) && !addFileToDesMap(map, next, new FileNameChecker() { // from class: com.netease.androidcrashhandler.zip.ZipCore.5
                @Override // com.netease.androidcrashhandler.zip.ZipCore.FileNameChecker
                public String getFileType(String str) {
                    return Const.ErrorTypeValue.JNI_TYPE;
                }

                AnonymousClass5() {
                }

                @Override // com.netease.androidcrashhandler.zip.ZipCore.FileNameChecker
                public boolean checkFileName(String str) {
                    return str.endsWith(Const.FileNameTag.DMP_FILE) || str.endsWith(Const.FileNameTag.RUNTIME_FILE) || str.endsWith(Const.FileNameTag.STACK_FILE) || str.startsWith("javaStackTrace") || str.contains("ntunisdk_so_uuids");
                }
            }) && !addFileToDesMap(map, next, new FileNameChecker() { // from class: com.netease.androidcrashhandler.zip.ZipCore.6
                @Override // com.netease.androidcrashhandler.zip.ZipCore.FileNameChecker
                public String getFileType(String str) {
                    return Const.ErrorTypeValue.ANR_TYPE;
                }

                AnonymousClass6() {
                }

                @Override // com.netease.androidcrashhandler.zip.ZipCore.FileNameChecker
                public boolean checkFileName(String str) {
                    return str.endsWith(Const.FileNameTag.ANR_FILE) || str.endsWith(Const.FileNameTag.MESSAGE_FILE) || str.endsWith(Const.FileNameTag.TRACE_FILE);
                }
            }) && !addFileToDesMap(map, next, new FileNameChecker() { // from class: com.netease.androidcrashhandler.zip.ZipCore.7
                AnonymousClass7() {
                }

                @Override // com.netease.androidcrashhandler.zip.ZipCore.FileNameChecker
                public boolean checkFileName(String str) {
                    return str.startsWith(Const.FileNameTag.MAIN_FILE);
                }

                @Override // com.netease.androidcrashhandler.zip.ZipCore.FileNameChecker
                public String getFileType(String str) {
                    if (TextUtils.isEmpty(ZipCore.this.mErrorType)) {
                        if (str.endsWith(Const.FileNameTag.OTHER_FILE)) {
                            ZipCore.this.mErrorType = Const.ErrorTypeValue.OTHER_TYPE;
                        } else if (str.endsWith(Const.FileNameTag.SCRIPT_FILE) && TextUtils.isEmpty(ZipCore.this.mErrorType)) {
                            ZipCore.this.mErrorType = Const.ErrorTypeValue.SCRIPT_TYPE;
                        } else if (str.endsWith(Const.FileNameTag.U3D_FILE) && TextUtils.isEmpty(ZipCore.this.mErrorType)) {
                            ZipCore.this.mErrorType = Const.ErrorTypeValue.U3D_TYPE;
                        }
                    }
                    return ZipCore.this.mErrorType;
                }
            })) {
                LogUtils.i(LogUtils.TAG, "ZipCore [filterDifferentTypes] add to baseList, fileName = " + next);
                arrayList2.add(next);
            }
        }
        File[] fileArrListFiles = new File(InitProxy.sUploadFilePath).listFiles();
        ArrayList arrayList3 = new ArrayList();
        boolean z = false;
        for (File file : fileArrListFiles) {
            if (file.isFile() && !identifySdkFile(file.getName())) {
                arrayList3.add(file);
            }
        }
        Iterator<String> it2 = map.keySet().iterator();
        while (it2.hasNext()) {
            BaseZip baseZip = map.get(it2.next());
            baseZip.addFileList(arrayList2);
            if (arrayList3.size() > 0) {
                baseZip.copyExternalFile(arrayList3);
                z = true;
            }
        }
        if (z) {
            Iterator it3 = arrayList3.iterator();
            while (it3.hasNext()) {
                ((File) it3.next()).delete();
            }
            LogUtils.i(LogUtils.TAG, "ZipCore [filterDifferentTypes] delete file = " + arrayList3.toString());
        }
        LogUtils.i(LogUtils.TAG, "ZipCore [filterDifferentTypes] result = " + map.toString());
        return map;
    }

    /* renamed from: com.netease.androidcrashhandler.zip.ZipCore$4 */
    class AnonymousClass4 implements FileNameChecker {
        @Override // com.netease.androidcrashhandler.zip.ZipCore.FileNameChecker
        public String getFileType(String str) {
            return Const.ErrorTypeValue.JAVA_TYPE;
        }

        AnonymousClass4() {
        }

        @Override // com.netease.androidcrashhandler.zip.ZipCore.FileNameChecker
        public boolean checkFileName(String str) {
            return str.endsWith(Const.FileNameTag.ACI_FILE);
        }
    }

    /* renamed from: com.netease.androidcrashhandler.zip.ZipCore$5 */
    class AnonymousClass5 implements FileNameChecker {
        @Override // com.netease.androidcrashhandler.zip.ZipCore.FileNameChecker
        public String getFileType(String str) {
            return Const.ErrorTypeValue.JNI_TYPE;
        }

        AnonymousClass5() {
        }

        @Override // com.netease.androidcrashhandler.zip.ZipCore.FileNameChecker
        public boolean checkFileName(String str) {
            return str.endsWith(Const.FileNameTag.DMP_FILE) || str.endsWith(Const.FileNameTag.RUNTIME_FILE) || str.endsWith(Const.FileNameTag.STACK_FILE) || str.startsWith("javaStackTrace") || str.contains("ntunisdk_so_uuids");
        }
    }

    /* renamed from: com.netease.androidcrashhandler.zip.ZipCore$6 */
    class AnonymousClass6 implements FileNameChecker {
        @Override // com.netease.androidcrashhandler.zip.ZipCore.FileNameChecker
        public String getFileType(String str) {
            return Const.ErrorTypeValue.ANR_TYPE;
        }

        AnonymousClass6() {
        }

        @Override // com.netease.androidcrashhandler.zip.ZipCore.FileNameChecker
        public boolean checkFileName(String str) {
            return str.endsWith(Const.FileNameTag.ANR_FILE) || str.endsWith(Const.FileNameTag.MESSAGE_FILE) || str.endsWith(Const.FileNameTag.TRACE_FILE);
        }
    }

    /* renamed from: com.netease.androidcrashhandler.zip.ZipCore$7 */
    class AnonymousClass7 implements FileNameChecker {
        AnonymousClass7() {
        }

        @Override // com.netease.androidcrashhandler.zip.ZipCore.FileNameChecker
        public boolean checkFileName(String str) {
            return str.startsWith(Const.FileNameTag.MAIN_FILE);
        }

        @Override // com.netease.androidcrashhandler.zip.ZipCore.FileNameChecker
        public String getFileType(String str) {
            if (TextUtils.isEmpty(ZipCore.this.mErrorType)) {
                if (str.endsWith(Const.FileNameTag.OTHER_FILE)) {
                    ZipCore.this.mErrorType = Const.ErrorTypeValue.OTHER_TYPE;
                } else if (str.endsWith(Const.FileNameTag.SCRIPT_FILE) && TextUtils.isEmpty(ZipCore.this.mErrorType)) {
                    ZipCore.this.mErrorType = Const.ErrorTypeValue.SCRIPT_TYPE;
                } else if (str.endsWith(Const.FileNameTag.U3D_FILE) && TextUtils.isEmpty(ZipCore.this.mErrorType)) {
                    ZipCore.this.mErrorType = Const.ErrorTypeValue.U3D_TYPE;
                }
            }
            return ZipCore.this.mErrorType;
        }
    }

    private JSONObject crateOldFileParamJson() throws Throwable {
        JSONObject jSONObject;
        String strFile2Str = CUtil.file2Str(InitProxy.sOldUploadFilePath, "crashhunter.param");
        LogUtils.i(LogUtils.TAG, "ZipCore [crateOldFileParamJson] start is lanuch mode:" + this.mIsAppLaunch);
        JSONObject jSONObject2 = null;
        try {
            jSONObject = new JSONObject(strFile2Str);
        } catch (Exception e) {
            e = e;
        }
        try {
        } catch (Exception e2) {
            e = e2;
            jSONObject2 = jSONObject;
            e.printStackTrace();
            LogUtils.i(LogUtils.TAG, "ZipCore [crateOldFileParamJson] Exception e=" + e.toString());
            jSONObject = jSONObject2;
            LogUtils.i(LogUtils.TAG, "ZipCore [crateOldFileParamJson] reasonableParamJson=" + jSONObject);
            return jSONObject;
        }
        if (jSONObject.length() < 0) {
            LogUtils.i(LogUtils.TAG, "ZipCore [crateOldFileParamJson] paramsMap error");
            return jSONObject;
        }
        String strOptString = jSONObject.optString(Const.ParamKey.RES_VERSION);
        String strOptString2 = jSONObject.optString(Const.ParamKey.ENGINE_VERSION);
        jSONObject.put(Const.ParamKey.CLIENT_V, strOptString2 + "(" + strOptString + ")");
        LogUtils.i(LogUtils.TAG, "ZipCore [crateOldFileParamJson] reasonableParamJson = " + jSONObject.toString() + ", engineVersion=" + strOptString2 + ", resVersion=" + strOptString);
        LogUtils.i(LogUtils.TAG, "ZipCore [crateOldFileParamJson] reasonableParamJson=" + jSONObject);
        return jSONObject;
    }

    private JSONObject createValidParamJson() throws JSONException {
        ParamsInfo paramsInfo;
        LogUtils.i(LogUtils.TAG, "ZipCore [createValidParamJson] start is lanuch mode:" + this.mIsAppLaunch);
        JSONObject jSONObject = null;
        try {
            if (this.mIsAppLaunch) {
                paramsInfo = NTCrashHunterKit.sharedKit().getmLastTimeParamsInfo();
                LogUtils.i(LogUtils.TAG, "ZipCore [createValidParamJson] paramsInfo is null 111");
            } else {
                paramsInfo = NTCrashHunterKit.sharedKit().getmCurrentParamsInfo();
                LogUtils.i(LogUtils.TAG, "ZipCore [createValidParamJson] paramsInfo is null 222");
            }
        } catch (Exception e) {
            e = e;
        }
        if (paramsInfo == null) {
            LogUtils.i(LogUtils.TAG, "ZipCore [createValidParamJson] paramsInfo is null");
            return null;
        }
        JSONObject jSONObject2 = new JSONObject(paramsInfo.getmParamsJson().toString());
        try {
            LogUtils.i(LogUtils.TAG, "ZipCore [createValidParamJson] validParamJson:" + jSONObject2.toString());
        } catch (Exception e2) {
            e = e2;
            jSONObject = jSONObject2;
            e.printStackTrace();
            LogUtils.i(LogUtils.TAG, "ZipCore [createValidParamJson] Exception e=" + e.toString());
            jSONObject2 = jSONObject;
            LogUtils.i(LogUtils.TAG, "ZipCore [createValidParamJson] reasonableParamJson=" + jSONObject2);
            return jSONObject2;
        }
        if (jSONObject2.length() < 0) {
            LogUtils.i(LogUtils.TAG, "ZipCore [createValidParamJson] paramsMap error");
            return jSONObject2;
        }
        String strOptString = jSONObject2.optString(Const.ParamKey.RES_VERSION);
        String strOptString2 = jSONObject2.optString(Const.ParamKey.ENGINE_VERSION);
        jSONObject2.put(Const.ParamKey.CLIENT_V, strOptString2 + "(" + strOptString + ")");
        LogUtils.i(LogUtils.TAG, "ZipCore [createValidParamJson] reasonableParamJson = " + jSONObject2.toString() + ", engineVersion=" + strOptString2 + ", resVersion=" + strOptString);
        LogUtils.i(LogUtils.TAG, "ZipCore [createValidParamJson] reasonableParamJson=" + jSONObject2);
        return jSONObject2;
    }

    private void addExtensionInfoToDiFile(String str) throws Throwable {
        CUtil.addInfoToDiFile(InitProxy.sOldUploadFilePath, str, "filter_pipe", this.mExtensionInfos.toString());
    }
}