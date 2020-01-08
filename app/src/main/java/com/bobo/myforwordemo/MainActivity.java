package com.bobo.myforwordemo;

import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bobo.common.service.IEventBusService;
import com.bobo.common.service.INewService;
import com.bobo.common.service.IRxJavaDemoSerview;
import com.bobo.myforwordemo.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

/**
 * 主界面,笑话跟eventbus模块的入口,databinding的基本用法
 *
 * @author 陈锦波  2019/1/4
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.btnChengeUsreName.setText("修改用户姓名");
        binding.btnChangeUsreAge.setText("修改用户年龄");
        binding.btnChangeUsre.setText("改变用户");
        binding.btnEventBus.setText("跳转到EventBusModel");
        binding.btnJoke.setText("跳转到笑话Model");
        binding.btnRxJava.setText("跳转到RxJavaModel");

        mUser = new User("陈锦波", 25, "福尔科技的Android试用员工");
        binding.setUser(mUser);
        binding.setMainhandler(new MainHandler());
    }

    //事件绑定的内部类
    public class MainHandler {

        //改变user实体类的Name值 单向绑定 public
        public void changeUsreName() {
            mUser.setName("陈浩航");
        }

        //改变user实体类的Age值 单向绑定 private
        public void changeUsreAge() {
            mUser.setAge(26);
        }

        //改变user实体类        EditText双向绑定Detail值
        public void changeUsre() {
            if (mUser.getName().equals("陈锦波")) {
                mUser = new User("陈浩航", 26, "福尔科技的Android实习员工");
            } else {
                mUser = new User("陈锦波", 25, "福尔科技的Android试用员工");
            }
            binding.setUser(mUser);
        }

        //通过ARouter跳转到笑话model 练习网络请求RxJava,Retrofit,GreenDao
        public void startJoke() {
            INewService service = (INewService) ARouter.getInstance().build("/new/NewService").navigation();
            if (service != null) { //没有依赖的情况，判空处理
                service.startNew();
            }
        }

        //通过ARouter跳转到EventBus model  练习EventBus
        public void startEventBus() {
            IEventBusService service = (IEventBusService) ARouter.getInstance().build("/eventbus/EventBusService").navigation();
            if (service != null) { //没有依赖的情况，判空处理
                service.startEventBus();
            }
        }

        //通过ARouter跳转到Rxjavamodel  练习Rxjava
        public void startRxJava() {
            IRxJavaDemoSerview service = (IRxJavaDemoSerview) ARouter.getInstance().build("/rxjavademo/RxJavaDemoSerview").navigation();
            if (service != null) { //没有依赖的情况，判空处理
                service.startRxJava();
            }
        }
    }
}
