package com.itheima.googleplay.holder;

import android.view.View;
import android.widget.TextView;

import com.itheima.googleplay.R;
import com.itheima.googleplay.utils.UIUtils;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      1.提供视图
 * 描述	      2.接收数据
 * 描述	      3.数据和视图的绑定
 */
public class HomeHolderBackUP {
    public View mHolderView;//view
    public String mData;//model


    private TextView mTvTmp1;
    private TextView mTvTmp2;

    public HomeHolderBackUP() {
        //初始化根视图
        mHolderView = initHolderView();
        //找一个符合条件的holder,绑定在自己身上
        mHolderView.setTag(this);
    }

    /**
     * @return
     * @des 初始化holderView, 决定所能提供的视图长什么样子
     * @called HomeHolder一旦创建的时候
     */
    private View initHolderView() {
        View itemView = View.inflate(UIUtils.getContext(), R.layout.item_temp, null);
        //初始化孩子对象
        mTvTmp1 = (TextView) itemView.findViewById(R.id.tmp_tv_1);
        mTvTmp2 = (TextView) itemView.findViewById(R.id.tmp_tv_2);
        return itemView;
    }

    /**
     * @param data
     * @des 1.接收数据
     * @des 2.数据和视图的绑定
     */
    public void setDataAndRefreshHolderView(String data) {
        //保存数据到成员变量
        mData = data;
        //进行数据的视图的绑定
        refreshHolderView(data);
    }

    /**
     * @param data
     * @des 数据和视图的绑定操作
     */
    private void refreshHolderView(String data) {
        //view-->成员变量
        //data-->局部变量有,成员变量里面也有
        //data+view
        mTvTmp1.setText("我是头-" + data);
        mTvTmp2.setText("我是尾巴-" + data);
    }
}
