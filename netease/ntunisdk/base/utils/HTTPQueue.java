package com.netease.ntunisdk.base.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import androidx.browser.trusted.sharing.ShareTarget;
import com.CCMsgSdk.ControlCmdType;
import com.netease.mpay.httpdns.HttpDns;
import com.netease.mpay.httpdns.ResolveDnsResult;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.base.utils.clientlog.ClientLog;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;

/* loaded from: classes3.dex */
public class HTTPQueue {
    private static final int CAPACITY = 100;
    public static final int CONNECTION_TIMEOUT = 15000;
    public static final String HTTPQUEUE_COMMON = "UniSDK";
    public static final String HTTPQUEUE_LOG = "LOG";
    public static final String HTTPQUEUE_PAY = "PAY";
    private static final String KEY_QUEUE_DELAY = "delay";
    private static final String KEY_QUEUE_ITEM_PREFIX = "item_";
    private static final String KEY_QUEUE_RESEND = "resend";
    private static final int MAX_RETRY = 5;
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final int SO_TIMEOUT = 15000;
    private static Map<String, HTTPQueue> s_instances = new TreeMap();
    private static ReentrantLock s_lock = new ReentrantLock();
    private LinkedBlockingQueue<Runnable> linkedQueue;
    private HTTPCallback m_callback;
    private Context m_context;
    private ExecutorService m_executorService;
    private String m_name;
    private SharedPreferences sp;
    private String TAG = "UniSDK HTTPQ_";
    private Object obj = new Object();

    public void onNetworkConnected() {
    }

    public void setCapacity(int i) {
    }

    public HTTPQueue(String str, int i) {
        this.TAG += str;
        this.m_name = str;
        this.linkedQueue = new LinkedBlockingQueue<>();
        this.m_executorService = new ThreadPoolExecutor(1, 3, 5000L, TimeUnit.MILLISECONDS, this.linkedQueue);
    }

    public static HTTPQueue getInstance(String str) {
        if (s_lock == null) {
            s_lock = new ReentrantLock();
        }
        s_lock.lock();
        if (s_instances == null) {
            s_instances = new TreeMap();
        }
        HTTPQueue hTTPQueue = s_instances.containsKey(str) ? s_instances.get(str) : null;
        if (hTTPQueue == null) {
            hTTPQueue = new HTTPQueue(str, 0);
            s_instances.put(str, hTTPQueue);
        }
        s_lock.unlock();
        return hTTPQueue;
    }

    public static class QueueItem {
        private static final String TAG = "UniSDK_QueueItem";
        private static AtomicInteger s_index = new AtomicInteger(0);
        public Boolean bSync;
        private Map<String, String> bodyPairs;
        private String bodyStr;
        public HTTPCallback callback;
        public int connectionTimeout;
        private Map<String, String> headers;
        public String host;
        public String id;
        public boolean isNeedHttpDns;
        public String keyRSA;
        public int leftRetry;
        public String method;
        public int soTimeout;
        public String strResp;
        public String transParam;
        public String url;

        /* synthetic */ QueueItem(AnonymousClass1 anonymousClass1) {
            this();
        }

        private QueueItem() {
            this.bSync = Boolean.TRUE;
            this.method = "";
            this.url = "";
            this.headers = new TreeMap();
            this.bodyStr = "";
            this.bodyPairs = new TreeMap();
            this.keyRSA = "";
            this.strResp = "";
            this.transParam = "";
            this.connectionTimeout = 15000;
            this.soTimeout = 15000;
            this.id = "";
            this.leftRetry = 5;
            this.isNeedHttpDns = false;
            this.host = "";
            int iIncrementAndGet = s_index.incrementAndGet();
            if (iIncrementAndGet >= 10000) {
                s_index.set(0);
                iIncrementAndGet = 1;
            }
            this.id = System.currentTimeMillis() + "_" + String.format(Locale.US, "%d", Integer.valueOf(iIncrementAndGet));
            if (SdkMgr.getInst() != null) {
                this.connectionTimeout = SdkMgr.getInst().getPropInt(ConstProp.UNISDK_JF_TIMEOUT, 15000);
                this.soTimeout = SdkMgr.getInst().getPropInt(ConstProp.UNISDK_JF_TIMEOUT, 15000);
                if (this.headers.containsKey("X-TASK-ID")) {
                    return;
                }
                this.headers.put("X-TASK-ID", "transid=" + SdkMgr.getInst().getPropStr(ConstProp.TRANS_ID) + ",uni_transaction_id=" + ClientLog.getInst().getUniTransactionId());
            }
        }

        public void setHeaders(Map<String, String> map) {
            if (map != null) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    this.headers.put(entry.getKey(), entry.getValue());
                }
            }
        }

        public Map<String, String> getHeaders() {
            return this.headers;
        }

        public void setBody(Map<String, String> map) {
            this.bodyPairs = map;
            this.headers.put("Content-type", ShareTarget.ENCODING_TYPE_URL_ENCODED);
        }

        public void setBody(String str) {
            this.bodyStr = str;
            this.headers.put("Content-type", ClientLogConstant.NORMAL_TYPE_VALUE);
        }

        public String getBodyStr() {
            return this.bodyStr;
        }

        public String toString() {
            return ((((((((((("" + String.format("id=%s\n", this.id)) + String.format("bSync=%s\n", this.bSync)) + String.format("method=%s\n", this.method)) + String.format("url=%s\n", this.url)) + String.format("headers=%s\n", this.headers)) + String.format("bodyStr=%s\n", this.bodyStr)) + String.format("bodyPairs=%s\n", this.bodyPairs)) + String.format("keyRSA=%s\n", this.keyRSA)) + String.format("transParam=%s\n", this.transParam)) + String.format("connectionTimeout=%s\n", Integer.valueOf(this.connectionTimeout))) + String.format("soTimeout=%s\n", Integer.valueOf(this.soTimeout))) + String.format("leftRetry=%s\n", Integer.valueOf(this.leftRetry));
        }

        public String marshal() throws JSONException {
            TreeMap treeMap = new TreeMap();
            treeMap.put(ResIdReader.RES_TYPE_ID, this.id);
            treeMap.put("bSync", this.bSync);
            treeMap.put("method", this.method);
            treeMap.put("url", this.url);
            treeMap.put("bodyStr", this.bodyStr);
            treeMap.put("keyRSA", this.keyRSA);
            treeMap.put("transParam", this.transParam);
            treeMap.put("connectionTimeout", Integer.valueOf(this.connectionTimeout));
            treeMap.put("soTimeout", Integer.valueOf(this.soTimeout));
            treeMap.put("leftRetry", Integer.valueOf(this.leftRetry));
            Map<String, String> map = this.headers;
            if (map != null && map.size() > 0) {
                treeMap.put("headers", this.headers);
            }
            Map<String, String> map2 = this.bodyPairs;
            if (map2 != null && map2.size() > 0) {
                treeMap.put("bodyPairs", this.bodyPairs);
            }
            return StrUtil.mapToJson(treeMap).toString();
        }

        public void unmarshal(String str) throws JSONException {
            Map<String, Object> mapJsonToMapSet = StrUtil.jsonToMapSet(str);
            UniSdkUtils.d(TAG, "unmarshal strJson=".concat(String.valueOf(str)));
            UniSdkUtils.d(TAG, "unmarshal map=".concat(String.valueOf(mapJsonToMapSet)));
            if (mapJsonToMapSet.containsKey(ResIdReader.RES_TYPE_ID)) {
                this.id = (String) mapJsonToMapSet.get(ResIdReader.RES_TYPE_ID);
            }
            if (mapJsonToMapSet.containsKey("bSync")) {
                this.bSync = (Boolean) mapJsonToMapSet.get("bSync");
            }
            if (mapJsonToMapSet.containsKey("method")) {
                this.method = (String) mapJsonToMapSet.get("method");
            }
            if (mapJsonToMapSet.containsKey("url")) {
                this.url = (String) mapJsonToMapSet.get("url");
            }
            if (mapJsonToMapSet.containsKey("bodyStr")) {
                this.bodyStr = (String) mapJsonToMapSet.get("bodyStr");
            }
            if (mapJsonToMapSet.containsKey("keyRSA")) {
                this.keyRSA = (String) mapJsonToMapSet.get("keyRSA");
            }
            if (mapJsonToMapSet.containsKey("transParam")) {
                this.transParam = (String) mapJsonToMapSet.get("transParam");
            }
            if (mapJsonToMapSet.containsKey("connectionTimeout")) {
                this.connectionTimeout = ((Integer) mapJsonToMapSet.get("connectionTimeout")).intValue();
            }
            if (mapJsonToMapSet.containsKey("soTimeout")) {
                this.soTimeout = ((Integer) mapJsonToMapSet.get("soTimeout")).intValue();
            }
            if (mapJsonToMapSet.containsKey("leftRetry")) {
                this.leftRetry = ((Integer) mapJsonToMapSet.get("leftRetry")).intValue();
            }
            if (mapJsonToMapSet.containsKey("headers")) {
                this.headers = (TreeMap) mapJsonToMapSet.get("headers");
            }
            if (mapJsonToMapSet.containsKey("bodyPairs")) {
                this.bodyPairs = (TreeMap) mapJsonToMapSet.get("bodyPairs");
            }
        }
    }

    public static QueueItem NewQueueItem() {
        return new QueueItem();
    }

    public synchronized void init(Context context, HTTPCallback hTTPCallback) {
        UniSdkUtils.i(this.TAG, "init, ctx=".concat(String.valueOf(context)));
        if (this.m_context != null) {
            this.m_context = context;
            UniSdkUtils.w(this.TAG, "set context again");
        } else {
            this.m_context = context;
            this.m_callback = hTTPCallback;
            getSharedPref();
        }
    }

    public synchronized void close() {
        UniSdkUtils.i(this.TAG, ControlCmdType.CLOSE);
        String str = "";
        try {
            LinkedBlockingQueue<Runnable> linkedBlockingQueue = this.linkedQueue;
            if (linkedBlockingQueue != null && !linkedBlockingQueue.isEmpty()) {
                Iterator<Runnable> it = this.linkedQueue.iterator();
                while (it.hasNext()) {
                    QueueItem queueItem = ((QueueItemRunnable) it.next()).mItem;
                    if (!TextUtils.isEmpty(str)) {
                        str = str + ",";
                    }
                    str = str + queueItem.id;
                }
                if (!TextUtils.isEmpty(str) && this.m_context != null) {
                    String string = getSharedPref().getString(KEY_QUEUE_RESEND, "");
                    if (!TextUtils.isEmpty(string)) {
                        string = string + ",";
                    }
                    getSharedPref().edit().putString(KEY_QUEUE_RESEND, string + str).commit();
                }
                this.linkedQueue.clear();
            }
            ExecutorService executorService = this.m_executorService;
            if (executorService != null) {
                executorService.shutdownNow();
            }
        } catch (Exception e) {
            UniSdkUtils.d(this.TAG, "close exception\uff1a" + e.getMessage());
        }
    }

    public static void clear() {
        Map<String, HTTPQueue> map = s_instances;
        if (map != null) {
            map.clear();
            s_instances = null;
        }
        if (s_lock != null) {
            s_lock = null;
        }
    }

    public synchronized void checkResend() {
        UniSdkUtils.i(this.TAG, "checkResend");
        if (this.m_context == null) {
            return;
        }
        ExecutorService executorService = this.m_executorService;
        if (executorService != null && !executorService.isShutdown() && !this.m_executorService.isTerminated()) {
            this.m_executorService.execute(new Runnable() { // from class: com.netease.ntunisdk.base.utils.HTTPQueue.1
                AnonymousClass1() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    HTTPQueue.this.checkResendByThread();
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.utils.HTTPQueue$1 */
    class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            HTTPQueue.this.checkResendByThread();
        }
    }

    public synchronized void checkResendDelay(String str, String str2) {
        UniSdkUtils.i(this.TAG, "checkResendDelay");
        if (this.m_context == null) {
            return;
        }
        ExecutorService executorService = this.m_executorService;
        if (executorService != null && !executorService.isShutdown() && !this.m_executorService.isTerminated()) {
            this.m_executorService.execute(new Runnable() { // from class: com.netease.ntunisdk.base.utils.HTTPQueue.2
                final /* synthetic */ String val$newUrl;
                final /* synthetic */ String val$tag;

                AnonymousClass2(String str22, String str3) {
                    str = str22;
                    str = str3;
                }

                @Override // java.lang.Runnable
                public void run() {
                    String string = HTTPQueue.this.getSharedPref().getString("resend_" + str + "_delay", "");
                    HTTPQueue.this.getSharedPref().edit().putString("resend_" + str + "_delay", "").commit();
                    if (TextUtils.isEmpty(string)) {
                        return;
                    }
                    for (String str3 : string.split(",")) {
                        String string2 = HTTPQueue.this.getSharedPref().getString(HTTPQueue.KEY_QUEUE_ITEM_PREFIX.concat(String.valueOf(str3)), "");
                        if (!TextUtils.isEmpty(string2)) {
                            QueueItem queueItemNewQueueItem = HTTPQueue.NewQueueItem();
                            try {
                                queueItemNewQueueItem.unmarshal(string2);
                                queueItemNewQueueItem.url = str;
                                if (TextUtils.isEmpty(queueItemNewQueueItem.method)) {
                                    UniSdkUtils.d(HTTPQueue.this.TAG, "checkResendDelay, item.method empty, ship");
                                } else if (!"POST".equalsIgnoreCase(queueItemNewQueueItem.method) || !TextUtils.isEmpty(queueItemNewQueueItem.bodyStr) || (queueItemNewQueueItem.bodyPairs != null && !queueItemNewQueueItem.bodyPairs.isEmpty())) {
                                    UniSdkUtils.d(HTTPQueue.this.TAG, "checkResendDelay, itemId=" + queueItemNewQueueItem.id);
                                    ApiRequestUtil.modifySecureHeader(queueItemNewQueueItem);
                                    HTTPQueue.this.addItem(queueItemNewQueueItem);
                                } else {
                                    UniSdkUtils.d(HTTPQueue.this.TAG, "checkResendDelay, item null, ship");
                                    HTTPQueue.this.getSharedPref().edit().remove(HTTPQueue.KEY_QUEUE_ITEM_PREFIX + queueItemNewQueueItem.id).commit();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.utils.HTTPQueue$2 */
    class AnonymousClass2 implements Runnable {
        final /* synthetic */ String val$newUrl;
        final /* synthetic */ String val$tag;

        AnonymousClass2(String str22, String str3) {
            str = str22;
            str = str3;
        }

        @Override // java.lang.Runnable
        public void run() {
            String string = HTTPQueue.this.getSharedPref().getString("resend_" + str + "_delay", "");
            HTTPQueue.this.getSharedPref().edit().putString("resend_" + str + "_delay", "").commit();
            if (TextUtils.isEmpty(string)) {
                return;
            }
            for (String str3 : string.split(",")) {
                String string2 = HTTPQueue.this.getSharedPref().getString(HTTPQueue.KEY_QUEUE_ITEM_PREFIX.concat(String.valueOf(str3)), "");
                if (!TextUtils.isEmpty(string2)) {
                    QueueItem queueItemNewQueueItem = HTTPQueue.NewQueueItem();
                    try {
                        queueItemNewQueueItem.unmarshal(string2);
                        queueItemNewQueueItem.url = str;
                        if (TextUtils.isEmpty(queueItemNewQueueItem.method)) {
                            UniSdkUtils.d(HTTPQueue.this.TAG, "checkResendDelay, item.method empty, ship");
                        } else if (!"POST".equalsIgnoreCase(queueItemNewQueueItem.method) || !TextUtils.isEmpty(queueItemNewQueueItem.bodyStr) || (queueItemNewQueueItem.bodyPairs != null && !queueItemNewQueueItem.bodyPairs.isEmpty())) {
                            UniSdkUtils.d(HTTPQueue.this.TAG, "checkResendDelay, itemId=" + queueItemNewQueueItem.id);
                            ApiRequestUtil.modifySecureHeader(queueItemNewQueueItem);
                            HTTPQueue.this.addItem(queueItemNewQueueItem);
                        } else {
                            UniSdkUtils.d(HTTPQueue.this.TAG, "checkResendDelay, item null, ship");
                            HTTPQueue.this.getSharedPref().edit().remove(HTTPQueue.KEY_QUEUE_ITEM_PREFIX + queueItemNewQueueItem.id).commit();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public synchronized void checkResendByThread() {
        String string = getSharedPref().getString(KEY_QUEUE_RESEND, "");
        getSharedPref().edit().putString(KEY_QUEUE_RESEND, "").commit();
        if (!TextUtils.isEmpty(string)) {
            for (String str : string.split(",")) {
                String string2 = getSharedPref().getString(KEY_QUEUE_ITEM_PREFIX.concat(String.valueOf(str)), "");
                if (!TextUtils.isEmpty(string2)) {
                    QueueItem queueItemNewQueueItem = NewQueueItem();
                    try {
                        queueItemNewQueueItem.unmarshal(string2);
                        if (TextUtils.isEmpty(queueItemNewQueueItem.method)) {
                            UniSdkUtils.d(this.TAG, "checkResend, item.method empty, ship");
                        } else if ("POST".equalsIgnoreCase(queueItemNewQueueItem.method) && TextUtils.isEmpty(queueItemNewQueueItem.bodyStr) && (queueItemNewQueueItem.bodyPairs == null || queueItemNewQueueItem.bodyPairs.isEmpty())) {
                            UniSdkUtils.d(this.TAG, "checkResend, item null, ship");
                            getSharedPref().edit().remove(KEY_QUEUE_ITEM_PREFIX + queueItemNewQueueItem.id).commit();
                        } else {
                            UniSdkUtils.d(this.TAG, "checkResend, itemId=" + queueItemNewQueueItem.id);
                            ApiRequestUtil.modifySecureHeader(queueItemNewQueueItem);
                            addItem(queueItemNewQueueItem);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public SharedPreferences getSharedPref() {
        if (this.sp == null) {
            synchronized (this.obj) {
                this.sp = this.m_context.getSharedPreferences("HTTPQ_" + this.m_name, 0);
            }
        }
        return this.sp;
    }

    public void post(String str, String str2, Boolean bool, Map<String, String> map) {
        UniSdkUtils.i(this.TAG, String.format("post, url=%s, bodyStr=%s", str, str2));
        QueueItem queueItemNewQueueItem = NewQueueItem();
        queueItemNewQueueItem.method = "POST";
        queueItemNewQueueItem.url = str;
        queueItemNewQueueItem.setBody(str2);
        queueItemNewQueueItem.bSync = bool;
        queueItemNewQueueItem.setHeaders(map);
        addItem(queueItemNewQueueItem);
    }

    public void post(String str, Map<String, String> map, Boolean bool, Map<String, String> map2) {
        UniSdkUtils.i(this.TAG, String.format("post, url=%s, bodyPairs=%s", str, map));
        QueueItem queueItemNewQueueItem = NewQueueItem();
        queueItemNewQueueItem.method = "POST";
        queueItemNewQueueItem.url = str;
        queueItemNewQueueItem.setBody(map);
        queueItemNewQueueItem.bSync = bool;
        queueItemNewQueueItem.setHeaders(map2);
        addItem(queueItemNewQueueItem);
    }

    public void get(String str, Boolean bool, Map<String, String> map) {
        UniSdkUtils.i(this.TAG, "get, url=".concat(String.valueOf(str)));
        QueueItem queueItemNewQueueItem = NewQueueItem();
        queueItemNewQueueItem.method = "GET";
        queueItemNewQueueItem.url = str;
        queueItemNewQueueItem.bSync = bool;
        queueItemNewQueueItem.setHeaders(map);
        addItem(queueItemNewQueueItem);
    }

    public void get(String str, Map<String, String> map, Boolean bool, Map<String, String> map2) {
        UniSdkUtils.i(this.TAG, String.format("get, url=%s, params=%s", str, map));
        QueueItem queueItemNewQueueItem = NewQueueItem();
        queueItemNewQueueItem.method = "GET";
        if (!str.endsWith("?")) {
            str = str + "?";
        }
        queueItemNewQueueItem.url = str + StrUtil.createLinkString(map, true, true);
        queueItemNewQueueItem.bSync = bool;
        queueItemNewQueueItem.setHeaders(map2);
        addItem(queueItemNewQueueItem);
    }

    public synchronized void addItem(QueueItem queueItem) {
        UniSdkUtils.d(this.TAG, "addItem, item=".concat(String.valueOf(queueItem)));
        LinkedBlockingQueue<Runnable> linkedBlockingQueue = this.linkedQueue;
        if (linkedBlockingQueue != null && linkedBlockingQueue.size() <= 100) {
            ExecutorService executorService = this.m_executorService;
            if (executorService != null) {
                if (!executorService.isShutdown() && !this.m_executorService.isTerminated()) {
                    try {
                        this.m_executorService.execute(new QueueItemRunnable(queueItem));
                        return;
                    } catch (Exception e) {
                        UniSdkUtils.e(this.TAG, "ExecutorService.execute exception:" + e.getMessage());
                        return;
                    }
                }
                UniSdkUtils.e(this.TAG, "ExecutorService have shutdown");
                return;
            }
            UniSdkUtils.e(this.TAG, "ExecutorService null");
            return;
        }
        UniSdkUtils.d(this.TAG, "linkedQueue full");
    }

    public synchronized void addItemDelay(QueueItem queueItem, String str) {
        String strMarshal;
        UniSdkUtils.d(this.TAG, "addItemDelay, item=".concat(String.valueOf(queueItem)));
        try {
            strMarshal = queueItem.marshal();
        } catch (Exception e) {
            e.printStackTrace();
            strMarshal = "";
        }
        UniSdkUtils.d(this.TAG, "item to resend delay:".concat(String.valueOf(strMarshal)));
        if (this.m_context != null) {
            if (!TextUtils.isEmpty(strMarshal)) {
                String string = getSharedPref().getString("resend_" + str + "_delay", "");
                if (!TextUtils.isEmpty(string)) {
                    string = string + ",";
                }
                String str2 = string + queueItem.id;
                getSharedPref().edit().putString("resend_" + str + "_delay", str2);
                getSharedPref().edit().putString(KEY_QUEUE_ITEM_PREFIX + queueItem.id, strMarshal).commit();
                return;
            }
            getSharedPref().edit().remove(KEY_QUEUE_ITEM_PREFIX + queueItem.id).commit();
        }
    }

    public void send(QueueItem queueItem) {
        UniSdkUtils.d(this.TAG, "send itemId:" + queueItem.id);
        try {
            String strMarshal = queueItem.marshal();
            if (!TextUtils.isEmpty(strMarshal) && this.m_context != null) {
                getSharedPref().edit().putString(KEY_QUEUE_ITEM_PREFIX + queueItem.id, strMarshal).commit();
            }
        } catch (JSONException e) {
            UniSdkUtils.d(this.TAG, "item.marshal()\uff1a" + e.getMessage());
        }
        String strHTTPDo = HTTPDo(queueItem);
        if (hasHttpDnsSDK(queueItem.isNeedHttpDns, queueItem.url)) {
            HTTPDnsDo(queueItem);
        } else {
            handleOnCall(queueItem, strHTTPDo);
        }
    }

    private void handleOnCall(QueueItem queueItem, String str) {
        HTTPCallback hTTPCallback = queueItem.callback;
        if (hTTPCallback == null) {
            hTTPCallback = this.m_callback;
        }
        handleResponse(queueItem, hTTPCallback != null ? hTTPCallback.processResult(str, queueItem.transParam) : false);
    }

    private boolean hasHttpDnsSDK(boolean z, String str) {
        return z && HttpDns.getInstance().switchHttpDnsMode(str);
    }

    private boolean isIp(String str) {
        return !TextUtils.isEmpty(str) && str.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
    }

    private void handleResponse(QueueItem queueItem, boolean z) {
        String strMarshal;
        UniSdkUtils.d(this.TAG, String.format("handleResponse, item.id=%s, bResend=%s", queueItem.id, Boolean.valueOf(z)));
        if (!z || queueItem.leftRetry <= 0) {
            if (z && queueItem.leftRetry <= 0) {
                UniSdkUtils.e(this.TAG, "reach max retry limit, give up");
            }
            if (this.m_context != null) {
                getSharedPref().edit().remove(KEY_QUEUE_ITEM_PREFIX + queueItem.id).commit();
                return;
            }
            return;
        }
        queueItem.leftRetry--;
        try {
            strMarshal = queueItem.marshal();
            UniSdkUtils.d(this.TAG, "item to resend:".concat(String.valueOf(strMarshal)));
        } catch (Exception e) {
            e.printStackTrace();
            strMarshal = "";
        }
        if (this.m_context != null) {
            if (!TextUtils.isEmpty(strMarshal)) {
                String string = getSharedPref().getString(KEY_QUEUE_RESEND, "");
                if (!TextUtils.isEmpty(string)) {
                    string = string + ",";
                }
                getSharedPref().edit().putString(KEY_QUEUE_RESEND, string + queueItem.id);
                getSharedPref().edit().putString(KEY_QUEUE_ITEM_PREFIX + queueItem.id, strMarshal).commit();
                return;
            }
            getSharedPref().edit().remove(KEY_QUEUE_ITEM_PREFIX + queueItem.id).commit();
        }
    }

    private void HTTPDnsDo(QueueItem queueItem) {
        String httpResponse;
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        UniSdkUtils.i(this.TAG, "HTTPDnsDo");
        boolean z = queueItem.callback instanceof HTTPCallbackExt;
        httpResponse = "";
        HttpURLConnection httpURLConnection2 = null;
        try {
            try {
                String str = queueItem.url;
                ResolveDnsResult resolveDnsResultResolve = (!HttpDns.getInstance().isHttpDnsMode() || TextUtils.isEmpty(str)) ? null : HttpDns.getInstance().resolve(str);
                if (resolveDnsResultResolve != null && !TextUtils.isEmpty(resolveDnsResultResolve.url)) {
                    str = resolveDnsResultResolve.url;
                }
                URL url = new URL(str);
                if (str.trim().startsWith("https")) {
                    httpURLConnection = (HttpsURLConnection) url.openConnection();
                } else {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                }
                httpURLConnection2 = httpURLConnection;
                if (resolveDnsResultResolve != null) {
                    resolveDnsResultResolve.setSNI(httpURLConnection2);
                }
                httpURLConnection2.setReadTimeout(queueItem.soTimeout);
                httpURLConnection2.setConnectTimeout(queueItem.connectionTimeout);
                if ("POST".equalsIgnoreCase(queueItem.method)) {
                    httpURLConnection2.setRequestMethod("POST");
                    httpURLConnection2.setDoOutput(true);
                } else {
                    httpURLConnection2.setRequestMethod("GET");
                    httpURLConnection2.setUseCaches(false);
                }
                if (queueItem.headers != null && !queueItem.headers.isEmpty()) {
                    for (Map.Entry entry : queueItem.headers.entrySet()) {
                        httpURLConnection2.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
                    }
                }
                if ("POST".equalsIgnoreCase(queueItem.method)) {
                    if (TextUtils.isEmpty(queueItem.bodyStr)) {
                        if (queueItem.bodyPairs != null && queueItem.bodyPairs.size() > 0) {
                            StringBuilder sb = new StringBuilder();
                            for (Map.Entry entry2 : queueItem.bodyPairs.entrySet()) {
                                if (sb.length() > 0) {
                                    sb.append(com.alipay.sdk.m.s.a.l);
                                }
                                sb.append(URLEncoder.encode((String) entry2.getKey(), "UTF-8"));
                                sb.append("=");
                                if (!TextUtils.isEmpty((CharSequence) entry2.getValue())) {
                                    sb.append(URLEncoder.encode((String) entry2.getValue(), "UTF-8"));
                                }
                            }
                            String string = sb.toString();
                            if (!TextUtils.isEmpty(queueItem.keyRSA)) {
                                string = Crypto.rsaEncrypt(string.getBytes("UTF-8"), queueItem.keyRSA);
                                httpURLConnection2.setRequestProperty("Encryption", "rsa");
                            }
                            OutputStream outputStream = httpURLConnection2.getOutputStream();
                            outputStream.write(string.getBytes("UTF-8"));
                            outputStream.flush();
                            outputStream.close();
                        }
                    } else {
                        OutputStream outputStream2 = httpURLConnection2.getOutputStream();
                        outputStream2.write(queueItem.bodyStr.getBytes("UTF-8"));
                        outputStream2.flush();
                        outputStream2.close();
                    }
                }
                int responseCode = httpURLConnection2.getResponseCode();
                UniSdkUtils.d(this.TAG, "HTTPDnsDo httRequst code:".concat(String.valueOf(responseCode)));
                if (z) {
                    ((HTTPCallbackExt) queueItem.callback).responseCode = responseCode;
                    try {
                        inputStream = httpURLConnection2.getInputStream();
                    } catch (Exception unused) {
                        UniSdkUtils.i(this.TAG, "input stream invalid, get error stream instead");
                        inputStream = httpURLConnection2.getErrorStream();
                    }
                } else {
                    inputStream = httpURLConnection2.getInputStream();
                }
                httpResponse = inputStream != null ? readHttpResponse(inputStream) : "";
                UniSdkUtils.d(this.TAG, String.format("HTTPDnsDo, strResp=%s, item=%s", httpResponse, queueItem));
            } catch (Exception e) {
                e.printStackTrace();
                UniSdkUtils.d(this.TAG, "HTTPDnsDo httPost exception:" + e.getMessage());
                if (z) {
                    ((HTTPCallbackExt) queueItem.callback).throwable = e;
                }
                if (httpURLConnection2 != null) {
                }
            }
            handleOnCall(queueItem, httpResponse);
        } finally {
            if (httpURLConnection2 != null) {
                httpURLConnection2.disconnect();
            }
        }
    }

    private String stripIpWithPort(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return str.split(":")[0];
    }

    private String HTTPDo(QueueItem queueItem) {
        String httpResponse;
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        UniSdkUtils.i(this.TAG, "HTTPDo");
        httpResponse = "";
        boolean z = queueItem.callback instanceof HTTPCallbackExt;
        HttpURLConnection httpURLConnection2 = null;
        try {
            try {
                String str = queueItem.url;
                ResolveDnsResult resolveDnsResultResolve = (!HttpDns.getInstance().isHttpDnsMode() || TextUtils.isEmpty(str)) ? null : HttpDns.getInstance().resolve(str);
                if (resolveDnsResultResolve != null && !TextUtils.isEmpty(resolveDnsResultResolve.url)) {
                    str = resolveDnsResultResolve.url;
                }
                URL url = new URL(str);
                queueItem.host = url.getHost();
                if (str.trim().startsWith("https")) {
                    httpURLConnection = (HttpsURLConnection) url.openConnection();
                } else {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                }
                httpURLConnection2 = httpURLConnection;
                if (resolveDnsResultResolve != null) {
                    resolveDnsResultResolve.setSNI(httpURLConnection2);
                }
                httpURLConnection2.setReadTimeout(queueItem.soTimeout);
                httpURLConnection2.setConnectTimeout(queueItem.connectionTimeout);
                if ("POST".equalsIgnoreCase(queueItem.method)) {
                    httpURLConnection2.setRequestMethod("POST");
                    httpURLConnection2.setDoOutput(true);
                } else {
                    httpURLConnection2.setRequestMethod("GET");
                    httpURLConnection2.setUseCaches(false);
                }
                if (queueItem.headers != null && !queueItem.headers.isEmpty()) {
                    for (Map.Entry entry : queueItem.headers.entrySet()) {
                        httpURLConnection2.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
                    }
                }
                if ("POST".equalsIgnoreCase(queueItem.method)) {
                    if (TextUtils.isEmpty(queueItem.bodyStr)) {
                        if (queueItem.bodyPairs != null && queueItem.bodyPairs.size() > 0) {
                            StringBuilder sb = new StringBuilder();
                            for (Map.Entry entry2 : queueItem.bodyPairs.entrySet()) {
                                if (sb.length() > 0) {
                                    sb.append(com.alipay.sdk.m.s.a.l);
                                }
                                sb.append(URLEncoder.encode((String) entry2.getKey(), "UTF-8"));
                                sb.append("=");
                                if (!TextUtils.isEmpty((CharSequence) entry2.getValue())) {
                                    sb.append(URLEncoder.encode((String) entry2.getValue(), "UTF-8"));
                                }
                            }
                            String string = sb.toString();
                            if (!TextUtils.isEmpty(queueItem.keyRSA)) {
                                string = Crypto.rsaEncrypt(string.getBytes("UTF-8"), queueItem.keyRSA);
                                httpURLConnection2.setRequestProperty("Encryption", "rsa");
                            }
                            OutputStream outputStream = httpURLConnection2.getOutputStream();
                            outputStream.write(string.getBytes("UTF-8"));
                            outputStream.flush();
                            outputStream.close();
                        }
                    } else {
                        OutputStream outputStream2 = httpURLConnection2.getOutputStream();
                        outputStream2.write(queueItem.bodyStr.getBytes("UTF-8"));
                        outputStream2.flush();
                        outputStream2.close();
                    }
                }
                int responseCode = httpURLConnection2.getResponseCode();
                queueItem.isNeedHttpDns = responseCode != 585 && String.valueOf(responseCode).startsWith("5");
                UniSdkUtils.d(this.TAG, "httRequst code:".concat(String.valueOf(responseCode)));
                if (z) {
                    ((HTTPCallbackExt) queueItem.callback).responseCode = responseCode;
                    try {
                        inputStream = httpURLConnection2.getInputStream();
                    } catch (Exception unused) {
                        UniSdkUtils.i(this.TAG, "input stream invalid, get error stream instead");
                        inputStream = httpURLConnection2.getErrorStream();
                    }
                } else {
                    inputStream = httpURLConnection2.getInputStream();
                }
                httpResponse = inputStream != null ? readHttpResponse(inputStream) : "";
            } catch (Exception e) {
                e.printStackTrace();
                UniSdkUtils.d(this.TAG, "httPost exception:" + e.getMessage());
                if (z) {
                    ((HTTPCallbackExt) queueItem.callback).throwable = e;
                }
                queueItem.isNeedHttpDns = true;
                if (httpURLConnection2 != null) {
                }
            }
            UniSdkUtils.d(this.TAG, String.format("HTTPDo, strResp=%s, item=%s", httpResponse, queueItem));
            return httpResponse;
        } finally {
            if (httpURLConnection2 != null) {
                httpURLConnection2.disconnect();
            }
        }
    }

    private String readHttpResponse(InputStream inputStream) throws IOException {
        String string = "";
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[2048];
            while (true) {
                int i = inputStream.read(bArr, 0, 2048);
                if (i != -1) {
                    byteArrayOutputStream.write(bArr, 0, i);
                } else {
                    byteArrayOutputStream.flush();
                    string = byteArrayOutputStream.toString("UTF-8");
                    inputStream.close();
                    byteArrayOutputStream.close();
                    return string;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            UniSdkUtils.d(this.TAG, "readHttpResponse exception:" + e.getMessage());
            return string;
        }
    }

    class QueueItemRunnable implements Runnable {
        public QueueItem mItem;

        public QueueItemRunnable(QueueItem queueItem) {
            this.mItem = queueItem;
        }

        @Override // java.lang.Runnable
        public void run() {
            HTTPQueue.this.send(this.mItem);
        }
    }
}