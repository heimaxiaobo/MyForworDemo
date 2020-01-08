package com.bobo.news;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 网络接口类
 *
 * @author 陈锦波  2019/1/4
 */
public interface Api {

    //get请求  http://zhouxunwang.cn/data/?id=75&key=UbnG/dJhH4/+iJyO94kzT2bHPATgsJeZ/px16A&type=top
    @GET("/data/?id=75&key=UbnG/dJhH4/+iJyO94kzT2bHPATgsJeZ/px16A")
    Observable<NewBean> getJsonData(@Query("type") String type);

}
