package tbja.co.kr.feelingdiary.view.balllist.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.Semaphore;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import tbja.co.kr.feelingdiary.R;
import tbja.co.kr.feelingdiary.box2d.Game;
import tbja.co.kr.feelingdiary.box2d.model.Ball;
import tbja.co.kr.feelingdiary.box2d.model.Diary;
import tbja.co.kr.feelingdiary.customviews.DrawerMenu;
import tbja.co.kr.feelingdiary.realmodel.DiaryRealm;
import tbja.co.kr.feelingdiary.realmodel.FirstDiaryInfo;
import tbja.co.kr.feelingdiary.sound.FSounds;
import tbja.co.kr.feelingdiary.utils.DiaryUtil;
import tbja.co.kr.feelingdiary.utils.PopupUtil;
import tbja.co.kr.feelingdiary.utils.Util;
import tbja.co.kr.feelingdiary.view.FActivity;
import tbja.co.kr.feelingdiary.view.FNOBGMActivity;
import tbja.co.kr.feelingdiary.view.diaryimageview.activity.DiaryImageViewActivity;
import tbja.co.kr.feelingdiary.view.feeling_choice.activity.FeelingChoiceActivity;
import tbja.co.kr.feelingdiary.view.moveball.activity.BallMoveActivity;
import tbja.co.kr.feelingdiary.view.selectemotionball.activity.SelectEmotionBallActivity;

import static android.graphics.PixelFormat.TRANSLUCENT;


public class BallListActivity extends FNOBGMActivity implements Renderer {
    public static Integer SELECT_PICTURE = 0;
    @BindView(R.id.show_popup) RelativeLayout show_popup;
    @BindView(R.id.show_popup_inner) RelativeLayout show_popup_inner;
    @BindView(R.id.diary_content) TextView diary_content;
    @BindView(R.id.btn_close_popup) Button btn_close_popup;
    @BindView(R.id.diary_image) ImageView diary_image;
    @BindView(R.id.text_diary_list_popup_date) TextView text_diary_list_popup_date;
    @BindView(R.id.text_diary_now_month) TextView text_diary_now_month;
    @BindView(R.id.text_diary_month_str) TextView text_diary_month_str;
    @BindView(R.id.diary_detail_image) RelativeLayout diary_detail_image;
    @BindView(R.id.diary_zoom_image) ImageViewTouch diary_zoom_image;
    @BindView(R.id.img_ball_color) ImageView img_ball_color;
    @BindView(R.id.detail_date) TextView detail_date;
    @BindView(R.id.modify_popup) RelativeLayout modify_popup;
    @BindView(R.id.text_modify_popup_date) TextView text_modify_popup_date;
    @BindView(R.id.modify_content) EditText modify_content;
    @BindView(R.id.modify_inner_popup) RelativeLayout modify_inner_popup;
    @BindView(R.id.btn_modify_add_pic) ImageView btn_modify_add_pic;
    @BindView(R.id.game_canvas) GLSurfaceView glSurfaceView;
    @BindView(R.id.layout_body) RelativeLayout LayoutBody;
    @BindView(R.id.main_drawer) LinearLayout main_drawer;
    @BindView(R.id.main_drawer_container) DrawerMenu main_drawer_container;
    @BindView(R.id.diary_image_container) RelativeLayout diary_image_container;
    @BindView(R.id.text_diary_msg) TextView text_diary_msg;
    //@BindView(R.id.adView) AdView mAdView;


    Semaphore semaphore = new Semaphore(1,true);
    SensorManager manager;
    Sensor sensor;
    AccelerometerListener listener;
    float ax, ay, az;

    Handler handler = new Handler();

    private GestureDetectorCompat gestureDetectorCompat;

    Uri selectedImageUri = null;
    String imgPath = "";
    ProgressDialog dialog;

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
    Game game;
    long lastTicks = -1;

    int year;
    int month;
    int day;

    Ball curr;
    FSounds fSounds;

    boolean newBallLoad = false;
    boolean drawerOpen = false;

    Thread soundThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_beed_list);
        ButterKnife.bind(this);

        Calendar cal = Calendar.getInstance();

        final Intent intent = getIntent();
        newBallLoad = intent.getBooleanExtra("newBallLoad",false);
        year = intent.getIntExtra("year",cal.get(Calendar.YEAR));
        month = intent.getIntExtra("month",cal.get(Calendar.MONTH));
        day = intent.getIntExtra("day",cal.get(Calendar.DAY_OF_MONTH));

        fSounds = new FSounds(this);

        if (newBallLoad) {
            Toast.makeText(BallListActivity.this,getString(R.string.toast_save_diary),Toast.LENGTH_LONG).show();
        }

        setDate();

        //LayoutBody.setVisibility(View.INVISIBLE);

        manager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new AccelerometerListener();
        manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        //Chose EGL Config Here To Set Element Size For RGB data Alpha,
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(this);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        game = new Game(this,semaphore,year,month,day);

        game.setBallClickListener(new Game.BallClickListener() {
            @Override
            public void onClickBall(final Ball ball) {
                if (ball.getDiary().getId() == 0) {
                    if (show_popup.getVisibility() == View.VISIBLE) return;
                    if (modify_popup.getVisibility() == View.VISIBLE) return;

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(BallListActivity.this);
                    alertDialog.setTitle(getString(R.string.dialog_empty_ball_title));
                    alertDialog.setMessage(getString(R.string.dialog_empty_ball,ball.getDiary().getMonth()+1,ball.getDiary().getDay()));
                    alertDialog.setPositiveButton(getString(R.string.dialog_empty_ball_go_write), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent1 = new Intent(BallListActivity.this, FeelingChoiceActivity.class);
                            intent1.putExtra("year",ball.getDiary().getYear());
                            intent1.putExtra("month",ball.getDiary().getMonth());
                            intent1.putExtra("day",ball.getDiary().getDay());
                            startActivity(intent1);
                        }
                    });
                    alertDialog.show();
                } else {

                        ballClick(ball);
                }
            }
        });


        game.getB2World().setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                final Object fix1 = contact.getFixtureA().getBody().getUserData();
                final Object fix2 = contact.getFixtureB().getBody().getUserData();

                if (soundThread == null) {
                    soundThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (fix1 instanceof Ball) {
                                int dDay = ((Ball)fix1).getDiary().getDay();
                                if (dDay == game.getLastDay()) {
                                    fSounds.playBallCollisoin();
                                }
                            }

                            if (fix2 instanceof Ball) {
                                int dDay = ((Ball)fix2).getDiary().getDay();
                                if (dDay == game.getLastDay()) {
                                    fSounds.playBallCollisoin();
                                }
                            }
                            soundThread = null;
                        }
                    });
                    soundThread.start();
                }


            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold manifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse) {

            }
        });


        gestureDetectorCompat = new GestureDetectorCompat(this, new MyGestureListener());
        Realm.init(BallListActivity.this);
    }

    public void ballClick(Ball ball){
        curr = ball;
        text_diary_list_popup_date.setText(DiaryUtil.getMonthStr(ball.getDiary().getMonth()) + ", " + ball.getDiary().getDay());
        show_popup_inner.setBackgroundResource(PopupUtil.getPopupImageRes(ball.getDiary().getBallColor()));

        diary_content.setText(ball.getDiary().getContent());
        show_popup.setVisibility(View.VISIBLE);

        Animation anim3 = AnimationUtils.loadAnimation(BallListActivity.this, R.anim.popup_show_anim);
        show_popup_inner.startAnimation(anim3);

        if (!ball.getDiary().getImg_src().equals("")) {
            diary_image_container.setVisibility(View.VISIBLE);
            Glide.with(BallListActivity.this)
                    .load(getFilesDir() + "/" + ball.getDiary().getImg_src())
                    .signature(new StringSignature(UUID.randomUUID().toString()))
                    .into(diary_image);
        } else {
            diary_image_container.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (diary_detail_image.getVisibility() == View.VISIBLE) {
            diary_detail_image.setVisibility(View.GONE);
        } else if (show_popup.getVisibility() == View.VISIBLE) {
            show_popup.setVisibility(View.GONE);
        } else if (drawerOpen) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }

    }

    public void setDate() {
        text_diary_now_month.setText(((Integer)(month+1)).toString());
        text_diary_month_str.setText(DiaryUtil.getMonthStr(month));
    }

    @Override
    protected void onPause() {
        game.removeAllBodies();
        glSurfaceView.onPause();

        if (manager != null) {
            manager.unregisterListener(listener);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();


        if (manager != null) {
            manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }
	
	@Override
    protected void onStop() {
        super.onStop();
        if (Util.applicazioneInBackground(BallListActivity.this)) {
            finish();
        } else {
        }
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        try
        {
            semaphore.acquire(1);
            GLES20.glClearColor(100.0f / 255.0f, 149f / 255.0f, 237f / 255.0f, 255f / 255.0f);
            game.Init(R.drawable.main_bg);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    game.Reset(newBallLoad);
                    if (newBallLoad) {
                        game.newBallCreate(year,month,day);
                        newBallLoad = false;
                    }
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

        }
        finally
        {
            semaphore.release(1);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);

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

        return super.onTouchEvent(event);
    }

    @OnClick(R.id.img_gra)
    public void clickImgGra(View view) {
        Intent intent = new Intent(BallListActivity.this, FeelingChoiceActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.show_popup_inner)
    public void clickShowPopupInner(View view) {

    }

    @OnClick({R.id.btn_close_popup,R.id.show_popup})
    public void clickBtnCloswPopup(View view) {

        show_popup.setVisibility(View.GONE);
    }

    @OnClick(R.id.diary_image)
    public void clickDiaryImage(View view) {

        detail_date.setText(DiaryUtil.getMonthStr(curr.getDiary().getMonth())
                + "," + curr.getDiary().getDay() + "," + curr.getDiary().getYear());
        img_ball_color.setBackgroundResource(Ball.ballSmallResource[curr.getDiary().getBallColor()]);
        Glide.with(this)
                .load(getFilesDir() + "/" + curr.getDiary().getImg_src())
                .signature(new StringSignature(UUID.randomUUID().toString()))
                .into(diary_zoom_image);

        diary_detail_image.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.btn_close)
    public void clickBtnClose(View view) {
        diary_detail_image.setVisibility(View.GONE);
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

    @OnClick(R.id.btn_remove_diary)
    public void clickBtnRemoveDiary(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BallListActivity.this);
        alertDialog.setTitle(getString(R.string.delete));
        alertDialog.setMessage(getString(R.string.dialog_delete_diary));
        alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();

                RealmResults<DiaryRealm> diaries = realm.where(DiaryRealm.class).
                        equalTo("year",curr.getDiary().getYear()).equalTo("month",curr.getDiary().getMonth()).
                        equalTo("day",curr.getDiary().getDay()).findAll();
                Diary diary = Diary.copyFromRealmObj(diaries.first());

                diaries.deleteAllFromRealm();

                game.removeBall(diary);
                realm.commitTransaction();
                realm.close();
                clickBtnCloswPopup(null);
            }
        }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    @OnClick(R.id.btn_modify)
    public void clickBtnModify(View view) {
        ModifyTask modifyTask = new ModifyTask(curr.getDiary());
        modifyTask.execute();
    }

    @OnClick(R.id.btn_modify_diary)
    public void clickBtnModifyDiary(View view) {
        openModifyPopup();
    }

    @OnClick({R.id.btn_modify_close_popup,R.id.btn_modify_cancel})
    public void clickBtnClosePopup(View view) {
        modify_popup.setVisibility(View.GONE);
    }

    public void pickImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

    }

    @OnClick(R.id.btn_modify_add_pic)
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

    @OnClick(R.id.img_drawer_icon)
    public void clickImgDrawerIcon(View view) {
        openDrawer();

    }

    @OnClick(R.id.main_drawer_container)
    public void clickMainDrawerContainer(View view) {
        closeDrawer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                selectedImageUri = data.getData();
                imgPath = Util.getRealPath(BallListActivity.this,selectedImageUri);
                if (imgPath == null || imgPath.equals("")) {
                    selectedImageUri = null;
                    Toast.makeText(BallListActivity.this,getText(R.string.toast_image_load_fail),Toast.LENGTH_LONG).show();
                } else {
                    Glide.with(this)
                            .load(data.getData())
                            .into(btn_modify_add_pic);
                }
            }

        }

    }

    public void openDrawer() {
        main_drawer_container.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(BallListActivity.this,R.anim.drawer_left_to_right);
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
        Animation anim = AnimationUtils.loadAnimation(BallListActivity.this,R.anim.drawer_right_to_left);
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

    public void openModifyPopup() {
        modify_popup.setVisibility(View.VISIBLE);
        text_modify_popup_date.setText(DiaryUtil.getMonthStr(curr.getDiary().getMonth()) + ", " + curr.getDiary().getDay());
        modify_content.setText(curr.getDiary().getContent());
        modify_inner_popup.setBackgroundResource(PopupUtil.getPopupImageRes(curr.getDiary().getBallColor()));

        Glide.with(BallListActivity.this)
                .load(getFilesDir() + "/" + curr.getDiary().getImg_src())
                .signature(new StringSignature(UUID.randomUUID().toString()))
                .into(btn_modify_add_pic);
    }

    public boolean isFisrtDiaryDate(int year,int month, int day) {
        Realm realm = Realm.getDefaultInstance();


        RealmResults<FirstDiaryInfo> diaries = realm.where(FirstDiaryInfo.class).findAll();

        FirstDiaryInfo firstDiaryInfo = diaries.first();

        int fYear = firstDiaryInfo.getYear();
        int fMonth = firstDiaryInfo.getMonth();
        int fDay = firstDiaryInfo.getDay();

        realm.close();

        if (fYear == year && fMonth == month) {
            return true;
        }

        return false;
    }

    public boolean isNextMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH,1);
        int todayYear = c.get(Calendar.YEAR);
        int todayMonth = c.get(Calendar.MONTH);
        int todayDay = c.get(Calendar.DAY_OF_MONTH);

        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,day);

        c.add(Calendar.MONTH,1);

        int nYear = c.get(Calendar.YEAR);
        int nMonth = c.get(Calendar.MONTH);
        int nDay = c.get(Calendar.DAY_OF_MONTH);

        if (todayYear == nYear && todayMonth == nMonth) {
            return true;
        }

        return false;
    }

    public boolean isNextNextMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH,2);
        int todayYear = c.get(Calendar.YEAR);
        int todayMonth = c.get(Calendar.MONTH);
        int todayDay = c.get(Calendar.DAY_OF_MONTH);

        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,day);

        c.add(Calendar.MONTH,1);

        int nYear = c.get(Calendar.YEAR);
        int nMonth = c.get(Calendar.MONTH);
        int nDay = c.get(Calendar.DAY_OF_MONTH);

        if (todayYear == nYear && todayMonth == nMonth) {
            return true;
        }

        return false;
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        //handle 'swipe left' action only

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {

            if (show_popup.getVisibility() == View.VISIBLE) return false;

            if(event2.getX() < event1.getX()){

                if (isNextMonth()) {
                    Calendar c = Calendar.getInstance();

                    c.set(Calendar.YEAR,year);
                    c.set(Calendar.MONTH,month);
                    c.set(Calendar.DAY_OF_MONTH,day);

                    c.add(Calendar.MONTH,1);

                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);

                    setDate();
                    text_diary_msg.setText(getString(R.string.next_month_msg,year,(month+1)));
                    text_diary_msg.setVisibility(View.VISIBLE);

                    game.setDateVar(year,month,day);
                    game.destroyAllBall();

                } else if (isNextNextMonth()) {
                } else {
                    text_diary_msg.setVisibility(View.GONE);
                    Calendar c = Calendar.getInstance();

                    c.set(Calendar.YEAR,year);
                    c.set(Calendar.MONTH,month);
                    c.set(Calendar.DAY_OF_MONTH,day);

                    c.add(Calendar.MONTH,1);

                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);

                    setDate();

                    game.setDateVar(year,month,day);
                    game.moveNextDiary();
                }


            }

            if(event2.getX() > event1.getX()){
                if (isFisrtDiaryDate(year,month,day)) return false;
                text_diary_msg.setVisibility(View.GONE);

                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR,year);
                c.set(Calendar.MONTH,month);
                c.set(Calendar.DAY_OF_MONTH,day);

                c.add(Calendar.MONTH,-1);

                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                setDate();

                game.setDateVar(year,month,day);
                game.movePrevDiary();
            }

            return true;
        }
    }

    public Diary modifyDiary(Diary diary) {
        final String content = modify_content.getText().toString();

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
            fileName = DiaryUtil.getDiaryFileName(diary.getYear(),diary.getMonth(),diary.getDay());

            Util.saveImage(BallListActivity.this, bitmap, BallListActivity.this.getFilesDir().getPath(), fileName);
        }

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        RealmResults<DiaryRealm> diaries = realm.where(DiaryRealm.class).
                equalTo("year",diary.getYear()).equalTo("month",diary.getMonth()).equalTo("day",diary.getDay()).findAll();
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
        diaryRealm.setYear(diary.getYear());
        diaryRealm.setMonth(diary.getMonth());
        diaryRealm.setDay(diary.getDay());
        diaryRealm.setBallColor(diary.getBallColor());

        Diary diary2 = Diary.copyFromRealmObj(diaryRealm);
        realm.commitTransaction();
        realm.close();

        return diary2;
    }

    private class ModifyTask extends AsyncTask<Void, Void, Diary> {
        Diary diary;

        public ModifyTask(Diary diary){
            this.diary = diary;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(BallListActivity.this, "Title", "로딩중..", true);
            dialog.setCancelable(true);
        }

        @Override
        protected Diary doInBackground(Void... params) {
            Diary diary = modifyDiary(this.diary);
            return diary;
        }

        @Override
        protected void onPostExecute(Diary diary) {
            super.onPostExecute(diary);
            game.modifyBall(diary);
            clickBtnClosePopup(null);
            clickBtnCloswPopup(null);
            dialog.dismiss();
            Toast.makeText(BallListActivity.this,getString(R.string.toast_modify_diary),Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (game != null) {
            game.onDestroy();
        }
        if (fSounds != null) {
            fSounds.destroy();
        }
    }
}
