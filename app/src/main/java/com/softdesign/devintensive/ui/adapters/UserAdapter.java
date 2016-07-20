package com.softdesign.devintensive.ui.adapters;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.ui.views.AspectRatioImageView;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.UiHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> mUsers;
    private Context mContext;
    private UserViewHolder.CustomClickListener mCustomClickListener;
    private final String TAG = ConstantManager.TAG_PREFIX + " UsersAdapter";

    public UserAdapter(List<User> users, UserViewHolder.CustomClickListener clickListener) {
        mUsers = users;
        mCustomClickListener = clickListener;
    }

    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);

        mContext = parent.getContext();
        return new UserViewHolder(convertView, mCustomClickListener);
    }

    @Override
    public void onBindViewHolder(final UserAdapter.UserViewHolder holder, int position) {
        final User user = mUsers.get(position);
        final String userPhoto;
        if (user.getPhoto().isEmpty()) {
            userPhoto = "null";
            Log.e(TAG, "onBindViewHolder: user with name " + user.getFullName() + " has no photo");
        } else {
            userPhoto = user.getPhoto();
        }

        UiHelper.getCachedImagePicasso(userPhoto, holder.mPhoto, holder.mDummy, false);
//        DataManager.getInstance().getPicasso()
//                .load(userPhoto)
//                .placeholder(holder.mDummy)
//                .error(holder.mDummy)
//                .fit()
//                .centerCrop()
//                .networkPolicy(NetworkPolicy.OFFLINE)
//                .into(holder.mPhoto, new Callback() {
//                            @Override
//                            public void onSuccess() {
//                                Log.d(TAG, " load from cache");
//                            }
//
//                            @Override
//                            public void onError() {
//                                DataManager.getInstance().getPicasso()
//                                        .load(userPhoto)
//                                        .placeholder(holder.mDummy)
//                                        .error(holder.mDummy)
//                                        .fit()
//                                        .centerCrop()
//                                        .into(holder.mPhoto, new Callback() {
//                                            @Override
//                                            public void onSuccess() {
//
//                                            }
//
//                                            @Override
//                                            public void onError() {
//                                                Log.d(TAG, " cannot fetch a image");
//                                            }
//                                        });
//                            }
//                        }
//                );
        holder.mFullName.setText(user.getFullName());
        holder.mRating.setText(String.valueOf(user.getRating() ));
        holder.mCodeLines.setText(String.valueOf(user.getCodeLines()));
        if (user.getBio() == null || user.getBio().isEmpty()){
            holder.mProjects.setText(String.valueOf(user.getProjects()));
            holder.mBio.setVisibility(View.GONE);
        } else {
            holder.mBio.setVisibility(View.VISIBLE);
            holder.mBio.setText(user.getBio());
        }

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected AspectRatioImageView mPhoto;
        protected TextView mFullName;
        protected TextView mRating;
        protected TextView mCodeLines;
        protected TextView mProjects;
        protected TextView mBio;
        protected Button mDetails;

        protected Drawable mDummy;

        private CustomClickListener mListener;


        public UserViewHolder(View itemView, CustomClickListener clickListener) {
            super(itemView);

            mPhoto = (AspectRatioImageView) itemView.findViewById(R.id.item_user_photo);
            mFullName = (TextView) itemView.findViewById(R.id.item_user_full_name_txt);
            mRating = (TextView) itemView.findViewById(R.id.item_user_rating_txt);
            mCodeLines = (TextView) itemView.findViewById(R.id.item_user_code_lines_txt);
            mProjects = (TextView) itemView.findViewById(R.id.item_user_projects_txt);
            mBio = (TextView) itemView.findViewById(R.id.item_user_bio_txt);
            mDetails = (Button) itemView.findViewById(R.id.item_user_more_info_btn);

            mDummy = mPhoto.getContext().getResources().getDrawable(R.drawable.user_bg);

            this.mListener = clickListener;
            mDetails.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (mListener != null) {
                mListener.onUserClikListener(getAdapterPosition());
            }

        }

        public interface CustomClickListener {
            void onUserClikListener(int position);
        }
    }

}
