package com.qin.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qin.R;
import com.qin.adapter.recyclerview.LoginRecyclerViewAdapter;
import com.qin.constant.ConstantValues;
import com.qin.dao.LoginUsers;
import com.qin.util.RxKeyboardTool;
import com.qin.util.SPUtils;
import com.qin.util.ScreenUtils;
import com.qin.view.button.CircularProgressButton;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.iv_login_logo)
    ImageView ivLoginLogo;
    @BindView(R.id.ll_login_username)
    LinearLayout llLoginUsername;
    @BindView(R.id.et_login_username)
    EditText etLoginUsername;
    @BindView(R.id.iv_login_clear_username)
    ImageView ivLoginClearUsername;
    @BindView(R.id.iv_login_more_user)
    ImageView ivLoginMoreUser;
    @BindView(R.id.tv_login_error_username)
    TextView tvLoginErrorUsername;
    @BindView(R.id.ll_login_password)
    LinearLayout llLoginPassword;
    @BindView(R.id.et_login_password)
    EditText etLoginPassword;
    @BindView(R.id.iv_login_clear_password)
    ImageView ivLoginClearPassword;
    @BindView(R.id.iv_login_show_password)
    ImageView ivLoginShowPassword;
    @BindView(R.id.tv_login_error_password)
    TextView tvLoginErrorPassword;
    @BindView(R.id.cb_login_remember_pwd)
    CheckBox cbLoginRememberPwd;
    @BindView(R.id.tv_login_forgetpwd)
    TextView tvLoginForgetpwd;
    @BindView(R.id.tv_login_register)
    TextView tvLoginRegister;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;
    @BindView(R.id.btn_login)
    CircularProgressButton btnLogin;
    @BindView(R.id.ll_login_bottom)
    LinearLayout llLoginBottom;
    @BindView(R.id.rl_login_content)
    RelativeLayout rlLoginContent;
    @BindView(R.id.fl_login_container)
    FrameLayout flLoginContainer;
    private boolean showPassword = false;
    private View mPopView;
    private PopupWindow mPopupWindow;
    private boolean isUp = false;
    private List<LoginUsers> users = new ArrayList<>();
    ObjectAnimator animator2, animator1;
    private LinearLayout.LayoutParams mParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ((Animatable) ivLoginLogo.getDrawable()).start();
        mParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        mParams.weight= (int) 0.75;
        boolean isChecked = SPUtils.getInstance(this).getBoolean(ConstantValues.LOGIN_CHECKBOX, false);
        if (isChecked) {
            String username = SPUtils.getInstance(this).getString(ConstantValues.LOGIN_USERNAME, "");
            String password = SPUtils.getInstance(this).getString(ConstantValues.LOGIN_PASSWORD, "");
            etLoginUsername.setText(username);
            etLoginPassword.setText(password);
            cbLoginRememberPwd.setChecked(isChecked);

            if (!TextUtils.isEmpty(username)) {
                translateUp(llLoginUsername);
                ivLoginClearUsername.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(password)) {
                translateUp(llLoginPassword);
                ivLoginClearPassword.setVisibility(View.VISIBLE);
            }
        }
        String json = SPUtils.getInstance(this).getString(ConstantValues.LOGIN_USERSLIST, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<LoginUsers>>() {
            }.getType();
            List<LoginUsers> userInfos = new ArrayList<>();
            userInfos = gson.fromJson(json, type);
            for (int i = 0; i < userInfos.size(); i++) {
                Log.d(TAG, userInfos.get(i).getUsername() + ":" + userInfos.get(i).getPassword());
                users.add(userInfos.get(i));
            }
        }

        btnLogin.setIndeterminateProgressMode(true);
        etLoginUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (TextUtils.isEmpty(etLoginUsername.getText())) {
                        translateUp(llLoginUsername);
                    }
                } else {
                    username();
                    if (TextUtils.isEmpty(etLoginUsername.getText())) {
                        translateDown(llLoginUsername);
                    } else {

                    }
                }
            }
        });
        etLoginPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (TextUtils.isEmpty(etLoginPassword.getText())) {
                        translateUp(llLoginPassword);
                    }
                } else {
                    password();
                    if (TextUtils.isEmpty(etLoginPassword.getText())) {
                        translateDown(llLoginPassword);
                    } else {

                    }
                }
            }
        });
        etLoginUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                username();
            }
        });

        etLoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password();
            }
        });

    }

    private void translateUp(View view) {
        AnimationSet set = new AnimationSet(false);
        TranslateAnimation t = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0
                , Animation.RELATIVE_TO_SELF, 0
                , Animation.RELATIVE_TO_SELF, 0
                , Animation.RELATIVE_TO_SELF, -1.3f);
        t.setDuration(500);

        ScaleAnimation s = new ScaleAnimation(1f, 0.7f, 1, 0.7f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
        s.setDuration(500);
        s.setRepeatCount(1);
        s.setRepeatMode(Animation.REVERSE);

        AlphaAnimation a = new AlphaAnimation(1f, 0.2f);
        a.setDuration(500);
        a.setRepeatMode(Animation.REVERSE);
        a.setRepeatCount(1);

        set.addAnimation(t);
        set.addAnimation(s);
        set.addAnimation(a);
        set.setFillAfter(true);
        view.startAnimation(set);
    }

    private void translateDown(View view) {
        AnimationSet set = new AnimationSet(true);
        TranslateAnimation t = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0
                , Animation.RELATIVE_TO_SELF, 0
                , Animation.RELATIVE_TO_SELF, -1.3f
                , Animation.RELATIVE_TO_SELF, 0);
        t.setDuration(500);
        t.setFillAfter(true);

        ScaleAnimation s = new ScaleAnimation(1f, 0.7f, 1f, 0.7f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        s.setDuration(500);
        s.setFillAfter(true);
        s.setRepeatCount(1);
        s.setRepeatMode(Animation.REVERSE);

        AlphaAnimation a = new AlphaAnimation(1f, 0.2f);
        a.setDuration(500);
        a.setRepeatMode(Animation.REVERSE);
        a.setRepeatCount(1);

        set.addAnimation(t);
        set.addAnimation(s);
        view.startAnimation(set);
    }

    public void initPopWindow(int layoutRes, int width, int height) {
        mPopView = View.inflate(this, R.layout.pop_login_user, null);
        mPopupWindow = new PopupWindow(mPopView, width, height, true);
        RecyclerView rlvLoginUser = mPopView.findViewById(R.id.rlv_login_user);
        final LinearLayout llLoginDelete = mPopView.findViewById(R.id.ll_login_delete);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvLoginUser.setLayoutManager(layoutManager);
        final LoginRecyclerViewAdapter adapter = new LoginRecyclerViewAdapter(users);
        rlvLoginUser.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rlvLoginUser.setItemAnimator(new DefaultItemAnimator());
        rlvLoginUser.setAdapter(adapter);
        adapter.setOnItemClickListener(new LoginRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, LoginRecyclerViewAdapter.ViewName name, final int postion) {
                if (LoginRecyclerViewAdapter.ViewName.ITEM == name) {
                    RelativeLayout layout = (RelativeLayout) layoutManager.findViewByPosition(postion);
                    TextView text = (TextView) layout.getChildAt(1);
                    etLoginUsername.setText(users.get(postion).getUsername());
                    etLoginPassword.setText(users.get(postion).getPassword());
                    mPopupWindow.dismiss();
                }
                if (LoginRecyclerViewAdapter.ViewName.IMAGEVIEW == name) {
                    users.remove(postion);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        llLoginDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users.clear();
                adapter.notifyDataSetChanged();
            }
        });
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mPopupWindow != null) {
                        mPopupWindow.dismiss();
                    }
                }
                return false;
            }
        });
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivLoginMoreUser.setImageResource(R.mipmap.down_login);
            }
        });
        mPopupWindow.setAnimationStyle(R.style.popSearchAnimtion);
        mPopupWindow.showAsDropDown(etLoginUsername, 0, 5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 10) {
            Bundle bundle = data.getExtras();
            String username = bundle.getString("username", "");
            String password = bundle.getString("password", "");
            Log.i(TAG, username + password);
            etLoginUsername.setText(username);
            etLoginPassword.setText(password);
        }
    }

    @SuppressLint("RestrictedApi")
    @OnClick({R.id.iv_login_logo, R.id.iv_login_clear_username, R.id.iv_login_more_user, R.id.iv_login_clear_password, R.id.iv_login_show_password, R.id.tv_login_forgetpwd, R.id.tv_login_register, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_login_logo:
                break;
            case R.id.iv_login_clear_username:
                etLoginUsername.setText("");
                break;
            case R.id.iv_login_more_user:
                //    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (isSoftShowing()) {
                    RxKeyboardTool.hideKeyboard(this, ivLoginMoreUser);
                }
                ivLoginMoreUser.setImageResource(R.mipmap.up_login);
                initPopWindow(R.layout.pop_login_user, etLoginUsername.getWidth(), (int) (ScreenUtils.getWindowHeight(this) / 2.3));

                break;
            case R.id.iv_login_clear_password:
                etLoginPassword.setText("");
                break;
            case R.id.iv_login_show_password:
                if (showPassword) {
                    // 显示密码
                    ivLoginShowPassword.setImageDrawable(getResources().getDrawable(R.mipmap.eye_open));
                    etLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPassword = !showPassword;
                } else {
                    // 隐藏密码
                    ivLoginShowPassword.setImageDrawable(getResources().getDrawable(R.mipmap.eye_close));
                    etLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPassword = !showPassword;
                }
                break;
            case R.id.tv_login_forgetpwd:
                Intent intent1 = new Intent();
                intent1.setClass(this, MainActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.tv_login_register:
                Intent intent = new Intent();
                intent.setClass(this, RegisterActivity.class);
                startActivityForResult(intent, 10, ActivityOptionsCompat.makeSceneTransitionAnimation(this, tvLoginRegister, "tvLoginRegister").toBundle());
                break;
            case R.id.btn_login:
               btnLogin.setProgress(50);
                if (username() && password()) {
                    //TODO 进行网络请求
                    String userName = etLoginUsername.getText().toString().trim();
                    String passWord = etLoginPassword.getText().toString().trim();
                    LoginUsers user = new LoginUsers();
                    user.setUsername(userName);
                    user.setPassword(passWord);
                    users.add(user);
                    Gson gson = new Gson();
                    String json = gson.toJson(users);
                    //TODO 上传用户名和密码
                    accessNet(userName, passWord);
                    EnterMainActivity();
                    //TODO 登陆成功才保存用户名和密码
                    if (cbLoginRememberPwd.isChecked()) {
                        SPUtils.getInstance(this).putString(ConstantValues.LOGIN_USERNAME, userName, true);
                        SPUtils.getInstance(this).putString(ConstantValues.LOGIN_PASSWORD, passWord, true);
                        SPUtils.getInstance(this).putString(ConstantValues.LOGIN_USERSLIST, json, true);
                    }
                    SPUtils.getInstance(this).putBoolean(ConstantValues.LOGIN_CHECKBOX, cbLoginRememberPwd.isChecked(), true);
                }
                break;
        }
    }

    private void accessNet(String userName, String passWord) {
        OkGo.<String>post("http://192.168.191.3/androidLoginValide")
                .tag(this)
                .params("userName",userName)
                .params("passWord",passWord)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("accessNet",response.body());
                        //TODO 处理服务器返回的信息

                        btnLogin.setProgress(1000);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                EnterMainActivity();
                            }
                        },500);
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }

    private boolean username() {
        if (TextUtils.isEmpty(etLoginUsername.getText().toString().trim())) {
            ivLoginClearUsername.setVisibility(View.INVISIBLE);
            tvLoginErrorUsername.setVisibility(View.VISIBLE);
            return false;
        } else {
            ivLoginClearUsername.setVisibility(View.VISIBLE);
            tvLoginErrorUsername.setVisibility(View.INVISIBLE);
        }
        return true;
    }

    private boolean password() {
        if (TextUtils.isEmpty(etLoginPassword.getText().toString().trim())) {
            ivLoginClearPassword.setVisibility(View.INVISIBLE);
            tvLoginErrorPassword.setVisibility(View.VISIBLE);
            return false;
        } else {
            ivLoginClearPassword.setVisibility(View.VISIBLE);
            tvLoginErrorPassword.setVisibility(View.INVISIBLE);
        }
        return true;
    }

    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return screenHeight - rect.bottom != 0;
    }

    @Override
    public void onBackPressed() {
        if (btnLogin.getProgress() != 0) {
            btnLogin.setProgress(0);
        } else {
            finish();
        }
    }
    private void EnterMainActivity(){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
