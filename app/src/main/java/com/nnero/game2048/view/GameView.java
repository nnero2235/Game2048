package com.nnero.game2048.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.TextView;
import com.nnero.game2048.base.App;
import com.nnero.game2048.util.DimensionUtils;
import com.nnero.game2048.util.LogUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Author:NNERO
 * <p/>
 * TIME: 15/10/16 下午8:56
 * <p/>
 * DESCRIPTION:主要游戏展示view
 */
public class GameView extends GridLayout implements View.OnTouchListener{

    private GameItem[][] mGameMatrix;//方格矩阵
    private List<Point> mBlanks;//空格位置  表示矩阵中 的空格位置 用于判断生产方格 和游戏是否结束

    private OperationType mOperation;//当前操作
    private int mItemSize;

    //onTouch  初始的 坐标
    private float mInitX;
    private float mInitY;

    enum OperationType{
        LEFT,
        RIGHT,
        TOP,
        DOWN,
        INIT //初始化的时候
    }

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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
        case MotionEvent.ACTION_DOWN:
            mInitX = event.getX();
            mInitY = event.getY();
            break;
        case MotionEvent.ACTION_MOVE:
            break;
        case MotionEvent.ACTION_UP:
            doAction(mInitX, mInitY, event.getX(), event.getY());
            break;
        }
        return true;
    }
    //执行操作 判断 是左滑 右滑 下滑 下滑  如果连续2次同一方向 则视为无效滑动
    private void doAction(float downX,float downY,float upX,float upY){
        float x = downX - upX;
        float y = downY - upY;

        if(Math.abs(x) - Math.abs(y) > 10){
            if(x > 0)
                slipLeft();
            else
                slipRight();
        } else if(Math.abs(x) - Math.abs(y) < -10){
            if(y > 0)
                slipTop();
            else
                slipDown();
        } else {
            //滑动不明显 不作处理
        }
    }

    //包括 矩阵  以及方格生产
    private void init(){
        this.setOnTouchListener(this);

        mGameMatrix = new GameItem[App.level][App.level];
        mBlanks = new LinkedList<>();//会频繁 增删
        mItemSize = DimensionUtils.calculateItemSize(App.level);//通过screenwidth计算 size
        setRowCount(App.level);
        setColumnCount(App.level);

        removeAllViews();
        GameItem item;
        for(int i=0;i<App.level;i++){
            for(int j=0;j<App.level;j++){
                item = new GameItem(getContext(),0); //生产方格
                mGameMatrix[i][j] = item;
                mBlanks.add(new Point(i,j));
                addView(item,mItemSize,mItemSize);
            }
        }
        //初始生成2个格子
        mOperation = OperationType.INIT;
        randomBox(mOperation);
        randomBox(mOperation);
    }

    private void randomBox(OperationType type){ //生产方格：主要是2 和 4 2个类型  调用一次 生产一个
        Random random = new Random();
        int number = random.nextInt(2) == 0 ? 2 : 4;
        switch (type){
        case LEFT:
            break;
        case RIGHT:
            break;
        case TOP:
            break;
        case DOWN:
            break;
        case INIT:
            boolean flag = true;
            while(flag) { //由于是 生成2个格子  如果 再次随机到 同一位置则不成功 就继续随机
                int x = random.nextInt(App.level);
                int y = random.nextInt(App.level);
                for (Point p : mBlanks) {
                    if (p.x == x && p.y == y) {
                        mGameMatrix[x][y].setItem(number);
                        reduceBlanks(x, y);
                        flag = false;
                        break;
                    }
                }
            }
            break;
        }
    }

    //减少空白格子 当 randomBox的时候
    private void reduceBlanks(int x,int y){
        for(Point p : mBlanks) {
            if(p.x == x && p.y == y) {
                mBlanks.remove(p);
                break;
            }
        }
    }

    //增加空白格子 当 2个数字相同 消减格子的时候
    private void addBlanks(Point p){
        mBlanks.add(p);
    }

    private void slipLeft(){
        LogUtil.d("左滑");
        if(mOperation == OperationType.LEFT){
            return;
        }
    }

    private void slipRight(){
        LogUtil.d("右滑");
        if(mOperation == OperationType.RIGHT){
            return;
        }
        for(int i=0;i<App.level;i++){
            for(int j=0;j<App.level;j++){
                if(mGameMatrix[i][j].getNumber() != 0){
                    mGameMatrix[i][j].animate().translationX(mItemSize);
                }
            }
        }
    }

    private void slipTop(){
        LogUtil.d("上滑");
        if(mOperation == OperationType.TOP){
            return;
        }
    }

    private void slipDown(){
        LogUtil.d("下滑");
        if(mOperation == OperationType.DOWN){
            return;
        }
    }

    //记录 坐标
    class Point{
        public int x;
        public int y;

        public Point(int x,int y){
            this.x = x;
            this.y = y;
        }
    }

}
