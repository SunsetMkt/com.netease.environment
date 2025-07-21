package com.netease.pharos.protocolCheck.kcp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/* loaded from: classes5.dex */
public class KcpJavaClient extends KcpJava {
    public DatagramSocket mDatagramSocket;
    private InetAddress mInetAddress;
    private final int mPort;

    public KcpJavaClient(long j, String str, int i) throws IOException {
        super(j);
        this.mDatagramSocket = null;
        this.mInetAddress = null;
        this.mDatagramSocket = new DatagramSocket();
        this.mInetAddress = InetAddress.getByName(str);
        this.mPort = i;
    }

    @Override // com.netease.pharos.protocolCheck.kcp.KcpJava
    public void output(byte[] bArr, int i) throws IOException {
        try {
            this.mDatagramSocket.send(new DatagramPacket(bArr, i, this.mInetAddress, this.mPort));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}