package com.netease.ntunisdk.modules.clientlog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.netease.ntunisdk.modules.base.utils.LogModule;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogCode;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class DatabaseManager {
    private final DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public DatabaseManager(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public synchronized void getWritableDatabase() {
        if (this.sqLiteDatabase == null) {
            this.sqLiteDatabase = this.databaseHelper.getWritableDatabase();
        }
    }

    public synchronized void closeDatabase() {
        SQLiteDatabase sQLiteDatabase = this.sqLiteDatabase;
        if (sQLiteDatabase != null) {
            sQLiteDatabase.close();
        }
    }

    public synchronized boolean isOpen() {
        SQLiteDatabase sQLiteDatabase = this.sqLiteDatabase;
        if (sQLiteDatabase == null) {
            return false;
        }
        return sQLiteDatabase.isOpen();
    }

    public synchronized ClientLogCode insert(JSONObject jSONObject) {
        try {
        } catch (Exception e) {
            LogModule.d(ClientLogConstant.TAG, "insert Exception: " + e);
        }
        if (this.sqLiteDatabase.isOpen()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ClientLogConstant.LOG, jSONObject.getString(ClientLogConstant.LOG));
            contentValues.put(ClientLogConstant.SDK, jSONObject.getString(ClientLogConstant.SDK));
            contentValues.put("type", jSONObject.getString("type"));
            contentValues.put("udid", jSONObject.getString("udid"));
            contentValues.put("status", Integer.valueOf(jSONObject.getInt("status")));
            contentValues.put(ClientLogConstant.TRANSID, jSONObject.getString(ClientLogConstant.TRANSID));
            contentValues.put("platform", jSONObject.getString("platform"));
            contentValues.put("timestamp", jSONObject.getString("timestamp"));
            this.sqLiteDatabase.insert(ClientLogConstant.TABLE_NAME, null, contentValues);
            LogModule.d(ClientLogConstant.TAG, "insert success, json: " + jSONObject);
            return ClientLogCode.SUCCESS;
        }
        LogModule.d(ClientLogConstant.TAG, "record insert failed: database is not open");
        return ClientLogCode.UNKNOWN_ERROR;
    }

    public synchronized void delete(String str, String[] strArr) {
        try {
            if (this.sqLiteDatabase.isOpen()) {
                this.sqLiteDatabase.delete(ClientLogConstant.TABLE_NAME, str + " = ?", strArr);
            } else {
                LogModule.d(ClientLogConstant.TAG, "record delete failed: database is not open");
            }
        } catch (Exception e) {
            LogModule.d(ClientLogConstant.TAG, "record delete failed: " + e);
        }
    }

    public synchronized void update(JSONObject jSONObject, String str, String[] strArr) {
        try {
            if (this.sqLiteDatabase.isOpen()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("type", jSONObject.getString("type"));
                contentValues.put(ClientLogConstant.SDK, jSONObject.getString(ClientLogConstant.SDK));
                contentValues.put("platform", jSONObject.getString("platform"));
                contentValues.put(ClientLogConstant.TRANSID, jSONObject.getString(ClientLogConstant.TRANSID));
                contentValues.put("udid", jSONObject.getString("udid"));
                contentValues.put(ClientLogConstant.LOG, jSONObject.getString(ClientLogConstant.LOG));
                contentValues.put("status", Integer.valueOf(jSONObject.getInt("status")));
                contentValues.put("timestamp", jSONObject.getString("timestamp"));
                this.sqLiteDatabase.update(ClientLogConstant.TABLE_NAME, contentValues, str + " = ?", strArr);
            } else {
                LogModule.d(ClientLogConstant.TAG, "record update failed: database is not open");
            }
        } catch (Exception e) {
            LogModule.d(ClientLogConstant.TAG, "record update failed: " + str + Arrays.toString(strArr));
            e.printStackTrace();
        }
    }

    public ClientLogTable limitQuerySubmittingRecord() {
        if (queryAllSubmittingCount() > 0) {
            return queryAllSubmittingRecord();
        }
        return null;
    }

    public int queryAllCount() {
        Cursor cursorQuery = this.sqLiteDatabase.query(ClientLogConstant.TABLE_NAME, new String[]{ClientLogConstant.ID, "type", ClientLogConstant.SDK, "platform", ClientLogConstant.TRANSID, "udid", ClientLogConstant.LOG, "timestamp", "status"}, null, null, null, null, "ID ASC");
        int count = cursorQuery.moveToFirst() ? cursorQuery.getCount() : 0;
        cursorQuery.close();
        return count;
    }

    public int queryAllNoSubmitCount() {
        return queryCount("0");
    }

    public int queryAllSubmittingCount() {
        return queryCount("1");
    }

    public synchronized int queryCount(String str) {
        int count;
        Cursor cursorQuery = this.sqLiteDatabase.query(ClientLogConstant.TABLE_NAME, new String[]{ClientLogConstant.ID, "type", ClientLogConstant.SDK, "platform", ClientLogConstant.TRANSID, "udid", ClientLogConstant.LOG, "timestamp", "status"}, "status = ?", !TextUtils.isEmpty(str) ? new String[]{str} : null, null, null, "ID ASC");
        count = cursorQuery.moveToFirst() ? cursorQuery.getCount() : 0;
        cursorQuery.close();
        return count;
    }

    public synchronized ClientLogTable queryRecord(String str) {
        ClientLogTable clientLogTable;
        String[] strArr = !TextUtils.isEmpty(str) ? new String[]{str} : null;
        clientLogTable = new ClientLogTable();
        Cursor cursorQuery = this.sqLiteDatabase.query(ClientLogConstant.TABLE_NAME, new String[]{ClientLogConstant.ID, "type", ClientLogConstant.SDK, "platform", ClientLogConstant.TRANSID, "udid", ClientLogConstant.LOG, "timestamp", "status"}, "status = ?", strArr, null, null, "ID ASC");
        if (cursorQuery.getCount() > 0 && cursorQuery.moveToNext()) {
            clientLogTable.setID(cursorQuery.getInt(cursorQuery.getColumnIndex(ClientLogConstant.ID)));
            clientLogTable.setType(cursorQuery.getString(cursorQuery.getColumnIndex("type")));
            clientLogTable.setSdk(cursorQuery.getString(cursorQuery.getColumnIndex(ClientLogConstant.SDK)));
            clientLogTable.setPlatform(cursorQuery.getString(cursorQuery.getColumnIndex("platform")));
            clientLogTable.setTransid(cursorQuery.getString(cursorQuery.getColumnIndex(ClientLogConstant.TRANSID)));
            clientLogTable.setUdid(cursorQuery.getString(cursorQuery.getColumnIndex("udid")));
            clientLogTable.setLog(cursorQuery.getString(cursorQuery.getColumnIndex(ClientLogConstant.LOG)));
            clientLogTable.setStatus(cursorQuery.getInt(cursorQuery.getColumnIndex("status")));
            clientLogTable.setTimestamp(cursorQuery.getString(cursorQuery.getColumnIndex("timestamp")));
        }
        cursorQuery.close();
        return clientLogTable;
    }

    public synchronized ClientLogTable queryAllNoSubmitRecord() {
        return queryRecord("0");
    }

    public synchronized ClientLogTable queryAllSubmittingRecord() {
        return queryRecord("1");
    }

    public synchronized List<ClientLogTable> queryAllRecord() {
        ArrayList arrayList;
        arrayList = new ArrayList();
        Cursor cursorQuery = this.sqLiteDatabase.query(ClientLogConstant.TABLE_NAME, new String[]{ClientLogConstant.ID, "type", ClientLogConstant.SDK, "platform", ClientLogConstant.TRANSID, "udid", ClientLogConstant.LOG, "timestamp", "status"}, null, null, null, null, "ID ASC");
        if (cursorQuery.getCount() > 0) {
            while (cursorQuery.moveToNext()) {
                ClientLogTable clientLogTable = new ClientLogTable();
                clientLogTable.setID(cursorQuery.getInt(cursorQuery.getColumnIndex(ClientLogConstant.ID)));
                clientLogTable.setType(cursorQuery.getString(cursorQuery.getColumnIndex("type")));
                clientLogTable.setSdk(cursorQuery.getString(cursorQuery.getColumnIndex(ClientLogConstant.SDK)));
                clientLogTable.setPlatform(cursorQuery.getString(cursorQuery.getColumnIndex("platform")));
                clientLogTable.setTransid(cursorQuery.getString(cursorQuery.getColumnIndex(ClientLogConstant.TRANSID)));
                clientLogTable.setUdid(cursorQuery.getString(cursorQuery.getColumnIndex("udid")));
                clientLogTable.setLog(cursorQuery.getString(cursorQuery.getColumnIndex(ClientLogConstant.LOG)));
                clientLogTable.setStatus(cursorQuery.getInt(cursorQuery.getColumnIndex("status")));
                clientLogTable.setTimestamp(cursorQuery.getString(cursorQuery.getColumnIndex("timestamp")));
                arrayList.add(clientLogTable);
                if (arrayList.size() >= 10) {
                    break;
                }
            }
        }
        cursorQuery.close();
        return arrayList;
    }
}