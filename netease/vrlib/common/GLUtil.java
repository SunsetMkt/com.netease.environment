package com.netease.vrlib.common;

import android.app.ActivityManager;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;
import com.netease.vrlib.objects.MDAbsObject3D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes5.dex */
public class GLUtil {
    private static final String TAG = "GLUtil";
    public static final float[] sIdentityMatrix;

    static {
        float[] fArr = new float[16];
        sIdentityMatrix = fArr;
        Matrix.setIdentityM(fArr, 0);
    }

    public static boolean supportsEs2(Context context) {
        return ((ActivityManager) context.getSystemService("activity")).getDeviceConfigurationInfo().reqGlEsVersion >= 131072;
    }

    public static void glCheck(String str) {
        while (true) {
            int iGlGetError = GLES20.glGetError();
            if (iGlGetError == 0) {
                return;
            }
            Log.e(TAG, str + ": glError " + GLUtils.getEGLErrorString(iGlGetError));
        }
    }

    public static String readTextFileFromRaw(Context context, int i) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(i)));
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                String line = bufferedReader.readLine();
                if (line != null) {
                    sb.append(line);
                    sb.append('\n');
                } else {
                    return sb.toString();
                }
            } catch (IOException unused) {
                return null;
            }
        }
    }

    public static int compileShader(int i, String str) {
        int iGlCreateShader = GLES20.glCreateShader(i);
        if (iGlCreateShader != 0) {
            GLES20.glShaderSource(iGlCreateShader, str);
            GLES20.glCompileShader(iGlCreateShader);
            int[] iArr = new int[1];
            GLES20.glGetShaderiv(iGlCreateShader, 35713, iArr, 0);
            if (iArr[0] == 0) {
                Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(iGlCreateShader));
                GLES20.glDeleteShader(iGlCreateShader);
                iGlCreateShader = 0;
            }
        }
        if (iGlCreateShader != 0) {
            return iGlCreateShader;
        }
        throw new RuntimeException("Error creating shader.");
    }

    public static int createAndLinkProgram(int i, int i2, String[] strArr) {
        int iGlCreateProgram = GLES20.glCreateProgram();
        if (iGlCreateProgram != 0) {
            GLES20.glAttachShader(iGlCreateProgram, i);
            GLES20.glAttachShader(iGlCreateProgram, i2);
            if (strArr != null) {
                int length = strArr.length;
                for (int i3 = 0; i3 < length; i3++) {
                    GLES20.glBindAttribLocation(iGlCreateProgram, i3, strArr[i3]);
                }
            }
            GLES20.glLinkProgram(iGlCreateProgram);
            int[] iArr = new int[1];
            GLES20.glGetProgramiv(iGlCreateProgram, 35714, iArr, 0);
            if (iArr[0] == 0) {
                Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(iGlCreateProgram));
                GLES20.glDeleteProgram(iGlCreateProgram);
                iGlCreateProgram = 0;
            }
        }
        if (iGlCreateProgram != 0) {
            return iGlCreateProgram;
        }
        throw new RuntimeException("Error creating program.");
    }

    public static void loadObject3D(Context context, int i, MDAbsObject3D mDAbsObject3D) throws IOException {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(i)));
        while (true) {
            try {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                if (line.startsWith("v ")) {
                    arrayList.add(line.substring(2));
                }
                if (line.startsWith("vt ")) {
                    arrayList2.add(line.substring(3));
                }
                if (line.startsWith("f ")) {
                    arrayList3.add(line.substring(2));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int size = arrayList3.size() * 3 * 3;
        float[] fArr = new float[size];
        int size2 = arrayList3.size() * 3 * 2;
        float[] fArr2 = new float[size2];
        int size3 = arrayList3.size() * 3;
        short[] sArr = new short[size3];
        Iterator it = arrayList3.iterator();
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (it.hasNext()) {
            String[] strArrSplit = ((String) it.next()).split(" ");
            int length = strArrSplit.length;
            Iterator it2 = it;
            int i5 = 0;
            while (i5 < length) {
                int i6 = length;
                String str = strArrSplit[i5];
                int i7 = i3 + 1;
                int i8 = i2;
                sArr[i3] = (short) i3;
                String[] strArrSplit2 = str.split("/");
                String str2 = (String) arrayList.get(Integer.parseInt(strArrSplit2[0]) - 1);
                String str3 = (String) arrayList2.get(Integer.parseInt(strArrSplit2[1]) - 1);
                String[] strArrSplit3 = str2.split(" ");
                String[] strArrSplit4 = str3.split(" ");
                int length2 = strArrSplit3.length;
                ArrayList arrayList4 = arrayList;
                int i9 = 0;
                while (i9 < length2) {
                    fArr[i8] = Float.parseFloat(strArrSplit3[i9]);
                    i9++;
                    i8++;
                }
                int length3 = strArrSplit4.length;
                int i10 = 0;
                while (i10 < length3) {
                    fArr2[i4] = Float.parseFloat(strArrSplit4[i10]);
                    i10++;
                    i4++;
                }
                i5++;
                length = i6;
                i3 = i7;
                i2 = i8;
                arrayList = arrayList4;
            }
            it = it2;
        }
        FloatBuffer floatBufferPut = ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(fArr);
        floatBufferPut.position(0);
        FloatBuffer floatBufferPut2 = ByteBuffer.allocateDirect(size2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(fArr2);
        floatBufferPut2.position(0);
        mDAbsObject3D.setVerticesBuffer(0, floatBufferPut);
        mDAbsObject3D.setVerticesBuffer(1, floatBufferPut);
        mDAbsObject3D.setTexCoordinateBuffer(0, floatBufferPut2);
        mDAbsObject3D.setTexCoordinateBuffer(1, floatBufferPut2);
        mDAbsObject3D.setNumIndices(size3);
    }
}