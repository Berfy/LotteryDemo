package cn.zcgames.lottery.home.listener;

import android.view.View;

import cn.zcgames.lottery.home.bean.ADInfo;

/**
 *
 * 轮播控件的监听事件
 *
 * Created by admin on 2017/4/12.
 */

public interface AdCycleViewListener {

    /**
     * 单击图片事件
     *
     * @param imageView
     */
    void onAdImageClick(ADInfo info, int postion, View imageView);
}
