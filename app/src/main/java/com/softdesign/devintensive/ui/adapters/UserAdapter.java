package com.softdesign.devintensive.ui.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.ui.views.AspectRatioImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<UserListRes.UserData> mUsers;
    private Context mContext;
    private UserViewHolder.CustomClickListener mCustomClickListener;

    public UserAdapter(List<UserListRes.UserData> users, UserViewHolder.CustomClickListener clickListener) {
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
    public void onBindViewHolder(UserAdapter.UserViewHolder holder, int position) {
        UserListRes.UserData user = mUsers.get(position);

        Picasso.with(mContext)
                .load(user.getPublicInfo().getPhoto())
                .placeholder(mContext.getResources().getDrawable(R.drawable.user_bg))
                //.resize()
                //.centerCrop()
                .error(mContext.getResources().getDrawable(R.drawable.user_bg))
                .into(holder.mPhoto);
        holder.mFullName.setText(user.getFullName());
        holder.mRating.setText(String.valueOf(user.getProfileValues().getRaiting()));
        holder.mCodeLines.setText(String.valueOf(user.getProfileValues().getRaiting()));
        holder.mProjects.setText(String.valueOf(user.getProfileValues().getRaiting()));
        if (user.getPublicInfo().getBio() == null || user.getPublicInfo().getBio().isEmpty()) {
            holder.mBio.setVisibility(View.GONE);
        }else {
            holder.mBio.setVisibility(View.VISIBLE);
            holder.mBio.setText(user.getPublicInfo().getBio());
        }

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        protected AspectRatioImageView mPhoto;
        protected TextView mFullName;
        protected TextView mRating;
        protected TextView mCodeLines;
        protected TextView mProjects;
        protected TextView mBio;
        protected Button mDetails;

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

            this.mListener = clickListener;
            mDetails.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (mListener != null){
                mListener.onUserClikListener(getAdapterPosition());
            }

        }

        public interface CustomClickListener{
            void onUserClikListener(int position);
        }
    }

}
