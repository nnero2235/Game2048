package com.nnero.game2048.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;

/**
 * Author:NNERO
 * <p/>
 * TIME: 15/10/16 下午8:56
 * <p/>
 * DESCRIPTION:主要游戏展示view
 */
public class GameView extends GridLayout{

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
