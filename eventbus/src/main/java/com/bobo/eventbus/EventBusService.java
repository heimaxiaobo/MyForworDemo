package com.bobo.eventbus;

import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bobo.common.service.IEventBusService;

@Route(path = "/eventbus/EventBusService")
public class EventBusService implements IEventBusService {

    private Context mContext;

    @Override
    public void startEventBus() {
        mContext.startActivity(new Intent(mContext,EventBusActivity.class));
    }

    @Override
    public void init(Context context) {
        mContext = context;
    }
}
