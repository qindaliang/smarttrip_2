package com.qin.callback;

import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

/**
 * Created by Administrator on 2018/3/31 0031.
 */

public class BaseCallback<T> implements Callback<T> {
    @Override
    public void onStart(Request<T, ? extends Request> request) {

    }

    @Override
    public void onSuccess(Response<T> response) {

    }

    @Override
    public void onCacheSuccess(Response<T> response) {

    }

    @Override
    public void onError(Response<T> response) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void uploadProgress(Progress progress) {

    }

    @Override
    public void downloadProgress(Progress progress) {

    }

    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        return null;
    }
}
