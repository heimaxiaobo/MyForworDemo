package com.bobo.common.activity;

import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bobo.common.service.IWebService;

@Route(path = "/common/WebService")
public class WebService implements IWebService {

    private Context mContext;

    @Override
    public void startWeb(String url) {
        Intent intent = new Intent();
        intent.setClass(mContext, WebActivity.class);
        intent.putExtra("url", url);
        mContext.startActivity(intent);
    }

    @Override
    public void init(Context context) {
        mContext = context;
    }
}
