package com.itheima.googleplay.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.itheima.googleplay.factory.ThreadPoolProxyFactory;
import com.itheima.googleplay.holder.LoadMoreHolder;
import com.itheima.googleplay.utils.LogUtils;

import java.util.List;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      针对MyBaseAdapter里面的getView方法
 * 描述	      在getView方法中引入了BaseHolder这个类
 */
public abstract class SuperBaseAdapter<ITEMBEANTYPE> extends MyBaseAdapter implements AdapterView.OnItemClickListener {
    public static final int VIEWTYPE_LOADMORE = 0;
    public static final int VIEWTYPE_NORMAL = 1;
    private LoadMoreHolder mLoadMoreHolder;
    private LoadMoreTask mLoadMoreTask;
    private AbsListView mAbsListView;
    private int mState;

    public SuperBaseAdapter(List<ITEMBEANTYPE> dataSets, AbsListView absListView) {
        super(dataSets);
        mAbsListView = absListView;
        mAbsListView.setOnItemClickListener(this);
    }

    /*
           ListView中显示几种ViewType如何做?
           1.覆写2个方法
           2.在getView中分别处理
        */
    /*
        get(得到)ViewType(ViewType)Count(总数),默认是1钟类型
     */
    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;//1(普通类型)+1(加载更多) = 2
    }

    /*
    get(得到)Item(指定条目)ViewType(ViewType类型)(int position),默认是0
    0
    to
    getViewTypeCount() - 1.
     */
    @Override
    public int getItemViewType(int position) {
        if (position == getCount() - 1) {
            return VIEWTYPE_LOADMORE;
        } else {
//            return VIEWTYPE_NORMAL;
            return getNormalItemViewType(position);
        }
    }

    /**
     * 得到普通条目的ViewType类型
     * 子类可以覆写该方法,返回更多的普通条目的viewType类型
     *
     * @param position
     * @return
     */
    public int getNormalItemViewType(int position) {
        return VIEWTYPE_NORMAL;//默认值是一
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;//其实就是加的加载更多的条目
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         /*--------------- 决定根布局(itemView) ---------------*/
        BaseHolder holder = null;
        //当前条目的ViewType类型判断
        int curViewType = getItemViewType(position);
        if (convertView == null) {
            if (curViewType == VIEWTYPE_LOADMORE) {//当前的条目是加载更多类型
                holder = getLoadMoreHolder();
            } else {//当前的条目是普通的类型
                //创建holer对象
                holder = getSpecialBaseHolder(position);
            }
        } else {
            holder = (BaseHolder) convertView.getTag();
        }
            /*--------------- 得到数据,然后绑定数据 ---------------*/
        if (curViewType == VIEWTYPE_LOADMORE) {//当前的条目是加载更多类型
            if (hasLoadMore()) {
                //显示正在加载更多的视图
                mLoadMoreHolder.setDataAndRefreshHolderView(LoadMoreHolder.LOADMORE_LOADING);

                //触发加载更多的数据
                triggerLoadMoreData();

            } else {
                //隐藏加载更多的视图,以及重试视图
                mLoadMoreHolder.setDataAndRefreshHolderView(LoadMoreHolder.LOADMORE_NONE);
            }
        } else {
            Object data = (ITEMBEANTYPE) mDataSets.get(position);
            holder.setDataAndRefreshHolderView(data);
        }
        LogUtils.s(holder.mHolderView.toString());

//        View holderView = holder.mHolderView;
//
//        holderView.setScaleX(0.6f);
//        holderView.setScaleY(0.5f);
//
//        ViewCompat.animate(holderView).scaleX(1).scaleY(1).setDuration(400).
//                setInterpolator(new OvershootInterpolator(4)).start();

        return holder.mHolderView;//其实这个convertView是经过了数据绑定的convertView
    }


    /**
     * @return
     * @des 是否有加载更多, 默认没有加载更多
     * @des 子类可以覆写该方法, 可以决定有加载跟多
     */
    public boolean hasLoadMore() {
        return false;//默认没有加载更多
    }

    /**
     * 触发加载更多的数据
     */
    private void triggerLoadMoreData() {
        if (mLoadMoreTask == null) {
            LogUtils.s("###triggerLoadMoreData");

            //加载之前显示正在加载更多
            int state = LoadMoreHolder.LOADMORE_LOADING;
            mLoadMoreHolder.setDataAndRefreshHolderView(state);


            //异步加载
            mLoadMoreTask = new LoadMoreTask();
            ThreadPoolProxyFactory.getNormalThreadPoolProxy().submit(mLoadMoreTask);
        }

    }


    class LoadMoreTask implements Runnable {
        private static final int PAGESIZE = 20;//每页请求的总数

        @Override
        public void run() {
            /*--------------- 定义刷新ui需要用到的两个值 ---------------*/
            List loadMoreList = null;

            /*--------------- 真正的在子线程中加载更多的数据,得到数据,处理数据 ---------------*/
            try {
                loadMoreList = onLoadMore();
                //处理数据
                if (loadMoreList == null) {
                    mState = LoadMoreHolder.LOADMORE_NONE;//没有加载更多
                } else {
                    if (loadMoreList.size() == PAGESIZE) {
                        //有加载更多
                        mState = LoadMoreHolder.LOADMORE_LOADING;//mLoadMoreHolder显示就是正在加载更多-->用户下一次看到的就是正在加载更多
                    } else {
                        //没有加载更多
                        mState = LoadMoreHolder.LOADMORE_NONE;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                mState = LoadMoreHolder.LOADMORE_ERROR;//加载更多失败
            }

            /*--------------- 生成了两个临时变量 ---------------*/
            final List finalLoadMoreList = loadMoreList;
            final int finalState = mState;

            /*--------------- 具体刷新ui ---------------*/
            MyApplication.getMainThreadHandler().post(new Runnable() {
                @Override
                public void run() {
                    //刷新ui-->ListView-->修改数据集(mDataSets.add(loadMoreList))-->adapter.notifyDataSetChanged();
                    if (finalLoadMoreList != null) {
                        mDataSets.addAll(finalLoadMoreList);
                        notifyDataSetChanged();
                    }
                    //刷新ui-->mLoadMoreHolder-->mLoadMoreHolder.setDataAndRefreshHolder(curState);
                    mLoadMoreHolder.setDataAndRefreshHolderView(finalState);
                }
            });

            //代表走到run方法体的最后了,任务已经执行完成了,置空任务
            mLoadMoreTask = null;
        }


    }

    /**
     * @return
     * @throws Exception 加载更多过程中,出现了异常
     * @des 在子线中真正的加载更多的数据
     * @des 在SuperBaseAdapter中不知道如何加载更多的数据, 只能交给子类
     * @des 子类是选择性实现, 只有子类有加载更多的时候才覆写该方法, 完成具体加载更多
     */
    public List onLoadMore() throws Exception {
        return null;//默认是null
    }

    /**
     * @return
     * @des 属于BaseHolder的子类对象
     * @des 加载更多的Holder的对象
     */
    private LoadMoreHolder getLoadMoreHolder() {
        if (mLoadMoreHolder == null) {
            mLoadMoreHolder = new LoadMoreHolder();
        }
        return mLoadMoreHolder;
    }

    /**
     * @param position
     * @return
     * @des 得到BaseHolder具体的子类对象
     * @des 在SuperBaseAdapter中不知道如何创建BaseHolder的子类对象, 所以只能交给子类, 子类必须实现
     * @des 必须实现, 但是不知道具体实现, 定义成为抽象方法, 交给子类具体实现
     */
    public abstract BaseHolder getSpecialBaseHolder(int position);

    /*--------------- 处理条目的点击事件 ---------------*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //处理position
        if (mAbsListView instanceof ListView) {
            position = position - ((ListView) mAbsListView).getHeaderViewsCount();
        }

        int curViewType = getItemViewType(position);
        if (curViewType == VIEWTYPE_LOADMORE) {
            if (mState == LoadMoreHolder.LOADMORE_ERROR) {
                //再次触发加载更多
                triggerLoadMoreData();
            }
        } else {//点击了普通条目
            onNormalitemClick(parent, view, position, id);
        }
    }

    /**
     * @param parent
     * @param view
     * @param position
     * @param id
     * @des 普通条目的点击事件
     * @des 在SuperBaseAdapter中不知道如何处理普通条目的点击事件, 只能交给子类
     * @des 子类是选择性实现普通条目的点击事件
     */
    public void onNormalitemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
