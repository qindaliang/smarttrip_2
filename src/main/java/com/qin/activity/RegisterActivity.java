package com.qin.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.qin.R;
import com.qin.util.ToastUtils;
import com.qin.view.dialog.SuccessTickView;
import com.qin.view.textview.MyTextWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.tv_register_step_one)
    TextView tvRegisterStepOne;
    @BindView(R.id.line_register_one)
    View lineRegisterOne;
    @BindView(R.id.tv_register_step_two)
    TextView tvRegisterStepTwo;
    @BindView(R.id.line_register_two)
    View lineRegisterTwo;
    @BindView(R.id.tv_register_step_three)
    TextView tvRegisterStepThree;
    @BindView(R.id.iv_register_step_one)
    ImageView ivRegisterStepOne;
    @BindView(R.id.iv_register_step_two)
    ImageView ivRegisterStepTwo;
    @BindView(R.id.iv_register_step_three)
    ImageView ivRegisterStepThree;
    @BindView(R.id.et_register_phone)
    EditText etRegisterPhone;
    @BindView(R.id.iv_register_clean_phone)
    ImageView ivRegisterCleanPhone;
    @BindView(R.id.et_register_password)
    EditText etRegisterPassword;
    @BindView(R.id.iv_register_clean_password)
    ImageView ivRegisterCleanPassword;
    @BindView(R.id.et_register_password_con)
    EditText etRegisterPasswordCon;
    @BindView(R.id.iv_register_clean_passwordcon)
    ImageView ivRegisterCleanPasswordcon;
    @BindView(R.id.et_register_name)
    EditText etRegisterName;
    @BindView(R.id.iv_register_clean_name)
    ImageView ivRegisterCleanName;
    @BindView(R.id.et_register_chepai)
    EditText etRegisterChepai;
    @BindView(R.id.iv_register_clean_chepai)
    ImageView ivRegisterCleanChepai;
    @BindView(R.id.et_register_chejia)
    EditText etRegisterChejia;
    @BindView(R.id.iv_register_clean_chejia)
    ImageView ivRegisterCleanChejia;
    @BindView(R.id.et_register_enginenumber)
    EditText etRegisterEnginenumber;
    @BindView(R.id.iv_register_clean_enginenumber)
    ImageView ivRegisterCleanEnginenumber;
    @BindView(R.id.et_register_insurancename)
    EditText etRegisterInsurancename;
    @BindView(R.id.iv_register_clean_insurancename)
    ImageView ivRegisterCleanInsurancename;
    @BindView(R.id.et_register_insurancetel)
    EditText etRegisterInsurancetel;
    @BindView(R.id.iv_register_clean_insurancetel)
    ImageView ivRegisterCleanInsurancetel;
    @BindView(R.id.et_register_baodan)
    EditText etRegisterBaodan;
    @BindView(R.id.iv_register_clean_baodan)
    ImageView ivRegisterCleanBaodan;
    @BindView(R.id.et_register_producer)
    EditText etRegisterProducer;
    @BindView(R.id.iv_register_clean_producer)
    ImageView ivRegisterCleanProducer;
    @BindView(R.id.et_register_dealer)
    EditText etRegisterDealer;
    @BindView(R.id.iv_register_clean_dealer)
    ImageView ivRegisterCleanDealer;
    @BindView(R.id.btn_register_one_next)
    Button btnRegisterOneNext;
    @BindView(R.id.ll_register_one_content)
    LinearLayout llRegisterOneContent;
    @BindView(R.id.et_register_two_code)
    EditText etRegisterTwoCode;
    @BindView(R.id.btn_register_two_getcode)
    Button btnRegisterTwoGetcode;
    @BindView(R.id.btn_register_two_register)
    Button btnRegisterTwoRegister;
    @BindView(R.id.ll_register_two_content)
    LinearLayout llRegisterTwoContent;
    @BindView(R.id.register_mask_right_success)
    View registerMaskRightSuccess;
    @BindView(R.id.register_mask_left_success)
    View registerMaskLeftSuccess;
    @BindView(R.id.register_successtick)
    SuccessTickView registerSuccesstick;
    @BindView(R.id.btn_register_three_login)
    Button btnRegisterThreeLogin;
    @BindView(R.id.ll_registered_success)
    LinearLayout llRegisteredSuccess;
    private int i = -1;
    private Handler mHandler = new Handler();
    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initTextWatcherListener();
    }

    private void initTextWatcherListener() {
        etRegisterPhone.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterPhone.getText())) {
                    ivRegisterCleanPhone.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanPhone.setVisibility(View.GONE);
                }
            }
        });

        etRegisterPassword.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterPassword.getText())) {
                    ivRegisterCleanPassword.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanPassword.setVisibility(View.GONE);
                }
            }
        });

        etRegisterPasswordCon.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterPasswordCon.getText())) {
                    ivRegisterCleanPasswordcon.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanPasswordcon.setVisibility(View.GONE);
                }
            }
        });
        etRegisterName.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterName.getText())) {
                    ivRegisterCleanName.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanName.setVisibility(View.GONE);
                }
            }
        });
        etRegisterChepai.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterChepai.getText())) {
                    ivRegisterCleanChepai.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanChepai.setVisibility(View.GONE);
                }
            }
        });
        etRegisterChejia.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterChejia.getText())) {
                    ivRegisterCleanChejia.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanChejia.setVisibility(View.GONE);
                }
            }
        });
        etRegisterEnginenumber.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterEnginenumber.getText())) {
                    ivRegisterCleanEnginenumber.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanEnginenumber.setVisibility(View.GONE);
                }
            }
        });
        etRegisterInsurancename.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterInsurancename.getText())) {
                    ivRegisterCleanInsurancename.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanInsurancename.setVisibility(View.GONE);
                }
            }
        });
        etRegisterInsurancetel.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterInsurancetel.getText())) {
                    ivRegisterCleanInsurancetel.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanInsurancetel.setVisibility(View.GONE);
                }
            }
        });
        etRegisterBaodan.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterBaodan.getText())) {
                    ivRegisterCleanBaodan.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanBaodan.setVisibility(View.GONE);
                }
            }
        });
        etRegisterProducer.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterProducer.getText())) {
                    ivRegisterCleanProducer.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanProducer.setVisibility(View.GONE);
                }
            }
        });
        etRegisterDealer.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(etRegisterDealer.getText())) {
                    ivRegisterCleanDealer.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterCleanDealer.setVisibility(View.GONE);
                }
            }
        });
    }


    @OnClick({R.id.iv_register_clean_phone, R.id.iv_register_clean_password, R.id.iv_register_clean_passwordcon, R.id.iv_register_clean_name, R.id.iv_register_clean_chepai, R.id.iv_register_clean_chejia, R.id.iv_register_clean_enginenumber, R.id.iv_register_clean_insurancename, R.id.iv_register_clean_insurancetel, R.id.iv_register_clean_baodan, R.id.iv_register_clean_producer, R.id.iv_register_clean_dealer, R.id.btn_register_one_next, R.id.btn_register_two_getcode, R.id.btn_register_two_register, R.id.btn_register_three_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_register_clean_phone:
                ivRegisterCleanPhone.setVisibility(View.GONE);
                etRegisterPhone.setText("");
                break;
            case R.id.iv_register_clean_password:
                etRegisterPassword.setText("");
                ivRegisterCleanPassword.setVisibility(View.GONE);
                break;
            case R.id.iv_register_clean_passwordcon:
                etRegisterPasswordCon.setText("");
                ivRegisterCleanPasswordcon.setVisibility(View.GONE);
                break;
            case R.id.iv_register_clean_name:
                etRegisterName.setText("");
                ivRegisterCleanName.setVisibility(View.GONE);
                break;
            case R.id.iv_register_clean_chepai:
                etRegisterChepai.setText("");
                ivRegisterCleanChepai.setVisibility(View.GONE);
                break;
            case R.id.iv_register_clean_chejia:
                etRegisterChejia.setText("");
                ivRegisterCleanChejia.setVisibility(View.GONE);
                break;
            case R.id.iv_register_clean_enginenumber:
                etRegisterEnginenumber.setText("");
                ivRegisterCleanEnginenumber.setVisibility(View.GONE);
                break;
            case R.id.iv_register_clean_insurancename:
                etRegisterInsurancename.setText("");
                ivRegisterCleanInsurancename.setVisibility(View.GONE);
                break;
            case R.id.iv_register_clean_insurancetel:
                etRegisterInsurancetel.setText("");
                ivRegisterCleanInsurancetel.setVisibility(View.GONE);
                break;
            case R.id.iv_register_clean_baodan:
                etRegisterBaodan.setText("");
                ivRegisterCleanBaodan.setVisibility(View.GONE);
                break;
            case R.id.iv_register_clean_producer:
                etRegisterProducer.setText("");
                ivRegisterCleanProducer.setVisibility(View.GONE);
                break;
            case R.id.iv_register_clean_dealer:
                etRegisterDealer.setText("");
                ivRegisterCleanDealer.setVisibility(View.GONE);
                break;
            case R.id.btn_register_one_next:
                Animator animatorleft = AnimatorInflater.loadAnimator(this, R.animator.animator_register_stepleftexit);
                animatorleft.setTarget(llRegisterOneContent);
                animatorleft.start();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        llRegisterOneContent.setVisibility(View.GONE);
                    }
                }, 500);
                llRegisterTwoContent.setVisibility(View.VISIBLE);
                Animator animatorright = AnimatorInflater.loadAnimator(this, R.animator.animator_register_steprightenter);
                animatorright.setTarget(llRegisterTwoContent);
                animatorright.start();
                lineRegisterOne.setBackgroundColor(getResources().getColor(R.color.green));
                tvRegisterStepTwo.setBackground(getResources().getDrawable(R.drawable.vector_register_step_selected));
                break;
            case R.id.btn_register_two_getcode:
                break;
            case R.id.btn_register_two_register:
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight());
                params.width = (int) (getWindowManager().getDefaultDisplay().getWidth() * 0.7f);
                params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final Dialog dialog = new Dialog(RegisterActivity.this);
                mView = getLayoutInflater().inflate(R.layout.dialog_ok, null, false);
                dialog.setContentView(mView, params);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                final View mask_left_success = mView.findViewById(R.id.mask_left_success);
                final View mask_right_success = mView.findViewById(R.id.mask_right_success);
                final View mask_left_error = mView.findViewById(R.id.mask_left_error);
                final View mask_right_error = mView.findViewById(R.id.mask_right_error);
                final ImageView iv_x = mView.findViewById(R.id.iv_x);
                final ImageView iv_network_error = mView.findViewById(R.id.iv_network_error);
                final SuccessTickView mSuccessTick = (SuccessTickView) mView.findViewById(R.id.success_tick);
                final LinearLayout ll_registering = mView.findViewById(R.id.ll_registering);
                final LinearLayout ll_registered_success = mView.findViewById(R.id.ll_registered_success);
                final LinearLayout ll_registered_error = mView.findViewById(R.id.ll_registered_error);
                final LinearLayout ll_registered_networkerror = mView.findViewById(R.id.ll_registered_networkerror);
                final ProgressWheel progressWheel = mView.findViewById(R.id.progressWheel);
                final AnimationSet animation = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.success_mask_layout);
                dialog.show();

                new CountDownTimer(400 * 7, 400) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        i++;
                        switch (i) {
                            case 0:
                                progressWheel.setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                                break;
                            case 1:
                                progressWheel.setBarColor(getResources().getColor(R.color.material_deep_teal_50));
                                break;
                            case 2:
                                progressWheel.setBarColor(getResources().getColor(R.color.success_stroke_color));
                                break;
                            case 3:
                                progressWheel.setBarColor(getResources().getColor(R.color.material_deep_teal_20));
                                break;
                            case 4:
                                progressWheel.setBarColor(getResources().getColor(R.color.material_blue_grey_80));
                                break;
                            case 5:
                                progressWheel.setBarColor(getResources().getColor(R.color.warning_stroke_color));
                                break;
                        }
                    }

                    @Override
                    public void onFinish() {
                        i = -1;

//                注册成功显示对话框
                        ll_registering.setVisibility(View.GONE);
                        ll_registered_success.setVisibility(View.VISIBLE);
                        mask_left_success.startAnimation(animation.getAnimations().get(0));
                        mask_right_success.startAnimation(animation.getAnimations().get(1));
                        mSuccessTick.startTickAnim();


                        //               注册失败显示对话框
//                        ll_registering.setVisibility(View.GONE);
//                        ll_registered_error.setVisibility(View.VISIBLE);
//                        mask_left_error.startAnimation(animation.getAnimations().get(0));
//                        mask_right_error.startAnimation(animation.getAnimations().get(1));
//
//                        AnimationSet set1 = new AnimationSet(RegisterActivity.this, null);
//                        ScaleAnimation scaleAnimation = new ScaleAnimation(
//                                0, 1,
//                                0, 1,
//                                Animation.RELATIVE_TO_SELF, 0.5f,
//                                Animation.RELATIVE_TO_SELF, 0.5f);
//                        scaleAnimation.setDuration(500);
//                        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
//                        alphaAnimation.setDuration(500);
//                        set1.addAnimation(scaleAnimation);
//                        set1.addAnimation(alphaAnimation);
//                        iv_x.startAnimation(set1);
//
//
//                        //网络错误显示对话框
//                        ll_registering.setVisibility(View.GONE);
//                        ll_registered_networkerror.setVisibility(View.VISIBLE);
//                        AnimationSet set2 = new AnimationSet(RegisterActivity.this, null);
//                        ScaleAnimation scaleAnimation2 = new ScaleAnimation(
//                                0, 1,
//                                0, 1,
//                                Animation.RELATIVE_TO_SELF, 0.5f,
//                                Animation.RELATIVE_TO_SELF, 0.5f);
//                        scaleAnimation.setDuration(500);
//                        AlphaAnimation alphaAnimation2 = new AlphaAnimation(0, 1);
//                        alphaAnimation.setDuration(500);
//                        set2.addAnimation(scaleAnimation);
//                        set2.addAnimation(alphaAnimation);
//                        iv_network_error.startAnimation(set2);

                    }
                }.start();
                if (dialog.isShowing()) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    }, 5000);
                }
                //TODO 横向注册成功才会变绿
                Animator animatorleft1 = AnimatorInflater.loadAnimator(this, R.animator.animator_register_stepleftexit);
                animatorleft1.setTarget(llRegisterTwoContent);
                animatorleft1.start();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        llRegisterTwoContent.setVisibility(View.GONE);
                        llRegisteredSuccess.setVisibility(View.VISIBLE);
                        Animator animatorright1 = AnimatorInflater.loadAnimator(RegisterActivity.this, R.animator.animator_register_steprightenter);
                        animatorright1.setTarget(llRegisteredSuccess);
                        animatorright1.start();
                    }
                }, 200);
                lineRegisterTwo.setBackgroundColor(getResources().getColor(R.color.green));
                tvRegisterStepThree.setBackground(getResources().getDrawable(R.drawable.vector_register_step_selected));

                //TODO 注册成功显示成功页面


                break;
            case R.id.btn_register_three_login:
                String username = etRegisterPhone.getText().toString().trim();
                String password = etRegisterPassword.getText().toString().trim();
                Intent intent = new Intent();
                intent.putExtra("username",username);
                intent.putExtra("password",password);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (llRegisteredSuccess.getVisibility() == View.VISIBLE){
            Animator animatorleft = AnimatorInflater.loadAnimator(this, R.animator.animator_register_steprightexit);
            animatorleft.setTarget(llRegisteredSuccess);
            animatorleft.start();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    llRegisteredSuccess.setVisibility(View.GONE);
                }
            }, 500);
            llRegisterOneContent.setVisibility(View.VISIBLE);
            Animator animatorright = AnimatorInflater.loadAnimator(this, R.animator.animator_register_stepleftenter);
            animatorright.setTarget(llRegisterOneContent);
            animatorright.start();
            lineRegisterOne.setBackgroundColor(getResources().getColor(R.color.gray));
            lineRegisterTwo.setBackgroundColor(getResources().getColor(R.color.gray));
            tvRegisterStepThree.setBackground(getResources().getDrawable(R.drawable.vector_register_step_unselected));
            tvRegisterStepTwo.setBackground(getResources().getDrawable(R.drawable.vector_register_step_unselected));
        }
        if (llRegisterTwoContent.getVisibility() == View.VISIBLE){
            Animator animatorleft = AnimatorInflater.loadAnimator(this, R.animator.animator_register_steprightexit);
            animatorleft.setTarget(llRegisterTwoContent);
            animatorleft.start();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    llRegisterTwoContent.setVisibility(View.GONE);
                }
            }, 500);
            llRegisterOneContent.setVisibility(View.VISIBLE);
            Animator animatorright = AnimatorInflater.loadAnimator(this, R.animator.animator_register_stepleftenter);
            animatorright.setTarget(llRegisterOneContent);
            animatorright.start();
            lineRegisterOne.setBackgroundColor(getResources().getColor(R.color.gray));
            tvRegisterStepTwo.setBackground(getResources().getDrawable(R.drawable.vector_register_step_unselected));
        }else
        {
            finish();
        }
    }
}
