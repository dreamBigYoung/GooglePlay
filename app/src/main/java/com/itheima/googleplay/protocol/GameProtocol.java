package com.itheima.googleplay.protocol;

import android.support.annotation.NonNull;

import com.itheima.googleplay.base.BaseProtocol;
import com.itheima.googleplay.bean.ItemBean;

import java.util.List;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class GameProtocol extends BaseProtocol<List<ItemBean>> {
    /**
     * 决定协议的关键字
     *
     * @return
     */
    @NonNull
    @Override
    public String getInterfaceKey() {
        return "game";
    }

    /**
     * 解析网络请求回来的数据
     *
     * @param resJsonString
     * @return
     */
 /*   @Override
    protected List<ItemBean> parseJson(String resJsonString) {
        Gson gson = new Gson();
        return gson.fromJson(resJsonString, new TypeToken<List<ItemBean>>() {
        }.getType());
    }*/
}
