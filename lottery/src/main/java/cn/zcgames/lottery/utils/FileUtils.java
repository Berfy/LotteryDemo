package cn.zcgames.lottery.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.app.AppConstants;

/**
 * Created by admin on 2017/3/14.
 */

public class FileUtils {

    private static final String TAG = "FileUtils";

    /**
     * 根据路径加载本地图片
     *
     * @param path
     * @return
     */
    public static Bitmap getBitmapByLocalPath(String path) {
        boolean isFile = isFileExistByPath(path);
        if (!isFile) {
            return null;
        }
        return BitmapFactory.decodeFile(path);
    }

    /**
     * 保存截取好的图片
     *
     * @param bitmap
     * @return
     */
    public static boolean saveCropPic(Bitmap bitmap) {
        boolean isOk = false;
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return isOk;
        }
        FileOutputStream b = null;
        try {
            if (FileUtils.isFileExistByPath(FileUtils.getLocalUserHeaderTempPath())) {
                File file = new File(FileUtils.getLocalUserHeaderTempPath());
                file.delete();
            }
            b = new FileOutputStream(FileUtils.getLocalUserHeaderTempPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            isOk = true;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "saveCropPic: FileNotFoundException error : " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                if (b != null) {
                    b.flush();
                    b.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return isOk;
    }

    /**
     * 保存bitmap到本地
     *
     * @param filePath
     * @param bitmap
     */
    public static void saveBitmap(String filePath, Bitmap bitmap) {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (Exception e) {
            }
        }
    }


    /**
     * 获取手机内置存储路径
     *
     * @return
     */
    public static String getExternalStorageDirectory() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 根据路径判断文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean isFileExistByPath(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File f = new File(path);
        if (f.exists() && f.isFile()) {
            return true;
        }
        return false;
    }

    /**
     * 获得系统的路径
     *
     * @return
     */
    public static String getLocalDCIMDir() {
        String result = null;
        File videoDirFile = null;
        boolean error = false;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            result = getExternalStorageDirectory();
            result += File.separator + AppConstants.DCIM_CAMERA_PATH;
            videoDirFile = new File(result);
            if (!videoDirFile.exists() && !videoDirFile.mkdirs()) {
                error = true;
            }
        }
        if (error)
            return null;
        return result;
    }

    /**
     * 获得系统的路径
     *
     * @return
     */
    public static String getLocalTempDir() {
        String result = null;
        File videoDirFile = null;
        boolean error = false;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            result = getExternalStorageDirectory();
            result += File.separator + AppConstants.LOCAL_FILE_TEMP_PATH;
            videoDirFile = new File(result);
            if (!videoDirFile.exists() && !videoDirFile.mkdirs()) {
                error = true;
            }
        }
        if (error)
            return null;
        return result;
    }

    /**
     * 获得用户头像路径
     *
     * @return
     */
    public static String getLocalUserHeaderTempPath() {
        return getLocalTempDir() + File.separator + AppConstants.USER_PIC_TEMP_FILE_NAME + AppConstants.USER_HEADER_TEMP_FILE_SUFFIX;
    }

    /**
     * download picture file and save it into local place
     *
     * @param picUurl
     * @param context
     * @param localPath
     */
    public void downloadPicFile(String picUurl, Context context, final String localPath) {
        //Target
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                File dcimFile = new File(localPath);
                FileOutputStream ostream = null;
                try {
                    ostream = new FileOutputStream(dcimFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    ostream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.e(TAG, "onBitmapFailed: ");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.e(TAG, "onPrepareLoad: ");
            }
        };
        //Picasso下载
        Picasso.with(context).load(picUurl).into(target);
    }

    public static boolean createFile(String dirOrFile, boolean isDeleteOld) {
        LogF.d("创建文件", dirOrFile + " 是否删除已存在文件" + isDeleteOld);
        try {
            File file = new File(dirOrFile);
            if (isDeleteOld) {
                boolean isDeleted = file.delete();
                LogF.d("创建文件", dirOrFile + "是否删除" + isDeleted);
            }
            if (file.isFile()) {
                boolean isCreated = file.createNewFile();
                LogF.d("创建文件", "isFile created = " + isCreated);
                return isCreated;
            } else {
                boolean isCreated = file.mkdirs();
                LogF.d("创建文件夹", "isDir created = " + isCreated);
                return isCreated;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
