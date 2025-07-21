package com.netease.pharos.linkcheck;

import com.netease.pharos.PharosProxy;
import com.netease.pharos.config.CheckResult;
import com.netease.pharos.protocolCheck.ProtocolCheckListener;
import com.netease.pharos.protocolCheck.ProtocolCheckProxy;
import com.netease.pharos.qos.QosProxy;
import com.netease.pharos.threadManager.ThreadPoolManager;
import com.netease.pharos.util.LogUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes4.dex */
public class ScanProxy {
    private static final String TAG = "ScanProxy";
    private static ScanProxy sScanProxy;
    private volatile ArrayList<String> mCheckTypeList = new ArrayList<>();
    private final ArrayList<String> mCheckCycleTypeList = new ArrayList<>();
    private CycleTaskStopListener mCycleTaskStopListener = null;
    private ConfigInfoListener mConfigInfoListener = null;
    private final CheckOverNotifyListener mCheckOverNotifyListener = new CheckOverNotifyListener() { // from class: com.netease.pharos.linkcheck.ScanProxy.1
        AnonymousClass1() {
        }

        @Override // com.netease.pharos.linkcheck.CheckOverNotifyListener
        public void callBack(String str) {
            if (!ScanProxy.this.mCheckCycleTypeList.contains(str)) {
                ScanProxy.this.mCheckCycleTypeList.add(str);
            }
            CopyOnWriteArrayList<String> copyOnWriteArrayList = Proxy.getInstance().getmCycleList();
            ArrayList arrayList = new ArrayList();
            Iterator<String> it = copyOnWriteArrayList.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next());
            }
            try {
                LogUtil.i(ScanProxy.TAG, "mCheckCycleTypeList\u5faa\u73af debug=" + PharosProxy.getInstance().isDebug());
                if (PharosProxy.getInstance().isDebug()) {
                    LogUtil.i(ScanProxy.TAG, "mCheckCycleTypeList\u5faa\u73af=" + ScanProxy.this.mCheckCycleTypeList.toString() + ", pList=" + arrayList);
                }
            } catch (Exception e) {
                LogUtil.w(ScanProxy.TAG, "mCheckCycleTypeList\u5faa\u73af Exception=" + e);
            }
            if (copyOnWriteArrayList == null || copyOnWriteArrayList.size() <= 0 || !ScanProxy.this.mCheckCycleTypeList.containsAll(copyOnWriteArrayList)) {
                return;
            }
            ScanProxy.this.mCheckCycleTypeList.clear();
            QosProxy.getInstance().clean();
            QosProxy.getInstance().start_qosCore();
            Proxy.getInstance().setmPharosResultCache(Proxy.getInstance().getPharosResultInfo());
        }
    };
    private final ProtocolCheckListener mListener = new ProtocolCheckListener() { // from class: com.netease.pharos.linkcheck.ScanProxy.2
        AnonymousClass2() {
        }

        /* JADX WARN: Can't wrap try/catch for region: R(12:79|81|(2:144|82)|86|(4:88|150|89|93)(2:95|(4:97|152|98|102)(7:103|(1:105)(2:106|(1:108)(2:109|(1:111)(2:112|(1:114)(2:115|(1:119)))))|120|(1:126)|146|127|(3:148|132|(1:156)(4:136|(1:138)|139|157))(1:154)))|94|120|(3:122|124|126)|146|127|(0)(0)|(1:(0))) */
        /* JADX WARN: Code restructure failed: missing block: B:129:0x0289, code lost:
        
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:130:0x028a, code lost:
        
            com.netease.pharos.util.LogUtil.e(com.netease.pharos.linkcheck.ScanProxy.TAG, "LinkCheckListener callBack Exception2 =" + r0);
         */
        /* JADX WARN: Removed duplicated region for block: B:148:0x029d A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:154:? A[RETURN, SYNTHETIC] */
        @Override // com.netease.pharos.protocolCheck.ProtocolCheckListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void callBack(com.netease.pharos.config.CheckResult r18) throws java.lang.NumberFormatException {
            /*
                Method dump skipped, instructions count: 763
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.linkcheck.ScanProxy.AnonymousClass2.callBack(com.netease.pharos.config.CheckResult):void");
        }
    };

    private ScanProxy() {
    }

    public static ScanProxy getInstance() {
        if (sScanProxy == null) {
            sScanProxy = new ScanProxy();
        }
        return sScanProxy;
    }

    public ArrayList<String> getmCycleList() {
        return this.mCheckTypeList;
    }

    public void setmCycleList(ArrayList<String> arrayList) {
        this.mCheckTypeList = arrayList;
    }

    public void init(CycleTaskStopListener cycleTaskStopListener, ConfigInfoListener configInfoListener) {
        this.mCycleTaskStopListener = cycleTaskStopListener;
        this.mConfigInfoListener = configInfoListener;
    }

    /* renamed from: com.netease.pharos.linkcheck.ScanProxy$1 */
    class AnonymousClass1 implements CheckOverNotifyListener {
        AnonymousClass1() {
        }

        @Override // com.netease.pharos.linkcheck.CheckOverNotifyListener
        public void callBack(String str) {
            if (!ScanProxy.this.mCheckCycleTypeList.contains(str)) {
                ScanProxy.this.mCheckCycleTypeList.add(str);
            }
            CopyOnWriteArrayList<String> copyOnWriteArrayList = Proxy.getInstance().getmCycleList();
            ArrayList arrayList = new ArrayList();
            Iterator<String> it = copyOnWriteArrayList.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next());
            }
            try {
                LogUtil.i(ScanProxy.TAG, "mCheckCycleTypeList\u5faa\u73af debug=" + PharosProxy.getInstance().isDebug());
                if (PharosProxy.getInstance().isDebug()) {
                    LogUtil.i(ScanProxy.TAG, "mCheckCycleTypeList\u5faa\u73af=" + ScanProxy.this.mCheckCycleTypeList.toString() + ", pList=" + arrayList);
                }
            } catch (Exception e) {
                LogUtil.w(ScanProxy.TAG, "mCheckCycleTypeList\u5faa\u73af Exception=" + e);
            }
            if (copyOnWriteArrayList == null || copyOnWriteArrayList.size() <= 0 || !ScanProxy.this.mCheckCycleTypeList.containsAll(copyOnWriteArrayList)) {
                return;
            }
            ScanProxy.this.mCheckCycleTypeList.clear();
            QosProxy.getInstance().clean();
            QosProxy.getInstance().start_qosCore();
            Proxy.getInstance().setmPharosResultCache(Proxy.getInstance().getPharosResultInfo());
        }
    }

    /* renamed from: com.netease.pharos.linkcheck.ScanProxy$2 */
    class AnonymousClass2 implements ProtocolCheckListener {
        AnonymousClass2() {
        }

        @Override // com.netease.pharos.protocolCheck.ProtocolCheckListener
        public void callBack(CheckResult v) throws NumberFormatException {
            /*
                Method dump skipped, instructions count: 763
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.linkcheck.ScanProxy.AnonymousClass2.callBack(com.netease.pharos.config.CheckResult):void");
        }
    }

    public void start() {
        LogUtil.i(TAG, "ScanProxy [start] start");
        if (RegionConfigInfo.getInstance().getmResult() != null && RegionConfigInfo.getInstance().getmResult().length() > 0) {
            StringBuilder sb = new StringBuilder("\u6a21\u62df\u7684\u6570\u636e= ");
            sb.append(RegionConfigInfo.getInstance().getmResult());
            LogUtil.i(TAG, sb.toString() != null ? RegionConfigInfo.getInstance().getmResult().toString() : "result is null");
        }
        this.mCheckTypeList.clear();
        ArrayList arrayList = new ArrayList();
        arrayList.add(ThreadPoolManager.getInstance().getFixedThreadPool().submit(createScanCore("nap_icmp")));
        arrayList.add(ThreadPoolManager.getInstance().getFixedThreadPool().submit(createScanCore("rap_icmp")));
        arrayList.add(ThreadPoolManager.getInstance().getFixedThreadPool().submit(createScanCore("rap_udp")));
        arrayList.add(ThreadPoolManager.getInstance().getFixedThreadPool().submit(createScanCore("rap_transfer")));
        arrayList.add(ThreadPoolManager.getInstance().getFixedThreadPool().submit(createScanCore("sap_udp")));
        arrayList.add(ThreadPoolManager.getInstance().getFixedThreadPool().submit(createScanCore("sap_transfer")));
        arrayList.add(ThreadPoolManager.getInstance().getFixedThreadPool().submit(createScanCore("resolve")));
    }

    public ScanCore createScanCore(String str) {
        ScanCore scanCore = new ScanCore();
        scanCore.setProtocolProxyProxy(ProtocolCheckProxy.getInstance());
        scanCore.init(str, this.mListener, this.mCycleTaskStopListener, this.mConfigInfoListener, this.mCheckOverNotifyListener);
        return scanCore;
    }
}