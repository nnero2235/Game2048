package com.nnero.game2048.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;
import com.nnero.game2048.R;
import com.nnero.game2048.base.App;
import com.nnero.game2048.base.BaseActivity;
import com.nnero.game2048.ui.config.ConfigActivity;
import com.nnero.game2048.util.SPKeys;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView mGoalView;
    private TextView mRecordView;
    private TextView mOptionView;
    private TextView mPointView;
    private SharedPreferences mSp;

    @Override
    protected void initView() {
        mOptionView = (TextView) findViewById(R.id.main_option_tv);
        mRecordView = (TextView) findViewById(R.id.main_record_tv);
        mGoalView = (TextView) findViewById(R.id.main_goal_tv);
        mPointView = (TextView) findViewById(R.id.main_point_tv);

        mOptionView.setOnClickListener(this);


        mRecordView.setText(mSp.getInt(SPKeys.RECORD, 0)+"");
        mGoalView.setText(App.goal+"");
        mPointView.setText("0");

    }

    @Override
    protected void initData() {
        mSp = App.getSharedPreferences();
        App.goal = App.GOALS[mSp.getInt(SPKeys.GOAL_POS,1)];
        App.level = App.LEVELS[mSp.getInt(SPKeys.LEVEL_POS,0)];
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGoalView.setText(App.goal + "");
    }
}
