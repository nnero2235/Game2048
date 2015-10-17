package com.nnero.game2048.util;

import android.util.Log;

/**
 * Author:NNERO
 * <p/>
 * TIME: 15/10/16 下午10:41
 * <p/>
 * DESCRIPTION:打印log
 */
public class LogUtil {

    private static boolean DEBUG = true;
    private static String TAG = "game2048";

    public static void d(String msg){
        if(DEBUG) {
            Log.d(TAG, msg);
        }
    }
}
