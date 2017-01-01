package com.jiushu.wifipwmanager.util;

import android.content.Context;
import android.util.Log;

import com.jiushu.wifipwmanager.R;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by simon on 14/11/15.
 */
public class RootUtil {
    /**
     * 执行命令progArray，同时通过returnKey来获取返回值 returnKey
     *
     * @param progArray
     * @param returnKey
     * @return
     */
    public static String exeCmd(String[] progArray, String returnKey) {
        DataOutputStream os = null;
        Process process = null;
        BufferedReader reader = null;
        String returnCode = Constants.RETURN_CODE_ERROR;
        for (String str : progArray) {
            Log.d("SystemOut", "progArray: " + str);
        }
        try {
            process = Runtime.getRuntime().exec(progArray);
            os = new DataOutputStream(process.getOutputStream());
            os.flush();
            process.waitFor();
            reader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            String backMsg = null;
            backMsg = reader.readLine();
            while ((backMsg) != null) {
                Log.d("SystemOut", "backMsg: " + backMsg);
                returnCode = Utilities.getReturnCode(returnKey, backMsg, returnCode);
            }
        } catch (IOException e) {
            Log.d("SystemOut", "exeCmd IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.d("SystemOut", "exeCmd InterruptedException: " + e.getMessage());
            e.printStackTrace();
        } finally {

            try {
                if (os != null) {
                    os.close();
                }
                if (reader != null) {
                    reader.close();
                }
                if (process != null)
                    process.destroy();
            } catch (IOException e) {
                Log.d("SystemOut", "IOException: " + e.getMessage());
                e.printStackTrace();
            }

        }
        return returnCode;
    }

    public static String exeCmd(String cmd, String returnKey) {
        DataOutputStream os = null;
        Process process = null;
        BufferedReader reader = null;
        String returnCode = Constants.RETURN_CODE_ERROR;

        Log.d("SystemOut", "exeCmd: " + cmd);
        try {
            process = Runtime.getRuntime().exec(Constants.ROOT_SU);
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
            reader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            String print = null;
            while ((print = reader.readLine()) != null) {
                Log.d("SystemOut", "print: " + print);
                returnCode = Utilities.getReturnCode(returnKey, print,returnCode);
            }
            Log.d("SystemOut", process.exitValue()+"");
        } catch (IOException e) {
            Log.d("SystemOut", "exeCmd IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.d("SystemOut", "exeCmd InterruptedException: " + e.getMessage());
            e.printStackTrace();
        } finally {

            try {
                if (os != null) {
                    os.close();
                }
                if (reader != null) {
                    reader.close();
                }
                if (process != null)
                    process.destroy();
            } catch (IOException e) {
                Log.d("SystemOut", "IOException: " + e.getMessage());
                e.printStackTrace();
            }

        }
        return returnCode;
    }

    /**
     * 调用su获取root权限再把zlsu写到system/bin目录下
     * 以便永久获取root权限（一旦获取成功，下次不再调用su）
     *
     * @param ctx
     */
    public static void prepareWifiScannerSu(Context ctx) {
        try {
            File aawifiscannersu = new File("/system/bin/" + Constants.ROOT_SU);

            InputStream suStream = ctx.getResources().openRawResource(
                    R.raw.aawifiscannersu);
            /**
             * 如果wifiscannersu存在，则和raw目录下的aawifiscannersu比较大小，大小相同则不替换
             */
            if (aawifiscannersu.exists()) {
                if (aawifiscannersu.length() == suStream.available()) {
                    suStream.close();
                    return;
                }
            }

            /**
             * 先把aawifiscannersu 写到/data/data/pkgName中然后再调用 su 权限写到
             * /system/bin目录下
             */
            byte[] bytes = new byte[suStream.available()];
            DataInputStream dis = new DataInputStream(suStream);
            dis.readFully(bytes);

            String pkgPath = ctx.getApplicationContext().getPackageName();
            String zlsuPath = "/data/data/" + pkgPath + File.separator + Constants.ROOT_SU;
            File zlsuFileInData = new File(zlsuPath);
            if(!zlsuFileInData.exists()){
                Log.d("SystemOut", zlsuPath + " not exist! ");
                try{
                    Log.d("SystemOut", "creating " + zlsuPath + "......");
                    zlsuFileInData.createNewFile();
                }catch(IOException e){
                    Log.d("SystemOut", "create " + zlsuPath + " failed ! ");
                }
                Log.d("SystemOut", "create " + zlsuPath + " successfully ! ");
            }
            FileOutputStream suOutStream = new FileOutputStream(zlsuPath);
            suOutStream.write(bytes);
            suOutStream.close();

            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(
                    process.getOutputStream());
            os.writeBytes("mount -oremount,rw /dev/block/mtdblock3 /system\n");
//			"busybox cp /data/data/com.zl.movepkgdemo/aawifiscannersu /system/bin/aawifiscannersu \n"
            os.writeBytes("busybox cp " + zlsuPath + " /system/bin/"
                    + Constants.ROOT_SU + "\n");
//			"busybox chown 0:0 /system/bin/aawifiscannersu \n"
            os.writeBytes("busybox chown 0:0 /system/bin/" + Constants.ROOT_SU
                    + "\n");
//			"chmod 4755 /system/bin/aawifiscannersu \n"
            os.writeBytes("chmod 4755 /system/bin/" + Constants.ROOT_SU + "\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (Exception e) {
            Log.d("SystemOut", "RootUtil prepareWifiScannerSu: error");
            e.printStackTrace();
        }
    }
}