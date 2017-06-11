package com.itheima.googleplay.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.bean.ItemBean;
import com.itheima.googleplay.conf.Constants;
import com.itheima.googleplay.utils.UIUtils;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class DetailSafeHolder extends BaseHolder<ItemBean> implements View.OnClickListener {


    @InjectView(R.id.app_detail_safe_iv_arrow)
    ImageView mAppDetailSafeIvArrow;

    @InjectView(R.id.app_detail_safe_pic_container)
    LinearLayout mAppDetailSafePicContainer;

    @InjectView(R.id.app_detail_safe_des_container)
    LinearLayout mAppDetailSafeDesContainer;

    private boolean isOpen = true;

    @Override
    public View initHolderView() {
        View holderView = View.inflate(UIUtils.getContext(), R.layout.item_detail_safe, null);
        //找出孩子
        ButterKnife.inject(this, holderView);

        holderView.setOnClickListener(this);
        return holderView;
    }

    @Override
    public void refreshHolderView(ItemBean data) {

        List<ItemBean.ItemSafeBean> itemSafeBeans = data.safe;
        for (int i = 0; i < itemSafeBeans.size(); i++) {
            ItemBean.ItemSafeBean itemSafeBean = itemSafeBeans.get(i);
            String safeDes = itemSafeBean.safeDes;
            int safeDesColor = itemSafeBean.safeDesColor;
            String safeDesUrl = itemSafeBean.safeDesUrl;
            String safeUrl = itemSafeBean.safeUrl;


            /*---------------  往mAppDetailSafePicContainer 容器动态加载孩子 ---------------*/
            ImageView ivIcon = new ImageView(UIUtils.getContext());
            //图片的加载
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + safeUrl).into(ivIcon);
            mAppDetailSafePicContainer.addView(ivIcon);

            /*---------------  往mAppDetailSafeDesContainer 容器动态加载孩子 ---------------*/
            LinearLayout line = new LinearLayout(UIUtils.getContext());
            ImageView ivDesIcon = new ImageView(UIUtils.getContext());
            TextView tvDesNote = new TextView(UIUtils.getContext());

            //设置单行效果
            tvDesNote.setSingleLine(true);

            //设置数据
            tvDesNote.setText(safeDes);
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + safeDesUrl).into(ivDesIcon);

            if (safeDesColor == 0) {
                tvDesNote.setTextColor(UIUtils.getColor(R.color.app_detail_safe_normal));
            } else {
                tvDesNote.setTextColor(UIUtils.getColor(R.color.app_detail_safe_warning));
            }

            line.addView(ivDesIcon);
            line.addView(tvDesNote);

            mAppDetailSafeDesContainer.addView(line);

        }
        //默认折叠mAppDetailSafeDesContainer
        changeSafeDesContainerHeight(false);
    }

    @Override
    public void onClick(View v) {
        changeSafeDesContainerHeight(true);
    }

    /**
     * 修改SafeDesContainer高度
     *
     * @param isAnimation true:动画的方式修改高度 false:直接修改高度
     */
    private void changeSafeDesContainerHeight(boolean isAnimation) {
        if (isOpen) {
            //折叠  mAppDetailSafeDesContainer 高度  应有的高度-->0
            int start = mAppDetailSafeDesContainer.getMeasuredHeight();
            int end = 0;
//            Toast.makeText(UIUtils.getContext(), start + "", Toast.LENGTH_SHORT).show();
            if (isAnimation) {
                doAnimation(start, end);
            } else {
                ViewGroup.LayoutParams layoutParams = mAppDetailSafeDesContainer.getLayoutParams();
                layoutParams.height = end;

                //重新设置layoutParams
                mAppDetailSafeDesContainer.setLayoutParams(layoutParams);
            }

        } else {
            //展开 mAppDetailSafeDesContainer 高度  0-->应有的高度
            mAppDetailSafeDesContainer.measure(0, 0);
            int end = mAppDetailSafeDesContainer.getMeasuredHeight();
            int start = 0;
            if (isAnimation) {
                doAnimation(start, end);
            } else {
                ViewGroup.LayoutParams layoutParams = mAppDetailSafeDesContainer.getLayoutParams();
                layoutParams.height = end;

                //重新设置layoutParams
                mAppDetailSafeDesContainer.setLayoutParams(layoutParams);
            }
        }
        isOpen = !isOpen;
    }

    private void doAnimation(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.start();

        //得到动画执行过程中的渐变值
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int tempHeight = (int) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = mAppDetailSafeDesContainer.getLayoutParams();
                layoutParams.height = tempHeight;

                //重新设置layoutParams
                mAppDetailSafeDesContainer.setLayoutParams(layoutParams);
            }
        });
        //箭头跟着旋转
        if (isOpen) {
            ObjectAnimator.ofFloat(mAppDetailSafeIvArrow, "rotation", 180, 0).start();
        } else {
            ObjectAnimator.ofFloat(mAppDetailSafeIvArrow, "rotation", 0, 180).start();
        }
    }
}
