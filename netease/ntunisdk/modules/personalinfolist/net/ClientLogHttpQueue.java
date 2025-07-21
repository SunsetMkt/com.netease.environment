package com.netease.ntunisdk.modules.personalinfolist.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import androidx.browser.trusted.sharing.ShareTarget;
import com.CCMsgSdk.ControlCmdType;
import com.alipay.sdk.m.s.a;
import com.netease.ntunisdk.modules.base.utils.LogModule;
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
import java.util.concurrent.locks.ReentrantLock;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;

/* loaded from: classes.dex */
public class ClientLogHttpQueue {
    private static final int CAPACITY = 100;
    public static final int CONNECTION_TIMEOUT = 15000;
    public static final String HTTPQUEUE_CLIENTLOGREPORTER = "ClientLogReporter";
    private static final String KEY_QUEUE_ITEM_PREFIX = "PersonalInfoListModule_item_";
    private static final String KEY_QUEUE_RESEND = "PersonalInfoListModule_resend";
    private static final int MAX_RETRY = 5;
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final int SO_TIMEOUT = 15000;
    private static Map<String, ClientLogHttpQueue> s_instances = new TreeMap();
    private static ReentrantLock s_lock = new ReentrantLock();
    private LinkedBlockingQueue<Runnable> linkedQueue;
    private ClientLogHttpCallback m_callback;
    private Context m_context;
    private ExecutorService m_executorService;
    private String m_name;
    private SharedPreferences sp;
    private String TAG = "PersonalInfoListModule HTTPQ_";
    private Object obj = new Object();

    public void onNetworkConnected() {
    }

    public void setCapacity(int i) {
    }

    public ClientLogHttpQueue(String str, int i) {
        this.TAG += str;
        this.m_name = str;
        this.linkedQueue = new LinkedBlockingQueue<>();
        this.m_executorService = new ThreadPoolExecutor(1, 3, 5000L, TimeUnit.MILLISECONDS, this.linkedQueue);
    }

    public static ClientLogHttpQueue getInstance(String str) {
        if (s_lock == null) {
            s_lock = new ReentrantLock();
        }
        s_lock.lock();
        if (s_instances == null) {
            s_instances = new TreeMap();
        }
        ClientLogHttpQueue clientLogHttpQueue = s_instances.containsKey(str) ? s_instances.get(str) : null;
        if (clientLogHttpQueue == null) {
            clientLogHttpQueue = new ClientLogHttpQueue(str, 0);
            s_instances.put(str, clientLogHttpQueue);
        }
        s_lock.unlock();
        return clientLogHttpQueue;
    }

    public static class QueueItem {
        private static final String TAG = "UniSDK_QueueItem";
        private static int s_index;
        public Boolean bSync;
        private Map<String, String> bodyPairs;
        private String bodyStr;
        public ClientLogHttpCallback callback;
        public int connectionTimeout;
        private Map<String, String> headers;
        public String id;
        public String keyRSA;
        public int leftRetry;
        public String method;
        public int soTimeout;
        public String strResp;
        public String transParam;
        public String url;

        private QueueItem() {
            this.bSync = true;
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
            s_index++;
            if (s_index >= 10000) {
                s_index = 1;
            }
            this.id = System.currentTimeMillis() + "_" + String.format(Locale.US, "%d", Integer.valueOf(s_index));
        }

        public void setHeaders(Map<String, String> map) {
            if (map != null) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    this.headers.put(entry.getKey(), entry.getValue());
                }
            }
        }

        public void setBody(Map<String, String> map) {
            this.bodyPairs = map;
            this.headers.put("Content-type", ShareTarget.ENCODING_TYPE_URL_ENCODED);
        }

        public void setBody(String str) {
            this.bodyStr = str;
            this.headers.put("Content-type", ClientLogConstant.NORMAL_TYPE_VALUE);
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
            LogModule.d(TAG, "unmarshal strJson=" + str);
            LogModule.d(TAG, "unmarshal map=" + mapJsonToMapSet);
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

    public synchronized void init(Context context, ClientLogHttpCallback clientLogHttpCallback) {
        LogModule.i(this.TAG, "init, ctx=" + context);
        if (this.m_context != null) {
            this.m_context = context;
            LogModule.w(this.TAG, "set context again");
        } else {
            this.m_context = context;
            this.m_callback = clientLogHttpCallback;
            getSharedPref();
        }
    }

    public synchronized void close() {
        LogModule.i(this.TAG, ControlCmdType.CLOSE);
        String str = "";
        try {
            if (this.linkedQueue != null && !this.linkedQueue.isEmpty()) {
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
            if (this.m_executorService != null) {
                this.m_executorService.shutdownNow();
            }
        } catch (Exception e) {
            LogModule.d(this.TAG, "close exception\uff1a" + e.getMessage());
        }
    }

    public static void clear() {
        Map<String, ClientLogHttpQueue> map = s_instances;
        if (map != null) {
            map.clear();
            s_instances = null;
        }
        if (s_lock != null) {
            s_lock = null;
        }
    }

    public synchronized void checkResend() {
        LogModule.i(this.TAG, "checkResend");
        if (this.m_context == null) {
            return;
        }
        if (this.m_executorService != null && !this.m_executorService.isShutdown() && !this.m_executorService.isTerminated()) {
            new Thread(new Runnable() { // from class: com.netease.ntunisdk.modules.personalinfolist.net.ClientLogHttpQueue.1
                @Override // java.lang.Runnable
                public void run() {
                    final String string = ClientLogHttpQueue.this.getSharedPref().getString(ClientLogHttpQueue.KEY_QUEUE_RESEND, "");
                    ClientLogHttpQueue.this.getSharedPref().edit().putString(ClientLogHttpQueue.KEY_QUEUE_RESEND, "").commit();
                    if (TextUtils.isEmpty(string)) {
                        return;
                    }
                    ClientLogHttpQueue.this.m_executorService.execute(new Runnable() { // from class: com.netease.ntunisdk.modules.personalinfolist.net.ClientLogHttpQueue.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            for (String str : string.split(",")) {
                                String string2 = ClientLogHttpQueue.this.getSharedPref().getString(ClientLogHttpQueue.KEY_QUEUE_ITEM_PREFIX + str, "");
                                if (!TextUtils.isEmpty(string2)) {
                                    QueueItem queueItemNewQueueItem = ClientLogHttpQueue.NewQueueItem();
                                    try {
                                        queueItemNewQueueItem.unmarshal(string2);
                                        if (TextUtils.isEmpty(queueItemNewQueueItem.method)) {
                                            LogModule.d(ClientLogHttpQueue.this.TAG, "checkResend, item.method empty, ship");
                                        } else if (!"POST".equalsIgnoreCase(queueItemNewQueueItem.method) || !TextUtils.isEmpty(queueItemNewQueueItem.bodyStr) || (queueItemNewQueueItem.bodyPairs != null && !queueItemNewQueueItem.bodyPairs.isEmpty())) {
                                            LogModule.d(ClientLogHttpQueue.this.TAG, "checkResend, itemId=" + queueItemNewQueueItem.id);
                                            ClientLogHttpQueue.this.addItem(queueItemNewQueueItem);
                                        } else {
                                            LogModule.d(ClientLogHttpQueue.this.TAG, "checkResend, item null, ship");
                                            ClientLogHttpQueue.this.getSharedPref().edit().remove(ClientLogHttpQueue.KEY_QUEUE_ITEM_PREFIX + queueItemNewQueueItem.id).commit();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                }
            }).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SharedPreferences getSharedPref() {
        if (this.sp == null) {
            synchronized (this.obj) {
                this.sp = this.m_context.getSharedPreferences("HTTPQ_" + this.m_name, 0);
            }
        }
        return this.sp;
    }

    public void post(String str, String str2, Boolean bool, Map<String, String> map) {
        LogModule.i(this.TAG, String.format("post, url=%s, bodyStr=%s", str, str2));
        QueueItem queueItemNewQueueItem = NewQueueItem();
        queueItemNewQueueItem.method = "POST";
        queueItemNewQueueItem.url = str;
        queueItemNewQueueItem.setBody(str2);
        queueItemNewQueueItem.bSync = bool;
        queueItemNewQueueItem.setHeaders(map);
        addItem(queueItemNewQueueItem);
    }

    public void post(String str, Map<String, String> map, Boolean bool, Map<String, String> map2) {
        LogModule.i(this.TAG, String.format("post, url=%s, bodyPairs=%s", str, map));
        QueueItem queueItemNewQueueItem = NewQueueItem();
        queueItemNewQueueItem.method = "POST";
        queueItemNewQueueItem.url = str;
        queueItemNewQueueItem.setBody(map);
        queueItemNewQueueItem.bSync = bool;
        queueItemNewQueueItem.setHeaders(map2);
        addItem(queueItemNewQueueItem);
    }

    public void get(String str, Boolean bool, Map<String, String> map) {
        LogModule.i(this.TAG, "get, url=" + str);
        QueueItem queueItemNewQueueItem = NewQueueItem();
        queueItemNewQueueItem.method = "GET";
        queueItemNewQueueItem.url = str;
        queueItemNewQueueItem.bSync = bool;
        queueItemNewQueueItem.setHeaders(map);
        addItem(queueItemNewQueueItem);
    }

    public void get(String str, Map<String, String> map, Boolean bool, Map<String, String> map2) {
        LogModule.i(this.TAG, String.format("get, url=%s, params=%s", str, map));
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
        LogModule.d(this.TAG, "addItem, item=" + queueItem);
        if (this.linkedQueue != null && this.linkedQueue.size() <= 100) {
            if (this.m_executorService != null) {
                if (!this.m_executorService.isShutdown() && !this.m_executorService.isTerminated()) {
                    try {
                        this.m_executorService.execute(new QueueItemRunnable(queueItem));
                    } catch (Exception e) {
                        LogModule.e(this.TAG, "ExecutorService.execute exception:" + e.getMessage());
                    }
                } else {
                    LogModule.e(this.TAG, "ExecutorService have shutdown");
                }
            } else {
                LogModule.e(this.TAG, "ExecutorService null");
            }
        } else {
            LogModule.d(this.TAG, "linkedQueue full");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void send(QueueItem queueItem) {
        LogModule.d(this.TAG, "send itemId:" + queueItem.id);
        try {
            String strMarshal = queueItem.marshal();
            if (!TextUtils.isEmpty(strMarshal) && this.m_context != null) {
                getSharedPref().edit().putString(KEY_QUEUE_ITEM_PREFIX + queueItem.id, strMarshal).commit();
            }
        } catch (JSONException e) {
            LogModule.d(this.TAG, "item.marshal()\uff1a" + e.getMessage());
        }
        String strHTTPDo = HTTPDo(queueItem);
        ClientLogHttpCallback clientLogHttpCallback = queueItem.callback;
        if (clientLogHttpCallback == null) {
            clientLogHttpCallback = this.m_callback;
        }
        handleResponse(queueItem, clientLogHttpCallback != null ? clientLogHttpCallback.processResult(strHTTPDo, queueItem.transParam) : false);
    }

    private void handleResponse(QueueItem queueItem, boolean z) {
        String strMarshal;
        LogModule.d(this.TAG, String.format("handleResponse, item.id=%s, bResend=%s", queueItem.id, Boolean.valueOf(z)));
        if (!z || queueItem.leftRetry <= 0) {
            if (z && queueItem.leftRetry <= 0) {
                LogModule.e(this.TAG, "reach max retry limit, give up");
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
            LogModule.d(this.TAG, "item to resend:" + strMarshal);
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
                getSharedPref().edit().putString(KEY_QUEUE_RESEND, string + queueItem.id).commit();
                getSharedPref().edit().putString(KEY_QUEUE_ITEM_PREFIX + queueItem.id, strMarshal).commit();
                return;
            }
            getSharedPref().edit().remove(KEY_QUEUE_ITEM_PREFIX + queueItem.id).commit();
        }
    }

    private String HTTPDo(QueueItem queueItem) {
        String httpResponse;
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        LogModule.i(this.TAG, "HTTPDo");
        httpResponse = "";
        boolean z = queueItem.callback instanceof ClientLogHttpCallbackExt;
        HttpURLConnection httpURLConnection2 = null;
        try {
            try {
                URL url = new URL(queueItem.url);
                if (queueItem.url.trim().startsWith("https")) {
                    httpURLConnection = (HttpsURLConnection) url.openConnection();
                } else {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                }
                httpURLConnection2 = httpURLConnection;
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
                                    sb.append(a.l);
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
                LogModule.d(this.TAG, "httRequst code:" + responseCode);
                if (z) {
                    ((ClientLogHttpCallbackExt) queueItem.callback).responseCode = responseCode;
                    try {
                        inputStream = httpURLConnection2.getInputStream();
                    } catch (Exception unused) {
                        LogModule.d(this.TAG, "input stream invalid, get error stream instead");
                        inputStream = httpURLConnection2.getErrorStream();
                    }
                } else {
                    inputStream = httpURLConnection2.getInputStream();
                }
                httpResponse = inputStream != null ? readHttpResponse(inputStream) : "";
            } finally {
                if (httpURLConnection2 != null) {
                    httpURLConnection2.disconnect();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogModule.d(this.TAG, "httPost exception:" + e.getMessage());
            if (z) {
                ((ClientLogHttpCallbackExt) queueItem.callback).throwable = e;
            }
            if (httpURLConnection2 != null) {
            }
        }
        LogModule.d(this.TAG, String.format("HTTPDo, strResp=%s, item=%s", httpResponse, queueItem));
        return httpResponse;
    }

    private String readHttpResponse(InputStream inputStream) throws IOException {
        String string = "";
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[2048];
            while (true) {
                int i = inputStream.read(bArr, 0, bArr.length);
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
            LogModule.d(this.TAG, "readHttpResponse exception:" + e.getMessage());
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
            ClientLogHttpQueue.this.send(this.mItem);
        }
    }
}