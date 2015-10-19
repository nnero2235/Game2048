package com.nnero.game2048.ui.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Author:NNERO
 * <p/>
 * TIME: 15/10/19 下午4:59
 * <p/>
 * DESCRIPTION:对话框生成器 一句话封装 对话框
 */
public class DialogGenerator {

    public static void showGameOverDialog(final Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("格子满了，游戏失败");
        builder.setTitle("失败");
        builder.setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("退出游戏", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                activity.finish();
            }
        });
        builder.create().show();
    }

}
