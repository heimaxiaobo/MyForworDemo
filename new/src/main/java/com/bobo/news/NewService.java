package com.bobo.news;

import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bobo.common.service.INewService;

@Route(path = "/new/NewService")
public class NewService implements INewService {

    private Context mContext;

    @Override
    public void init(Context context) {
        mContext = context;
    }

    @Override
    public void startNew() {
        mContext.startActivity(new Intent(mContext, NewsActivity.class));
    }
}
