package com.qin.map.activity.baidu;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviTheme;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiAddrInfo;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.qin.R;
import com.qin.constant.ConstantValues;
import com.qin.map.baiduoverlay.PoiOverlay;
import com.qin.map.util.AmapTTSController;
import com.qin.util.ToastUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class BaiduMainToMapPoiActivity extends Activity implements SensorEventListener, OnGetPoiSearchResultListener, INaviInfoCallback {

    // 定位相关
    LocationClient mLocClient;

    @BindView(R.id.mapview)
    MapView mMapView;
    @BindView(R.id.tv_baidumap_searchpoi)
    TextView tvBaidumapSearchpoi;
    @BindView(R.id.et_baidumap_searchpoi)
    EditText etBaidumapSearchpoi;
    @BindView(R.id.search_bar_layout)
    RelativeLayout searchBarLayout;
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.iv_poi_photo)
    ImageView ivPoiPhoto;
    @BindView(R.id.tv_poi_name)
    TextView tvPoiName;
    @BindView(R.id.tv_poi_addressandis)
    TextView tvPoiAddressandis;
    @BindView(R.id.tv_poi_shophours)
    TextView tvPoiShophours;
    @BindView(R.id.tv_poi_price)
    TextView tvPoiPrice;
    @BindView(R.id.rat_poi_overallrating)
    MaterialRatingBar ratPoiOverallrating;
    @BindView(R.id.tv_poi_overallrating)
    TextView tvPoiOverallrating;
    @BindView(R.id.ll_poi_panorama)
    LinearLayout llPoiPanorama;
    @BindView(R.id.dragView)
    LinearLayout dragView;
    @BindView(R.id.iv_poi_collection)
    ImageView ivPoiCollection;
    @BindView(R.id.ll_poi_collection)
    LinearLayout llPoiCollection;
    @BindView(R.id.ll_poi_share)
    LinearLayout llPoiShare;
    @BindView(R.id.ll_poi_tip)
    LinearLayout llPoiTip;
    @BindView(R.id.iv_poi_go)
    ImageView ivPoiGo;
    @BindView(R.id.tv_poi_go)
    TextView tvPoiGo;
    @BindView(R.id.ll_poi_go)
    LinearLayout llPoiGo;
    @BindView(R.id.iv_poi_phone)
    ImageView ivPoiPhone;
    @BindView(R.id.ll_poi_moreinfo)
    LinearLayout llPoiMoreinfo;
    @BindView(R.id.rat_poi_environmentrating)
    MaterialRatingBar ratPoiEnvironmentrating;
    @BindView(R.id.tv_poi_environmentrating)
    TextView tvPoiEnvironmentrating;
    @BindView(R.id.rat_poi_servicerating)
    MaterialRatingBar ratPoiServicerating;
    @BindView(R.id.tv_poi_servicerating)
    TextView tvPoiServicerating;
    @BindView(R.id.rat_poi_hygienerating)
    MaterialRatingBar ratPoiHygienerating;
    @BindView(R.id.tv_poi_hygienerating)
    TextView tvPoiHygienerating;
    @BindView(R.id.rat_poi_facilityrating)
    MaterialRatingBar ratPoiFacilityrating;
    @BindView(R.id.tv_poi_facilityrating)
    TextView tvPoiFacilityrating;
    @BindView(R.id.rat_poi_tasterating)
    MaterialRatingBar ratPoiTasterating;
    @BindView(R.id.tv_poi_tasterating)
    TextView tvPoiTasterating;
    @BindView(R.id.tv_poi_comment)
    TextView tvPoiComment;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingLayout;
    @BindView(R.id.ll_poi_write)
    LinearLayout llPoiWrite;
    @BindView(R.id.tv_poi_distance)
    TextView tvPoiDistance;
    @BindView(R.id.tv_poi_phone)
    TextView tvPoiPhone;
    private LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    BaiduMap mBaiduMap;
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private float direction;
    private PoiSearch mPoiSearch;
    private LatLng mCenter;
    private String mUid;
    private double lat;
    private double lon;
    private AmapTTSController amapTTSController;
    private String startpoi;
    private String endpoi;
    private MyLocationListenner mListener;
    private double mLat;
    private double mLon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidumap_searchmappoi);
        ButterKnife.bind(this);
        amapTTSController = AmapTTSController.getInstance(getApplicationContext());
        amapTTSController.init();
        tvPoiName.setSelected(true);
        tvPoiPrice.setSelected(true);
        tvPoiShophours.setSelected(true);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mCurrentMode = LocationMode.NORMAL;
        button1.setText("普通");
        mLat = getIntent().getExtras().getDouble("lat");
        mLon = getIntent().getExtras().getDouble("lon");
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.mapview);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mListener = new MyLocationListenner();
        mLocClient.registerLocationListener(mListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        if (mLat == 0 || mLon == 0) {
            mLocClient.start();
        }else{
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(mLat,mLon)));
        }
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(BaiduMainToMapPoiActivity.this, "未找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mBaiduMap.clear();
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();
            StringBuilder sb = new StringBuilder(256);
            sb.append("\ngetCurrentPageCapacity-------");
            sb.append(result.getCurrentPageCapacity());
            sb.append("\ngetTotalPageNum-------");
            sb.append(result.getTotalPageNum());
            sb.append("\ngetTotalPoiNum-------");
            sb.append(result.getTotalPoiNum());
            List<PoiAddrInfo> allAddr = result.getAllAddr();
            if (allAddr != null && allAddr.size() > 0) {
                sb.append("\nallAddr.get(0).name-------");
                sb.append(allAddr.get(0).name);
                sb.append("\nallAddr.get(0).address-------");
                sb.append(allAddr.get(0).address);
            }
            sb.append("\n\n\n");
            List<PoiInfo> allPoi = result.getAllPoi();
            if (allPoi != null && allPoi.size() > 0) {
                for (int i = 0; i < allPoi.size(); i++) {
                    PoiInfo poiInfo = allPoi.get(i);
                    sb.append("\naddress-------");
                    sb.append(poiInfo.address);
                    sb.append("\ncity-------");
                    sb.append(poiInfo.city);
                    sb.append("\nhasCaterDetails-------");
                    sb.append(poiInfo.hasCaterDetails);
                    sb.append("\nisPano-------");
                    sb.append(poiInfo.isPano);
                    sb.append("\nname-------");
                    sb.append(poiInfo.name);
                    sb.append("\nphoneNum-------");
                    sb.append(poiInfo.phoneNum);
                    sb.append("\ntype-------");
                    sb.append(poiInfo.type);
                    sb.append("\nuid-------");
                    sb.append(poiInfo.uid);
                }
            }
            sb.append("\n\n\n");
            Log.i("poi", sb.toString());

            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(BaiduMainToMapPoiActivity.this, strInfo, Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            ToastUtils.showBgResource(BaiduMainToMapPoiActivity.this, "抱歉，未找到结果");
        } else {
            ToastUtils.showBgResource(BaiduMainToMapPoiActivity.this, result.getName() + ": " + result.getAddress());
            mUid = result.getUid();

            lat = result.getLocation().latitude;
            lon = result.getLocation().longitude;
            com.amap.api.maps.model.LatLng poilocation = new com.amap.api.maps.model.LatLng(result.getLocation().latitude, result.getLocation().longitude);
            com.amap.api.maps.model.LatLng currentLocation = new com.amap.api.maps.model.LatLng(mCurrentLat, mCurrentLon);
            int distance = (int) AMapUtils.calculateLineDistance(poilocation, currentLocation);
            tvPoiName.setText(result.getName());
            tvPoiAddressandis.setText(result.getAddress());
            tvPoiDistance.setText(String.valueOf(distance) + "米");
            tvPoiComment.setText(String.valueOf(result.getCommentNum()));
            tvPoiOverallrating.setText(result.getOverallRating() + "分");
            ratPoiOverallrating.setProgress((int) result.getOverallRating() * 2);
            tvPoiEnvironmentrating.setText(result.getEnvironmentRating() + "分");
            ratPoiEnvironmentrating.setProgress((int) result.getEnvironmentRating() * 2);
            tvPoiFacilityrating.setText(result.getFacilityRating() + "分");
            ratPoiFacilityrating.setProgress((int) result.getFacilityRating() * 2);
            tvPoiHygienerating.setText(result.getHygieneRating() + "分");
            ratPoiHygienerating.setProgress((int) result.getHygieneRating() * 2);
            tvPoiServicerating.setText(result.getServiceRating() + "分");
            ratPoiServicerating.setProgress((int) result.getServiceRating() * 2);
            tvPoiTasterating.setText(result.getTasteRating() + "分");
            ratPoiTasterating.setProgress((int) result.getTasteRating() * 2);
            double resultPrice = result.getPrice();
            if (resultPrice == 0) {
                tvPoiPrice.setText("暂无");
            } else {
                tvPoiPrice.setText(String.valueOf(resultPrice));
            }
            String shopHours = result.getShopHours();
            if ("".equals(shopHours) || "" == shopHours) {
                tvPoiShophours.setText("暂无");
            } else {
                tvPoiShophours.setText(result.getShopHours() + "");
            }
            String telephone = result.getTelephone();
            if ("".equals(telephone) || "" == telephone) {
                tvPoiPhone.setText("暂无");
            }else{
                tvPoiPhone.setText(telephone);
            }

            endpoi = result.getName();

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
            Log.i("poidetail1", sb.toString());

            //TODO  geipoi详情控件设置值
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    /**
     * 通过高德地图获取照片
     */
    private void getPhoto() {

    }

    @OnClick({R.id.ll_poi_write, R.id.tv_baidumap_searchpoi, R.id.et_baidumap_searchpoi, R.id.button1, R.id.iv_poi_photo, R.id.tv_poi_name, R.id.ll_poi_panorama, R.id.ll_poi_collection, R.id.ll_poi_share, R.id.ll_poi_tip, R.id.ll_poi_go, R.id.iv_poi_phone, R.id.ll_poi_moreinfo})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.tv_baidumap_searchpoi:
                PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption().keyword(etBaidumapSearchpoi.getText().toString())
                        .sortType(PoiSortType.distance_from_near_to_far).location(mCenter)
                        .radius(5000).pageNum(1).pageCapacity(50);
                mPoiSearch.searchNearby(nearbySearchOption);
                break;
            case R.id.et_baidumap_searchpoi:
                intent.putExtra("latitude", mCurrentLat);
                intent.putExtra("longitude", mCurrentLon);
                intent.setClass(BaiduMainToMapPoiActivity.this, BaiduMapSearchPoiActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.button1:
                mLocClient.start();
                switch (mCurrentMode) {
                    case NORMAL:
                        button1.setText("跟随");
                        mCurrentMode = LocationMode.FOLLOWING;
                        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                        MapStatus.Builder builder = new MapStatus.Builder();
                        builder.overlook(-45);
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                        break;
                    case COMPASS:
                        button1.setText("普通");
                        mCurrentMode = LocationMode.NORMAL;
                        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                        MapStatus.Builder builder1 = new MapStatus.Builder();
                        builder1.overlook(-45);
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder1.build()));
                        break;
                    case FOLLOWING:
                        button1.setText("罗盘");
                        mCurrentMode = LocationMode.COMPASS;
                        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                        MapStatus.Builder builder2 = new MapStatus.Builder();
                        builder2.overlook(-45);
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder2.build()));
                        break;
                    default:
                        break;
                }
                break;
            case R.id.iv_poi_photo:
                ToastUtils.showBgResource(BaiduMainToMapPoiActivity.this, "待完成");
                break;
            case R.id.tv_poi_name:
                ToastUtils.showBgResource(BaiduMainToMapPoiActivity.this, "待完成");
                break;
            case R.id.ll_poi_panorama:
                if (lat != 0 && lon != 0) {
                    intent.putExtra("lat", lat);
                    intent.putExtra("lon", lon);
                    Log.i("mUid", mUid);
                    intent.setClass(BaiduMainToMapPoiActivity.this, BaiduMapPanoramaActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtils.showBgResource(BaiduMainToMapPoiActivity.this, "网络错误！");
                }
                break;
            case R.id.ll_poi_collection:
                ToastUtils.showBgResource(BaiduMainToMapPoiActivity.this, "待完成");
                break;
            case R.id.ll_poi_share:
                ToastUtils.showBgResource(BaiduMainToMapPoiActivity.this, "待完成");
                break;
            case R.id.ll_poi_tip:
                ToastUtils.showBgResource(BaiduMainToMapPoiActivity.this, "待完成");
                break;
            case R.id.ll_poi_go:

                com.amap.api.maps.model.LatLng mEndLatlng = new com.amap.api.maps.model.LatLng(lat, lon);
                com.amap.api.maps.model.LatLng mStartLatlng = new com.amap.api.maps.model.LatLng(mCurrentLat, mCurrentLon);

                AmapNaviPage.getInstance().showRouteActivity(getApplicationContext()
                        ,new AmapNaviParams(new Poi(startpoi, mStartLatlng, "")
                                , null
                                , new Poi(endpoi, mEndLatlng, "")
                                , AmapNaviType.DRIVER).setTheme(AmapNaviTheme.WHITE)
                        , BaiduMainToMapPoiActivity.this);
                break;
            case R.id.iv_poi_phone:
                ToastUtils.showBgResource(BaiduMainToMapPoiActivity.this, "待完成");
                break;
            case R.id.ll_poi_moreinfo:
                ToastUtils.showBgResource(BaiduMainToMapPoiActivity.this, "待完成");
                break;
            case R.id.ll_poi_write:
                ToastUtils.showBgResource(BaiduMainToMapPoiActivity.this, "待完成");
                break;
        }
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {
        amapTTSController.onGetNavigationText(s);
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
        amapTTSController.stopSpeaking();
    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }

    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            // if (poi.hasCaterDetails) {
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poi.uid));
            // }
            return true;
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCenter = new LatLng(mCurrentLat, mCurrentLon);
            startpoi = location.getAddrStr();
            Log.i("startloc",location.getLatitude()+"---"+location.getCity());
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mPoiSearch.destroy();
        mMapView = null;
        amapTTSController.destroy();
        mLocClient.unRegisterLocationListener(mListener);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            String string = data.getExtras().getString("keyword", "美食");
            etBaidumapSearchpoi.setText(string);
            PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption().keyword(string)
                    .sortType(PoiSortType.distance_from_near_to_far).location(mCenter)
                    .radius(5000).pageNum(1).pageCapacity(50);
            mPoiSearch.searchNearby(nearbySearchOption);
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }
}
