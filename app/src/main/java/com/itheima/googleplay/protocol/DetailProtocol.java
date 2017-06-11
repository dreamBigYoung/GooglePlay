package com.itheima.googleplay.protocol;

import android.support.annotation.NonNull;

import com.itheima.googleplay.base.BaseProtocol;
import com.itheima.googleplay.bean.ItemBean;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class DetailProtocol extends BaseProtocol<ItemBean> {
    //url-->http://localhost:8080/GooglePlayServer/detail?index=0
    //实际的url-->http://localhost:8080/GooglePlayServer/detail?packageName=com.itheima.www
    public String packageName;

    public DetailProtocol(String packageName) {
        this.packageName = packageName;
    }

    @NonNull
    @Override
    public String getInterfaceKey() {
        return "detail";
    }

/*    @Override
    protected ItemBean parseJson(String resJsonString) {
        Gson gson = new Gson();
        ItemBean itemBean = gson.fromJson(resJsonString, ItemBean.class);
        return itemBean;
    }*/

    /**
     * 覆写getParamsMap,返回不同的参数
     *
     * @param index
     * @return
     */
    @NonNull
    @Override
    public Map<String, Object> getParamsMap(int index) {
        Map<String, Object> parasmMap = new HashMap<>();
        parasmMap.put("packageName", packageName);
        return parasmMap;
    }

    /**
     * 覆写方法,返回不同的缓存的key
     *
     * @param index
     * @return
     */
    @NonNull
    @Override
    public String generateKey(int index) {
        return getInterfaceKey() + "." + packageName;
    }
}
