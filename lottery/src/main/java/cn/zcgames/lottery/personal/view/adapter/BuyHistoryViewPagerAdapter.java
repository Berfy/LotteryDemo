package cn.zcgames.lottery.personal.view.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 历史投注适配器
 *
 * @author NorthStar
 * @date 2018/8/20 16:56
 */
public class BuyHistoryViewPagerAdapter extends PagerAdapter {

    private List<View> mViews;

    public BuyHistoryViewPagerAdapter(List<View> mViews) {
        this.mViews = mViews;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
