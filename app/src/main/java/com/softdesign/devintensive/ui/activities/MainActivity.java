package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.utils.CircleTransform;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";
    private DataManager mDataManager;
    @BindView(R.id.main_coordinator_container)
    private CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)
    private Toolbar mToolbar;
    @BindView(R.id.navigation_drawer)
    private DrawerLayout mNavigationDrawer;
    @BindView(R.id.fab)
    private FloatingActionButton mFloatingActionButton;
    @BindView(R.id.profile_placeholder)
    private RelativeLayout mProfilePlaceholder;
    @BindView(R.id.collapsing_toolbar)
    private CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.user_photo_img)
    private ImageView mProfileImage;
    @BindView(R.id.appbar_layout)
    private AppBarLayout mAppBarLayout;
    @BindView(R.id.phone_et)
    private EditText mUserPhone;
    @BindView(R.id.email_et)
    private EditText mUserMail;
    @BindView(R.id.vk_et)
    private EditText mUserVK;
    @BindView(R.id.git_1_et)
    private EditText mUserGit;
    @BindView(R.id.about_et)
    private EditText mUserAbout;
    @BindViews({R.id.phone_et, R.id.email_et, R.id.vk_et, R.id.git_1_et, R.id.about_et})
    private List<EditText> mUserInfo;

    private boolean mEditMode = false;
    @BindView(R.id.btn_profile_call)
    private ImageView mBtnCall;
    @BindView(R.id.btn_profile_send)
    private ImageView mBtnSend;
    @BindView(R.id.btn_profile_vk)
    private ImageView mBtnVK;
    @BindView(R.id.btn_profile_git)
    private ImageView mBtnGit;

    @BindView(R.id.user_name_txt)
    private TextView mUserNameDrawer;
    @BindView(R.id.user_email_txt)
    private TextView mUserMailDrawer;
    @BindView(R.id.drawer_avatar)
    private ImageView mUserAvatarDrawer;
    @BindViews({R.id.user_name_txt, R.id.user_email_txt})
    private List<TextView> mUserDrawer;


    @BindViews({R.id.rating_et, R.id.code_lines_et, R.id.projects_et})
    private List<TextView> mUserValues;
    @BindView(R.id.rating_et)
    private TextView mUserRating;
    @BindView(R.id.code_lines_et)
    private TextView mUserCode;
    @BindView(R.id.projects_et)
    private TextView mUserProjects;

    private AppBarLayout.LayoutParams mAppbarLayoutParams = null;
    private File mPhotoFile = null;
    private Uri mSelectedImage;


    /**
     * Инициализация переменных класса
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mProfilePlaceholder = (RelativeLayout) findViewById(R.id.profile_placeholder);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        mProfileImage = (ImageView) findViewById(R.id.user_photo_img);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mBtnCall = (ImageView) findViewById(R.id.btn_profile_call);
        mBtnSend = (ImageView) findViewById(R.id.btn_profile_send);
        mBtnVK = (ImageView) findViewById(R.id.btn_profile_vk);
        mBtnGit = (ImageView) findViewById(R.id.btn_profile_git);

        setupToolbar();
        setupDrawer();


        mFloatingActionButton.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);
        mBtnCall.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
        mBtnVK.setOnClickListener(this);
        mBtnGit.setOnClickListener(this);

        NavigationView drawerLayout = (NavigationView) findViewById(R.id.navigation_view);
        View drawerView = drawerLayout.getHeaderView(0);
        mUserNameDrawer = (TextView)drawerView.findViewById(R.id.user_name_txt);
        mUserMailDrawer = (TextView)drawerView.findViewById(R.id.user_email_txt) ;
        mUserAvatarDrawer = (ImageView)drawerView.findViewById(R.id.drawer_avatar) ;

        mUserDrawer = new ArrayList<>();
        mUserDrawer.add(mUserNameDrawer);
        mUserDrawer.add(mUserMailDrawer);

        mUserPhone = (EditText) findViewById(R.id.phone_et);
        mUserMail = (EditText) findViewById(R.id.email_et);
        mUserVK = (EditText) findViewById(R.id.vk_et);
        mUserGit = (EditText) findViewById(R.id.git_1_et);
        mUserAbout = (EditText) findViewById(R.id.about_et);

        mUserInfo = new ArrayList<>();
        mUserInfo.add(mUserPhone);
        mUserInfo.add(mUserMail);
        mUserInfo.add(mUserVK);
        mUserInfo.add(mUserGit);
        mUserInfo.add(mUserAbout);

        mUserRating = (TextView) findViewById(R.id.rating_et);
        mUserCode = (TextView) findViewById(R.id.code_lines_et);
        mUserProjects = (TextView) findViewById(R.id.projects_et);
        mUserValues = new ArrayList<>();
        mUserValues.add(mUserRating);
        mUserValues.add(mUserCode);
        mUserValues.add(mUserProjects);


        mDataManager = DataManager.getInstance();



        loadUserFields();
        loadUserInfoValues();
        loadDrawerInfo();

        Uri tempProfileImage = mDataManager.getPreferencesManager().loadUserPhoto();
        if (tempProfileImage != null) {
            insertProfileImage(tempProfileImage);
        }
        Uri tempAvatarImage = mDataManager.getPreferencesManager().loadUserAvatar();
        if (tempAvatarImage != null) {
            insertProfileAvatar(tempAvatarImage);
        }

        if (savedInstanceState == null) {
            //Впервые;
        } else {
            mEditMode = savedInstanceState.getBoolean(ConstantManager.EDIT_MODE_KEY, false);
        }
        changeEditMode(mEditMode);

        Log.d(TAG, "onCreate");
    }

    /**
     * Обработка нажатия на меню - открытие бокового меню
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mNavigationDrawer != null) {
                mNavigationDrawer.openDrawer(GravityCompat.START, true);
            }
        }


        return super.onOptionsItemSelected(item);

    }

    /**
     * Обработчик запуска активити
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    /**
     * Обработчик восстановления активити
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    /**
     * Обработчик приостановки активити
     */
    @Override
    protected void onPause() {
        super.onPause();

        if (mEditMode) {
            saveUserFields();
        }
        Log.d(TAG, "onPause");
    }

    /**
     * Обработчик остановки активити
     */
    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "onStop");
    }

    /**
     * Обработчик завершения активити
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    /**
     * Обработчик перезапуска активити
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    /**
     * Показываем сообщение в нижней полоске
     *
     * @param message
     */
    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Настройка параметров тулбара
     */
    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        mAppbarLayoutParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Настройка бокового меню
     */
    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);

                if (item.getItemId() == R.id.user_profile_auth) { //Запуск активити логина
                    Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                    startActivityForResult(intent, ConstantManager.REQUEST_AUTH);
                    return false;
                }

                showSnackbar(item.getTitle().toString());
                return false;
            }
        });
    }

    /**
     * Обработка сохранения состояния данных приложения
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ConstantManager.EDIT_MODE_KEY, mEditMode);
    }

    /**
     * Обработчик нажатия на различные эленменты
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                changeEditMode(!mEditMode);
                break;
            case R.id.profile_placeholder:
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
            case R.id.btn_profile_call:
                if (mUserPhone != null) {
                    callPhoneNumber(mUserPhone.getText().toString());
                }
                break;
            case R.id.btn_profile_send:
                if (mUserMail != null) {
                    sendEmailTo(mUserMail.getText().toString());
                }
                break;
            case R.id.btn_profile_vk:
                if (mUserVK != null) {
                    browseLink(mUserVK.getText().toString());
                }
                break;
            case R.id.btn_profile_git:
                if (mUserGit != null) {
                    browseLink(mUserGit.getText().toString());
                }
                break;
        }
    }

    /**
     * Открываем браузер по ссылке  link
     *
     * @param link
     */
    private void browseLink(String link) {
        if (link.isEmpty()) {
            showSnackbar(getString(R.string.message_empty_link));
            return;
        }
        String url = (link.startsWith("http:") || link.startsWith("https:")) ? link : "http://" + link;
        Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(Intent.createChooser(browser, getString(R.string.message_browser_chooser)));
    }

    /**
     * Открывает новое письмо с введенным получателем
     *
     * @param email
     */
    private void sendEmailTo(String email) {
        if (email.isEmpty()) {
            showSnackbar(getString(R.string.message_empty_email));
            return;
        }
        Intent mailer = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
        startActivity(Intent.createChooser(mailer, getString(R.string.message_send_email_chooser)));
    }

    /**
     * Звонит по номеру, переданному в параметре
     *
     * @param phoneNumber
     */
    private void callPhoneNumber(String phoneNumber) {
        if (phoneNumber.isEmpty()) {
            showSnackbar(getString(R.string.message_empty_phone_number));
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

            Intent caller = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            startActivity(caller);

        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.CALL_PHONE
                    },
                    ConstantManager.REQUEST_PERMISSION_PHONE_CODE);
            Snackbar.make(mCoordinatorLayout, getString(R.string.permission_request_snackbar), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.permission_allow_snackbar), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openApplicationSettings();
                        }
                    })
                    .show();
        }

    }

    /**
     * Обработчик нажатия на клавиши для перехвата нажатия на кнопку "Назад"
     */
    @Override
    public void onBackPressed() {
        if (mNavigationDrawer != null)
            if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
                mNavigationDrawer.closeDrawer(GravityCompat.START, true);
                return;
            }

        super.onBackPressed();
    }

    /**
     * получение состояния завершения вызванной активности
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);
                    ServiceGenerator.uploadFile(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);
                    ServiceGenerator.uploadFile(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_AUTH:
                if (resultCode == RESULT_OK) {
                    showSnackbar("User is logged in!");
                }
                break;
        }

    }


    /**
     * Переключает режим редактирования данных
     *
     * @param mode
     */
    private void changeEditMode(boolean mode) {
        if (!validateUserInfo())
            return;

        mEditMode = mode;
        for (EditText uv : mUserInfo) {
            uv.setEnabled(mode);
            uv.setFocusable(mode);
            uv.setFocusableInTouchMode(mode);
        }

        if (mEditMode) {
            mFloatingActionButton.setImageResource(R.drawable.ic_done_black_24dp);
            showProfilePlaceholder();
            lockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
            setFocusToEdit(mUserPhone);

        } else {
            mFloatingActionButton.setImageResource(R.drawable.ic_mode_edit_black_24dp);
            hideProfilePlaceholder();
            unlockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.color_white));
            saveUserFields();
        }

    }

    /**
     * Устанавливает фокус на поле ввода с вызовов клавиатуры, если доступно для ввода
     *
     * @param field
     */
    private void setFocusToEdit(EditText field) {
        if (!field.isFocusable())
            return;
        field.requestFocus();
        //show keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(field, InputMethodManager.SHOW_IMPLICIT);

    }

    /**
     * Проверяет на корректность введенные пользователем поля
     *
     * @return boolean
     */
    private boolean validateUserInfo() {
        String phoneNumber = mUserPhone.getText().toString();
        if (!phoneNumber.isEmpty()) {
            if (phoneNumber.length() < 11 || phoneNumber.length() > 20) {
                showToast(getString(R.string.message_error_phone_length));
                setFocusToEdit(mUserPhone);
                return false;
            }
            if (!phoneNumber.matches("^(\\+?\\d+)?\\s*([\\( ]\\d+[\\) ])?[\\s-]*([\\d-]*)$")) {
                showToast(getString(R.string.message_error_phone_format));
                setFocusToEdit(mUserPhone);
                return false;
            }
        }

        String email = mUserMail.getText().toString();
        if (!email.isEmpty()) {
            if (!email.matches("^[a-z0-9_]([a-z0-9_-]+\\.*)+[a-z0-9_]@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$")) {
                showToast(getString(R.string.message_error_email_format));
                setFocusToEdit(mUserMail);
                return false;
            }
        }

        //общее для урл адресов ^((https?)://)?([a-z0-9])((\.[a-z0-9-])|([a-z0-9-]))*\.([a-z]{2,6})[(/?)|[a-zA-Z0-9_-]+(/?)]?$
        String url_vk = mUserVK.getText().toString();
        url_vk = url_vk.replace("\\", "/");
        if (!url_vk.isEmpty()) {
            if (!url_vk.matches("^((https?)://)?vk.com/[a-zA-Z0-9_-]+$")) {
                showToast(getString(R.string.message_error_url_vk_format));
                setFocusToEdit(mUserVK);
                return false;
            }
        }
        url_vk = url_vk.replaceAll("((https?)://)?", "");
        mUserVK.setText(url_vk);

        String url_git = mUserGit.getText().toString();
        url_git = url_git.replace("\\", "/");
        if (!url_git.isEmpty()) {
            if (!url_git.matches("^((https?)://)?github.com/[a-zA-Z0-9-]+/[a-zA-Z0-9-]+$")) {
                showToast(getString(R.string.message_error_url_git_format));
                setFocusToEdit(mUserGit);
                return false;
            }
        }
        url_git = url_git.replaceAll("((https?)://)?", "");
        mUserGit.setText(url_git);

        return true;
    }

    /**
     * Загружает данные пользователя
     */
    private void loadUserFields() {
        List<String> data = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int i = 0; i < data.size(); i++) {
            mUserInfo.get(i).setText(data.get(i));
        }
    }


    /**
     * Сохраняет данные пользователя
     */
    private void saveUserFields() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < mUserInfo.size(); i++) {
            data.add(mUserInfo.get(i).getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(data);

    }

    /**
     * Загружает поля рейтинга из профиля пользователя
     */
    private void loadUserInfoValues(){
        List<String> data = mDataManager.getPreferencesManager().getUserInfoValues();
        for (int i = 0; i < data.size(); i++) {
            mUserValues.get(i).setText(data.get(i));
        }
    }

    /**
     * Загружает поля рейтинга из профиля пользователя
     */
    private void loadDrawerInfo(){
        List<String> data = mDataManager.getPreferencesManager().getDrawerInfo();
        for (int i = 0; i < data.size(); i++) {
            mUserDrawer.get(i).setText(data.get(i));
        }
    }

    /**
     * Запуск намерения выбрать фото из галереи
     */
    private void loadPhotoFromGallery() {
        Intent takeGalleryPicture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        takeGalleryPicture.setType("image/");

        startActivityForResult(Intent.createChooser(takeGalleryPicture, getString(R.string.user_profile_choose_image_title)), ConstantManager.REQUEST_GALLERY_PICTURE);
    }

    /**
     * Запуск активности фотокамеры для получения снимка
     */
    private void loadPhotoFromCamera() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                showToast(getString(R.string.message_error_creating_file));
                return;
            }

            if (mPhotoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takePictureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    ConstantManager.REQUEST_PERMISSION_CAMERA_CODE);
            Snackbar.make(mCoordinatorLayout, getString(R.string.permission_request_snackbar), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.permission_allow_snackbar), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openApplicationSettings();
                        }
                    })
                    .show();
        }
    }

    /**
     * Обработчик ответа на запрос разрешегий для приложения
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ConstantManager.REQUEST_PERMISSION_CAMERA_CODE:
                if (grantResults.length == 2) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        loadPhotoFromCamera();
                    } else {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            showSnackbar(getString(R.string.permission_denied_camera));
                        }
                        if (grantResults[1] == PackageManager.PERMISSION_DENIED) {
                            showSnackbar(getString(R.string.permission_denied_storage));
                        }
                    }
                }
                break;
        }
    }

    /**
     * прячет контейнер профиля для загрузки фото
     */
    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    /**
     * показывает контейнер профиля для загрузки фото
     */
    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    /**
     * Зафиксировать тулбар от сворачивания
     */
    private void lockToolbar() {
        mAppBarLayout.setExpanded(true, true);
        mAppbarLayoutParams.setScrollFlags(0);
        mCollapsingToolbar.setLayoutParams(mAppbarLayoutParams);
    }

    /**
     * Разрешить тулбару от сворачиваться
     */
    private void unlockToolbar() {
        mAppbarLayoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbar.setLayoutParams(mAppbarLayoutParams);
    }

    /**
     * Вывод диалога для выбора варианта значения
     *
     * @param id
     * @return
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {getString(R.string.user_profile_dialog_gallery), getString(R.string.user_profile_dialog_photo), getString(R.string.user_profile_dialog_cancel)};
                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle(R.string.user_profile_dialog_title);
                dialog.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int choiceItem) {
                        switch (choiceItem) {
                            case 0:

                                loadPhotoFromGallery();
                                break;
                            case 1:
                                loadPhotoFromCamera();
                                break;
                            case 2:
                                dialogInterface.cancel();
                        }
                    }
                });
                return dialog.create();

            default:
                return null;
        }

    }

    /**
     * Создание временного файла для передачи в фотокамеру
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        storageDir.mkdirs();


        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());

        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;
    }

    /**
     * Замена фото профиля на полченную с галереи или камеры
     *
     * @param selectedImage
     */
    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .placeholder(R.drawable.header_bg)
                .resizeDimen(R.dimen.profile_photo_width, R.dimen.profile_photo_height)
                .centerCrop()
                .into(mProfileImage);

        mDataManager.getPreferencesManager().saveUserPhoto(selectedImage);
    }

    private void insertProfileAvatar(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .placeholder(R.drawable.avatar)
                .resizeDimen(R.dimen.profile_avatar_size, R.dimen.profile_avatar_size)
                .transform(new CircleTransform())
                .centerCrop()
                .into(mUserAvatarDrawer);

        mDataManager.getPreferencesManager().saveUserAvatar(selectedImage);
    }

    /**
     * Запуск активности  - открытия настроек приложения
     */
    private void openApplicationSettings() {
        Intent appSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));

        startActivityForResult(appSettings, ConstantManager.REQUEST_PERMISSION_CODE);
    }

}

