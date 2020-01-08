package com.bobo.eventbus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bobo.eventbus.databinding.ActivityEventBusBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * EventBus接收，发送粘性数据练习
 * @author 陈锦波  2019/1/4
 * */
public class EventBusActivity extends AppCompatActivity {

    private ActivityEventBusBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_event_bus);
        EventBus.getDefault().register(this);

        // 事件发送
        mBinding.btnPostSticky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky( MessageWrap.getInstance("我是粘性EventBus数据"));
                startActivity(new Intent(EventBusActivity.this,StickyActivity.class));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(MessageWrap message) {
        mBinding.tvReceiveData.setText("接收到的数据："+message.message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
