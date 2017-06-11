package com.itheima.googleplay.fragment;

import android.os.SystemClock;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.itheima.googleplay.base.BaseFragment;
import com.itheima.googleplay.base.ItemAdapter;
import com.itheima.googleplay.base.LoadingPager;
import com.itheima.googleplay.bean.HomeBean;
import com.itheima.googleplay.bean.ItemBean;
import com.itheima.googleplay.factory.ListViewFactory;
import com.itheima.googleplay.holder.HomePicturesHolder;
import com.itheima.googleplay.holder.ItemHolder;
import com.itheima.googleplay.manager.DownLoadInfo;
import com.itheima.googleplay.manager.DownLoadManager;
import com.itheima.googleplay.protocol.HomeProtocol;
import com.itheima.googleplay.utils.LogUtils;

import java.util.List;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class HomeFragment extends BaseFragment {

    private static final java.lang.String TAG = "HomeFragment";
    private List<String> mDatas;
    private List<ItemBean> mItemBeans;
    private List<String> mPictures;
    private HomeProtocol mProtocol;
    private HomeAdapter mAdapter;

    /**
     * @des 在子线程中真正的加载具体的数据
     * @called triggerLoadData()方法被调用的时候
     */
    @Override
    public LoadingPager.LoadedResult initData() {
        LogUtils.i(TAG, "initData");
        /*try {
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

                *//*--------------- 完成json解析 ---------------*//*
                Gson gson = new Gson();
                HomeBean homeBean = gson.fromJson(resJsonString, HomeBean.class);

                LoadingPager.LoadedResult state = checkResult(homeBean);
                if (state != LoadingPager.LoadedResult.SUCCESS) {//说明homeBean有问题,homeBean==null
                    return state;
                }
                state = checkResult(homeBean.list);
                if (state != LoadingPager.LoadedResult.SUCCESS) {//说明list有问题,list.size==0
                    return state;
                }

                //走到这里来说明是成功的
                //保存数据到成员变量
                mItemBeans = homeBean.list;
                mPictures = homeBean.picture;

                //返回相应的状态
                return state;//successs

            } else {//响应没有成功
                return LoadingPager.LoadedResult.ERROR;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }*/
        /*--------------- 协议进行简单封装以后 ---------------*/
        mProtocol = new HomeProtocol();
        try {
            HomeBean homeBean = mProtocol.loadData(0);

            LoadingPager.LoadedResult state = checkResult(homeBean);
            if (state != LoadingPager.LoadedResult.SUCCESS) {//说明homeBean有问题,homeBean==null
                return state;
            }
            state = checkResult(homeBean.list);
            if (state != LoadingPager.LoadedResult.SUCCESS) {//说明list有问题,list.size==0
                return state;
            }

            //走到这里来说明是成功的
            //保存数据到成员变量
            mItemBeans = homeBean.list;
            mPictures = homeBean.picture;

            //返回相应的状态
            return state;//successs
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }
    }

    /**
     * @return
     * @des 决定成功视图长什么样子(需要定义成功视图)
     * @des 数据和视图的绑定过程
     * @called triggerLoadData()方法被调用, 而且数据加载完成了, 而且数据加载成功
     */
    @Override
    public View initSuccessView() {
        LogUtils.i(TAG, "initSuccessView");
        //view
        ListView listView = ListViewFactory.createListView();
        //data-->dataSets-->在成员变量里面

        HomePicturesHolder homePicturesHolder = new HomePicturesHolder();
        //取出HomePicturesHolder所能提供的视图
        View headerView = homePicturesHolder.mHolderView;

        //让HomePicturesHolder接收数据,然后就行数据和视图的绑定
        homePicturesHolder.setDataAndRefreshHolderView(mPictures);


        //为listView添加一个headerView(轮播图)
        listView.addHeaderView(headerView);
        //data+view
        mAdapter = new HomeAdapter(mItemBeans, listView);
        listView.setAdapter(mAdapter);

        return listView;
    }

    class HomeAdapter extends ItemAdapter {
        public HomeAdapter(List<ItemBean> dataSets, AbsListView absListView) {
            super(dataSets, absListView);
            LogUtils.i(TAG, "HomeAdapter");
        }

        /**
         * @return
         * @throws Exception 加载更多过程中,出现了异常
         * @des 在子线中真正的加载更多的数据
         */
        @Override
        public List onLoadMore() throws Exception {
            SystemClock.sleep(2000);
            HomeBean homeBean = mProtocol.loadData(mItemBeans.size());
            if (homeBean != null) {
                return homeBean.list;
            }
            return null;
        }
    }
    /*class HomeAdapter extends SuperBaseAdapter<String> {
        public HomeAdapter(List<String> dataSets) {
            super(dataSets);
        }

        */

    /**
     * @return
     * @des 得到BaseHolder具体的子类对象
     *//*
        @Override
        public BaseHolder getSpecialBaseHolder() {
            return new ItemHolder();
        }
    }*/
    /*class HomeAdapter extends MyBaseAdapter<String> {

        public HomeAdapter(List<String> dataSets) {
            super(dataSets);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            *//*--------------- 决定根布局(itemView) ---------------*//*
            ItemHolder holder = null;
            if (convertView == null) {
                //创建holer对象
                holder = new ItemHolder();
            } else {
                holder = (ItemHolder) convertView.getTag();
            }
            *//*--------------- 得到数据,然后绑定数据 ---------------*//*
            //data
            String data = mDatas.get(position);
            holder.setDataAndRefreshHolderView(data);
            return holder.mHolderView;//其实这个convertView是经过了数据绑定的convertView
        }
    }*/
    /*class HomeAdapter extends MyBaseAdapter<String> {

        public HomeAdapter(List<String> dataSets) {
            super(dataSets);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            *//*--------------- 决定根布局(itemView) ---------------*//*
            ViewHolder holder = null;
            if (convertView == null) {
                //创建holer对象
                holder = new ViewHolder();
                //初始化根视图
                convertView = View.inflate(UIUtils.getContext(), R.layout.item_temp, null);
                //初始化孩子对象
                holder.mTvTmp1 = (TextView) convertView.findViewById(R.id.tmp_tv_1);
                holder.mTvTmp2 = (TextView) convertView.findViewById(R.id.tmp_tv_2);
                //找一个符合条件的holder,绑定在自己身上
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            *//*--------------- 得到数据,然后绑定数据 ---------------*//*
            //data
            String data = mDatas.get(position);

            //view-->在holder对象中

            //data+view
            holder.mTvTmp1.setText("我是头-" + data);
            holder.mTvTmp2.setText("我是尾巴-" + data);
            return convertView;//其实这个convertView是经过了数据绑定的convertView
        }

        *//*
          做ViewHolder需要什么条件
          1.持有根视图所对应的孩子对象-->严格的条件
          2.持有根视图->宽松的条件-->可以自行找出孩子,转换成成员变量
         *//*
        class ViewHolder {
            TextView mTvTmp1;
            TextView mTvTmp2;
        }
    }*/
    @Override
    public void onResume() {
        LogUtils.i(TAG, "onResume");
        //listview-->adapter
        if (mAdapter != null) {
            List<ItemHolder> itemHolders = mAdapter.mItemHolders;
            if (itemHolders != null && itemHolders.size() != 0) {
                for (ItemHolder o : itemHolders) {
                    //添加观察者到观察者集合中
                    DownLoadManager.getInstance().addObserver(o);

                    //手动发布最新的状态
                    DownLoadInfo downLoadInfo = DownLoadManager.getInstance().getDownLoadInfo(o.mData);
                    DownLoadManager.getInstance().notifyObservers(downLoadInfo);
                }
            }
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        LogUtils.i(TAG, "onPause");
        if (mAdapter != null) {
            List<ItemHolder> itemHolders = mAdapter.mItemHolders;
            if (itemHolders != null && itemHolders.size() != 0) {
                for (ItemHolder o : itemHolders) {
                    //从观察者集合中移除观察者
                    DownLoadManager.getInstance().deleteObserver(o);
                }
            }
        }
        super.onPause();
    }
}
