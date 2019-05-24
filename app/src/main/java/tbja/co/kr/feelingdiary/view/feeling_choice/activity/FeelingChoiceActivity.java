package tbja.co.kr.feelingdiary.view.feeling_choice.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.otto.Subscribe;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Semaphore;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import tbja.co.kr.feelingdiary.R;
import tbja.co.kr.feelingdiary.box2d.Game;
import tbja.co.kr.feelingdiary.box2d.model.Ball;
import tbja.co.kr.feelingdiary.box2d.model.Diary;
import tbja.co.kr.feelingdiary.box2d.model.FeelName;
import tbja.co.kr.feelingdiary.customviews.DrawerMenu;
import tbja.co.kr.feelingdiary.dialog.WaitTimeDialog;
import tbja.co.kr.feelingdiary.event.BusProvider;
import tbja.co.kr.feelingdiary.event.NotifySoundOptChanged;
import tbja.co.kr.feelingdiary.realmodel.DiaryRealm;
import tbja.co.kr.feelingdiary.realmodel.FirstDiaryInfo;
import tbja.co.kr.feelingdiary.realmodel.SelectedBallRealm;
import tbja.co.kr.feelingdiary.service.BGMService;
import tbja.co.kr.feelingdiary.sharedpre.SharedPreManager;
import tbja.co.kr.feelingdiary.sound.FSounds;
import tbja.co.kr.feelingdiary.utils.DiaryUtil;
import tbja.co.kr.feelingdiary.utils.PopupUtil;
import tbja.co.kr.feelingdiary.utils.Util;
import tbja.co.kr.feelingdiary.view.FActivity;
import tbja.co.kr.feelingdiary.view.FNOBGMActivity;
import tbja.co.kr.feelingdiary.view.balllist.activity.BallListActivity;
import tbja.co.kr.feelingdiary.view.moveball.activity.BallMoveActivity;
import tbja.co.kr.feelingdiary.view.selectemotionball.activity.SelectEmotionBallActivity;

public class FeelingChoiceActivity extends FNOBGMActivity implements GLSurfaceView.Renderer {
    public static Integer SELECT_PICTURE = 0;
    public static Integer SELECT_BALL = 1;
    @BindView(R.id.main_canvas) GLSurfaceView glSurfaceView;
    @BindView(R.id.main_layout_body) RelativeLayout LayoutBody;
    @BindView(R.id.input_popup) RelativeLayout input_popup;
    @BindView(R.id.btn_close_popup) Button btn_close_popup;
    @BindView(R.id.input_content) EditText input_content;
    @BindView(R.id.btn_add_pic) ImageView btn_add_pic;
    @BindView(R.id.input_inner_popup) RelativeLayout input_inner_popup;
    @BindView(R.id.main_splash) ImageView main_splash;
    @BindView(R.id.text_diary_date) TextView text_diary_date;
    @BindView(R.id.text_popup_date) TextView text_popup_date;
    @BindView(R.id.input_title_popup) RelativeLayout input_title_popup;
    @BindView(R.id.btn_input_title_close) ImageView btn_input_title_close;
    @BindView(R.id.text_diary_title) TextView text_diary_title;
    @BindView(R.id.et_diary_title) EditText et_diary_title;
    @BindView(R.id.main_drawer) LinearLayout main_drawer;
    @BindView(R.id.img_drawer_icon) ImageView img_drawer_icon;
    @BindView(R.id.main_drawer_container) DrawerMenu main_drawer_container;
    @BindView(R.id.btn_move_list) ImageView btn_move_list;

    Semaphore semaphore = new Semaphore(1,true);
    SensorManager manager;
    Sensor sensor;
    AccelerometerListener listener;
    float ax, ay, az;
    Game game;
    long lastTicks = -1;
    String imgPath = "";
    Uri selectedImageUri = null;
    ProgressDialog dialog;

    Handler handler = new Handler();

    Ball currBall = null;

    FSounds fSounds;
    FeelName feelName;

    int year;
    int month;
    int day;

    boolean drawerOpen = false;

    private final Runnable uiRunnable = new Runnable() {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        semaphore.acquire(1);
                        game.UI();
                    }
                    catch (Exception e)
                    {

                    }
                    finally
                    {
                        semaphore.release(1);
                        //LayoutBody.setVisibility(View.VISIBLE);


                    }
                }
            });

        }
    };

    SharedPreManager sharedPreManager;
    Thread soundThread = null;

    public BGMService bgmService;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BGMService.LocalBinder binder = (BGMService.LocalBinder) iBinder;
            bgmService = binder.getService();

            bgmService.startBGM();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bgmService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeling_choice);
        ButterKnife.bind(this);

        Realm.init(FeelingChoiceActivity.this);
        feelName = new FeelName(FeelingChoiceActivity.this);
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);


        Intent intent = getIntent();
        int dYear = intent.getIntExtra("year",0);
        int dMonth = intent.getIntExtra("month",0);
        int dDay = intent.getIntExtra("day",0);

        if (dYear != 0) {
            year = dYear;
            month = dMonth;
            day = dDay;
        }

        sharedPreManager = new SharedPreManager(FeelingChoiceActivity.this);

        fSounds = new FSounds(this);

        //LayoutBody.setVisibility(View.INVISIBLE);

        text_diary_date.setText(DiaryUtil.getMonthStr(month) + ", " + day);
        text_popup_date.setText(DiaryUtil.getMonthStr(month) + ", " + day);

        manager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new AccelerometerListener();
        manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(this);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        game = new Game(this,semaphore,year,month,day);
        game.setBigBallClickListener(new Game.BigBallClickListener() {
            @Override
            public void onClickBigBall(Ball ball) {
                fSounds.playBallClick();
                if (isExistTodayDiary(year,month,day)) {
                    WaitTimeDialog dialog = new WaitTimeDialog(FeelingChoiceActivity.this);
                    Realm realm = Realm.getDefaultInstance();

                    RealmResults<DiaryRealm> diaries = realm.where(DiaryRealm.class).
                            equalTo("year",year).equalTo("month",month).equalTo("day",day).findAll();

                    Diary diary = Diary.copyFromRealmObj(diaries.first());

                    realm.close();
                    Calendar c = Calendar.getInstance();
                    int hour = 23-c.get(Calendar.HOUR_OF_DAY);
                    int min = 60-c.get(Calendar.MINUTE);

                    dialog.setRemainingTime(hour,min);
                    dialog.setTodayFeeling(feelName.getFeelingOneName(diary.getBallColor()));
                    dialog.show();
                } else {
                    currBall = ball;
                    input_inner_popup.setBackgroundResource(PopupUtil.getPopupImageRes(ball.getBallColor()));
                    input_popup.setVisibility(View.VISIBLE);
                    Animation anim2 = AnimationUtils.loadAnimation(FeelingChoiceActivity.this, R.anim.popup_show_anim);
                    input_inner_popup.startAnimation(anim2);
                }
            }
        });

        game.getB2World().setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {


            }

            @Override
            public void endContact(Contact contact) {
                final Object fix1 = contact.getFixtureA().getBody().getUserData();
                final Object fix2 = contact.getFixtureB().getBody().getUserData();
                if (soundThread == null) {
                    soundThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (fix1 instanceof Ball) {
                                fSounds.playBallCollisoin();
                            }
                            soundThread = null;
                        }
                    });
                    soundThread.start();
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold manifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse) {
            }
        });

        setDiaryTitle(sharedPreManager.loadTitle());
        Animation anim = AnimationUtils.loadAnimation(FeelingChoiceActivity.this, R.anim.arrow_down);
        btn_move_list.startAnimation(anim);

        if (isFirstDiary()) {
            insertFirstDiaryDate(year,month,day);
        }


        if (!isExistTodayDiary(year,month,day)) {
            Toast.makeText(FeelingChoiceActivity.this, getString(R.string.toast_empty_add_diary, month + 1, day), Toast.LENGTH_LONG).show();
            if (isExistRealTodayDiary(year,month,day)) {
                //Toast.makeText(FeelingChoiceActivity.this, getString(R.string.toast_today_diary), Toast.LENGTH_LONG).show();
            } else {

            }
        }

        Intent intent2 = new Intent(FeelingChoiceActivity.this,BGMService.class);
        startService(intent2);
        bindService(intent2,connection,BIND_AUTO_CREATE);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        try
        {
            semaphore.acquire(1);
            GLES20.glClearColor(100.0f / 255.0f, 149f / 255.0f, 237f / 255.0f, 255f / 255.0f);
            game.Init(R.drawable.main_bg_2);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    game.Reset2();
                }
            }).start();
            //runOnUiThread(uiRunnable);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            semaphore.release(1);
        }
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        try
        {
            semaphore.acquire(1);
            gl.glViewport(0, 0, width, height);
            game.SetSize(width, height);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            semaphore.release(1);
            GLU.gluLookAt(gl,0,5,0,0,0,0,0,0,0);
        }
    }
    float Accumulator = 0.0f;
    public void onDrawFrame(GL10 gl) {
        try
        {
            semaphore.acquire(1);
            if(lastTicks == -1) lastTicks = Calendar.getInstance().getTime().getTime();
            final float min_timestep = 1.0f / 100.0f;
            // Calculate Delta Ticks
            long nowticks = Calendar.getInstance().getTime().getTime();
            Accumulator += (float) (nowticks - lastTicks) / 1000.0f;
            lastTicks = nowticks;
            // Update for the total amount of time and any remainder. This ensures smoothest framerate.
            while (Accumulator > min_timestep) {
                game.Update(min_timestep);
                Accumulator -= min_timestep;
            }
            //game.Update(total_delta);
            game.Draw(ax,ay,az);
            runOnUiThread(uiRunnable);
            GLU.gluLookAt(gl, 0, 0, -5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        }
        catch (Exception e)
        {

        }
        finally
        {
            semaphore.release(1);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try
        {
            semaphore.acquire(1);
            game.TouchEvent(event);
        }
        catch (Exception e)
        {

        }
        finally
        {
            semaphore.release(1);
        }
        return true;
    }

    public void pickImage() {

        Toast.makeText(FeelingChoiceActivity.this,getString(R.string.toast_input_photo),Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                selectedImageUri = data.getData();
                imgPath = Util.getRealPath(FeelingChoiceActivity.this,selectedImageUri);

                if (imgPath == null || imgPath.equals("")) {
                    selectedImageUri = null;
                    Toast.makeText(FeelingChoiceActivity.this,getText(R.string.toast_image_load_fail),Toast.LENGTH_LONG).show();
                } else {
                    Glide.with(this)
                            .load(data.getData())
                            .into(btn_add_pic);
                }
            } else if (requestCode == SELECT_BALL) {
                //game.loadFeelingBalls();
                //game.removeAllFeelingBalls();
            }
        }

    }

    public void saveDiary() {
        final String content =input_content.getText().toString();

        String fileName ="";

        if (selectedImageUri != null) {
            Bitmap bitmap = null;
            ExifInterface exif = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                exif = new ExifInterface(imgPath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            bitmap = Util.rotateBitmap(bitmap, orientation);
            fileName = DiaryUtil.getDiaryFileName(year,month,day);

            Util.saveImage(FeelingChoiceActivity.this, bitmap, FeelingChoiceActivity.this.getFilesDir().getPath(), fileName);
        }

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        RealmResults<DiaryRealm> diaries = realm.where(DiaryRealm.class).
                equalTo("year",year).equalTo("month",month).equalTo("day",day).findAll();
        diaries.deleteAllFromRealm();

        Number num = realm.where(DiaryRealm.class).max("id");
        int nextId;
        if (num == null) {
            nextId = 1;
        } else {
            nextId = num.intValue() + 1;
        }
        DiaryRealm diaryRealm = realm.createObject(DiaryRealm.class,nextId);
        diaryRealm.setContent( content);
        diaryRealm.setImg_src(fileName);
        diaryRealm.setYear(year);
        diaryRealm.setMonth(month);
        diaryRealm.setDay(day);
        diaryRealm.setBallColor(currBall.getBallColor());

        realm.commitTransaction();
        realm.close();

    }

    public void setDiaryTitle(String title) {

        if (!title.equals("")) {
            text_diary_title.setText(title);
        } else {
            text_diary_title.setText(getString(R.string.no_title));
        }
    }


    public void openDrawer() {
        main_drawer_container.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(FeelingChoiceActivity.this,R.anim.drawer_left_to_right);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                drawerOpen = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        main_drawer.setVisibility(View.VISIBLE);
        main_drawer.startAnimation(anim);
    }

    public void closeDrawer() {
        Animation anim = AnimationUtils.loadAnimation(FeelingChoiceActivity.this,R.anim.drawer_right_to_left);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                main_drawer.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                drawerOpen = false;
                main_drawer_container.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        main_drawer.startAnimation(anim);
    }

    public boolean isExistTodayDiary(int year,int month, int day) {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<DiaryRealm> diaries = realm.where(DiaryRealm.class).
                equalTo("year",year).equalTo("month",month).equalTo("day",day).findAll();

        if (diaries.size() > 0) {
            realm.close();
            return true;
        }

        realm.close();
        return false;
    }

    public boolean isExistRealTodayDiary(int year,int month, int day) {
        Calendar c = Calendar.getInstance();
        int tY = c.get(Calendar.YEAR);
        int tM = c.get(Calendar.MONTH);
        int tD = c.get(Calendar.DAY_OF_MONTH);

        if (tY == year && tM == month && tD == day) {
            return true;
        }

        return false;
    }

    public boolean isFirstDiary() {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<FirstDiaryInfo> diaries = realm.where(FirstDiaryInfo.class).findAll();

        if (diaries.size() > 0) {
            realm.close();
            return false;
        }

        realm.close();
        return true;
    }

    public void insertFirstDiaryDate(int year,int month, int day) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        RealmResults<FirstDiaryInfo> diaries = realm.where(FirstDiaryInfo.class).
                equalTo("year",year).equalTo("month",month).equalTo("day",day).findAll();
        diaries.deleteAllFromRealm();


        FirstDiaryInfo diaryRealm = realm.createObject(FirstDiaryInfo.class);
        diaryRealm.setYear(year);
        diaryRealm.setMonth(month);
        diaryRealm.setDay(day);

        realm.commitTransaction();
        realm.close();
    }

    @OnClick(R.id.main_drawer_container)
    public void clickMainDrawerContainer(View view) {
        closeDrawer();
    }

    @OnClick(R.id.img_drawer_icon)
    public void clickImgDrawerIcon(View view) {
        //main_drawer_container.openDrawer(main_drawer);
        openDrawer();

    }

    @OnClick(R.id.main_drawer)
    public void clickMainDrawer(View view) {

    }

    @OnClick(R.id.text_diary_title)
    public void clickTextDiaryTitle(View view) {
        input_title_popup.setVisibility(View.VISIBLE);
        text_diary_title.setVisibility(View.INVISIBLE);

        et_diary_title.setText(sharedPreManager.loadTitle());
    }

    @OnClick(R.id.input_title_popup)
    public void clickInputTItlePopup(View view) {
        clickInputTitleClose(null);
    }

    @OnClick(R.id.btn_input_title_close)
    public void clickInputTitleClose(View view) {
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        input_title_popup.setVisibility(View.GONE);
        text_diary_title.setVisibility(View.VISIBLE);

        sharedPreManager.saveTitle(et_diary_title.getText().toString());
        setDiaryTitle(sharedPreManager.loadTitle());
    }

    @OnClick(R.id.btn_save)
    public void clickBtnSave(View view) {
        SaveTask saveTask =new SaveTask();
        saveTask.execute();
    }


    @OnClick(R.id.btn_add_pic)
    public void clickBtnAddPic(View view) {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                pickImage();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            }


        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    @OnClick({R.id.btn_close_popup,R.id.btn_cancel})
    public void clickBtnClosePopup(View view) {
        input_popup.setVisibility(View.GONE);
    }


    @OnClick(R.id.btn_move_page)
    public void clickBtnMovePage(View view) {
        Intent intent = new Intent(FeelingChoiceActivity.this, SelectEmotionBallActivity.class);
        startActivityForResult(intent,SELECT_BALL);
    }

    @OnClick(R.id.btn_move_list)
    public void clickBtnMoveList(View view) {
        Intent intent = new Intent(FeelingChoiceActivity.this, BallListActivity.class);
        startActivity(intent);
    }

    private class SaveTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(FeelingChoiceActivity.this, "Title", "로딩중..", true);
            dialog.setCancelable(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            saveDiary();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();

            Intent intent = new Intent(FeelingChoiceActivity.this,BallMoveActivity.class);
            intent.putExtra("ballColor",currBall.getBallColor());
            intent.putExtra("year",year);
            intent.putExtra("month",month);
            intent.putExtra("day",day);
            intent.putExtra("newBallLoad",true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
        if (bgmService != null) {
            bgmService.startBGM();
        }
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        game.removeAllBodies();
        glSurfaceView.onPause();
        if (bgmService != null) {
            bgmService.pauseBGM();
        }
        BusProvider.getInstance().unregister(this);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (input_popup.getVisibility() == View.VISIBLE) {
            input_popup.setVisibility(View.GONE);
        } else if (input_title_popup.getVisibility() == View.VISIBLE) {
            clickInputTItlePopup(null);
        } else if (drawerOpen) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }

    }

    class AccelerometerListener implements SensorEventListener {
        public void onSensorChanged(SensorEvent event) {
            ax = event.values[0];
            ay = event.values[1];
            az = event.values[2];
        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    @Override
    protected void onDestroy() {
        if (fSounds != null) {
            fSounds.destroy();
        }

        if (game != null) {
            game.onDestroy();
        }

        if (bgmService != null ) {
            unbindService(connection);
        }

        super.onDestroy();
    }




    @Subscribe
    public void soundOptionChange(NotifySoundOptChanged event) {
        fSounds.setSound(event.isSoundOpt());

        if (!event.isSoundOpt()) {
            if (bgmService != null) {
                bgmService.pauseBGM();
            }
        } else {
            if (bgmService != null) {
                bgmService.startBGM();
            }
        }
    }

}
