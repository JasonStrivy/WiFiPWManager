package com.jiushu.wifipwmanager.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by simon on 14/11/15.
 */
public class CopyFileFromSys {
    public static void main(String[] args) {
        Log.d("SystemOut", "copy start: ");
        if (args.length >= 2) {
            String sourceFile = args[0];
            String targetFile = args[1];
            Log.d("SystemOut", "From: " + sourceFile);
            Log.d("SystemOut", "To: " + targetFile);

            CopyFileFromSys.copyFile(sourceFile, targetFile);
        } else {
            Log.d("SystemOut", "need source and target file path.");
        }
        Log.d("SystemOut", "copy end.");
    }

    public static int copyFile(String sFile, String tFile) {

        InputStream fosfrom;
        OutputStream fosto;
        String sourceFile;
        String targetFile;

        try {
            for (String fileName : Constants.NEED_SYS_FILENAMES.values()) {

                sourceFile = sFile+fileName;
                targetFile = tFile+fileName;

                File destDir = new File(targetFile);
                if (!destDir.exists()) {
                    destDir.getParentFile().mkdirs();
                }

                fosfrom = new FileInputStream(sourceFile);
                fosto = new FileOutputStream(targetFile);
                byte bt[] = new byte[1024];
                int c;
                while ((c = fosfrom.read(bt)) > 0) {
                    fosto.write(bt, 0, c);
                }
                fosfrom.close();
                fosto.close();

                Process p;
                p = Runtime.getRuntime().exec("chmod 777 " + destDir);
                p = Runtime.getRuntime().exec("chmod 777 " + destDir.getParentFile());
                p.waitFor();
            }

            return Constants.COPYFILE_SUCCESS;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("SystemOut", e.getMessage());
            return Constants.COPYFILE_FILENOTFOUNDEXCEPTION;

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("SystemOut", e.getMessage());
            return Constants.COPYFILE_IOEXCEPTION;
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("SystemOut", e.getMessage());
            return Constants.COPYFILE_ERMISSIONEXCEPTION;
        }
    }
}
