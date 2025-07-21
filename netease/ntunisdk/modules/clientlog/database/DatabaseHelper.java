package com.netease.ntunisdk.modules.clientlog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import com.netease.ntunisdk.modules.base.utils.LogModule;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;

/* loaded from: classes5.dex */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_MESSENGER_SQL = "create table client_log_table (ID integer primary key autoincrement, type text, sdk text, platform text, transid text, udid text, timestamp text, log text,status integer)";
    private static final String TRIGGER = "CREATE TRIGGER delete_till_2000 BEFORE INSERT ON client_log_table WHEN (select count(*) from client_log_table )>=5000 BEGIN DELETE FROM client_log_table WHERE client_log_table.ID IN (SELECT client_log_table.ID FROM client_log_table ORDER BY client_log_table.ID limit (select count(*) -5000 from client_log_table ));END;";

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public DatabaseHelper(Context context) {
        super(context, ClientLogConstant.DB_NAME, (SQLiteDatabase.CursorFactory) null, 1);
        getWritableDatabase().enableWriteAheadLogging();
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        LogModule.d(ClientLogConstant.TAG, "onCreate SQLiteDatabase");
        try {
            try {
                sQLiteDatabase.beginTransaction();
                sQLiteDatabase.execSQL(CREATE_MESSENGER_SQL);
                sQLiteDatabase.execSQL(TRIGGER);
                sQLiteDatabase.setTransactionSuccessful();
                LogModule.d(ClientLogConstant.TAG, "onCreate table client_log_db");
            } catch (SQLiteException e) {
                LogModule.d(ClientLogConstant.TAG, "onCreate SQLiteDatabase Exception: " + e.toString());
            }
        } finally {
            sQLiteDatabase.endTransaction();
        }
    }
}