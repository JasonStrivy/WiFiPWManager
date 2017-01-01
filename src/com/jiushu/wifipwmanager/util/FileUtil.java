package com.jiushu.wifipwmanager.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.jiushu.wifipwmanager.data.WiFiNetWork;
import com.jiushu.wifipwmanager.data.WpaSupplication;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by simon on 13/11/15.
 */
public class FileUtil {
    public static String cpFile(Context context, String sourceFile, String targetFile) {
        String returnCode = "ERROR";

        String classpath;
        String minePkgName = context.getPackageName();

        //RootUtil.exeCmd(cmd, Constants.RETURNCODE_CP_FILE);

        try {
            classpath = context.getPackageManager().getPackageInfo(minePkgName,
                    0).applicationInfo.publicSourceDir;

            String[] progArray = new String[]{
                    Constants.ROOT_SU,
                    "-c",
                    "export CLASSPATH="
                            + classpath
                            + " && export LD_LIBRARY_PATH=/vendor/lib:/system/lib && exec app_process "
                            + "/data/app " + minePkgName + "/util/CopyFileFromSys "
                            + sourceFile + " " + targetFile
            };

            returnCode = RootUtil.exeCmd(progArray, Constants.RETURNCODE_CP_FILE);

            Log.d("SystemOut", "result: " + returnCode);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }

        return returnCode;
    }

    public static List<WiFiNetWork> readFiles(Context context) {

        List<WiFiNetWork> netWorks = new ArrayList<>();

        WpaSupplication wpaSupplication;
        wpaSupplication = readToWpaSupplication(context, Constants.NEED_SYS_FILENAMES.get("wpa_supplicant"));

        //倒序排列
        if (wpaSupplication != null) {
            Collections.reverse(wpaSupplication.wiFiNetWorks);
            netWorks = wpaSupplication.wiFiNetWorks;

            Map<String, Integer> result = DBUtil.upsert(context, netWorks);
            //如果result中有-1，则表示改ssid插入失败
        }

        List<WiFiNetWork> tempNetWorks = new ArrayList();
        tempNetWorks.addAll(netWorks);
        //去除删除标记为true的
        for (WiFiNetWork wiFiNetWork : tempNetWorks)
            if (wiFiNetWork.isDel)
                netWorks.remove(wiFiNetWork);

        return netWorks;
    }

    public static WpaSupplication readToWpaSupplication(Context context, String fileName) {

        WpaSupplication wpaSupplication = null;

        try {
            FileInputStream inputStream;
            inputStream = context.openFileInput(fileName);

            InputStreamReader inputreader = new InputStreamReader(inputStream);
            BufferedReader buffreader = new BufferedReader(inputreader);

            wpaSupplication = new WpaSupplication();
            wpaSupplication.headerStr = "";
            wpaSupplication.wiFiNetWorks = new ArrayList<>();

            String line;
            boolean networksStart = false;
            WiFiNetWork netWork = null;
            //分行读取
            while ((line = buffreader.readLine()) != null) {
                line = initLineContent(line);

                if (!networksStart && line.equals("network={"))
                    networksStart = true;

                if (!networksStart)
                    wpaSupplication.headerStr += line + "\n";

                else {
                    if (line.equals("network={")) {
                        netWork = new WiFiNetWork();
                        continue;

                    } else if (line.equals("}")) {
                        if (netWork.psk != null)
                            wpaSupplication.wiFiNetWorks.add(netWork);
                        continue;

                    } else {
                        if (line.startsWith("ssid=")) {
                            //ssid="WIFI_NAME"
                            line = line.substring(line.indexOf("=") + 1);

                            boolean isChineseSsid = !line.contains("\"");
                            netWork.setIsChineseSsid(isChineseSsid);

                            line = line.replaceAll("\"", "");

                            netWork.setSsid(line);
                        }

                        if (line.startsWith("psk=")) {
                            //psk="WIFI_PASSWORD"
                            line = line.substring(line.indexOf("=") + 1);
                            line = line.replaceAll("\"", "");

                            netWork.setPsk(line);
                        }

                        if (line.startsWith("key_mgmt=")) {
                            //key_mgmt=WPA-PSK
                            line = line.substring(line.indexOf("=") + 1);

                            netWork.setKey_mgmt(line);
                        }

                        if (line.startsWith("priority=")) {
                            //priority=100
                            line = line.substring(line.indexOf("=") + 1);

                            netWork.setPriority(Integer.valueOf(line));
                        }
                    }
                }
            }

            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return wpaSupplication;
    }


    public static List<WiFiNetWork> readToWiFiNetWork(Context context, String fileName) {
        List<WiFiNetWork> wiFiNetWorks = new ArrayList<>();
        WiFiNetWork wiFiNetWork = null;

        try {
            FileInputStream inputStream;
            inputStream = context.openFileInput(fileName);

            InputStreamReader inputreader = new InputStreamReader(inputStream);
            BufferedReader buffreader = new BufferedReader(inputreader);

            String line;
            int networkCount = 0;
            while ((line = buffreader.readLine()) != null) {
                line = initLineContent(line);

                if (line.startsWith("\u0000") && line.length() >= 2)
                    line = line.substring(2);

                if (line.startsWith("CONFIG:")) {
                    wiFiNetWork = new WiFiNetWork();
                    networkCount++;

                } else if (line.startsWith("ID:")) {
                    //ID:  11
                    line = line.substring(line.indexOf(":") + 1);
                    line = line.trim();
                    wiFiNetWork.setId(Integer.valueOf(line));

                } else if (line.startsWith("SSID:")) {
                    //SSID:  "WIFI_NAME"
                    line = line.substring(line.indexOf(":") + 1);
                    line = line.trim();
                    line = line.replaceAll("\"", "");
                    wiFiNetWork.setSsid(line);

                } else if (line.startsWith("SUP_DIS_REASON:")) {
                    //SUP_DIS_REASON:  0
                    line = line.substring(line.indexOf(":") + 1);
                    line = line.trim();
                    wiFiNetWork.setSup_dis_reason(Integer.valueOf(line));

                } else if (line.startsWith("AUTH:")) {
                    //AUTH:  WPA-PSK
                    line = line.substring(line.indexOf(":") + 1);
                    line = line.trim();
                    wiFiNetWork.setKey_mgmt(line);

                } else if (line.startsWith("PRIORITY:")) {
                    //PRIORITY:  93
                    line = line.substring(line.indexOf(":") + 1);
                    line = line.trim();
                    wiFiNetWork.setPriority(Integer.valueOf(line));

                    if (wiFiNetWork.priority == null)
                        wiFiNetWork.priority = networkCount - 1000;

                    wiFiNetWorks.add(wiFiNetWork);
                }
            }

            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("SystemOut", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("SystemOut", e.getMessage());
        }

        return wiFiNetWorks;
    }

    private static String initLineContent(String line) {
        if (line != null) {
            line = line.replace("\t", "");

            return line;
        }

        return null;
    }
}
