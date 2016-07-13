package com.softdesign.devintensive.ui.behaviors;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.UiHelper;

public class CustomNestedScrollBehavior extends AppBarLayout.ScrollingViewBehavior{
    private final int mMaxAppBarHeight;
    private final int mMinAppBarHeight;
    private final int mMaxUserInfoHeight;

    public CustomNestedScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        mMaxAppBarHeight = context.getResources().getDimensionPixelSize(R.dimen.profile_image_size);
        mMinAppBarHeight = UiHelper.getActionBarHeight() + UiHelper.getStatusBarHeight();
        mMaxUserInfoHeight = context.getResources().getDimensionPixelSize(R.dimen.size_large_112);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float currentFriction = UiHelper.currentFriction(mMinAppBarHeight, mMaxAppBarHeight, dependency.getBottom());
        int offsetY = UiHelper.lerp(mMaxUserInfoHeight/2, mMaxUserInfoHeight, currentFriction);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)child.getLayoutParams();
        lp.topMargin =  offsetY;

        child.setLayoutParams(lp);

        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

}
