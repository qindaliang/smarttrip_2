package com.qin.fragment.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qin.R;
import com.qin.fragment.BaseFragment;

/**
 * Created by Administrator on 2018/3/14 0014.
 */

public class UpdateInfoFragment extends BaseFragment {

    @Override
    protected void initData() {

    }

    @Override
    public View initView() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_updateinfo, null, false);
        return view;
    }
}
