package com.neandro.copy.utils;


import android.content.Context;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AssetsCopy {

    private static final String TAG = AssetsCopy.class.getSimpleName();

    private void copyAssets(Context context, String src, String dest) throws IOException {
        Log.d(TAG, "copyAssets: src = " + src);
        Log.d(TAG, "copyAssets: dest = " + dest);
        File destDirFile = new File(dest);
        if (!destDirFile.exists()) {
            destDirFile.mkdirs();
        }

        String[] paths = context.getAssets().list(src);
        for (String path : paths) {
            Log.v(TAG, "path:" + path);
            String newDestPath = dest + File.separator + path;
            String newPath = src + File.separator + path;
            String[] kidsPaths = context.getAssets().list(newPath);
            if (kidsPaths.length > 0) {
                copyAssets(context, newPath, newDestPath);
            } else {
                InputStream inputStream = context.getAssets().open(newPath);
                FileOutputStream fileOutputStream = new FileOutputStream(newDestPath);

                byte[] input = new byte[1024];
                int count;
                while ((count = inputStream.read(input)) != -1) {
                    fileOutputStream.write(input, 0, count);
                }

                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            }
        }
    }

    private static void copyFilesFromAssets(Context context, String assetsSrcDir, String sdcardDstDir, boolean override) {
        Log.d(TAG, "copyFilesFromAssets: assetsSrcDir = " + assetsSrcDir);
        Log.d(TAG, "copyFilesFromAssets: sdcardDstDir = " + sdcardDstDir);
        try {
            String fileNames[] = context.getAssets().list(assetsSrcDir);
            Log.d(TAG, "copyFilesFromAssets: files size = " + fileNames.length);
            if (fileNames.length > 0) {
                Log.i(TAG, assetsSrcDir + " directory has " + fileNames.length + " files.\n");
                File dir = new File(sdcardDstDir);
                if (!dir.exists()) {
                    if (!dir.mkdirs()) {
                        Log.e(TAG, "mkdir failed: " + sdcardDstDir);
                        return;
                    } else {
                        Log.i(TAG, "mkdir ok: " + sdcardDstDir);
                    }
                } else {
                    Log.w(TAG, sdcardDstDir + " already exists! ");
                }
                for (String fileName : fileNames) {
                    //copyFilesFromAssets(context, assetsSrcDir + "/" + fileName, sdcardDstDir + "/" + fileName, override);
                    copyFilesFromAssets(context, assetsSrcDir + File.separator + fileName, sdcardDstDir + File.separator + fileName, override);
                }
            } else {
                Log.i(TAG, assetsSrcDir + " is file\n");
                File outFile = new File(sdcardDstDir);
                if (outFile.exists()) {
                    if (override) {
                        outFile.delete();
                        Log.e(TAG, "overriding file " + sdcardDstDir + "\n");
                    } else {
                        Log.e(TAG, "file " + sdcardDstDir + " already exists. No override.\n");
                        return;
                    }
                }
                Log.d(TAG, "copyFilesFromAssets: assetsSrcDir = " + assetsSrcDir);
                Log.d(TAG, "copyFilesFromAssets: outFile = " + outFile.getAbsolutePath());
                InputStream is = context.getAssets().open(assetsSrcDir);
                FileOutputStream fos = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
                Log.i(TAG, "copy to " + sdcardDstDir + " ok!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyResFromAssetsToSD(Context context) {
        //String appPath = context.getPackageName();
        //String ASSETS_RES_DIR = RecorderConfig.ASSETS_RES_DIR;
        //String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        //String DEFAULT_WORK_SPACE = sdPath + File.separator + appPath + File.separator + RecorderConfig.ASSETS_RES_DIR;
        //String envWorkSpace = DEFAULT_WORK_SPACE;

        SDCardHelper sdcardHelper = SDCardHelper.getInstance();
        sdcardHelper.setContext(context);
        String envWorkSpace = sdcardHelper.getSDCardPath();
        String assetsResDir = "snowboy";
        Log.d(TAG, "copyResFromAssetsToSD: snowboy = " + assetsResDir);
        Log.d(TAG, "copyResFromAssetsToSD: envWorkSpace = " + envWorkSpace);
        copyFilesFromAssets(context, assetsResDir, envWorkSpace, true);
    }
}
