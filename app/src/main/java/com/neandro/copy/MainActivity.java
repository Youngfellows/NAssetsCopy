package com.neandro.copy;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.neandro.copy.base.BaseActivity;
import com.neandro.copy.utils.AssetsCopy;
import com.neandro.copy.utils.MainInternal;

import java.io.IOException;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 拷贝Assets资源
     *
     * @param view
     */
    public void onAssetsCopy1(View view) {
        Log.d(TAG, "onAssetsCopy1: ");
        AssetsCopy.copyResFromAssetsToSD(this);
    }

    /**
     * 拷贝Assets资源
     *
     * @param view
     */
    public void onAssetsCopy2(View view) {
        Log.d(TAG, "onAssetsCopy2: ");
        try {
            MainInternal.extractAssetsFiles(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPermissionsFull() {
        Log.d(TAG, "onPermissionsFull: children ... ");
        super.onPermissionsFull();
    }
}
