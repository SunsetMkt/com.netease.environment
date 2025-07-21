package com.netease.unisdk.ngvoice;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.text.TextUtils;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import com.netease.unisdk.ngvoice.log.NgLog;
import com.netease.unisdk.ngvoice.task.TaskExecutor;
import com.netease.unisdk.ngvoice.utils.FileUtil;
import com.netease.unisdk.ngvoice.utils.StorageUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes5.dex */
public class NgVoiceManager implements NgVoiceInterface {
    public static final int IDLE_STATE = 0;
    private static final int MIN_USABLE_SPACE = 5242880;
    private static final int NG_VIDEO_PERMISSIONS_REQUEST_CODE = 105;
    public static final int PLAYING_STATE = 2;
    public static final int RECORDING_STATE = 1;
    private static final String TAG = "ng_voice Manager";
    private static final String VOICE_DIR_NAME = "ng_voice";
    private static final String VOICE_FILE_SUFFIX = ".amr";
    private static NgVoiceManager sInstance;
    private AudioManager mAudioManager;
    private Context mContext;
    private MediaPlayer mPlayer;
    private MediaRecorder mRecorder;
    private NgVoiceSettings mSettings;
    private long mStartRecordTime;
    private int mState;
    private File mVoiceFile;
    private List<NgVoiceCallback> mCallbacks = new ArrayList();
    private NgVoiceHttpHelper mHttpHelper = new NgVoiceHttpHelper();

    private NgVoiceManager(Context context) {
        this.mContext = context;
        TaskExecutor.init(2, 5, 0);
    }

    public static NgVoiceManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (NgVoiceManager.class) {
                if (sInstance == null) {
                    sInstance = new NgVoiceManager(context);
                    NgLog.checkIsDebug(context);
                }
            }
        }
        return sInstance;
    }

    public void setCallback(NgVoiceCallback ngVoiceCallback) {
        this.mCallbacks.add(ngVoiceCallback);
    }

    public void setVoiceSettings(NgVoiceSettings ngVoiceSettings) {
        this.mSettings = ngVoiceSettings;
        this.mHttpHelper.setVoiceSettings(ngVoiceSettings);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startRecord(String str) throws IllegalStateException, IOException, IllegalArgumentException {
        NgLog.i(TAG, "start record in thread : " + Thread.currentThread().getId());
        if (this.mSettings == null) {
            NgVoiceSettings ngVoiceSettings = new NgVoiceSettings();
            this.mSettings = ngVoiceSettings;
            this.mHttpHelper.setVoiceSettings(ngVoiceSettings);
        }
        File fileDir = getFileDir(5242880L);
        if (fileDir == null) {
            NgLog.e(TAG, "can't find a path to save voice file");
            TaskExecutor.runTaskOnUiThread(new Runnable() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.1
                @Override // java.lang.Runnable
                public void run() {
                    Iterator it = NgVoiceManager.this.mCallbacks.iterator();
                    while (it.hasNext()) {
                        ((NgVoiceCallback) it.next()).onRecordFinish(false, null, 0.0f, NgVoiceCallback.ERROR_NO_ENOUGH_SPACE);
                    }
                }
            });
            return;
        }
        if (TextUtils.isEmpty(str)) {
            str = System.currentTimeMillis() + VOICE_FILE_SUFFIX;
        }
        File fileCreateFile = FileUtil.createFile(fileDir, str);
        this.mVoiceFile = fileCreateFile;
        if (fileCreateFile == null) {
            NgLog.e(TAG, "can't create voice file");
            TaskExecutor.runTaskOnUiThread(new Runnable() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.2
                @Override // java.lang.Runnable
                public void run() {
                    Iterator it = NgVoiceManager.this.mCallbacks.iterator();
                    while (it.hasNext()) {
                        ((NgVoiceCallback) it.next()).onRecordFinish(false, null, 0.0f, NgVoiceCallback.ERROR_CREATE_FILE_ERROR);
                    }
                }
            });
            return;
        }
        NgLog.i(TAG, "voice file save path = %s", fileCreateFile.getAbsolutePath());
        if (this.mRecorder != null) {
            stopRecord(false, false);
        }
        NgLog.i(TAG, "new MediaRecorder");
        MediaRecorder mediaRecorder = new MediaRecorder();
        this.mRecorder = mediaRecorder;
        try {
            mediaRecorder.setAudioSource(1);
            this.mRecorder.setOutputFormat(3);
            this.mRecorder.setAudioEncoder(1);
            this.mRecorder.setAudioEncodingBitRate(4750);
            this.mRecorder.setMaxDuration(this.mSettings.maxDuration);
            this.mRecorder.setOutputFile(this.mVoiceFile.getAbsolutePath());
            this.mRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.3
                @Override // android.media.MediaRecorder.OnErrorListener
                public void onError(MediaRecorder mediaRecorder2, int i, int i2) throws IllegalStateException {
                    NgLog.e(NgVoiceManager.TAG, "record onError what = %d,extra = %d", Integer.valueOf(i), Integer.valueOf(i2));
                    NgVoiceManager.this.stopRecord(false, true);
                    TaskExecutor.runTaskOnUiThread(new Runnable() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Iterator it = NgVoiceManager.this.mCallbacks.iterator();
                            while (it.hasNext()) {
                                ((NgVoiceCallback) it.next()).onRecordFinish(false, null, 0.0f, "exception");
                            }
                        }
                    });
                }
            });
            this.mRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.4
                @Override // android.media.MediaRecorder.OnInfoListener
                public void onInfo(MediaRecorder mediaRecorder2, int i, int i2) throws IllegalStateException {
                    NgLog.i(NgVoiceManager.TAG, "record onInfo what = %d,extra = %d", Integer.valueOf(i), Integer.valueOf(i2));
                    if (i == 800) {
                        NgVoiceManager.this.stopRecord(true, true);
                    }
                }
            });
            this.mRecorder.prepare();
            try {
                this.mRecorder.start();
                this.mStartRecordTime = System.currentTimeMillis();
                this.mState = 1;
                NgLog.i(TAG, "startRecord end");
            } catch (Exception e) {
                NgLog.e(TAG, "Recorder.start Exception : " + e.getMessage());
                stopRecord(false, false);
                TaskExecutor.runTaskOnUiThread(new Runnable() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.6
                    @Override // java.lang.Runnable
                    public void run() {
                        Iterator it = NgVoiceManager.this.mCallbacks.iterator();
                        while (it.hasNext()) {
                            ((NgVoiceCallback) it.next()).onRecordFinish(false, null, 0.0f, "exception");
                        }
                    }
                });
            }
        } catch (Exception e2) {
            NgLog.e(TAG, "prepare >> " + e2.getMessage());
            stopRecord(false, false);
            TaskExecutor.runTaskOnUiThread(new Runnable() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.5
                @Override // java.lang.Runnable
                public void run() {
                    Iterator it = NgVoiceManager.this.mCallbacks.iterator();
                    while (it.hasNext()) {
                        ((NgVoiceCallback) it.next()).onRecordFinish(false, null, 0.0f, "exception");
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopRecord(boolean z, boolean z2) throws IllegalStateException {
        if (z2) {
            try {
                this.mRecorder.stop();
            } catch (Exception e) {
                NgLog.e(TAG, "stopRecord Exception : " + e.getMessage());
            }
        }
        this.mRecorder.release();
        if (z && this.mVoiceFile != null) {
            float fCurrentTimeMillis = ((System.currentTimeMillis() - this.mStartRecordTime) * 1.0f) / 1000.0f;
            Iterator<NgVoiceCallback> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                it.next().onRecordFinish(true, this.mVoiceFile.getAbsolutePath(), fCurrentTimeMillis, null);
            }
        }
        this.mRecorder = null;
        this.mState = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startPlayback(String str) throws IllegalStateException, IOException, IllegalArgumentException {
        NgLog.i(TAG, "start playback in thread : " + Thread.currentThread().getId());
        NgLog.i(TAG, "voiceFilePath : " + str);
        this.mPlayer = new MediaPlayer();
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            this.mPlayer.reset();
            this.mPlayer.setDataSource(fileInputStream.getFD());
            this.mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.7
                @Override // android.media.MediaPlayer.OnCompletionListener
                public void onCompletion(MediaPlayer mediaPlayer) throws IllegalStateException {
                    NgLog.i(NgVoiceManager.TAG, "play onCompletion");
                    NgVoiceManager.this.stopPlayback();
                    TaskExecutor.runTaskOnUiThread(new Runnable() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.7.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Iterator it = NgVoiceManager.this.mCallbacks.iterator();
                            while (it.hasNext()) {
                                ((NgVoiceCallback) it.next()).onPlaybackFinish(true);
                            }
                        }
                    });
                }
            });
            this.mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.8
                @Override // android.media.MediaPlayer.OnErrorListener
                public boolean onError(MediaPlayer mediaPlayer, int i, int i2) throws IllegalStateException {
                    NgLog.e(NgVoiceManager.TAG, "play onError what = %d,extra = %d", Integer.valueOf(i), Integer.valueOf(i2));
                    NgVoiceManager.this.stopPlayback();
                    TaskExecutor.runTaskOnUiThread(new Runnable() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.8.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Iterator it = NgVoiceManager.this.mCallbacks.iterator();
                            while (it.hasNext()) {
                                ((NgVoiceCallback) it.next()).onPlaybackFinish(false);
                            }
                        }
                    });
                    return false;
                }
            });
            this.mPlayer.prepare();
            this.mPlayer.start();
            this.mState = 2;
        } catch (Exception e) {
            e.printStackTrace();
            stopPlayback();
            TaskExecutor.runTaskOnUiThread(new Runnable() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.9
                @Override // java.lang.Runnable
                public void run() {
                    Iterator it = NgVoiceManager.this.mCallbacks.iterator();
                    while (it.hasNext()) {
                        ((NgVoiceCallback) it.next()).onPlaybackFinish(false);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopPlayback() throws IllegalStateException {
        MediaPlayer mediaPlayer = this.mPlayer;
        if (mediaPlayer == null || this.mState != 2) {
            return;
        }
        try {
            mediaPlayer.stop();
            this.mPlayer.release();
        } catch (Exception unused) {
        }
        this.mPlayer = null;
        this.mState = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void uploadVoiceFile(final String str) {
        final String strUpload = this.mHttpHelper.upload(new File(str));
        final boolean z = !TextUtils.isEmpty(strUpload);
        TaskExecutor.runTaskOnUiThread(new Runnable() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.10
            @Override // java.lang.Runnable
            public void run() {
                Iterator it = NgVoiceManager.this.mCallbacks.iterator();
                while (it.hasNext()) {
                    ((NgVoiceCallback) it.next()).onUploadFinish(z, str, strUpload);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void translateFinish(final String str, final String str2) {
        TaskExecutor.runTaskOnUiThread(new Runnable() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.11
            @Override // java.lang.Runnable
            public void run() {
                Iterator it = NgVoiceManager.this.mCallbacks.iterator();
                while (it.hasNext()) {
                    ((NgVoiceCallback) it.next()).onTranslateFinish(str, str2);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public File getFileDir(long j) {
        if (StorageUtil.isSDCardAvailable()) {
            File fileCheckDirUsable = checkDirUsable(StorageUtil.getExternalFileDir(this.mContext), j);
            return fileCheckDirUsable == null ? checkDirUsable(this.mContext.getFilesDir(), j) : fileCheckDirUsable;
        }
        return checkDirUsable(this.mContext.getFilesDir(), j);
    }

    private File checkDirUsable(File file, long j) {
        if (file == null) {
            return null;
        }
        File file2 = new File(file, VOICE_DIR_NAME);
        if (FileUtil.createDir(file2) == null) {
            NgLog.w(TAG, "can't create dir <%s>", file2.getAbsolutePath());
            return null;
        }
        if (hasUsableSpace(file2, j)) {
            return file2;
        }
        NgLog.w(TAG, "<%s> has't enough space", file2.getAbsolutePath());
        return null;
    }

    private boolean hasUsableSpace(File file, long j) {
        long usableSpace = StorageUtil.getUsableSpace(file);
        NgLog.i(TAG, " %s :usable size = " + usableSpace, file.getAbsolutePath());
        return usableSpace > j;
    }

    @Override // com.netease.unisdk.ngvoice.NgVoiceInterface
    public boolean hasPermissions() {
        return checkPermissions("android.permission.RECORD_AUDIO") && checkPermissions("android.permission.WRITE_EXTERNAL_STORAGE");
    }

    @Override // com.netease.unisdk.ngvoice.NgVoiceInterface
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i != 105 || iArr == null) {
            return;
        }
        int length = iArr.length;
        boolean z = false;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                z = true;
                break;
            } else if (iArr[i2] != 0) {
                break;
            } else {
                i2++;
            }
        }
        Iterator<NgVoiceCallback> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            it.next().onRequestPermissions(z);
        }
    }

    @Override // com.netease.unisdk.ngvoice.NgVoiceInterface
    public void requestPermissions() {
        boolean zCheckPermissions = checkPermissions("android.permission.RECORD_AUDIO");
        boolean zCheckPermissions2 = checkPermissions("android.permission.WRITE_EXTERNAL_STORAGE");
        NgLog.i(TAG, "permission.RECORD_AUDIO : " + zCheckPermissions);
        NgLog.i(TAG, "permission.WRITE_EXTERNAL_STORAGE : " + zCheckPermissions2);
        if (zCheckPermissions && zCheckPermissions2) {
            Iterator<NgVoiceCallback> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                it.next().onRequestPermissions(true);
            }
            return;
        }
        TaskExecutor.runTaskOnUiThread(new Runnable() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.12
            @Override // java.lang.Runnable
            public void run() {
                ActivityCompat.requestPermissions((Activity) NgVoiceManager.this.mContext, new String[]{"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"}, 105);
            }
        });
    }

    @Override // com.netease.unisdk.ngvoice.NgVoiceInterface
    public void ntStartRecord(final String str) {
        NgLog.i(TAG, "nt start record ... " + str);
        int i = this.mState;
        if (i == 2) {
            stopPlayback();
        } else if (i == 1) {
            stopRecord(false, true);
        }
        TaskExecutor.executeTask(new Runnable() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.13
            @Override // java.lang.Runnable
            public void run() throws IllegalStateException, IOException, IllegalArgumentException {
                NgVoiceManager.this.startRecord(str);
            }
        });
    }

    private boolean checkPermissions(String str) {
        int i;
        try {
            i = this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0).applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            i = 0;
        }
        NgLog.i(TAG, "targetSdkVersion = " + i);
        if (i >= 23) {
            if (this.mContext.checkSelfPermission(str) != 0) {
                return false;
            }
        } else if (PermissionChecker.checkSelfPermission(this.mContext, str) != 0) {
            return false;
        }
        return true;
    }

    @Override // com.netease.unisdk.ngvoice.NgVoiceInterface
    public void ntStopRecord() {
        NgLog.i(TAG, "nt stop record ... ");
        if (this.mRecorder == null || this.mState != 1) {
            TaskExecutor.runTaskOnUiThread(new Runnable() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.14
                @Override // java.lang.Runnable
                public void run() {
                    Iterator it = NgVoiceManager.this.mCallbacks.iterator();
                    while (it.hasNext()) {
                        ((NgVoiceCallback) it.next()).onRecordFinish(false, null, 0.0f, "short_time");
                    }
                }
            });
        } else {
            stopRecord(true, true);
        }
    }

    @Override // com.netease.unisdk.ngvoice.NgVoiceInterface
    public void ntStartPlayback(final String str) {
        NgLog.i(TAG, "nt start playback ... " + str);
        int i = this.mState;
        if (i == 2) {
            stopPlayback();
        } else if (i == 1) {
            stopRecord(false, true);
        }
        TaskExecutor.executeTask(new Runnable() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.15
            @Override // java.lang.Runnable
            public void run() throws IllegalStateException, IOException, IllegalArgumentException {
                NgVoiceManager.this.startPlayback(str);
            }
        });
    }

    @Override // com.netease.unisdk.ngvoice.NgVoiceInterface
    public void ntStopPlayback() {
        NgLog.i(TAG, "nt stop playback ... ");
        stopPlayback();
    }

    @Override // com.netease.unisdk.ngvoice.NgVoiceInterface
    public void ntCancelRecord() {
        NgLog.i(TAG, "nt cancel record ... ");
        stopRecord(false, true);
        File file = this.mVoiceFile;
        if (file != null) {
            file.delete();
        }
    }

    @Override // com.netease.unisdk.ngvoice.NgVoiceInterface
    public void ntUploadVoiceFile(final String str) {
        NgLog.i(TAG, "nt upload voice file ... " + str);
        if (!NgVoiceHttpHelper.isNetworkAvailable(this.mContext)) {
            NgLog.e(TAG, "network not available");
            Iterator<NgVoiceCallback> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                it.next().onUploadFinish(false, str, null);
            }
            return;
        }
        TaskExecutor.executeTask(new Runnable() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.16
            @Override // java.lang.Runnable
            public void run() {
                NgVoiceManager.this.uploadVoiceFile(str);
            }
        });
    }

    @Override // com.netease.unisdk.ngvoice.NgVoiceInterface
    public void ntDownloadVoiceFile(final String str, final String str2) {
        NgLog.i(TAG, "nt download voice file ... key = %s,voiceFileName = %s", str, str2);
        if (!NgVoiceHttpHelper.isNetworkAvailable(this.mContext)) {
            NgLog.e(TAG, "network not available");
            Iterator<NgVoiceCallback> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                it.next().onDownloadFinish(false, str, null);
            }
            return;
        }
        TaskExecutor.executeTask(new Runnable() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.17
            @Override // java.lang.Runnable
            public void run() throws IOException {
                String str3;
                InputStream inputStreamDownloadVoiceFile = NgVoiceManager.this.mHttpHelper.downloadVoiceFile(str);
                byte[] bArr = new byte[1];
                try {
                    inputStreamDownloadVoiceFile.read(bArr);
                    str3 = new String(bArr);
                } catch (Exception e) {
                    e.printStackTrace();
                    str3 = null;
                }
                if ("0".equals(str3)) {
                    File fileDir = NgVoiceManager.this.getFileDir(5242880L);
                    if (fileDir == null) {
                        TaskExecutor.runTaskOnUiThread(new Runnable() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.17.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Iterator it2 = NgVoiceManager.this.mCallbacks.iterator();
                                while (it2.hasNext()) {
                                    ((NgVoiceCallback) it2.next()).onDownloadFinish(false, str, null);
                                }
                            }
                        });
                        return;
                    }
                    String str4 = str2;
                    if (TextUtils.isEmpty(str4)) {
                        str4 = System.currentTimeMillis() + NgVoiceManager.VOICE_FILE_SUFFIX;
                    }
                    final File fileCreateFile = FileUtil.createFile(fileDir, str4);
                    if (!NgVoiceManager.this.saveDownloadVoiceFile(inputStreamDownloadVoiceFile, fileCreateFile)) {
                        NgVoiceManager.this.downloadError(str, null);
                        return;
                    } else {
                        TaskExecutor.runTaskOnUiThread(new Runnable() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.17.2
                            @Override // java.lang.Runnable
                            public void run() {
                                Iterator it2 = NgVoiceManager.this.mCallbacks.iterator();
                                while (it2.hasNext()) {
                                    ((NgVoiceCallback) it2.next()).onDownloadFinish(true, str, fileCreateFile.getAbsolutePath());
                                }
                            }
                        });
                        return;
                    }
                }
                NgVoiceManager.this.downloadError(str, null);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downloadError(final String str, final String str2) {
        TaskExecutor.runTaskOnUiThread(new Runnable() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.18
            @Override // java.lang.Runnable
            public void run() {
                Iterator it = NgVoiceManager.this.mCallbacks.iterator();
                while (it.hasNext()) {
                    ((NgVoiceCallback) it.next()).onDownloadFinish(false, str, str2);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean saveDownloadVoiceFile(InputStream inputStream, File file) throws IOException {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            inputStream.skip(1L);
            byte[] bArr = new byte[1024];
            while (true) {
                int i = inputStream.read(bArr);
                if (i > 0) {
                    fileOutputStream.write(bArr, 0, i);
                } else {
                    fileOutputStream.close();
                    inputStream.close();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override // com.netease.unisdk.ngvoice.NgVoiceInterface
    public void ntGetTranslation(final String str) {
        NgLog.i(TAG, "nt get translation ... " + str);
        if (!NgVoiceHttpHelper.isNetworkAvailable(this.mContext)) {
            NgLog.e(TAG, "network not available");
            Iterator<NgVoiceCallback> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                it.next().onTranslateFinish(str, "");
            }
            return;
        }
        TaskExecutor.executeTask(new Runnable() { // from class: com.netease.unisdk.ngvoice.NgVoiceManager.19
            @Override // java.lang.Runnable
            public void run() {
                NgVoiceManager.this.translateFinish(str, NgVoiceManager.this.mHttpHelper.getTranslation(str));
            }
        });
    }

    @Override // com.netease.unisdk.ngvoice.NgVoiceInterface
    public void ntClearVoiceCache(long j) {
        if (StorageUtil.isSDCardAvailable()) {
            doDelete(new File(StorageUtil.getExternalFileDir(this.mContext), VOICE_DIR_NAME), j);
        }
        doDelete(new File(this.mContext.getFilesDir(), VOICE_DIR_NAME), j);
    }

    @Override // com.netease.unisdk.ngvoice.NgVoiceInterface
    public float ntGetVoiceAmplitude() {
        if (this.mRecorder == null) {
            return 0.0f;
        }
        float maxAmplitude = (r0.getMaxAmplitude() * 1.0f) / 300.0f;
        return (maxAmplitude > 1.0f ? (float) (Math.log10(maxAmplitude) * 20.0d) : 0.0f) / 70.0f;
    }

    private void doDelete(File file, long j) {
        File[] fileArrListFiles;
        if (file == null || !file.exists() || (fileArrListFiles = file.listFiles()) == null || fileArrListFiles.length == 0) {
            return;
        }
        for (File file2 : fileArrListFiles) {
            if (System.currentTimeMillis() - file2.lastModified() > 1000 * j) {
                NgLog.i(TAG, "delete file :%s", file2.getAbsolutePath());
                file2.delete();
            }
        }
    }

    public static void clear() {
        if (sInstance != null) {
            TaskExecutor.shutdown();
        }
        sInstance = null;
    }
}