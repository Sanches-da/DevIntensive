package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.softdesign.devintensive.R;

import java.util.List;

public class RepositoriesAdapter extends BaseAdapter{
    private List<String> mRepositoriesList;
    Context mContext;
    LayoutInflater mInflater;

    public RepositoriesAdapter(Context context, List<String> repositoriesList) {
        mContext = context;
        mRepositoriesList = repositoriesList;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mRepositoriesList.size();
    }

    @Override
    public Object getItem(int i) {
        return mRepositoriesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;
        if (itemView == null){
            itemView = mInflater.inflate(R.layout.item_repositories_list, viewGroup, false);
        }

        TextView repoName = (TextView)itemView.findViewById(R.id.git_1_et);
        repoName.setText(mRepositoriesList.get(i));

        return itemView;
    }
}
