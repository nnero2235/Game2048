package com.nnero.game2048.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import com.nnero.game2048.base.App;
import com.nnero.game2048.util.LogUtil;
import com.nnero.game2048.viewmodel.AnimationViewModel;
import com.nnero.game2048.viewmodel.GameViewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Author: nnero
 * <p>
 * time : 15/11/3  上午11:38
 * <p>
 * Description:动画层
 */
public class AnimationLayer extends FrameLayout{

    private List<GameItem> animationItems;

    private CompositeSubscription subs = new CompositeSubscription();

    public AnimationLayer(Context context) {
        super(context);
        init();
    }

    public AnimationLayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimationLayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void recycleItem(GameItem item){
        item.setVisibility(INVISIBLE);
        item.setAnimation(null);
        item.setNumber(0);
        animationItems.add(item);
    }

    private void init(){
        animationItems = new LinkedList<>();

        subs.add(AnimationViewModel.INSTANCE.getScalePoint().subscribe(point->{createScaleAnimation(point);}, e -> {
        }));

    }

    public void createScaleAnimation(GameView.Point point){
        LogUtil.d("animation start");
        GameItem item = animationItems.remove(0);

        if(item == null){
            item = new GameItem(getContext(),0);
            addView(item);
        }
        item.setVisibility(VISIBLE);
        item.setItem(point.number);

        LayoutParams params = new LayoutParams(App.itemSize,App.itemSize);
        params.leftMargin = point.x * App.itemSize;
        params.topMargin = point.y * App.itemSize;
        item.setLayoutParams(params);

        ScaleAnimation scale = new ScaleAnimation(0.5f,1.0f,0.5f,1.0f,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        scale.setDuration(300);
        final GameItem finalItem = item;
        scale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                LogUtil.d("animation over");
                recycleItem(finalItem);
                GameViewModel.INSTANCE.randomBox(point);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        item.startAnimation(scale);
    }
}
