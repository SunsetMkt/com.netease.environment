package com.netease.ntunisdk.external.protocol;

import android.content.Context;
import android.text.TextUtils;
import com.netease.ntunisdk.external.protocol.data.ProtocolProvider;
import com.netease.ntunisdk.external.protocol.utils.L;
import com.netease.ntunisdk.external.protocol.utils.Response;
import com.netease.ntunisdk.external.protocol.utils.TextCompat;
import com.netease.ntunisdk.external.protocol.utils.UrlConnectImpl;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
class SyncClassesTask extends Thread {
    private static final String TAG = "SyncClassesTask";
    private static final int[] bonusPool = {0, 0, 1, 0, 0, 0, 0, 0, 0, 0};
    private boolean directUpload;
    private String mAppKey;
    private final int mAppVersion;
    private String mChannel;
    private Context mContext;
    private long mCurrentTime;
    private String mETag;
    private String mProject;
    private ProtocolProvider mProvider;

    public SyncClassesTask() {
        super(TAG);
        this.mAppVersion = 0;
        this.directUpload = false;
    }

    public SyncClassesTask init(Context context, String str, String str2, String str3) {
        this.mProject = str;
        this.mChannel = str2;
        this.mAppKey = str3;
        this.mContext = context;
        this.mProvider = ProtocolManager.getInstance().getProvider();
        return this;
    }

    public void setDirectly(boolean z) {
        this.directUpload = z;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            if (this.directUpload) {
                L.d(TAG, "update classes directly!");
                uploadClass(filterClasses(download()));
                return;
            }
            L.d(TAG, "update classes!");
            long lastUploadTime = this.mProvider.getLastUploadTime();
            long jCurrentTimeMillis = System.currentTimeMillis() / 86400000;
            this.mCurrentTime = jCurrentTimeMillis;
            if (jCurrentTimeMillis < lastUploadTime + 7) {
                L.d(TAG, "not need upload classes!");
            } else if (lottery()) {
                L.d(TAG, "need upload classes!");
                this.mProvider.getAppVersionCode();
                uploadClass(filterClasses(download()));
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private boolean lottery() {
        int iNextInt = new Random(System.currentTimeMillis()).nextInt(10);
        L.d(TAG, "random:" + iNextInt);
        return bonusPool[iNextInt] == 1;
    }

    private ArrayList<String> download() {
        L.d(TAG, "start download class list");
        HashMap map = new HashMap();
        String eTag = getETag();
        if (!TextUtils.isEmpty(eTag)) {
            if (eTag.contains("\"")) {
                eTag = eTag.replace("\"", "").trim();
            }
            map.put("If-None-Match", eTag);
        }
        Response responseFetch = UrlConnectImpl.fetch(Const.PROTOCOL_CLASS_DOWNLOAD, map);
        this.mETag = responseFetch.getHeaderProperty(Const.ETAG);
        return responseFetch.getContent();
    }

    private String getETag() {
        return this.mProvider.getLocalETag(TextCompat.md5(Const.PROTOCOL_CLASS_UPLOAD));
    }

    private ArrayList<String> filterClasses(ArrayList<String> arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            L.d(TAG, "classes is empty!");
            return arrayList;
        }
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            if (!isClassFound(it.next())) {
                it.remove();
            }
        }
        return arrayList;
    }

    private void uploadClass(ArrayList<String> arrayList) throws Throwable {
        if (arrayList == null || arrayList.isEmpty()) {
            L.d(TAG, "classes is empty!");
            return;
        }
        byte[] bytes = null;
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("project", this.mProject);
            jSONObject.put("app_key", this.mAppKey);
            jSONObject.put("channel_id", this.mChannel);
            JSONArray jSONArray = new JSONArray();
            Iterator<String> it = arrayList.iterator();
            while (it.hasNext()) {
                jSONArray.put(it.next());
            }
            jSONObject.put(Const.CLASS_LIST, jSONArray);
            bytes = jSONObject.toString().getBytes("UTF-8");
        } catch (Exception unused) {
        }
        if (bytes != null) {
            HashMap map = new HashMap();
            map.put("Content-Type", ClientLogConstant.NORMAL_TYPE_VALUE);
            String strPost = UrlConnectImpl.post(Const.PROTOCOL_CLASS_UPLOAD, map, bytes);
            this.mProvider.saveUploadClassTag(0, this.mCurrentTime, TextCompat.md5(Const.PROTOCOL_CLASS_UPLOAD), this.mETag);
            L.d(TAG, strPost);
        }
    }

    private boolean isClassFound(String str) throws ClassNotFoundException {
        try {
            Class.forName(str);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }
}