package com.itheima.googleplay.holder;

import android.view.View;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.utils.UIUtils;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class LeftMenuHolder extends BaseHolder<Object> {
    @Override
    public View initHolderView() {
        View holderView = View.inflate(UIUtils.getContext(), R.layout.menu_view, null);
        return holderView;
    }

    @Override
    public void refreshHolderView(Object data) {

    }
}
