package com.itheima.googleplay.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.base.MyApplication;
import com.itheima.googleplay.bean.ItemBean;
import com.itheima.googleplay.conf.Constants;
import com.itheima.googleplay.manager.DownLoadInfo;
import com.itheima.googleplay.manager.DownLoadManager;
import com.itheima.googleplay.utils.CommonUtils;
import com.itheima.googleplay.utils.PrintDownLoadInfo;
import com.itheima.googleplay.utils.StringUtils;
import com.itheima.googleplay.utils.UIUtils;
import com.itheima.googleplay.views.ProgressView;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      1.提供视图
 * 描述	      2.接收数据
 * 描述	      3.数据和视图的绑定
 * 描述	      根据不同状态,展示不同的ui
 * 描述	      根据不同状态,触发不同的操作
 */
public class ItemHolder extends BaseHolder<ItemBean> implements DownLoadManager.DownLoadInfoObserver{
    @InjectView(R.id.item_appinfo_iv_icon)
    ImageView mItemAppinfoIvIcon;
    @InjectView(R.id.item_appinfo_tv_title)
    TextView mItemAppinfoTvTitle;
    @InjectView(R.id.item_appinfo_rb_stars)
    RatingBar mItemAppinfoRbStars;
    @InjectView(R.id.item_appinfo_tv_size)
    TextView mItemAppinfoTvSize;
    @InjectView(R.id.item_appinfo_tv_des)
    TextView mItemAppinfoTvDes;

    @InjectView(R.id.item_appinfo_progressview)
    ProgressView mProgressView;
    private ItemBean mItemBean;

    /**
     * 决定所能提供的视图长什么样子
     *
     * @return
     */
    @Override
    public View initHolderView() {
        View itemView = View.inflate(UIUtils.getContext(), R.layout.item_home, null);
        //找出孩子,并转换成成员变量
        ButterKnife.inject(this, itemView);
        return itemView;
    }

    /**
     * 视图和数据具体绑定
     *
     * @param data
     */
    @Override
    public void refreshHolderView(ItemBean data) {
        //保存数据到成员变量
        mItemBean = data;

        //重置进度
        mProgressView.setProgress(0);


        //view-->成员变量
        //data-->局部变量,基类
        //data+view
        mItemAppinfoTvTitle.setText(data.name);
        mItemAppinfoTvSize.setText(StringUtils.formatFileSize(data.size));
        mItemAppinfoTvDes.setText(data.des);

        //ratingbar
        mItemAppinfoRbStars.setRating(data.stars);
        //图片加载
        //http://localhost:8080/GooglePlayServer/image?name=
        String url = Constants.URLS.IMGBASEURL + data.iconUrl;
        Picasso.with(UIUtils.getContext()).load(url).into(mItemAppinfoIvIcon);
        
        /*--------------- 根据不同状态,展示不同的ui ---------------*/
        //curState-->downLoadInfo
        DownLoadInfo downLoadInfo = DownLoadManager.getInstance().getDownLoadInfo(data);
        /*
        状态(编程记录)  	|  给用户的提示(ui展现)
        ----------------|-----------------------
        未下载			|下载
        下载中			|显示进度条
        暂停下载			|继续下载
        等待下载			|等待中...
        下载失败 		|重试
        下载完成 		|安装
        已安装 			|打开
         */
        refreshProgressViewUI(downLoadInfo);
    }

    /**
     * 更新下载按钮的ui展现
     *
     * @param downLoadInfo
     */
    private void refreshProgressViewUI(DownLoadInfo downLoadInfo) {
        int curState = downLoadInfo.curState;

        switch (curState) {
            case DownLoadManager.STATE_UNDOWNLOAD://未下载
                mProgressView.setNote("下载");
                mProgressView.setIcon(R.drawable.ic_download);
                break;
            case DownLoadManager.STATE_DOWNLOADING://下载中
                mProgressView.setIcon(R.drawable.ic_pause);
                mProgressView.setIsProgressEnable(true);

                int index = (int) (downLoadInfo.progress * 1.0f / downLoadInfo.max * 100 + .5f);
                mProgressView.setNote(index + "%");
                mProgressView.setMax(downLoadInfo.max);
                mProgressView.setProgress(downLoadInfo.progress);
                break;
            case DownLoadManager.STATE_PAUSEDOWNLOAD://暂停下载
                mProgressView.setIcon(R.drawable.ic_resume);
                mProgressView.setNote("继续");
                break;
            case DownLoadManager.STATE_WAITINGDOWNLOAD://等待下载
                mProgressView.setIcon(R.drawable.ic_pause);
                mProgressView.setNote("等待");
                break;
            case DownLoadManager.STATE_DOWNLOADFAILED://下载失败
                mProgressView.setIcon(R.drawable.ic_redownload);
                mProgressView.setNote("重试");
                break;
            case DownLoadManager.STATE_DOWNLOADED://下载完成
                mProgressView.setIcon(R.drawable.ic_install);
                mProgressView.setIsProgressEnable(false);
                mProgressView.setNote("安装");
                break;
            case DownLoadManager.STATE_INSTALLED://已安装
                mProgressView.setIcon(R.drawable.ic_install);
                mProgressView.setNote("打开");
                break;

            default:
                break;
        }
    }

    @OnClick(R.id.item_appinfo_progressview)
    public void clickProgressView(View view) {
        /*--------------- 根据不同状态,触发不同的操作 ---------------*/
        //curState-->downLoadInfo
        DownLoadInfo downLoadInfo = DownLoadManager.getInstance().getDownLoadInfo(mItemBean);
        int curState = downLoadInfo.curState;
        /*
         状态(编程记录)  | 用户行为(触发操作)
        ----------------| -----------------
        未下载			| 去下载
        下载中			| 暂停下载
        暂停下载			| 断点继续下载
        等待下载			| 取消下载
        下载失败 		| 重试下载
        下载完成 		| 安装应用
        已安装 			| 打开应用
         */
        switch (curState) {
            case DownLoadManager.STATE_UNDOWNLOAD://未下载
                doDownLoad(downLoadInfo);
                break;
            case DownLoadManager.STATE_DOWNLOADING://下载中
                doPause(downLoadInfo);
                break;
            case DownLoadManager.STATE_PAUSEDOWNLOAD://暂停下载
                doDownLoad(downLoadInfo);
                break;
            case DownLoadManager.STATE_WAITINGDOWNLOAD://等待下载
                doCancle(downLoadInfo);
                break;
            case DownLoadManager.STATE_DOWNLOADFAILED://下载失败
                doDownLoad(downLoadInfo);
                break;
            case DownLoadManager.STATE_DOWNLOADED://下载完成
                doInstall(downLoadInfo);
                break;
            case DownLoadManager.STATE_INSTALLED://已安装
                doOpen(downLoadInfo);
                break;

            default:
                break;
        }
    }

    /**
     * 打开apk
     *
     * @param downLoadInfo
     */
    private void doOpen(DownLoadInfo downLoadInfo) {
        CommonUtils.openApp(UIUtils.getContext(), downLoadInfo.packageName);
    }

    /**
     * 安装apk
     *
     * @param downLoadInfo
     */
    private void doInstall(DownLoadInfo downLoadInfo) {
        File apkFile = new File(downLoadInfo.savePath);
        CommonUtils.installApp(UIUtils.getContext(), apkFile);
    }

    /**
     * 取消下载
     *
     * @param downLoadInfo
     */
    private void doCancle(DownLoadInfo downLoadInfo) {
        DownLoadManager.getInstance().cancel(downLoadInfo);
    }

    /**
     * 暂停下载
     *
     * @param downLoadInfo
     */
    private void doPause(DownLoadInfo downLoadInfo) {
        DownLoadManager.getInstance().pause(downLoadInfo);
    }

    /**
     * 开始下载,断点继续下载,重试下载
     *
     * @param downLoadInfo
     */
    private void doDownLoad(DownLoadInfo downLoadInfo) {
        DownLoadManager.getInstance().downLoad(downLoadInfo);
    }
    /*--------------- 接收到DownLoadInfo改变的通知 ---------------*/
    @Override
    public void onDonwLoadInfoChanged(final DownLoadInfo downLoadInfo) {
        //针对接收到的DownLoadInfo进行过滤
        if (!downLoadInfo.packageName.equals(mItemBean.packageName)) {
            return;
        }
        //日志输入downLoadInfo里面的curState
        PrintDownLoadInfo.printDownLoadInfo(downLoadInfo);

        //刷新ui
        MyApplication.getMainThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                refreshProgressViewUI(downLoadInfo);
            }
        });
    }
}
