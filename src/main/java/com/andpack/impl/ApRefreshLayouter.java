package com.andpack.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.andframe.api.pager.status.OnRefreshListener;
import com.andframe.api.pager.status.RefreshLayouter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.Date;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.ListPopupWindow.WRAP_CONTENT;

/**
 * 使用第三方刷新控件 PullRefreshLayout
 * Created by SCWANG on 2016/10/21.
 */

@SuppressWarnings("unused")
public class ApRefreshLayouter implements RefreshLayouter<SmartRefreshLayout> {

    protected final SmartRefreshLayout mRefreshLayout;

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new BezierRadarHeader(context));
    }

    public ApRefreshLayouter(Context context) {
        mRefreshLayout = new SmartRefreshLayout(context);
        mRefreshLayout.setRefreshHeader(newHeader(context));
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setEnableOverScrollBounce(false);
    }

    public ApRefreshLayouter(Context context, int primaryId, int frontId) {
        mRefreshLayout = new SmartRefreshLayout(context);
        mRefreshLayout.setRefreshHeader(newHeader(context));
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setPrimaryColorsId(primaryId, frontId);
        mRefreshLayout.setEnableOverScrollBounce(false);
    }

    public ApRefreshLayouter(SmartRefreshLayout refreshLayout) {
        mRefreshLayout = refreshLayout;
        mRefreshLayout.setRefreshHeader(newHeader(refreshLayout.getContext()));
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setEnableOverScrollBounce(false);
    }

    protected RefreshHeader newHeader(Context context) {
        return new BezierRadarHeader(context);
    }

    @NonNull
    @Override
    public SmartRefreshLayout getLayout() {
        return mRefreshLayout;
    }

    @Override
    public void setContentView(View content) {
        ViewGroup.LayoutParams params = content.getLayoutParams();
        int height = params == null ? MATCH_PARENT : params.height;
        if (params != null && params.height == WRAP_CONTENT) {
            params.height = MATCH_PARENT;
        }
//        mRefreshLayout.addView(content, MATCH_PARENT, height == 0 ? MATCH_PARENT : height);
        mRefreshLayout.setRefreshContent(content, MATCH_PARENT, height == 0 ? MATCH_PARENT : height);
    }

    @Override
    public void wrapper(View content) {
        ViewParent parent = content.getParent();
        if (parent instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) parent;
            ViewGroup.LayoutParams params = content.getLayoutParams();
            int index = group.indexOfChild(content);
            group.removeViewAt(index);
            setContentView(content);
            group.addView(getLayout(),index,params);
        }
    }

    @Override
    public void finishRefresh(boolean success) {
        mRefreshLayout.finishRefresh(success);
    }

    //    @Override
//    public void setRefreshComplete() {
//        mRefreshLayout.finishRefresh();
//    }
//
//    @Override
//    public void setRefreshFailed() {
//        mRefreshLayout.finishRefresh(false);
//    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        mRefreshLayout.setOnRefreshListener(refreshLayout -> {
            if (!listener.onRefresh()) {
                mRefreshLayout.finishRefresh(0, false);
            }
        });
    }

    @Override
    public void setLastRefreshTime(Date date) {
        final RefreshHeader header = mRefreshLayout.getRefreshHeader();
        if (header instanceof ClassicsHeader) {
            ((ClassicsHeader) header).setLastUpdateTime(date);
        }
    }

    @Override
    public boolean isRefreshing() {
        return mRefreshLayout.getState() == RefreshState.Refreshing;
    }
}
