package com.nnero.game2048.viewmodel;

import com.nnero.game2048.base.App;
import com.nnero.game2048.ui.view.GameView;
import com.nnero.game2048.util.LogUtil;
import com.nnero.game2048.util.SPKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Author: nnero
 * <p>
 * time : 15/11/2  上午10:01
 * <p>
 * Description:数据的viewmodel
 */
public enum  GameViewModel {
    INSTANCE;

    public static final int ACTION_LEFT = 0;
    public static final int ACTION_RIGHT = 1;
    public static final int ACTION_UP = 2;
    public static final int ACTION_DOWN = 3;
    public static final int ACTION_RANDOM = 4;

    public static final int STATE_CONTINUE = 10;
    public static final int STATE_OVER = 11;
    public static final int STATE_SUCCESS = 12;

    private Integer[][] mGameMatrix; //游戏数据矩阵
    private List<GameView.Point> mBlanks = new ArrayList<>(); //计算游戏 number为0的 集合
    private List<Integer> mTempNumbers; //临时集合 辅助计算游戏逻辑
    private int mPoint; //本次操作得分
    private int mTempPoint; //辅助计算 结果得分
    private int mPoints;//当前总得分
    private int mRecordPoint;//记录 得分

    private PublishSubject<Boolean> mCheckBlanksSub = PublishSubject.create();
    private PublishSubject<GameView.Point> mRandomBoxSub = PublishSubject.create();
    private PublishSubject<Integer> mActionSub = PublishSubject.create();
    private PublishSubject<Integer[][]> mGameMatrixSub = PublishSubject.create();

    private BehaviorSubject<List<GameView.Point>> mBlanksSub = BehaviorSubject.create();
    private BehaviorSubject<Integer> mGameStateSub = BehaviorSubject.create();
    private BehaviorSubject<Integer> mPointSub = BehaviorSubject.create();
    private BehaviorSubject<Integer> mPointsSub = BehaviorSubject.create();
    private BehaviorSubject<Integer> mRecordPointSub = BehaviorSubject.create();

    private CompositeSubscription subs = new CompositeSubscription();
    private int mKeyNumber = -1;

    public static void initGameData(){
        GameViewModel.INSTANCE.init();
    }

    private void init(){
        initGamePoint();
        initGameMatrix();
        subs.clear();
        subs.add(getAction().subscribe(action->{
            switch (action){
            case ACTION_LEFT:
                leftGameLogic();
                break;
            case ACTION_RIGHT:
                rightGameLogic();
                break;
            case ACTION_DOWN:
                downGameLogic();
                break;
            case ACTION_UP:
                upGameLogic();
                break;
            case ACTION_RANDOM:
                randomGameLogic();
                return; //直接返回
            }
            mPointSub.onNext(mTempPoint);
            mGameMatrixSub.onNext(mGameMatrix);
            mTempPoint = 0;
        },e->{}));
        subs.add(isCheckBlanks().subscribe(check->{
            if(check) {
                mBlanks.clear();
                for (int i = 0; i < App.level; i++) {
                    for (int j = 0; j < App.level; j++) {
                        if (mGameMatrix[i][j] == 0) {
                            mBlanks.add(new GameView.Point(i, j));
                        }
                    }
                }
                mBlanksSub.onNext(mBlanks);
            }
        },e->{}));
        subs.add(getCurrentPoint().subscribe(point->{
            mPoint = point;
            mPoints += point;
            if(mPoints > mRecordPoint){
                mRecordPoint = mPoints;
                mRecordPointSub.onNext(mRecordPoint);
            }
            mPointsSub.onNext(mPoints);
        },e->{}));
    }

    private void initGameMatrix(){
        mGameMatrix = new Integer[App.level][App.level];
        mTempNumbers = new ArrayList<>();
        for(int i=0;i<App.level;i++){
            for(int j=0;j<App.level;j++){
                mGameMatrix[i][j] = 0;
            }
        }
    }
    //初始化游戏point
    private void initGamePoint(){
        mPoint = 0;
        mPoints = 0;
        mRecordPoint = App.getSharedPreferences().getInt(SPKeys.RECORD, 0);
        mRecordPointSub.onNext(mRecordPoint); //当即 就保存一个 数据
    }

    private void leftGameLogic(){
        LogUtil.d("左滑");
        for(int i=0;i<App.level;i++){
            for(int j=0;j<App.level;j++){
                popLogic(i,j);
            }
            if(mKeyNumber != -1){//如果全时0的行 就不会出现这个情况
                mTempNumbers.add(mKeyNumber);
            }
            for(int j=0;j<App.level;j++){
                if(j < mTempNumbers.size())
                    mGameMatrix[i][j] = mTempNumbers.get(j);
                else
                    mGameMatrix[i][j] = 0;
            }
            mKeyNumber = -1;
            mTempNumbers.clear();
        }
    }

    private void rightGameLogic(){
        LogUtil.d("右滑");
        for(int i=App.level-1;i>=0;i--){
            for(int j=App.level-1;j>=0;j--){
                popLogic(i,j);
            }
            if(mKeyNumber != -1){//如果全时0的行 就不会出现这个情况
                mTempNumbers.add(mKeyNumber);
            }
            int index = 0;
            for(int j=App.level-1;j>=0;j--){
                if(index < mTempNumbers.size())
                    mGameMatrix[i][j] = mTempNumbers.get(index++);
                else
                    mGameMatrix[i][j] = 0;
            }
            mKeyNumber = -1;
            mTempNumbers.clear();
        }
    }

    private void downGameLogic(){
        LogUtil.d("下滑");
        for(int i=App.level-1;i>=0;i--){
            for(int j=App.level-1;j>=0;j--){
                popLogic(j,i);
            }
            if(mKeyNumber != -1){//如果全时0的行 就不会出现这个情况
                mTempNumbers.add(mKeyNumber);
            }
            int index = 0;
            for(int j=App.level-1;j>=0;j--){
                if(index < mTempNumbers.size())
                    mGameMatrix[j][i] = mTempNumbers.get(index++);
                else
                    mGameMatrix[j][i] = 0;
            }
            mKeyNumber = -1;
            mTempNumbers.clear();
        }
    }

    private void upGameLogic(){
        LogUtil.d("上滑");
        for(int i=0;i<App.level;i++){
            for(int j=0;j<App.level;j++){
                popLogic(j,i);
            }
            if(mKeyNumber != -1){//如果全时0的行 就不会出现这个情况
                mTempNumbers.add(mKeyNumber);
            }
            for(int j=0;j<App.level;j++){
                if(j < mTempNumbers.size())
                    mGameMatrix[j][i] = mTempNumbers.get(j);
                else
                    mGameMatrix[j][i] = 0;
            }
            mKeyNumber = -1;
            mTempNumbers.clear();
        }
    }
    //抽取了一个公共方法
    private void popLogic(int row,int column){
        if(mGameMatrix[row][column] != 0) {
            if (mKeyNumber == -1) {
                mKeyNumber = mGameMatrix[row][column];
            } else {
                if (mKeyNumber == mGameMatrix[row][column]) { //碰撞
                    mTempNumbers.add(mKeyNumber * 2);
                    mTempPoint += (mKeyNumber * 2);
                    mKeyNumber = -1;//碰撞之后不能再进这里了
                } else {//未发生碰撞
                    mTempNumbers.add(mKeyNumber);
                    mKeyNumber = mGameMatrix[row][column];
                }
            }
        }
    }

    private void randomGameLogic(){
        Random random = new Random();
        int number = random.nextInt(4) == 3 ? 4 : 2; //4 和 2 是4比1的比例
        checkBlanks();
        if(mBlanks.size() == 0){
            return;
        }
        int index = random.nextInt(mBlanks.size());
        mGameMatrix[mBlanks.get(index).x][mBlanks.get(index).y] = number;
        AnimationViewModel.INSTANCE.startScaleAnimation(new GameView.Point(mBlanks.get(index).x,mBlanks.get(index).y,number));
    }

    //判断 是否 继续 相邻 元素 是否相同  都不同 则结束
    private boolean checkFullBoxContinue(boolean isRow){ //列 还是 行
        for(int i=0;i<App.level;i++){
            for(int j=0;j<App.level;j++){
                int currentNumber;
                if(isRow){
                    currentNumber = mGameMatrix[i][j];
                } else {
                    currentNumber = mGameMatrix[j][i];
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

    /**
     * 发送一个需要显示random的点
     * @param point
     */
    public void randomBox(GameView.Point point){
        mRandomBoxSub.onNext(point);
    }

    /**
     * 是否结束  没有格子 可以合并 并且 格子填满 则结束  或者 达到goal
     */
    public void checkResult(){
        checkBlanks();
        if(mBlanks.size() == 0){
            if(!checkFullBoxContinue(true) && !checkFullBoxContinue(false)){
                mGameStateSub.onNext(STATE_OVER);
            }else {
                mGameStateSub.onNext(STATE_CONTINUE);
            }
        } else {
            for(int i=0;i<App.level;i++){
                for(int j=0;j<App.level;j++){
                    if(mGameMatrix[i][j] == App.goal){
                        mGameStateSub.onNext(STATE_SUCCESS);
                        return;
                    }
                }
            }
            mGameStateSub.onNext(STATE_CONTINUE);
        }
    }

    /**
     * 执行哪个动作
     * @param action
     */
    public void doAction(int action){
        mActionSub.onNext(action);
    }

    /**
     * 获取当前 空格数
     */
    public void checkBlanks(){
        mCheckBlanksSub.onNext(true);
    }

    /**
     * 得分
     * @param point
     */
    public void point(int point){
        mPointSub.onNext(point);
    }

    /**
     * 重置所有数据
     */
    public void resetGameData(){
        mTempPoint = 0;
        mPoint = 0;
        mPoints = 0;
        for(int i=0;i<App.level;i++){
            for(int j=0;j<App.level;j++){
                mGameMatrix[i][j] = 0;
            }
        }
    }
//Observables
    public Observable<Integer> getAction(){
        return mActionSub.asObservable();
    }

    public Observable<Integer[][]> getGameMatrix(){
        return mGameMatrixSub.asObservable();
    }

    public Observable<List<GameView.Point>> getBlanks(){
        return mBlanksSub.asObservable();
    }

    public Observable<Boolean> isCheckBlanks(){
        return mCheckBlanksSub.asObservable();
    }

    public Observable<GameView.Point> getRandomPoint(){
        return mRandomBoxSub.asObservable();
    }

    public Observable<Integer> getGameState(){
        return mGameStateSub.asObservable();
    }

    public Observable<Integer> getCurrentPoint(){
        return mPointSub.asObservable();
    }

    public Observable<Integer> getTotalPoints(){
        return mPointsSub.asObservable();
    }

    public Observable<Integer> getRecordPoint(){
        return mRecordPointSub.asObservable();
    }
}
