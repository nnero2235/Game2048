package com.nnero.game2048.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Author:NNERO
 * <p/>
 * TIME: 15/10/17 下午7:48
 * <p/>
 * DESCRIPTION:
 */
public class GameItem extends FrameLayout {

    public static final int COLOR_GREY = 0xeeeeee;
    public static final int COLOR_WHITE = 0xffffff;
    public static final int COLOR_LIGHT_YELLOW = 0xffffe0;
    public static final int COLOR_DEEP_YELLOW = 0xffec8b;
    public static final int COLOR_LIGHT_ORANGE = 0xffc125;
    public static final int COLOR_DEEP_ORANGE = 0xff8c00;
    public static final int COLOR_LIGHT_RED = 0xff7256;
    public static final int COLOR_DEEP_RED = 0xff3030;
    public static final int COLOR_LIGHT_BLUE = 0xb2dfee;
    public static final int COLOR_DEEP_BLUE = 0x6495ed;
    public static final int COLOR_LIGHT_PURPLE = 0xdda0dd;
    public static final int COLOR_DEEP_PURPLE = 0x9370d8;
    public static final int COLOR_DEEP_GREEN = 0x00cc99;

    public static final int COLOR_BLACK = 0x000000;

    private TextView mNumberView;
    private int mNumber;

    public GameItem(Context context,int number) {
        super(context);
        mNumber = number;
        setBackground(mNumber);
    }

    public GameItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initItem(Context context){
        mNumberView = new TextView(context);
        mNumberView.setTextColor(COLOR_BLACK);
        mNumberView.setTextSize(16);

    }

    private void setBackground(int number){
        switch (number){
        case 0:
            this.setBackgroundColor(COLOR_GREY);
            break;
        case 2:
            this.setBackgroundColor(COLOR_WHITE);
            break;
        case 4:
            this.setBackgroundColor(COLOR_LIGHT_YELLOW);
            break;
        case 8:
            this.setBackgroundColor(COLOR_DEEP_YELLOW);
            break;
        case 16:
            this.setBackgroundColor(COLOR_LIGHT_ORANGE);
            break;
        case 32:
            this.setBackgroundColor(COLOR_DEEP_ORANGE);
            break;
        case 64:
            this.setBackgroundColor(COLOR_LIGHT_RED);
            break;
        case 128:
            this.setBackgroundColor(COLOR_DEEP_RED);
            break;
        case 256:
            this.setBackgroundColor(COLOR_LIGHT_BLUE);
            break;
        case 512:
            this.setBackgroundColor(COLOR_DEEP_BLUE);
            break;
        case 1024:
            this.setBackgroundColor(COLOR_LIGHT_PURPLE);
            break;
        case 2048:
            this.setBackgroundColor(COLOR_DEEP_PURPLE);
            break;
        case 4096:
            this.setBackgroundColor(COLOR_DEEP_GREEN);
            break;
        }
    }
}
