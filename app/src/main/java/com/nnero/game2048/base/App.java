package com.nnero.game2048.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.nnero.game2048.util.LogUtil;
import com.nnero.game2048.util.SPKeys;

/**
 * Author:NNERO
 * <p/>
 * TIME: 15/10/16 下午9:28
 * <p/>
 * DESCRIPTION:全局上下文
 */
public class App extends Application{

    public static int goal;
    public static int level;
    private static Context instance;

    public static final int[] LEVELS = {4,5,6};
    public static final int[] GOALS = {1024,2048,4096};

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }

    /**
     * 获取上下文
     * @return
     */
    public static Context getContext(){
        return instance;
    }

    /**
     * 获取sp
     * @return
     */
    public static SharedPreferences getSharedPreferences(){
        return instance.getSharedPreferences("info",MODE_PRIVATE);
    }
}
