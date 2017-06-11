package com.itheima.googleplay.fragment;

import android.os.SystemClock;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.itheima.googleplay.base.BaseFragment;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.base.LoadingPager;
import com.itheima.googleplay.base.SuperBaseAdapter;
import com.itheima.googleplay.bean.SubjectBean;
import com.itheima.googleplay.factory.ListViewFactory;
import com.itheima.googleplay.holder.SubjectHolder;
import com.itheima.googleplay.protocol.SubjectProtocol;
import com.itheima.googleplay.utils.UIUtils;

import java.util.List;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class SubjectFragment extends BaseFragment {

    private SubjectProtocol mProtocol;
    private List<SubjectBean> mDatas;

    /**
     * @des 在子线程中真正的加载具体的数据
     * @called triggerLoadData()方法被调用的时候
     */
    @Override
    public LoadingPager.LoadedResult initData() {
        mProtocol = new SubjectProtocol();
        try {
            mDatas = mProtocol.loadData(0);
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
        //view-->listview
        ListView listView = ListViewFactory.createListView();
        //data-->成员变量

        //data+view
        listView.setAdapter(new SubjectAdapter(mDatas, listView));
        return listView;
    }

    class SubjectAdapter extends SuperBaseAdapter<SubjectBean> {

        public SubjectAdapter(List<SubjectBean> dataSets, AbsListView absListView) {
            super(dataSets, absListView);
        }

        @Override
        public BaseHolder getSpecialBaseHolder(int position) {
            return new SubjectHolder();
        }

        /**
         * 有加载更多
         */
        @Override
        public boolean hasLoadMore() {
            return true;
        }

        /**
         * 具体加载更多
         */
        @Override
        public List onLoadMore() throws Exception {
            SystemClock.sleep(2000);
            List<SubjectBean> subjectBeans = mProtocol.loadData(mDatas.size());
            return subjectBeans;
        }

        /**
         * 条目的点击事件
         *
         * @param parent
         * @param view
         * @param position
         * @param id
         */
        @Override
        public void onNormalitemClick(AdapterView<?> parent, View view, int position, long id) {
            //itemBean
            SubjectBean subjectBean = mDatas.get(position);
            Toast.makeText(UIUtils.getContext(), subjectBean.des, Toast.LENGTH_SHORT).show();
        }
    }

}
