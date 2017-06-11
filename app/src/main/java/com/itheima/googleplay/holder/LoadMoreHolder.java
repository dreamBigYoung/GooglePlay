package com.itheima.googleplay.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.utils.UIUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class LoadMoreHolder extends BaseHolder<Integer> {
    public static final int LOADMORE_LOADING = 0;//正在加载更多
    public static final int LOADMORE_ERROR = 1;//加载更多失败,点击重试
    public static final int LOADMORE_NONE = 2;//没有加载更多

    @InjectView(R.id.item_loadmore_container_loading)
    LinearLayout mItemLoadmoreContainerLoading;
    @InjectView(R.id.item_loadmore_tv_retry)
    TextView mItemLoadmoreTvRetry;
    @InjectView(R.id.item_loadmore_container_retry)
    LinearLayout mItemLoadmoreContainerRetry;

    @Override
    public View initHolderView() {
        View holderView = View.inflate(UIUtils.getContext(), R.layout.item_loadmore, null);
        //找孩子
        ButterKnife.inject(this, holderView);
        return holderView;
    }

    @Override
    public void refreshHolderView(Integer curState) {
        //首先隐藏所有的视图
        mItemLoadmoreContainerLoading.setVisibility(View.GONE);
        mItemLoadmoreContainerRetry.setVisibility(View.GONE);
        switch (curState) {
            case LOADMORE_LOADING:
                mItemLoadmoreContainerLoading.setVisibility(View.VISIBLE);
                break;
            case LOADMORE_ERROR:
                mItemLoadmoreContainerRetry.setVisibility(View.VISIBLE);
                break;
            case LOADMORE_NONE:

                break;

            default:
                break;
        }

    }
    //传递进来的数据类型有什么用?-->决定ui的具体展现
}
