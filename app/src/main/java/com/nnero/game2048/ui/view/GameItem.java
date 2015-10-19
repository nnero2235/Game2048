package com.nnero.game2048.ui.view;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.nnero.game2048.R;

/**
 * Author:NNERO
 * <p/>
 * TIME: 15/10/17 下午7:48
 * <p/>
 * DESCRIPTION:小方块 view
 */
public class GameItem extends FrameLayout {

    public static final int COLOR_GREY = 0xffbbbbbb;
    public static final int COLOR_DEEP_GREY = 0xff999999;
    public static final int COLOR_WHITE = 0xffffffff;
    public static final int COLOR_LIGHT_YELLOW = 0xffffffe0;
    public static final int COLOR_DEEP_YELLOW = 0xffffec8b;
    public static final int COLOR_LIGHT_ORANGE = 0xffffc125;
    public static final int COLOR_DEEP_ORANGE = 0xffff8c00;
    public static final int COLOR_LIGHT_RED = 0xffff7256;
    public static final int COLOR_DEEP_RED = 0xffff3030;
    public static final int COLOR_LIGHT_BLUE = 0xffb2dfee;
    public static final int COLOR_DEEP_BLUE = 0xff6495ed;
    public static final int COLOR_LIGHT_PURPLE = 0xffdda0dd;
    public static final int COLOR_DEEP_PURPLE = 0xff9370d8;
    public static final int COLOR_DEEP_GREEN = 0xff00cc99;

    public static final int COLOR_BLACK = 0xff000000;

    private TextView mNumberView;
    private int mNumber;

    public GameItem(Context context,int number) {
        super(context);
        mNumber = number;
        init();
    }

    private void init(){
        initItem();
        initLayout();
        setItem(mNumber);
    }

    private void initItem(){
        mNumberView = new TextView(getContext());
        mNumberView.setTextColor(COLOR_BLACK);
        mNumberView.setTextSize(20);
        mNumberView.setGravity(Gravity.CENTER);
    }

    private void initLayout(){
        FrameLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.setMargins(10, 10, 10, 10); //方格之间有间距
        addView(mNumberView, params);
        setBackgroundColor(COLOR_DEEP_GREY);
    }

    private void setBackground(int number){
        switch (number){
        case 0:
            mNumberView.setBackgroundResource(R.drawable.item_grey_corner_background);
            break;
        case 2:
            mNumberView.setBackgroundResource(R.drawable.item_white_corner_background);
            break;
        case 4:
            mNumberView.setBackgroundResource(R.drawable.item_light_yellow_corner_background);
            break;
        case 8:
            mNumberView.setBackgroundResource(R.drawable.item_deep_yellow_corner_background);
            break;
        case 16:
            mNumberView.setBackgroundResource(R.drawable.item_light_orange_corner_background);
            break;
        case 32:
            mNumberView.setBackgroundResource(R.drawable.item_deep_orange_corner_background);
            break;
        case 64:
            mNumberView.setBackgroundResource(R.drawable.item_light_red_corner_background);
            break;
        case 128:
            mNumberView.setBackgroundResource(R.drawable.item_deep_red_corner_background);
            break;
        case 256:
            mNumberView.setBackgroundResource(R.drawable.item_light_blue_corner_background);
            break;
        case 512:
            mNumberView.setBackgroundResource(R.drawable.item_deep_blue_corner_background);
            break;
        case 1024:
            mNumberView.setBackgroundResource(R.drawable.item_light_purple_corner_background);
            break;
        case 2048:
            mNumberView.setBackgroundResource(R.drawable.item_deep_purple_corner_background);
            break;
        case 4096:
            mNumberView.setBackgroundResource(R.drawable.item_deep_green_corner_background);
            break;
        default:
            mNumberView.setBackgroundResource(R.drawable.item_grey_corner_background);
        }
    }

    //设置Item  也就是更新数据
    public void setItem(int number){
        mNumber = number;
        mNumberView.setText(mNumber == 0 ? "" : mNumber + "");
        setBackground(mNumber);
    }

    public void setNumber(int number){
        this.mNumber = number;
    }

    public int getNumber(){
        return this.mNumber;
    }

}
