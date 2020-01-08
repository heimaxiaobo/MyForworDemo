package com.bobo.common.service;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * 搞笑咨询模块对外提供的服务
 */
public interface INewService extends IProvider {
    /**
     * 进入新闻页面
     */
    void startNew();
}
