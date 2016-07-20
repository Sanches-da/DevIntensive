package com.softdesign.devintensive.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.TypedValue;
import android.widget.ImageView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class UiHelper {
    private static Context mContext = DevintensiveApplication.getContext();

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    public static int getActionBarHeight() {
        int result = 0;
        TypedValue tv = new TypedValue();
        if (mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            result = TypedValue.complexToDimensionPixelSize(tv.data, mContext.getResources().getDisplayMetrics());
        }

        return result;
    }

    public static int lerp(int start, int end, float friction) {
        return (int) (start + (end - start) * friction);
    }

    public static float currentFriction(int start, int end, int currentValue) {
        return (float) (currentValue - start) / (end - start);
    }

    public static void getCachedImagePicasso(final String uri, final ImageView view, final Drawable dummy, final boolean rounded) {
        RequestCreator picasso = DataManager.getInstance().getPicasso()
                .load(uri)
                .placeholder(dummy)
                .error(dummy)
                .fit()
                .centerCrop()
                .networkPolicy(NetworkPolicy.OFFLINE);
        if (rounded) {
            picasso.transform(new CircleTransform());
        }

        picasso.into(view, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        RequestCreator picasso = DataManager.getInstance().getPicasso()
                                .load(uri)
                                .placeholder(dummy)
                                .error(dummy)
                                .fit()
                                .centerCrop();

                        if (rounded) {
                            picasso.transform(new CircleTransform());
                        }

                        picasso.into(view, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                            }
                        });
                    }
                }
        );

    }
}
