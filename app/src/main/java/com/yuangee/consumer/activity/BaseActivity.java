package com.yuangee.consumer.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by developerLzh on 2017/4/19.
 */

public class BaseActivity extends AppCompatActivity {


    public void backAction(View v) {
        BaseActivity.this.finish();
    }

}
