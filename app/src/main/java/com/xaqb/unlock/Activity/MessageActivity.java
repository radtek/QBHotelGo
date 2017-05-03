package com.xaqb.unlock.Activity;

import android.view.View;

import com.xaqb.unlock.R;
import com.xaqb.unlock.Utils.Globals;
import com.xaqb.unlock.Utils.GsonUtil;
import com.xaqb.unlock.Utils.HttpUrlUtils;
import com.xaqb.unlock.Utils.LogUtils;
import com.xaqb.unlock.Utils.SPUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by chengeng on 2016/12/2.
 * 空activity，用于复制粘贴
 */
public class MessageActivity extends BaseActivity {
    private MessageActivity instance;

    @Override
    public void initTitleBar() {
        setTitle("消息中心");
        showBackwardView(true);
    }

    @Override
    public void initViews() {
        setContentView(R.layout.message_activity);
        instance = this;
        assignViews();
    }

    private void assignViews() {

    }

    @Override
    public void initData() {
        if (!checkNetwork()) return;
        LogUtils.i(HttpUrlUtils.getHttpUrl().getOrderList() + "?id=" + SPUtils.get(instance, "userid", "") +"?access_token=" + SPUtils.get(instance, "access_token", ""));
        OkHttpUtils.get()
                .url(HttpUrlUtils.getHttpUrl().getOrderList() + "?id=" + SPUtils.get(instance, "userid", "")+"?access_token=" + SPUtils.get(instance, "access_token", "") )
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        try {
                            Map<String, Object> map = GsonUtil.JsonToMap(s);
                            LogUtils.i(map.toString());
                            if (map.get("state").toString().equals(Globals.httpSuccessState)) {
                                LogUtils.i("senddata", "" + map.toString());
                                List<Map<String, Object>> data = GsonUtil.GsonToListMaps(GsonUtil.GsonString(map.get("table")));
                            } else {
                                showToast(map.get("mess").toString());
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    public void addListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}