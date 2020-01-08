package com.bobo.common.service;


import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * EventBus练习模块对外提供的服务
 */
public interface IEventBusService extends IProvider {
    /**
     * 进入EventBus练习页面
     */
    void startEventBus();
}
