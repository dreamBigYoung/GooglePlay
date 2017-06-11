package com.itheima.googleplay.holder;

import android.view.View;
import android.widget.TextView;

import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.bean.CategoryInfoBean;
import com.itheima.googleplay.utils.UIUtils;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class CategoryTitleHolder extends BaseHolder<CategoryInfoBean> {

    private TextView mTvTitle;

    /**
     * 决定对应holder所能提供的视图是啥
     *
     * @return
     */
    @Override
    public View initHolderView() {
        mTvTitle = new TextView(UIUtils.getContext());
        int padding = UIUtils.dip2Px(5);
        mTvTitle.setPadding(padding, padding, padding, padding);
        return mTvTitle;
    }

    /**
     * 进行数据和视图的绑定
     *
     * @param data
     */
    @Override
    public void refreshHolderView(CategoryInfoBean data) {
        mTvTitle.setText(data.title);
    }
}
