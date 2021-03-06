package com.itheima.googleplay.holder;

import android.view.View;
import android.widget.TextView;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.utils.UIUtils;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      1.提供视图
 * 描述	      2.接收数据
 * 描述	      3.数据和视图的绑定
 */
public class HomeHolderBackUpBackUp extends BaseHolder<String> {
    private TextView mTvTmp1;
    private TextView mTvTmp2;

    /**
     * @return
     * @des 初始化holderView, 决定所能提供的视图长什么样子
     * @called HomeHolder一旦创建的时候
     */
    @Override
    public View initHolderView() {
        View itemView = View.inflate(UIUtils.getContext(), R.layout.item_temp, null);
        //找出孩子对象
        mTvTmp1 = (TextView) itemView.findViewById(R.id.tmp_tv_1);
        mTvTmp2 = (TextView) itemView.findViewById(R.id.tmp_tv_2);
        return itemView;
    }

    /**
     * @param data
     * @des 数据和视图的绑定操作
     */
    @Override
    public void refreshHolderView(String data) {
        //view-->成员变量
        //data-->局部变量,基类还有
        //data+view
        mTvTmp1.setText("我是头-" + data);
        mTvTmp2.setText("我是尾巴-" + data);
    }
}
