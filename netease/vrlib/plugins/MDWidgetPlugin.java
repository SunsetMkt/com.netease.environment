package com.netease.vrlib.plugins;

import com.netease.vrlib.model.MDHotspotBuilder;

/* loaded from: classes5.dex */
public class MDWidgetPlugin extends MDHotspotPlugin {
    private static final int STATUS_FOCUSED = 1;
    private static final int STATUS_NORMAL = 0;
    private boolean mChecked;
    private int[] mCheckedStatusList;
    private int mCurrentStatus;
    private int[] mStatusList;

    public MDWidgetPlugin(MDHotspotBuilder mDHotspotBuilder) {
        super(mDHotspotBuilder);
        this.mCurrentStatus = 0;
        this.mStatusList = mDHotspotBuilder.statusList;
        this.mCheckedStatusList = mDHotspotBuilder.checkedStatusList;
        if (this.mStatusList == null) {
            this.mStatusList = new int[]{0, 0, 0};
        }
    }

    public void setChecked(boolean z) {
        this.mChecked = z;
        updateStatus();
    }

    public boolean getChecked() {
        return this.mChecked;
    }

    @Override // com.netease.vrlib.plugins.MDHotspotPlugin, com.netease.vrlib.plugins.IMDHotspot
    public void onEyeHitIn(long j) {
        super.onEyeHitIn(j);
        this.mCurrentStatus = 1;
        updateStatus();
    }

    @Override // com.netease.vrlib.plugins.MDHotspotPlugin, com.netease.vrlib.plugins.IMDHotspot
    public void onEyeHitOut() {
        super.onEyeHitOut();
        this.mCurrentStatus = 0;
        updateStatus();
    }

    private void updateStatus() {
        int i;
        int[] iArr = this.mChecked ? this.mCheckedStatusList : this.mStatusList;
        if (iArr == null) {
            iArr = this.mStatusList;
        }
        if (iArr == null || (i = this.mCurrentStatus) >= iArr.length) {
            return;
        }
        useTexture(iArr[i]);
    }
}