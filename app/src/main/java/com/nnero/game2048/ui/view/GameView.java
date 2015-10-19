package com.nnero.game2048.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import com.nnero.game2048.base.App;
import com.nnero.game2048.ui.main.PointRecorder;
import com.nnero.game2048.util.DimensionUtils;
import com.nnero.game2048.util.LogUtil;

import java.util.ArrayList;
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
    private List<Integer> mTempItems;//辅助集合 用于计算 执行结果

    private OperationType mOperation;//当前操作
    private int mItemSize;
    private int mKeyNumber = -1; //用于碰撞判断
    private boolean isMove; //是否移动过  必须等到checkResult过后才能 继续move

    //onTouch  初始的 坐标
    private float mInitX;
    private float mInitY;

    private OnGameOverListener mListener;

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
        if(mListener == null){
            throw new RuntimeException("必须设置游戏结束监听！");
        }
        if(isMove){
            return true;
        }
        isMove = true;
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
        PointRecorder.getRecorder().updatePoint();
        checkResult();
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
        mTempItems = new ArrayList<>();
        mBlanks = new LinkedList<>();//会频繁 增删
        mItemSize = DimensionUtils.calculateItemSize(App.level);//通过screenwidth计算 size
        setRowCount(App.level);
        setColumnCount(App.level);

        initGame();
    }

    public void initGame(){
        isMove = false;
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
        int number = random.nextInt(4) == 3 ? 4 : 2; //4 和 2 是4比1的比例
        checkBlanks();
        if(mBlanks.size() == 0)
            return;
        int index;
        //TODO:待 重构  四个方向 只能生产边上的 方格
        switch (type){
        case LEFT:
            index = random.nextInt(mBlanks.size());
            mGameMatrix[mBlanks.get(index).x][mBlanks.get(index).y].setItem(number);
            break;
        case RIGHT:
            index = random.nextInt(mBlanks.size());
            mGameMatrix[mBlanks.get(index).x][mBlanks.get(index).y].setItem(number);
            break;
        case TOP:
            index = random.nextInt(mBlanks.size());
            mGameMatrix[mBlanks.get(index).x][mBlanks.get(index).y].setItem(number);
            break;
        case DOWN:
            index = random.nextInt(mBlanks.size());
            mGameMatrix[mBlanks.get(index).x][mBlanks.get(index).y].setItem(number);
            break;
        case INIT:
            index = random.nextInt(mBlanks.size());
            mGameMatrix[mBlanks.get(index).x][mBlanks.get(index).y].setItem(number);
            break;
        }
    }


    private void checkBlanks(){//获取空白格子
        mBlanks.clear();
        for(int i=0;i<App.level;i++){
            for(int j=0;j<App.level;j++){
                if(mGameMatrix[i][j].getNumber() == 0){
                    mBlanks.add(new Point(i,j));
                }
            }
        }
    }

    public void registGameOverListener(OnGameOverListener l){
        mListener = l;
    }

    //是否结束  没有格子 可以合并 并且 格子填满 则结束  或者 达到goal
    private void checkResult(){
        checkBlanks();
        if(mBlanks.size() == 0){
            if(!checkFullBoxContinue(true) && !checkFullBoxContinue(false)){
                mListener.onFailure();
            }
        } else {
            for(int i=0;i<App.level;i++){
                for(int j=0;j<App.level;j++){
                    if(mGameMatrix[i][j].getNumber() == App.goal){
                        mListener.onSuccess();
                        break;
                    }
                }
            }
        }
        isMove = false;
    }

    //判断 是否 继续 相邻 元素 是否相同  都不同 则结束
    private boolean checkFullBoxContinue(boolean isRow){ //列 还是 行
        for(int i=0;i<App.level;i++){
            for(int j=0;j<App.level;j++){
                int currentNumber;
                if(isRow){
                    currentNumber = mGameMatrix[i][j].getNumber();
                } else {
                    currentNumber = mGameMatrix[j][i].getNumber();
                }
                if(mKeyNumber == -1){
                    mKeyNumber = currentNumber;
                } else {
                    if(mKeyNumber == currentNumber){
                        mKeyNumber = -1;
                        return true;
                    } else {
                        mKeyNumber = currentNumber;
                    }
                }
            }
            mKeyNumber = -1;
        }
        return false;
    }


    private void slipLeft(){
        LogUtil.d("左滑");
        if(mOperation == OperationType.LEFT){
            return;
        }
        for(int i=0;i<App.level;i++){
            for(int j=0;j<App.level;j++){ //处理每一行
                int currentNumber = mGameMatrix[i][j].getNumber();
                if(currentNumber != 0) {
                    if(mKeyNumber == -1){
                        mKeyNumber = currentNumber;
                    } else {
                        if(mKeyNumber == currentNumber){
                            PointRecorder.getRecorder().recordPoint(mKeyNumber*2);
                            mTempItems.add(mKeyNumber*2);
                            mKeyNumber = -1; //防止重复碰撞  碰撞只会发生一次
                        }else{
                            mTempItems.add(mKeyNumber);
                            mKeyNumber = currentNumber;
                        }
                    }
                } else {
                    continue;
                }
            }

            if(mKeyNumber != -1){
                mTempItems.add(mKeyNumber);//碰撞 会漏掉一个元素  需要补上
            }

            //不够的补0
            for(int k = mTempItems.size(); k < App.level;k++){
                mTempItems.add(0);
            }

            //进行view更新
            for(int k=0;k<App.level;k++) {
                mGameMatrix[i][k].setItem(mTempItems.get(k));
            }
            mTempItems.clear();
            mKeyNumber = -1;//重置
        }
        randomBox(OperationType.LEFT);
    }

    private void slipRight(){
        LogUtil.d("右滑");
        if(mOperation == OperationType.RIGHT){
            return;
        }
        for(int i=0;i<App.level;i++){
            for(int j=App.level-1;j>=0;j--){ //处理每一行
                int currentNumber = mGameMatrix[i][j].getNumber();
                if(currentNumber != 0) {
                    if(mKeyNumber == -1){
                        mKeyNumber = currentNumber;
                    } else {
                        if(mKeyNumber == currentNumber){
                            PointRecorder.getRecorder().recordPoint(mKeyNumber*2);
                            mTempItems.add(mKeyNumber*2);
                            mKeyNumber = -1; //防止重复碰撞  碰撞只会发生一次
                        }else{
                            mTempItems.add(mKeyNumber);
                            mKeyNumber = currentNumber;
                        }
                    }
                } else {
                    continue;
                }
            }

            if(mKeyNumber != -1){
                mTempItems.add(mKeyNumber);//碰撞 会漏掉一个元素  需要补上
            }

            //不够的补0
            for(int k = mTempItems.size(); k < App.level;k++){
                mTempItems.add(0);
            }


            //进行view更新
            int index = mTempItems.size()-1;
            for(int k=0;k<App.level;k++) {
                mGameMatrix[i][k].setItem(mTempItems.get(index--));
            }
            mTempItems.clear();
            mKeyNumber = -1;//重置
        }
        randomBox(OperationType.RIGHT);
    }

    private void slipTop(){
        LogUtil.d("上滑");
        if(mOperation == OperationType.TOP){
            return;
        }
        for(int i=0;i<App.level;i++){
            for(int j=0;j<App.level;j++){ //处理每一行
                int currentNumber = mGameMatrix[j][i].getNumber();
                if(currentNumber != 0) {
                    if(mKeyNumber == -1){
                        mKeyNumber = currentNumber;
                    } else {
                        if(mKeyNumber == currentNumber){
                            PointRecorder.getRecorder().recordPoint(mKeyNumber*2);
                            mTempItems.add(mKeyNumber*2);
                            mKeyNumber = -1; //防止重复碰撞  碰撞只会发生一次
                        }else{
                            mTempItems.add(mKeyNumber);
                            mKeyNumber = currentNumber;
                        }
                    }
                } else {
                    continue;
                }
            }

            if(mKeyNumber != -1){
                mTempItems.add(mKeyNumber);//碰撞 会漏掉一个元素  需要补上
            }

            //不够的补0
            for(int k = mTempItems.size(); k < App.level;k++){
                mTempItems.add(0);
            }

            //进行view更新
            for(int k=0;k<App.level;k++) {
                mGameMatrix[k][i].setItem(mTempItems.get(k));
            }
            mTempItems.clear();
            mKeyNumber = -1;//重置
        }
        randomBox(OperationType.TOP);
    }


    private void slipDown(){
        LogUtil.d("下滑");
        if(mOperation == OperationType.DOWN){
            return;
        }
        for(int i=0;i<App.level;i++){
            for(int j=App.level-1;j>=0;j--){ //处理每一行
                int currentNumber = mGameMatrix[j][i].getNumber();
                if(currentNumber != 0) {
                    if(mKeyNumber == -1){
                        mKeyNumber = currentNumber;
                    } else {
                        if(mKeyNumber == currentNumber){
                            PointRecorder.getRecorder().recordPoint(mKeyNumber*2);
                            mTempItems.add(mKeyNumber*2);
                            mKeyNumber = -1; //防止重复碰撞  碰撞只会发生一次
                        }else{
                            mTempItems.add(mKeyNumber);
                            mKeyNumber = currentNumber;
                        }
                    }
                } else {
                    continue;
                }
            }

            if(mKeyNumber != -1){
                mTempItems.add(mKeyNumber);//碰撞 会漏掉一个元素  需要补上
            }

            //不够的补0
            for(int k = mTempItems.size(); k < App.level;k++){
                mTempItems.add(0);
            }

            //进行view更新
            int index = mTempItems.size()-1;
            for(int k=0;k<App.level;k++) {
                mGameMatrix[k][i].setItem(mTempItems.get(index));
                index--;
            }
            mTempItems.clear();
            mKeyNumber = -1;//重置
        }
        randomBox(OperationType.DOWN);
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

    public interface OnGameOverListener{
        void onSuccess();
        void onFailure();
    }

}
