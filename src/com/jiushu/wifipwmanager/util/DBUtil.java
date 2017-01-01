package com.jiushu.wifipwmanager.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jiushu.wifipwmanager.data.DBHelper;
import com.jiushu.wifipwmanager.data.WiFiNetWork;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by simon on 21/11/15.
 */
public class DBUtil {

    public static Map<String, Integer> upsert(Context context, List<WiFiNetWork> wiFiNetWorks) {
        Map result = new HashMap();

        if (wiFiNetWorks == null) return null;

        //SQLiteDatabase对象
        SQLiteDatabase db;
        //DB_Name
        String dbName = Constants.DATABASENAME;
        //Table_Name
        String tableName = "wifinetwork";

        final DBHelper helper = new DBHelper(context, dbName, null, 1);
        //从辅助类获得数据库对象
        db = helper.getWritableDatabase();
        //ContentValues对象
        ContentValues cv = new ContentValues();

        long insertResult = -1;
        for (WiFiNetWork wifiNetWork : wiFiNetWorks) {
            cv.put("ssid", wifiNetWork.getSsid());
            cv.put("psk", wifiNetWork.getPsk());
            cv.put("key_mgmt", wifiNetWork.getKey_mgmt());
            cv.put("ischinesessid", wifiNetWork.isChineseSsid() + "");
            cv.put("priority", wifiNetWork.getPriority());
            cv.put("sup_dis_reason", wifiNetWork.getSup_dis_reason());
            cv.put("inserttime", DateUtil.now());

            //防止每次初始化时，初始化掉已经保存的备注
            if (!StringUtil.isEmpty(wifiNetWork.getComment()))
                cv.put("comment", wifiNetWork.getComment());

            //删除操作，亦即将isdel字段置为true
            if (wifiNetWork.isDel)
                cv.put("isdel", "true");

            String ssid = wifiNetWork.getSsid();

            //update()方法的返回值表示数据库表中被更新的数据数量
            insertResult = db.update(tableName, cv, "ssid='" + ssid + "'", null);

            //insert()返回插入数据的位置,添加成功后返回行号，失败后返回-1
            if (insertResult <= 0)
                insertResult = db.insert(tableName, "", cv);

            int strid = -1;
            String comment = "";
            boolean isdel = false;
            Cursor cursor = db.rawQuery("SELECT _id, comment, isdel FROM " + tableName + " WHERE ssid='" + ssid + "'", null);
            if (cursor.moveToFirst()) {
                strid = cursor.getInt(0);
                comment = cursor.getString(1);
                isdel = Boolean.valueOf(cursor.getString(2));
            }

            result.put(wifiNetWork.getSsid(), Integer.valueOf(strid));
            wifiNetWork.setId(Integer.valueOf(strid));
            wifiNetWork.setComment(comment);
            wifiNetWork.setIsDel(isdel);
        }

        return result;
    }
}
