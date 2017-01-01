package com.jiushu.wifipwmanager.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by simon on 14/11/15.
 */
public class Constants {
    /**
     * 第一次获取root权限后，push 到 /system/bin/ 目录下的二进制可执行程序的名称
     */
    public static final String ROOT_SU = "aawifiscannersu";

    /**
     * 需要从系统内读取的文件，注意顺序
     */
    public static final Map<String, String> NEED_SYS_FILENAMES = new HashMap<String, String>() {{
        put("wpa_supplicant", "wpa_supplicant.conf");
        put("apconnectrecord", "ApConnectRecord.conf");
    }};

    /**
     * networkHistory.txt文件中需要的属性
     */
    public static final List<String> WIFINETWORK_PROPERTYS = new ArrayList<String>() {{
        add("ID");
        add("SSID");
        add("PRIORITY");
        add("SUP_DIS_REASON");
        add("AUTH");
    }};

    /**
     * 调用 {@link #ROOT_SU}命令时传入的returnKey 返回值是通过该returnKey来获取
     */
    public static final String RETURNCODE_CP_FILE = "returnCode_cp_file";
    public static final String RETURN_CODE_ERROR = "return_code_error";

    public static final int COPYFILE_SUCCESS = 0;
    public static final int COPYFILE_FILENOTFOUNDEXCEPTION = -1;
    public static final int COPYFILE_IOEXCEPTION = -2;
    public static final int COPYFILE_ERMISSIONEXCEPTION = -3;

    /**
     * PopupMenuItem
     */
    public static class PopupMenuItem {
        public static final int SET_COMMENT = 0;
        public static final int COPY_SSID_PW = 1;
        public static final int COPY_PW = 2;
        public static final int SHARE = 3;
        public static final int DELETE = 4;
    }

    /**
     * DB
     * */
    public static final String DATABASENAME = "wifipwmanager.sqlite";
}