package com.andpack.application;

import android.support.v4.app.Fragment;

import com.andframe.activity.AfActivity;
import com.andframe.impl.pager.AfPagerManager;
import com.andpack.activity.ApFragmentActivity;

/**
 * 页面管理器
 * Created by SCWANG on 2017/5/14.
 */

public class ApPagerManager extends AfPagerManager {

    @Override
    @SuppressWarnings("unchecked")
    public boolean startPager(Class clazz, Object... args) {
        if (Fragment.class.isAssignableFrom(clazz)) {
            startFragment(clazz, args);
        } else {
            return super.startPager(clazz, args);
        }
        return true;
    }

    @Override
    public void startFragment(Class<? extends Fragment> clazz, Object... args) {
        AfActivity activity = currentActivity();
        if (activity != null && !activity.isRecycled()) {
            activity.startFragment(clazz, args);
        } else {
            ApFragmentActivity.start(null, clazz, args);
        }
    }
}
