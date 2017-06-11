package com.itheima.googleplay.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.itheima.googleplay.base.BaseFragment;
import com.itheima.googleplay.base.LoadingPager;
import com.itheima.googleplay.protocol.RecommendProtocol;
import com.itheima.googleplay.utils.UIUtils;
import com.itheima.googleplay.views.flyinout.ShakeListener;
import com.itheima.googleplay.views.flyinout.StellarMap;

import java.util.List;
import java.util.Random;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class RecommendFragment extends BaseFragment {

    private List<String> mDatas;
    private ShakeListener mShakeListener;

    /**
     * @des 在子线程中真正的加载具体的数据
     * @called triggerLoadData()方法被调用的时候
     */
    @Override
    public LoadingPager.LoadedResult initData() {
        RecommendProtocol protocol = new RecommendProtocol();
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
        final StellarMap stellarMap = new StellarMap(UIUtils.getContext());
        //data
        //data+view
        final RecommendAdapter adapter = new RecommendAdapter();
        stellarMap.setAdapter(adapter);

        //1.首页未展示
        stellarMap.setGroup(0, true);

        //2.每一页展示的条目数目不统一
        stellarMap.setRegularity(15, 20);

        //摇一摇切换
        mShakeListener = new ShakeListener(UIUtils.getContext());
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                //切换
                int currentGroup = stellarMap.getCurrentGroup();
                if (currentGroup == adapter.getGroupCount() - 1) {//最后一页
                    currentGroup = 0;
                } else {
                    currentGroup++;
                }
                //设置group切换
                stellarMap.setGroup(currentGroup, true);
            }
        });
        return stellarMap;
    }


    class RecommendAdapter implements StellarMap.Adapter {
        //没有多少个
        public static final int PAGESIZE = 15;

        @Override
        public int getGroupCount() {//一共多少组 32  15 15 2
            if (mDatas.size() % PAGESIZE == 0) {
                return mDatas.size() / PAGESIZE;
            } else {
                return mDatas.size() / PAGESIZE + 1;
            }
        }

        @Override
        public int getCount(int group) {//每组多少个
            if (mDatas.size() % PAGESIZE == 0) {
                return PAGESIZE;
            } else {//15 15 2
                if (group == getGroupCount() - 1) {//最后一页
                    return mDatas.size() % PAGESIZE;
                } else {
                    return PAGESIZE;
                }
            }
        }

        @Override
        public View getView(int group, int position, View convertView) {//返回具体的孩子


            //view
            TextView tv = new TextView(UIUtils.getContext());

            //随机大小
            Random random = new Random();
            tv.setTextSize(random.nextInt(5) + 12);//[12,16]

            int alpha = 255;
            int red = random.nextInt(170) + 30;//30-200
            int green = random.nextInt(170) + 30;//30-200
            int blue = random.nextInt(170) + 30;//30-200
            int color = Color.argb(alpha, red, green, blue);
            tv.setTextColor(color);

            //data
            int index = group * PAGESIZE + position;
            String data = mDatas.get(index);
            tv.setText(data);
            return tv;
        }

        @Override
        public int getNextGroupOnPan(int group, float degree) {
            return 0;
        }

        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            return 0;
        }
    }

    @Override
    public void onResume() {
        if (mShakeListener != null) {
            mShakeListener.resume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mShakeListener != null) {
            mShakeListener.pause();
        }
        super.onPause();
    }

}
