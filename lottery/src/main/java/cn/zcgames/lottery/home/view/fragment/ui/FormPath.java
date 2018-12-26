package cn.zcgames.lottery.home.view.fragment.ui;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * 存放各类画笔及不同分支路径类
 *
 * @author NorthStar
 * @date 2018/10/25 18:25
 */
public class FormPath {
    private Path path;//存放分类线路
    private Paint paint;//存放分类画笔

    FormPath(Path path, Paint paint) {
        this.path = path;
        this.paint = paint;
    }

    public Path getPath() {
        return path;
    }

    public Paint getPaint() {
        return paint;
    }
}
