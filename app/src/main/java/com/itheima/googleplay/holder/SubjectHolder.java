package com.itheima.googleplay.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.bean.SubjectBean;
import com.itheima.googleplay.conf.Constants;
import com.itheima.googleplay.utils.UIUtils;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class SubjectHolder extends BaseHolder<SubjectBean> {
    @InjectView(R.id.item_subject_iv_icon)
    ImageView mItemSubjectIvIcon;
    @InjectView(R.id.item_subject_tv_title)
    TextView mItemSubjectTvTitle;

    /**
     * 决定holder所能提供的视图
     *
     * @return
     */
    @Override
    public View initHolderView() {
        View holderView = View.inflate(UIUtils.getContext(), R.layout.item_subject, null);
        //找出孩子
        ButterKnife.inject(this, holderView);
        return holderView;
    }

    @Override
    public void refreshHolderView(SubjectBean data) {
        mItemSubjectTvTitle.setText(data.des);
        Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + data.url).into(mItemSubjectIvIcon);
    }
}
