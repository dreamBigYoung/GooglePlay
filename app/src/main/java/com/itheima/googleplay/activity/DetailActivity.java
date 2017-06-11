package com.itheima.googleplay.activity;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.LoadingPager;
import com.itheima.googleplay.bean.ItemBean;
import com.itheima.googleplay.holder.DetailDesHolder;
import com.itheima.googleplay.holder.DetailDownloadHolder;
import com.itheima.googleplay.holder.DetailInfoHolder;
import com.itheima.googleplay.holder.DetailPicHolder;
import com.itheima.googleplay.holder.DetailSafeHolder;
import com.itheima.googleplay.manager.DownLoadInfo;
import com.itheima.googleplay.manager.DownLoadManager;
import com.itheima.googleplay.protocol.DetailProtocol;
import com.itheima.googleplay.utils.UIUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      视图的展示-->有加载中视图,成功视图,错误视图,空视图-->LoadingPager
 * 描述        网络请求-->异步-->BaseProtocol
 */
public class DetailActivity extends AppCompatActivity {

    @InjectView(R.id.detail_fl_download)
    FrameLayout mDetailFlDownload;
    @InjectView(R.id.detail_fl_info)
    FrameLayout mDetailFlInfo;
    @InjectView(R.id.detail_fl_safe)
    FrameLayout mDetailFlSafe;
    @InjectView(R.id.detail_fl_pic)
    FrameLayout mDetailFlPic;
    @InjectView(R.id.detail_fl_des)
    FrameLayout mDetailFlDes;
    private String mPackageName;
    private LoadingPager mLoadingPager;
    private ItemBean mItemBean;
    private DetailDownloadHolder mDetailDownloadHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadingPager = new LoadingPager(this) {
            @Override
            public LoadedResult initData() {
                return DetailActivity.this.initData();
            }

            @Override
            public View initSuccessView() {
                return DetailActivity.this.initSuccessView();
            }
        };
        setContentView(mLoadingPager);
        init();
        initActionBar();
        triggerLoadData();
    }

    public void initActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        mPackageName = getIntent().getStringExtra("packageName");
        Toast.makeText(getApplicationContext(), mPackageName, Toast.LENGTH_SHORT).show();
    }

    /**
     * 触发加载详情里面的数据
     */
    private void triggerLoadData() {
        mLoadingPager.triggerLoadData();
    }

    /**
     * 在子线程中真正的加载详情页面的数据
     *
     * @return
     */
    private LoadingPager.LoadedResult initData() {
//        SystemClock.sleep(2000);

        DetailProtocol protocol = new DetailProtocol(mPackageName);
        try {
            mItemBean = protocol.loadData(0);
            if (mItemBean != null) {
                return LoadingPager.LoadedResult.SUCCESS;
            } else {
                return LoadingPager.LoadedResult.EMPTY;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }
    }

    /**
     * 决定成功视图是什么
     * 成功视图和数据的绑定
     *
     * @return
     */
    private View initSuccessView() {

        //view-->successView-->复杂视图
        View successView = View.inflate(UIUtils.getContext(), R.layout.layout_detail, null);
        //找孩子
        ButterKnife.inject(this, successView);
        //data-->成员变量
        //data+view
//        往应用的信息部分这个容器里面添加内容
        DetailInfoHolder detailInfoHolder = new DetailInfoHolder();
        View holderView = detailInfoHolder.mHolderView;

        ViewCompat.animate(holderView).rotationX(360)
                .setInterpolator(new OvershootInterpolator(4))
                .setDuration(1000)
                .start();

        mDetailFlInfo.addView(holderView);
        detailInfoHolder.setDataAndRefreshHolderView(mItemBean);

//        往应用的安全部分这个容器里面添加内容
        DetailSafeHolder detailSafeHolder = new DetailSafeHolder();
        mDetailFlSafe.addView(detailSafeHolder.mHolderView);
        detailSafeHolder.setDataAndRefreshHolderView(mItemBean);

//        往应用的截图部分这个容器里面添加内容
        DetailPicHolder detailPicHolder = new DetailPicHolder();
        mDetailFlPic.addView(detailPicHolder.mHolderView);
        detailPicHolder.setDataAndRefreshHolderView(mItemBean);


//        往应用的描述部分这个容器里面添加内容
        DetailDesHolder detailDesHolder = new DetailDesHolder();
        mDetailFlDes.addView(detailDesHolder.mHolderView);
        detailDesHolder.setDataAndRefreshHolderView(mItemBean);

//        往应用的下载部分这个容器里面添加内容
        mDetailDownloadHolder = new DetailDownloadHolder();
        mDetailFlDownload.addView(mDetailDownloadHolder.mHolderView);
        mDetailDownloadHolder.setDataAndRefreshHolderView(mItemBean);

        //添加观察者到观察者集合中
        DownLoadManager.getInstance().addObserver(mDetailDownloadHolder);

        return successView;
    }

    @Override
    protected void onResume() {
        //动态的添加观察者到观察者集合中
        if (mDetailDownloadHolder != null) {
            DownLoadManager.getInstance().addObserver(mDetailDownloadHolder);

            //手动发布最新的DownLoadInfo
            DownLoadInfo downLoadInfo  = DownLoadManager.getInstance().getDownLoadInfo(mItemBean);
            DownLoadManager.getInstance().notifyObservers(downLoadInfo);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        //动态从观察者集合中移除观察者
        if (mDetailDownloadHolder != null) {
            DownLoadManager.getInstance().deleteObserver(mDetailDownloadHolder);
        }
        super.onPause();
    }
}
