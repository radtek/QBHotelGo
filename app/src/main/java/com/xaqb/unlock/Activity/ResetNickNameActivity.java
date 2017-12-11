package com.xaqb.unlock.Activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.xaqb.unlock.R;
import com.xaqb.unlock.Utils.ActivityController;
import com.xaqb.unlock.Utils.Globals;
import com.xaqb.unlock.Utils.GsonUtil;
import com.xaqb.unlock.Utils.HttpUrlUtils;
import com.xaqb.unlock.Utils.QBCallback;
import com.xaqb.unlock.Utils.QBHttp;
import com.xaqb.unlock.Utils.SPUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by lenovo on 2016/11/22.
 */
public class ResetNickNameActivity extends BaseActivityNew {

    private Button btComplete;
    private ResetNickNameActivity instance;
    private EditText etNickName;
    private TextView tvTitle;
    private String nickName, oldNickName;


    @Override
    public void initViews() {
        StatusBarUtil.setTranslucent(this,0);
        setContentView(R.layout.reset_nick_name_activity);
        instance = this;
        Intent intent = getIntent();
        oldNickName = intent.getStringExtra("nickName");
        assignViews();
        tvTitle.setText("修改昵称");
    }

    private void assignViews() {
        etNickName = (EditText) findViewById(R.id.et_nick_name);
        btComplete = (Button) findViewById(R.id.bt_complete);
        tvTitle = (TextView) findViewById(R.id.tv_title);
    }

    @Override
    public void initData() {
        if (oldNickName != null)
            etNickName.setText(oldNickName);
    }

    @Override
    public void addListener() {
        btComplete.setOnClickListener(instance);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_complete:
                nickName = etNickName.getText().toString().trim();
                if (nickName == null || nickName.equals("")) {
                    showToast("请输入昵称");
                } else if (oldNickName != null && oldNickName.equals(nickName)) {
                    showToast("昵称与原昵称相同");
                } else {
                    try {
                        resetNickName();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void resetNickName() {
        if (!checkNetwork()) {
            showToast(getResources().getString(R.string.network_not_alive));
            return;
        }
        loadingDialog.show("正在修改");
        Map<String, String> map = new HashMap<>();
        map.put("nickname", nickName);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), GsonUtil.GsonString(map));

        QBHttp.put(
                instance
                , HttpUrlUtils.getHttpUrl().getUpdataUserinfoUrl() + SPUtils.get(instance, "userid", "") + "?access_token=" + SPUtils.get(instance, "access_token", "")
                , body
                , new QBCallback() {
                    @Override
                    public void doWork(Map<?, ?> map) {
                        try {
                            loadingDialog.dismiss();
                            if (map.get("state").toString().equals(Globals.httpSuccessState)) {
                                SPUtils.put(instance, "staff_nickname", nickName);
                                showToast("修改昵称成功");
                                finish();
                            } else if (map.get("state").toString().equals(Globals.httpTokenFailure)) {
                                ActivityController.finishAll();
                                showToast("登录失效，请重新登录");
                                startActivity(new Intent(instance, LoginActivity.class));
                            } else {
                                showToast(map.get("mess").toString());
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void doError(Exception e) {
                        e.printStackTrace();
                        loadingDialog.dismiss();
                        showToast("网络访问异常");
                    }

                    @Override
                    public void reDoWork() {

                    }
                }
        );

    }
}

