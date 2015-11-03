package com.nnero.game2048.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import com.nnero.game2048.base.App;
import com.nnero.game2048.util.DimensionUtils;
import com.nnero.game2048.util.LogUtil;
import com.nnero.game2048.viewmodel.AnimationViewModel;
import com.nnero.game2048.viewmodel.GameViewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import rx.subscriptions.CompositeSubscription;

/**
 * Author:NNERO
 * <p/>
 * TIME: 15/10/16 下午8:56
 * <p/>
 * DESCRIPTION:主要游戏展示view
 */
public class GameView extends GridLayout implements View.OnTouchListener{

    private GameItem[][] mGameMatrix;//方格矩阵

    private OperationType mOperation;//当前操作

    //onTouch  初始的 坐标
    private float mInitX;
    private float mInitY;

    private CompositeSubscription subs = new CompositeSubscription();

    enum OperationType{
        LEFT,
        RIGHT,
        TOP,
        DOWN,
        INIT, //初始化的时候
        OVER //游戏结束 无法移动方块
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
            if(x > 0) {
                GameViewModel.INSTANCE.doAction(GameViewModel.ACTION_LEFT);
            }else
                GameViewModel.INSTANCE.doAction(GameViewModel.ACTION_RIGHT);
        } else if(Math.abs(x) - Math.abs(y) < -10){
            if(y > 0)
                GameViewModel.INSTANCE.doAction(GameViewModel.ACTION_UP);
            else
                GameViewModel.INSTANCE.doAction(GameViewModel.ACTION_DOWN);
        } else {
            //滑动不明显 不作处理
        }
    }

    //包括 矩阵  以及方格生产
    private void init(){
        this.setOnTouchListener(this);

        mGameMatrix = new GameItem[App.level][App.level];
        setRowCount(App.level);
        setColumnCount(App.level);

        initGame();
    }


    public void initGame(){
        subs.clear();
        GameViewModel.INSTANCE.resetGameData();
        removeAllViews();
        GameItem item;
        for(int i=0;i<App.level;i++){
            for(int j=0;j<App.level;j++){
                item = new GameItem(getContext(),0); //生产方格
                mGameMatrix[i][j] = item;
                addView(item,App.itemSize,App.itemSize);
            }
        }
        mOperation = OperationType.INIT;
        //随机出现格子
        subs.add(GameViewModel.INSTANCE.getRandomPoint().subscribe(point -> {
            mGameMatrix[point.x][point.y].setItem(point.number);
        }, e -> {}));
        //移动手势 更新所有格子
        subs.add(GameViewModel.INSTANCE.getGameMatrix().subscribe(gameMatrix -> {
            for (int i = 0; i < App.level; i++) {
                for (int j = 0; j < App.level; j++) {
                    mGameMatrix[i][j].setItem(gameMatrix[i][j]);
                }
            }
            GameViewModel.INSTANCE.checkResult();
            GameViewModel.INSTANCE.doAction(GameViewModel.ACTION_RANDOM);
        }, e -> {}));
        //动画层设置
        //在播放动画的同时，逻辑层应该不显示
        subs.add(AnimationViewModel.INSTANCE.getScalePoint().subscribe(point->{
            mGameMatrix[point.x][point.y].setItem(0);
        },e->{}));
    }

    //记录 坐标
    public static class Point{
        public int x;
        public int y;
        public int number;

        public Point(int x,int y){
            this.x = x;
            this.y = y;
        }

        public Point(int x,int y,int number){
            this(x, y);
            this.number = number;
        }
    }

}
