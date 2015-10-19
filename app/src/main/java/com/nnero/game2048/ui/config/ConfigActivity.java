package com.nnero.game2048.ui.config;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import com.nnero.game2048.R;
import com.nnero.game2048.base.App;
import com.nnero.game2048.base.BaseActivity;
import com.nnero.game2048.util.LogUtil;
import com.nnero.game2048.util.SPKeys;

/**
 * Author:NNERO
 * <p/>
 * TIME: 15/10/16 下午9:27
 * <p/>
 * DESCRIPTION:
 */
public class ConfigActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private Spinner mLevelSpinner;
    private Spinner mGoalSpinner;

    private SharedPreferences mSp;

    @Override
    protected void initView() {
        mLevelSpinner = (Spinner) findViewById(R.id.config_levels_sp);
        mGoalSpinner = (Spinner) findViewById(R.id.config_goal_sp);

        mLevelSpinner.setOnItemSelectedListener(this);
        mGoalSpinner.setOnItemSelectedListener(this);

        mLevelSpinner.setSelection(mSp.getInt(SPKeys.LEVEL_POS,0));
        mGoalSpinner.setSelection(mSp.getInt(SPKeys.GOAL_POS,1));
    }

    @Override
    protected void initData() {
        mSp = App.getSharedPreferences();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_config;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSp.edit().putInt(SPKeys.GOAL_POS,mGoalSpinner.getSelectedItemPosition())
                 .putInt(SPKeys.LEVEL_POS,mLevelSpinner.getSelectedItemPosition())
                 .apply();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        App.goal = App.GOALS[mGoalSpinner.getSelectedItemPosition()];
        App.level = App.LEVELS[mLevelSpinner.getSelectedItemPosition()];
        setResult(200);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
