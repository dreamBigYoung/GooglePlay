package com.itheima.googleplay.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.itheima.googleplay.utils.UIUtils;

import java.util.List;
import java.util.Map;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      谷歌市场里面,对Fragment一个抽取
 */
public abstract class BaseFragment extends Fragment {

    private LoadingPager mLoadingPager;

    public LoadingPager getLoadingPager() {
        return mLoadingPager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mLoadingPager == null) {
            mLoadingPager = new LoadingPager(UIUtils.getContext()) {
                /**
                 * @des 在子线程中真正的加载具体的数据
                 * @called triggerLoadData()方法被调用的时候
                 */
                @Override
                public LoadedResult initData() {
                    return BaseFragment.this.initData();
                }

                /**
                 * @return
                 * @des 决定成功视图长什么样子(需要定义成功视图)
                 * @des 数据和视图的绑定过程
                 * @called triggerLoadData()方法被调用, 而且数据加载完成了, 而且数据加载成功
                 */
                @Override
                public View initSuccessView() {
                    return BaseFragment.this.initSuccessView();
                }
            };
        }

        //低版本兼容
        ViewParent parent = mLoadingPager.getParent();
        if(parent!=null&&parent instanceof  ViewGroup){
            ((ViewGroup)parent).removeView(mLoadingPager);
        }

        //触发加载数据
//        mLoadingPager.triggerLoadData();
        return mLoadingPager;//4种视图中的一种(加载中,错误,空,成功)
    }


    /**
     * @des 在子线程中真正的加载具体的数据
     * @des 在BaseFragent不知道如何具体的加载数据, 只能交给子类, 子类必须实现
     * @des 必须实现, 但是不知道具体实现, 定义成为抽象方法, 交给子类具体实现
     * @called triggerLoadData()方法被调用的时候
     */
    public abstract LoadingPager.LoadedResult initData();

    /**
     * @return
     * @des 决定成功视图长什么样子(需要定义成功视图)
     * @des 数据和视图的绑定过程
     * @des 在BaseFragment中不知道成功视图具体是啥, 不知道具体数据是啥, 不知道数据和视图如何绑定, 交给子类, 子类必须实现
     * @des 必须实现, 但是不知道具体实现, 定义成为抽象方法交给子类具体实现
     * @called triggerLoadData()方法被调用, 而且数据加载完成了, 而且数据加载成功
     */
    public abstract View initSuccessView();
    /*
     针对BaseFragment提供的视图进行分析
     1.针对BaseFragment的子类,其实视图展示的情况/类型如下
         只会出现4种视图类型(加载中的视图,错误视图,空视图,成功视图)
     2.针对4种视图类型,由如下特定
        发现其中加载中的视图,错误视图,空视图属于静态视图,无需数据绑定-->只需在xml中定义即可
        发现其中成功视图,属于动态的视图-->它的具体展示是不同的.到时候需要具体实现,具体决定

    多种-->2种以上
	coni  加载中
	coni  错误
	coni  空
	coni  成功
	用一个变量记录当前的情况 curState = 加载中
	记录curState变化-->刷新ui
	错误视图,空视图,成功视图什么情况下,才可能被显示?
	状态在什么时候才会由默认状态(加载中状态)发生变化?
	        ==>数据加载

     */

    /*
    针对BaseFragment进行数据加载的分析
    数据加载的基类流程?
        1.触发加载          =====>加载中的视图
            进入页面开始下载
            下拉刷新
            上滑加载更多
            点击重试
        2.异步加载-->得到数据   ==>加载中的视图
        3.处理数据-->得到自己最终想要的数据
        4.刷新UI
            请求失败==>错误视图
            请求成功,但是想要的数据为空===>空视图
            请求成功,得到了想要的数据===>成功视图


     */

    /**
     * @param resObj
     * @return
     * @des 校验请求回来的数据
     */
    public LoadingPager.LoadedResult checkResult(Object resObj) {
        if (resObj == null) {
            return LoadingPager.LoadedResult.EMPTY;
        }
        //resObj -->List
        if (resObj instanceof List) {
            if (((List) resObj).size() == 0) {
                return LoadingPager.LoadedResult.EMPTY;
            }
        }
        //resObj -->Map
        if (resObj instanceof Map) {
            if (((Map) resObj).size() == 0) {
                return LoadingPager.LoadedResult.EMPTY;
            }
        }
        return LoadingPager.LoadedResult.SUCCESS;
    }
}
