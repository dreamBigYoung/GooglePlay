package com.itheima.googleplay.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.bean.ItemBean;
import com.itheima.googleplay.conf.Constants;
import com.itheima.googleplay.utils.StringUtils;
import com.itheima.googleplay.utils.UIUtils;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class DetailInfoHolder extends BaseHolder<ItemBean> {


    @InjectView(R.id.app_detail_info_iv_icon)
    ImageView mAppDetailInfoIvIcon;
    @InjectView(R.id.app_detail_info_tv_name)
    TextView mAppDetailInfoTvName;
    @InjectView(R.id.app_detail_info_rb_star)
    RatingBar mAppDetailInfoRbStar;
    @InjectView(R.id.app_detail_info_tv_downloadnum)
    TextView mAppDetailInfoTvDownloadnum;
    @InjectView(R.id.app_detail_info_tv_version)
    TextView mAppDetailInfoTvVersion;
    @InjectView(R.id.app_detail_info_tv_time)
    TextView mAppDetailInfoTvTime;
    @InjectView(R.id.app_detail_info_tv_size)
    TextView mAppDetailInfoTvSize;

    /**
     * 决定成功视图长什么样子
     *
     * @return
     */
    @Override
    public View initHolderView() {
        View holderView = View.inflate(UIUtils.getContext(), R.layout.item_detail_info, null);
        //找出孩子
        ButterKnife.inject(this, holderView);
        return holderView;
    }

    @Override
    public void refreshHolderView(ItemBean data) {
        String date = UIUtils.getResources().getString(R.string.detail_date, data.date);
        String downLoadNum = UIUtils.getResources().getString(R.string.detail_downloadnum, data.downloadNum);
        String size = UIUtils.getResources().getString(R.string.detail_size, StringUtils.formatFileSize(data.size));
        String version = UIUtils.getResources().getString(R.string.detail_version, data.version);


        mAppDetailInfoTvName.setText(data.name);

        mAppDetailInfoTvVersion.setText(version);
        mAppDetailInfoTvTime.setText(date);
        mAppDetailInfoTvSize.setText(size);
        mAppDetailInfoTvDownloadnum.setText(downLoadNum);

        //ratingbar
        mAppDetailInfoRbStar.setRating(data.stars);

        //图标
        Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + data.iconUrl).into(mAppDetailInfoIvIcon);
    }
}
