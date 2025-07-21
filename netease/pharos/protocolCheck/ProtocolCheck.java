package com.netease.pharos.protocolCheck;

import android.os.Process;
import com.alipay.sdk.m.u.b;
import com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager;
import com.netease.pharos.Const;
import com.netease.pharos.config.CheckResult;
import com.netease.pharos.linkcheck.CheckOverNotifyListener;
import com.netease.pharos.linkcheck.CycleTaskStopListener;
import com.netease.pharos.linkcheck.Result;
import com.netease.pharos.protocolCheck.kcp.KcpJavaClient;
import com.netease.pharos.threadManager.TimerManager;
import com.netease.pharos.util.LogUtil;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes4.dex */
public class ProtocolCheck {
    private static final String TAG = "LinkCheck";
    private CheckResult mCheckResult;
    private String mRegion = null;
    private int mInterval = 0;
    private ProtocolCheckListener mListener = null;
    private CycleTaskStopListener mCycleTaskStopListener = null;
    private CheckOverNotifyListener mCheckOverNotifyListener = null;
    private String mExtra = null;
    private final MyTimeTask mTask = new MyTimeTask();

    public void setRegion(String str) {
        this.mRegion = str;
    }

    public String getmExtra() {
        return this.mExtra;
    }

    public void setmExtra(String str) {
        this.mExtra = str;
    }

    public void setInterval(int i) {
        this.mInterval = i;
    }

    public ProtocolCheckListener getmListener() {
        return this.mListener;
    }

    public void setmListener(ProtocolCheckListener protocolCheckListener) {
        this.mListener = protocolCheckListener;
    }

    public CycleTaskStopListener getmCycleTaskStopListener() {
        return this.mCycleTaskStopListener;
    }

    public void setmCycleTaskStopListener(CycleTaskStopListener cycleTaskStopListener) {
        this.mCycleTaskStopListener = cycleTaskStopListener;
    }

    public CheckOverNotifyListener getmCheckOverNotifyListener() {
        return this.mCheckOverNotifyListener;
    }

    public void setmCheckOverNotifyListener(CheckOverNotifyListener checkOverNotifyListener) {
        this.mCheckOverNotifyListener = checkOverNotifyListener;
    }

    class MyTimeTask extends TimerTask {
        int mCount;
        int mPort;
        int mSize;
        int mTime;
        int mType;
        String mIp = null;
        int mIndex = 0;
        boolean mIsOpen = true;

        public MyTimeTask() {
            LogUtil.i(ProtocolCheck.TAG, "MyTimeTask [MyTimeTask]");
        }

        public void setIsOpen(boolean z) {
            this.mIsOpen = z;
        }

        public void setType(int i) {
            this.mType = i;
        }

        public void setTime(int i) {
            this.mCount = i;
        }

        public void setmIp(String str) {
            this.mIp = str;
        }

        public void setmPort(int i) {
            this.mPort = i;
        }

        public void setmTime(int i) {
            this.mTime = i;
        }

        public void setmSize(int i) {
            this.mSize = i;
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            LogUtil.i(ProtocolCheck.TAG, "MyTimeTask checkOnce mType=" + this.mType + ", isOpen=" + this.mIsOpen);
            if (this.mIsOpen) {
                int i = this.mIndex + 1;
                this.mIndex = i;
                LogUtil.i(ProtocolCheck.TAG, "\u7b2c " + i + " \u6b21\u6267\u884c");
                ProtocolCheck.this.checkOnce(this.mType, this.mIp, this.mPort, this.mCount, this.mTime, this.mSize);
                if (i > 1 && ProtocolCheck.this.mCheckOverNotifyListener != null) {
                    ProtocolCheck.this.mCheckOverNotifyListener.callBack(ProtocolCheck.this.mExtra);
                }
                if (10 == i) {
                    LogUtil.i(ProtocolCheck.TAG, "\u7ed3\u675f\u5faa\u73af\u5668");
                    ProtocolCheck.this.mTask.cancel();
                    if (ProtocolCheck.this.mCycleTaskStopListener != null) {
                        ProtocolCheck.this.mCycleTaskStopListener.callBack(ProtocolCheck.this.mExtra);
                    }
                }
            }
        }
    }

    public int checkOnce(int i, String str, int i2, int i3, int i4, int i5) {
        LogUtil.i(TAG, "\u5355\u6b21\u6267\u884c\uff0c\u53c2\u6570 type=" + i + ", ip=" + str + ", port=" + i2 + ", count=" + i3 + ", time=" + i4 + ", size=" + i5 + ", mExtra=" + this.mExtra);
        CheckResult checkResult = new CheckResult();
        this.mCheckResult = checkResult;
        String str2 = this.mRegion;
        if (str2 != null) {
            checkResult.setmRegion(str2);
        }
        Result.getInstance().getLinktestId();
        this.mCheckResult.setProtocol(i);
        this.mCheckResult.setPacketCount(i3);
        this.mCheckResult.setPacketBytesCount(i5);
        this.mCheckResult.setIp(str);
        this.mCheckResult.setmPort(i2);
        this.mCheckResult.setmExtra(this.mExtra);
        if (1 == i) {
            return tcpCheck(str, i2, i3, i4, i5);
        }
        if (2 == i) {
            return udpCheck(str, i2, i3, i4, i5);
        }
        if (3 == i) {
            return kcpCheck(i3);
        }
        if (4 == i) {
            return ping(str, i3, i4);
        }
        if (5 == i) {
            return dns(str);
        }
        return 11;
    }

    public int check(int i, String str, int i2, int i3, int i4, int i5) {
        LogUtil.i(TAG, "Link check2 \u53c2\u6570 type=" + i + ", ip=" + str + ", port=" + i2 + ", count=" + i3 + ", time=" + i4 + ", size=" + i5 + ", mInterval=" + this.mInterval + ", mExtra= " + this.mExtra);
        if (this.mInterval == 0) {
            LogUtil.i(TAG, "\u4e00\u6b21\u6027\u6267\u884c");
            return checkOnce(i, str, i2, i3, i4, i5);
        }
        LogUtil.i(TAG, "\u5faa\u73af\u6267\u884c\uff0c\u65f6\u95f4\u95f4\u9694\u4e3a=" + this.mInterval);
        this.mTask.setType(i);
        this.mTask.setmIp(str);
        this.mTask.setmPort(i2);
        this.mTask.setTime(i3);
        this.mTask.setmTime(i4);
        this.mTask.setmSize(i5);
        TimerManager.getInstance().getTimer().schedule(this.mTask, 0, this.mInterval * 1000 * 60);
        return 0;
    }

    public int tcpCheck(String str, int i, int i2, int i3, int i4) throws IOException {
        int i5;
        BufferedInputStream bufferedInputStream;
        int i6;
        String str2 = str;
        int i7 = i;
        int i8 = i2;
        LogUtil.i(TAG, "LinkCheck tcpCheck ip=" + str2 + ", port=" + i7 + ", count=" + i8 + ", time=" + i3 + ", size=" + i4);
        try {
            byte[] bArr = new byte[i4];
            for (int i9 = 0; i9 < i4; i9++) {
                bArr[i9] = 115;
            }
            Socket socket = new Socket(str2, i7);
            socket.setSoTimeout(i3);
            BufferedInputStream bufferedInputStream2 = new BufferedInputStream(socket.getInputStream());
            OutputStream outputStream = socket.getOutputStream();
            LogUtil.i(TAG, "TCP time=" + i8);
            int i10 = 0;
            int i11 = 0;
            while (i10 < i8) {
                try {
                    long jCurrentTimeMillis = System.currentTimeMillis();
                    i6 = i10;
                    try {
                        bArr[0] = (byte) (i10 + 48);
                        outputStream.write(bArr, 0, i4);
                        int i12 = 0;
                        while (true) {
                            bufferedInputStream = bufferedInputStream2;
                            if (bufferedInputStream2.read() == 10) {
                                break;
                            }
                            i12++;
                            bufferedInputStream2 = bufferedInputStream;
                        }
                        if (i4 != i12) {
                            try {
                                LogUtil.e(TAG, "TCP Packet loss, count=" + i12 + "ip=" + str2 + ", port=" + i7 + ", count=" + i8 + ", time=" + i3 + ", size=" + i4);
                                i11++;
                                ProtocolCheckCore.mNetmonReportMap.get(1).addPacketLossCount();
                            } catch (Exception e) {
                                e = e;
                                LogUtil.i(TAG, "LinkCheck tcpCheck Exception1=" + e);
                                e.printStackTrace();
                                i11++;
                                ProtocolCheckCore.mNetmonReportMap.get(1).addPacketLossCount();
                                i10 = i6 + 1;
                                str2 = str;
                                i7 = i;
                                bufferedInputStream2 = bufferedInputStream;
                            }
                        } else {
                            LogUtil.i(TAG, "TCP Packet sucess count=" + i12);
                            long jCurrentTimeMillis2 = System.currentTimeMillis() - jCurrentTimeMillis;
                            this.mCheckResult.addTime(jCurrentTimeMillis2);
                            isRecordMtr(1, jCurrentTimeMillis2);
                        }
                    } catch (Exception e2) {
                        e = e2;
                        bufferedInputStream = bufferedInputStream2;
                    }
                } catch (Exception e3) {
                    e = e3;
                    bufferedInputStream = bufferedInputStream2;
                    i6 = i10;
                }
                i10 = i6 + 1;
                str2 = str;
                i7 = i;
                bufferedInputStream2 = bufferedInputStream;
            }
            if (!socket.isClosed()) {
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            }
            i8 = i11;
            i5 = 0;
        } catch (Exception e4) {
            LogUtil.i(TAG, "LinkCheck tcpCheck Exception2=" + e4);
            if (ProtocolCheckCore.mNetmonReportMap.get(1) != null) {
                ProtocolCheckCore.mNetmonReportMap.get(1).setPacketLossCount(i8);
            }
            i5 = 11;
        }
        LogUtil.i(TAG, "LinkCheck tcpCheck mCheckResult=" + this.mCheckResult);
        this.mCheckResult.setPacketLossCount(i8);
        this.mListener.callBack(this.mCheckResult);
        return i5;
    }

    /* JADX WARN: Removed duplicated region for block: B:115:0x0196  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int udpCheck(java.lang.String r21, int r22, int r23, int r24, int r25) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 425
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.protocolCheck.ProtocolCheck.udpCheck(java.lang.String, int, int, int, int):int");
    }

    public int kcpCheck(int i) {
        int length;
        LogUtil.i(TAG, "LinkCheck kcpCheck");
        int iNextInt = new Random().nextInt(999999);
        Timer timer = new Timer();
        int i2 = 11;
        int i3 = 0;
        try {
            try {
                KcpJavaClient kcpJavaClient = new KcpJavaClient(iNextInt, Const.UPLOAD_SERVER_IP, Const.KCP_PORT);
                kcpJavaClient.WndSize(1024, 1024);
                kcpJavaClient.NoDelay(1, 20, 2, 1);
                byte[] bArr = new byte[2048];
                for (int i4 = 0; i4 < 2048; i4++) {
                    bArr[i4] = 115;
                }
                int i5 = 0;
                for (int i6 = 0; i6 < i; i6++) {
                    try {
                        long jCurrentTimeMillis = System.currentTimeMillis();
                        kcpJavaClient.Send(bArr);
                        int i7 = i2;
                        try {
                            timer.scheduleAtFixedRate(new TimerTask() { // from class: com.netease.pharos.protocolCheck.ProtocolCheck.1
                                final /* synthetic */ KcpJavaClient val$client;

                                AnonymousClass1(KcpJavaClient kcpJavaClient2) {
                                    kcpJavaClient = kcpJavaClient2;
                                }

                                @Override // java.util.TimerTask, java.lang.Runnable
                                public void run() {
                                    kcpJavaClient.Update(System.currentTimeMillis());
                                }
                            }, Calendar.getInstance().getTime(), LooperMessageLoggingManager.LAG_TIME);
                            byte[] bArr2 = new byte[2048];
                            length = 0;
                            do {
                                DatagramPacket datagramPacket = new DatagramPacket(new byte[2048], 2048);
                                try {
                                    kcpJavaClient2.mDatagramSocket.receive(datagramPacket);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                byte[] data = datagramPacket.getData();
                                length += datagramPacket.getLength();
                                kcpJavaClient2.Input(data);
                            } while (kcpJavaClient2.Recv(bArr2) <= 0);
                            if (2048 > length) {
                                LogUtil.e(TAG, "UDP Packet loss");
                                i5++;
                                ProtocolCheckCore.mNetmonReportMap.get(3).addPacketLossCount();
                            } else {
                                long jCurrentTimeMillis2 = System.currentTimeMillis() - jCurrentTimeMillis;
                                this.mCheckResult.addTime(jCurrentTimeMillis2);
                                isRecordMtr(3, jCurrentTimeMillis2);
                            }
                        } catch (Exception e2) {
                            e = e2;
                            i2 = i7;
                            try {
                                LogUtil.i(TAG, "kcpCheck Exception1=" + e);
                                e.printStackTrace();
                                i5++;
                                ProtocolCheckCore.mNetmonReportMap.get(3).addPacketLossCount();
                            } catch (Exception e3) {
                                e = e3;
                                i3 = i5;
                                LogUtil.i(TAG, "kcpCheck Exception2=" + e);
                                e.printStackTrace();
                                i3++;
                                ProtocolCheckCore.mNetmonReportMap.get(3).addPacketLossCount();
                                timer.cancel();
                                this.mCheckResult.setPacketLossCount(i3);
                                this.mListener.callBack(this.mCheckResult);
                                return i2;
                            } catch (Throwable unused) {
                                i3 = i5;
                                timer.cancel();
                                this.mCheckResult.setPacketLossCount(i3);
                                this.mListener.callBack(this.mCheckResult);
                                return i2;
                            }
                        } catch (Throwable unused2) {
                            i3 = i5;
                            i2 = i7;
                            timer.cancel();
                            this.mCheckResult.setPacketLossCount(i3);
                            this.mListener.callBack(this.mCheckResult);
                            return i2;
                        }
                        try {
                            LogUtil.i(TAG, "KCP recePacket length=" + length);
                            i2 = 0;
                        } catch (Exception e4) {
                            e = e4;
                            i2 = 0;
                            LogUtil.i(TAG, "kcpCheck Exception1=" + e);
                            e.printStackTrace();
                            i5++;
                            ProtocolCheckCore.mNetmonReportMap.get(3).addPacketLossCount();
                        } catch (Throwable unused3) {
                            i2 = 0;
                            i3 = i5;
                            timer.cancel();
                            this.mCheckResult.setPacketLossCount(i3);
                            this.mListener.callBack(this.mCheckResult);
                            return i2;
                        }
                    } catch (Exception e5) {
                        e = e5;
                    } catch (Throwable unused4) {
                    }
                }
                int i8 = i2;
                timer.cancel();
                this.mCheckResult.setPacketLossCount(i5);
                this.mListener.callBack(this.mCheckResult);
                return i8;
            } catch (Throwable unused5) {
            }
        } catch (Exception e6) {
            e = e6;
        }
    }

    /* renamed from: com.netease.pharos.protocolCheck.ProtocolCheck$1 */
    class AnonymousClass1 extends TimerTask {
        final /* synthetic */ KcpJavaClient val$client;

        AnonymousClass1(KcpJavaClient kcpJavaClient2) {
            kcpJavaClient = kcpJavaClient2;
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            kcpJavaClient.Update(System.currentTimeMillis());
        }
    }

    private boolean isRecordMtr(int i, long j) {
        long j2;
        if (i == 1) {
            LogUtil.i(TAG, "LinkCheck isRecordMtr ptotocal=tcp , useTime=" + j);
            j2 = 1000;
        } else if (i == 2) {
            LogUtil.i(TAG, "LinkCheck isRecordMtr ptotocal=udp , useTime=" + j);
            j2 = LooperMessageLoggingManager.LAG_TIME;
        } else if (i != 3) {
            j2 = 0;
        } else {
            LogUtil.i(TAG, "LinkCheck isRecordMtr ptotocal=kcp , useTime=" + j);
            j2 = b.f1465a;
        }
        return j > j2;
    }

    /* JADX WARN: Removed duplicated region for block: B:174:0x0263  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0281 A[PHI: r13 r14
  0x0281: PHI (r13v8 java.lang.Process) = (r13v6 java.lang.Process), (r13v7 java.lang.Process), (r13v10 java.lang.Process) binds: [B:174:0x0263, B:179:0x027f, B:157:0x0238] A[DONT_GENERATE, DONT_INLINE]
  0x0281: PHI (r14v4 int) = (r14v2 int), (r14v3 int), (r14v7 int) binds: [B:174:0x0263, B:179:0x027f, B:157:0x0238] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:185:0x0289  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int ping(java.lang.String r21, int r22, int r23) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 653
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.protocolCheck.ProtocolCheck.ping(java.lang.String, int, int):int");
    }

    private static void processDestroy(Process process) {
        if (process != null) {
            try {
                if (process.exitValue() != 0) {
                    killProcess(process);
                }
            } catch (IllegalThreadStateException unused) {
                killProcess(process);
            }
        }
    }

    private static void killProcess(Process process) {
        int processId = getProcessId(process);
        if (processId != 0) {
            try {
                try {
                    Process.killProcess(processId);
                } catch (Exception unused) {
                    process.destroy();
                }
            } catch (Exception unused2) {
            }
        }
    }

    private static int getProcessId(Process process) {
        String string = process.toString();
        try {
            return Integer.parseInt(string.substring(string.indexOf("=") + 1, string.indexOf("]")));
        } catch (Exception unused) {
            return 0;
        }
    }

    public int dns(String str) throws UnknownHostException {
        InetAddress[] allByName;
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            allByName = InetAddress.getAllByName(str);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            allByName = null;
        }
        for (InetAddress inetAddress : allByName) {
            String hostAddress = inetAddress.getHostAddress();
            LogUtil.i(TAG, "dns ip=" + hostAddress);
            arrayList.add(hostAddress);
        }
        if (arrayList.size() <= 0) {
            return 11;
        }
        this.mCheckResult.setmIpList(arrayList);
        this.mListener.callBack(this.mCheckResult);
        return 0;
    }

    /* renamed from: com.netease.pharos.protocolCheck.ProtocolCheck$2 */
    class AnonymousClass2 implements Runnable {
        final /* synthetic */ InputStream val$input;

        AnonymousClass2(InputStream inputStream) {
            inputStream = inputStream;
        }

        @Override // java.lang.Runnable
        public void run() throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while (true) {
                try {
                    try {
                        try {
                            String line = bufferedReader.readLine();
                            if (line != null) {
                                System.out.println(line);
                                LogUtil.i(ProtocolCheck.TAG, line);
                            } else {
                                inputStream.close();
                                return;
                            }
                        } catch (Throwable th) {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            throw th;
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        inputStream.close();
                        return;
                    }
                } catch (IOException e3) {
                    e3.printStackTrace();
                    return;
                }
            }
        }
    }

    public void printMessage(InputStream inputStream) {
        new Thread(new Runnable() { // from class: com.netease.pharos.protocolCheck.ProtocolCheck.2
            final /* synthetic */ InputStream val$input;

            AnonymousClass2(InputStream inputStream2) {
                inputStream = inputStream2;
            }

            @Override // java.lang.Runnable
            public void run() throws IOException {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while (true) {
                    try {
                        try {
                            try {
                                String line = bufferedReader.readLine();
                                if (line != null) {
                                    System.out.println(line);
                                    LogUtil.i(ProtocolCheck.TAG, line);
                                } else {
                                    inputStream.close();
                                    return;
                                }
                            } catch (Throwable th) {
                                try {
                                    inputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                throw th;
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            inputStream.close();
                            return;
                        }
                    } catch (IOException e3) {
                        e3.printStackTrace();
                        return;
                    }
                }
            }
        }).start();
    }

    public void clean() {
        this.mTask.setIsOpen(false);
        LogUtil.i(TAG, "33333 isOpen=" + this.mTask.mIsOpen);
    }
}