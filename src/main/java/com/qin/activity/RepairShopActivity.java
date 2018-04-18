package com.qin.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviTheme;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiAddrInfo;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.qin.R;
import com.qin.adapter.recyclerview.parking.ParkingRecyclerViewAdapter;
import com.qin.constant.ConstantValues;
import com.qin.pojo.parking.Parking;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * Created by Administrator on 2018/4/8 0008.
 */

public class RepairShopActivity extends AppCompatActivity implements OnGetPoiSearchResultListener, View.OnClickListener, OnRefreshLoadMoreListener, INaviInfoCallback {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_search_nearbypoi_about)
    ImageView ivSearchNearbypoiAbout;
    @BindView(R.id.ll_parking_about)
    LinearLayout llParkingAbout;
    @BindView(R.id.tv_parking_synthesize)
    TextView tvParkingSynthesize;
    @BindView(R.id.tv_parking_distance)
    TextView tvParkingDistance;
    @BindView(R.id.rlv_parking)
    RecyclerView rlvParking;
    @BindView(R.id.refresh_repairshop)
    SmartRefreshLayout refreshParking;
    @BindView(R.id.tv_repairshop_no)
    TextView tvWashcarNo;
    private View popView;
    private int popIsShowing = 1;

    private DisplayMetrics mDisplayMetrics;
    private int mWidthPixels;
    private int mHeightPixels;

    public PopupWindow popupWindow;
    private RelativeLayout mRl_pop_bound1;
    private RelativeLayout mRl_pop_bound2;
    private RelativeLayout mRl_pop_bound3;
    private RelativeLayout mRl_pop_bound4;
    private RelativeLayout mRl_pop_bound5;
    private ImageView ivPop1;
    private ImageView ivPop2;
    private ImageView ivPop3;
    private ImageView ivPop4;
    private ImageView ivPop5;
    private PoiSearch mPoiSearch;
    private ParkingRecyclerViewAdapter mAdapter;
    private List<Parking> mList = new ArrayList<>();
    private List<PoiInfo> mPoiInfoList;
    private List<PoiAddrInfo> mPoiAddrInfoList;
    private double mLatitude;
    private double mLongitude;
    private String mUid;
    private int mTotalPageNum;
    private boolean isLoadMore = false;
    int firstPage = 1;
    private Dialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairshop);
        ButterKnife.bind(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initDialog(this);

        mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        mWidthPixels = mDisplayMetrics.widthPixels;
        mHeightPixels = mDisplayMetrics.heightPixels;

        mLatitude = getIntent().getExtras().getDouble(ConstantValues.NEARBY_LAT, 119.203488);
        mLongitude = getIntent().getExtras().getDouble(ConstantValues.NEARBY_LON, 26.062197);
        Log.i("parking", mLatitude + "---" + mLongitude);
        tvParkingSynthesize.setTextColor(getResources().getColor(R.color.red));
        refreshParking.setOnRefreshLoadMoreListener(this);
        refreshParking.setPrimaryColorsId(R.color.colorGreen, android.R.color.white);

        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        initPopWindow(this, R.layout.popwindow_search_bound, mWidthPixels - 60, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvParking.setItemAnimator(new DefaultItemAnimator());
        rlvParking.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        rlvParking.setLayoutManager(layoutManager);

        setPoiNearbySearchOption(PoiSortType.comprehensive, 1, 10000);
        mAdapter = new ParkingRecyclerViewAdapter(mList);
        rlvParking.setAdapter(mAdapter);
    }

    @OnClick({R.id.ll_parking_about, R.id.tv_parking_synthesize, R.id.tv_parking_distance})
    public void onViewClicked(View view) {
        isLoadMore = false;
        switch (view.getId()) {
            case R.id.ll_parking_about:
                ToastUtils.showBgResource(this, "fanwei");
                ivSearchNearbypoiAbout.setImageResource(R.mipmap.location);
                if (popIsShowing == 2) {
                    popupWindow.dismiss();
                    popIsShowing = 1;
                } else {
                    popupWindow.showAsDropDown(llParkingAbout, -24, 20);
                    setWindowGray(0.7f);
                }
                break;
            case R.id.tv_parking_synthesize:
                setPoiNearbySearchOption(PoiSortType.comprehensive, 1, 10000);
                rlvParking.setAdapter(mAdapter);
                tvParkingDistance.setTextColor(Color.GRAY);
                tvParkingSynthesize.setTextColor(getResources().getColor(R.color.red));
                break;
            case R.id.tv_parking_distance:
                setPoiNearbySearchOption(PoiSortType.distance_from_near_to_far, 1, 10000);
                rlvParking.setAdapter(mAdapter);
                tvParkingDistance.setTextColor(getResources().getColor(R.color.red));
                tvParkingSynthesize.setTextColor(Color.GRAY);
                break;
        }
    }

    private void initDialog(Context context) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getWindowWidth(this), ScreenUtils.getWindowHeight(this));
        params.width = (int) (getWindowManager().getDefaultDisplay().getWidth() * 0.4f);
        params.height = (int) (getWindowManager().getDefaultDisplay().getWidth() * 0.4f);
        mDialog = new Dialog(context);
        View view = View.inflate(context, R.layout.dialog_loading, null);
        mDialog.setContentView(view);
        mDialog.setContentView(view, params);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(true);
    }

    public void setPoiNearbySearchOption(PoiSortType type, int page, int radius) {
        mDialog.show();
        PoiNearbySearchOption searchOption = new PoiNearbySearchOption()
                .keyword("汽车维修店")
                .sortType(type)
                .pageCapacity(10)
                .pageNum(page)
                .radius(radius)
                .location(new LatLng(mLatitude, mLongitude));
        mPoiSearch.searchNearby(searchOption);
    }

    public void setPoiNearbySearchOptionLoadMore(PoiSortType type, int page, int radius) {
        PoiNearbySearchOption searchOption = new PoiNearbySearchOption()
                .keyword("汽车维修店")
                .sortType(type)
                .pageCapacity(10)
                .pageNum(page)
                .radius(radius)
                .location(new LatLng(mLatitude, mLongitude));
        mPoiSearch.searchNearby(searchOption);
    }

    public static void startActivityWithParams(Context context, Intent intent, int number, double lat, double lon) {
        intent.putExtra(ConstantValues.NEARBY_NUMBER, number);
        intent.putExtra(ConstantValues.NEARBY_LAT, lat);
        intent.putExtra(ConstantValues.NEARBY_LON, lon);
        intent.setClass(context, RepairShopActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void initListener() {
        mRl_pop_bound1 = popView.findViewById(R.id.rl_pop_bound1);
        mRl_pop_bound2 = popView.findViewById(R.id.rl_pop_bound2);
        mRl_pop_bound3 = popView.findViewById(R.id.rl_pop_bound3);
        mRl_pop_bound4 = popView.findViewById(R.id.rl_pop_bound4);
        mRl_pop_bound5 = popView.findViewById(R.id.rl_pop_bound5);
        ivPop1 = popView.findViewById(R.id.iv_pop1);
        ivPop2 = popView.findViewById(R.id.iv_pop2);
        ivPop3 = popView.findViewById(R.id.iv_pop3);
        ivPop4 = popView.findViewById(R.id.iv_pop4);
        ivPop5 = popView.findViewById(R.id.iv_pop5);
        mRl_pop_bound1.setOnClickListener(this);
        mRl_pop_bound2.setOnClickListener(this);
        mRl_pop_bound3.setOnClickListener(this);
        mRl_pop_bound4.setOnClickListener(this);
        mRl_pop_bound5.setOnClickListener(this);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowGray(1.0f);
            }
        });
    }

    public void initPopWindow(Context context, int layoutRes, int width, int height) {
        popView = View.inflate(this, R.layout.popwindow_search_bound, null);
        popupWindow = new PopupWindow(popView, width, height, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(false);

        popIsShowing = 1;
        popView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                }
                return false;
            }
        });
        popupWindow.setAnimationStyle(R.style.popSearchAnimtion);

        initListener();
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            ToastUtils.showBgResource(this, "未找到结果");
            mDialog.dismiss();
            tvWashcarNo.setVisibility(View.VISIBLE);
            refreshParking.setVisibility(View.GONE);
            return;
        }
        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
            tvWashcarNo.setVisibility(View.GONE);
            refreshParking.setVisibility(View.VISIBLE);
            mPoiInfoList = poiResult.getAllPoi();
            if (!isLoadMore) {
                mList.clear();
            }
            mTotalPageNum = poiResult.getTotalPageNum();
            StringBuilder sb = new StringBuilder(256);
            for (PoiInfo poiInfo : mPoiInfoList) {
                String address = poiInfo.address;
                String name = poiInfo.name;
                double longitude = poiInfo.location.longitude;
                double latitude = poiInfo.location.latitude;
                String phoneNum = poiInfo.phoneNum;
                mUid = poiInfo.uid;
                boolean hasCaterDetails = poiInfo.hasCaterDetails;
                boolean isPano = poiInfo.isPano;
                int i = poiInfo.describeContents();
//                mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
//                        .poiUid(poiInfo.uid));

                sb.append("\naddress");
                sb.append(address);
                sb.append("\nname");
                sb.append(name);
                sb.append("\nphoneNum");
                sb.append(phoneNum);
                sb.append("\nuid");
                sb.append(mUid);
                sb.append("\nhasCaterDetails");
                sb.append(hasCaterDetails);
                sb.append("\nisPano");
                sb.append(isPano);
                sb.append("\ndescribeContents");
                sb.append(i);
                sb.append("\ndescribeContents=======");
                sb.append(poiResult.isHasAddrInfo());
                if (name != "" && name != null || address != null) {
                    Parking park = new Parking();
                    park.setName(name);
                    park.setAddress(address);
                    park.setLat(latitude);
                    park.setLon(longitude);
                    mList.add(park);
                }
            }
            mAdapter.notifyDataSetChanged();
            refreshParking.finishLoadMore();
            mDialog.dismiss();
            mPoiAddrInfoList = poiResult.getAllAddr();
            if (poiResult.isHasAddrInfo()) {
                for (PoiAddrInfo addrInfo : mPoiAddrInfoList) {
                    String name = addrInfo.name;
                    String address = addrInfo.address;
                    LatLng location = addrInfo.location;
                    sb.append("==============================");
                    sb.append("\nname");
                    sb.append(name);
                    sb.append("\naddress");
                    sb.append(address);
                    sb.append("\nlocation");
                    sb.append(location);
                }
            }
            Log.i("poiResult", "=====附近parking======" + sb.toString());
            mAdapter.setOnItemClickListener(new ParkingRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, ParkingRecyclerViewAdapter.ViewName viewName, int postion) {
                    if (ParkingRecyclerViewAdapter.ViewName.ITEM == viewName) {
                        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext()
                                , new AmapNaviParams(new Poi(null, null, "")
                                        , null
                                        , new Poi(mList.get(postion).getName(), new com.amap.api.maps.model.LatLng(mList.get(postion).getLat(), mList.get(postion).getLon()), "")
                                        , AmapNaviType.DRIVER).setTheme(AmapNaviTheme.WHITE)
                                , RepairShopActivity.this);
                    }
                    if (ParkingRecyclerViewAdapter.ViewName.IMAGEVIEW == viewName) {
                        ToastUtils.showBgResource(RepairShopActivity.this, "跳转" + postion);
                    }
                }
            });
        }
        if (poiResult.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : poiResult.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";

        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            ToastUtils.showBgResource(RepairShopActivity.this, "抱歉，未找到结果");
        } else {
            StringBuilder sb = new StringBuilder(256);
            sb.append("\ngetAddress-------");
            sb.append(result.getAddress());
            sb.append("\ndescribeContents-------");
            sb.append(result.describeContents());
            sb.append("\ngetCheckinNum-------");
            sb.append(result.getCheckinNum());
            sb.append("\ngetCommentNum-------");
            sb.append(result.getCommentNum());
            sb.append("\ngetDetailUrl-------");
            sb.append(result.getDetailUrl());
            sb.append("\ngetEnvironmentRating-------");
            sb.append(result.getEnvironmentRating());
            sb.append("\ngetFacilityRating-------");
            sb.append(result.getFacilityRating());
            sb.append("\ngetFavoriteNum-------");
            sb.append(result.getFavoriteNum());
            sb.append("\ngetGrouponNum-------");
            sb.append(result.getGrouponNum());
            sb.append("\ngetHygieneRating-------");
            sb.append(result.getHygieneRating());
            sb.append("\ngetImageNum-------");
            sb.append(result.getImageNum());
            sb.append("\ngetLocation-------");
            sb.append(result.getLocation());
            sb.append("\ngetName-------");
            sb.append(result.getName());
            sb.append("\ngetOverallRating-------");
            sb.append(result.getOverallRating());
            sb.append("\ngetPrice-------");
            sb.append(result.getPrice());
            sb.append("\ngetServiceRating-------");
            sb.append(result.getServiceRating());
            sb.append("\ngetShopHours-------");
            sb.append(result.getShopHours());
            sb.append("\ngetTag-------");
            sb.append(result.getTag());
            sb.append("\ngetTasteRating-------");
            sb.append(result.getTasteRating());
            sb.append("\ngetTechnologyRating-------");
            sb.append(result.getTechnologyRating());
            sb.append("\ngetTasteRating-------");
            sb.append(result.getTasteRating());
            sb.append("\ngetTelephone-------");
            sb.append(result.getTelephone());
            sb.append("\ngetType-------");
            sb.append(result.getType());
            sb.append("\ngetUid-------");
            sb.append(result.getUid());
            Log.i("parking", sb.toString());

            //TODO  geipoi详情控件设置值
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    public void setWindowGray(float f) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = f;
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(params);
    }

    @Override
    public void onClick(View v) {
        isLoadMore = false;
        switch (v.getId()) {
            case R.id.rl_pop_bound1:
                setPoiNearbySearchOption(PoiSortType.comprehensive, 1, 500);
                rlvParking.setAdapter(mAdapter);
                ivPop1.setVisibility(View.VISIBLE);
                ivPop2.setVisibility(View.GONE);
                ivPop3.setVisibility(View.GONE);
                ivPop4.setVisibility(View.GONE);
                ivPop5.setVisibility(View.GONE);
                popupWindow.dismiss();

                break;
            case R.id.rl_pop_bound2:
                setPoiNearbySearchOption(PoiSortType.comprehensive, 1, 1000);
                rlvParking.setAdapter(mAdapter);
                ivPop1.setVisibility(View.GONE);
                ivPop2.setVisibility(View.VISIBLE);
                ivPop3.setVisibility(View.GONE);
                ivPop4.setVisibility(View.GONE);
                ivPop5.setVisibility(View.GONE);
                popupWindow.dismiss();
                break;
            case R.id.rl_pop_bound3:
                setPoiNearbySearchOption(PoiSortType.comprehensive, 1, 2000);
                rlvParking.setAdapter(mAdapter);
                ivPop1.setVisibility(View.GONE);
                ivPop2.setVisibility(View.GONE);
                ivPop3.setVisibility(View.VISIBLE);
                ivPop4.setVisibility(View.GONE);
                popupWindow.dismiss();
                ivPop5.setVisibility(View.GONE);
                break;
            case R.id.rl_pop_bound4:
                setPoiNearbySearchOption(PoiSortType.comprehensive, 1, 5000);
                rlvParking.setAdapter(mAdapter);
                ivPop1.setVisibility(View.GONE);
                ivPop2.setVisibility(View.GONE);
                popupWindow.dismiss();
                ivPop3.setVisibility(View.GONE);
                ivPop4.setVisibility(View.VISIBLE);
                ivPop5.setVisibility(View.GONE);
                break;
            case R.id.rl_pop_bound5:
                setPoiNearbySearchOption(PoiSortType.comprehensive, 1, 10000);
                rlvParking.setAdapter(mAdapter);
                ivPop1.setVisibility(View.GONE);
                ivPop2.setVisibility(View.GONE);
                popupWindow.dismiss();
                ivPop3.setVisibility(View.GONE);
                ivPop4.setVisibility(View.GONE);
                ivPop5.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        isLoadMore = true;
        if (firstPage < mTotalPageNum) {
            setPoiNearbySearchOptionLoadMore(PoiSortType.comprehensive, firstPage, 10000);
            firstPage++;
        } else {
            ToastUtils.showBgResource(this, "已加载所有数据");
            refreshParking.finishLoadMore();
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        setPoiNearbySearchOptionLoadMore(PoiSortType.comprehensive, 1, 10000);
        refreshParking.finishRefresh();
    }

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
}
