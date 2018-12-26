package cn.zcgames.lottery.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.format.DateFormat;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.personal.view.activity.AccountInfoActivity;

import static cn.zcgames.lottery.app.AppConstants.SYSTEM_PATH_CHANGE_HEADER;

/**
 * Created by admin on 2017/4/11.
 */

public class ImageFileUtils {

    /**
     * 传入图片得到模糊图片
     *
     * @param bitmap 要模糊的图片
     * @param radius 模糊等级 >=0 && <=25
     * @return
     */
    public static Bitmap getBlurBitmap(Context context, Bitmap bitmap, int radius) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return blurBitmap(context, bitmap, radius);
        }
        return bitmap;
    }

    /**
     * android系统的模糊方法
     *
     * @param bitmap 要模糊的图片
     * @param radius 模糊等级 >=0 && <=25
     */
    private static Bitmap blurBitmap(Context context, Bitmap bitmap, int radius) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //Let's create an empty bitmap with the same size of the bitmap we want to blur
            Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            //Instantiate a new Renderscript
            RenderScript rs = RenderScript.create(context);
            //Create an Intrinsic Blur Script using the Renderscript
            ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
            Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
            Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
            //Set the radius of the blur
            blurScript.setRadius(radius);
            //Perform the Renderscript
            blurScript.setInput(allIn);
            blurScript.forEach(allOut);
            //Copy the final bitmap created by the out Allocation to the outBitmap
            allOut.copyTo(outBitmap);
            //recycle the original bitmap
            bitmap.recycle();
            //After finishing everything, we destroy the Renderscript.
            rs.destroy();
            return outBitmap;
        } else {
            return bitmap;
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public static void startCropActivity(Activity context, Uri uri) {
        String name = DateFormat.format("yyyy-MM-dd--hh-mm-ss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        File file = new File(context.getCacheDir(), name);
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(file));
        UCrop.Options options = new UCrop.Options();//下面参数分别是缩放,旋转,裁剪框的比例
        options.setAllowedGestures(UCropActivity.ALL, UCropActivity.NONE, UCropActivity.ALL);
        options.setToolbarTitle("裁剪图片");//设置标题栏文字
        options.setCropGridStrokeWidth(1);//设置裁剪网格线的宽度(我这网格设置不显示，所以没效果)
        options.setCropFrameStrokeWidth(1);//设置裁剪框的宽度
        options.setMaxScaleMultiplier(3);//设置最大缩放比例
        options.setHideBottomControls(true);//隐藏下边控制栏
        options.setShowCropGrid(false);  //设置是否显示裁剪网格
        options.setOvalDimmedLayer(false);//设置是否为圆形裁剪框
        options.setShowCropFrame(true); //设置是否显示裁剪边框(true为方
        options.setToolbarWidgetColor(Color.parseColor("#ffffff"));//标题字的颜色以及按钮颜色
        options.setDimmedLayerColor(Color.parseColor("#AA000000"));//设置裁剪外颜色
        options.setToolbarColor(Color.parseColor("#000000")); // 设置标题栏颜色
        options.setStatusBarColor(Color.parseColor("#000000"));//设置状态栏颜色
        options.setCropGridColor(Color.parseColor("#ffffff"));//设置裁剪网格的颜色
        options.setCropFrameColor(Color.parseColor("#ffffff"));//设置裁剪框的颜色
        uCrop.withOptions(options);
        uCrop.withAspectRatio(1, 1);
        uCrop.start(context);
    }

    /**
     * 处理剪切成功的返回值
     *
     * @param result
     */
    public static Uri handleCropResult(Intent result) {
        Uri littleIconUri = UCrop.getOutput(result);
        if (null != littleIconUri) {
            return littleIconUri;
        } else {
            ToastUtil.getInstances().showShort("无法剪切选择图片");
        }
        return null;
    }

    /**
     * 调用系统的裁剪
     *
     * @param uri
     */
    public static void cropPhoto(Uri uri, Activity context) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, SYSTEM_PATH_CHANGE_HEADER);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        context.startActivityForResult(intent, AppConstants.CROP_REQUEST);
    }
}
