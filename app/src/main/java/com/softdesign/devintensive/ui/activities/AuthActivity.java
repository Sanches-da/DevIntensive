package com.softdesign.devintensive.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.softdesign.devintensive.R;

public class AuthActivity extends BaseActivity implements View.OnClickListener {

    private Intent mIntent = null;

    /**
     * Инициализация внутренних переменных активити
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mIntent = getIntent();

        TextView signInBtn = (TextView) findViewById(R.id.login_btn);
        signInBtn.setOnClickListener(this);
    }

    /**
     * Обработчик нажатий на различные объекты
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                if (mIntent == null) {
                    setResult(Activity.RESULT_OK);
                }else{
                    setResult(Activity.RESULT_OK, mIntent);
                }
                onBackPressed();
                break;
        }
    }

    /**
     * Обработчик нажатия на кнопку "Назад"
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
