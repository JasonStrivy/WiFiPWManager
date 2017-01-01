package com.jiushu.wifipwmanager.util;

import android.util.Log;

/**
 * Created by simon on 14/11/15.
 */
public class Utilities {
    /**
     * 通过键值对来设置{@link #exeCmd} 中的命令 progArray 的返回值
     *
     * @param returnKey
     *            键
     * @param returnCode
     *            返回值
     */
    public static void setReturnCode(String returnKey, String returnCode) {
        Log.d("SystemOut", returnKey + returnCode);
    }

    /**
     * 通过键(returnKey) 来获取{@link #exeCmd} 中的命令 progArray 的返回值(returnCode)
     *
     * @param returnKey
     *            键
     * @param printf
     *            {@link #exeCmd} 中的命令 progArray 的完整输出信息,格式：returnKey +
     *            returnCode
     * @return returnCode 返回值
     */
    public static String getReturnCode(String returnKey, String printf,String defaultReturnCode) {
        String resultCode = defaultReturnCode;
        if (!Utilities.isEmpty(returnKey) & !Utilities.isEmpty(printf)) {
            if (printf.startsWith(returnKey)) {
                int keyLen = returnKey.length();
                resultCode = printf.substring(keyLen, printf.length());
            }
        }
        return resultCode;
    }

    /**
     * 1、对象是否为空 2、String是否为空，或者是否为""
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        boolean flag = true;
        if (obj != null) {
            if (obj instanceof String) {
                String s = (String) obj;
                if (!s.trim().equals("")) {
                    flag = false;
                }
            } else {
                flag = false;
            }
        }
        return flag;
    }
}