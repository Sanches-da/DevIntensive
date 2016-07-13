package com.softdesign.devintensive.ui.behaviors;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.softdesign.devintensive.utils.UiHelper;

public class UserInfoBehavior<V extends LinearLayout> extends AppBarLayout.ScrollingViewBehavior{
    private final int mMaxAppBarHeight;
    private final int mMinAppBarHeight;
    private final int mMaxUserInfoHeight;
    private final int mMinUserInfoHeight;

    public UserInfoBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UserInfoBehavior);

        mMaxAppBarHeight = context.getResources().getDimensionPixelSize(R.dimen.profile_image_size);
        mMinAppBarHeight = UiHelper.getActionBarHeight() + UiHelper.getStatusBarHeight();
        mMaxUserInfoHeight = context.getResources().getDimensionPixelSize(R.dimen.size_large_112);
        mMinUserInfoHeight = a.getDimensionPixelSize(R.styleable.UserInfoBehavior_behavior_min_height, context.getResources().getDimensionPixelSize(R.dimen.size_medium_56));
        a.recycle();
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float currentFriction = UiHelper.currentFriction(mMinAppBarHeight, mMaxAppBarHeight, dependency.getBottom());
        int currentHeight = UiHelper.lerp(mMinUserInfoHeight, mMaxUserInfoHeight, currentFriction);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)child.getLayoutParams();
        lp.height =  currentHeight;

        child.setLayoutParams(lp);

        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }
}
