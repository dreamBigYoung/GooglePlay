package com.itheima.googleplay.fragment;

import android.os.SystemClock;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.itheima.googleplay.base.BaseFragment;
import com.itheima.googleplay.base.ItemAdapter;
import com.itheima.googleplay.base.LoadingPager;
import com.itheima.googleplay.bean.ItemBean;
import com.itheima.googleplay.factory.ListViewFactory;
import com.itheima.googleplay.holder.ItemHolder;
import com.itheima.googleplay.manager.DownLoadInfo;
import com.itheima.googleplay.manager.DownLoadManager;
import com.itheima.googleplay.protocol.AppProtocol;

import java.util.List;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class AppFragment extends BaseFragment {

    private AppProtocol mProtocol;
    private List<ItemBean> mDatas;
    private AppAdpater mAdapter;

    /**
     * @des 在子线程中真正的加载具体的数据
     * @called triggerLoadData()方法被调用的时候
     */
    @Override
    public LoadingPager.LoadedResult initData() {
        mProtocol = new AppProtocol();
        try {
            //网络请求得到数据
            mDatas = mProtocol.loadData(0);

            //校验网络请求回来的数据,返回对应的状态
            LoadingPager.LoadedResult loadedResult = checkResult(mDatas);

            return loadedResult;
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
        //view
        ListView listView = ListViewFactory.createListView();
        //dataSets-->成员变量-->mDatas

        //data+view
        mAdapter = new AppAdpater(mDatas, listView);
        listView.setAdapter(mAdapter);

        return listView;
    }

    class AppAdpater extends ItemAdapter {

        public AppAdpater(List<ItemBean> dataSets, AbsListView absListView) {
            super(dataSets, absListView);
        }

        /**
         * 具体去加载更多的数据
         */
        @Override
        public List onLoadMore() throws Exception {
            SystemClock.sleep(2000);
            List<ItemBean> itemBeans = mProtocol.loadData(mDatas.size());
            return itemBeans;
        }
    }
    @Override
    public void onResume() {
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
