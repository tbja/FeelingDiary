package tbja.co.kr.feelingdiary.view.selectemotionball.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import tbja.co.kr.feelingdiary.R;
import tbja.co.kr.feelingdiary.box2d.model.Ball;
import tbja.co.kr.feelingdiary.box2d.model.FeelName;
import tbja.co.kr.feelingdiary.realmodel.SelectedBallRealm;
import tbja.co.kr.feelingdiary.sound.FSounds;
import tbja.co.kr.feelingdiary.utils.Util;
import tbja.co.kr.feelingdiary.view.FActivity;
import tbja.co.kr.feelingdiary.view.FNOBGMActivity;
import tbja.co.kr.feelingdiary.view.feeling_choice.activity.FeelingChoiceActivity;

public class SelectEmotionBallActivity extends FNOBGMActivity {
    @BindView(R.id.img_drawer_bottom) RelativeLayout img_drawer_bottom;
    @BindView(R.id.feeling_container_0) RelativeLayout feeling_container_0;
    @BindView(R.id.feeling_container_1) RelativeLayout feeling_container_1;
    @BindView(R.id.feeling_container_2) RelativeLayout feeling_container_2;
    @BindView(R.id.feeling_container_3) RelativeLayout feeling_container_3;
    @BindView(R.id.feeling_container_4) RelativeLayout feeling_container_4;
    //@BindView(R.id.feeling_container_5) RelativeLayout feeling_container_0;
    @BindView(R.id.feeling_container_6) RelativeLayout feeling_container_6;
    @BindView(R.id.feeling_container_7) RelativeLayout feeling_container_7;
    @BindView(R.id.feeling_container_8) RelativeLayout feeling_container_8;
    @BindView(R.id.feeling_container_9) RelativeLayout feeling_container_9;
    @BindView(R.id.feeling_container_10) RelativeLayout feeling_container_10;
    @BindView(R.id.iv_feeling_lock_0) ImageView iv_feeling_lock_0;
    @BindView(R.id.iv_feeling_lock_1) ImageView iv_feeling_lock_1;
    @BindView(R.id.iv_feeling_lock_2) ImageView iv_feeling_lock_2;
    @BindView(R.id.iv_feeling_lock_3) ImageView iv_feeling_lock_3;
    @BindView(R.id.iv_feeling_lock_4) ImageView iv_feeling_lock_4;
    //@BindView(R.id.iv_feeling_lock_5) ImageView iv_feeling_lock_5;
    @BindView(R.id.iv_feeling_lock_6) ImageView iv_feeling_lock_6;
    @BindView(R.id.iv_feeling_lock_7) ImageView iv_feeling_lock_7;
    @BindView(R.id.iv_feeling_lock_8) ImageView iv_feeling_lock_8;
    @BindView(R.id.iv_feeling_lock_9) ImageView iv_feeling_lock_9;
    @BindView(R.id.iv_feeling_lock_10) ImageView iv_feeling_lock_10;
    @BindView(R.id.iv_feeling_select_0) ImageView iv_feeling_select_0;
    @BindView(R.id.iv_feeling_select_1) ImageView iv_feeling_select_1;
    @BindView(R.id.iv_feeling_select_2) ImageView iv_feeling_select_2;
    @BindView(R.id.iv_feeling_select_3) ImageView iv_feeling_select_3;
    @BindView(R.id.iv_feeling_select_4) ImageView iv_feeling_select_4;
    //@BindView(R.id.iv_feeling_lock_5) ImageView iv_feeling_select_5;
    @BindView(R.id.iv_feeling_select_6) ImageView iv_feeling_select_6;
    @BindView(R.id.iv_feeling_select_7) ImageView iv_feeling_select_7;
    @BindView(R.id.iv_feeling_select_8) ImageView iv_feeling_select_8;
    @BindView(R.id.iv_feeling_select_9) ImageView iv_feeling_select_9;
    @BindView(R.id.iv_feeling_select_10) ImageView iv_feeling_select_10;
    @BindView(R.id.text_selected_count) TextView text_selected_count;

    @BindView(R.id.title_container) LinearLayout title_container;
    @BindView(R.id.feeling_all_container) LinearLayout feeling_all_container;
    @BindView(R.id.now_feeling) Button now_feeling;
    @BindView(R.id.now_feeling_desc) TextView now_feeling_desc;
    @BindView(R.id.btn_take_this) Button btn_take_this;
    @BindView(R.id.now_feeling_check) ImageView now_feeling_check;
    @BindView(R.id.img_ball_check) ImageView img_ball_check;
    @BindView(R.id.btn_deselect) Button btn_deselect;

    int selectedMaxCount = 5;
    RelativeLayout[] feelingBalls;
    ImageView[] locks;
    ImageView[] selected;
    boolean[] selectedFeelings;
    int nowSelectedFeelingIndex = -1;

    FSounds fSounds;
    FeelName feelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_emotion_ball);
        ButterKnife.bind(this);

        Realm.init(this);

        feelName = new FeelName(this);
        fSounds = new FSounds(this);

        feelingBalls = new RelativeLayout[FeelName.FEELINGCOUNT];
        feelingBalls[0] = feeling_container_0;
        feelingBalls[1] = feeling_container_1;
        feelingBalls[2] = feeling_container_2;
        feelingBalls[3] = feeling_container_3;
        feelingBalls[4] = feeling_container_4;
        feelingBalls[5] = new RelativeLayout(this);
        feelingBalls[6] = feeling_container_6;
        feelingBalls[7] = feeling_container_7;
        feelingBalls[8] = feeling_container_8;
        feelingBalls[9] = feeling_container_9;
        feelingBalls[10] = feeling_container_10;

        locks = new ImageView[FeelName.FEELINGCOUNT];
        locks[0] = iv_feeling_lock_0;
        locks[1] = iv_feeling_lock_1;
        locks[2] = iv_feeling_lock_2;
        locks[3] = iv_feeling_lock_3;
        locks[4] = iv_feeling_lock_4;
        locks[5] = new ImageView(this);
        locks[6] = iv_feeling_lock_6;
        locks[7] = iv_feeling_lock_7;
        locks[8] = iv_feeling_lock_8;
        locks[9] = iv_feeling_lock_9;
        locks[10] = iv_feeling_lock_10;

        selected = new ImageView[FeelName.FEELINGCOUNT];
        selected[0] = iv_feeling_select_0;
        selected[1] = iv_feeling_select_1;
        selected[2] = iv_feeling_select_2;
        selected[3] = iv_feeling_select_3;
        selected[4] = iv_feeling_select_4;
        selected[5] = new ImageView(this);;
        selected[6] = iv_feeling_select_6;
        selected[7] = iv_feeling_select_7;
        selected[8] = iv_feeling_select_8;
        selected[9] = iv_feeling_select_9;
        selected[10] = iv_feeling_select_10;

        selectedFeelings = new boolean[FeelName.FEELINGCOUNT];
        for (int i = 0 ; i < selectedFeelings.length ; i++) {
            selectedFeelings[i] = false;
        }

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        RealmResults<SelectedBallRealm> selectedBalls = realm.where(SelectedBallRealm.class).findAll();

        for (int i = 0 ; i < selectedBalls.size() ; i++) {
            selectedFeelings[selectedBalls.get(i).getId()] = true;
        }

        realm.commitTransaction();
        realm.close();

        img_drawer_bottom.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.drawer_bottom);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                fSounds.playDrawerSlide();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        img_drawer_bottom.startAnimation(anim);

        for (int i = 0 ; i < feelingBalls.length ; i++) {
            final int finalI = i;
            feelingBalls[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nowSelectedFeelingIndex = finalI;
                    changeSelectedFeeling();
                }
            });
        }

        showFeelingBall();
        setSelectedCount();
        //Toast.makeText(SelectEmotionBallActivity.this,getString(R.string.toast_drawer_desc),Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btn_back)
    public void clickBtnBack(View view) {
        onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        fSounds.destroy();
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult(RESULT_OK,intent);
        finish();
        //super.onBackPressed();
    }

    @OnClick({R.id.btn_take_this,R.id.btn_deselect,R.id.now_feeling})
    public void clickBtnTakeThis(View view) {


        if (selectedFeelings[nowSelectedFeelingIndex]) {
            if (1 == getSelectedCount()) {
                Toast.makeText(this,getText(R.string.min_select_ball_msg),Toast.LENGTH_SHORT).show();
                return;
            }
            selectedFeelings[nowSelectedFeelingIndex] = false;
        } else {
            if (selectedMaxCount == getSelectedCount()) {
                Toast.makeText(this,getString(R.string.max_select_ball_msg),Toast.LENGTH_SHORT).show();
                return;
            }
            selectedFeelings[nowSelectedFeelingIndex] = true;

        }
        saveFeelingToDB();
        showFeelingBall();
        setSelectedCount();
        chTakeBtn();
        //startCheckAni();
    }

    public void saveFeelingToDB() {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        RealmResults<SelectedBallRealm> selectedBalls = realm.where(SelectedBallRealm.class).findAll();
        selectedBalls.deleteAllFromRealm();

        for (int i = 0 ; i < selectedFeelings.length ; i++) {
            if (selectedFeelings[i]) {
                SelectedBallRealm selectedBallRealm = realm.createObject(SelectedBallRealm.class,i);
            }
        }

        realm.commitTransaction();
        realm.close();
    }

    public void changeSelectedFeeling() {
        title_container.setVisibility(View.GONE);
        feeling_all_container.setVisibility(View.VISIBLE);
        now_feeling.setBackgroundResource(Ball.ballResource[nowSelectedFeelingIndex]);
        now_feeling.setText(feelName.getFeelingOneName(nowSelectedFeelingIndex));
        now_feeling_desc.setText(feelName.getFeelingOneDesc(nowSelectedFeelingIndex));

        chTakeBtn();

        Animation anim = AnimationUtils.loadAnimation(SelectEmotionBallActivity.this, R.anim.fadein_feeling_ball);
        now_feeling.clearAnimation();
        now_feeling.startAnimation(anim);
    }

    public void startCheckAni() {
        if (selectedFeelings[nowSelectedFeelingIndex]) {
            Animation anim = AnimationUtils.loadAnimation(SelectEmotionBallActivity.this, R.anim.ball_select_check_fade_in);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    img_ball_check.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            img_ball_check.setVisibility(View.VISIBLE);
            img_ball_check.clearAnimation();
            img_ball_check.startAnimation(anim);
        }
    }

    public void chTakeBtn() {
        if (selectedFeelings[nowSelectedFeelingIndex]) {
            now_feeling_check.setVisibility(View.VISIBLE);
            btn_deselect.setVisibility(View.GONE);
            btn_take_this.setVisibility(View.GONE);

        } else {
            now_feeling_check.setVisibility(View.GONE);
            btn_deselect.setVisibility(View.GONE);
            btn_take_this.setVisibility(View.GONE);
        }
    }

    public void setSelectedCount() {
        text_selected_count.setText("(" + getSelectedCount() + "/" + selectedMaxCount + ")");
    }

    public int getSelectedCount() {
        int count = 0;
        for (int i = 0 ; i < selectedFeelings.length ; i++) {
            if (selectedFeelings[i]) {
                count++;
            }
        }

        return count;
    }

    public void showFeelingBall() {
        for (int i = 0 ; i < selected.length ; i++) {
            if (selectedFeelings[i]) {
                selected[i].setVisibility(View.VISIBLE);
            } else {
                selected[i].setVisibility(View.GONE);
            }
        }
    }

}
