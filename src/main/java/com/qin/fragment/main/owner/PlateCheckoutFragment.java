package com.qin.fragment.main.owner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qin.R;
import com.qin.fragment.BaseFragment;

/**
 * Created by Administrator on 2018/4/7 0007.
 */

public class PlateCheckoutFragment extends BaseFragment {

    @Override
    public View initView() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_platecheckout,null,false);
        return view;
    }

    @Override
    protected void initData() {

    }

}
