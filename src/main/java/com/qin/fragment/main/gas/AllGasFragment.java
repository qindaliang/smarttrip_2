package com.qin.fragment.main.gas;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviTheme;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qin.R;
import com.qin.activity.GasStationActivity;
import com.qin.activity.ParkingSeatActivity;
import com.qin.adapter.recyclerview.gas.AllGasRecycleAdapter;
import com.qin.constant.ConstantValues;
import com.qin.fragment.BaseFragment;
import com.qin.map.activity.baidu.BaiduMainToMapPoiActivity;
import com.qin.pojo.gas.AllGasStation;
import com.qin.pojo.gas.Data;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.weavey.loading.lib.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * Created by Administrator on 2018/3/30 0012.
 */

public class AllGasFragment extends BaseFragment implements OnRefreshLoadMoreListener, INaviInfoCallback {

    private static final String URL_CITY = "http://apis.haoservice.com/oil/region?key=0c3bcf8c01b04c3bbffceb48dd5859e9&city=";
    private static final String URL_LON = "http://apis.haoservice.com/oil/local?key=0c3bcf8c01b04c3bbffceb48dd5859e9&lon=";
    private static final String URL_PAGE = "&page=";
    private static final String URL_PAY = "&paybyvas=false";
    private static final String URL_LAT = "&lat=";
    private static final String URL_RADIUS = "&r=";
    private static final String TAG = "AllGasFragment";

    @BindView(R.id.ll_gas_around)
    LinearLayout llGasAround;
    @BindView(R.id.ll_gas_distance)
    LinearLayout llGasDistance;
    @BindView(R.id.ll_gas_price)
    LinearLayout llGasPrice;
    @BindView(R.id.ll_gas_comment)
    LinearLayout llGasComment;
    @BindView(R.id.ll_gas_sort)
    LinearLayout llGasSort;
    @BindView(R.id.appbar_gas)
    AppBarLayout appbarGas;
    @BindView(R.id.recyclerView_gas_all)
    RecyclerView recyclerViewGasAll;
    @BindView(R.id.refresh_gas)
    SmartRefreshLayout refreshGas;

    private String city;
    private int page = 1;
    private String mUrl;
    Unbinder unbinder;
    private View mView;
    private List<Data> mDatas = new ArrayList<>();
    private double mLat;
    private double mLon;
    private AllGasRecycleAdapter mAdapter;
    private Dialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        mView = View.inflate(mActivity, R.layout.fragment_gas_all, null);
        unbinder = ButterKnife.bind(this, mView);
        initDialog(mActivity);
        return mView;
    }

    @Override
    protected void initData() {
        //正常加载显示的界面
        Log.i("viewpager", this.getClass().getName());
        mLat = mActivity.getIntent().getExtras().getDouble(ConstantValues.NEARBY_LAT);
        mLon = mActivity.getIntent().getExtras().getDouble(ConstantValues.NEARBY_LON);
        Log.i("lat",mLat+"--------"+mLon);

        //下拉刷新，上拉加载更多
        refreshGas.setOnRefreshLoadMoreListener(this);
        refreshGas.setPrimaryColorsId(R.color.colorGreen, android.R.color.white);
        accessNet(1, 3000);

    }
    private void initDialog(Context context) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getWindowWidth(mActivity), ScreenUtils.getWindowHeight(mActivity));
        params.width = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.5f);
        params.height = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.5f);
        mDialog = new Dialog(context);
        View view = View.inflate(context, R.layout.dialog_loading, null);
        mDialog.setContentView(view);
        mDialog.setContentView(view, params);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(true);
    }
    public void accessNet(int page, int radius) {
        if (mLat != 0 && mLon != 0) {
            mUrl = URL_LON + mLon + URL_LAT + mLat + URL_RADIUS + radius + URL_PAY;
            OkGo.<String>post(mUrl).tag(this).execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    Log.i("lat", response.body()+"body"+mLat+"--------"+mLon);
                    parseData(response.body());
                }

                @Override
                public void onError(Response<String> response) {
                    GasStationActivity.loadinglayout.setStatus(LoadingLayout.Error);
                }
            });
        }
    }

    private void parseData(String json) {
        Gson gson = new Gson();
        AllGasStation allGasStation = gson.fromJson(json, AllGasStation.class);
        List<Data> data = allGasStation.getResult().getData();
        Log.i(TAG, allGasStation.toString());
        mDatas.addAll(data);
        initDatas();
    }

    private void accessNetMax(int page, int radius) {
        mDialog.show();
        if (mLat != 0 && mLon != 0) {
            mUrl = URL_LON + mLon + URL_LAT + mLat + URL_PAGE + page + URL_RADIUS + radius + URL_PAY;
            OkGo.<String>post(mUrl).tag(this).execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    Log.i(TAG, response.body());
                    String json = response.body();
                    Gson gson = new Gson();
                    AllGasStation allGasStation = gson.fromJson(json, AllGasStation.class);
                    List<Data> data = allGasStation.getResult().getData();
                    Log.i(TAG, allGasStation.toString());
                    mAdapter.refresh(data);
                    mDialog.dismiss();
                }
            });
        }
    }

    private void accessNetLoadMore(int page, int radius) {
        if (mLat != 0 && mLon != 0) {
            mUrl = URL_LON + mLon + URL_LAT + mLat + URL_PAGE + page + URL_RADIUS + radius + URL_PAY;
            OkGo.<String>post(mUrl).tag(this).execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    Log.i(TAG, response.body());
                    String json = response.body();
                    Gson gson = new Gson();
                    AllGasStation allGasStation = gson.fromJson(json, AllGasStation.class);
                    List<Data> data = allGasStation.getResult().getData();
                    Log.i(TAG, allGasStation.toString());
                    mAdapter.loadMore(data);
                }
            });
        }
    }

    private void initDatas() {
        recyclerViewGasAll.setItemAnimator(new DefaultItemAnimator());
        recyclerViewGasAll.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewGasAll.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
        Log.i(TAG, "走到这" + mDatas.size());
        if (mDatas.size() > 0) {
            mAdapter = new AllGasRecycleAdapter(mDatas) {
                @Override
                protected void onBbindViewHolder(ViewHolder itemView, int position) {
                    String name = mDatas.get(position).getName();
                    Log.i(TAG, name + "CityAllGasRecycleAdapter");
                    itemView.tv_gas_name.setText(name);
                    itemView.tv_gas_brandname.setText(mDatas.get(position).getBrandname());
                    itemView.tv_gas_discount.setText(mDatas.get(position).getDiscount());
                    itemView.tv_gas_exhaust.setText(mDatas.get(position).getExhaust());
                    itemView.tv_gas_location.setText(mDatas.get(position).getAddress() + "距离您大约" + mDatas.get(position).getDistance() + "米");
                    itemView.tv_gas_e0.setText(mDatas.get(position).getPrice().getE0());
                    itemView.tv_gas_e90.setText(mDatas.get(position).getPrice().getE90());
                    itemView.tv_gas_e93.setText(mDatas.get(position).getPrice().getE93());
                    itemView.tv_gas_e97.setText(mDatas.get(position).getPrice().getE97());
                }
            };
            recyclerViewGasAll.setAdapter(mAdapter);
            mDialog.dismiss();
            mAdapter.setOnItemClickListener(new AllGasRecycleAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, AllGasRecycleAdapter.ViewName viewName, int postion) {
                    if (AllGasRecycleAdapter.ViewName.LL_GAS_COLLECTION == viewName) {
                        ToastUtils.showBgResource(mActivity, "收藏");
                    } else if (AllGasRecycleAdapter.ViewName.ITEM == viewName) {
//                        ToastUtils.showBgResource(mActivity, "ITEM" + postion + "导航");
//                        Intent intent = new Intent();
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.putExtra("lat",mLat);
//                        intent.putExtra("lon",mLon);
//                        intent.putExtra("name",mDatas.get(postion).getName());
//                        intent.setClass(mActivity, BaiduMainToMapPoiActivity.class);
//                        startActivity(intent);
                        Log.i("allfragment",mDatas.get(postion).getLat()+"============"+mDatas.get(postion).getLon());
                        //直接启动导航
                        //TODO 金纬度是字符串，带转换
                        AmapNaviPage.getInstance().showRouteActivity(mActivity
                                ,new AmapNaviParams(new Poi(null, null, "")
                                        , null
                                        , new Poi(mDatas.get(postion).getName(), new LatLng(mDatas.get(postion).getLat(),mDatas.get(postion).getLon()), "")
                                        , AmapNaviType.DRIVER).setTheme(AmapNaviTheme.WHITE)
                                , AllGasFragment.this);
                    } else if (AllGasRecycleAdapter.ViewName.LL_GAS_SHARE == viewName) {
                        ToastUtils.showBgResource(mActivity, "分享");
                    } else if (AllGasRecycleAdapter.ViewName.LL_GAS_TIP == viewName) {
                        ToastUtils.showBgResource(mActivity, "提示");
                    }
                }
            });
        }
    }

    @Override
    public View initView() {
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_gas_around, R.id.ll_gas_distance, R.id.ll_gas_price, R.id.ll_gas_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_gas_around:
                //TODO 刷新数据有问题
                accessNetMax(1, 10000);
                recyclerViewGasAll.setAdapter(mAdapter);
                break;
            case R.id.ll_gas_distance:
                break;
            case R.id.ll_gas_price:
                break;
            case R.id.ll_gas_comment:
                break;
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        accessNetLoadMore(page, 10000);
        page++;
        refreshLayout.finishLoadMore(1000);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        accessNetLoadMore(1, 10000);
        refreshLayout.finishRefresh(1000);
    }

    public ReloadGasListener mReloadGasListener;

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onStopSpeaking() {

    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }

    public interface ReloadGasListener{
        public void reLoadGas();
    }

    public void setReloadGasListener( ReloadGasListener reloadGasListener){
        this.mReloadGasListener = reloadGasListener;
    }
}
