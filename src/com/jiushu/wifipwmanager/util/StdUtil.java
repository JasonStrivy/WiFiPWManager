package com.jiushu.wifipwmanager.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by simon on 20/11/15.
 */
public class StdUtil {
    /**
     * 分享功能
     *
     * @param context        上下文
     * @param activityTitle  Activity的名字
     * @param msgTitle       消息标题
     * @param sharingMsgText 消息内容
     * @param sharingImgPath 图片路径，不分享图片则传null
     */
    public static void shareMsg(Context context, String activityTitle, String msgTitle, String sharingMsgText,
                                String sharingImgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (sharingImgPath == null || sharingImgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(sharingImgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/jpg");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, sharingMsgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (activityTitle == null)
            activityTitle = context.getResources().
                    getString(context.getApplicationInfo().labelRes);
        context.startActivity(Intent.createChooser(intent, activityTitle));
    }
}
