package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softdesign.devintensive.ui.adapters.UserAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileUserActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageView mProfileImage;
    private EditText mBio;
    private TextView mUserRating, mUserCodeLines, mUserProjects;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private CoordinatorLayout mCoordinatorLayout;

    private ListView mRepoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mProfileImage = (ImageView) findViewById(R.id.user_photo_img);
        mBio = (EditText) findViewById(R.id.about_et);
        mUserRating = (TextView) findViewById(R.id.rating_et);
        mUserCodeLines = (TextView) findViewById(R.id.code_lines_et);
        mUserProjects = (TextView) findViewById(R.id.projects_et);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCoordinatorLayout= (CoordinatorLayout) findViewById(R.id.main_coordinator_container);

        mRepoList = (ListView) findViewById(R.id.repositoriesList);

        setupToolBar();
        initProfileData();
    }

    private void setupToolBar(){
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void initProfileData(){
        UserDTO userData = getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY);

        final List<String> repo = userData.getRepositoriesList();
        final RepositoriesAdapter repoAddapter = new RepositoriesAdapter(this, repo);

        mRepoList.setAdapter(repoAddapter);
        mBio.setText(userData.getBio());
        mUserCodeLines.setText(userData.getCodeLines());
        mUserProjects.setText(userData.getProjects());
        mUserRating.setText(userData.getRating());

        mCollapsingToolbarLayout.setTitle(userData.getFullName());
        //TODO Разобраться с ошибкой подключения онкликлистнер
//        mRepoList.setOnClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            }
//        });

        Picasso.with(this)
                .load(userData.getPhoto())
                .error(R.drawable.user_bg)
                .placeholder(R.drawable.user_bg)
                .into(mProfileImage);

    }
}
