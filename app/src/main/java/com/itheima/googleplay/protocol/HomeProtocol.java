package com.itheima.googleplay.protocol;

import android.support.annotation.NonNull;

import com.itheima.googleplay.base.BaseProtocol;
import com.itheima.googleplay.bean.HomeBean;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      负责对HomeFragment里面涉及到的网络请求进行封装
 */
public class HomeProtocol extends BaseProtocol<HomeBean> {
    /**
     * 决定协议的关键字
     *
     * @return
     */
    @NonNull
    @Override
    public String getInterfaceKey() {
        return "home";
    }

    /**
     * 完成网络请求回来jsonString的解析
     * @param resJsonString
     * @return
     */
/*    @Override
    protected HomeBean parseJson(String resJsonString) {
        Gson gson = new Gson();
        return gson.fromJson(resJsonString,HomeBean.class);
    }*/
}
