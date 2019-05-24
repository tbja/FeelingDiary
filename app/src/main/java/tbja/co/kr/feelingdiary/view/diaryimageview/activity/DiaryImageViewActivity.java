package tbja.co.kr.feelingdiary.view.diaryimageview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import tbja.co.kr.feelingdiary.R;
import tbja.co.kr.feelingdiary.box2d.model.Ball;
import tbja.co.kr.feelingdiary.utils.DiaryUtil;
import tbja.co.kr.feelingdiary.view.FActivity;
import tbja.co.kr.feelingdiary.view.FNOBGMActivity;

public class DiaryImageViewActivity extends FNOBGMActivity {
    @BindView(R.id.diary_zoom_image)
    ImageViewTouch diary_zoom_image;
    @BindView(R.id.img_ball_color)
    ImageView img_ball_color;
    @BindView(R.id.detail_date)
    TextView detail_date;

    int year;
    int month;
    int day;
    int ballColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_image_view);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String filePath = intent.getStringExtra("imgPath");
        year = intent.getIntExtra("year",0);
        month = intent.getIntExtra("month",0);
        day = intent.getIntExtra("day",0);
        ballColor = intent.getIntExtra("ballColor",ballColor);

        detail_date.setText(DiaryUtil.getMonthStr(month) + "," + day + "," + year);

        img_ball_color.setBackgroundResource(Ball.ballSmallResource[ballColor]);


        Glide.with(this)
                .load(filePath)
                .into(diary_zoom_image);
    }

    @OnClick(R.id.btn_close)
    public void clickBtnClose(View view) {
        finish();
    }
}
