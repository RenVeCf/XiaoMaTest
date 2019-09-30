package com.ipd.xiaomatest.api;

import com.ipd.xiaomatest.bean.LockBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.ipd.xiaomatest.common.config.UrlConfig.BIND;
import static com.ipd.xiaomatest.common.config.UrlConfig.BOX;
import static com.ipd.xiaomatest.common.config.UrlConfig.LOCK;

/**
 * Description ：请求配置
 * Author ： MengYang
 * Email ： 942685687@qq.com
 * Time ： 2018/8/27.
 */
public interface ApiService {

    //绑定车辆
    @FormUrlEncoded
    @POST(BIND)
    Observable<LockBean> getBind(@FieldMap Map<String, String> map);

    //开关车辆
    @FormUrlEncoded
    @POST(LOCK)
    Observable<LockBean> getLock(@FieldMap Map<String, String> map);

    //开启电池仓
    @FormUrlEncoded
    @POST(BOX)
    Observable<LockBean> getBox(@FieldMap Map<String, String> map);
}
