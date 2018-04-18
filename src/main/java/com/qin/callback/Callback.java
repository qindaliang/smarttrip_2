package com.qin.callback;

import com.lzy.okgo.model.Response;

/**
 * Created by Administrator on 2018/3/31 0031.
 */

public abstract class Callback<T> extends BaseCallback<T> {
    @Override
    public void onSuccess(Response<T> response) {
        super.onSuccess(response);
        success(response);
    }

    protected abstract void success(Response<T> response);

    @Override
    public void onError(Response<T> response) {
        super.onError(response);
        error(response);
    }

    protected abstract void error(Response<T> response);
}
