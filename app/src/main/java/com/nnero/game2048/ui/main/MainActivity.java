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
import com.nnero.game2048.viewmodel.GameViewModel;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView mGoalView;
    private TextView mRecordView;
    private TextView mOptionView;
    private TextView mPointView;
    private TextView mRestartView;
    private TextView mUndoView;
    private GameView mGameView;
    private SharedPreferences mSp;

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

        mGoalView.setText(App.goal+"");
        mPointView.setText("0");


        //游戏状态
        GameViewModel.INSTANCE.getGameState().subscribe(state -> {
            switch (state) {
            case GameViewModel.STATE_CONTINUE:
                break;
            case GameViewModel.STATE_OVER:
                Intent intent = new Intent(App.getContext(), ResultActivity.class);
                intent.setAction(ResultActivity.ACTION_FAILURE);
                startActivityForResult(intent, 0);
                break;
            case GameViewModel.STATE_SUCCESS:
                Intent intentSuccess = new Intent(App.getContext(), ResultActivity.class);
                intentSuccess.setAction(ResultActivity.ACTION_SUCCESS);
                startActivityForResult(intentSuccess, 0);
                break;
            }
        }, e -> {});
        GameViewModel.INSTANCE.getTotalPoints().subscribe(points->{
            mPointView.setText(points+"");
        },e->{});
        GameViewModel.INSTANCE.getRecordPoint().subscribe(recordPoint->{
            mRecordView.setText(recordPoint+"");
        },e->{});
    }

    @Override
    protected void initData() {
        mSp = App.getSharedPreferences();
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
