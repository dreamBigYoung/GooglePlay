package com.itheima.googleplay.protocol;

import android.support.annotation.NonNull;

import com.itheima.googleplay.base.BaseProtocol;

import java.util.List;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class HotProtocol extends BaseProtocol<List<String>>{
    @NonNull
    @Override
    public String getInterfaceKey() {
        return "hot";
    }
/*
    @Override
    protected List<String> parseJson(String resJsonString) {
        return new Gson().fromJson(resJsonString,new TypeToken<List<String>>(){}.getType());
    }*/
}
