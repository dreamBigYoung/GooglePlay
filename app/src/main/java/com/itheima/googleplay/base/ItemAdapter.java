package com.itheima.googleplay.base;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.itheima.googleplay.activity.DetailActivity;
import com.itheima.googleplay.bean.ItemBean;
import com.itheima.googleplay.holder.ItemHolder;
import com.itheima.googleplay.manager.DownLoadManager;
import com.itheima.googleplay.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class ItemAdapter extends SuperBaseAdapter<ItemBean> {
    //创建一个集合保存所有的ItemHolder(观察者)
    public List<ItemHolder> mItemHolders = new ArrayList<>();

    public ItemAdapter(List<ItemBean> dataSets, AbsListView absListView) {
        super(dataSets, absListView);
    }

    /**
     * @param position
     * @return
     * @des 得到BaseHolder具体的子类对象
     */
    @Override
    public BaseHolder getSpecialBaseHolder(int position) {
        ItemHolder itemHolder = new ItemHolder();
        //添加观察者到集合中
        mItemHolders.add(itemHolder);

        //把观察者添加到观察者集合中
        DownLoadManager.getInstance().addObserver(itemHolder);

        return itemHolder;
    }

    /**
     * 覆写该方法,决定当前类,有加载更多
     *
     * @return
     */
    @Override
    public boolean hasLoadMore() {
        return true;
    }

    /**
     * 覆写该方法,处理普通条目的点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onNormalitemClick(AdapterView<?> parent, View view, int position, long id) {
        //data
        ItemBean itemBean = (ItemBean) mDataSets.get(position);
//        Toast.makeText(UIUtils.getContext(), itemBean.name, Toast.LENGTH_SHORT).show();

        //intent跳转
        Intent intent = new Intent(UIUtils.getContext(), DetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //传值
        intent.putExtra("packageName", itemBean.packageName);
        UIUtils.getContext().startActivity(intent);
    }
}
