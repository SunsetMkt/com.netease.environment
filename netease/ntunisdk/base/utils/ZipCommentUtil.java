package com.netease.ntunisdk.base.utils;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import org.jose4j.jwk.RsaJsonWebKey;

/* loaded from: classes3.dex */
public class ZipCommentUtil {
    private static final String CHARSET_NAME = "UTF-8";
    private static String MAGICTAG = "NETEASE";
    private static final String TAG = "UniSDK ZipCommentUtil";
    private static int TOTAL_COMMENT_LENGTH = 128;

    public static String getCommentAppchannel(Context context) throws IOException {
        byte[] bArr;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(new File(context.getPackageCodePath()), RsaJsonWebKey.PRIME_FACTOR_OTHER_MEMBER_NAME);
            byte[] bytes = MAGICTAG.getBytes("UTF-8");
            byte[] bArr2 = new byte[bytes.length];
            long length = randomAccessFile.length() - bytes.length;
            readFully(randomAccessFile, length, bArr2);
            if (Arrays.equals(bArr2, bytes)) {
                byte[] bArr3 = new byte[2];
                long j = length - 2;
                readFully(randomAccessFile, j, bArr3);
                short sStream2Short = stream2Short(bArr3, 0);
                int length2 = (((TOTAL_COMMENT_LENGTH - 2) - sStream2Short) - 2) - MAGICTAG.getBytes().length;
                bArr = new byte[length2];
                readFully(randomAccessFile, (j - sStream2Short) - length2, bArr);
            } else {
                bArr = null;
            }
            randomAccessFile.close();
        } catch (Exception e) {
            Log.d(TAG, "getCommentAppchannel excption:" + e.getMessage());
        }
        if (bArr != null) {
            String str = new String(bArr, "UTF-8");
            Log.d(TAG, "getCommentAppchannel:".concat(str));
            return str;
        }
        Log.d(TAG, "getCommentAppchannel null");
        return null;
    }

    private static void readFully(RandomAccessFile randomAccessFile, long j, byte[] bArr) throws IOException {
        randomAccessFile.seek(j);
        randomAccessFile.readFully(bArr);
    }

    private static short stream2Short(byte[] bArr, int i) {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(2);
        byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
        byteBufferAllocate.put(bArr[i]);
        byteBufferAllocate.put(bArr[i + 1]);
        return byteBufferAllocate.getShort(0);
    }
}