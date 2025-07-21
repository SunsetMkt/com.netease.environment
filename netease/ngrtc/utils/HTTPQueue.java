package com.netease.ngrtc.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import androidx.browser.trusted.sharing.ShareTarget;
import com.CCMsgSdk.ControlCmdType;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

/* loaded from: classes5.dex */
public class HTTPQueue {
    private static final int CAPACITY = 200;
    public static final int CONNECTION_TIMEOUT = 5000;
    private static final String KEY_QUEUE_ITEM_PREFIX = "item_";
    private static final String KEY_QUEUE_RESEND = "resend";
    private static final int MAX_RETRY = 20;
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final int SO_TIMEOUT = 10000;
    private static Map<String, HTTPQueue> s_instances = new TreeMap();
    private static ReentrantLock s_lock = new ReentrantLock();
    private String TAG;
    private HTTPCallback m_callback;
    private int m_capacity;
    private Context m_context;
    private String m_name;
    private Condition m_notEmpty;
    private List<QueueItem> m_queue = new ArrayList();
    private ReentrantLock m_lock = new ReentrantLock();
    private ReentrantReadWriteLock m_lockPref = new ReentrantReadWriteLock();
    private Boolean m_bClosed = false;

    public HTTPQueue(String str, int i) {
        this.TAG = "HTTPQ_";
        this.m_capacity = 200;
        this.TAG = String.valueOf(this.TAG) + str;
        this.m_name = str;
        if (i > 0) {
            this.m_capacity = i;
        }
    }

    public static HTTPQueue getInstance(String str) {
        s_lock.lock();
        HTTPQueue hTTPQueue = s_instances.containsKey(str) ? s_instances.get(str) : null;
        if (hTTPQueue == null) {
            hTTPQueue = new HTTPQueue(str, 200);
            s_instances.put(str, hTTPQueue);
        }
        s_lock.unlock();
        return hTTPQueue;
    }

    public static class QueueItem {
        private static final String TAG = "UniSDK_QueueItem";
        private static int s_index;
        public Boolean bSync;
        private List<NameValuePair> bodyPairs;
        private String bodyStr;
        public HTTPCallback callback;
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
            this.bodyPairs = new ArrayList();
            this.keyRSA = "";
            this.strResp = "";
            this.transParam = "";
            this.connectionTimeout = 5000;
            this.soTimeout = 10000;
            this.id = "";
            this.leftRetry = 20;
            int i = s_index + 1;
            s_index = i;
            if (i >= 1000) {
                s_index = 0;
            }
            this.id = String.valueOf(System.currentTimeMillis()) + "_" + String.format("%03d", Integer.valueOf(s_index));
        }

        /* synthetic */ QueueItem(QueueItem queueItem) {
            this();
        }

        public void setHeaders(Map<String, String> map) {
            if (map != null) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    this.headers.put(entry.getKey(), entry.getValue());
                }
            }
        }

        public void setBody(Map<String, String> map) {
            try {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    this.bodyPairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.headers.put("Content-type", ShareTarget.ENCODING_TYPE_URL_ENCODED);
        }

        public void setBody(String str) {
            this.bodyStr = str;
            this.headers.put("Content-type", ClientLogConstant.NORMAL_TYPE_VALUE);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(String.valueOf("" + String.format("id=%s\n", this.id)));
            sb.append(String.format("bSync=%s\n", this.bSync));
            StringBuilder sb2 = new StringBuilder(String.valueOf(String.valueOf(sb.toString()) + String.format("method=%s\n", this.method)));
            sb2.append(String.format("url=%s\n", this.url));
            StringBuilder sb3 = new StringBuilder(String.valueOf(String.valueOf(sb2.toString()) + String.format("headers=%s\n", this.headers)));
            sb3.append(String.format("bodyStr=%s\n", this.bodyStr));
            StringBuilder sb4 = new StringBuilder(String.valueOf(String.valueOf(sb3.toString()) + String.format("bodyPairs=%s\n", this.bodyPairs)));
            sb4.append(String.format("keyRSA=%s\n", this.keyRSA));
            StringBuilder sb5 = new StringBuilder(String.valueOf(String.valueOf(sb4.toString()) + String.format("transParam=%s\n", this.transParam)));
            sb5.append(String.format("connectionTimeout=%s\n", Integer.valueOf(this.connectionTimeout)));
            return String.valueOf(String.valueOf(sb5.toString()) + String.format("soTimeout=%s\n", Integer.valueOf(this.soTimeout))) + String.format("leftRetry=%s\n", Integer.valueOf(this.leftRetry));
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
            List<NameValuePair> list = this.bodyPairs;
            if (list != null && list.size() > 0) {
                treeMap.put("bodyPairs", StrUtil.nameValuePairsToMap(this.bodyPairs));
            }
            return StrUtil.mapToJson(treeMap).toString();
        }

        public void unmarshal(String str) throws JSONException {
            Map<String, Object> mapJsonToMapSet = StrUtil.jsonToMapSet(str);
            Log.d(TAG, "unmarshal strJson=" + str);
            Log.d(TAG, "unmarshal map=" + mapJsonToMapSet);
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
                this.bodyPairs = StrUtil.mapToNameValuePairs((TreeMap) mapJsonToMapSet.get("bodyPairs"));
            }
        }
    }

    public static QueueItem NewQueueItem() {
        return new QueueItem(null);
    }

    public void init(Context context, HTTPCallback hTTPCallback) {
        Log.i(this.TAG, "init, ctx=" + context);
        if (this.m_context != null) {
            this.m_context = context;
            Log.w(this.TAG, "set context again");
            return;
        }
        this.m_context = context;
        this.m_callback = hTTPCallback;
        this.m_notEmpty = this.m_lock.newCondition();
        new Thread(new Runnable() { // from class: com.netease.ngrtc.utils.HTTPQueue.1
            @Override // java.lang.Runnable
            public void run() {
                HTTPQueue.this.send();
            }
        }).start();
        checkResend();
    }

    public void close() {
        Log.i(this.TAG, ControlCmdType.CLOSE);
        this.m_lock.lock();
        if (this.m_bClosed.booleanValue()) {
            this.m_lock.unlock();
            return;
        }
        this.m_bClosed = true;
        this.m_lock.unlock();
        this.m_lock.lock();
        String str = "";
        for (QueueItem queueItem : this.m_queue) {
            if (!TextUtils.isEmpty(str)) {
                str = String.valueOf(str) + ",";
            }
            str = String.valueOf(str) + queueItem.id;
        }
        this.m_queue.clear();
        this.m_lock.unlock();
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.m_lockPref.writeLock().lock();
        String string = getSharedPref().getString(KEY_QUEUE_RESEND, "");
        if (!TextUtils.isEmpty(string)) {
            string = String.valueOf(string) + ",";
        }
        getSharedPref().edit().putString(KEY_QUEUE_RESEND, String.valueOf(string) + str).commit();
        this.m_lockPref.writeLock().unlock();
    }

    public void setCapacity(int i) {
        this.m_capacity = i;
    }

    public void onNetworkConnected() {
        Log.i(this.TAG, "onNetworkConnected");
        checkResend();
    }

    public void checkResend() {
        Log.i(this.TAG, "checkResend");
        this.m_lockPref.writeLock().lock();
        String string = getSharedPref().getString(KEY_QUEUE_RESEND, "");
        getSharedPref().edit().putString(KEY_QUEUE_RESEND, "").commit();
        this.m_lockPref.writeLock().unlock();
        for (String str : string.split(",")) {
            this.m_lockPref.readLock().lock();
            String string2 = getSharedPref().getString(KEY_QUEUE_ITEM_PREFIX + str, "");
            this.m_lockPref.readLock().unlock();
            if (!TextUtils.isEmpty(string2)) {
                QueueItem queueItemNewQueueItem = NewQueueItem();
                try {
                    queueItemNewQueueItem.unmarshal(string2);
                    addItem(queueItemNewQueueItem);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private SharedPreferences getSharedPref() {
        return this.m_context.getSharedPreferences("HTTPQ_" + this.m_name, 0);
    }

    public void post(String str, String str2, Boolean bool, Map<String, String> map) {
        Log.i(this.TAG, String.format("post, url=%s, bodyStr=%s", str, str2));
        QueueItem queueItemNewQueueItem = NewQueueItem();
        queueItemNewQueueItem.method = "POST";
        queueItemNewQueueItem.url = str;
        queueItemNewQueueItem.setBody(str2);
        queueItemNewQueueItem.bSync = bool;
        queueItemNewQueueItem.setHeaders(map);
        addItem(queueItemNewQueueItem);
    }

    public void post(String str, Map<String, String> map, Boolean bool, Map<String, String> map2) {
        Log.i(this.TAG, String.format("post, url=%s, bodyPairs=%s", str, map));
        QueueItem queueItemNewQueueItem = NewQueueItem();
        queueItemNewQueueItem.method = "POST";
        queueItemNewQueueItem.url = str;
        queueItemNewQueueItem.setBody(map);
        queueItemNewQueueItem.bSync = bool;
        queueItemNewQueueItem.setHeaders(map2);
        addItem(queueItemNewQueueItem);
    }

    public void get(String str, Boolean bool, Map<String, String> map) {
        Log.i(this.TAG, "get, url=" + str);
        QueueItem queueItemNewQueueItem = NewQueueItem();
        queueItemNewQueueItem.method = "GET";
        queueItemNewQueueItem.url = str;
        queueItemNewQueueItem.bSync = bool;
        queueItemNewQueueItem.setHeaders(map);
        addItem(queueItemNewQueueItem);
    }

    public void get(String str, Map<String, String> map, Boolean bool, Map<String, String> map2) {
        Log.i(this.TAG, String.format("get, url=%s, params=%s", str, map));
        QueueItem queueItemNewQueueItem = NewQueueItem();
        queueItemNewQueueItem.method = "GET";
        if (!str.endsWith("?")) {
            str = String.valueOf(str) + "?";
        }
        queueItemNewQueueItem.url = String.valueOf(str) + StrUtil.createLinkString(map, true, true);
        queueItemNewQueueItem.bSync = bool;
        queueItemNewQueueItem.setHeaders(map2);
        addItem(queueItemNewQueueItem);
    }

    public void addItem(QueueItem queueItem) {
        Log.i(this.TAG, "addItem, item=" + queueItem);
        this.m_lock.lock();
        try {
            if (this.m_bClosed.booleanValue()) {
                return;
            }
            try {
                if (this.m_queue.size() < this.m_capacity) {
                    String strMarshal = queueItem.marshal();
                    if (!TextUtils.isEmpty(strMarshal)) {
                        this.m_lockPref.writeLock().lock();
                        getSharedPref().edit().putString(KEY_QUEUE_ITEM_PREFIX + queueItem.id, strMarshal).commit();
                        this.m_lockPref.writeLock().unlock();
                    }
                    this.m_queue.add(queueItem);
                    this.m_notEmpty.signal();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            this.m_lock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void send() {
        while (true) {
            this.m_lock.lock();
            if (this.m_bClosed.booleanValue()) {
                this.m_lock.unlock();
                return;
            }
            while (this.m_queue.isEmpty()) {
                try {
                    try {
                        this.m_notEmpty.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (this.m_lock.isHeldByCurrentThread()) {
                        }
                    }
                } catch (Throwable th) {
                    if (this.m_lock.isHeldByCurrentThread()) {
                        this.m_lock.unlock();
                    }
                    throw th;
                }
            }
            QueueItem queueItem = this.m_queue.get(0);
            this.m_queue.remove(0);
            this.m_lock.unlock();
            if (queueItem.bSync.booleanValue()) {
                queueItem.strResp = HTTPDo(queueItem);
                Context context = this.m_context;
                if (context != null) {
                    ((Activity) context).runOnUiThread(new HandleResponseTask(queueItem));
                }
            } else {
                new SendTask(queueItem).execute(new Void[0]);
            }
            if (this.m_lock.isHeldByCurrentThread()) {
                this.m_lock.unlock();
            }
        }
    }

    private class HandleResponseTask implements Runnable {
        private QueueItem m_item;

        public HandleResponseTask(QueueItem queueItem) {
            this.m_item = queueItem;
        }

        @Override // java.lang.Runnable
        public void run() {
            Log.d(HTTPQueue.this.TAG, "processResult sync");
            HTTPCallback hTTPCallback = this.m_item.callback;
            if (hTTPCallback == null) {
                hTTPCallback = HTTPQueue.this.m_callback;
            }
            HTTPQueue.this.handleResponse(this.m_item, hTTPCallback != null ? hTTPCallback.processResult(this.m_item.strResp, this.m_item.transParam) : false);
        }
    }

    private class SendTask extends AsyncTask<Void, Void, String> {
        private QueueItem m_item;

        public SendTask(QueueItem queueItem) {
            this.m_item = queueItem;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public String doInBackground(Void... voidArr) {
            return HTTPQueue.this.HTTPDo(this.m_item);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(String str) {
            HTTPCallback hTTPCallback = this.m_item.callback;
            if (hTTPCallback == null) {
                hTTPCallback = HTTPQueue.this.m_callback;
            }
            HTTPQueue.this.handleResponse(this.m_item, hTTPCallback != null ? hTTPCallback.processResult(str, this.m_item.transParam) : false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String HTTPDo(QueueItem queueItem) throws ParseException, IOException {
        String string;
        HttpUriRequest httpGet;
        Log.i(this.TAG, "HTTPDo");
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        HttpParams params = defaultHttpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, queueItem.connectionTimeout);
        HttpConnectionParams.setSoTimeout(params, queueItem.soTimeout);
        Boolean bool = true;
        HttpResponse httpResponseExecute = null;
        try {
            if ("POST".equalsIgnoreCase(queueItem.method)) {
                httpGet = new HttpPost(queueItem.url);
            } else if ("GET".equalsIgnoreCase(queueItem.method)) {
                bool = false;
                httpGet = new HttpGet(queueItem.url);
            } else {
                httpGet = null;
            }
            if (httpGet != null) {
                if (queueItem.headers != null) {
                    for (Map.Entry entry : queueItem.headers.entrySet()) {
                        httpGet.setHeader((String) entry.getKey(), (String) entry.getValue());
                    }
                }
                if (bool.booleanValue()) {
                    if (!TextUtils.isEmpty(queueItem.bodyStr)) {
                        ((HttpPost) httpGet).setEntity(new StringEntity(queueItem.bodyStr, "UTF-8"));
                    } else if (queueItem.bodyPairs != null && queueItem.bodyPairs.size() > 0) {
                        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(queueItem.bodyPairs, "UTF-8");
                        if (!TextUtils.isEmpty(queueItem.keyRSA)) {
                            String strRsaEncrypt = Crypto.rsaEncrypt(StrUtil.readAll(urlEncodedFormEntity.getContent()), queueItem.keyRSA);
                            httpGet.setHeader("Encryption", "rsa");
                            ((HttpPost) httpGet).setEntity(new StringEntity(strRsaEncrypt, "UTF-8"));
                        } else {
                            ((HttpPost) httpGet).setEntity(urlEncodedFormEntity);
                        }
                    }
                }
                httpResponseExecute = defaultHttpClient.execute(httpGet);
            }
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        if (httpResponseExecute != null) {
            try {
                string = EntityUtils.toString(httpResponseExecute.getEntity());
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        } else {
            string = "";
        }
        Log.d(this.TAG, String.format("HTTPDo, strResp=%s, item=%s", string, queueItem));
        return string;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleResponse(QueueItem queueItem, boolean z) {
        String strMarshal;
        if (z) {
            Log.e(this.TAG, String.format("handleResponse, id=%s, bResend=%s", queueItem.id, Boolean.valueOf(z)));
        } else {
            Log.i(this.TAG, String.format("handleResponse, id=%s, bResend=%s", queueItem.id, Boolean.valueOf(z)));
        }
        if (!z || queueItem.leftRetry <= 0) {
            if (z && queueItem.leftRetry <= 0) {
                Log.e(this.TAG, "reach max retry limit, give up");
            }
            this.m_lockPref.writeLock().lock();
            getSharedPref().edit().remove(KEY_QUEUE_ITEM_PREFIX + queueItem.id).commit();
            this.m_lockPref.writeLock().unlock();
            return;
        }
        queueItem.leftRetry--;
        try {
            strMarshal = queueItem.marshal();
            Log.d(this.TAG, "item to resend:" + strMarshal);
        } catch (Exception e) {
            e.printStackTrace();
            strMarshal = "";
        }
        this.m_lockPref.writeLock().lock();
        if (!TextUtils.isEmpty(strMarshal)) {
            getSharedPref().edit().putString(KEY_QUEUE_ITEM_PREFIX + queueItem.id, strMarshal).commit();
            String string = getSharedPref().getString(KEY_QUEUE_RESEND, "");
            if (!TextUtils.isEmpty(string)) {
                string = String.valueOf(string) + ",";
            }
            getSharedPref().edit().putString(KEY_QUEUE_RESEND, String.valueOf(string) + queueItem.id).commit();
        } else {
            getSharedPref().edit().remove(KEY_QUEUE_ITEM_PREFIX + queueItem.id).commit();
        }
        this.m_lockPref.writeLock().unlock();
    }
}