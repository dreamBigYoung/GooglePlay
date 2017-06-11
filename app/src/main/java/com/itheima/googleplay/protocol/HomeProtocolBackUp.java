package com.itheima.googleplay.protocol;

import com.google.gson.Gson;
import com.itheima.googleplay.bean.HomeBean;
import com.itheima.googleplay.conf.Constants;
import com.itheima.googleplay.utils.HttpUtils;
import com.itheima.googleplay.utils.LogUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      负责对HomeFragment里面涉及到的网络请求进行封装
 */
public class HomeProtocolBackUp {
    /*
    我们的异常到底是抛出去还是try catch
    异常如果抛出去是抛到哪里去了?
        -->抛到方法的调用处
    什么时候抛什么时候try catch
        -->如果抛出的异常,在方法调用出会根据异常处理相应的逻辑的时候,就应该抛出去

     */

    public HomeBean loadData() throws Exception {
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.创建请求对象
        //http://localhost:8080/GooglePlayServer/home
        // ?index=0
        String url = Constants.URLS.BASEURL + "home";

        //定义参数对应的map
        Map<String, Object> params = new HashMap<>();
        params.put("index", 0);//暂时不考虑分页

        String urlParamsByMap = HttpUtils.getUrlParamsByMap(params);
        //url拼接参数对应的字符串信息
        url = url + "?" + urlParamsByMap;

        Request request = new Request.Builder().get().url(url).build();
        //3.发起请求-->同步请求
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            //取出响应的内容
            String resJsonString = response.body().string();
            LogUtils.s("resJsonString-->" + resJsonString);

                /*--------------- 完成json解析 ---------------*/
            Gson gson = new Gson();
            HomeBean homeBean = gson.fromJson(resJsonString, HomeBean.class);
            return homeBean;
        } else {
            return null;
        }
    }
}
