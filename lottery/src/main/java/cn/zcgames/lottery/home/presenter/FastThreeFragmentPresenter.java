package cn.zcgames.lottery.home.presenter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.home.view.iview.ITowSameSingleFragment;

/**
 * Created by admin on 2017/6/19.
 */

public class FastThreeFragmentPresenter {

    private Context mContext;
    private IBaseView iBaseView;
    private ITowSameSingleFragment iTowSameSingleFragment;

    public FastThreeFragmentPresenter(Context context, IBaseView iview) {
        mContext = context;
        this.iBaseView = iview;
    }

    public FastThreeFragmentPresenter(Context context, ITowSameSingleFragment iview) {
        mContext = context;
        this.iTowSameSingleFragment = iview;
    }

    public void requestSumFragmentOptions() {
        List<LotteryBall> btns = new ArrayList<>();

        for (int i = 3; i < 19; i++) {
            LotteryBall btn = new LotteryBall();
            btn.setNumber(i);
            String tip = "奖金";
            btn.setSelected(false);
            if (i == 3) {
                tip = tip + 240;
            } else if (i == 4) {
                tip = tip + 80;
            } else if (i == 5) {
                tip = tip + 40;
            } else if (i == 6) {
                tip = tip + 25;
            } else if (i == 7) {
                tip = tip + 16;
            } else if (i == 8) {
                tip = tip + 12;
            } else if (i == 9) {
                tip = tip + 10;
            } else if (i == 10) {
                tip = tip + 9;
            } else if (i == 11) {
                tip = tip + 9;
            } else if (i == 12) {
                tip = tip + 10;
            } else if (i == 13) {
                tip = tip + 12;
            } else if (i == 14) {
                tip = tip + 16;
            } else if (i == 15) {
                tip = tip + 25;
            } else if (i == 16) {
                tip = tip + 40;
            } else if (i == 17) {
                tip = tip + 80;
            } else if (i == 18) {
                tip = tip + 240;
            }
            btn.setTip(tip);
            btns.add(btn);
        }

        iBaseView.requestResult(true, btns);
    }

    public void requestThreeDifferentOptions() {
        List<LotteryBall> btns = new ArrayList<>();

        for (int i = 1; i < 7; i++) {
            LotteryBall btn = new LotteryBall();
            btn.setNumber(i);
            btn.setSelected(false);
            btn.setTip("");
            btns.add(btn);
        }

        iBaseView.requestResult(true, btns);
    }

    public void requestThreeSameSingleOptions() {
        List<LotteryBall> btns = new ArrayList<>();

        for (int i = 1; i < 7; i++) {
            LotteryBall btn = new LotteryBall();
            btn.setNumber(i * 100 + i * 10 + i);
            String tip = "奖金240";
            btn.setSelected(false);
            btn.setTip(tip);
            btns.add(btn);
        }

        iBaseView.requestResult(true, btns);
    }

    public void requestTowSameMoreOptions() {
        List<LotteryBall> btns = new ArrayList<>();

        for (int i = 1; i < 7; i++) {
            LotteryBall btn = new LotteryBall();
            btn.setNumber(i * 10 + i);
            btn.setSelected(false);
            btn.setTip("");
            btns.add(btn);
        }
        iTowSameSingleFragment.requestResult(true, btns);
    }

    public void requestTowSameSingleSame() {
        List<LotteryBall> btns = new ArrayList<>();

        for (int i = 1; i < 7; i++) {
            LotteryBall btn = new LotteryBall();
            btn.setNumber(i * 10 + i);
            btn.setSelected(false);
            btn.setTip("");
            btns.add(btn);
        }

        iTowSameSingleFragment.requestTowSameSingleSame(btns);
    }

    public void requestTowSameSingleDifferent() {
        List<LotteryBall> btns = new ArrayList<>();

        for (int i = 1; i < 7; i++) {
            LotteryBall btn = new LotteryBall();
            btn.setNumber(i);
            btn.setSelected(false);
            btn.setTip("");
            btns.add(btn);
        }

        iTowSameSingleFragment.requestTowSameSingleDifferent(btns);
    }
}
