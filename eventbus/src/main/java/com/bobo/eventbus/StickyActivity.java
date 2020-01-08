package com.bobo.eventbus;

import android.os.Bundle;
import android.view.View;

import com.bobo.eventbus.databinding.ActivityStickyBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

/**
 * EventBus粘性接收，发送数据练习
 *
 * @author 陈锦波  2019/1/4
 */
public class StickyActivity extends AppCompatActivity {

    private ActivityStickyBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sticky);
        EventBus.getDefault().register(this);

        // 事件发送
        mBinding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(MessageWrap.getInstance("我是普通EventBus数据"));
                finish();
            }
        });
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onGetMessage(MessageWrap message) {
        mBinding.tvReceiveData.setText("接收到的粘性数据：" + message.message);
        //处理粘性事件
        MessageWrap stickyEvent = EventBus.getDefault().getStickyEvent(MessageWrap.class);
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
