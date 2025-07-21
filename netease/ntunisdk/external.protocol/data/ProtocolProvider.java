package com.netease.ntunisdk.external.protocol.data;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.ntunisdk.external.protocol.Situation;
import com.netease.ntunisdk.external.protocol.data.ProtocolInfo;
import com.netease.ntunisdk.external.protocol.utils.AsyncTask;
import com.netease.ntunisdk.external.protocol.utils.FetchProtocolException;
import com.netease.ntunisdk.external.protocol.utils.FileUtil;
import com.netease.ntunisdk.external.protocol.utils.L;
import com.netease.ntunisdk.external.protocol.utils.Response;
import com.netease.ntunisdk.external.protocol.utils.TextCompat;
import com.netease.ntunisdk.external.protocol.utils.UrlConnectImpl;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ProtocolProvider {
    private static final String TAG = "P";
    private boolean hasAcceptLaunchProtocol = false;
    private int launchProtocolId;
    private int launchProtocolVersion;
    private Context mContext;
    private String mCurCompactUrl;
    private ProtocolInfo mCurrentProtocol;
    private ProtocolProp mProp;

    public void init(Context context) {
        if (this.mContext != null) {
            return;
        }
        L.d(TAG, "init ProtocolProvider");
        this.mContext = context;
        Store.getInstance().init(context);
    }

    @Deprecated
    public void setContext(Context context) {
        init(context);
    }

    public ProtocolInfo getBaseProtocol() {
        return this.mCurrentProtocol;
    }

    public void setProp(ProtocolProp protocolProp) {
        this.mProp = protocolProp;
    }

    public boolean isProtocolChanged() {
        return (this.mProp == null || TextUtils.isEmpty(this.mCurCompactUrl) || !this.mCurCompactUrl.equals(this.mProp.getUrl())) ? false : true;
    }

    public void checkLatestVersionProtocol() throws Exception {
        String protocolMainUrl = getProtocolMainUrl();
        if (TextUtils.isEmpty(this.mCurCompactUrl)) {
            this.mCurCompactUrl = protocolMainUrl;
        } else if (!this.mCurCompactUrl.equals(protocolMainUrl)) {
            this.mCurrentProtocol = null;
            SDKRuntime.getInstance().clearProtocolInMemory();
            this.mCurCompactUrl = protocolMainUrl;
        }
        L.d(TAG, "start checkLatestProtocol !! ");
        if (ProtocolInfo.hasUpdate(this.mCurrentProtocol)) {
            L.d(TAG, "start checkLatestProtocol [has update = true]!! ");
            return;
        }
        if (TextUtils.isEmpty(this.mCurCompactUrl)) {
            L.d(TAG, "CompactUrl is Null, No Need Show Protocol!! ");
            throw new Exception("CompactUrl is Null, No Need Show Protocol!! ");
        }
        ProtocolFile protocolFile = new ProtocolFile(this.mCurCompactUrl);
        ProtocolInfo localMainProtocol = protocolFile.getLocalMainProtocol();
        if (localMainProtocol != null) {
            int i = localMainProtocol.version;
        }
        ProtocolInfo protocolInfoDownloadAndCheckNewProtocol = downloadAndCheckNewProtocol(protocolFile, localMainProtocol, this.mCurCompactUrl);
        if (protocolInfoDownloadAndCheckNewProtocol == null || ProtocolInfo.isEmpty(protocolInfoDownloadAndCheckNewProtocol)) {
            if (this.mCurrentProtocol == null) {
                this.mCurrentProtocol = ProtocolParser.readLocalProtocol(protocolFile, protocolMainUrl);
            }
            if (this.mCurrentProtocol == null) {
                clearLocalCache();
                throw new FetchProtocolException();
            }
        } else {
            this.mCurrentProtocol = protocolInfoDownloadAndCheckNewProtocol;
        }
        ProtocolInfo protocolInfo = this.mCurrentProtocol;
        protocolInfo.status = Math.max(protocolInfo.status, 2);
        SDKRuntime.getInstance().addProtocolIntoMemory(this.mCurrentProtocol);
    }

    private final void clearLocalCache() {
        L.d(TAG, "load Protocol failed, start clear cache");
        FileUtil.deleteFileTree(SDKRuntime.getInstance().getCacheDir());
        SDKRuntime.getInstance().setHasCopiedAsserts(false);
    }

    public ProtocolInfo downloadAndCheckNewProtocol(ProtocolFile protocolFile, ProtocolInfo protocolInfo, String str) {
        String strWrapperUrl = TextCompat.wrapperUrl(str);
        L.d(TAG, "requestUrl :" + strWrapperUrl);
        String strMd5 = TextCompat.md5(str);
        Response responseFetch = UrlConnectImpl.fetch(strWrapperUrl, prepareHeader(protocolFile, str, strMd5));
        String contentStr = responseFetch.getContentStr();
        L.d(TAG, "getProtocolFromServer : \n" + contentStr + ", response code:" + responseFetch.getStatus());
        if (TextUtils.isEmpty(contentStr)) {
            return null;
        }
        try {
            ProtocolInfo byJson = ProtocolParser.parseByJson(str, new JSONObject(contentStr), false);
            if (byJson != null) {
                byJson.setProtocolFile(protocolFile);
                String protocolTextSavePath = getProtocolTextSavePath(protocolFile, str);
                L.d(TAG, "save protocol path = " + protocolTextSavePath + ", origin:" + str);
                if (!TextUtils.isEmpty(contentStr)) {
                    saveETag(strMd5, responseFetch.getHeaderProperty(Const.ETAG));
                    saveModifyTime(strMd5, responseFetch.getHeaderProperty(Const.LAST_MODIFIED));
                }
                FileUtil.writeFile(protocolTextSavePath, contentStr);
                boolean zCheckProtocolUpdate = checkProtocolUpdate(protocolInfo, byJson);
                L.d(TAG, "needUpdateProtocol = " + zCheckProtocolUpdate);
                if (zCheckProtocolUpdate) {
                    downloadProtocolText(byJson, protocolFile);
                    Store.getInstance().updateLocalVersion(byJson.id, byJson.version);
                    SDKRuntime.getInstance().addProtocolIntoMemory(byJson);
                    Iterator<String> it = byJson.subProtocolUrls.iterator();
                    while (it.hasNext()) {
                        String next = it.next();
                        checkSubProtocol(byJson, zCheckProtocolUpdate, next);
                        ProtocolInfo.SubProtocolInfo subProtocol = byJson.getSubProtocol(next);
                        ProtocolInfo.SubProtocolInfo subProtocol2 = protocolInfo != null ? protocolInfo.getSubProtocol(next) : null;
                        if (subProtocol != null) {
                            subProtocol.setStatus(2);
                            if (subProtocol2 != null && subProtocol.getVersion() > subProtocol2.getVersion()) {
                                subProtocol.setStatus(3);
                            }
                        }
                    }
                }
            }
            return byJson;
        } catch (Exception e) {
            L.e(TAG, "jsonException >> " + e.getMessage());
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkSubProtocol(final ProtocolInfo protocolInfo, final boolean z, final String str) {
        if (TextUtils.isEmpty(str) || SDKRuntime.getInstance().hasInMemory(str)) {
            return;
        }
        final ProtocolFile protocolFile = protocolInfo.getProtocolFile();
        AsyncTask.execute(TextCompat.md5(str), new Runnable() { // from class: com.netease.ntunisdk.external.protocol.data.ProtocolProvider.1
            @Override // java.lang.Runnable
            public void run() {
                boolean z2 = !SDKRuntime.getInstance().hasInMemory(str);
                ProtocolInfo localProtocol = ProtocolParser.readLocalProtocol(protocolFile, str);
                int i = localProtocol != null ? localProtocol.version : 0;
                ProtocolInfo byJson = null;
                if (z || z2) {
                    L.d(ProtocolProvider.TAG, "Url[subProtocol] : " + str);
                    String strWrapperUrl = TextCompat.wrapperUrl(str);
                    L.d(ProtocolProvider.TAG, "requestUrl[subProtocol] :" + strWrapperUrl);
                    String contentStr = UrlConnectImpl.fetch(strWrapperUrl, ProtocolProvider.this.prepareHeader(protocolFile, str, TextCompat.md5(str))).getContentStr();
                    if (!TextUtils.isEmpty(contentStr)) {
                        try {
                            byJson = ProtocolParser.parseByJson(str, new JSONObject(contentStr), false);
                        } catch (Exception unused) {
                        }
                    }
                    if (byJson != null) {
                        byJson.setProtocolFile(protocolFile);
                        if (byJson.version > i) {
                            byJson.status = 3;
                            ProtocolProvider.this.downloadProtocolText(byJson, protocolFile);
                            localProtocol = byJson;
                        } else {
                            byJson.status = 2;
                        }
                        Iterator<String> it = localProtocol.subProtocolUrls.iterator();
                        while (it.hasNext()) {
                            ProtocolProvider.this.checkSubProtocol(localProtocol, true, it.next());
                        }
                    } else if (localProtocol != null) {
                        localProtocol.status = 2;
                    }
                    if (localProtocol != null && (TextUtils.isEmpty(localProtocol.text) || TextUtils.isEmpty(localProtocol.updateText))) {
                        ProtocolProvider.this.downloadProtocolText(localProtocol, protocolFile);
                    }
                    byJson = localProtocol;
                }
                if (byJson != null) {
                    if (byJson.globalInfo == null) {
                        byJson.globalInfo = protocolInfo.globalInfo;
                    }
                    protocolInfo.subProtocol.add(byJson);
                    SDKRuntime.getInstance().addProtocolIntoMemory(byJson);
                }
            }
        });
    }

    private boolean checkProtocolUpdate(ProtocolInfo protocolInfo, ProtocolInfo protocolInfo2) {
        int i = protocolInfo != null ? protocolInfo.version : 0;
        int localVersion = Store.getInstance().getLocalVersion(protocolInfo2.id);
        if (localVersion == 0 && protocolInfo != null) {
            localVersion = protocolInfo.version;
        }
        L.d(TAG, "localVersion = " + i);
        if (i > 0) {
            if (protocolInfo2.version > i || protocolInfo2.version > localVersion) {
                protocolInfo2.status = 3;
            } else if (!TextUtils.isEmpty(protocolInfo.text)) {
                return false;
            }
        }
        return true;
    }

    private String getProtocolMainUrl() {
        String url = (SDKRuntime.getInstance().isPublishMainLand() || this.mProp.getProtocolConfig() == null) ? this.mProp.getUrl() : this.mProp.getProtocolConfig().url;
        if (TextUtils.isEmpty(url)) {
            url = this.mCurCompactUrl;
        }
        return TextUtils.isEmpty(url) ? Const.PROTOCOL_DEFAULT : url;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public HashMap<String, String> prepareHeader(ProtocolFile protocolFile, String str, String str2) {
        HashMap<String, String> map = new HashMap<>();
        if (protocolFile.checkProtocolExist(str)) {
            String localETag = getLocalETag(str2);
            L.d("ETag:" + localETag);
            if (!TextUtils.isEmpty(localETag)) {
                map.put("If-None-Match", localETag);
            }
            String modifyTime = getModifyTime(str2);
            L.d("lastModifyTime:" + modifyTime);
            if (!TextUtils.isEmpty(modifyTime)) {
                map.put(Const.IF_MODIFIED_SINCE, modifyTime);
            }
        }
        return map;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downloadProtocolText(ProtocolInfo protocolInfo, ProtocolFile protocolFile) {
        String strDownload = download(protocolFile, protocolInfo.textUrl, protocolInfo.textHash);
        if (!TextUtils.isEmpty(strDownload)) {
            strDownload = ProtocolTextHandler.handle(strDownload, this.mProp.getIssuer(), this.mProp.getGameName());
            protocolInfo.text = strDownload;
        }
        String strDownload2 = download(protocolFile, protocolInfo.updateTextUrl, protocolInfo.updateTextHash);
        if (!TextUtils.isEmpty(strDownload2)) {
            protocolInfo.updateText = ProtocolTextHandler.handle(strDownload2, this.mProp.getIssuer(), this.mProp.getGameName());
        }
        protocolInfo.reviewText = strDownload;
    }

    private String download(ProtocolFile protocolFile, String str, String str2) {
        String str3;
        String str4;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String protocolText = getProtocolText(protocolFile, str);
        if (!TextUtils.isEmpty(protocolText)) {
            try {
                if (Build.VERSION.SDK_INT >= 19) {
                    str3 = new String(Base64.decode(protocolText, 0), StandardCharsets.UTF_8);
                } else {
                    str3 = new String(Base64.decode(protocolText, 0), "UTF-8");
                }
            } catch (Exception unused) {
                str3 = null;
            }
            if (!TextUtils.isEmpty(str3)) {
                return str3;
            }
        }
        String strMd5 = TextCompat.md5(str);
        Response responseFetch = UrlConnectImpl.fetch(TextCompat.wrapperUrl(str), prepareHeader(protocolFile, str, strMd5));
        String contentStr = responseFetch.getContentStr();
        if (!TextUtils.isEmpty(contentStr)) {
            String protocolTextSavePath = getProtocolTextSavePath(protocolFile, str);
            L.d(TAG, "save protocol path = " + protocolTextSavePath + ", origin:" + str);
            if (FileUtil.writeFile(protocolTextSavePath, contentStr)) {
                saveETag(strMd5, responseFetch.getHeaderProperty(Const.ETAG));
                saveModifyTime(strMd5, responseFetch.getHeaderProperty(Const.LAST_MODIFIED));
                L.e(TAG, "save Protocol success");
            } else {
                L.e(TAG, "save Protocol error");
            }
        }
        if (TextUtils.isEmpty(str2)) {
            return contentStr;
        }
        try {
            if (Build.VERSION.SDK_INT >= 19) {
                str4 = new String(Base64.decode(contentStr, 0), StandardCharsets.UTF_8);
            } else {
                str4 = new String(Base64.decode(contentStr, 0), "UTF-8");
            }
            String strMd52 = TextCompat.md5(str4);
            L.d(TAG, "textMd5 = " + strMd52);
            if (str2.equalsIgnoreCase(strMd52)) {
                L.d(TAG, "MD5 match");
                return str4;
            }
            L.d(TAG, "MD5 not matched");
            return str4;
        } catch (Exception unused2) {
            return null;
        }
    }

    public boolean checkNeedShowProtocolWhenLaunch() {
        if (this.mCurrentProtocol == null) {
            return false;
        }
        return !Store.getInstance().hasUserAcceptProtocol(this.mCurrentProtocol.id);
    }

    public int checkHasAcceptProtocolByUrl(String str) {
        TreeSet<String> acceptedProtocolsByUid;
        if (!TextUtils.isEmpty(str) && (acceptedProtocolsByUid = Store.getInstance().getAcceptedProtocolsByUid(str)) != null && acceptedProtocolsByUid.size() != 0) {
            Matcher matcher = Pattern.compile("v(\\d+).json").matcher(this.mCurCompactUrl);
            String strGroup = null;
            try {
                if (matcher.find()) {
                    strGroup = matcher.group(1);
                }
            } catch (Throwable unused) {
            }
            if (TextUtils.isEmpty(strGroup)) {
                return 0;
            }
            Iterator<String> it = acceptedProtocolsByUid.iterator();
            while (it.hasNext()) {
                String next = it.next();
                if (next.startsWith(strGroup + "-") && !next.endsWith("-0")) {
                    return 1;
                }
            }
        }
        return 0;
    }

    public int checkNeedShowProtocolByUid(User user, String str) {
        L.d(TAG, "enter checkNeedShowProtocolByUid");
        if (this.mCurrentProtocol == null || user == null || TextUtils.isEmpty(user.getUid())) {
            return 1;
        }
        String uid = user.getUid();
        TreeSet<String> acceptedProtocolsByUid = Store.getInstance().getAcceptedProtocolsByUid(uid);
        if (acceptedProtocolsByUid == null || acceptedProtocolsByUid.size() == 0) {
            return 0;
        }
        int iIsAcceptProtocol = isAcceptProtocol(user, this.mCurrentProtocol, acceptedProtocolsByUid, str);
        if (iIsAcceptProtocol == 0 || iIsAcceptProtocol == 1) {
            Object[] objArr = new Object[2];
            objArr[0] = uid;
            objArr[1] = Boolean.valueOf(iIsAcceptProtocol != 0);
            L.d(TAG, "%s isAccept = %b", objArr);
        } else {
            L.d(TAG, "%s is need Update", uid);
        }
        return iIsAcceptProtocol;
    }

    private int isAcceptProtocol(User user, ProtocolInfo protocolInfo, TreeSet<String> treeSet, String str) {
        String next;
        String str2;
        if (protocolInfo == null || user == null || TextUtils.isEmpty(user.getUid())) {
            return 1;
        }
        L.d(TAG, "isAcceptProtocol : " + treeSet.toString());
        String uid = user.getUid();
        Iterator<String> it = treeSet.iterator();
        while (true) {
            if (!it.hasNext()) {
                next = null;
                break;
            }
            next = it.next();
            if (next.startsWith(protocolInfo.id + "-") && !next.endsWith("-0")) {
                break;
            }
        }
        L.d(TAG, "curIdVersion = " + next);
        if (next == null) {
            return 0;
        }
        Iterator<String> it2 = protocolInfo.subProtocolUrls.iterator();
        while (it2.hasNext()) {
            ProtocolInfo.SubProtocolInfo subProtocol = protocolInfo.getSubProtocol(it2.next());
            if (subProtocol != null && subProtocol.isRequiredProtocol(str)) {
                Iterator<String> it3 = treeSet.iterator();
                while (true) {
                    if (!it3.hasNext()) {
                        str2 = null;
                        break;
                    }
                    String next2 = it3.next();
                    if (next2.startsWith(subProtocol.getId() + "-") && !next2.endsWith("-0")) {
                        str2 = subProtocol.getId() + "-" + subProtocol.getVersion();
                        break;
                    }
                }
                if (TextUtils.isEmpty(str2)) {
                    return 3;
                }
            }
        }
        ArrayList arrayList = new ArrayList();
        if (protocolInfo.isMinorChange) {
            int acceptedProtocolVersion = Store.getInstance().getAcceptedProtocolVersion(next);
            if (protocolInfo.prevMajorChangeId > 0 && acceptedProtocolVersion < protocolInfo.prevMajorChangeId) {
                return 3;
            }
            String str3 = protocolInfo.id + "-" + protocolInfo.version;
            if (!next.equals(str3)) {
                treeSet.remove(next);
                arrayList.add(str3);
            }
            JSONArray jSONArray = new JSONArray();
            Iterator<String> it4 = protocolInfo.subProtocolUrls.iterator();
            while (it4.hasNext()) {
                ProtocolInfo.SubProtocolInfo subProtocol2 = protocolInfo.getSubProtocol(it4.next());
                if (subProtocol2 != null) {
                    Iterator<String> it5 = treeSet.iterator();
                    while (it5.hasNext()) {
                        String next3 = it5.next();
                        if (next3.startsWith(subProtocol2.getId() + "-")) {
                            if (next3.endsWith("-0")) {
                                subProtocol2.setAccept(false);
                                L.d(subProtocol2.getAlias() + "subProtocolInfo isNotAccept:" + subProtocol2.isAccept());
                            } else {
                                subProtocol2.setAccept(true);
                                L.d(subProtocol2.getAlias() + "subProtocolInfo isAccept:" + subProtocol2.isAccept());
                                jSONArray.put(subProtocol2.getAlias());
                                arrayList.add(subProtocol2.getId() + "-" + subProtocol2.getVersion());
                                it5.remove();
                            }
                        }
                    }
                }
            }
            arrayList.addAll(treeSet);
            Store.getInstance().acceptProtocol(protocolInfo, uid, false);
            user.addAcceptProtocolAlias("" + protocolInfo.id, jSONArray);
            return 1;
        }
        String str4 = protocolInfo.id + "-" + protocolInfo.version;
        L.d(TAG, "protocolV : " + str4);
        JSONArray jSONArray2 = new JSONArray();
        Iterator<String> it6 = protocolInfo.subProtocolUrls.iterator();
        int i = 1;
        while (it6.hasNext()) {
            ProtocolInfo.SubProtocolInfo subProtocol3 = protocolInfo.getSubProtocol(it6.next());
            if (subProtocol3 != null) {
                subProtocol3.isRequiredProtocol(str);
                String str5 = subProtocol3.getId() + "-" + subProtocol3.getVersion();
                Iterator<String> it7 = treeSet.iterator();
                while (it7.hasNext()) {
                    String next4 = it7.next();
                    if (next4.startsWith(subProtocol3.getId() + "-")) {
                        if (next4.endsWith("-0")) {
                            subProtocol3.setStatus(0);
                        } else if (!next4.equals(str5)) {
                            subProtocol3.setStatus(3);
                            i = 3;
                        } else {
                            jSONArray2.put(subProtocol3.getAlias());
                        }
                    }
                }
            }
        }
        user.addAcceptProtocolAlias("" + protocolInfo.id, jSONArray2);
        if (next.equals(str4)) {
            return i;
        }
        return 3;
    }

    public void saveConfirmByUid(String str) {
        saveConfirmByUid(str, true);
    }

    public void saveConfirmByUid(String str, boolean z) {
        ProtocolInfo protocolInfo;
        if (TextUtils.isEmpty(str) || (protocolInfo = this.mCurrentProtocol) == null) {
            return;
        }
        protocolInfo.acceptStatus = 1;
        Store.getInstance().acceptProtocol(this.mCurrentProtocol, str, z);
    }

    public String getProtocolText(ProtocolFile protocolFile, String str) {
        if (str == null) {
            return null;
        }
        String protocolTextSavePath = getProtocolTextSavePath(protocolFile, str);
        if (new File(protocolTextSavePath).exists()) {
            return FileUtil.readFile(protocolTextSavePath, "UTF-8");
        }
        return null;
    }

    public boolean hasAcceptLaunchProtocol() {
        if (!this.hasAcceptLaunchProtocol && Store.getInstance() != null) {
            this.hasAcceptLaunchProtocol = Store.getInstance().hasAcceptLaunchProtocol();
        }
        return this.hasAcceptLaunchProtocol;
    }

    public void setAcceptLaunchProtocol() {
        if (!this.hasAcceptLaunchProtocol) {
            Store.getInstance().setAcceptLaunchProtocol();
        }
        this.hasAcceptLaunchProtocol = true;
    }

    public final void readConfig() {
        if (SDKRuntime.getInstance().getConfig() == null) {
            File file = new File(SDKRuntime.getInstance().getCacheDir(), TextCompat.md5(Const.PROTOCOL_CONFIG));
            if (file.exists()) {
                try {
                    SDKRuntime.getInstance().setConfig(ProtocolParser.parseConfig(new JSONObject(FileUtil.readFile(file.getPath(), "UTF-8"))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getProtocolTextSavePath(ProtocolFile protocolFile, String str) {
        File file = protocolFile.cacheDir;
        if (!file.exists()) {
            file.mkdirs();
        }
        return new File(file, TextCompat.md5(str)).getAbsolutePath();
    }

    public void loadLocalProtocol() {
        L.d(TAG, "loadLocalProtocol");
        ProtocolInfo protocolInfo = new ProtocolInfo(0, null);
        this.mCurrentProtocol = protocolInfo;
        protocolInfo.version = 0;
        if (FileUtil.isAssetsFileExist(this.mContext, Const.PROTOCOL_DEFAULT_FILE_NAME)) {
            String assetsFileAsString = FileUtil.readAssetsFileAsString(this.mContext, Const.PROTOCOL_DEFAULT_FILE_NAME);
            this.mCurrentProtocol.text = ProtocolTextHandler.handle(assetsFileAsString, this.mProp.getIssuer(), this.mProp.getGameName());
            return;
        }
        this.mCurrentProtocol = null;
    }

    public void loadLocalProtocolFromFiles() throws Exception {
        String protocolMainUrl = getProtocolMainUrl();
        L.d(TAG, "load local protocol : requestUrl = " + protocolMainUrl);
        if (TextUtils.isEmpty(this.mCurCompactUrl)) {
            this.mCurCompactUrl = protocolMainUrl;
        } else if (this.mCurCompactUrl.equals(protocolMainUrl)) {
            if (this.mCurrentProtocol != null) {
                return;
            }
        } else {
            this.mCurrentProtocol = null;
            this.mCurCompactUrl = protocolMainUrl;
        }
        if (TextUtils.isEmpty(this.mCurCompactUrl)) {
            L.d(TAG, "read Protocol failed! compact url is Null!");
            return;
        }
        try {
            ProtocolFile protocolFile = new ProtocolFile(this.mCurCompactUrl);
            L.d(TAG, "start load local protocol !! ");
            this.mCurrentProtocol = ProtocolParser.readLocalProtocol(protocolFile, protocolMainUrl);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("load local protocol error!");
        }
    }

    public int getAppVersionCode() {
        return Store.getInstance().getAppVersionCode();
    }

    public long getLastUploadTime() {
        return Store.getInstance().getLastUploadTime();
    }

    public String getLocalETag(String str) {
        return Store.getInstance().getETag(str);
    }

    public void saveETag(String str, String str2) {
        Store.getInstance().saveETag(str, str2);
    }

    public String getModifyTime(String str) {
        return Store.getInstance().getModifyTime(str);
    }

    public void saveModifyTime(String str, String str2) {
        Store.getInstance().saveModifyTime(str, str2);
    }

    public void saveUploadClassTag(int i, long j, String str, String str2) {
        Store.getInstance().saveUploadClassTag(i, j, str, str2);
    }

    public int getLaunchProtocolId() {
        if (this.launchProtocolId == 0) {
            ProtocolInfo protocolInfo = this.mCurrentProtocol;
            this.launchProtocolId = protocolInfo != null ? protocolInfo.id : 0;
        }
        return this.launchProtocolId;
    }

    public synchronized void setLaunchProtocolId(int i) {
        this.launchProtocolId = i;
    }

    public int getLaunchProtocolVersion() {
        if (this.launchProtocolVersion == 0) {
            ProtocolInfo protocolInfo = this.mCurrentProtocol;
            this.launchProtocolVersion = protocolInfo != null ? protocolInfo.version : 0;
        }
        return this.launchProtocolVersion;
    }

    public synchronized void setLaunchProtocolVersion(int i) {
        L.d(TAG, "setLaunchProtocolVersion [Manager]: " + i);
        this.launchProtocolVersion = i;
    }

    public final ArrayList<ProtocolInfo.ConcreteSubProtocol> filterProtocolInfo(Situation situation, String str, ProtocolInfo protocolInfo, User user) {
        ArrayList<ProtocolInfo.ConcreteSubProtocol> arrayList = new ArrayList<>();
        if (protocolInfo != null && user != null && protocolInfo.isHomeStyle()) {
            boolean zHasAcceptProtocol = user.hasAcceptProtocol(String.valueOf(protocolInfo.id));
            Iterator<String> it = protocolInfo.subProtocolUrls.iterator();
            int i = 0;
            while (it.hasNext()) {
                String next = it.next();
                ProtocolInfo.SubProtocolInfo subProtocol = protocolInfo.getSubProtocol(next);
                if (subProtocol != null) {
                    int showStatusByScene = subProtocol.getShowStatusByScene(str);
                    int protocolAcceptStatus = user.getProtocolAcceptStatus(String.valueOf(subProtocol.getId()));
                    boolean z = protocolAcceptStatus == 1;
                    boolean z2 = protocolAcceptStatus == 4;
                    subProtocol.setAccept(z);
                    L.d(TAG, "subProtocol:" + next + ",hasAcceptSubProtocol:" + z + ", isNewSubProtocol:" + z2);
                    boolean z3 = showStatusByScene == 1;
                    if (subProtocol.isShowing(showStatusByScene) && (Situation.REVIEW == situation || !zHasAcceptProtocol || z2 || (subProtocol.getStatus() == 3 && (z || z3)))) {
                        ProtocolInfo.ConcreteSubProtocol concreteSubProtocol = new ProtocolInfo.ConcreteSubProtocol(str, showStatusByScene, subProtocol, protocolInfo.globalInfo);
                        if (Situation.REVIEW == situation) {
                            concreteSubProtocol.setCanAccept((user.isLogout() || user.isLauncher()) ? false : true);
                        }
                        if (showStatusByScene == 1) {
                            arrayList.add(i, concreteSubProtocol);
                            i++;
                        } else {
                            arrayList.add(concreteSubProtocol);
                        }
                    }
                }
            }
        }
        return arrayList;
    }
}