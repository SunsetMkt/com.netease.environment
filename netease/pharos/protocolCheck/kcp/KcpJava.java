package com.netease.pharos.protocolCheck.kcp;

import java.util.ArrayList;
import java.util.Iterator;
import org.xbill.DNS.TTL;

/* loaded from: classes5.dex */
public abstract class KcpJava {
    long conv;
    public final int IKCP_RTO_NDL = 30;
    public final int IKCP_RTO_MIN = 100;
    public final int IKCP_RTO_DEF = 200;
    public final int IKCP_RTO_MAX = 60000;
    public final int IKCP_CMD_PUSH = 81;
    public final int IKCP_CMD_ACK = 82;
    public final int IKCP_CMD_WASK = 83;
    public final int IKCP_CMD_WINS = 84;
    public final int IKCP_ASK_SEND = 1;
    public final int IKCP_ASK_TELL = 2;
    public final int IKCP_WND_SND = 32;
    public final int IKCP_WND_RCV = 32;
    public final int IKCP_MTU_DEF = 1400;
    public final int IKCP_ACK_FAST = 3;
    public final int IKCP_INTERVAL = 100;
    public final int IKCP_OVERHEAD = 24;
    public final int IKCP_DEADLINK = 10;
    public final int IKCP_THRESH_INIT = 2;
    public final int IKCP_THRESH_MIN = 2;
    public final int IKCP_PROBE_INIT = 7000;
    public final int IKCP_PROBE_LIMIT = 120000;
    long snd_una = 0;
    long snd_nxt = 0;
    long rcv_nxt = 0;
    long ts_recent = 0;
    long ts_lastack = 0;
    long ts_probe = 0;
    long probe_wait = 0;
    long snd_wnd = 32;
    long rcv_wnd = 32;
    long rmt_wnd = 32;
    long cwnd = 0;
    long incr = 0;
    long probe = 0;
    long mtu = 1400;
    long mss = 1400 - 24;
    byte[] buffer = new byte[((int) (1400 + 24)) * 3];
    ArrayList<Segment> nrcv_buf = new ArrayList<>(128);
    ArrayList<Segment> nsnd_buf = new ArrayList<>(128);
    ArrayList<Segment> nrcv_que = new ArrayList<>(128);
    ArrayList<Segment> nsnd_que = new ArrayList<>(128);
    long state = 0;
    ArrayList<Long> acklist = new ArrayList<>(128);
    long rx_srtt = 0;
    long rx_rttval = 0;
    long rx_rto = 200;
    long rx_minrto = 100;
    long current = 0;
    long interval = 100;
    long ts_flush = 100;
    long nodelay = 0;
    long updated = 0;
    long logmask = 0;
    long ssthresh = 2;
    long fastresend = 0;
    long nocwnd = 0;
    long xmit = 0;
    long dead_link = 10;

    static long _imax_(long j, long j2) {
        return j >= j2 ? j : j2;
    }

    static long _imin_(long j, long j2) {
        return j <= j2 ? j : j2;
    }

    static int _itimediff(long j, long j2) {
        return (int) (j - j2);
    }

    protected abstract void output(byte[] bArr, int i);

    public static void ikcp_encode8u(byte[] bArr, int i, byte b) {
        bArr[i + 0] = b;
    }

    public static byte ikcp_decode8u(byte[] bArr, int i) {
        return bArr[i + 0];
    }

    public static void ikcp_encode16u(byte[] bArr, int i, int i2) {
        bArr[i + 1] = (byte) (i2 >> 8);
        bArr[i + 0] = (byte) (i2 >> 0);
    }

    public static int ikcp_decode16u(byte[] bArr, int i) {
        return (bArr[i + 0] & 255) | ((bArr[i + 1] & 255) << 8);
    }

    public static void ikcp_encode32u(byte[] bArr, int i, long j) {
        bArr[i + 3] = (byte) (j >> 24);
        bArr[i + 2] = (byte) (j >> 16);
        bArr[i + 1] = (byte) (j >> 8);
        bArr[i + 0] = (byte) (j >> 0);
    }

    public static long ikcp_decode32u(byte[] bArr, int i) {
        return (bArr[i + 0] & 255) | ((bArr[i + 3] & 255) << 24) | ((bArr[i + 2] & 255) << 16) | ((bArr[i + 1] & 255) << 8);
    }

    public static void slice(ArrayList arrayList, int i, int i2) {
        int size = arrayList.size();
        for (int i3 = 0; i3 < size; i3++) {
            int i4 = i2 - i;
            if (i3 < i4) {
                arrayList.set(i3, arrayList.get(i3 + i));
            } else {
                arrayList.remove(i4);
            }
        }
    }

    static long _ibound_(long j, long j2, long j3) {
        return _imin_(_imax_(j, j2), j3);
    }

    private class Segment {
        protected byte[] data;
        protected long conv = 0;
        protected long cmd = 0;
        protected long frg = 0;
        protected long wnd = 0;
        protected long ts = 0;
        protected long sn = 0;
        protected long una = 0;
        protected long resendts = 0;
        protected long rto = 0;
        protected long fastack = 0;
        protected long xmit = 0;

        protected Segment(int i) {
            this.data = new byte[i];
        }

        protected int encode(byte[] bArr, int i) {
            KcpJava.ikcp_encode32u(bArr, i, this.conv);
            int i2 = i + 4;
            KcpJava.ikcp_encode8u(bArr, i2, (byte) this.cmd);
            int i3 = i2 + 1;
            KcpJava.ikcp_encode8u(bArr, i3, (byte) this.frg);
            int i4 = i3 + 1;
            KcpJava.ikcp_encode16u(bArr, i4, (int) this.wnd);
            int i5 = i4 + 2;
            KcpJava.ikcp_encode32u(bArr, i5, this.ts);
            int i6 = i5 + 4;
            KcpJava.ikcp_encode32u(bArr, i6, this.sn);
            int i7 = i6 + 4;
            KcpJava.ikcp_encode32u(bArr, i7, this.una);
            int i8 = i7 + 4;
            KcpJava.ikcp_encode32u(bArr, i8, this.data.length);
            return (i8 + 4) - i;
        }
    }

    public KcpJava(long j) {
        this.conv = 0L;
        this.conv = j;
    }

    public int PeekSize() {
        if (this.nrcv_que.size() == 0) {
            return -1;
        }
        int length = 0;
        Segment segment = this.nrcv_que.get(0);
        if (0 == segment.frg) {
            return segment.data.length;
        }
        if (this.nrcv_que.size() < segment.frg + 1) {
            return -1;
        }
        Iterator<Segment> it = this.nrcv_que.iterator();
        while (it.hasNext()) {
            Segment next = it.next();
            length += next.data.length;
            if (0 == next.frg) {
                break;
            }
        }
        return length;
    }

    public int Recv(byte[] bArr) {
        if (this.nrcv_que.size() == 0) {
            return -1;
        }
        int iPeekSize = PeekSize();
        if (iPeekSize < 0) {
            return -2;
        }
        if (iPeekSize > bArr.length) {
            return -3;
        }
        int i = 0;
        boolean z = ((long) this.nrcv_que.size()) >= this.rcv_wnd;
        Iterator<Segment> it = this.nrcv_que.iterator();
        int length = 0;
        int i2 = 0;
        while (it.hasNext()) {
            Segment next = it.next();
            System.arraycopy(next.data, 0, bArr, length, next.data.length);
            length += next.data.length;
            i2++;
            if (0 == next.frg) {
                break;
            }
        }
        if (i2 > 0) {
            ArrayList<Segment> arrayList = this.nrcv_que;
            slice(arrayList, i2, arrayList.size());
        }
        Iterator<Segment> it2 = this.nrcv_buf.iterator();
        while (it2.hasNext()) {
            Segment next2 = it2.next();
            if (next2.sn != this.rcv_nxt || this.nrcv_que.size() >= this.rcv_wnd) {
                break;
            }
            this.nrcv_que.add(next2);
            this.rcv_nxt++;
            i++;
        }
        if (i > 0) {
            ArrayList<Segment> arrayList2 = this.nrcv_buf;
            slice(arrayList2, i, arrayList2.size());
        }
        if (this.nrcv_que.size() < this.rcv_wnd && z) {
            this.probe |= 2;
        }
        return length;
    }

    public int Send(byte[] bArr) {
        if (bArr.length == 0) {
            return -1;
        }
        long length = bArr.length;
        long j = this.mss;
        int length2 = length < j ? 1 : ((int) ((bArr.length + j) - 1)) / ((int) j);
        if (255 < length2) {
            return -2;
        }
        if (length2 == 0) {
            length2 = 1;
        }
        long length3 = bArr.length;
        int i = 0;
        for (int i2 = 0; i2 < length2; i2++) {
            long j2 = this.mss;
            if (length3 <= j2) {
                j2 = length3;
            }
            int i3 = (int) j2;
            Segment segment = new Segment(i3);
            System.out.printf("the size is %d\n", Integer.valueOf(i3));
            System.out.printf("the offset is %d\n", Integer.valueOf(i));
            System.out.printf("the buffer is %d\n", Integer.valueOf(bArr.length));
            System.arraycopy(bArr, i, segment.data, 0, i3);
            i += i3;
            segment.frg = (length2 - i2) - 1;
            this.nsnd_que.add(segment);
            long j3 = this.mss;
            if (length3 > j3) {
                length3 -= j3;
            }
        }
        return 0;
    }

    void update_ack(int i) {
        long j = this.rx_srtt;
        if (0 == j) {
            this.rx_srtt = i;
            this.rx_rttval = i / 2;
        } else {
            long j2 = i;
            int i2 = (int) (j2 - j);
            if (i2 < 0) {
                i2 = -i2;
            }
            this.rx_rttval = ((this.rx_rttval * 3) + i2) / 4;
            long j3 = ((j * 7) + j2) / 8;
            this.rx_srtt = j3;
            if (j3 < 1) {
                this.rx_srtt = 1L;
            }
        }
        this.rx_rto = _ibound_(this.rx_minrto, (int) (this.rx_srtt + _imax_(1L, this.rx_rttval * 4)), 60000L);
    }

    void shrink_buf() {
        if (this.nsnd_buf.size() > 0) {
            this.snd_una = this.nsnd_buf.get(0).sn;
        } else {
            this.snd_una = this.snd_nxt;
        }
    }

    void parse_ack(long j) {
        if (_itimediff(j, this.snd_una) < 0 || _itimediff(j, this.snd_nxt) >= 0) {
            return;
        }
        Iterator<Segment> it = this.nsnd_buf.iterator();
        int i = 0;
        while (it.hasNext()) {
            Segment next = it.next();
            if (j == next.sn) {
                this.nsnd_buf.remove(i);
                return;
            } else {
                next.fastack++;
                i++;
            }
        }
    }

    void parse_una(long j) {
        Iterator<Segment> it = this.nsnd_buf.iterator();
        int i = 0;
        while (it.hasNext() && _itimediff(j, it.next().sn) > 0) {
            i++;
        }
        if (i > 0) {
            ArrayList<Segment> arrayList = this.nsnd_buf;
            slice(arrayList, i, arrayList.size());
        }
    }

    void ack_push(long j, long j2) {
        this.acklist.add(Long.valueOf(j));
        this.acklist.add(Long.valueOf(j2));
    }

    void parse_data(Segment segment) {
        int i;
        boolean z;
        long j = segment.sn;
        if (_itimediff(j, this.rcv_nxt + this.rcv_wnd) >= 0 || _itimediff(j, this.rcv_nxt) < 0) {
            return;
        }
        int size = this.nrcv_buf.size() - 1;
        while (true) {
            i = 0;
            if (size < 0) {
                size = -1;
                break;
            }
            Segment segment2 = this.nrcv_buf.get(size);
            if (segment2.sn == j) {
                z = true;
                size = -1;
                break;
            } else if (_itimediff(j, segment2.sn) > 0) {
                break;
            } else {
                size--;
            }
        }
        z = false;
        if (!z) {
            if (size == -1) {
                this.nrcv_buf.add(0, segment);
            } else {
                this.nrcv_buf.add(size + 1, segment);
            }
        }
        Iterator<Segment> it = this.nrcv_buf.iterator();
        while (it.hasNext()) {
            Segment next = it.next();
            if (next.sn != this.rcv_nxt || this.nrcv_que.size() >= this.rcv_wnd) {
                break;
            }
            this.nrcv_que.add(next);
            this.rcv_nxt++;
            i++;
        }
        if (i > 0) {
            ArrayList<Segment> arrayList = this.nrcv_buf;
            slice(arrayList, i, arrayList.size());
        }
    }

    public int Input(byte[] bArr) {
        int i;
        int i2;
        long j = this.snd_una;
        int i3 = 0;
        if (bArr.length < 24) {
            return 0;
        }
        int i4 = 0;
        for (int i5 = 24; bArr.length - i4 >= i5; i5 = 24) {
            long jIkcp_decode32u = ikcp_decode32u(bArr, i4);
            int i6 = i4 + 4;
            if (this.conv != jIkcp_decode32u) {
                return -1;
            }
            byte bIkcp_decode8u = ikcp_decode8u(bArr, i6);
            int i7 = i6 + 1;
            byte bIkcp_decode8u2 = ikcp_decode8u(bArr, i7);
            int i8 = i7 + 1;
            int iIkcp_decode16u = ikcp_decode16u(bArr, i8);
            int i9 = i8 + 2;
            long jIkcp_decode32u2 = ikcp_decode32u(bArr, i9);
            int i10 = i9 + 4;
            long jIkcp_decode32u3 = ikcp_decode32u(bArr, i10);
            int i11 = i10 + 4;
            long jIkcp_decode32u4 = ikcp_decode32u(bArr, i11);
            int i12 = i11 + 4;
            long j2 = j;
            long jIkcp_decode32u5 = ikcp_decode32u(bArr, i12);
            int i13 = i12 + 4;
            if (bArr.length - i13 < jIkcp_decode32u5) {
                return -2;
            }
            if (bIkcp_decode8u != 81 && bIkcp_decode8u != 82 && bIkcp_decode8u != 83 && bIkcp_decode8u != 84) {
                return -3;
            }
            long j3 = iIkcp_decode16u;
            this.rmt_wnd = j3;
            parse_una(jIkcp_decode32u4);
            shrink_buf();
            if (82 == bIkcp_decode8u) {
                if (_itimediff(this.current, jIkcp_decode32u2) >= 0) {
                    update_ack(_itimediff(this.current, jIkcp_decode32u2));
                }
                parse_ack(jIkcp_decode32u3);
                shrink_buf();
                i = i13;
            } else {
                if (81 == bIkcp_decode8u) {
                    if (_itimediff(jIkcp_decode32u3, this.rcv_nxt + this.rcv_wnd) < 0) {
                        ack_push(jIkcp_decode32u3, jIkcp_decode32u2);
                        if (_itimediff(jIkcp_decode32u3, this.rcv_nxt) >= 0) {
                            int i14 = (int) jIkcp_decode32u5;
                            Segment segment = new Segment(i14);
                            segment.conv = jIkcp_decode32u;
                            segment.cmd = bIkcp_decode8u;
                            segment.frg = bIkcp_decode8u2;
                            segment.wnd = j3;
                            segment.ts = jIkcp_decode32u2;
                            segment.sn = jIkcp_decode32u3;
                            segment.una = jIkcp_decode32u4;
                            if (jIkcp_decode32u5 > 0) {
                                i = i13;
                                i2 = 0;
                                System.arraycopy(bArr, i, segment.data, 0, i14);
                            } else {
                                i = i13;
                                i2 = 0;
                            }
                            parse_data(segment);
                        }
                    }
                    i = i13;
                } else {
                    i = i13;
                    i2 = 0;
                    if (83 == bIkcp_decode8u) {
                        this.probe |= 2;
                    } else if (84 != bIkcp_decode8u) {
                        return -3;
                    }
                }
                i4 = i + ((int) jIkcp_decode32u5);
                i3 = i2;
                j = j2;
            }
            i2 = 0;
            i4 = i + ((int) jIkcp_decode32u5);
            i3 = i2;
            j = j2;
        }
        if (_itimediff(this.snd_una, j) > 0) {
            long j4 = this.cwnd;
            long j5 = this.rmt_wnd;
            if (j4 < j5) {
                long j6 = this.mss;
                if (j4 < this.ssthresh) {
                    this.cwnd = j4 + 1;
                    this.incr += j6;
                } else {
                    if (this.incr < j6) {
                        this.incr = j6;
                    }
                    long j7 = this.incr;
                    long j8 = j7 + ((j6 * j6) / j7) + (j6 / 16);
                    this.incr = j8;
                    if ((j4 + 1) * j6 <= j8) {
                        this.cwnd = j4 + 1;
                    }
                }
                if (this.cwnd > j5) {
                    this.cwnd = j5;
                    this.incr = j5 * j6;
                }
            }
        }
        return i3;
    }

    int wnd_unused() {
        long size = this.nrcv_que.size();
        long j = this.rcv_wnd;
        if (size < j) {
            return ((int) j) - this.nrcv_que.size();
        }
        return 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:80:0x0234  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x027e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void flush() {
        /*
            Method dump skipped, instructions count: 719
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.protocolCheck.kcp.KcpJava.flush():void");
    }

    public void Update(long j) {
        this.current = j;
        if (0 == this.updated) {
            this.updated = 1L;
            this.ts_flush = j;
        }
        int i_itimediff = _itimediff(j, this.ts_flush);
        if (i_itimediff >= 10000 || i_itimediff < -10000) {
            this.ts_flush = this.current;
            i_itimediff = 0;
        }
        if (i_itimediff >= 0) {
            long j2 = this.ts_flush + this.interval;
            this.ts_flush = j2;
            if (_itimediff(this.current, j2) >= 0) {
                this.ts_flush = this.current + this.interval;
            }
            flush();
        }
    }

    public long Check(long j) {
        long j2 = this.ts_flush;
        if (0 == this.updated) {
            return j;
        }
        if (_itimediff(j, j2) >= 10000 || _itimediff(j, j2) < -10000) {
            j2 = j;
        }
        if (_itimediff(j, j2) >= 0) {
            return j;
        }
        long j_itimediff = _itimediff(j2, j);
        Iterator<Segment> it = this.nsnd_buf.iterator();
        long j3 = TTL.MAX_VALUE;
        while (it.hasNext()) {
            int i_itimediff = _itimediff(it.next().resendts, j);
            if (i_itimediff <= 0) {
                return j;
            }
            long j4 = i_itimediff;
            if (j4 < j3) {
                j3 = j4;
            }
        }
        if (j3 < j_itimediff) {
            j_itimediff = j3;
        }
        long j5 = this.interval;
        if (j_itimediff >= j5) {
            j_itimediff = j5;
        }
        return j + j_itimediff;
    }

    public int SetMtu(int i) {
        if (i < 50 || i < 24) {
            return -1;
        }
        long j = i;
        this.mtu = j;
        this.mss = j - 24;
        this.buffer = new byte[(i + 24) * 3];
        return 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:4:0x0004 A[PHI: r0
  0x0004: PHI (r0v3 int) = (r0v0 int), (r0v1 int) binds: [B:3:0x0002, B:6:0x0008] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int Interval(int r3) {
        /*
            r2 = this;
            r0 = 5000(0x1388, float:7.006E-42)
            if (r3 <= r0) goto L6
        L4:
            r3 = r0
            goto Lb
        L6:
            r0 = 10
            if (r3 >= r0) goto Lb
            goto L4
        Lb:
            long r0 = (long) r3
            r2.interval = r0
            r3 = 0
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.protocolCheck.kcp.KcpJava.Interval(int):int");
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0016 A[PHI: r3
  0x0016: PHI (r3v8 int) = (r3v5 int), (r3v6 int) binds: [B:9:0x0014, B:12:0x001a] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int NoDelay(int r3, int r4, int r5, int r6) {
        /*
            r2 = this;
            if (r3 <= 0) goto L10
            long r0 = (long) r3
            r2.nodelay = r0
            if (r3 == 0) goto Lc
            r0 = 30
            r2.rx_minrto = r0
            goto L10
        Lc:
            r0 = 100
            r2.rx_minrto = r0
        L10:
            if (r4 < 0) goto L20
            r3 = 5000(0x1388, float:7.006E-42)
            if (r4 <= r3) goto L18
        L16:
            r4 = r3
            goto L1d
        L18:
            r3 = 10
            if (r4 >= r3) goto L1d
            goto L16
        L1d:
            long r3 = (long) r4
            r2.interval = r3
        L20:
            if (r5 < 0) goto L25
            long r3 = (long) r5
            r2.fastresend = r3
        L25:
            if (r6 < 0) goto L2a
            long r3 = (long) r6
            r2.nocwnd = r3
        L2a:
            r3 = 0
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.protocolCheck.kcp.KcpJava.NoDelay(int, int, int, int):int");
    }

    public int WndSize(int i, int i2) {
        if (i > 0) {
            this.snd_wnd = i;
        }
        if (i2 <= 0) {
            return 0;
        }
        this.rcv_wnd = i2;
        return 0;
    }

    public int WaitSnd() {
        return this.nsnd_buf.size() + this.nsnd_que.size();
    }
}