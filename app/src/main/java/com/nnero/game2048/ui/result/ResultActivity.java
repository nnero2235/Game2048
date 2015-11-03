package com.nnero.game2048.ui.result;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.nnero.game2048.R;
import com.nnero.game2048.base.App;
import com.nnero.game2048.base.BaseActivity;
import com.nnero.game2048.util.SPKeys;
import com.nnero.game2048.viewmodel.GameViewModel;

/**
 * Author:NNERO
 * <p/>
 * TIME: 15/10/19 下午8:57
 * <p/>
 * DESCRIPTION:结果页面，达成目标 或者失败
 */
public class ResultActivity extends BaseActivity implements View.OnClickListener {

    public static final String ACTION_SUCCESS ="action.success";
    public static final String ACTION_FAILURE ="action.failure";

    private TextView mRestartView;
    private TextView mExitView;
    private ImageView mIv;
    private String action;

    @Override
    protected void initView() {
        mRestartView = (TextView) findViewById(R.id.result_restart_tv);
        mExitView = (TextView) findViewById(R.id.result_exit_tv);
        mIv = (ImageView) findViewById(R.id.result_iv);

        mRestartView.setOnClickListener(this);
        mExitView.setOnClickListener(this);

        if(ACTION_SUCCESS.equals(action)){
            mIv.setImageResource(R.drawable.result_success);
        } else {
            mIv.setImageResource(R.drawable.result_gameover);
        }

        GameViewModel.INSTANCE.getRecordPoint().subscribe(recordPoint->{
            int oldRecord = App.getSharedPreferences().getInt(SPKeys.RECORD,0);
            if(recordPoint > oldRecord){
                App.getSharedPreferences().edit().putInt(SPKeys.RECORD,recordPoint).apply();
            }
        },e->{});
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        action = intent.getAction();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        case R.id.result_restart_tv:
            finish();
            setResult(100);
            break;
        case R.id.result_exit_tv:
            finish();
            break;
        }
    }
}
