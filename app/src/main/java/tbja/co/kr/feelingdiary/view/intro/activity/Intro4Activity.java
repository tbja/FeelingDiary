package tbja.co.kr.feelingdiary.view.intro.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tbja.co.kr.feelingdiary.R;
import tbja.co.kr.feelingdiary.event.BusProvider;
import tbja.co.kr.feelingdiary.event.NotifyBGMChanged;
import tbja.co.kr.feelingdiary.sharedpre.SharedPreManager;
import tbja.co.kr.feelingdiary.sound.FSounds;
import tbja.co.kr.feelingdiary.view.FActivity;
import tbja.co.kr.feelingdiary.view.feeling_choice.activity.FeelingChoiceActivity;

public class Intro4Activity extends FActivity {
    @BindView(R.id.btn_move_page) Button btn_move_page;
    @BindView(R.id.bg_light) ImageView bg_light;
    SharedPreManager sharedPreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro4);
        ButterKnife.bind(this);

        sharedPreManager = new SharedPreManager(Intro4Activity.this);

        Animation anim = AnimationUtils.loadAnimation(Intro4Activity.this, R.anim.bg_light);
        bg_light.startAnimation(anim);
    }

    @OnClick(R.id.btn_move_page)
    public void clickBtnMovePage(View view) {
        sharedPreManager.checkIntro();
        BusProvider.getInstance().post(new NotifyBGMChanged(FSounds.BGM));
        Intent intent = new Intent(Intro4Activity.this,FeelingChoiceActivity.class);
        startActivity(intent);
        finish();
    }
}
