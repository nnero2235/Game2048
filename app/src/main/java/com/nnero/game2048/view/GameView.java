package com.nnero.game2048.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.TextView;
import com.nnero.game2048.base.App;
import com.nnero.game2048.util.DimensionUtils;

/**
 * Author:NNERO
 * <p/>
 * TIME: 15/10/16 下午8:56
 * <p/>
 * DESCRIPTION:主要游戏展示view
 */
public class GameView extends GridLayout{

    private int mFactor;
    private int mItemSize;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        removeAllViews();
        calculateItemSize();
        setRowCount(4);
        setColumnCount(4);
        mFactor = 2;
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                addView(new GameItem(getContext(),mFactor%4096),mItemSize,mItemSize);
                mFactor *= 2;
            }
        }
    }


    private void calculateItemSize(){
        // 初始化View参数
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        mItemSize = (metrics.widthPixels - DimensionUtils.dip2px(App.getContext(),10) * 2) / 4;
    }

}
