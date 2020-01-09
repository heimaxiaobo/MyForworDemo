package kotlindemo.forwor.com.rxjavademo;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;

import java.net.HttpURLConnection;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * RxJava2的练习
 *
 * @author chenjinbo 2020/1/7
 */
public class RxJavaTestActivity extends AppCompatActivity {

    private static final String TAG = "RxJavaTestActivity";
    private TextView mTestView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_test);

        //Rxjava的简单用法
        rxjavaSimpleUsage();

    }

    private void rxjavaSimpleUsage() {

        /**
         * 分步骤实现
         * 1.创建被观察者
         * 2.创建观察者
         * 3.通过订阅（链接被观察者和观察者）
         */
        //1.被观察者
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                //onNext()相同的值只能发送两次
                e.onNext("你好！！");
                e.onNext("你好！！");
                e.onNext(new String("你好！！"));
                e.onNext("你好！！4");
                e.onNext("你好！！5");
                //onError()后被观察者停止执行其余的onNext()事件
                e.onNext("你好！！6");
                e.onComplete();
            }
        });

        //2.观察者  直接使用Observable接口实现
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtils.v("开始采用subscribe连接");
            }

            @Override
            public void onNext(String value) {
                //制造空指针异常，观察者将不接收其余的onNext()传递的事件
                if(value.contains("5")){
                    mTestView.setText("haha");
                }
                 LogUtils.v( value);
            }

            @Override
            public void onError(Throwable e) {
                 LogUtils.v( "onError()" + e.getMessage());
            }

            @Override
            public void onComplete() {
                 LogUtils.v( "onComplete()");
            }
        };

        //3.订阅  subscribe 方法具备多个重载方式 又返回值的Disposable 对象又断开连接的api dispose()
        observable.subscribe(observer);
        Disposable subscribe = observable.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {

            }
        });
        if (subscribe.isDisposed()) {
            subscribe.dispose();
        }

        /**
         * 链式调用实现 特点：逻辑简洁，实现优雅，使用简单
         */
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("嗨咯1");
                e.onNext("嗨咯2");
                e.onNext("嗨咯3");
                e.onNext("嗨咯3");
                e.onNext("嗨咯3");
                e.onNext("嗨咯4");
                e.onNext("嗨咯5");
                e.onNext("嗨咯6");
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {
                        Log.e(TAG, "onNext: " + value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: ");
                    }
                });
    }
}
