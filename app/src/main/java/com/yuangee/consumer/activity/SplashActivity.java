package com.yuangee.consumer.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.widget.LinearLayout;

import com.yuangee.consumer.R;
import com.yuangee.consumer.permission.RxPermissions;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by developerLzh on 2017/4/19.
 */

public class SplashActivity extends BaseActivity {

    RxPermissions rxPermissions;

    @Bind(R.id.root_view)
    LinearLayout rootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        rxPermissions = new RxPermissions(this);

        showDialog();
    }

    private void showDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("为了能够正常的使用APP，请授予以下权限")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPer();
                    }
                })
                .create();
        dialog.show();
    }

    private void requestPer() {
        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            Snackbar.make(rootView, "授权成功", Snackbar.LENGTH_SHORT).show();
                            startActivity(new Intent(SplashActivity.this, PersonalActivity.class));
                        } else {
                            Snackbar.make(rootView, "授权失败", Snackbar.LENGTH_SHORT).show();
                            showDialog();
                        }
                    }
                });
    }
}
