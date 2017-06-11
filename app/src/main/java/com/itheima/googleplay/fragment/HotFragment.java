package com.itheima.googleplay.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseFragment;
import com.itheima.googleplay.base.LoadingPager;
import com.itheima.googleplay.protocol.HotProtocol;
import com.itheima.googleplay.utils.UIUtils;
import com.itheima.googleplay.views.FlowLayout;

import java.util.List;
import java.util.Random;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class HotFragment extends BaseFragment {

    private List<String> mDatas;

    /**
     * @des 在子线程中真正的加载具体的数据
     * @called triggerLoadData()方法被调用的时候
     */
    @Override
    public LoadingPager.LoadedResult initData() {
        HotProtocol protocol = new HotProtocol();
        try {
            mDatas = protocol.loadData(0);
            return checkResult(mDatas);
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }
    }

    /**
     * @return
     * @des 决定成功视图长什么样子(需要定义成功视图)
     * @des 数据和视图的绑定过程
     * @called triggerLoadData()方法被调用, 而且数据加载完成了, 而且数据加载成功
     */
    @Override
    public View initSuccessView() {
        //view
        ScrollView scrollView = new ScrollView(UIUtils.getContext());
        FlowLayout flowLayout = new FlowLayout(UIUtils.getContext());

        for (int i = 0; i < mDatas.size(); i++) {
            //data
            final String data = mDatas.get(i);
            //view
            TextView tv = new TextView(UIUtils.getContext());
            //data+view
            tv.setText(data);

            //属性设置
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.WHITE);
            int padding = UIUtils.dip2Px(5);
            tv.setPadding(padding, padding, padding, padding);

            tv.setBackgroundResource(R.drawable.shape_hot_tv);

            //创建了一个默认情况的背景
            GradientDrawable normalBg = new GradientDrawable();
            normalBg.setCornerRadius(10);

            Random random = new Random();
            int alpha = 255;
            int red = random.nextInt(170) + 30;//30-200
            int green = random.nextInt(170) + 30;//30-200
            int blue = random.nextInt(170) + 30;//30-200
            int argb = Color.argb(alpha, red, green, blue);
            normalBg.setColor(argb);

            //创建一个按下去的背景
            GradientDrawable pressedBg = new GradientDrawable();
            pressedBg.setCornerRadius(10);
            pressedBg.setColor(Color.DKGRAY);

            //创建一个带有状态的背景
            StateListDrawable selectorBg = new StateListDrawable();
            selectorBg.addState(new int[]{-android.R.attr.state_pressed}, normalBg);
            selectorBg.addState(new int[]{android.R.attr.state_pressed}, pressedBg);
            tv.setBackgroundDrawable(selectorBg);


            tv.setClickable(true);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(), data, Toast.LENGTH_SHORT).show();
                }
            });


            flowLayout.addView(tv);

        }
        scrollView.addView(flowLayout);

        return scrollView;
    }

}
