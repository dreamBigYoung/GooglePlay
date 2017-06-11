package com.itheima.googleplay.holder;

import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.bean.ItemBean;
import com.itheima.googleplay.utils.UIUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class DetailDesHolder extends BaseHolder<ItemBean> implements View.OnClickListener {


    @InjectView(R.id.app_detail_des_tv_des)
    TextView mAppDetailDesTvDes;
    @InjectView(R.id.app_detail_des_tv_author)
    TextView mAppDetailDesTvAuthor;
    @InjectView(R.id.app_detail_des_iv_arrow)
    ImageView mAppDetailDesIvArrow;
    private boolean isOpen = true;
    private int mMAppDetailDesTvDesHeight;
    private ItemBean mItemBean;

    @Override
    public View initHolderView() {
        View holderView = View.inflate(UIUtils.getContext(), R.layout.item_detail_des, null);
        //找孩子
        ButterKnife.inject(this, holderView);

        holderView.setOnClickListener(this);
        return holderView;
    }

    @Override
    public void refreshHolderView(ItemBean data) {
        //保存数据为成员变量
        mItemBean = data;

        mAppDetailDesTvAuthor.setText(data.author);
        mAppDetailDesTvDes.setText(data.des);

        mAppDetailDesTvDes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //默认折叠mAppDetailDesTvDes
                changeDetailDesHeight(false);
                mAppDetailDesTvDes.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    @Override
    public void onClick(View v) {
        changeDetailDesHeight(true);
    }

    /**
     * @param isAnimation true:有动画效果 false:没有动画效果
     * @des 修改mAppDetailDesTvDes高度
     */
    private void changeDetailDesHeight(boolean isAnimation) {
        if (mMAppDetailDesTvDesHeight == 0) {
            mMAppDetailDesTvDesHeight = mAppDetailDesTvDes.getMeasuredHeight();
        }

        if (isOpen) {
            //折叠 mAppDetailDesTvDes高度 应有的高度-->7行的高度

//            mAppDetailDesTvDes.measure(0,0);
            Toast.makeText(UIUtils.getContext(), mMAppDetailDesTvDesHeight + "", Toast.LENGTH_SHORT).show();

            int start = mMAppDetailDesTvDesHeight;
            int end = getShortLineHeight(7, mItemBean.des);//7行的高度
            if (isAnimation) {
                doAnimation(start, end);
            } else {
                mAppDetailDesTvDes.setHeight(end);
            }
        } else {
            //展开 mAppDetailDesTvDes高度 7行的高度-->应有的高度
            int start = getShortLineHeight(7, mItemBean.des);//7行的高度
            int end = mMAppDetailDesTvDesHeight;
            if (isAnimation) {
                doAnimation(start, end);
            } else {
                mAppDetailDesTvDes.setHeight(end);
            }
        }
        isOpen = !isOpen;
    }

    private void doAnimation(int start, int end) {
        ObjectAnimator animator = ObjectAnimator.ofInt(mAppDetailDesTvDes, "height", start, end);
        animator.start();
        //箭头跟着旋转
        if (isOpen) {
            ObjectAnimator.ofFloat(mAppDetailDesIvArrow, "rotation", 180, 0).start();
        } else {
            ObjectAnimator.ofFloat(mAppDetailDesIvArrow, "rotation", 0, 180).start();
        }

        //监听动画执行完成
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //动画结束-->找到外层scrollView-->完成自动滚动
                ViewParent parent = mAppDetailDesTvDes.getParent();//父容器
                while (true) {
                    parent = parent.getParent();//父容器-->父容器
                    if (parent instanceof ScrollView) {
                        ((ScrollView) parent).fullScroll(View.FOCUS_DOWN);
                        break;
                    }
                    if (parent == null) {
                        break;
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    /**
     * 得到指定行高,指定内容的textview的高度
     *
     * @param lineHeight
     * @param content
     * @return
     */
    private int getShortLineHeight(int lineHeight, String content) {
        TextView tempTextView = new TextView(UIUtils.getContext());
        tempTextView.setLines(lineHeight);
        tempTextView.setText(content);

        tempTextView.measure(0, 0);
        return tempTextView.getMeasuredHeight();
    }
}
