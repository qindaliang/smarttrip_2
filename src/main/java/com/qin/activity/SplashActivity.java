package com.qin.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qin.R;
import com.qin.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.tv_splash)
    TextView tvSplash;
    private int n = 0;
    private int titleLength = 0;
    private String title = "易行，简单你的生活";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        // ((Animatable) ivSplash.getDrawable()).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startShowText();
            }
        },500);
      //  startShowText();
        initUpdateDialog();
    }

    private void startShowText() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String text = title.substring(0, n);
                    tvSplash.post(new Runnable() {
                        @Override
                        public void run() {
                            tvSplash.setText(text);
                            Log.i("text",n+"");
                        }
                    });
                    SystemClock.sleep(150);
                    n++;
                    if (n <= title.length()) {
                        startShowText();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void initUpdateDialog() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight());
        params.width = (int) (getWindowManager().getDefaultDisplay().getWidth() * 0.7f);
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final Dialog dialog = new Dialog(SplashActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_version_upgrde, null, false);
        dialog.setContentView(view, params);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ToastUtils.showBgResource(SplashActivity.this, "消失");
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                ToastUtils.showBgResource(SplashActivity.this, "更新");
            }
        });
        new Thread() {
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(3000);
                //startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                });
            }
        }.start();
    }

}
