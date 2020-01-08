package com.bobo.common.service;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface IWebService extends IProvider {
    /**
     * 进入Web页面
     */
    void startWeb(String url);
}
