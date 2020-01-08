package kotlindemo.forwor.com.rxjavademo;

import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bobo.common.service.IRxJavaDemoSerview;

/**
 * Created by Xuhui on 2020/1/7.
 */
@Route(path = "/rxjavademo/RxJavaDemoSerview")
public class RxJavaDemoSerview implements IRxJavaDemoSerview {
    @Override
    public void startRxJava() {
        mContext.startActivity(new Intent(mContext, RxJavaTestActivity.class));
    }

    private Context mContext;

    @Override
    public void init(Context context) {
        mContext = context;
    }
}
