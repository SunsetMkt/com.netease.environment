package com.netease.ntunisdk.external.protocol.data;

import android.text.TextUtils;
import com.netease.ntunisdk.external.protocol.utils.L;
import com.netease.ntunisdk.external.protocol.utils.TextCompat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ProtocolInfo {
    public int acceptStatus;
    public String displayName;
    public GlobalInfo globalInfo;
    public int id;
    public boolean isHtml;
    public boolean isMinorChange;
    public String key;
    private ProtocolFile mProtocolFile;
    public int prevMajorChangeId;
    public String reviewText;
    public String reviewTextUrl;
    public String text;
    public String textHash;
    public String textUrl;
    public int uiStyle;
    public String updateText;
    public String updateTextHash;
    public String updateTextUrl;
    public String url;
    public boolean isLocal = false;
    public int status = 0;
    public int textAlign = 1;
    public int version = 0;
    public List<ProtocolInfo> subProtocol = new ArrayList();
    public CopyOnWriteArrayList<String> subProtocolUrls = new CopyOnWriteArrayList<>();
    public final ConcurrentHashMap<String, SubProtocolInfo> mSubProtocolInfos = new ConcurrentHashMap<>();
    public HashSet<String> addParamsHost = new HashSet<>();

    public ProtocolInfo(int i, String str) {
        boolean z = false;
        this.isHtml = false;
        this.id = i;
        this.url = str;
        this.key = TextCompat.md5(str);
        if (i == -101 && !TextUtils.isEmpty(str)) {
            z = true;
        }
        this.isHtml = z;
    }

    public static boolean isEmpty(ProtocolInfo protocolInfo) {
        return protocolInfo == null || TextUtils.isEmpty(protocolInfo.text);
    }

    public static boolean hasUpdate(ProtocolInfo protocolInfo) {
        return !isEmpty(protocolInfo) && protocolInfo.status >= 2;
    }

    public boolean isSubProtocolEmpty() {
        ConcurrentHashMap<String, SubProtocolInfo> concurrentHashMap = this.mSubProtocolInfos;
        return concurrentHashMap == null || concurrentHashMap.isEmpty();
    }

    public SubProtocolInfo getSubProtocol(String str) {
        ConcurrentHashMap<String, SubProtocolInfo> concurrentHashMap = this.mSubProtocolInfos;
        if (concurrentHashMap == null) {
            return null;
        }
        return concurrentHashMap.get(str);
    }

    public ProtocolInfo findProtocolByUrl(String str) {
        if (this.subProtocol == null) {
            return null;
        }
        ProtocolInfo protocol = this.subProtocolUrls.contains(str) ? SDKRuntime.getInstance().getProtocol(str) : null;
        if (protocol != null) {
            return protocol;
        }
        Iterator<ProtocolInfo> it = this.subProtocol.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ProtocolInfo next = it.next();
            if (str.equals(next.url)) {
                protocol = next;
                break;
            }
        }
        if (protocol == null) {
            protocol = ProtocolParser.readLocalProtocol(this.mProtocolFile, str);
        }
        if (protocol != null) {
            SDKRuntime.getInstance().addProtocolIntoMemory(protocol);
        }
        return protocol;
    }

    public void addSubInfo(String str, SubProtocolInfo subProtocolInfo) {
        this.mSubProtocolInfos.put(str, subProtocolInfo);
    }

    public String toString() {
        return "ProtocolInfo{id=" + this.id + ", version=" + this.version + ", url='" + this.url + "', isMinorChange=" + this.isMinorChange + ", prevMajorChangeId=" + this.prevMajorChangeId + '}';
    }

    public ProtocolFile getProtocolFile() {
        return this.mProtocolFile;
    }

    public void setProtocolFile(ProtocolFile protocolFile) {
        this.mProtocolFile = protocolFile;
    }

    public boolean isHomeStyle() {
        return this.uiStyle == 1;
    }

    public void setUiStyle(int i) {
        this.uiStyle = i;
    }

    public boolean isLocal() {
        return this.isLocal;
    }

    public void setLocal(boolean z) {
        this.isLocal = z;
    }

    public static class SubProtocolInfo {
        private String alias;
        private boolean isLocal;
        private boolean isMinorChange;
        public final String mName;
        private int mPrevMajorChangeId;
        public final String mUrl;
        private int mVersion;
        private int mId = -1;
        private boolean isAccept = true;
        private int mStatus = 0;
        private final HashMap<String, String> mScenes = new HashMap<>();

        public boolean isShowing(int i) {
            return i == 1 || i == 2;
        }

        public SubProtocolInfo(String str, String str2, boolean z) {
            this.isLocal = false;
            this.mUrl = str;
            this.mName = str2;
            this.isLocal = z;
        }

        public boolean isAccept() {
            return this.isAccept;
        }

        public void setAccept(boolean z) {
            this.isAccept = z;
        }

        public boolean isMinorChange() {
            return this.isMinorChange;
        }

        public void setMinorChange(boolean z) {
            this.isMinorChange = z;
        }

        public int getPrevMajorChangeId() {
            return this.mPrevMajorChangeId;
        }

        public void setPrevMajorChangeId(int i) {
            this.mPrevMajorChangeId = i;
        }

        public String getAlias() {
            return this.alias;
        }

        public void setAlias(String str) {
            this.alias = str;
        }

        public int getId() {
            return this.mId;
        }

        public void setId(int i) {
            this.mId = i;
        }

        public int getVersion() {
            return this.mVersion;
        }

        public void setVersion(int i) {
            this.mVersion = i;
        }

        public void addScenes(JSONObject jSONObject) {
            if (jSONObject != null) {
                Iterator<String> itKeys = jSONObject.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    this.mScenes.put(next, jSONObject.optString(next, "0"));
                }
            }
        }

        public int getStatus() {
            return this.mStatus;
        }

        public void setStatus(int i) {
            this.mStatus = i;
        }

        public int getShowStatusByScene(String str) {
            String str2 = this.mScenes.get(str);
            if (str2 == null) {
                return 0;
            }
            try {
                return Integer.parseInt(str2);
            } catch (Exception unused) {
                return 0;
            }
        }

        public boolean isRequiredProtocol(String str) {
            return !TextUtils.isEmpty(str) && getShowStatusByScene(str) == 1;
        }

        public boolean isLocal() {
            return this.isLocal;
        }
    }

    public static class ConcreteSubProtocol {
        private boolean isChecked;
        private boolean isWarning;
        public GlobalInfo mGlobalInfo;
        public String mScene;
        public int mShow;
        public SubProtocolInfo mSubProtocolInfo;
        private boolean isCanAccept = true;
        private boolean hasAsync = false;

        public ConcreteSubProtocol(String str, int i, SubProtocolInfo subProtocolInfo, GlobalInfo globalInfo) {
            this.mScene = str;
            this.mShow = i;
            this.mSubProtocolInfo = subProtocolInfo;
            this.mGlobalInfo = globalInfo;
            this.isChecked = subProtocolInfo.isAccept;
        }

        public boolean isCanAccept() {
            return this.isCanAccept;
        }

        public void setCanAccept(boolean z) {
            this.isCanAccept = z;
        }

        public boolean isRequired() {
            return this.mShow == 1;
        }

        public boolean isChecked() {
            return this.isChecked;
        }

        public void setChecked(boolean z) {
            L.d("setChecked:" + z);
            this.isChecked = z;
            this.mSubProtocolInfo.setAccept(z);
        }

        public boolean isWarning() {
            return this.isWarning;
        }

        public void setWarning(boolean z) {
            this.isWarning = z;
        }

        public String getProtocolNamePrefix() {
            GlobalInfo globalInfo = this.mGlobalInfo;
            if (globalInfo == null) {
                return "";
            }
            int i = this.mShow;
            if (i != 1) {
                return i != 2 ? "" : globalInfo.optional;
            }
            return globalInfo.required;
        }

        public boolean isHasAsync() {
            return this.hasAsync;
        }

        public void setHasAsync(boolean z) {
            this.hasAsync = z;
        }
    }
}