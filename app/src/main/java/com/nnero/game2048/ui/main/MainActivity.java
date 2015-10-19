package com.nnero.game2048.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;
import com.nnero.game2048.R;
import com.nnero.game2048.base.App;
import com.nnero.game2048.base.BaseActivity;
import com.nnero.game2048.ui.config.ConfigActivity;
import com.nnero.game2048.ui.result.ResultActivity;
import com.nnero.game2048.util.SPKeys;
import com.nnero.game2048.ui.view.GameView;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView mGoalView;
    private TextView mRecordView;
    private TextView mOptionView;
    private TextView mPointView;
    private TextView mRestartView;
    private TextView mUndoView;
    private GameView mGameView;
    private SharedPreferences mSp;
    private int mRecord;

    @Override
    protected void initView() {
        mOptionView = (TextView) findViewById(R.id.main_option_tv);
        mRecordView = (TextView) findViewById(R.id.main_record_tv);
        mGoalView = (TextView) findViewById(R.id.main_goal_tv);
        mPointView = (TextView) findViewById(R.id.main_point_tv);
        mGameView = (GameView) findViewById(R.id.main_gameview);
        mRestartView = (TextView) findViewById(R.id.main_restart_tv);
        mUndoView = (TextView) findViewById(R.id.main_undo_tv);

        mOptionView.setOnClickListener(this);
        mRestartView.setOnClickListener(this);
        mUndoView.setOnClickListener(this);

        mRecordView.setText(mRecord + "");
        mGoalView.setText(App.goal+"");
        mPointView.setText("0");

        mGameView.registGameOverListener(new GameView.OnGameOverListener() {
            @Override
            public void onSuccess() {
                mSp.edit().putInt(SPKeys.RECORD, mRecord).apply();
                Intent intent = new Intent(App.getContext(), ResultActivity.class);
                intent.setAction(ResultActivity.ACTION_SUCCESS);
                startActivityForResult(intent, 0);
            }

            @Override
            public void onFailure() {
                mSp.edit().putInt(SPKeys.RECORD, mRecord).apply();
                Intent intent = new Intent(App.getContext(), ResultActivity.class);
                intent.setAction(ResultActivity.ACTION_FAILURE);
                startActivityForResult(intent, 0);
            }
        });

        PointRecorder.getRecorder().init();
        PointRecorder.getRecorder().registOnRecordListener(new PointRecorder.OnRecordListener() {
            @Override
            public void updateViews(int point, int points) {
                mPointView.setText(points+"");
                if(points > mRecord){
                    mRecordView.setText(points+"");
                    mRecord = points;
                }
            }
        });
    }

    @Override
    protected void initData() {
        mSp = App.getSharedPreferences();
        App.goal = App.GOALS[mSp.getInt(SPKeys.GOAL_POS,1)];
        App.level = App.LEVELS[mSp.getInt(SPKeys.LEVEL_POS,0)];
        mRecord = mSp.getInt(SPKeys.RECORD, 0);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        case R.id.main_option_tv:
            startActivityForResult(new Intent(this, ConfigActivity.class), 0);
            break;
        case R.id.main_restart_tv:
            mGameView.initGame();
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 100){
            mGameView.initGame();
        } else if(resultCode == 200) {
            mGoalView.setText(App.goal + "");
        }
    }

}
