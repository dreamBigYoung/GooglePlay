package com.itheima.googleplay.holder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.base.MyApplication;
import com.itheima.googleplay.bean.ItemBean;
import com.itheima.googleplay.manager.DownLoadInfo;
import com.itheima.googleplay.manager.DownLoadManager;
import com.itheima.googleplay.utils.CommonUtils;
import com.itheima.googleplay.utils.PrintDownLoadInfo;
import com.itheima.googleplay.utils.UIUtils;
import com.itheima.googleplay.views.ProgressBtn;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class DetailDownloadHolder extends BaseHolder<ItemBean> implements DownLoadManager.DownLoadInfoObserver {
    @InjectView(R.id.app_detail_download_btn_favo)
    Button mAppDetailDownloadBtnFavo;
    @InjectView(R.id.app_detail_download_btn_share)
    Button mAppDetailDownloadBtnShare;
    @InjectView(R.id.app_detail_download_btn_download)
    ProgressBtn mAppDetailDownloadBtnDownload;
    private TextView mTv;
    private ItemBean mItemBean;

    @Override
    public View initHolderView() {
        View holderView = View.inflate(UIUtils.getContext(), R.layout.item_detail_download, null);
        ButterKnife.inject(this, holderView);
        return holderView;
    }

    @Override
    public void refreshHolderView(ItemBean data) {
        //保存数据到成员变量
        mItemBean = data;
        /*--------------- 2.根据不同的状态给用户提示 ---------------*/
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
        refreshProgressBtnUI(downLoadInfo);
    }

    /**
     * 更新下载按钮的ui展现
     *
     * @param downLoadInfo
     */
    private void refreshProgressBtnUI(DownLoadInfo downLoadInfo) {
        int curState = downLoadInfo.curState;

        mAppDetailDownloadBtnDownload.setBackgroundResource(R.drawable.selector_app_detail_bottom_normal);
        switch (curState) {
            case DownLoadManager.STATE_UNDOWNLOAD://未下载
                mAppDetailDownloadBtnDownload.setText("下载");
                break;
            case DownLoadManager.STATE_DOWNLOADING://下载中
                mAppDetailDownloadBtnDownload.setIsProgressEnable(true);
                mAppDetailDownloadBtnDownload.setBackgroundResource(R.drawable.selector_app_detail_bottom_downloading);

                int index = (int) (downLoadInfo.progress * 1.0f / downLoadInfo.max * 100 + .5f);
                mAppDetailDownloadBtnDownload.setText(index + "%");
                mAppDetailDownloadBtnDownload.setMax(downLoadInfo.max);
                mAppDetailDownloadBtnDownload.setProgress(downLoadInfo.progress);
                break;
            case DownLoadManager.STATE_PAUSEDOWNLOAD://暂停下载
                mAppDetailDownloadBtnDownload.setText("继续下载");
                break;
            case DownLoadManager.STATE_WAITINGDOWNLOAD://等待下载
                mAppDetailDownloadBtnDownload.setText("等待中...");
                break;
            case DownLoadManager.STATE_DOWNLOADFAILED://下载失败
                mAppDetailDownloadBtnDownload.setText("重试");
                break;
            case DownLoadManager.STATE_DOWNLOADED://下载完成
                mAppDetailDownloadBtnDownload.setIsProgressEnable(false);
                mAppDetailDownloadBtnDownload.setText("安装");
                break;
            case DownLoadManager.STATE_INSTALLED://已安装
                mAppDetailDownloadBtnDownload.setText("打开");
                break;

            default:
                break;
        }
    }

    @OnClick(R.id.app_detail_download_btn_download)
    public void clickBtnDownLoad(View view) {
       /* Toast.makeText(UIUtils.getContext(), "下载", Toast.LENGTH_SHORT).show();

        DownLoadInfo downLoadInfo = new DownLoadInfo();
        String dir = FileUtils.getDir("apk");//sdcard/Android/data/包目录/apk
        String fileName = mItemBean.packageName + ".apk";
        File saveApk = new File(dir, fileName);

        downLoadInfo.downloadUrl = mItemBean.downloadUrl;
        downLoadInfo.savePath = saveApk.getAbsolutePath();

        DownLoadManager.getInstance().downLoad(downLoadInfo);*/

        /*--------------- 3.根据不同的状态触发不同的操作 ---------------*/
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
    //被观察者通知消息所在的线程是什么线程,那接收消息所在的现在就是什么线程
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
                refreshProgressBtnUI(downLoadInfo);
            }
        });
    }
}
