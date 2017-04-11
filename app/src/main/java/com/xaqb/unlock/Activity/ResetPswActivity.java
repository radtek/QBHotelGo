package com.xaqb.unlock.Activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xaqb.unlock.R;
import com.xaqb.unlock.Utils.Globals;
import com.xaqb.unlock.Utils.GsonUtil;
import com.xaqb.unlock.Utils.HttpUrlUtils;
import com.xaqb.unlock.Utils.LogUtils;
import com.xaqb.unlock.Utils.SPUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by lenovo on 2016/11/22.
 */
public class ResetPswActivity extends BaseActivity {

    private Button btComplete;
    private ResetPswActivity instance;
    private EditText etOldPsw, etNewPsw, etConfirmPsw;
    private int requestCode = 0;
    private String oldPsw, newPsw, confirmPsw;

    @Override
    public void initTitleBar() {
        setTitle("修改密码");
        showBackwardView(true);
    }

    @Override
    public void initViews() {
        setContentView(R.layout.resetpsw_activity);
        instance = this;
        assignViews();
    }

    private void assignViews() {
        etOldPsw = (EditText) findViewById(R.id.et_oldpsw);
        etNewPsw = (EditText) findViewById(R.id.et_newpsw);
        etConfirmPsw = (EditText) findViewById(R.id.et_confirm_psw);
        btComplete = (Button) findViewById(R.id.bt_complete);
    }

    @Override
    public void initData() {


    }

    @Override
    public void addListener() {
        btComplete.setOnClickListener(instance);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_complete:
                try {
                    resetPsw();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void resetPsw() {
        oldPsw = etOldPsw.getText().toString().trim();
        newPsw = etNewPsw.getText().toString().trim();
        confirmPsw = etConfirmPsw.getText().toString().trim();
        if (oldPsw == null || oldPsw.equals("")) {
            showToast("请输入旧密码");
        } else if (newPsw == null || newPsw.equals("")) {
            showToast("请输入新密码");
        } else if (confirmPsw == null || confirmPsw.equals("")) {
            showToast("请确认新密码");
        } else if (!newPsw.equals(confirmPsw)) {
            showToast("两次输入的密码不一致");
        } else {
            LogUtils.i(HttpUrlUtils.getHttpUrl().getResetPswUrl() + SPUtils.get(instance, "userid", "") + "?access_token=" + SPUtils.get(instance, "access_token", ""));
            loadingDialog.show("正在修改");
            OkHttpUtils
                    .post()
                    .url(HttpUrlUtils.getHttpUrl().getResetPswUrl() + SPUtils.get(instance, "userid", "") + "?access_token=" + SPUtils.get(instance, "access_token", ""))
                    .addParams("old_pwd", oldPsw)
                    .addParams("new_pwd", confirmPsw)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String s, int i) {
                            try {
                                loadingDialog.dismiss();
                                Map<String, Object> map = GsonUtil.JsonToMap(s);
                                if (map.get("state").toString().equals(Globals.httpSuccessState)) {
                                    showToast("修改密码成功");
                                    finish();
                                } else if (map.get("state").toString().equals(Globals.httpTokenFailure)) {
                                    finish();
                                    showToast("登录失效，请重新登录");
                                    startActivity(new Intent(instance, LoginActivity.class));
                                } else {
                                    showToast(map.get("mess").toString());
                                    return;
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

}
