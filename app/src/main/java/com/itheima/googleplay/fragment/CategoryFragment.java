package com.itheima.googleplay.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.itheima.googleplay.base.BaseFragment;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.base.LoadingPager;
import com.itheima.googleplay.base.SuperBaseAdapter;
import com.itheima.googleplay.bean.CategoryInfoBean;
import com.itheima.googleplay.factory.ListViewFactory;
import com.itheima.googleplay.holder.CategoryNormalHolder;
import com.itheima.googleplay.holder.CategoryTitleHolder;
import com.itheima.googleplay.protocol.CategoryProtocol;
import com.itheima.googleplay.utils.LogUtils;

import java.util.List;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class CategoryFragment extends BaseFragment {

    private List<CategoryInfoBean> mDatas;

    /**
     * @des 在子线程中真正的加载具体的数据
     * @called triggerLoadData()方法被调用的时候
     */
    @Override
    public LoadingPager.LoadedResult initData() {
        CategoryProtocol protocol = new CategoryProtocol();
        try {
            mDatas = protocol.loadData(0);
            LogUtils.printList(mDatas);
            return checkResult(mDatas);
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
        //data-->成员变量
        //data+view
        listView.setAdapter(new CategoryAdapter(mDatas, listView));

        return listView;
    }

    class CategoryAdapter extends SuperBaseAdapter<CategoryInfoBean> {

        public CategoryAdapter(List<CategoryInfoBean> dataSets, AbsListView absListView) {
            super(dataSets, absListView);
        }

        @Override
        public BaseHolder getSpecialBaseHolder(int position) {
            CategoryInfoBean itemBean = mDatas.get(position);
            if (itemBean.isTitle) {
                return new CategoryTitleHolder();
            } else {
                return new CategoryNormalHolder();
            }
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;//2+1=3
        }


        /*--------------- 实现方案一 ---------------*/
      /*  @Override
        public int getItemViewType(int position) {
            CategoryInfoBean itemBean = mDatas.get(position);
            if (position == getCount() - 1) {
                return VIEWTYPE_LOADMORE;//加载更多-->0
            } else {
                if (itemBean.isTitle) {
                    return 1;
                } else {
                    return 2;
                }
            }
        }*/

        /**
         * 覆写getNormalItemViewType
         */
        @Override
        public int getNormalItemViewType(int position) {
            CategoryInfoBean itemBean = mDatas.get(position);
            if (itemBean.isTitle) {
                return 1;
            } else {
                return 2;
            }
        }
    }

}
