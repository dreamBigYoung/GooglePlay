package com.itheima.googleplay.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.bean.CategoryInfoBean;
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
public class CategoryNormalHolder extends BaseHolder<CategoryInfoBean> {


    @InjectView(R.id.item_category_icon_1)
    ImageView mItemCategoryIcon1;
    @InjectView(R.id.item_category_name_1)
    TextView mItemCategoryName1;
    @InjectView(R.id.item_category_item_1)
    LinearLayout mItemCategoryItem1;
    @InjectView(R.id.item_category_icon_2)
    ImageView mItemCategoryIcon2;
    @InjectView(R.id.item_category_name_2)
    TextView mItemCategoryName2;
    @InjectView(R.id.item_category_item_2)
    LinearLayout mItemCategoryItem2;
    @InjectView(R.id.item_category_icon_3)
    ImageView mItemCategoryIcon3;
    @InjectView(R.id.item_category_name_3)
    TextView mItemCategoryName3;
    @InjectView(R.id.item_category_item_3)
    LinearLayout mItemCategoryItem3;

    /**
     * 决定对应Holder所能提供的视图是啥
     *
     * @return
     */
    @Override
    public View initHolderView() {
        View holderView = View.inflate(UIUtils.getContext(), R.layout.item_category_normal, null);
        //找出孩子,转换成成员变量
        ButterKnife.inject(this, holderView);
        return holderView;
    }

    @Override
    public void refreshHolderView(CategoryInfoBean data) {
        /*mItemCategoryName1.setText(data.name1);
        mItemCategoryName2.setText(data.name2);
        mItemCategoryName3.setText(data.name3);

        Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + data.url1).into(mItemCategoryIcon1);
        Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + data.url2).into(mItemCategoryIcon2);
        Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + data.url3).into(mItemCategoryIcon3);*/

        refreshUI(data.name1, data.url1, mItemCategoryName1, mItemCategoryIcon1);
        refreshUI(data.name2, data.url2, mItemCategoryName2, mItemCategoryIcon2);
        refreshUI(data.name3, data.url3, mItemCategoryName3, mItemCategoryIcon3);
    }

    public void refreshUI(final String name, String url, TextView tvName, ImageView ivIcon) {
        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(url)) {
            ViewParent parent = tvName.getParent();
            ((ViewGroup) parent).setVisibility(View.INVISIBLE);
        } else {
            tvName.setText(name);
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + url).into(ivIcon);
            ViewParent parent = tvName.getParent();
            ((ViewGroup) parent).setVisibility(View.VISIBLE);
            //设置点击事件
            ((ViewGroup) parent).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(), name, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
