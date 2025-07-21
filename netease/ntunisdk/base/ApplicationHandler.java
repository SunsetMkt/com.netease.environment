package com.netease.ntunisdk.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;
import dalvik.system.DexFile;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes2.dex */
public class ApplicationHandler extends Application {

    /* renamed from: a, reason: collision with root package name */
    private static Map<String, SdkApplication> f1601a = new HashMap();

    @Override // android.content.ContextWrapper
    protected void attachBaseContext(Context context) throws IOException, SecurityException {
        super.attachBaseContext(context);
        handleOnApplicationAttachBaseContext(context, this);
    }

    @Override // android.app.Application
    public void onCreate() throws IllegalAccessException, NoSuchMethodException, IOException, SecurityException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        super.onCreate();
        handleOnApplicationOnCreate(getApplicationContext(), this);
    }

    @Override // android.app.Application
    public void onTerminate() {
        super.onTerminate();
        handleOnApplicationTerminate(getApplicationContext(), this);
    }

    @Override // android.app.Application, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        handleOnApplicationConfigurationChanged(getApplicationContext(), this, configuration);
    }

    @Override // android.app.Application, android.content.ComponentCallbacks
    public void onLowMemory() {
        super.onLowMemory();
        handleOnApplicationLowMemory(getApplicationContext(), this);
    }

    @Override // android.app.Application, android.content.ComponentCallbacks2
    public void onTrimMemory(int i) {
        super.onTrimMemory(i);
        handleOnApplicationTrimMemory(getApplicationContext(), this, i);
    }

    public static void handleOnApplicationAttachBaseContext(Context context) throws IOException, SecurityException {
        b(context);
        Log.d("ApplicationHandler", "sdkMap size:" + f1601a.size());
        Iterator<String> it = f1601a.keySet().iterator();
        while (it.hasNext()) {
            f1601a.get(it.next()).handleOnApplicationAttachBaseContext(context);
        }
    }

    public static void handleOnApplicationOnCreate(Context context) throws IOException, SecurityException {
        Log.i("ApplicationHandler", "sdkMap size:" + f1601a.size());
        if (f1601a.isEmpty()) {
            Log.i("ApplicationHandler", "sdkMap.isEmpty");
            b(context);
        }
        Log.i("ApplicationHandler", "sdkMap size:" + f1601a.size());
        Iterator<String> it = f1601a.keySet().iterator();
        while (it.hasNext()) {
            f1601a.get(it.next()).handleOnApplicationOnCreate(context);
        }
        a();
    }

    public static void handleOnApplicationAttachBaseContext(Context context, Application application) throws IOException, SecurityException {
        Log.i("ApplicationHandler", "ApplicationHandler [handleOnApplicationAttachBaseContext]");
        b(context);
        Log.i("ApplicationHandler", "sdkMap size:" + f1601a.size());
        for (String str : f1601a.keySet()) {
            f1601a.get(str).handleOnApplicationAttachBaseContext(context, application);
            f1601a.get(str).handleOnApplicationAttachBaseContext(context);
        }
    }

    public static void handleOnApplicationOnCreate(Context context, Application application) throws IllegalAccessException, NoSuchMethodException, IOException, SecurityException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        String[] strArrSplit;
        Log.i("ApplicationHandler", "sdkMap size:" + f1601a.size());
        if (f1601a.isEmpty()) {
            Log.i("ApplicationHandler", "sdkMap.isEmpty");
            b(context);
        }
        Log.i("ApplicationHandler", "sdkMap size:" + f1601a.size());
        for (String str : f1601a.keySet()) {
            f1601a.get(str).handleOnApplicationOnCreate(context, application);
            f1601a.get(str).handleOnApplicationOnCreate(context);
        }
        a();
        Log.i("ApplicationHandler", "ApplicationHandler [handleOnApplicationOnCreate]");
        String strA = a(context);
        if (TextUtils.isEmpty(strA) || (strArrSplit = strA.split(";")) == null || strArrSplit.length <= 0) {
            return;
        }
        for (String str2 : strArrSplit) {
            try {
                String str3 = "com.netease.advertSdk.Sdk" + str2.substring(0, 1).toUpperCase(Locale.ROOT) + str2.substring(1);
                String str4 = str2 + "Init";
                Log.i("ApplicationHandler", "adC = " + str3 + ", methodName = " + str4);
                Class<?> cls = Class.forName(str3);
                Method declaredMethod = cls.getDeclaredMethod(str4, Application.class);
                declaredMethod.invoke(cls, application);
                Log.i("ApplicationHandler", "ApplicationHandler [handleOnApplicationOnCreate] invoke method=" + declaredMethod.getName());
            } catch (ClassNotFoundException e) {
                Log.w("ApplicationHandler", "ApplicationHandler [handleOnApplicationOnCreate] ClassNotFoundException=".concat(String.valueOf(e)));
            } catch (IllegalAccessException e2) {
                Log.w("ApplicationHandler", "ApplicationHandler [handleOnApplicationOnCreate] IllegalAccessException=".concat(String.valueOf(e2)));
            } catch (IllegalArgumentException e3) {
                Log.w("ApplicationHandler", "ApplicationHandler [handleOnApplicationOnCreate] IllegalArgumentException=".concat(String.valueOf(e3)));
            } catch (NoSuchMethodException e4) {
                Log.w("ApplicationHandler", "ApplicationHandler [handleOnApplicationOnCreate] NoSuchMethodException=".concat(String.valueOf(e4)));
            } catch (InvocationTargetException e5) {
                Log.w("ApplicationHandler", "ApplicationHandler [handleOnApplicationOnCreate] InvocationTargetException=".concat(String.valueOf(e5)));
                e5.printStackTrace();
            }
        }
    }

    public static void handleOnApplicationTerminate(Context context, Application application) {
        Log.d("ApplicationHandler", "sdkMap size:" + f1601a.size());
        try {
            Iterator<String> it = f1601a.keySet().iterator();
            while (it.hasNext()) {
                f1601a.get(it.next()).handleOnApplicationTerminate(context, application);
            }
        } catch (Throwable th) {
            UniSdkUtils.w("ApplicationHandler", th.getMessage());
        }
    }

    public static void handleOnApplicationConfigurationChanged(Context context, Application application, Configuration configuration) {
        Log.d("ApplicationHandler", "sdkMap size:" + f1601a.size());
        try {
            Iterator<String> it = f1601a.keySet().iterator();
            while (it.hasNext()) {
                f1601a.get(it.next()).handleOnApplicationConfigurationChanged(context, application, configuration);
            }
        } catch (Throwable th) {
            UniSdkUtils.w("ApplicationHandler", th.getMessage());
        }
    }

    public static void handleOnApplicationLowMemory(Context context, Application application) {
        Log.d("ApplicationHandler", "sdkMap size:" + f1601a.size());
        try {
            Iterator<String> it = f1601a.keySet().iterator();
            while (it.hasNext()) {
                f1601a.get(it.next()).handleOnApplicationLowMemory(context, application);
            }
        } catch (Throwable th) {
            UniSdkUtils.w("ApplicationHandler", th.getMessage());
        }
    }

    public static void handleOnApplicationTrimMemory(Context context, Application application, int i) {
        Log.d("ApplicationHandler", "sdkMap size:" + f1601a.size());
        try {
            Iterator<String> it = f1601a.keySet().iterator();
            while (it.hasNext()) {
                f1601a.get(it.next()).handleOnApplicationTrimMemory(context, application, i);
            }
        } catch (Throwable th) {
            UniSdkUtils.w("ApplicationHandler", th.getMessage());
        }
    }

    private static String a(Context context) throws IOException {
        int iAvailable;
        InputStream inputStreamOpen = null;
        try {
            try {
                try {
                    inputStreamOpen = context.getAssets().open("advert_clist", 3);
                } catch (IOException e) {
                    e.printStackTrace();
                    if (inputStreamOpen == null) {
                        return "";
                    }
                    inputStreamOpen.close();
                }
                if (inputStreamOpen != null && (iAvailable = inputStreamOpen.available()) > 0) {
                    byte[] bArr = new byte[iAvailable];
                    inputStreamOpen.read(bArr);
                    return new String(bArr, "UTF-8");
                }
                if (inputStreamOpen == null) {
                    return "";
                }
                inputStreamOpen.close();
                return "";
            } finally {
                if (inputStreamOpen != null) {
                    try {
                        inputStreamOpen.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        } catch (IOException e3) {
            e3.printStackTrace();
            return "";
        }
    }

    private static void a() {
        Map<String, SdkApplication> map = f1601a;
        if (map != null) {
            map.clear();
        }
    }

    public static SdkApplication getSdkApplication(String str) {
        return f1601a.get(str);
    }

    private static void b(Context context) throws IOException, SecurityException {
        DexFile dexFile;
        ArrayList<String> arrayList = new ArrayList();
        String strC = c(context);
        if (strC != null) {
            for (String str : strC.split(";")) {
                if (str.trim().startsWith("Application")) {
                    arrayList.add("com.netease.ntunisdk." + str.trim());
                }
            }
        }
        if (TextUtils.isEmpty(strC) && arrayList.isEmpty()) {
            try {
                dexFile = new DexFile(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.sourceDir);
                Enumeration<String> enumerationEntries = dexFile.entries();
                while (enumerationEntries != null && enumerationEntries.hasMoreElements()) {
                    String strNextElement = enumerationEntries.nextElement();
                    if (strNextElement.startsWith("com.netease.ntunisdk.Application") && !strNextElement.contains("$")) {
                        arrayList.add(strNextElement);
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
                return;
            }
        } else {
            dexFile = null;
        }
        for (String str2 : arrayList) {
            try {
                Log.d("ApplicationHandler", String.format("Class.forName(%s)", str2));
                Constructor<?>[] constructors = Class.forName(str2).getConstructors();
                if (constructors != null) {
                    int i = 0;
                    while (true) {
                        if (i >= constructors.length) {
                            break;
                        }
                        Type[] genericParameterTypes = constructors[i].getGenericParameterTypes();
                        if (genericParameterTypes != null && genericParameterTypes.length == 1) {
                            SdkApplication sdkApplication = (SdkApplication) constructors[i].newInstance(context);
                            f1601a.put(sdkApplication.getChannel(), sdkApplication);
                            break;
                        }
                        i++;
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            } catch (IllegalArgumentException e3) {
                e3.printStackTrace();
            } catch (InstantiationException e4) {
                e4.printStackTrace();
            } catch (InvocationTargetException e5) {
                e5.printStackTrace();
            } catch (Exception e6) {
                e6.printStackTrace();
            }
        }
        if (dexFile != null) {
            try {
                dexFile.close();
            } catch (Throwable th2) {
                th2.printStackTrace();
            }
        }
    }

    private static String c(Context context) throws IOException {
        InputStream inputStreamOpen;
        int iAvailable;
        String str = null;
        try {
            inputStreamOpen = context.getAssets().open("ntunisdk_data", 3);
            iAvailable = inputStreamOpen.available();
        } catch (IOException unused) {
            Log.i("ApplicationHandler", "ntunisdk_data config not found");
        }
        if (iAvailable == 0) {
            Log.d("ApplicationHandler", "ntunisdk_data empty");
            return null;
        }
        byte[] bArr = new byte[iAvailable];
        inputStreamOpen.read(bArr);
        str = new String(bArr, "UTF-8");
        Log.d("ApplicationHandler", "ntunisdk_data:".concat(String.valueOf(str)));
        return str;
    }
}