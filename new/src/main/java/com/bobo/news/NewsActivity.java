package com.bobo.news;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bobo.common.service.IWebService;
import com.bobo.news.databinding.ActivityNewsBinding;
import com.chad.library.adapter.base.BaseQuickAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * greenDao数据库的增删改查，网络请求 retrofit+Rjava
 *
 * @author 陈锦波  2019/1/4
 */
public class NewsActivity extends AppCompatActivity {

    private static final String TAG = "NewsActivity";
    private ActivityNewsBinding mBinding;
    //新闻列表的适配器
    private DemoAdapter demoAdapter;

    //SQLiteOpenHelper 对象
    private DaoMaster.DevOpenHelper mHelper;
    //数据库
    private SQLiteDatabase db;
    //连接类
    private DaoMaster mDaoMaster;
    //操作类
    private DaoSession mDaoSession;
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDatabase();
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_news);

        //配置RecyclerView新闻列表
        demoAdapter = new DemoAdapter();
        mBinding.rlvNews.setLayoutManager(new LinearLayoutManager(NewsActivity.this));
        //添加Item点击事件，查看新闻详情web页面
        demoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewInfoBean item = (NewInfoBean) adapter.getItem(position);
                startWeb(item.getUrl());
            }
        });
        mBinding.rlvNews.setAdapter(demoAdapter);
        demoAdapter.setNewData(mDaoSession.getNewInfoBeanDao().loadAll());

        //添加新闻
        mBinding.btnGetTopNews.setOnClickListener(addNews("top"));
        mBinding.btnGetSocietyNews.setOnClickListener(addNews("shehui"));
        mBinding.btnGetInternationalNews.setOnClickListener(addNews("guoji"));
        //删除新闻
        mBinding.btnDeleteTopNews.setOnClickListener(deleteNew("头条"));
        mBinding.btnDeleteSocietyNews.setOnClickListener(deleteNew("社会"));
        mBinding.btnDeleteInternationalNews.setOnClickListener(deleteNew("国际"));
        //修改新闻
        mBinding.btnUpdateInternationalNews.setOnClickListener(updateNew());
        
    }

    //通过ARouter跳转到通用model 查看网页
    public void startWeb(String url) {
        IWebService service = (IWebService) ARouter.getInstance().build("/common/WebService").navigation();
        if (service != null) { //没有依赖的情况，判空处理
            service.startWeb(url);
        }
    }

    /**
     * 修改第一个新闻的标题
     *
     * @return
     */
    private View.OnClickListener updateNew() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewInfoBean newInfoBean = mDaoSession.getNewInfoBeanDao().loadAll().get(0);
                newInfoBean.setTitle("我是陈锦波!我是陈锦波!我是陈锦波!");
                mDaoSession.getNewInfoBeanDao().update(newInfoBean);
                demoAdapter.setNewData(mDaoSession.getNewInfoBeanDao().loadAll());
            }
        };
    }

    /**
     * 删除新闻
     *
     * @param type 删除新闻的类型
     * @return
     */
    private View.OnClickListener deleteNew(final String type) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDaoSession.getNewInfoBeanDao().deleteInTx(mDaoSession.getNewInfoBeanDao().queryBuilder().where(NewInfoBeanDao.Properties.Category.eq(type)).list());
                demoAdapter.setNewData(mDaoSession.getNewInfoBeanDao().loadAll());
            }
        };
    }


    /**
     * 添加新闻
     *
     * @param type 添加新闻的类型
     * @return
     */
    private View.OnClickListener addNews(final String type) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitUtil.getInstance().getTestService()
                        .getJsonData(type)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<NewBean>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(NewBean value) {
                                Log.d("httpss", value.toString());
                                Log.d("httpss", value.getResult().getData().size() + "");
                                for (NewInfoBean datum : value.getResult().getData()) {
                                    mDaoSession.getNewInfoBeanDao().insertOrReplace(datum);
                                }

                                // 设置新的数据方法
                                demoAdapter.setNewData(mDaoSession.getNewInfoBeanDao().loadAll());
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        };
    }

    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new DaoMaster.DevOpenHelper(this, "new-db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

}
