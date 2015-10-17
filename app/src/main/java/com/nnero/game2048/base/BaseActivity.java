package com.nnero.game2048.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;

/**
 * Author:NNERO
 * <p/>
 * TIME: 15/10/16 下午9:28
 * <p/>
 * DESCRIPTION:基础的activity
 */
public abstract class BaseActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initData();
        initView();
    }

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 获取layoutid
     * @return
     */
    protected abstract int getLayoutId();
}
