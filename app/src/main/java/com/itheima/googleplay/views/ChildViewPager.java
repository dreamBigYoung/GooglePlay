package com.itheima.googleplay.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class ChildViewPager extends ViewPager {

    private float mDownX;
    private float mDownY;

    public ChildViewPager(Context context) {
        super(context);
    }

    public ChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*
        1.是否派发
            return true;-->不会来到onInterceptTouchEvent
            return false;-->不会来到onInterceptTouchEvent
            return super;-->来到onInterceptTouchEvent
            一般不修改它的的返回值
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /*
       2.是否拦截
            return true-->来到当前类的onTouchEvent方法中
            return false;-->事件传递给下一级
            return super;-->默认情况(当前类不做处理)
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    /*
        3.是否消费
            return true:消费事件-->事件终止了
            return false;不消费事件-->事件传递给上一级
            return super-->默认情况(当前类不做处理)

        什么时候会来到onTouchEvent中?
            1.当前控件拦截了事件
            2.事件传递给下一级之后,下一级拦截了事件,但是没有处理事件(事件没有被消费)
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();

                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getRawX();
                float moveY = ev.getRawY();
                int diffX = (int) (moveX - mDownX + .5f);
                int diffY = (int) (moveY - mDownY + .5f);
                if (Math.abs(diffX) > Math.abs(diffY)) {//水平滚动
                    //孩子处理事件-->申请父容器不拦截
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {//垂直滚动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            case MotionEvent.ACTION_UP:

                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
}
