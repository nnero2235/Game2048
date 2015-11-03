package com.nnero.game2048.viewmodel;

import com.nnero.game2048.ui.view.GameView;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Author: nnero
 * <p>
 * time : 15/11/3  上午11:49
 * <p>
 * Description:动画层的 数据驱动
 */
public enum  AnimationViewModel {
    INSTANCE;

    public static final int ANIMATION_STATE_START = 0;//动画状态 播一个动画
    public static final int ANIMATION_STATE_FINISH = 1;

    private PublishSubject<Integer> mScaleStateSub = PublishSubject.create();
    private PublishSubject<GameView.Point> mScalePointSub = PublishSubject.create();

    private CompositeSubscription subs = new CompositeSubscription();

    private void init(){
    }

    /**
     * 发送一个动画状态消息
     * @param state
     */
    public void setScaleAnimationState(int state){
        mScaleStateSub.onNext(state);
    }

    /**
     * 发送需要播放动画的 point
     * @param point
     */
    public void startScaleAnimation(GameView.Point point){
        mScalePointSub.onNext(point);
    }

    public Observable<Integer> getScaleAnimationState(){
        return mScaleStateSub.asObservable();
    }

    public Observable<GameView.Point> getScalePoint(){
        return mScalePointSub.asObservable();
    }
}
