package com.jiushu.wifipwmanager.util;

import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import com.jiushu.wifipwmanager.R;
import com.jiushu.wifipwmanager.data.WiFiNetWork;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by simon on 19/11/15.
 */
public class PopupMenuUtil {
/*
    public static void setComment(Context context, WiFiNetWork wiFiNetWork) {

        final Context mContext = context;
        final WiFiNetWork selectedWiFiNetWork = wiFiNetWork;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View setcommentView = inflater.inflate(R.layout.popup_menu_setcomment, null);
        final EditText commentET = (EditText) setcommentView.findViewById(R.id.setcomment_editor);

        CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
        builder.setMessage("    添加备注信息只是为了方便区分软件内的热点，并不影响系统中WiFi的任何信息。");
        builder.setTitle("热点：" + selectedWiFiNetWork.getSsid());
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                String comment = commentET.getText().toString();
            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }
*/
    public static void copySsidAndPw(Context context, WiFiNetWork netWork) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText("热点：" + netWork.getSsid() + "\n密码：" + netWork.getPsk().trim());

        Toast.makeText(context, "复制成功，长按输入框粘贴", Toast.LENGTH_LONG).show();
    }

    public static void copyPw(Context context, WiFiNetWork netWork) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(netWork.getPsk().trim());

        Toast.makeText(context, "复制成功，长按输入框粘贴", Toast.LENGTH_LONG).show();
    }

    public static void share(Context context, WiFiNetWork netWork) {
        String sharingStr = "热点：" + netWork.getSsid()
                + "\n密码：" + netWork.getPsk().trim()
                + "\n\n由【"
                + context.getResources().getString(R.string.app_name)
                + "】提供!";
        StdUtil.shareMsg(context, "分享【" + netWork.getSsid() + "】的信息给好友", "分享", sharingStr, null);
    }

    public static Map<String, Integer> delete(Context context, WiFiNetWork netWork) {
        return DBUtil.upsert(context, Arrays.asList(new WiFiNetWork[]{netWork}));
    }
}
