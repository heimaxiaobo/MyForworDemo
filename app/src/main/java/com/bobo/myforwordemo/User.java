package com.bobo.myforwordemo;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

/**
 * 用户实体
 * @author 陈锦波 2019/1/4
 */
public class User extends BaseObservable {
    //单向绑定用到的注解
    @Bindable
    public String name;
    @Bindable
    private int age;
    private String details;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        //更新所有字段
        notifyChange();
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
        notifyChange();
    }


    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
        notifyChange();
    }

    public User(String name, int age, String details) {
        this.name = name;
        this.age = age;
        this.details = details;
    }
}
