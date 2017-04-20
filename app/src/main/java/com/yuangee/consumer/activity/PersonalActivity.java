package com.yuangee.consumer.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.luck.picture.ui.AlbumDirectoryActivity;
import com.luck.picture.ui.ImageGridActivity;
import com.luck.picture.util.Constants;
import com.luck.picture.util.LocalMediaLoader;
import com.yuangee.consumer.R;
import com.yuangee.consumer.permission.RxPermissions;
import com.yuangee.consumer.util.SpCache;
import com.yuangee.consumer.util.StringUtils;
import com.yuangee.consumer.util.ToastUtil;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.itangqi.waveloadingview.WaveLoadingView;
import rx.functions.Action1;

/**
 * Created by developerLzh on 2017/4/19.
 */

public class PersonalActivity extends BaseActivity {

    @Bind(R.id.waveLoadingView)
    WaveLoadingView waveLoadingView;

    @Bind(R.id.cus_vip)
    TextView cusVip;

    @Bind(R.id.cus_name)
    TextView cusName;

    @Bind(R.id.cus_age)
    TextView cusAge;

    @Bind(R.id.cus_gender)
    TextView cusGender;

    @OnClick(R.id.vip_con)
    void setCusVip() {

    }

    @OnClick(R.id.name_con)
    void setCusName() {
        final EditText editName = new EditText(this);
        String sName = cusName.getText().toString();
        editName.setText(sName);
        if (StringUtils.isNotBlank(sName)) {
            editName.setSelection(sName.length());
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(50, 20, 50, 10);
        editName.setLayoutParams(layoutParams);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.addView(editName);
        dialog = new AlertDialog.Builder(this)
                .setTitle("姓名")
                .setPositiveButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s = editName.getText().toString();
                        if (StringUtils.isNotBlank(s)) {
                            cusName.setText(s);
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setView(linearLayout)
                .create();
        dialog.show();
    }

    @OnClick(R.id.age_con)
    void setCusAge() {
        if (inflater == null) {
            inflater = LayoutInflater.from(this);
        }
        View view = inflater.inflate(R.layout.age_select_layout, null);
        final NumberPicker picker = (NumberPicker) view.findViewById(R.id.num_picker);
        picker.setMinValue(18);
        picker.setMaxValue(60);
        final int currentAge;
        String sAge = cusAge.getText().toString();
        if (StringUtils.isNotBlank(sAge)) {
            currentAge = Integer.parseInt(sAge.replace("岁", ""));
        } else {
            currentAge = 23;
        }
        picker.setValue(currentAge);

        dialog = new AlertDialog.Builder(this)
                .setTitle("年龄")
                .setView(view)
                .setPositiveButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int age = picker.getValue();
                        cusAge.setText(age + "岁");
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    @OnClick(R.id.gender_con)
    void setCusGender() {
        if (inflater == null) {
            inflater = LayoutInflater.from(this);
        }
        View view = inflater.inflate(R.layout.gender_select_layout, null);
        RadioButton maleRadio = (RadioButton) view.findViewById(R.id.male_radio);
        RadioButton femaleRadio = (RadioButton) view.findViewById(R.id.female_radio);
        if (cusGender.getText().toString().equals("男")) {
            maleRadio.setChecked(true);
        } else {
            femaleRadio.setChecked(true);
        }
        maleRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cusGender.setText("男");
                    dialog.dismiss();
                }
            }
        });
        femaleRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cusGender.setText("女");
                    dialog.dismiss();
                }
            }
        });
        dialog = new AlertDialog.Builder(this)
                .setTitle("性别")
                .setView(view)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    @Bind(R.id.photo)
    ImageView photo;

    @OnClick(R.id.photo)
    void photoClick() {
        checkPer();
    }

    @Bind(R.id.photo_hint)
    TextView photoHint;

    private AlertDialog dialog;
    private LayoutInflater inflater;

    private Timer timer;
    private TimerTask timerTask;

    private int count = 0;
    private boolean isAdd = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        ButterKnife.bind(this);

        waveLoadingView.setAnimDuration(2000);

        initHint();
    }

    private void initHint() {
        SpCache spCache = new SpCache(this);
        if (spCache.get("showPhotoHint", true)) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (count > 0 && count < 10) {
                        if (isAdd) {
                            count++;
                        } else {
                            count--;
                        }
                    } else if (count == 10) {
                        isAdd = false;
                        count--;
                    } else if (count == 0) {
                        isAdd = true;
                        count++;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            photoHint.setAlpha((float) (count * 0.1));
                        }
                    });
                }
            };
            timer.schedule(timerTask, 100, 100);
        } else {
            photoHint.setVisibility(View.GONE);
        }
    }

    private void checkPer() {
        new RxPermissions(this).request(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            selectPhoto();
                        } else {
                            ToastUtil.showMessage(PersonalActivity.this, "未能获得访问外部存储权限，不能选择头像");
                        }
                    }
                });
    }

    private void selectPhoto() {
        int selectType = LocalMediaLoader.TYPE_IMAGE;
        int copyMode = Constants.COPY_MODEL_1_1;
        boolean enablePreview = false;//
        boolean isPreviewVideo = true;//视频预览
        boolean enableCrop = true;//裁剪
        int maxSelectNum = 1;//最大张数1
        int selectMode = Constants.MODE_SINGLE;//单选
        boolean isShow = true;
        AlbumDirectoryActivity.startPhoto(PersonalActivity.this, selectType, copyMode, maxSelectNum, selectMode, isShow, enablePreview, enableCrop, isPreviewVideo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ImageGridActivity.REQUEST_IMAGE) {
                List<String> images = (ArrayList<String>) data.getSerializableExtra(ImageGridActivity.REQUEST_OUTPUT);
                if (images != null && images.size() > 0) {
                    String path = images.get(0);
                    Glide.with(PersonalActivity.this)
                            .load(path)
                            .bitmapTransform(new CropCircleTransformation(this))
                            .placeholder(R.color.color_f6)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(photo);
                    new SpCache(this).put("showPhotoHint", false);
                    if (timer != null) {
                        timer.cancel();
                    }
                    if (timerTask != null) {
                        timerTask.cancel();
                    }
                }
            }
        }
    }
}
