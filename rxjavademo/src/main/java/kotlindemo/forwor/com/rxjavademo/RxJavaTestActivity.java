package kotlindemo.forwor.com.rxjavademo;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
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

        //1.Rxjava的简单用法
        //rxjavaSimpleUsage();
        //2操作符的使用
        //2.1创建操作符的用法 创建被观察者对象，来发送事件
        //rxjavaCreateObservable();
        //2.2变换操作符的用法 （加工数据）对事件序列中的事件 / 整个事件序列 进行加工处理（即变换），使得其转变成不同的事件 / 整个事件序列
        //rxjavaConversion();
        //2.3组合/合并操作符
        rxjavaGroup();
    }


    /**
     * 1.Rxjava的入门用法
     */
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
                LogUtils.v("测试观察者断开连接之后 被观察者能发送能发否继续执行事件");
                e.onComplete();
            }
        });

        //2.观察者  直接使用Observable接口实现
        Observer<String> observer = new Observer<String>() {
            private Disposable mDisposable;

            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
                LogUtils.v("开始采用subscribe连接");
            }

            @Override
            public void onNext(String value) {
                //制造空指针异常，观察者将不接收其余的onNext()传递的事件
                if (value.contains("4")) {
                    mDisposable.dispose();
                }
                if (value.contains("5")) {
                    mTestView.setText("haha");
                }
                LogUtils.v(value);
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.v("onError()" + e.getMessage());
            }

            @Override
            public void onComplete() {
                LogUtils.v("onComplete()");
            }
        };

        //3.订阅  subscribe 方法具备多个重载方式 又返回值的Disposable 对象有断开连接的api dispose()，但是不会影响被观察者继续发送事件
        observable.subscribe(observer);
        observable.subscribe();
//        Disposable subscribe = observable.subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//
//            }
//        });
//        if (subscribe.isDisposed()) {
//            subscribe.dispose();
//        }

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

    /**
     * 2.1创建操作符用法
     */
    private void rxjavaCreateObservable() {

        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                LogUtils.d("创建操作符接收到的数据" + value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        //1.create 方法创建
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> mitter) throws Exception {

            }
        });

        //2.just() 快速创建并发送事件(参数对象)，最多可以传10个参数
        Observable.just(1, 2, 3, 4)
                // 至此，一个Observable对象创建完毕，以下步骤仅为展示一个完整demo，可以忽略
                // 2. 通过通过订阅（subscribe）连接观察者和被观察者
                // 3. 创建观察者 & 定义响应事件的行为
                .subscribe(observer);

        //3.forArray() 数组对象拆分成多个事件发送
        Integer[] arr = {11, 22, 33, 44};
        Observable.fromArray(arr).subscribe(observer);

        //4.fromIterable() 数组对象拆分成多个事件发送
        ArrayList<Integer> list = new ArrayList<>();
        list.add(111);
        list.add(222);
        list.add(333);
        list.add(444);
        Observable.fromIterable(list).subscribe(observer);


        // 下列方法一般用于测试使用

        //<--empty()-- >
        // 该方法创建的被观察者对象发送事件的特点：仅发送Complete事件，直接通知完成
        Observable observable1 = Observable.empty();
        // 即观察者接收后会直接调用onCompleted（）

        //<--error()-- >
        // 该方法创建的被观察者对象发送事件的特点：仅发送Error事件，直接通知异常
        // 可自定义异常
        Observable observable2 = Observable.error(new RuntimeException());
        // 即观察者接收后会直接调用onError（）

        //  < --never()-- >
        // 该方法创建的被观察者对象发送事件的特点：不发送任何事件
        Observable observable3 = Observable.never();
        // 即观察者接收后什么都不调用


        //5.defer() 延时创建观察者
        Observable<Integer> deferOb = Observable.defer(new Callable<ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> call() throws Exception {
                LogUtils.d("defer 延时创建观察者");
                return Observable.just(1, 2, 3);
            }
        });
        LogUtils.d("defer 延时创建观察者 test");
        deferOb.subscribe(observer);

        //6.timer（） 创建一个观察者延时发送事件 onNext(0) -->事件类型固定是Long类型
        Observable.timer(2, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long value) {
                LogUtils.d("timer 延时发送事件" + value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


        //7.interval（） 周期性发送事件 0你开始发送
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(
                        new Observer<Long>() {

                            private Disposable disposable;

                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable = d;
                            }

                            @Override
                            public void onNext(Long value) {
                                if (value == 20) {
                                    disposable.dispose();
                                }
                                LogUtils.d("interval 周期发送事件" + value);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );

        //8.intervalRange（） 周期性发送事件 定义开始数值，次数，第一次延时，周期时间 ---》Long类型
        Observable.intervalRange(5, 10, 1, 1, TimeUnit.SECONDS)
                .subscribe(
                        new Observer<Long>() {

                            private Disposable disposable;

                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable = d;
                            }

                            @Override
                            public void onNext(Long value) {
                                LogUtils.d("intervalRange 周期发送事件" + value);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
        //9.range  参数是 初始值 总次数 --->Interger
        //rangeLong  参数是 初始值 总次数 --->Long
        Observable.range(5, 10)
                .subscribe(
                        new Observer<Integer>() {

                            private Disposable disposable;

                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable = d;
                            }

                            @Override
                            public void onNext(Integer value) {
                                LogUtils.d("range 周期发送事件" + value);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
    }

    /**
     * 2.2变换操作符的用法
     */
    private void rxjavaConversion() {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                LogUtils.d("rxjavaConversion " + value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        //1.map 对于事件流数据的转换 本身的事件数是不变的
        Observable.just(1, 2, 3, 4).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return integer + "";
            }
        }).subscribe(observer);
        //2.flatMap 不仅变换事件还可以添加新的事件流   新的事件流跟旧事件总流顺序无关
        //ConcatMap 是跟旧的事件流顺序有关
        Observable.just(1, 2, 3, 4, 5).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                ArrayList<String> objects = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    objects.add("第" + integer + "个flatMap ： " + i);
                }
                return Observable.fromIterable(objects);
            }
        }).subscribe(observer);
        //Buffer() 一个缓冲区获取事件流  返回事件流集合
        Observable.just(1, 2, 3, 4, 5).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return integer + "";
            }
        }).buffer(3, 2).subscribe(new Observer<List<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<String> value) {
                LogUtils.d("buffer:" + value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    /**
     * 2.3组合/合并操作符
     */
    private void rxjavaGroup() {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                LogUtils.d("rxjavaGroup " + value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };


        Function<Object, String> function = new Function<Object, String>() {
            @Override
            public String apply(Object integer) throws Exception {
                return integer + "";
            }
        };

        //1组合多个观察者
        //1.1 concat()/concatArray()  区别在于concat Arry能多于4个以上的组合
//        Observable.concat(Observable.just(1, 2, 3, 4), Observable.just(5, 6, 7, 8), Observable.just(9, 10, 11, 12), Observable.just(13, 14, 15, 16)).map(function).subscribe(observer);
//
//        Observable.concat( //Observable.interval(1,TimeUnit.SECONDS), concat组合按顺序发送事件流
//                 Observable.intervalRange(1,10,1,1,TimeUnit.SECONDS)
//                ,Observable.intervalRange(11,10,1,2,TimeUnit.SECONDS))
//        .map(function).subscribe(observer);
//
//        //1.2 merge（） / mergeArray（） 按时间顺序发送事件流的
//        Observable.merge( //Observable.interval(1,TimeUnit.SECONDS), concat组合按顺序发送事件流
//                Observable.intervalRange(1,10,1,1,TimeUnit.SECONDS)
//                ,Observable.intervalRange(11,10,1,2,TimeUnit.SECONDS))
//                .map(function).subscribe(observer);

        //1.3concatDelayError（） / mergeDelayError（）  concatArrayDelayError（） / mergeArrayDelayError（） 延时处理错误事件，等待其他组的事件流发送完毕
//        Observable.mergeDelayError( //Observable.interval(1,TimeUnit.SECONDS), concat组合按顺序发送事件流
//                Observable.create(new ObservableOnSubscribe<String>() {
//                    @Override
//                    public void subscribe(ObservableEmitter<String> mitter) throws Exception {
//                        mitter.onNext("qqq");
//                        mitter.onNext("www");
//                        mitter.onNext("eee");
//                        mitter.onError(new NullPointerException());
//                    }
//                })
//                , Observable.intervalRange(1, 10, 1, 1, TimeUnit.SECONDS)
//                , Observable.intervalRange(11, 10, 1, 2, TimeUnit.SECONDS))
//                .map(function).subscribe(observer);
        //1.4Zip() 实现事件流合并 生成新的事件流组合 生成最少的事件流组合，其余的事件流多出来的事件不接收
//        ArrayList<String> list = new ArrayList<>();
//        list.add("A");
//        list.add("B");
//        list.add("C");
//        Observable.zip(Observable.just(1, 2, 3, 4, 5), Observable.fromIterable(list), new BiFunction<Integer, String, String>() {
//            @Override
//            public String apply(Integer integer, String s) throws Exception {
//                return integer + s;
//            }
//        }).subscribe(observer);

        //1.5combineLatest() 按最后一个事件流的时间来组合事件

        Observable.combineLatest(Observable.just(1, 2, 3, 4, 5),
                Observable.just(6, 7, 8, 9, 10), Observable.just(11, 12, 13, 14, 15) , new Function3<Integer, Integer, Integer, String>() {
                    @Override
                    public String apply(Integer integer, Integer integer2, Integer aLong) throws Exception {
                        return integer + "-" + integer2 + "-" + aLong;
                    }
                }).subscribe(observer);
    }
}
