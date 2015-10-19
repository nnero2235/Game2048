package com.nnero.game2048.ui.main;

/**
 * Author:NNERO
 * <p/>
 * TIME: 15/10/19 下午10:45
 * <p/>
 * DESCRIPTION:计分器 ： 记录得分 和 记录
 */
public class PointRecorder {
    private static PointRecorder sPointRecorder;

    private int mPoints;//当前总分
    private int mPoint;//本次得分
    private OnRecordListener mListener;

    private PointRecorder(){}

    public static PointRecorder getRecorder(){
        if(sPointRecorder == null)
            sPointRecorder = new PointRecorder();
        return sPointRecorder;
    }

    public void init(){
        mPoint = 0;
        mPoints = 0;
    }

    public void registOnRecordListener(OnRecordListener l){
        mListener = l;
    }

    public void recordPoint(int p){
        mPoint += p;
        mPoints += p;
    }

    public void updatePoint(){
        if(mListener != null)
            mListener.updateViews(mPoint,mPoints);
        mPoint = 0;
    }


    public interface OnRecordListener{
        void updateViews(int point,int points);
    }
}
