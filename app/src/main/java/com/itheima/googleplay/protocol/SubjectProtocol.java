package com.itheima.googleplay.protocol;

import android.support.annotation.NonNull;

import com.itheima.googleplay.base.BaseProtocol;
import com.itheima.googleplay.bean.SubjectBean;

import java.util.List;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class SubjectProtocol extends BaseProtocol<List<SubjectBean>> {
    @NonNull
    @Override
    public String getInterfaceKey() {
        return "subject";
    }

/*    @Override
    protected List<SubjectBean> parseJson(String resJsonString) {
        Gson gson = new Gson();
        return gson.fromJson(resJsonString, new TypeToken<List<SubjectBean>>() {
        }.getType());
    }*/
}
