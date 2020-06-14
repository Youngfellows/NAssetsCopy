package com.neandro.copy.utils;


import android.content.Context;
import android.util.Log;


public class SDCardHelper {

    private String TAG = this.getClass().getSimpleName();

    private Context mContext;

    /**
     * Aseests snowboy模型资源目录
     */
    String ASSETS_RES_DIR = "snowboy";

    /**
     * snowboy sdcard 资源目录
     */
    String SDCARD_DES_DIR = ASSETS_RES_DIR;

    private SDCardHelper() {

    }

    private static class Holder {
        private static SDCardHelper INSTANCE = new SDCardHelper();
    }

    public static SDCardHelper getInstance() {
        return Holder.INSTANCE;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public boolean checkContext() {
        if (mContext == null) {
            throw new IllegalStateException("please call setContext init mContext");
        }
        return true;
    }

    /**
     * 获取应用的/sdcard/包名/xxx,目录
     * 内置sdcard路径(数据会保存，应用删除的时候，数据不会被清理掉)
     *
     * @return
     */
    public String getSDCardPath() {
        String sdCardPath = CachePathUtil.getExternalDirectory(mContext, SDCARD_DES_DIR).getAbsolutePath();
        Log.d(TAG, "getSDCardPath: sdCardPath: " + sdCardPath);
        return sdCardPath;
    }

    /**
     * 获取/data/data/包名/files/xxx,目录
     *
     * @return
     */
    public String getInternalDataPath() {
        String dataPath = CachePathUtil.getInternalCacheDirectory(mContext, SDCARD_DES_DIR).getAbsolutePath();
        Log.d(TAG, "getInternalDataPath: dataPath: " + dataPath);
        return dataPath;
    }

    /**
     * 获取应用的/sdcard/Android/包名/data/files/xxx,目录
     */
    public String getExternalDataDir() {
        checkContext();
        String sdCardDataPath = CachePathUtil.getCacheDirectory(mContext, SDCARD_DES_DIR).getAbsolutePath();
        Log.d(TAG, "getExternalDataDir: sdCardDataPath: " + sdCardDataPath);
        return sdCardDataPath;
    }

    /**
     * 获取应用包明
     */
    public String getPackageName() {
        checkContext();
        return mContext.getPackageName();
    }

}
