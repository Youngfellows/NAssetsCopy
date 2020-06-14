package com.neandro.copy.base;


import android.util.Log;

/**
 * 抽象BaseActivity
 */
public abstract class BaseActivity extends PermissionsActivity {
    @Override
    public void onPermissionsFull() {
        Log.d(TAG, "onPermissionsFull: father ... ");
    }
}
