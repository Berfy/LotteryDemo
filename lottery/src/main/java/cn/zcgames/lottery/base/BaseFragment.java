package cn.zcgames.lottery.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import cn.berfy.sdk.mvpbase.util.LogF;

/**
 * Created by admin on 2017/3/30.
 */

public class BaseFragment extends Fragment {

    private final String TAG = "BaseFragment";
    private boolean mIsViewCreated;
    private boolean mIsDestroy;
    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIsViewCreated = true;
        LogF.d(TAG, "生命周期 onViewCreated");
    }

    protected boolean isViewCreated() {
        return mIsViewCreated;
    }

    protected boolean isDestroy() {
        return mIsDestroy;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsViewCreated = false;
        mIsDestroy = true;
    }
}
