package com.jiushu.wifipwmanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by simon on 21/11/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE wifinetwork (" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " ssid VARCHAR," +
                " psk VARCHAR," +
                " comment VARCHAR," +
                " key_mgmt VARCHAR," +
                " isChineseSsid VARCHAR," +
                " priority INTEGER," +
                " sup_dis_reason INTEGER," +
                " insertTime VARCHAR," +
                " isdel VARCHAR" +
                " )";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
