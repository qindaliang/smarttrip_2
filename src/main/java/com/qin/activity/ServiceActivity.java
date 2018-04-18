package com.qin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.qin.R;
import com.qin.constant.ConstantValues;

/**
 * Created by Administrator on 2018/4/8 0008.
 */

public class ServiceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
    }

    public static void startActivityWithParams(Context context, Intent intent, int number, double lat, double lon) {
        intent.putExtra(ConstantValues.NEARBY_NUMBER, number);
        intent.putExtra(ConstantValues.NEARBY_LAT, lat);
        intent.putExtra(ConstantValues.NEARBY_LON, lon);
        intent.setClass(context, ServiceActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
