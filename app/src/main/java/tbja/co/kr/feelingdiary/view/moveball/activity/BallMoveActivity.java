package tbja.co.kr.feelingdiary.view.moveball.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import tbja.co.kr.feelingdiary.R;
import tbja.co.kr.feelingdiary.box2d.model.Ball;
import tbja.co.kr.feelingdiary.sound.FSounds;
import tbja.co.kr.feelingdiary.view.FActivity;
import tbja.co.kr.feelingdiary.view.FNOBGMActivity;
import tbja.co.kr.feelingdiary.view.balllist.activity.BallListActivity;

public class BallMoveActivity extends FNOBGMActivity {
    @BindView(R.id.beed_move)
    ImageView beed_move;
    Integer ballColor;

    FSounds fSounds;

    int year;
    int month;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beed_move);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        ballColor = intent.getIntExtra("ballColor",0);
        year = intent.getIntExtra("year",0);
        month = intent.getIntExtra("month",0);
        day = intent.getIntExtra("day",0);

        fSounds = new FSounds(this);

        beed_move.setBackgroundResource(Ball.ballMiddleResource[ballColor]);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.move_down);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                fSounds.playBallDown();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                beed_move.setVisibility(View.GONE);
                Intent intent = new Intent(BallMoveActivity.this,BallListActivity.class);
                intent.putExtra("ballColor",ballColor);
                intent.putExtra("year",year);
                intent.putExtra("month",month);
                intent.putExtra("day",day);
                intent.putExtra("newBallLoad",true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION|Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        beed_move.startAnimation(anim);
    }
}
