package com.qin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.qin.R;
import com.qin.constant.ConstantValues;
import com.qin.fragment.MainFragment;
import com.qin.recevier.NetworkChangedReceiver;
import com.qin.util.ToastUtils;

import app.dinus.com.loadingdrawable.LoadingView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/27 0027.
 */

public class MainActivity extends AppCompatActivity implements WeatherSearch.OnWeatherSearchListener {

    private static final long TIME_INTERVAL = 2000;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_main_weather)
    TextView tvMainWeather;
    @BindView(R.id.tv_main_temp)
    TextView tvMainTemp;
    @BindView(R.id.ll_main_weather)
    LinearLayout llMainWeather;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.drawer_container)
    FrameLayout drawerContainer;
    @BindView(R.id.gear_view)
    LoadingView gearView;
    private AccountHeader headerResult;
    private Drawer result;
    private NetworkChangedReceiver mNetworkChangedReceiver;
    private WeatherSearchQuery mquery;
    private WeatherSearch mweathersearch;
    private LocalWeatherLive weatherlive;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private String city;
    private long mBackPressed = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mNetworkChangedReceiver = new NetworkChangedReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkChangedReceiver, intentFilter);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("易行");
        if (savedInstanceState == null) {
            MainFragment homeFragment = MainFragment.newInstance("活儿");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
        }
        initLocation();
        startLocation();
        initSlidingMenu(savedInstanceState);
    }

    private void initSlidingMenu(Bundle savedInstanceState) {

        headerResult = new AccountHeaderBuilder()
                .withActivity(MainActivity.this)
                .withCompactStyle(false)
                .withHeaderBackground(R.color.blue)
                .addProfiles(new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(R.mipmap.gas_blank).withIdentifier(100),
                        new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(R.mipmap.gas_blank).withIdentifier(101),
                        new ProfileSettingDrawerItem().withName("添加账号").withDescription("添加一个账号").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(102),
                        new ProfileSettingDrawerItem().withName("管理账号").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(103))
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {

                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        switch ((int) profile.getIdentifier()) {
                            case 100:
                                Toast.makeText(MainActivity.this, "100", Toast.LENGTH_SHORT).show();
                                break;
                            case 101:
                                Toast.makeText(MainActivity.this, "101", Toast.LENGTH_SHORT).show();
                                break;
                            case 102:
                                Toast.makeText(MainActivity.this, "102", Toast.LENGTH_SHORT).show();
                                break;
                            case 103:
                                Toast.makeText(MainActivity.this, "103", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withRootView(R.id.drawer_container)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(false)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new SecondaryDrawerItem().withName("个人信息").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(10),
                        new SecondaryDrawerItem().withName("我的车辆").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(11),

                        new SecondaryDrawerItem().withName("历史足迹").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(13),
                        new SecondaryDrawerItem().withName("维修记录").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(14),
                        new SecondaryDrawerItem().withName("位置提醒点").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(15),
                        new SecondaryDrawerItem().withName("我的收藏").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(16),
                        new SecondaryDrawerItem().withName("使用指南").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(17),
                        new SecondaryDrawerItem().withName("检测更新").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(18),
                        new SecondaryDrawerItem().withName("当前版本").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(19),
                        new SecondaryDrawerItem().withName("开发人员").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(20),
                        new SecondaryDrawerItem().withName("退出账号").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(21),
                        new SectionDrawerItem()
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 10) {
                                StartFragment(10);
                            } else if (drawerItem.getIdentifier() == 11) {
                                StartFragment(11);
                            } else if (drawerItem.getIdentifier() == 13) {
                                StartFragment(13);
                            } else if (drawerItem.getIdentifier() == 14) {
                                StartFragment(14);
                            }else if (drawerItem.getIdentifier() == 15) {
                                StartFragment(15);
                            }else if (drawerItem.getIdentifier() == 16) {
                                StartFragment(16);
                            }else if (drawerItem.getIdentifier() == 17) {
                                StartFragment(17);
                            }else if (drawerItem.getIdentifier() == 18) {

                            }else if (drawerItem.getIdentifier() == 19) {

                            }else if (drawerItem.getIdentifier() == 20) {
                                StartFragment(20);
                            }else if (drawerItem.getIdentifier() == 21) {

                            }
                        }
                        return true;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }

    @NonNull
    public void StartFragment(int number) {
        Intent intent = new Intent();
        intent.putExtra(ConstantValues.DRAWERNUMBER, number);
        intent.setClass(this, DrawerActivity.class);
        startActivity(intent);
    }

    /**
     * 实时天气查询
     */
    private void searchliveweather(String city) {
        mquery = new WeatherSearchQuery(city, WeatherSearchQuery.WEATHER_TYPE_LIVE);//检索参数为城市和天气类型，实时天气为1、天气预报为2
        mweathersearch = new WeatherSearch(this);
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //异步搜索
    }

    /**
     * 实时天气查询回调
     */
    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                weatherlive = weatherLiveResult.getLiveResult();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvMainTemp.setVisibility(View.VISIBLE);
                        tvMainWeather.setVisibility(View.VISIBLE);
                        gearView.setVisibility(View.GONE);
                        tvMainWeather.setText(weatherlive.getWeather());
                        tvMainTemp.setText(weatherlive.getTemperature() + "°C");
                    }
                });
                Log.i("weather", weatherlive.getWeather() + "---" + weatherlive.getTemperature());
            } else {
                ToastUtils.showBgResource(MainActivity.this, "查询失败");
            }
        }
    }

    @Override
    public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.main.fragment_menu, main);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                return true;
            case 1:
                return true;
            case 2:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed();
                return;
            } else {
                ToastUtils.showBgResource(this, "再次点击返回键退出");
            }
            mBackPressed = System.currentTimeMillis();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mNetworkChangedReceiver);
        if (null != locationClient) {
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
        super.onDestroy();
    }

    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    private void startLocation() {
        // 启动定位
        locationClient.startLocation();
    }

    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                city = location.getCity();
                searchliveweather(city);
            }
        }
    };

    @OnClick(R.id.ll_main_weather)
    public void onViewClicked() {
        ToastUtils.showBgResource(this, "我是需要钱的");
    }

}

