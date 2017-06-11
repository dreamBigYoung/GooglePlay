package com.itheima.googleplay.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.bean.ItemBean;
import com.itheima.googleplay.conf.Constants;
import com.itheima.googleplay.utils.UIUtils;
import com.itheima.googleplay.views.RatioLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class DetailPicHolder extends BaseHolder<ItemBean> {


    @InjectView(R.id.app_detail_pic_iv_container)
    LinearLayout mAppDetailPicIvContainer;

    @Override
    public View initHolderView() {
        View holderView = View.inflate(UIUtils.getContext(), R.layout.item_detail_pic, null);
        //找孩子
        ButterKnife.inject(this, holderView);
        return holderView;
    }

    @Override
    public void refreshHolderView(ItemBean data) {
        //往mAppDetailPicIvContainer容器添加内容
        List<String> screenUrls = data.screen;
        for (int i = 0; i < screenUrls.size(); i++) {
            String url = screenUrls.get(i);
            ImageView iv = new ImageView(UIUtils.getContext());
            //图片的加载
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + url).into(iv);

            //创建ratioLayout
            RatioLayout rl = new RatioLayout(UIUtils.getContext());
            rl.setRelative(RatioLayout.RELATIVE_WIDTH);
            rl.setPicRatio((float) 150 / 250);

            //添加图片到ratioLayout中
            rl.addView(iv);


            //宽度已知-->屏幕的1/3
            int screenWidth = UIUtils.getResources().getDisplayMetrics().widthPixels;
            screenWidth = screenWidth-UIUtils.dip2Px(18);
            int width = screenWidth / 3;


            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            if (i != 0) {
                params.leftMargin = UIUtils.dip2Px(4);
            }
            mAppDetailPicIvContainer.addView(rl, params);
        }
    }
}
