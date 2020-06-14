package com.neandro.copy.utils;


import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainInternal {

    private static String TAG = MainInternal.class.getSimpleName();

    /**
     * 拷贝到的目标路径
     */
    private static String TARGET_BASE_PATH;

    public static void extractAssetsFiles(Context context) throws IOException {

        AssetManager assetManager = context.getAssets();

        TARGET_BASE_PATH = context.getExternalFilesDir(null).getAbsolutePath() + "/app/asr/";
        Log.d(TAG, "extractAssetsFiles: " + TARGET_BASE_PATH);
        if (null == TARGET_BASE_PATH) {
            TARGET_BASE_PATH = context.getFilesDir().getAbsolutePath();
        }

        copyFileOrDir("", assetManager);
    }

    private static void copyFileOrDir(String path, AssetManager assetManager) {
        String assets[] = null;
        try {
            Log.i(TAG, "copyFileOrDir(): path = " + path);
            assets = assetManager.list(path);
            Log.d(TAG, "copyFileOrDir(): assets.length = " + assets.length);
            if (assets.length == 0) {
                copyFile(path, assetManager);
            } else {
                String fullPath = TARGET_BASE_PATH + path;
                Log.i(TAG, "copyFileOrDir(): path = " + fullPath);
                File dir = new File(fullPath);
                if (!dir.exists() && !path.startsWith("images") && !path.startsWith("webkit")) {
                    if (!dir.mkdirs()) {
                        Log.i(TAG, "could not create dir " + fullPath);
                    }
                }
                for (int i = 0; i < assets.length; ++i) {
                    String p;
                    if (path.equals("")) {
                        p = "";
                    } else {
                        p = path + "/";
                    }
                    Log.d(TAG, "copyFileOrDir(): " + i + ", p = " + p + ",assets[" + i + "] = " + assets[i]);
                    if (!path.startsWith("images") && !path.startsWith("webkit")) {
                        Log.d(TAG, "copyFileOrDir(): p1 = " + p + assets[i]);
                        copyFileOrDir(p + assets[i], assetManager);
                    }
                }
            }
        } catch (IOException ex) {
            Log.e(TAG, "I/O Exception", ex);
        }
    }

    private static void copyFile(String filename, AssetManager assetManager) {
        InputStream in = null;
        OutputStream out = null;
        String newFileName = null;
        try {
            Log.w(TAG, "copyFile(): filename = " + filename);
            in = assetManager.open(filename);
            if (filename.endsWith(".jpg")) { // extension was added to avoid compression on APK file
                newFileName = TARGET_BASE_PATH + filename.substring(0, filename.length() - 4);
            } else {
                newFileName = TARGET_BASE_PATH + filename;
                out = new FileOutputStream(newFileName);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in copyFile() of " + newFileName);
            Log.e(TAG, "Exception in copyFile() " + e.toString());
        }
    }
}
