package com.netease.ntunisdk.modules.permission.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import com.netease.ntunisdk.modules.base.utils.LogModule;
import com.netease.ntunisdk.modules.permission.R;
import com.netease.ntunisdk.modules.permission.common.PermissionConstant;
import com.netease.ntunisdk.modules.permission.core.PermissionHandler;
import com.netease.ntunisdk.modules.permission.core.PermissionRequest;
import com.netease.ntunisdk.modules.permission.utils.PermissionTextUtils;
import com.netease.ntunisdk.modules.permission.utils.PermissionUtils;
import java.util.Arrays;
import java.util.Map;

/* loaded from: classes.dex */
public class PermissionFragment extends Fragment {
    private static final String TAG = "PermissionFragment";
    private Context context;
    private Map<String, PermissionHandler> permissionHandlerMap;
    private String permissionString = "";

    @Override // android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setPermissionHandlerMap(Map<String, PermissionHandler> map) {
        this.permissionHandlerMap = map;
    }

    public void classifyPermission(PermissionHandler permissionHandler) {
        LogModule.d(TAG, "Thread: " + Thread.currentThread().getName());
        String[] permissionArray = permissionHandler.getPermissionArray();
        if (isSpecialPermission(permissionArray[0])) {
            LogModule.d(TAG, "requestSensitivePermission: " + permissionArray[0]);
            requestSpecialPermission(permissionHandler);
            return;
        }
        LogModule.d(TAG, "requestNormalPermission: " + Arrays.toString(permissionArray));
        requestNormalPermission(permissionHandler);
    }

    private boolean isSpecialPermission(String str) {
        return TextUtils.equals(str, PermissionConstant.PERMISSION_WRITE_SETTINGS) || TextUtils.equals(str, PermissionConstant.PERMISSION_SYSTEM_ALERT_WINDOW);
    }

    protected void requestSpecialPermission(PermissionHandler permissionHandler) {
        String[] permissionArray = permissionHandler.getPermissionArray();
        if (TextUtils.equals(permissionArray[0], PermissionConstant.PERMISSION_WRITE_SETTINGS)) {
            LogModule.d(TAG, "requestWriteSettingsPermission ");
            requestWriteSettingsPermission(permissionHandler);
        }
        if (TextUtils.equals(permissionArray[0], PermissionConstant.PERMISSION_SYSTEM_ALERT_WINDOW)) {
            LogModule.d(TAG, "requestSystemAlertWindow ");
            requestSystemAlertWindow(permissionHandler);
        }
    }

    protected void requestWriteSettingsPermission(PermissionHandler permissionHandler) {
        String[] permissionArray = permissionHandler.getPermissionArray();
        if (Build.VERSION.SDK_INT >= 23) {
            if (PermissionUtils.hasAllPermissions(this.context, permissionArray) || Settings.System.canWrite(this.context)) {
                LogModule.d(TAG, "requestWriteSettingsPermission: permissionHandler.getPermissionRequestProxy().onNeed()");
                permissionHandler.getPermissionRequestProxy().onPermissionResult(new int[]{0}, permissionHandler, this);
            } else {
                LogModule.d(TAG, "requestWriteSettingsPermission: chooseDisplayDialog");
                showDialog(permissionHandler);
            }
        }
    }

    protected void requestSystemAlertWindow(PermissionHandler permissionHandler) {
        String[] permissionArray = permissionHandler.getPermissionArray();
        if (Build.VERSION.SDK_INT >= 23) {
            if (PermissionUtils.hasAllPermissions(this.context, permissionArray) || Settings.canDrawOverlays(this.context)) {
                permissionHandler.getPermissionRequestProxy().onPermissionResult(new int[]{0}, permissionHandler, this);
            } else {
                showDialog(permissionHandler);
            }
        }
    }

    private void showDialog(final PermissionHandler permissionHandler) {
        LogModule.d(TAG, "chooseDisplayDialog");
        if (permissionHandler.getShowDialog()) {
            PermissionDialog permissionDialog = new PermissionDialog(this.context);
            permissionDialog.setCallback(new PermissionDialogListener() { // from class: com.netease.ntunisdk.modules.permission.ui.PermissionFragment.1
                @Override // com.netease.ntunisdk.modules.permission.ui.PermissionDialogListener
                public void refuse() {
                    permissionHandler.getPermissionRequestProxy().refuse(PermissionUtils.getGrantResults(PermissionFragment.this.context, permissionHandler.getPermissionArray()), permissionHandler, PermissionFragment.this);
                    LogModule.d(PermissionFragment.TAG, "chooseDisplayDialog: refuse");
                }

                @Override // com.netease.ntunisdk.modules.permission.ui.PermissionDialogListener
                public void allow() {
                    permissionHandler.getPermissionRequestProxy().proceed(PermissionFragment.this.permissionString, PermissionFragment.this);
                    LogModule.d(PermissionFragment.TAG, "chooseDisplayDialog: proceed");
                }
            });
            permissionDialog.show();
            if (!TextUtils.isEmpty(permissionHandler.getPositiveText())) {
                permissionDialog.setPositiveButtonText(permissionHandler.getPositiveText());
            }
            if (!TextUtils.isEmpty(permissionHandler.getNegativeText())) {
                permissionDialog.setNegativeButtonText(permissionHandler.getNegativeText());
            }
            String firstText = permissionHandler.getFirstText();
            if (!TextUtils.isEmpty(firstText)) {
                permissionDialog.setMessage(firstText);
                return;
            } else {
                permissionDialog.dismiss();
                return;
            }
        }
        permissionHandler.getPermissionRequestProxy().proceed(this.permissionString, this);
        LogModule.d(TAG, "show_dialog_flag is false, needWithTwoBtnDialog: proceed");
    }

    @Override // android.app.Fragment
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        PermissionHandler permissionHandler;
        super.onRequestPermissionsResult(i, strArr, iArr);
        LogModule.d(TAG, "Handle onRequestPermissionsResult");
        String[] permissionArray = new String[0];
        PermissionRequest permissionRequestProxy = null;
        if (strArr == null || strArr.length <= 0) {
            permissionHandler = null;
        } else {
            StringBuilder sb = new StringBuilder();
            for (String str : strArr) {
                sb.append(str);
                sb.append(",");
            }
            permissionHandler = this.permissionHandlerMap.get(sb.substring(0, sb.length() - 1));
        }
        if (permissionHandler != null) {
            permissionArray = permissionHandler.getPermissionArray();
            permissionRequestProxy = permissionHandler.getPermissionRequestProxy();
        }
        if (permissionRequestProxy == null || i != 1336) {
            return;
        }
        if (PermissionUtils.verifyPermissions(iArr)) {
            permissionRequestProxy.onPermissionResult(iArr, permissionHandler, this);
            return;
        }
        PermissionUtils.putAllAskAgain((Activity) this.context, iArr, permissionArray);
        if (PermissionUtils.shouldShowRequestPermissionRationale((Activity) this.context, permissionArray) && permissionHandler.getShowDialog()) {
            permissionRequestProxy.onPermissionResult(iArr, permissionHandler, this);
        } else if (PermissionUtils.getBooleanArray(this.context, permissionArray, false) && permissionHandler.getGotoSetting()) {
            goToSetting(permissionHandler);
        } else {
            permissionRequestProxy.onPermissionResult(iArr, permissionHandler, this);
        }
    }

    protected void requestNormalPermission(PermissionHandler permissionHandler) {
        String[] permissionArray = permissionHandler.getPermissionArray();
        int[] grantResults = PermissionUtils.getGrantResults(this.context, permissionArray);
        if (PermissionUtils.hasAllPermissions(this.context, permissionArray)) {
            permissionHandler.getPermissionRequestProxy().onPermissionResult(grantResults, permissionHandler, this);
            return;
        }
        if (PermissionUtils.shouldShowRequestPermissionRationale((Activity) this.context, permissionArray)) {
            showDialog(permissionHandler);
            return;
        }
        if (!PermissionUtils.getBooleanArray(this.context, permissionArray, false)) {
            if (!PermissionUtils.grantOrNotAskAgain(this.context, permissionArray)) {
                showDialog(permissionHandler);
                return;
            } else {
                goToSetting(permissionHandler);
                return;
            }
        }
        if (permissionHandler.getGotoSetting()) {
            goToSetting(permissionHandler);
        } else {
            permissionHandler.getPermissionRequestProxy().onPermissionResult(grantResults, permissionHandler, this);
        }
    }

    @Override // android.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        LogModule.d(TAG, "Handle onActivityResult");
        if (TextUtils.isEmpty(this.permissionString)) {
            return;
        }
        String[] strArrSplit = this.permissionString.split(",");
        if (i == 1336) {
            PermissionHandler permissionHandler = this.permissionHandlerMap.get(this.permissionString);
            int[] grantResults = PermissionUtils.getGrantResults(this.context, strArrSplit);
            if (permissionHandler != null) {
                if (PermissionConstant.PERMISSION_WRITE_SETTINGS.equals(this.permissionString)) {
                    if (Build.VERSION.SDK_INT >= 23 && Settings.System.canWrite(this.context.getApplicationContext())) {
                        permissionHandler.getPermissionRequestProxy().onPermissionResult(new int[]{0}, permissionHandler, this);
                    } else {
                        permissionHandler.getPermissionRequestProxy().onPermissionResult(new int[]{-1}, permissionHandler, this);
                    }
                } else if (PermissionConstant.PERMISSION_SYSTEM_ALERT_WINDOW.equals(this.permissionString)) {
                    if (Build.VERSION.SDK_INT >= 23 && Settings.canDrawOverlays(this.context)) {
                        permissionHandler.getPermissionRequestProxy().onPermissionResult(new int[]{0}, permissionHandler, this);
                    } else {
                        permissionHandler.getPermissionRequestProxy().onPermissionResult(new int[]{-1}, permissionHandler, this);
                    }
                } else if (PermissionUtils.hasAllPermissionUpdateAskAgain(this.context, strArrSplit)) {
                    permissionHandler.getPermissionRequestProxy().onPermissionResult(grantResults, permissionHandler, this);
                } else {
                    permissionHandler.getPermissionRequestProxy().onPermissionResult(grantResults, permissionHandler, this);
                }
            }
            this.permissionString = "";
        }
    }

    public void goToSetting(final PermissionHandler permissionHandler) {
        final String permissionName = permissionHandler.getPermissionName();
        PermissionDialog permissionDialog = new PermissionDialog(this.context);
        permissionDialog.setCallback(new PermissionDialogListener() { // from class: com.netease.ntunisdk.modules.permission.ui.PermissionFragment.2
            @Override // com.netease.ntunisdk.modules.permission.ui.PermissionDialogListener
            public void refuse() {
                permissionHandler.getPermissionRequestProxy().onPermissionResult(PermissionUtils.getGrantResults(PermissionFragment.this.context, permissionHandler.getPermissionArray()), permissionHandler, PermissionFragment.this);
            }

            @Override // com.netease.ntunisdk.modules.permission.ui.PermissionDialogListener
            public void allow() {
                PermissionFragment.this.permissionString = permissionName;
                PermissionUtils.goSetting(PermissionConstant.REQUEST_CODE, permissionName, (Activity) PermissionFragment.this.context, PermissionFragment.this);
            }
        });
        permissionDialog.show();
        String tipSetting = permissionHandler.getTipSetting();
        String gotoSettingReason = permissionHandler.getGotoSettingReason();
        if (!TextUtils.isEmpty(gotoSettingReason)) {
            permissionDialog.setMessage(gotoSettingReason);
        } else if (!TextUtils.isEmpty(tipSetting)) {
            permissionDialog.setMessage(tipSetting);
        } else {
            permissionDialog.setMessage(PermissionTextUtils.getString(this.context, R.string.netease_permissionkit_sdk__goto_settings_tip));
        }
        permissionDialog.setPositiveButtonText(PermissionTextUtils.getString(this.context, R.string.netease_permissionkit_sdk__goto_settings));
        permissionDialog.setNegativeButtonText(PermissionTextUtils.getString(this.context, R.string.netease_permissionkit_sdk__cancel));
    }
}