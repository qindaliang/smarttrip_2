package com.qin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;

import com.qin.R;
import com.qin.constant.ConstantValues;
import com.qin.fragment.setting.FootPrintFragment;
import com.qin.fragment.setting.ProducerFragment;
import com.qin.fragment.setting.RepairHistoryFragment;
import com.qin.fragment.setting.UpdateInfoFragment;
import com.qin.fragment.setting.UseGuideFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class SettingDetailActivity extends AppCompatActivity {
    @BindView(R.id.fl_settingdetail)
    FrameLayout flSettingdetail;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private int number;
    private FragmentTransaction mBeginTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingdetail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        number = getIntent().getExtras().getInt(ConstantValues.SETTING_NUMBER);
        if (number == 1) {
            showFragment(new UpdateInfoFragment(), ConstantValues.UPDATEINFO);
            return;
        }
        if (number == 2) {
            showFragment(new FootPrintFragment(), ConstantValues.FOOTPRINT);
            return;
        }
        if (number == 3) {
            showFragment(new RepairHistoryFragment(), ConstantValues.REPAIRHISTORY);
            return;
        }
        if (number == 4) {
            showFragment(new UseGuideFragment(), ConstantValues.USEGUIDE);
            return;
        }
        if (number == 5) {
            showFragment(new ProducerFragment(), ConstantValues.PRODUCER);
            return;
        }
    }

    public static void startActivity(Context context, Intent intent, int number) {
        intent.putExtra(ConstantValues.SETTING_NUMBER, number);
        intent.setClass(context, SettingDetailActivity.class);
        context.startActivity(intent);
    }

    public void showFragment(Fragment fragment, String fragmentName) {
        Bundle bundle = new Bundle();
        bundle.putString(ConstantValues.SETTING_NUMBER, fragmentName);
        fragment.setArguments(bundle);
        mBeginTransaction = getSupportFragmentManager().beginTransaction();
        mBeginTransaction.replace(R.id.fl_settingdetail, fragment);
        mBeginTransaction.commit();
        Log.i("", "================" + "你点击了" + fragmentName);
        getSupportActionBar().setTitle(fragmentName);
    }
}
