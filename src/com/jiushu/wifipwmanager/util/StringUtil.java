package com.jiushu.wifipwmanager.util;

import android.util.Log;

/**
 * Created by simon on 17/11/15.
 */
public class StringUtil {
    public static String hexToStringUTF8(String hexStr) {
        byte[] utf8Bytes = new byte[hexStr.length() / 2];
        for (int i = 0; i < utf8Bytes.length; i++) {
            try {
                utf8Bytes[i] = (byte) (0xff & Integer.parseInt(hexStr.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                Log.d("SystemOut", e.getMessage());
                return "";
            }
        }
        try {
            hexStr = new String(utf8Bytes, "UTF-8");
        } catch (Exception e) {
            Log.d("SystemOut", e.getMessage());
            return "";
        }
        return hexStr;
    }

    public static String byte2Hex(byte[] b) {
        String hs = "";
        String stmp;
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toLowerCase();
    }

    // 完整的判断中文汉字和符号字符串
    public static boolean isChineseString(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    // 根据Unicode编码完美的判断中文汉字和符号字节
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String sourceStr){
        return sourceStr == null || "".equals(sourceStr);
    }
}
