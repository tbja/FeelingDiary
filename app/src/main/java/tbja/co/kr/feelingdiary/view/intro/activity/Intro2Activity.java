package tbja.co.kr.feelingdiary.view.intro.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tbja.co.kr.feelingdiary.R;
import tbja.co.kr.feelingdiary.sharedpre.SharedPreManager;
import tbja.co.kr.feelingdiary.view.FActivity;
import tbja.co.kr.feelingdiary.view.feeling_choice.activity.FeelingChoiceActivity;

public class Intro2Activity extends FActivity {
    @BindView(R.id.btn_skip) LinearLayout btn_skip;
    @BindView(R.id.et_diary_title_intro) EditText et_diary_title_intro;
    SharedPreManager sharedPreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro2);
        ButterKnife.bind(this);

        sharedPreManager = new SharedPreManager(Intro2Activity.this);
    }

    @OnClick(R.id.btn_skip)
    public void clickBtnSkip(View view) {
        moveNextAct();
    }

    @OnClick(R.id.btn_move_act)
    public void clickBtnMoveAct(View view) {

        if (!et_diary_title_intro.getText().toString().trim().equals("")) {
            sharedPreManager.saveTitle(et_diary_title_intro.getText().toString().trim());
        }
        moveNextAct();
    }

    public void moveNextAct() {


        Intent intent = new Intent(Intro2Activity.this, Intro3Activity.class);
        startActivity(intent);
        finish();
    }

}
