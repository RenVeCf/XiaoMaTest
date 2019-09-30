package com.ipd.xiaomatest.common.config;

/**
 * Description ：URL 配置
 * Author ： MengYang
 * Email ： 942685687@qq.com
 * Time ： 2018/8/26.
 */
public interface UrlConfig {
    /**
     * 域名
     */
    String BASE_URL = "http://zl.v-lionsafety.com/index/";
    String BASE_LOCAL_URL = "http://zl.v-lionsafety.com/";

    /**
     * 扫码
     */
    String BIND = "bind/add"; //绑定车辆
    String LOCK = "bind/lock"; //开关车辆
    String BOX = "bind/open"; //开启电池仓
}
