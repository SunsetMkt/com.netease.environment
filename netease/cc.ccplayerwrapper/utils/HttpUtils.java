package com.netease.cc.ccplayerwrapper.utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.james.mime4j.util.CharsetUtil;

/* loaded from: classes5.dex */
public class HttpUtils {

    /* renamed from: a, reason: collision with root package name */
    private static Object f1537a = new Object();
    private static Handler b;

    public static class b {

        /* renamed from: a, reason: collision with root package name */
        EnumC0060b f1539a = EnumC0060b.GET;
        String b;
        String[] c;
        a d;
        File e;
        int f;
        int g;

        public interface a {
            void callback(int i, String str);
        }

        /* renamed from: com.netease.cc.ccplayerwrapper.utils.HttpUtils$b$b, reason: collision with other inner class name */
        enum EnumC0060b {
            GET,
            POST,
            UPLOAD_FILE
        }
    }

    public static class c implements Runnable {

        /* renamed from: a, reason: collision with root package name */
        b f1541a;

        public c(b bVar) {
            this.f1541a = bVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            new a(this.f1541a).execute(new String[0]);
        }
    }

    public static void Init() {
        synchronized (f1537a) {
            if (b == null) {
                b = new Handler(Looper.getMainLooper());
            }
        }
    }

    public static void Release() {
        synchronized (f1537a) {
            b = null;
        }
    }

    public static void addRequest(b bVar) {
        Handler handler = b;
        if (handler != null) {
            handler.post(new c(bVar));
        }
    }

    public static void httpGet(String str) {
        b bVar = new b();
        bVar.f1539a = b.EnumC0060b.GET;
        bVar.b = str;
        bVar.c = null;
        bVar.d = null;
        addRequest(bVar);
    }

    public static void httpPost(String str, String[] strArr) {
        b bVar = new b();
        bVar.f1539a = b.EnumC0060b.POST;
        bVar.b = str;
        bVar.c = strArr;
        bVar.d = null;
        addRequest(bVar);
    }

    public static void uploadImage(String str, File file, int i, int i2) {
        b bVar = new b();
        bVar.f1539a = b.EnumC0060b.UPLOAD_FILE;
        bVar.b = str;
        bVar.c = null;
        bVar.d = null;
        bVar.e = file;
        bVar.f = i;
        bVar.g = i2;
        addRequest(bVar);
    }

    public static void httpGet(String str, b.a aVar) {
        b bVar = new b();
        bVar.f1539a = b.EnumC0060b.GET;
        bVar.b = str;
        bVar.c = null;
        bVar.d = aVar;
        addRequest(bVar);
    }

    public static class a extends AsyncTask<String, Void, Integer> {

        /* renamed from: a, reason: collision with root package name */
        private b f1538a;

        public a(b bVar) {
            this.f1538a = bVar;
        }

        /* JADX WARN: Code restructure failed: missing block: B:29:0x0052, code lost:
        
            if (r0 == null) goto L32;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        int a(java.lang.String r6, java.lang.StringBuffer r7) throws java.lang.Throwable {
            /*
                r5 = this;
                r0 = 0
                java.net.URL r1 = new java.net.URL     // Catch: java.lang.Throwable -> L43 java.lang.Exception -> L45 java.net.SocketTimeoutException -> L4d
                r1.<init>(r6)     // Catch: java.lang.Throwable -> L43 java.lang.Exception -> L45 java.net.SocketTimeoutException -> L4d
                java.net.URLConnection r6 = r1.openConnection()     // Catch: java.lang.Throwable -> L43 java.lang.Exception -> L45 java.net.SocketTimeoutException -> L4d
                java.net.HttpURLConnection r6 = (java.net.HttpURLConnection) r6     // Catch: java.lang.Throwable -> L43 java.lang.Exception -> L45 java.net.SocketTimeoutException -> L4d
                r0 = 5000(0x1388, float:7.006E-42)
                r6.setConnectTimeout(r0)     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3d java.net.SocketTimeoutException -> L40
                r6.setReadTimeout(r0)     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3d java.net.SocketTimeoutException -> L40
                int r0 = r6.getResponseCode()     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3d java.net.SocketTimeoutException -> L40
                r1 = 200(0xc8, float:2.8E-43)
                if (r0 != r1) goto L37
                java.io.InputStream r1 = r6.getInputStream()     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3d java.net.SocketTimeoutException -> L40
                java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3d java.net.SocketTimeoutException -> L40
                java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3d java.net.SocketTimeoutException -> L40
                r3.<init>(r1)     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3d java.net.SocketTimeoutException -> L40
                r2.<init>(r3)     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3d java.net.SocketTimeoutException -> L40
            L2a:
                java.lang.String r1 = r2.readLine()     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3d java.net.SocketTimeoutException -> L40
                if (r1 == 0) goto L34
                r7.append(r1)     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3d java.net.SocketTimeoutException -> L40
                goto L2a
            L34:
                r2.close()     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3d java.net.SocketTimeoutException -> L40
            L37:
                if (r6 == 0) goto L5c
                goto L57
            L3a:
                r7 = move-exception
                r0 = r6
                goto L5d
            L3d:
                r7 = move-exception
                r0 = r6
                goto L46
            L40:
                r7 = move-exception
                r0 = r6
                goto L4e
            L43:
                r7 = move-exception
                goto L5d
            L45:
                r7 = move-exception
            L46:
                r7.printStackTrace()     // Catch: java.lang.Throwable -> L43
                r6 = -2
                if (r0 == 0) goto L5b
                goto L54
            L4d:
                r7 = move-exception
            L4e:
                r7.printStackTrace()     // Catch: java.lang.Throwable -> L43
                r6 = -1
                if (r0 == 0) goto L5b
            L54:
                r4 = r0
                r0 = r6
                r6 = r4
            L57:
                r6.disconnect()
                goto L5c
            L5b:
                r0 = r6
            L5c:
                return r0
            L5d:
                if (r0 == 0) goto L62
                r0.disconnect()
            L62:
                throw r7
            */
            throw new UnsupportedOperationException("Method not decompiled: com.netease.cc.ccplayerwrapper.utils.HttpUtils.a.a(java.lang.String, java.lang.StringBuffer):int");
        }

        int a(String str, String[] strArr, StringBuffer stringBuffer) throws IOException {
            try {
                URLConnection uRLConnectionOpenConnection = new URL(str).openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) uRLConnectionOpenConnection;
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setInstanceFollowRedirects(true);
                httpURLConnection.connect();
                httpURLConnection.setConnectTimeout(20000);
                httpURLConnection.setReadTimeout(20000);
                if (strArr != null && strArr.length > 0 && strArr.length % 2 == 0) {
                    OutputStream outputStream = uRLConnectionOpenConnection.getOutputStream();
                    String string = "";
                    for (int i = 0; i < strArr.length; i += 2) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(string);
                        sb.append(string.isEmpty() ? "?" : com.alipay.sdk.m.s.a.l);
                        string = sb.toString();
                        String str2 = strArr[i];
                        String str3 = strArr[i + 1];
                        if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str2)) {
                            string = string + str2 + "=" + str3;
                        }
                    }
                    outputStream.write(string.getBytes());
                }
                int responseCode = ((HttpURLConnection) uRLConnectionOpenConnection).getResponseCode();
                if (responseCode == 200) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                    while (true) {
                        String line = bufferedReader.readLine();
                        if (line == null) {
                            break;
                        }
                        stringBuffer.append(line);
                        stringBuffer.append(CharsetUtil.CRLF);
                    }
                }
                return responseCode;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return -2;
            } catch (MalformedURLException e2) {
                e2.printStackTrace();
                return -1;
            } catch (SocketTimeoutException e3) {
                e3.printStackTrace();
                return -1;
            } catch (IOException e4) {
                e4.printStackTrace();
                return -3;
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:101:? A[RETURN, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:105:? A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:55:0x01a2 A[Catch: IOException -> 0x019e, TRY_LEAVE, TryCatch #11 {IOException -> 0x019e, blocks: (B:51:0x019a, B:55:0x01a2), top: B:87:0x019a }] */
        /* JADX WARN: Removed duplicated region for block: B:67:0x01ba A[Catch: IOException -> 0x01b6, TRY_LEAVE, TryCatch #3 {IOException -> 0x01b6, blocks: (B:63:0x01b2, B:67:0x01ba), top: B:83:0x01b2 }] */
        /* JADX WARN: Removed duplicated region for block: B:78:0x01ce A[Catch: IOException -> 0x01ca, TRY_LEAVE, TryCatch #9 {IOException -> 0x01ca, blocks: (B:74:0x01c6, B:78:0x01ce), top: B:85:0x01c6 }] */
        /* JADX WARN: Removed duplicated region for block: B:83:0x01b2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:85:0x01c6 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:87:0x019a A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:99:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        void a(java.lang.String r17, java.io.File r18, int r19, int r20) throws java.lang.Throwable {
            /*
                Method dump skipped, instructions count: 470
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.netease.cc.ccplayerwrapper.utils.HttpUtils.a.a(java.lang.String, java.io.File, int, int):void");
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Integer doInBackground(String... strArr) throws Throwable {
            int iA;
            if (this.f1538a == null) {
                return null;
            }
            StringBuffer stringBuffer = new StringBuffer();
            b bVar = this.f1538a;
            b.EnumC0060b enumC0060b = bVar.f1539a;
            if (enumC0060b == b.EnumC0060b.GET) {
                iA = a(bVar.b, stringBuffer);
            } else if (enumC0060b == b.EnumC0060b.POST) {
                iA = a(bVar.b, bVar.c, stringBuffer);
            } else {
                if (enumC0060b == b.EnumC0060b.UPLOAD_FILE) {
                    a(bVar.b, bVar.e, bVar.f, bVar.g);
                }
                iA = 0;
            }
            b.a aVar = this.f1538a.d;
            if (aVar == null) {
                return null;
            }
            aVar.callback(iA, stringBuffer.toString());
            return null;
        }
    }
}