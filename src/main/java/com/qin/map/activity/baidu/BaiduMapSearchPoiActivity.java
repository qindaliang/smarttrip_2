package com.qin.map.activity.baidu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qin.R;
import com.qin.adapter.recyclerview.MarginDecoration;
import com.qin.adapter.recyclerview.map.SearchHistoryLocationAdapter;
import com.qin.adapter.recyclerview.map.SearchNearbyPoiRecyclerViewAdapter;
import com.qin.adapter.recyclerview.map.SearchOftenLocationAdapter;
import com.qin.constant.ConstantValues;
import com.qin.util.EditTextHintSize;
import com.qin.util.SPUtils;
import com.qin.util.ToastUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * Created by Administrator on 2018/3/14 0014.
 */

public class BaiduMapSearchPoiActivity extends AppCompatActivity implements OnGetSuggestionResultListener, OnGetPoiSearchResultListener, View.OnClickListener {

    @BindView(R.id.iv_location_back)
    ImageView ivLocationBack;
    @BindView(R.id.iv_location_voice)
    ImageView ivLocationVoice;
    @BindView(R.id.iv_location_search)
    ImageView ivLocationSearch;
    @BindView(R.id.ll_location_search)
    LinearLayout llLocationSearch;
    @BindView(R.id.iv_location_add)
    ImageView ivLocationAdd;
    @BindView(R.id.iv_location_delete)
    ImageView ivLocationDelete;
    @BindView(R.id.location_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_location)
    AutoCompleteTextView etLocation;
    @BindView(R.id.recyclerView_search_often)
    RecyclerView recyclerViewSearchOften;
    @BindView(R.id.ll_search_fragment)
    LinearLayout llSearchFragment;
    @BindView(R.id.search_rlv_nearbypoi)
    RecyclerView searchRlvNearbypoi;
    @BindView(R.id.iv_search_nearbypoi_about)
    ImageView ivSearchNearbypoiAbout;
    @BindView(R.id.ll_nearbypoi_recyclerview)
    LinearLayout llNearbypoiRecyclerview;
    @BindView(R.id.ll_search_nearbypoi_about)
    LinearLayout llSearchNearbypoiAbout;
    @BindView(R.id.tv_search_nearbypoi_synthesize)
    TextView tvSearchNearbypoiSynthesize;
    @BindView(R.id.tv_search_nearbypoi_distance)
    TextView tvSearchNearbypoiDistance;
    @BindView(R.id.tv_location_nohistory)
    TextView tvLocationNohistory;
    @BindView(R.id.tv_location_often)
    TextView tvLocationOften;
    private List<String> mLists = new ArrayList<>();
    ;
    private List<String> mListsoften = new ArrayList<>();
    private List<String> mListNearbyPoi = new ArrayList<>(256);
    ;
    private LinearLayoutManager mLayoutManager;
    private SearchHistoryLocationAdapter mHistoryLocationAdapter;
    private SearchOftenLocationAdapter mSearchOftenLocationAdapter;
    private GridLayoutManager mSearchLayoutManager;
    private PoiSearch mPoiSearch;
    private SuggestionSearch mSuggestionSearch;
    private ArrayAdapter<String> mArrayAdapter;
    private ArrayList<String> mSuggessLists;
    private SearchNearbyPoiRecyclerViewAdapter mPoiAdapter;
    private List<PoiInfo> mPoiInfoList;
    private List<PoiAddrInfo> mPoiAddrInfoList;
    //默认显示福州大学符经搜索
    private LatLng fzu = new LatLng(119.203482, 26.062197);
    private double mLatitude;
    private double mLongitude;
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
    private WindowManager mWindowManager;
    private DisplayMetrics mDisplayMetrics;
    private int mWidthPixels;
    private int mHeightPixels;
    private View popView;
    private int popIsShowing = 1;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidumap_searchpoi);
        ButterKnife.bind(this);
        mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        mWidthPixels = mDisplayMetrics.widthPixels;
        mHeightPixels = mDisplayMetrics.heightPixels;
        EditTextHintSize.setAutoEditTextHintSize(this, etLocation, 15, "请输入搜索关键字");
        mLatitude = getIntent().getExtras().getDouble("latitude", 119.203482);
        mLongitude = getIntent().getExtras().getDouble("longitude", 26.062197);
        String json = SPUtils.getInstance(this).getString(ConstantValues.SEARCH_POI_HISORY, "");
        /**
         * 解析保存入sp中的list集合
         */
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> historypoi = gson.fromJson(json, type);
        if (historypoi != null) {
            tvLocationNohistory.setVisibility(View.GONE);
            mLists.addAll(historypoi);
        } else {
            tvLocationNohistory.setVisibility(View.VISIBLE);
        }

        String jsonoften = SPUtils.getInstance(this).getString(ConstantValues.SEARCH_POI_OFTEN, "");
        /**
         * 解析保存入sp中的list集合
         */
        Type typeoften = new TypeToken<List<String>>() {
        }.getType();
        List<String> often = gson.fromJson(jsonoften, typeoften);
        if (often != null) {
            tvLocationOften.setVisibility(View.GONE);
            mListsoften.addAll(often);
        } else {
            tvLocationOften.setVisibility(View.VISIBLE);
        }
        initPoiSuggesstionSearchSearch();
        initPopWindow(this, R.layout.popwindow_search_bound, mWidthPixels, LinearLayout.LayoutParams.WRAP_CONTENT);
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        etLocation.setAdapter(mArrayAdapter);

        //设置附近搜索的poi
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        searchRlvNearbypoi.setItemAnimator(new DefaultItemAnimator());
        searchRlvNearbypoi.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        searchRlvNearbypoi.setLayoutManager(layoutManager);

        //历史记录
        mHistoryLocationAdapter = new SearchHistoryLocationAdapter(this, mLists);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        mRecyclerView.setAdapter(mHistoryLocationAdapter);

        //使用习惯
        mSearchOftenLocationAdapter = new SearchOftenLocationAdapter(this, mListsoften);
        recyclerViewSearchOften.setHasFixedSize(true);
        //设置网格布局的列数
        mSearchLayoutManager = new GridLayoutManager(this, 3);
        recyclerViewSearchOften.setLayoutManager(mSearchLayoutManager);
        //给每一个item添加间距
        recyclerViewSearchOften.addItemDecoration(new MarginDecoration(10));
        //   recyclerViewSearchOften.addItemDecoration(new DividerItemDecoration(this, HORIZONTAL));
        recyclerViewSearchOften.setAdapter(mSearchOftenLocationAdapter);

        mSearchOftenLocationAdapter.setOnRecyclerViewItemClickListener(new SearchOftenLocationAdapter.OnRecyclerViewItemClickListener() {

            @Override
            public void onItemClick(View view, SearchOftenLocationAdapter.ViewName name, int postion) {
                ToastUtils.showBgResource(BaiduMapSearchPoiActivity.this, "" + postion);
                if (SearchOftenLocationAdapter.ViewName.TEXT == name) {
                    RelativeLayout relativeLayout = (RelativeLayout) mSearchLayoutManager.findViewByPosition(postion);
                    TextView tv = (TextView) relativeLayout.getChildAt(0);
                    etLocation.setText(tv.getText());
                    // ToastUtil.show(SearchActivity.this, tv.getText()+""+ postion);
                }
                if (SearchOftenLocationAdapter.ViewName.IMAGEVIEW == name) {
                    mSearchOftenLocationAdapter.removeData(postion);
                    String json = gson.toJson(mListsoften);
                    SPUtils.getInstance(BaiduMapSearchPoiActivity.this).putString(ConstantValues.SEARCH_POI_OFTEN, json, true);
                }
            }
        });

        mHistoryLocationAdapter.setOnRecyclerViewItemClickListener(new SearchHistoryLocationAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, SearchHistoryLocationAdapter.ViewName name, int postion) {
                if (SearchHistoryLocationAdapter.ViewName.ITEM == name) {
                    ToastUtils.showBgResource(BaiduMapSearchPoiActivity.this, "text" + postion);
                    RelativeLayout relativeLayout = (RelativeLayout) mLayoutManager.findViewByPosition(postion);
                    TextView tv = (TextView) relativeLayout.getChildAt(0);
                    etLocation.setText(tv.getText());
                }
                if (SearchHistoryLocationAdapter.ViewName.IMAGEVIEW == name) {
                    mHistoryLocationAdapter.removeData(postion);
                    String json = gson.toJson(mLists);
                    SPUtils.getInstance(BaiduMapSearchPoiActivity.this).putString(ConstantValues.SEARCH_POI_HISORY, json, true);
                    ToastUtils.showBgResource(BaiduMapSearchPoiActivity.this, "删除" + postion);
                }
            }
        });

        etLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    if (llSearchFragment.getVisibility() != View.VISIBLE) {
                        llSearchFragment.setVisibility(View.VISIBLE);
                    }
                } else {
                    mSuggestionSearch.requestSuggestion(new SuggestionSearchOption().keyword(s.toString()).city("福州"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    /**
     * 初始化poiserach，sugesstion
     */
    private void initPoiSuggesstionSearchSearch() {
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
    }

    @OnClick({R.id.iv_location_back, R.id.iv_location_delete, R.id.iv_location_voice, R.id.iv_location_search, R.id.iv_location_add, R.id.ll_search_nearbypoi_about, R.id.tv_search_nearbypoi_synthesize, R.id.tv_search_nearbypoi_distance})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_location_back:
                finish();
                break;
            case R.id.iv_location_voice:
                etLocation.setText("");
                break;
            case R.id.iv_location_search:
                ToastUtils.showBgResource(this, "查找");
                setPoiNearbySearchOption(PoiSortType.comprehensive, 2000);
                mPoiAdapter = new SearchNearbyPoiRecyclerViewAdapter(mListNearbyPoi);
                searchRlvNearbypoi.setAdapter(mPoiAdapter);
                break;
            case R.id.iv_location_add:
                ToastUtils.showBgResource(this, "添加常用");
                if (!mListsoften.isEmpty()) {
                    mSearchOftenLocationAdapter.addData(mSearchLayoutManager.findFirstVisibleItemPosition(), "新来的");
                } else {
                    mSearchOftenLocationAdapter.addData(-2, "加油站");
                }
                String json = gson.toJson(mListsoften);
                SPUtils.getInstance(this).putString(ConstantValues.SEARCH_POI_OFTEN, json, true);
                break;
            case R.id.iv_location_delete:
                mHistoryLocationAdapter.removeAllData();
                SPUtils.getInstance(this).putString(ConstantValues.SEARCH_POI_HISORY, "", true);
                break;
            case R.id.ll_search_nearbypoi_about:
                ToastUtils.showBgResource(this, "fanwei");
                ivSearchNearbypoiAbout.setImageResource(R.mipmap.location);
                if (popIsShowing == 2) {
                    popupWindow.dismiss();
                    popIsShowing = 1;
                } else {
                    popupWindow.showAsDropDown(llSearchNearbypoiAbout, 0, 0);
                    setWindowGray(0.8f);
                }
                break;
            case R.id.tv_search_nearbypoi_synthesize:
                setPoiNearbySearchOption(PoiSortType.comprehensive, 2000);
                searchRlvNearbypoi.setAdapter(mPoiAdapter);
                llNearbypoiRecyclerview.setVisibility(View.VISIBLE);
                llSearchFragment.setVisibility(View.GONE);
                tvSearchNearbypoiDistance.setTextColor(Color.GRAY);
                tvSearchNearbypoiSynthesize.setTextColor(getResources().getColor(R.color.red));
                break;
            case R.id.tv_search_nearbypoi_distance:
                setPoiNearbySearchOption(PoiSortType.distance_from_near_to_far, 2000);
                searchRlvNearbypoi.setAdapter(mPoiAdapter);
                llNearbypoiRecyclerview.setVisibility(View.VISIBLE);
                llSearchFragment.setVisibility(View.GONE);
                tvSearchNearbypoiDistance.setTextColor(getResources().getColor(R.color.red));
                tvSearchNearbypoiSynthesize.setTextColor(Color.GRAY);
                break;
        }
    }

    public void setPoiNearbySearchOption(PoiSortType type, int radius) {
        PoiNearbySearchOption searchOption = new PoiNearbySearchOption()
                .keyword(etLocation.getText().toString())
                .sortType(type)
                .pageCapacity(10)
                .radius(radius)
                .location(new LatLng(mLatitude, mLongitude));
        mPoiSearch.searchNearby(searchOption);
    }

    /**
     * @param suggestionResult
     */
    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult != null && suggestionResult.getAllSuggestions() != null) {
            StringBuilder sb = new StringBuilder(256);
            mSuggessLists = new ArrayList<>();
            for (SuggestionResult.SuggestionInfo info : suggestionResult.getAllSuggestions()) {
                String key = info.key;
                String city = info.city;
                //TODO 联想结果uid，全景
                String uid = info.uid;
                sb.append("\nkey:==========");
                sb.append(key);
                sb.append("\ncity:==========");
                sb.append(city);
                sb.append("\nuid:==========");
                sb.append(uid);
                mSuggessLists.add(key);
            }
            Log.i("suggesstion=====", sb.toString());
            mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mSuggessLists);
            etLocation.setAdapter(mArrayAdapter);
            mArrayAdapter.notifyDataSetChanged();
        }
    }

    /**
     * @param poiResult
     */
    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            ToastUtils.showBgResource(this, "未找到结果");
            llSearchFragment.setVisibility(View.VISIBLE);
            return;
        }
        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
            //查找成功才加入历史记录
            mLists.add(etLocation.getText().toString().trim());
            mHistoryLocationAdapter.notifyDataSetChanged();
            String json = gson.toJson(mLists);
            SPUtils.getInstance(this).putString(ConstantValues.SEARCH_POI_HISORY, json, true);
            //查找成功才显示成功poi页面
            llNearbypoiRecyclerview.setVisibility(View.VISIBLE);
            llSearchFragment.setVisibility(View.GONE);
            tvSearchNearbypoiSynthesize.setTextColor(getResources().getColor(R.color.red));
            tvSearchNearbypoiDistance.setTextColor(Color.GRAY);

            mPoiInfoList = poiResult.getAllPoi();
            mListNearbyPoi.removeAll(mListNearbyPoi);
            StringBuilder sb = new StringBuilder(256);
            for (PoiInfo poiInfo : mPoiInfoList) {
                String address = poiInfo.address;
                String name = poiInfo.name;
                String phoneNum = poiInfo.phoneNum;
                String uid = poiInfo.uid;
                boolean hasCaterDetails = poiInfo.hasCaterDetails;
                boolean isPano = poiInfo.isPano;
                int i = poiInfo.describeContents();
                sb.append("\naddress");
                sb.append(address);
                sb.append("\nname");
                sb.append(name);
                sb.append("\nphoneNum");
                sb.append(phoneNum);
                sb.append("\nuid");
                sb.append(uid);
                sb.append("\nhasCaterDetails");
                sb.append(hasCaterDetails);
                sb.append("\nisPano");
                sb.append(isPano);
                sb.append("\ndescribeContents");
                sb.append(i);
                sb.append("\ndescribeContents=======");
                sb.append(poiResult.isHasAddrInfo());
                if (name != "" && name != null) {
                    mListNearbyPoi.add(name);
                }
            }
            mPoiAdapter.notifyDataSetChanged();

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
            Log.i("=====附近poi======", sb.toString());
            mPoiAdapter.setOnItemClickListener(new SearchNearbyPoiRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, SearchNearbyPoiRecyclerViewAdapter.ViewName viewName, int postion) {
                    if (SearchNearbyPoiRecyclerViewAdapter.ViewName.TEXTVIEW == viewName) {
                        ToastUtils.showBgResource(BaiduMapSearchPoiActivity.this, "" + postion);
                        Intent intent = new Intent();
                        intent.putExtra("keyword", etLocation.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    if (SearchNearbyPoiRecyclerViewAdapter.ViewName.IMAGEVIEW == viewName) {
                        ToastUtils.showBgResource(BaiduMapSearchPoiActivity.this, "跳转" + postion);
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

    /**
     * @param poiDetailResult
     */
    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(this, poiDetailResult.getName() + ": " + poiDetailResult.getAddress(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
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

    public void setWindowGray(float f) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = f;
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_pop_bound1:
                setPoiNearbySearchOption(PoiSortType.comprehensive, 500);
                searchRlvNearbypoi.setAdapter(mPoiAdapter);
                ivPop1.setVisibility(View.VISIBLE);
                ivPop2.setVisibility(View.GONE);
                ivPop3.setVisibility(View.GONE);
                ivPop4.setVisibility(View.GONE);
                ivPop5.setVisibility(View.GONE);
                popupWindow.dismiss();
                break;
            case R.id.rl_pop_bound2:
                setPoiNearbySearchOption(PoiSortType.comprehensive, 1000);
                searchRlvNearbypoi.setAdapter(mPoiAdapter);
                ivPop1.setVisibility(View.GONE);
                ivPop2.setVisibility(View.VISIBLE);
                ivPop3.setVisibility(View.GONE);
                ivPop4.setVisibility(View.GONE);
                ivPop5.setVisibility(View.GONE);
                popupWindow.dismiss();
                break;
            case R.id.rl_pop_bound3:
                setPoiNearbySearchOption(PoiSortType.comprehensive, 2000);
                searchRlvNearbypoi.setAdapter(mPoiAdapter);
                ivPop1.setVisibility(View.GONE);
                ivPop2.setVisibility(View.GONE);
                ivPop3.setVisibility(View.VISIBLE);
                ivPop4.setVisibility(View.GONE);
                popupWindow.dismiss();
                ivPop5.setVisibility(View.GONE);
                break;
            case R.id.rl_pop_bound4:
                setPoiNearbySearchOption(PoiSortType.comprehensive, 5000);
                searchRlvNearbypoi.setAdapter(mPoiAdapter);
                ivPop1.setVisibility(View.GONE);
                ivPop2.setVisibility(View.GONE);
                popupWindow.dismiss();
                ivPop3.setVisibility(View.GONE);
                ivPop4.setVisibility(View.VISIBLE);
                ivPop5.setVisibility(View.GONE);
                break;
            case R.id.rl_pop_bound5:
                setPoiNearbySearchOption(PoiSortType.comprehensive, 10000);
                searchRlvNearbypoi.setAdapter(mPoiAdapter);
                ivPop1.setVisibility(View.GONE);
                ivPop2.setVisibility(View.GONE);
                popupWindow.dismiss();
                ivPop3.setVisibility(View.GONE);
                ivPop4.setVisibility(View.GONE);
                ivPop5.setVisibility(View.VISIBLE);
                break;
        }
    }
}
