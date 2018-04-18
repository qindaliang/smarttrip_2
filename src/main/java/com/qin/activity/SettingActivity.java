package com.qin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.qin.R;
import com.qin.constant.ConstantValues;
import com.qin.fragment.setting.FootPrintFragment;
import com.qin.fragment.setting.ProducerFragment;
import com.qin.fragment.setting.RepairHistoryFragment;
import com.qin.fragment.setting.UpdateInfoFragment;
import com.qin.fragment.setting.UseGuideFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/30 0014.
 */

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_me)
    Toolbar toolbarMe;
    @BindView(R.id.ll_me_changeinfo)
    LinearLayout llMeChangeinfo;
    @BindView(R.id.ll_setting_foothistory)
    LinearLayout llSettingFoothistory;
    @BindView(R.id.ll_setting_repairhistory)
    LinearLayout llSettingRepairhistory;
    @BindView(R.id.ll_setting_help)
    LinearLayout llSettingHelp;
    @BindView(R.id.ll_setting_update)
    LinearLayout llSettingUpdate;
    @BindView(R.id.ll_setting_versin)
    LinearLayout llSettingVersin;
    @BindView(R.id.ll_setting_producer)
    LinearLayout llSettingProducer;
    private FragmentTransaction mBeginTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        mBeginTransaction = getSupportFragmentManager().beginTransaction();
    }

    public static void startAction(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.ll_me_changeinfo, R.id.ll_setting_foothistory, R.id.ll_setting_repairhistory, R.id.ll_setting_help, R.id.ll_setting_update, R.id.ll_setting_versin, R.id.ll_setting_producer})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.ll_me_changeinfo:
               SettingDetailActivity.startActivity(SettingActivity.this,intent,1);
                break;
            case R.id.ll_setting_foothistory:
                SettingDetailActivity.startActivity(SettingActivity.this,intent,2);
                break;
            case R.id.ll_setting_repairhistory:
                SettingDetailActivity.startActivity(SettingActivity.this,intent,3);
                break;
            case R.id.ll_setting_help:
                SettingDetailActivity.startActivity(SettingActivity.this,intent,4);
                break;
            case R.id.ll_setting_update:
                break;
            case R.id.ll_setting_versin:
                break;
            case R.id.ll_setting_producer:
                SettingDetailActivity.startActivity(SettingActivity.this,intent,5);
                break;
        }
    }

}

