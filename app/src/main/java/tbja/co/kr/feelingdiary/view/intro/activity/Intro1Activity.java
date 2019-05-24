package tbja.co.kr.feelingdiary.view.intro.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class Intro1Activity extends FActivity {
    @BindView(R.id.btn_move_intro2)
    Button btn_move_intro2;
    SharedPreManager sharedPreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro1);

        ButterKnife.bind(this);

        sharedPreManager = new SharedPreManager(Intro1Activity.this);

        if (sharedPreManager.loadCheckIntro()) {
            Intent intent = new Intent(Intro1Activity.this,FeelingChoiceActivity.class);
            startActivity(intent);
            finish();
            return;
        }
    }

    @OnClick(R.id.btn_move_intro2)
    public void clickBtnMoveIntro2(View view) {
        Intent intent = new Intent(Intro1Activity.this,Intro2Activity.class);
        startActivity(intent);
        finish();
    }


}
