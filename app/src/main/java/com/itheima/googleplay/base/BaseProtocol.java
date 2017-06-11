package com.itheima.googleplay.base;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.itheima.googleplay.conf.Constants;
import com.itheima.googleplay.utils.FileUtils;
import com.itheima.googleplay.utils.HttpUtils;
import com.itheima.googleplay.utils.IOUtils;
import com.itheima.googleplay.utils.LogUtils;
import com.itheima.googleplay.utils.UIUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	     针对具体的protocol封装一个基类
 */
public abstract class BaseProtocol<RESTYPE> {
    /**
     * 加载数据
     * 1.从内存-->返回
     * 2.从磁盘-->返回,存内存
     * 3.从网络-->返回,存内存,存磁盘
     *
     * @return
     * @throws Exception
     */
    public RESTYPE loadData(int index) throws Exception {
        RESTYPE result = null;
        //1.从内存-->返回
        result = loadDataFromMem(index);
        if (result != null) {
            LogUtils.s("从内存加载了数据-->" + generateKey(index));
            return result;
        }
        // 2.从磁盘-->返回,存内存
        result = loadDataFromLocal(index);
        if (result != null) {
            //本地有数据
            LogUtils.s("从本地加载了数据-->" + getCacheFile(index).getAbsolutePath());
            return result;
        }
        //3.从网络-->返回,存内存,存磁盘
        return loadDataFromNet(index);

    }

    /**
     * 从内存加载数据
     *
     * @param index
     * @return
     */
    private RESTYPE loadDataFromMem(int index) {
        RESTYPE result;//1.从内存-->返回
        //找到存储结构
        MyApplication application = (MyApplication) UIUtils.getContext();//getApplicationContext();
        Map<String, String> memProtocolCacheMap = application.getMemProtocolCacheMap();

        //判断存储结构中是否有缓存
        String key = generateKey(index);
        if (memProtocolCacheMap.containsKey(key)) {
            String memCacheJsonString = memProtocolCacheMap.get(key);
            result = parseJson(memCacheJsonString);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * 生成缓存的唯一索引的key
     * 子类可以覆写该方法,返回不同的缓存的key
     *
     * @param index
     * @return
     */
    @NonNull
    public String generateKey(int index) {
        return getInterfaceKey() + "." + index;//默认情况
    }

    /**
     * 从本地加载数据
     *
     * @param index
     * @return
     */
    private RESTYPE loadDataFromLocal(int index) {
        BufferedReader reader = null;
        try {
            //找到缓存文件
            File cacheFile = getCacheFile(index);
            //判断是否存在
            if (cacheFile.exists()) {
                //可能有有效的缓存
                reader = new BufferedReader(new FileReader(cacheFile));
                //读取缓存的生成时间
                String firstLine = reader.readLine();
                Long cacheInsertTime = Long.parseLong(firstLine);


                //判断是否过期
                if ((System.currentTimeMillis() - cacheInsertTime) < Constants.PROTOCOLTIMEOUT) {
                    //有效的缓存
                    String diskCacheJsonString = reader.readLine();

                     /*--------------- 保存数据到内存 ---------------*/
                    MyApplication application = (MyApplication) UIUtils.getContext();
                    Map<String, String> memProtocolCacheMap = application.getMemProtocolCacheMap();
                    memProtocolCacheMap.put(generateKey(index), diskCacheJsonString);
                    LogUtils.s("保存磁盘数据到内存-->" + generateKey(index));

                    //解析返回
                    return parseJson(diskCacheJsonString);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(reader);
        }
        return null;
    }

    /**
     * 得到缓存文件
     *
     * @param index
     * @return
     */
    @NonNull
    private File getCacheFile(int index) {
        String dir = FileUtils.getDir("json");//优先保存到外置sdcard,应用程序的缓存目录(sdcard/Android/data/包目录/json)
        String fileName = generateKey(index);//唯一命中的问题 interfaceKey+"."+index
        return new File(dir, fileName);
    }

    /**
     * 从网络加载数据,存本地
     *
     * @param index
     * @return
     * @throws IOException
     */
    private RESTYPE loadDataFromNet(int index) throws IOException {
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();

        //2.创建请求对象
        //http://localhost:8080/GooglePlayServer/home
        // ?index=0
        String url = Constants.URLS.BASEURL + getInterfaceKey();

        //定义参数对应的map
        /*
         Map<String, Object> params = new HashMap<>();
         params.put("index", index);//暂时不考虑分页
         */
        Map<String, Object> params = getParamsMap(index);

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
            /*--------------- 保存数据到内存 ---------------*/
            MyApplication application = (MyApplication) UIUtils.getContext();
            Map<String, String> memProtocolCacheMap = application.getMemProtocolCacheMap();
            memProtocolCacheMap.put(generateKey(index), resJsonString);
            LogUtils.s("保存网络数据到内存-->" + generateKey(index));

            /*--------------- 保存数据到本地 ---------------*/
            LogUtils.s("保存数据到本地-->" + getCacheFile(index).getAbsolutePath());
            BufferedWriter writer = null;
            try {
                File cacheFile = getCacheFile(index);
                writer = new BufferedWriter(new FileWriter(cacheFile));

                //写第一行
                writer.write(System.currentTimeMillis() + "");
                //换行
                writer.newLine();
                //写第二行
                writer.write(resJsonString);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(writer);
            }

                /*--------------- 完成json解析 ---------------*/
            return parseJson(resJsonString);
        } else {
            return null;
        }
    }

    /**
     * @param index
     * @return
     * @des 得到请求参数所对应的HashMap
     * @des 子类可以覆写该方法, 返回不同的参数
     */
    @NonNull
    public Map<String, Object> getParamsMap(int index) {
        Map<String, Object> params = new HashMap<>();
        params.put("index", index);//暂时不考虑分页
        return params;//默认参数是index
    }

    /**
     * @return
     * @des得到协议的关键字
     * @des 在BaseProtocl中, 不知道协议关键字具体是啥, 交给子类
     * @des 子类是必须实现, 所以定义成为抽象方法, 交给子类具体实现
     */
    @NonNull
    public abstract String getInterfaceKey();

    /**
     * @param resJsonString
     * @return
     * @des 负责解析网络请求回来的jsonString
     * @des 一共有3种解析情况(结点解析, Bean解析, 泛型解析)
     */
    protected RESTYPE parseJson(String resJsonString) {
        Gson gson = new Gson();
        Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        RESTYPE object = gson.fromJson(resJsonString, type);
        return object;
    }
}
