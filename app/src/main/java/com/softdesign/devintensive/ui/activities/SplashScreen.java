package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.redmadrobot.chronos.gui.activity.ChronosActivity;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ChronosLoadUsers;
import com.softdesign.devintensive.utils.ConstantManager;

public class SplashScreen extends ChronosActivity {
    private DataManager mDataManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);


        mDataManager = DataManager.getInstance();
        mDataManager.getConnector().onCreate(this, savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mDataManager.getConnector().onResume();

        String token = mDataManager.getPreferencesManager().getAuthToken();
        String userId = mDataManager.getPreferencesManager().getUserId();

        if (token.isEmpty() || userId.isEmpty()) {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivityForResult(intent, ConstantManager.REQUEST_AUTH);
        } else {
            mDataManager.getConnector().runOperation(new ChronosLoadUsers(), ConstantManager.CHRONOS_LOAD_USERS, true);
        }
    }

    public void onOperationFinished(final ChronosLoadUsers.Result result) {
        if (result.isSuccessful()) {
            Intent login = new Intent(SplashScreen.this, UserListActivity.class);
            this.finish();
            startActivity(login);
        } else {
            showToast(getApplicationContext().getResources().getString(R.string.unexpected_error)+" "+result.getErrorMessage());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_AUTH:

                if (resultCode != RESULT_OK || mDataManager.getPreferencesManager().getAuthToken().isEmpty()) {
                    this.finish();
                } else {
                    mDataManager.getConnector().runOperation(new ChronosLoadUsers(), ConstantManager.CHRONOS_LOAD_USERS,true);
                }
                break;
        }
    }



    /**
     * Показываем сообщение в нижней полоске
     *
     * @param message
     */
    private void showToast(String message) {
        Toast.makeText(this, message,Toast.LENGTH_LONG).show();
        Log.d("DEV", message);
    }

}
