package com.netease.ntunisdk.external.protocol.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.netease.ntunisdk.external.protocol.ProtocolManager;
import com.netease.ntunisdk.external.protocol.data.ProtocolInfo;
import com.netease.ntunisdk.external.protocol.utils.L;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class Store {
    private static final String CURRENT_PROTOCOL_FLAG = "current_protocol";
    private static final String CURRENT_USER_FLAG = "current_user";
    private static final String PROTOCOL_APP_INSTALL_TIME = "protocol_app_install_time";
    private static final String PROTOCOL_APP_VERSION_CODE = "protocol_app_version";
    private static final String PROTOCOL_E_TAG = "protocol_etag";
    private static final String PROTOCOL_LAUNCH_FLAG = "protocol_launch_accept";
    private static final String PROTOCOL_MODIFY = "protocol_modify#";
    private static final String PROTOCOL_UPDATE_TIME = "protocol_update_time";
    private static final String TAG = "S#";
    private static volatile Store sInstance;
    private SharedPreferences mConfigSp;
    private SharedPreferences mPidUidSp;
    private SharedPreferences mPidVerSp;
    private SharedPreferences mUidPidSp;
    private final boolean hasInit = false;
    private TreeSet<String> mDisagreedAlias = new TreeSet<>();
    private String mDisagreedAliasStr = "";
    private boolean hasDisagreedAliasChanged = false;
    private String mCurrentUid = null;
    private String mCurrentProtocolUrl = null;
    private final ConcurrentHashMap<String, String> mUidPidCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, String> mPidUidCache = new ConcurrentHashMap<>();

    private Store() {
    }

    public static Store getInstance() {
        if (sInstance == null) {
            synchronized (Store.class) {
                if (sInstance == null) {
                    sInstance = new Store();
                }
            }
        }
        return sInstance;
    }

    public synchronized void init(Context context) {
        this.mPidUidSp = context.getSharedPreferences("pid_user", 0);
        this.mUidPidSp = context.getSharedPreferences("uid_pinfos", 0);
        this.mPidVerSp = context.getSharedPreferences("pid_version", 0);
        this.mConfigSp = context.getSharedPreferences("protocol_configs", 0);
    }

    public int getLocalVersion(int i) {
        return this.mPidVerSp.getInt(String.valueOf(i), 0);
    }

    public void updateLocalVersion(int i, int i2) {
        SharedPreferences.Editor editorEdit = this.mPidVerSp.edit();
        editorEdit.putInt(String.valueOf(i), i2);
        editorEdit.apply();
    }

    public boolean hasUserAcceptProtocol(int i) {
        String string = this.mPidUidCache.get(Integer.valueOf(i));
        if (TextUtils.isEmpty(string)) {
            string = this.mPidUidSp.getString(String.valueOf(i), null);
            this.mPidUidCache.put(Integer.valueOf(i), string);
        }
        L.d(TAG, "user[%s] has Accept", string);
        return true ^ TextUtils.isEmpty(string);
    }

    public String getCurrentUid() {
        if (this.mCurrentUid == null) {
            this.mCurrentUid = this.mPidUidSp.getString(CURRENT_USER_FLAG, "");
        }
        return this.mCurrentUid;
    }

    public String getCurrentProtocolUrl() {
        if (this.mCurrentProtocolUrl == null) {
            this.mCurrentProtocolUrl = this.mPidVerSp.getString(CURRENT_PROTOCOL_FLAG, "");
        }
        return this.mCurrentProtocolUrl;
    }

    public void updateCurrentProtocol(ProtocolInfo protocolInfo) {
        if (protocolInfo == null || protocolInfo.isHtml) {
            return;
        }
        SharedPreferences.Editor editorEdit = this.mPidVerSp.edit();
        editorEdit.putString(CURRENT_PROTOCOL_FLAG, protocolInfo.url);
        editorEdit.apply();
    }

    public boolean isUserAcceptProtocol(int i, String str) {
        String string = this.mPidUidCache.get(Integer.valueOf(i));
        if (TextUtils.isEmpty(string)) {
            string = this.mPidUidSp.getString(String.valueOf(i), null);
            this.mPidUidCache.put(Integer.valueOf(i), string);
        }
        if (TextUtils.isEmpty(string)) {
            return false;
        }
        return string.contains(str);
    }

    public TreeSet<String> filterAcceptProtocolByUid(ProtocolInfo protocolInfo, String str) {
        L.d(TAG, "enter filterAcceptProtocolByUid");
        if (TextUtils.isEmpty(str)) {
            return new TreeSet<>();
        }
        TreeSet<String> acceptedProtocolsByUid = getAcceptedProtocolsByUid(str);
        TreeSet<String> treeSet = new TreeSet<>();
        Iterator<String> it = protocolInfo.subProtocolUrls.iterator();
        while (it.hasNext()) {
            ProtocolInfo.SubProtocolInfo subProtocol = protocolInfo.getSubProtocol(it.next());
            if (subProtocol != null && subProtocol.getId() != -1 && !TextUtils.isEmpty(subProtocol.getAlias())) {
                Iterator<String> it2 = acceptedProtocolsByUid.iterator();
                while (it2.hasNext()) {
                    String next = it2.next();
                    if (next.startsWith("" + subProtocol.getId())) {
                        boolean z = !next.endsWith("-0");
                        subProtocol.setAccept(z);
                        if (!z) {
                            treeSet.add(subProtocol.getAlias());
                        }
                    }
                }
            }
        }
        if (!treeSet.isEmpty()) {
            this.mDisagreedAliasStr = TextUtils.join(",", treeSet);
        }
        boolean z2 = !this.mDisagreedAlias.equals(treeSet);
        this.hasDisagreedAliasChanged = z2;
        this.mDisagreedAlias = treeSet;
        L.d(TAG, "last DisagreedAliasStr:%s, hasDisagreedAliasChanged:%s", this.mDisagreedAliasStr, Boolean.valueOf(z2));
        return acceptedProtocolsByUid;
    }

    private synchronized void saveUserAcceptProtocol(int i, String str) {
        String str2;
        L.d(TAG, "saveUserAcceptProtocol--> pid[%s], uid[%s]", Integer.valueOf(i), str);
        if (!TextUtils.isEmpty(str) && !User.isLogoutUser(str)) {
            String string = this.mPidUidCache.get(Integer.valueOf(i));
            if (TextUtils.isEmpty(string)) {
                string = this.mPidUidSp.getString(String.valueOf(i), null);
            }
            L.d(TAG, "All accept [%s]", string);
            if (TextUtils.isEmpty(string)) {
                str2 = str;
            } else {
                if (string.contains(str)) {
                    if (!str.equals(this.mCurrentUid)) {
                        this.mCurrentUid = str;
                        SharedPreferences.Editor editorEdit = this.mPidUidSp.edit();
                        editorEdit.putString(CURRENT_USER_FLAG, this.mCurrentUid);
                        editorEdit.apply();
                    }
                    return;
                }
                str2 = string + "," + str;
            }
            if (!str.equals(this.mCurrentUid)) {
                this.mCurrentUid = str;
            }
            this.mPidUidCache.put(Integer.valueOf(i), str2);
            L.d(TAG, "user[%s] has Accept [%s]", str2, String.valueOf(i));
            SharedPreferences.Editor editorEdit2 = this.mPidUidSp.edit();
            editorEdit2.putString(String.valueOf(i), str2);
            editorEdit2.putString(CURRENT_USER_FLAG, this.mCurrentUid);
            editorEdit2.apply();
        }
    }

    public synchronized String getAcceptedProtocolStrByUid(String str) {
        if (!TextUtils.isEmpty(str) && !User.isLogoutUser(str)) {
            String string = this.mUidPidCache.get(str);
            if (TextUtils.isEmpty(string)) {
                string = this.mUidPidSp.getString(str, null);
            }
            return string;
        }
        return null;
    }

    public synchronized TreeSet<String> getAcceptedProtocolsByUid(String str) {
        L.d(TAG, "enter getAcceptedProtocolsByUid");
        if (TextUtils.isEmpty(str)) {
            return new TreeSet<>();
        }
        String acceptedProtocolStrByUid = getAcceptedProtocolStrByUid(str);
        if (TextUtils.isEmpty(acceptedProtocolStrByUid)) {
            L.d(TAG, "%s not Accepted any Protocol", str);
            return new TreeSet<>();
        }
        L.d(TAG, "getAcceptedProtocolsByUid [%s:%s]", str, acceptedProtocolStrByUid);
        TreeSet<String> treeSet = new TreeSet<>();
        if (acceptedProtocolStrByUid.contains(",")) {
            treeSet.addAll(Arrays.asList(acceptedProtocolStrByUid.split(",")));
        } else {
            treeSet.add(acceptedProtocolStrByUid);
        }
        return treeSet;
    }

    public synchronized void acceptProtocol(ProtocolInfo protocolInfo, String str, boolean z) {
        L.d(TAG, "acceptProtocol-->user[%s]", str);
        if (!TextUtils.isEmpty(str) && !User.isLogoutUser(str) && protocolInfo != null) {
            setHasDisagreedAliasChanged(false);
            String strFilterAcceptedProtocolData = filterAcceptedProtocolData(protocolInfo, str, getAcceptedProtocolsByUid(str), z);
            L.d(TAG, " %s acceptProtocol [%s] ", str, strFilterAcceptedProtocolData);
            SharedPreferences.Editor editorEdit = this.mUidPidSp.edit();
            if (User.isRealUser(str)) {
                editorEdit.remove(User.USER_NAME_LAUNCHER);
            }
            editorEdit.putString(str, strFilterAcceptedProtocolData);
            editorEdit.apply();
            saveUserAcceptProtocol(protocolInfo.id, str);
            updateCurrentProtocol(protocolInfo);
        }
    }

    private String filterAcceptedProtocolData(ProtocolInfo protocolInfo, String str, TreeSet<String> treeSet, boolean z) {
        TreeSet<String> treeSet2 = new TreeSet<>();
        ArrayList arrayList = new ArrayList();
        TreeSet<String> treeSet3 = new TreeSet<>();
        treeSet2.add(protocolInfo.id + "-" + protocolInfo.version);
        arrayList.add(String.valueOf(protocolInfo.id));
        Iterator<String> it = protocolInfo.subProtocolUrls.iterator();
        while (it.hasNext()) {
            ProtocolInfo.SubProtocolInfo subProtocol = protocolInfo.getSubProtocol(it.next());
            if (subProtocol != null && subProtocol.getId() != -1) {
                arrayList.add(String.valueOf(subProtocol.getId()));
                if (subProtocol.isAccept() || z) {
                    L.d("isAccept:" + subProtocol.isAccept() + ",isAcceptAll:" + z);
                    StringBuilder sb = new StringBuilder();
                    sb.append(subProtocol.getId());
                    sb.append("-");
                    sb.append(subProtocol.getVersion());
                    treeSet2.add(sb.toString());
                } else {
                    treeSet2.add(subProtocol.getId() + "-0");
                    String alias = subProtocol.getAlias();
                    if (!TextUtils.isEmpty(alias)) {
                        treeSet3.add(alias);
                    }
                }
            }
        }
        L.d("uid:" + str + ",last mDisagreedAliasStr:" + this.mDisagreedAliasStr);
        L.d("current diagreedAlias:" + treeSet3.toString() + ",last mDisagreedAlias:" + this.mDisagreedAlias.toString());
        setHasDisagreedAliasChanged(treeSet3.equals(this.mDisagreedAlias) ^ true);
        this.mDisagreedAlias = treeSet3;
        if (!treeSet3.isEmpty()) {
            this.mDisagreedAliasStr = TextUtils.join(",", treeSet3);
        } else {
            this.mDisagreedAliasStr = "";
        }
        L.d("uid:" + str + ",mDisagreedAliasStr:" + this.mDisagreedAliasStr + ", isDisagreedAliasChanged:" + isHasDisagreedAliasChanged());
        Iterator<String> it2 = treeSet.iterator();
        while (it2.hasNext()) {
            String next = it2.next();
            boolean zStartsWith = false;
            Iterator it3 = arrayList.iterator();
            while (it3.hasNext()) {
                zStartsWith = next.startsWith(((String) it3.next()) + "-");
                if (zStartsWith) {
                    break;
                }
            }
            if (!zStartsWith) {
                treeSet2.add(next);
            }
        }
        String strJoin = TextUtils.join(",", treeSet2);
        this.mUidPidCache.put(str, strJoin);
        ProtocolManager.getInstance().updateCurrentUser(str, treeSet2);
        return strJoin;
    }

    public boolean hasAcceptLaunchProtocol() {
        return this.mConfigSp.getBoolean(PROTOCOL_LAUNCH_FLAG, false);
    }

    public void setAcceptLaunchProtocol() {
        SharedPreferences.Editor editorEdit = this.mConfigSp.edit();
        editorEdit.putBoolean(PROTOCOL_LAUNCH_FLAG, true);
        editorEdit.apply();
    }

    public int getAppVersionCode() {
        return this.mConfigSp.getInt(PROTOCOL_APP_VERSION_CODE, 0);
    }

    public long getLastUploadTime() {
        return this.mConfigSp.getLong(PROTOCOL_UPDATE_TIME, 0L);
    }

    public String getETag(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String string = this.mConfigSp.getString(str, "");
        L.d(TAG, "etag key:" + str + ",eTag:" + string);
        return string;
    }

    public void saveETag(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return;
        }
        L.d(TAG, "etag key:" + str + ",eTag:" + str2);
        SharedPreferences.Editor editorEdit = this.mConfigSp.edit();
        editorEdit.putString(str, str2);
        editorEdit.apply();
    }

    public String getModifyTime(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String string = this.mConfigSp.getString(PROTOCOL_MODIFY + str, "");
        L.d(TAG, "Last-Modified key:" + str + ",Last-Modified:" + string);
        return string;
    }

    public void saveModifyTime(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return;
        }
        L.d(TAG, "lastModified key:" + str + ",lastModified:" + str2);
        SharedPreferences.Editor editorEdit = this.mConfigSp.edit();
        StringBuilder sb = new StringBuilder();
        sb.append(PROTOCOL_MODIFY);
        sb.append(str);
        editorEdit.putString(sb.toString(), str2);
        editorEdit.apply();
    }

    public void saveUploadClassTag(int i, long j, String str, String str2) {
        SharedPreferences.Editor editorEdit = this.mConfigSp.edit();
        editorEdit.putInt(PROTOCOL_APP_VERSION_CODE, i);
        editorEdit.putLong(PROTOCOL_UPDATE_TIME, j);
        if (!TextUtils.isEmpty(str)) {
            editorEdit.putString(str, str2);
        }
        editorEdit.apply();
    }

    public int getAcceptedProtocolVersion(String str) {
        if (TextUtils.isEmpty(str) || !str.contains("-")) {
            return 0;
        }
        String[] strArrSplit = str.split("-");
        if (strArrSplit.length != 2) {
            return 0;
        }
        try {
            return Integer.parseInt(strArrSplit[1]);
        } catch (Exception unused) {
            return 0;
        }
    }

    public String getDisagreedAliasStr() {
        return this.mDisagreedAliasStr;
    }

    public boolean isHasDisagreedAliasChanged() {
        return this.hasDisagreedAliasChanged;
    }

    public void setHasDisagreedAliasChanged(boolean z) {
        this.hasDisagreedAliasChanged = z;
    }
}