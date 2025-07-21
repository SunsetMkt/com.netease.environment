package com.netease.download.downloader;

import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import com.netease.download.util.LogUtil;
import com.netease.download.util.Util;
import com.netease.ntunisdk.okhttp3.Headers;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.json.JSONArray;
import org.json.JSONException;

/* loaded from: classes3.dex */
public class TaskHandleOp {
    private static final String TAG = "TaskHandleOp";
    private static TaskHandleOp sTaskHandleOp;
    private TaskHandle mTaskHandle = null;
    private BlockingQueue<InfoUnit> mQueue = new ArrayBlockingQueue(5000);
    private boolean mIsStart = false;

    private TaskHandleOp() {
    }

    public static TaskHandleOp getInstance() {
        if (sTaskHandleOp == null) {
            sTaskHandleOp = new TaskHandleOp();
        }
        return sTaskHandleOp;
    }

    public void reset() {
        this.mTaskHandle = null;
        this.mQueue.clear();
        this.mTaskHandle = new TaskHandle();
    }

    public synchronized void add(InfoUnit infoUnit) {
        try {
            BlockingQueue<InfoUnit> blockingQueue = this.mQueue;
            if (blockingQueue != null) {
                blockingQueue.add(infoUnit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addHttpCount() {
        LogUtil.i(TAG, "TaskHandleOp [addHttpCount] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "HttpCount";
            add(infoUnit);
        }
    }

    public void addHttpSuccessCount() {
        LogUtil.i(TAG, "TaskHandleOp [addHttpSuccessCount] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "HttpSuccessCount";
            add(infoUnit);
        }
    }

    public void addHttpFailCount() {
        LogUtil.i(TAG, "TaskHandleOp [addHttpFailCount] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "HttpFailCount";
            add(infoUnit);
        }
    }

    public void addHttpFailCountToMap(int i) {
        LogUtil.i(TAG, "TaskHandleOp [addHttpFailCountToMap] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "HttpFailMap";
            infoUnit.mCode = i;
            add(infoUnit);
        }
    }

    public synchronized void addHttpFailFileNameMapToMap(int i, Headers headers, String str) {
        LogUtil.i(TAG, "TaskHandleOp [addHttpFailFileNameMapToMap] start");
        LogUtil.i(TAG, "TaskHandleOp [addHttpFailFileNameMapToMap] start code=" + i + ",url=" + str);
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "HttpFailFileNameMap";
            infoUnit.mCode = i;
            LogUtil.i(TAG, "TaskHandleOp [addHttpFailFileNameMapToMap] header=" + headers.toString());
            infoUnit.mHeader = headers;
            infoUnit.mUrl = str;
            add(infoUnit);
        }
    }

    public void addTaskFileEnterDownloadProcessCoreCount() {
        LogUtil.i(TAG, "TaskHandleOp [addTaskFileEnterDownloadProcessCoreCount] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "TaskFileEnterDownloadProcessCoreCount";
            add(infoUnit);
        }
    }

    public void addTaskFileEnterDownloadProcessCoreSuccessCount() {
        LogUtil.i(TAG, "TaskHandleOp [addTaskFileEnterDownloadProcessCoreSuccessCount] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "TaskFileEnterDownloadProcessCoreSuccessCount";
            add(infoUnit);
        }
    }

    public void addTaskFileEnterDownloadProcessCoreFailCount() {
        LogUtil.i(TAG, "TaskHandleOp [addTaskFileEnterDownloadProcessCoreFailCount] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "TaskFileEnterDownloadProcessCoreFailCount";
            add(infoUnit);
        }
    }

    public void addTaskFileEnterDownloadProcessCoreVerifySuccessCount() {
        LogUtil.i(TAG, "TaskHandleOp [addTaskFileEnterDownloadProcessCoreVerifySuccessCount] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "TaskFileEnterDownloadProcessCoreVerifySuccessCount";
            add(infoUnit);
        }
    }

    public void addTaskFileEnterDownloadProcessCoreVerifyFailCount() {
        LogUtil.i(TAG, "TaskHandleOp [addTaskFileEnterDownloadProcessCoreVerifyFailCount] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "TaskFileEnterDownloadProcessCoreVerifyFailCount";
            add(infoUnit);
        }
    }

    public void addTaskFileSuccessCount() {
        LogUtil.i(TAG, "TaskHandleOp [addTaskFileSuccessCount] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "TaskFileSuccessCount";
            add(infoUnit);
        }
    }

    public synchronized void addTaskFileFailCount() {
        LogUtil.i(TAG, "TaskHandleOp [addTaskFileFailCount] start");
        this.mTaskHandle.mTaskFileFailCount++;
    }

    public void addTaskFileHasSuccessCount() {
        LogUtil.i(TAG, "TaskHandleOp [addTaskFileFailCount] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "TaskFileHasSuccessCount";
            add(infoUnit);
        }
    }

    public synchronized void addTaskFailCodeMap(int i) {
        LogUtil.i(TAG, "TaskHandleOp [addHttpFailCountToMap] start");
        if (this.mTaskHandle.mTaskFailCodeMap.containsKey(Integer.valueOf(i))) {
            this.mTaskHandle.mTaskFailCodeMap.put(Integer.valueOf(i), Long.valueOf(this.mTaskHandle.mTaskFailCodeMap.get(Integer.valueOf(i)).longValue() + 1));
        } else {
            this.mTaskHandle.mTaskFailCodeMap.put(Integer.valueOf(i), 1L);
        }
    }

    public synchronized void addTaskFailCancelCodeCount() {
        this.mTaskHandle.mTaskFailCancelCodeCount++;
    }

    public synchronized void addTaskFailCodeCountMap(int i, long j) {
        LogUtil.i(TAG, "TaskHandleOp [addHttpFailCountToMap] start");
        this.mTaskHandle.mTaskFailCodeMap.put(Integer.valueOf(i), Long.valueOf(j));
    }

    public synchronized void addTaskFailFileInfoMap(int i, String str, long j, long j2, String str2) {
        JSONArray jSONArray;
        LogUtil.i(TAG, "TaskHandleOp [addHttpFailFileNameMapToMap] start");
        LogUtil.i(TAG, "TaskHandleOp [addHttpFailFileNameMapToMap] code=" + i + ", url=" + str + ", first=" + j + ", last=" + j2 + ",md5=" + j2);
        if (i == 12) {
            return;
        }
        if (i != 0) {
            String str3 = str + "#" + j + "-" + j2 + "#" + str2;
            LogUtil.i(TAG, "TaskHandleOp [start] [mTaskFailFileInfoMap] finalUrlInfo=" + str3);
            if (this.mTaskHandle.mTaskFailFileInfoMap.containsKey(Integer.valueOf(i))) {
                jSONArray = this.mTaskHandle.mTaskFailFileInfoMap.get(Integer.valueOf(i));
            } else {
                jSONArray = new JSONArray();
            }
            if (jSONArray.length() < 10) {
                jSONArray.put(str3);
            }
            this.mTaskHandle.mTaskFailFileInfoMap.put(Integer.valueOf(i), jSONArray);
        }
    }

    public void addTaskFileEnterDownloadProcessCoreMergeFileCount() {
        LogUtil.i(TAG, "TaskHandleOp [addTaskFileEnterDownloadProcessCoreMergeFileCount] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "TaskFileEnterDownloadProcessCoreMergeFileCount";
            add(infoUnit);
        }
    }

    public void addTaskFileEnterDownloadProcessCoreMergeFileVerifySuccessCount() {
        LogUtil.i(TAG, "TaskHandleOp [addTaskFileEnterDownloadProcessCoreMergeFileVerifySuccessCount] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "TaskFileEnterDownloadProcessCoreMergeFileVerifySuccessCount";
            add(infoUnit);
        }
    }

    public void addTaskFileEnterDownloadProcessCoreMergeFileVerifyFailCount() {
        LogUtil.i(TAG, "TaskHandleOp [addTaskFileEnterDownloadProcessCoreMergeFileVerifyFailCount] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "TaskFileEnterDownloadProcessCoreMergeFileVerifyFailCount";
            add(infoUnit);
        }
    }

    public void addTaskFileEnterDownloadProcessCoreMergeFileFailCount() {
        LogUtil.i(TAG, "TaskHandleOp [addTaskFileEnterDownloadProcessCoreMergeFileFailCount] start");
        if (this.mQueue != null) {
            new InfoUnit().mObject = "TaskFileEnterDownloadProcessCoreMergeFileFailCount";
        }
    }

    public void addChannelDownloadCostTimeMap(Headers headers, long j) {
        LogUtil.i(TAG, "TaskHandleOp [addChannelDownloadCostTimeMap] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "ChannelDownloadCostTimeMap";
            infoUnit.mHeader = headers;
            infoUnit.mTime = j;
            add(infoUnit);
        }
    }

    public void addTaskHasDownloadVerifySizes(long j) {
        LogUtil.i(TAG, "TaskHandleOp [addTaskHasDownloadVerifySizes] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "TaskHasDownloadVerifySizes";
            infoUnit.mSize = j;
            add(infoUnit);
        }
    }

    public void addTaskCurTimeDownloadSizes(long j) {
        LogUtil.i(TAG, "TaskHandleOp [addTaskCurTimeDownloadSizes] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "TaskCurTimeDownloadSizes";
            infoUnit.mSize = j;
            add(infoUnit);
        }
    }

    public void addTaskDownloadFileVerifySizes(long j) {
        LogUtil.i(TAG, "TaskHandleOp [addTaskDownloadFileVerifySizes] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "TaskDownloadFileVerifySizes";
            infoUnit.mSize = j;
            add(infoUnit);
        }
    }

    public void addTaskDownloadMergeFileVerifySizes(long j) {
        LogUtil.i(TAG, "TaskHandleOp [addTaskDownloadMergeFileVerifySizes] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "TaskDownloadMergeFileVerifySizes";
            infoUnit.mSize = j;
            add(infoUnit);
        }
    }

    public void addChannelDnsCostTimeMap(String str, long j) {
        LogUtil.i(TAG, "TaskHandleOp [addChannelDnsCostTimeMap] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "ChannelDnsCostTimeMap";
            infoUnit.mUrl = str;
            infoUnit.mTime = j;
            add(infoUnit);
        }
    }

    public void addChannelDnsIpMap(String str, String str2) {
        LogUtil.i(TAG, "TaskHandleOp [addChannelDnsIpMap] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "ChannelDnsIpMap";
            infoUnit.mUrl = str;
            infoUnit.mIp = str2;
            add(infoUnit);
        }
    }

    public void addChannelHttpdnsIpMap(String str, String str2) {
        LogUtil.i(TAG, "TaskHandleOp [addChannelHttpdnsIpMap] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "ChannelHttpdnsIpMap";
            infoUnit.mUrl = str;
            infoUnit.mIp = str2;
            add(infoUnit);
        }
    }

    public void addChannleSpeedMap(String str, long j, long j2) {
        LogUtil.i(TAG, "TaskHandleOp [addChannleSpeedMap] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "ChannelSpeedMap";
            infoUnit.mUrl = str;
            infoUnit.mSize = j;
            infoUnit.mTime = j2;
            add(infoUnit);
        }
    }

    public void addRemoveIpsMap(String str, String str2) {
        LogUtil.i(TAG, "TaskHandleOp [addRemoveIpsMap] start");
        if (this.mQueue != null) {
            InfoUnit infoUnit = new InfoUnit();
            infoUnit.mObject = "RemoveIpsMap";
            infoUnit.mUrl = str;
            infoUnit.mIp = str2;
            add(infoUnit);
        }
    }

    public void start() {
        LogUtil.i(TAG, "TaskHandleOp [start] start");
        if (this.mIsStart) {
            return;
        }
        this.mIsStart = true;
        new Thread(new Runnable() { // from class: com.netease.download.downloader.TaskHandleOp.1
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() throws JSONException {
                JSONArray jSONArray;
                JSONArray jSONArray2;
                JSONArray jSONArray3;
                JSONArray jSONArray4;
                JSONArray jSONArray5;
                LogUtil.i(TaskHandleOp.TAG, "TaskHandleOp [start] thread start");
                while (true) {
                    try {
                        InfoUnit infoUnit = (InfoUnit) TaskHandleOp.this.mQueue.take();
                        if (infoUnit == null) {
                            return;
                        }
                        String str = infoUnit.mObject;
                        LogUtil.i(TaskHandleOp.TAG, "TaskHandleOp [start] object=" + str);
                        if ("HttpCount".equals(str)) {
                            TaskHandleOp.this.mTaskHandle.mHttpCount++;
                        } else if ("HttpSuccessCount".equals(str)) {
                            TaskHandleOp.this.mTaskHandle.mHttpSuccessCount++;
                        } else if ("HttpFailCount".equals(str)) {
                            TaskHandleOp.this.mTaskHandle.mHttpFailCount++;
                        } else if ("HttpFailMap".equals(str)) {
                            int i = infoUnit.mCode;
                            if (TaskHandleOp.this.mTaskHandle.mHttpFailMap.containsKey(Integer.valueOf(i))) {
                                TaskHandleOp.this.mTaskHandle.mHttpFailMap.put(Integer.valueOf(i), Long.valueOf(TaskHandleOp.this.mTaskHandle.mHttpFailMap.get(Integer.valueOf(i)).longValue() + 1));
                            } else {
                                TaskHandleOp.this.mTaskHandle.mHttpFailMap.put(Integer.valueOf(i), 1L);
                            }
                        } else if ("HttpFailFileNameMap".equals(str)) {
                            int i2 = infoUnit.mCode;
                            Headers headers = infoUnit.mHeader;
                            String str2 = headers.get("host");
                            String strReplace = headers.get("range");
                            if (!TextUtils.isEmpty(strReplace) && strReplace.contains("bytes=")) {
                                strReplace = strReplace.replace("bytes=", "");
                            }
                            String string = infoUnit.mUrl;
                            LogUtil.i(TaskHandleOp.TAG, "TaskHandleOp [start] headers=" + headers.toString());
                            LogUtil.i(TaskHandleOp.TAG, "TaskHandleOp [start] host=" + str2 + ", range=" + strReplace + ", urlInfo=" + string);
                            if (!TextUtils.isEmpty(str2)) {
                                String domainFromUrl = Util.getDomainFromUrl(string);
                                String strReplaceDomainWithIpAddr = Util.replaceDomainWithIpAddr(string, str2, "/");
                                LogUtil.i(TaskHandleOp.TAG, "TaskHandleOp [start] ip=" + domainFromUrl + ", range=" + strReplace + ", urlInfo=" + strReplaceDomainWithIpAddr);
                                StringBuilder sb = new StringBuilder();
                                sb.append(strReplaceDomainWithIpAddr);
                                sb.append("#");
                                sb.append(strReplace);
                                sb.append("#");
                                sb.append(domainFromUrl);
                                string = sb.toString();
                            }
                            LogUtil.i(TaskHandleOp.TAG, "TaskHandleOp [start] [mHttpFailFileNameMap] finalUrlInfo=" + string);
                            if (TaskHandleOp.this.mTaskHandle.mHttpFailFileNameMap.containsKey(Integer.valueOf(i2))) {
                                jSONArray = TaskHandleOp.this.mTaskHandle.mHttpFailFileNameMap.get(Integer.valueOf(i2));
                            } else {
                                jSONArray = new JSONArray();
                            }
                            if (jSONArray.length() < 10) {
                                jSONArray.put(string);
                            }
                            TaskHandleOp.this.mTaskHandle.mHttpFailFileNameMap.put(Integer.valueOf(i2), jSONArray);
                        } else {
                            boolean z = true;
                            if ("TaskFileEnterDownloadProcessCoreCount".equals(str)) {
                                TaskHandleOp.this.mTaskHandle.mTaskFileEnterDownloadProcessCoreCount++;
                            } else if ("TaskFileEnterDownloadProcessCoreSuccessCount".equals(str)) {
                                TaskHandleOp.this.mTaskHandle.mTaskFileEnterDownloadProcessCoreSuccessCount++;
                            } else if ("TaskFileEnterDownloadProcessCoreFailCount".equals(str)) {
                                TaskHandleOp.this.mTaskHandle.mTaskFileEnterDownloadProcessCoreFailCount++;
                            } else if ("TaskFileEnterDownloadProcessCoreVerifySuccessCount".equals(str)) {
                                TaskHandleOp.this.mTaskHandle.mTaskFileEnterDownloadProcessCoreVerifySuccessCount++;
                            } else if ("TaskFileEnterDownloadProcessCoreVerifyFailCount".equals(str)) {
                                TaskHandleOp.this.mTaskHandle.mTaskFileEnterDownloadProcessCoreVerifyFailCount++;
                            } else if ("TaskFileSuccessCount".equals(str)) {
                                TaskHandleOp.this.mTaskHandle.mTaskFileSuccessCount++;
                            } else if ("TaskFileFailCount".equals(str)) {
                                TaskHandleOp.this.mTaskHandle.mTaskFileFailCount++;
                            } else if ("TaskFileHasSuccessCount".equals(str)) {
                                TaskHandleOp.this.mTaskHandle.mTaskFileHasSuccessCount++;
                            } else if (!"TaskFailCodeMap".equals(str)) {
                                if ("TaskFailCancelCodeCount".equals(str)) {
                                    TaskHandleOp.this.mTaskHandle.mTaskFailCancelCodeCount++;
                                } else if (!"TaskFailCodeCountMap".equals(str) && !"TaskFailFileInfoMap".equals(str)) {
                                    if ("TaskFileEnterDownloadProcessCoreMergeFileCount".equals(str)) {
                                        TaskHandleOp.this.mTaskHandle.mTaskFileEnterDownloadProcessCoreMergeFileCount++;
                                    } else if ("TaskFileEnterDownloadProcessCoreMergeFileVerifySuccessCount".equals(str)) {
                                        TaskHandleOp.this.mTaskHandle.mTaskFileEnterDownloadProcessCoreMergeFileVerifySuccessCount++;
                                    } else if ("TaskFileEnterDownloadProcessCoreMergeFileVerifyFailCount".equals(str)) {
                                        TaskHandleOp.this.mTaskHandle.mTaskFileEnterDownloadProcessCoreMergeFileVerifyFailCount++;
                                    } else if (!"TaskFileEnterDownloadProcessCoreMergeFileFailCount".equals(str)) {
                                        if ("ChannelDownloadCostTimeMap".equals(str)) {
                                            Headers headers2 = infoUnit.mHeader;
                                            long j = infoUnit.mTime;
                                            String str3 = headers2.get("host");
                                            if (TaskHandleOp.this.mTaskHandle.mChannelDownloadCostTimeMap.containsKey(str3)) {
                                                TaskHandleOp.this.mTaskHandle.mChannelDownloadCostTimeMap.put(str3, Long.valueOf(TaskHandleOp.this.mTaskHandle.mChannelDownloadCostTimeMap.get(str3).longValue() + j));
                                            } else {
                                                TaskHandleOp.this.mTaskHandle.mChannelDownloadCostTimeMap.put(str3, Long.valueOf(j));
                                            }
                                        } else if ("TaskHasDownloadVerifySizes".equals(str)) {
                                            TaskHandleOp.this.mTaskHandle.mTaskHasDownloadVerifySizes += infoUnit.mSize;
                                        } else if ("TaskCurTimeDownloadSizes".equals(str)) {
                                            TaskHandleOp.this.mTaskHandle.mTaskCurTimeDownloadSizes += infoUnit.mSize;
                                        } else if ("TaskDownloadFileVerifySizes".equals(str)) {
                                            TaskHandleOp.this.mTaskHandle.mTaskDownloadFileVerifySizes += infoUnit.mSize;
                                        } else if ("TaskDownloadMergeFileVerifySizes".equals(str)) {
                                            TaskHandleOp.this.mTaskHandle.mTaskDownloadMergeFileVerifySizes += infoUnit.mSize;
                                        } else if ("ChannelDnsCostTimeMap".equals(str)) {
                                            TaskHandleOp.this.mTaskHandle.mChannelDnsCostTimeMap.put(infoUnit.mUrl, Long.valueOf(infoUnit.mTime));
                                        } else if ("ChannelDnsIpMap".equals(str)) {
                                            String str4 = infoUnit.mUrl;
                                            String str5 = infoUnit.mIp;
                                            if (TaskHandleOp.this.mTaskHandle.mChannelDnsIpMap.containsKey(str4)) {
                                                jSONArray2 = TaskHandleOp.this.mTaskHandle.mChannelDnsIpMap.get(str4);
                                                int i3 = 0;
                                                while (true) {
                                                    if (i3 >= jSONArray2.length()) {
                                                        z = false;
                                                        break;
                                                    } else if (str5.equals(jSONArray2.get(i3))) {
                                                        break;
                                                    } else {
                                                        i3++;
                                                    }
                                                }
                                                if (!z) {
                                                    jSONArray2.put(str5);
                                                }
                                            } else {
                                                jSONArray2 = new JSONArray();
                                                jSONArray2.put(str5);
                                            }
                                            TaskHandleOp.this.mTaskHandle.mChannelDnsIpMap.put(str4, jSONArray2);
                                        } else if ("ChannelHttpdnsIpMap".equals(str)) {
                                            String str6 = infoUnit.mUrl;
                                            String str7 = infoUnit.mIp;
                                            if (TaskHandleOp.this.mTaskHandle.mChannelHttpdnsIpMap.containsKey(str6)) {
                                                jSONArray3 = TaskHandleOp.this.mTaskHandle.mChannelHttpdnsIpMap.get(str6);
                                                int i4 = 0;
                                                while (true) {
                                                    if (i4 >= jSONArray3.length()) {
                                                        z = false;
                                                        break;
                                                    } else if (str7.equals(jSONArray3.get(i4))) {
                                                        break;
                                                    } else {
                                                        i4++;
                                                    }
                                                }
                                                if (!z) {
                                                    jSONArray3.put(str7);
                                                }
                                            } else {
                                                jSONArray3 = new JSONArray();
                                                jSONArray3.put(str7);
                                            }
                                            TaskHandleOp.this.mTaskHandle.mChannelHttpdnsIpMap.put(str6, jSONArray3);
                                        } else if ("ChannelSpeedMap".equals(str)) {
                                            long j2 = infoUnit.mSize;
                                            long j3 = 1 + j2;
                                            if (j3 > PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE_ENABLED) {
                                                String str8 = infoUnit.mUrl;
                                                long j4 = infoUnit.mTime;
                                                StringBuilder sb2 = new StringBuilder();
                                                sb2.append("TaskHandleOp [start] [ChannelSpeedMap] size=");
                                                sb2.append(j2);
                                                sb2.append(", time=");
                                                sb2.append(j4);
                                                sb2.append(", size/time=");
                                                double d = ((j3 * 1.0d) / 1024.0d) / ((j4 * 1.0d) / 1000.0d);
                                                sb2.append(d);
                                                LogUtil.i(TaskHandleOp.TAG, sb2.toString());
                                                if (TaskHandleOp.this.mTaskHandle.mChannelSpeedMap.containsKey(str8)) {
                                                    jSONArray4 = TaskHandleOp.this.mTaskHandle.mChannelSpeedMap.get(str8);
                                                    jSONArray4.put(d);
                                                } else {
                                                    jSONArray4 = new JSONArray();
                                                    jSONArray4.put(d);
                                                }
                                                TaskHandleOp.this.mTaskHandle.mChannelSpeedMap.put(str8, jSONArray4);
                                            }
                                        } else if ("RemoveIpsMap".equals(str)) {
                                            String str9 = infoUnit.mUrl;
                                            String str10 = infoUnit.mIp;
                                            if (TaskHandleOp.this.mTaskHandle.mRemoveIpsMap.containsKey(str9)) {
                                                jSONArray5 = TaskHandleOp.this.mTaskHandle.mRemoveIpsMap.get(str9);
                                                int i5 = 0;
                                                while (true) {
                                                    if (i5 >= jSONArray5.length()) {
                                                        z = false;
                                                        break;
                                                    } else if (str10.equals(jSONArray5.get(i5))) {
                                                        break;
                                                    } else {
                                                        i5++;
                                                    }
                                                }
                                                if (!z) {
                                                    jSONArray5.put(str10);
                                                }
                                            } else {
                                                jSONArray5 = new JSONArray();
                                                jSONArray5.put(str10);
                                            }
                                            TaskHandleOp.this.mTaskHandle.mRemoveIpsMap.put(str9, jSONArray5);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        LogUtil.i(TaskHandleOp.TAG, "TaskHandleOp [start] Exception=" + e.toString());
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }).start();
    }

    /* renamed from: com.netease.download.downloader.TaskHandleOp$1 */
    class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() throws JSONException {
            JSONArray jSONArray;
            JSONArray jSONArray2;
            JSONArray jSONArray3;
            JSONArray jSONArray4;
            JSONArray jSONArray5;
            LogUtil.i(TaskHandleOp.TAG, "TaskHandleOp [start] thread start");
            while (true) {
                try {
                    InfoUnit infoUnit = (InfoUnit) TaskHandleOp.this.mQueue.take();
                    if (infoUnit == null) {
                        return;
                    }
                    String str = infoUnit.mObject;
                    LogUtil.i(TaskHandleOp.TAG, "TaskHandleOp [start] object=" + str);
                    if ("HttpCount".equals(str)) {
                        TaskHandleOp.this.mTaskHandle.mHttpCount++;
                    } else if ("HttpSuccessCount".equals(str)) {
                        TaskHandleOp.this.mTaskHandle.mHttpSuccessCount++;
                    } else if ("HttpFailCount".equals(str)) {
                        TaskHandleOp.this.mTaskHandle.mHttpFailCount++;
                    } else if ("HttpFailMap".equals(str)) {
                        int i = infoUnit.mCode;
                        if (TaskHandleOp.this.mTaskHandle.mHttpFailMap.containsKey(Integer.valueOf(i))) {
                            TaskHandleOp.this.mTaskHandle.mHttpFailMap.put(Integer.valueOf(i), Long.valueOf(TaskHandleOp.this.mTaskHandle.mHttpFailMap.get(Integer.valueOf(i)).longValue() + 1));
                        } else {
                            TaskHandleOp.this.mTaskHandle.mHttpFailMap.put(Integer.valueOf(i), 1L);
                        }
                    } else if ("HttpFailFileNameMap".equals(str)) {
                        int i2 = infoUnit.mCode;
                        Headers headers = infoUnit.mHeader;
                        String str2 = headers.get("host");
                        String strReplace = headers.get("range");
                        if (!TextUtils.isEmpty(strReplace) && strReplace.contains("bytes=")) {
                            strReplace = strReplace.replace("bytes=", "");
                        }
                        String string = infoUnit.mUrl;
                        LogUtil.i(TaskHandleOp.TAG, "TaskHandleOp [start] headers=" + headers.toString());
                        LogUtil.i(TaskHandleOp.TAG, "TaskHandleOp [start] host=" + str2 + ", range=" + strReplace + ", urlInfo=" + string);
                        if (!TextUtils.isEmpty(str2)) {
                            String domainFromUrl = Util.getDomainFromUrl(string);
                            String strReplaceDomainWithIpAddr = Util.replaceDomainWithIpAddr(string, str2, "/");
                            LogUtil.i(TaskHandleOp.TAG, "TaskHandleOp [start] ip=" + domainFromUrl + ", range=" + strReplace + ", urlInfo=" + strReplaceDomainWithIpAddr);
                            StringBuilder sb = new StringBuilder();
                            sb.append(strReplaceDomainWithIpAddr);
                            sb.append("#");
                            sb.append(strReplace);
                            sb.append("#");
                            sb.append(domainFromUrl);
                            string = sb.toString();
                        }
                        LogUtil.i(TaskHandleOp.TAG, "TaskHandleOp [start] [mHttpFailFileNameMap] finalUrlInfo=" + string);
                        if (TaskHandleOp.this.mTaskHandle.mHttpFailFileNameMap.containsKey(Integer.valueOf(i2))) {
                            jSONArray = TaskHandleOp.this.mTaskHandle.mHttpFailFileNameMap.get(Integer.valueOf(i2));
                        } else {
                            jSONArray = new JSONArray();
                        }
                        if (jSONArray.length() < 10) {
                            jSONArray.put(string);
                        }
                        TaskHandleOp.this.mTaskHandle.mHttpFailFileNameMap.put(Integer.valueOf(i2), jSONArray);
                    } else {
                        boolean z = true;
                        if ("TaskFileEnterDownloadProcessCoreCount".equals(str)) {
                            TaskHandleOp.this.mTaskHandle.mTaskFileEnterDownloadProcessCoreCount++;
                        } else if ("TaskFileEnterDownloadProcessCoreSuccessCount".equals(str)) {
                            TaskHandleOp.this.mTaskHandle.mTaskFileEnterDownloadProcessCoreSuccessCount++;
                        } else if ("TaskFileEnterDownloadProcessCoreFailCount".equals(str)) {
                            TaskHandleOp.this.mTaskHandle.mTaskFileEnterDownloadProcessCoreFailCount++;
                        } else if ("TaskFileEnterDownloadProcessCoreVerifySuccessCount".equals(str)) {
                            TaskHandleOp.this.mTaskHandle.mTaskFileEnterDownloadProcessCoreVerifySuccessCount++;
                        } else if ("TaskFileEnterDownloadProcessCoreVerifyFailCount".equals(str)) {
                            TaskHandleOp.this.mTaskHandle.mTaskFileEnterDownloadProcessCoreVerifyFailCount++;
                        } else if ("TaskFileSuccessCount".equals(str)) {
                            TaskHandleOp.this.mTaskHandle.mTaskFileSuccessCount++;
                        } else if ("TaskFileFailCount".equals(str)) {
                            TaskHandleOp.this.mTaskHandle.mTaskFileFailCount++;
                        } else if ("TaskFileHasSuccessCount".equals(str)) {
                            TaskHandleOp.this.mTaskHandle.mTaskFileHasSuccessCount++;
                        } else if (!"TaskFailCodeMap".equals(str)) {
                            if ("TaskFailCancelCodeCount".equals(str)) {
                                TaskHandleOp.this.mTaskHandle.mTaskFailCancelCodeCount++;
                            } else if (!"TaskFailCodeCountMap".equals(str) && !"TaskFailFileInfoMap".equals(str)) {
                                if ("TaskFileEnterDownloadProcessCoreMergeFileCount".equals(str)) {
                                    TaskHandleOp.this.mTaskHandle.mTaskFileEnterDownloadProcessCoreMergeFileCount++;
                                } else if ("TaskFileEnterDownloadProcessCoreMergeFileVerifySuccessCount".equals(str)) {
                                    TaskHandleOp.this.mTaskHandle.mTaskFileEnterDownloadProcessCoreMergeFileVerifySuccessCount++;
                                } else if ("TaskFileEnterDownloadProcessCoreMergeFileVerifyFailCount".equals(str)) {
                                    TaskHandleOp.this.mTaskHandle.mTaskFileEnterDownloadProcessCoreMergeFileVerifyFailCount++;
                                } else if (!"TaskFileEnterDownloadProcessCoreMergeFileFailCount".equals(str)) {
                                    if ("ChannelDownloadCostTimeMap".equals(str)) {
                                        Headers headers2 = infoUnit.mHeader;
                                        long j = infoUnit.mTime;
                                        String str3 = headers2.get("host");
                                        if (TaskHandleOp.this.mTaskHandle.mChannelDownloadCostTimeMap.containsKey(str3)) {
                                            TaskHandleOp.this.mTaskHandle.mChannelDownloadCostTimeMap.put(str3, Long.valueOf(TaskHandleOp.this.mTaskHandle.mChannelDownloadCostTimeMap.get(str3).longValue() + j));
                                        } else {
                                            TaskHandleOp.this.mTaskHandle.mChannelDownloadCostTimeMap.put(str3, Long.valueOf(j));
                                        }
                                    } else if ("TaskHasDownloadVerifySizes".equals(str)) {
                                        TaskHandleOp.this.mTaskHandle.mTaskHasDownloadVerifySizes += infoUnit.mSize;
                                    } else if ("TaskCurTimeDownloadSizes".equals(str)) {
                                        TaskHandleOp.this.mTaskHandle.mTaskCurTimeDownloadSizes += infoUnit.mSize;
                                    } else if ("TaskDownloadFileVerifySizes".equals(str)) {
                                        TaskHandleOp.this.mTaskHandle.mTaskDownloadFileVerifySizes += infoUnit.mSize;
                                    } else if ("TaskDownloadMergeFileVerifySizes".equals(str)) {
                                        TaskHandleOp.this.mTaskHandle.mTaskDownloadMergeFileVerifySizes += infoUnit.mSize;
                                    } else if ("ChannelDnsCostTimeMap".equals(str)) {
                                        TaskHandleOp.this.mTaskHandle.mChannelDnsCostTimeMap.put(infoUnit.mUrl, Long.valueOf(infoUnit.mTime));
                                    } else if ("ChannelDnsIpMap".equals(str)) {
                                        String str4 = infoUnit.mUrl;
                                        String str5 = infoUnit.mIp;
                                        if (TaskHandleOp.this.mTaskHandle.mChannelDnsIpMap.containsKey(str4)) {
                                            jSONArray2 = TaskHandleOp.this.mTaskHandle.mChannelDnsIpMap.get(str4);
                                            int i3 = 0;
                                            while (true) {
                                                if (i3 >= jSONArray2.length()) {
                                                    z = false;
                                                    break;
                                                } else if (str5.equals(jSONArray2.get(i3))) {
                                                    break;
                                                } else {
                                                    i3++;
                                                }
                                            }
                                            if (!z) {
                                                jSONArray2.put(str5);
                                            }
                                        } else {
                                            jSONArray2 = new JSONArray();
                                            jSONArray2.put(str5);
                                        }
                                        TaskHandleOp.this.mTaskHandle.mChannelDnsIpMap.put(str4, jSONArray2);
                                    } else if ("ChannelHttpdnsIpMap".equals(str)) {
                                        String str6 = infoUnit.mUrl;
                                        String str7 = infoUnit.mIp;
                                        if (TaskHandleOp.this.mTaskHandle.mChannelHttpdnsIpMap.containsKey(str6)) {
                                            jSONArray3 = TaskHandleOp.this.mTaskHandle.mChannelHttpdnsIpMap.get(str6);
                                            int i4 = 0;
                                            while (true) {
                                                if (i4 >= jSONArray3.length()) {
                                                    z = false;
                                                    break;
                                                } else if (str7.equals(jSONArray3.get(i4))) {
                                                    break;
                                                } else {
                                                    i4++;
                                                }
                                            }
                                            if (!z) {
                                                jSONArray3.put(str7);
                                            }
                                        } else {
                                            jSONArray3 = new JSONArray();
                                            jSONArray3.put(str7);
                                        }
                                        TaskHandleOp.this.mTaskHandle.mChannelHttpdnsIpMap.put(str6, jSONArray3);
                                    } else if ("ChannelSpeedMap".equals(str)) {
                                        long j2 = infoUnit.mSize;
                                        long j3 = 1 + j2;
                                        if (j3 > PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE_ENABLED) {
                                            String str8 = infoUnit.mUrl;
                                            long j4 = infoUnit.mTime;
                                            StringBuilder sb2 = new StringBuilder();
                                            sb2.append("TaskHandleOp [start] [ChannelSpeedMap] size=");
                                            sb2.append(j2);
                                            sb2.append(", time=");
                                            sb2.append(j4);
                                            sb2.append(", size/time=");
                                            double d = ((j3 * 1.0d) / 1024.0d) / ((j4 * 1.0d) / 1000.0d);
                                            sb2.append(d);
                                            LogUtil.i(TaskHandleOp.TAG, sb2.toString());
                                            if (TaskHandleOp.this.mTaskHandle.mChannelSpeedMap.containsKey(str8)) {
                                                jSONArray4 = TaskHandleOp.this.mTaskHandle.mChannelSpeedMap.get(str8);
                                                jSONArray4.put(d);
                                            } else {
                                                jSONArray4 = new JSONArray();
                                                jSONArray4.put(d);
                                            }
                                            TaskHandleOp.this.mTaskHandle.mChannelSpeedMap.put(str8, jSONArray4);
                                        }
                                    } else if ("RemoveIpsMap".equals(str)) {
                                        String str9 = infoUnit.mUrl;
                                        String str10 = infoUnit.mIp;
                                        if (TaskHandleOp.this.mTaskHandle.mRemoveIpsMap.containsKey(str9)) {
                                            jSONArray5 = TaskHandleOp.this.mTaskHandle.mRemoveIpsMap.get(str9);
                                            int i5 = 0;
                                            while (true) {
                                                if (i5 >= jSONArray5.length()) {
                                                    z = false;
                                                    break;
                                                } else if (str10.equals(jSONArray5.get(i5))) {
                                                    break;
                                                } else {
                                                    i5++;
                                                }
                                            }
                                            if (!z) {
                                                jSONArray5.put(str10);
                                            }
                                        } else {
                                            jSONArray5 = new JSONArray();
                                            jSONArray5.put(str10);
                                        }
                                        TaskHandleOp.this.mTaskHandle.mRemoveIpsMap.put(str9, jSONArray5);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    LogUtil.i(TaskHandleOp.TAG, "TaskHandleOp [start] Exception=" + e.toString());
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public TaskHandle getTaskHandle() {
        if (this.mTaskHandle == null) {
            this.mTaskHandle = new TaskHandle();
        }
        return this.mTaskHandle;
    }

    public void showTime() {
        this.mTaskHandle.showTime();
    }

    public void showParams() {
        this.mTaskHandle.showParams();
    }

    public void showInitInfo() {
        this.mTaskHandle.showInitInfo();
    }

    class InfoUnit {
        public int mCode;
        public String mCommonKey;
        public String mCommonValue;
        public long mCount;
        public long mFirst;
        public Headers mHeader;
        public String mIp;
        public long mLast;
        public String mMd5;
        public String mObject = null;
        public long mSize;
        public long mTime;
        public String mUrl;

        InfoUnit() {
        }
    }
}