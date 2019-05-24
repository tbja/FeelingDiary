package tbja.co.kr.feelingdiary.box2d.model;

import android.content.Context;

import tbja.co.kr.feelingdiary.R;
import tbja.co.kr.feelingdiary.realmodel.DiaryRealm;

/**
 * Created by apple on 2017. 11. 7..
 */

public class Ball {
    public enum ObjectType
    {
        Box,
        Floor,
        Ball,
        Ball2
    }
    private int ballColor;
    private ObjectType type;
    public static int[] ballResource = {R.drawable.ball_yellow,R.drawable.ball_blue,R.drawable.ball_green,
            R.drawable.ball_puple,R.drawable.ball_red,R.drawable.ball_b_250,R.drawable.ball_orange,R.drawable.ball_pastel,
    R.drawable.ball_pink,R.drawable.ball_sky,R.drawable.ball_brown};
    public static int[] ballMiddleResource = {R.drawable.ball_yellow_250,R.drawable.ball_blue_250,R.drawable.ball_green_250,
            R.drawable.ball_pupple_250,R.drawable.ball_red_250,R.drawable.ball_b_250,R.drawable.ball_orange_250,R.drawable.ball_pastel_250,
            R.drawable.ball_pink_250,R.drawable.ball_sky_250,R.drawable.ball_brown_250};
    public static int[] ballSmallResource = {R.drawable.ball_yellow_103,R.drawable.ball_blue_103,R.drawable.ball_green_103,
            R.drawable.ball_pupple_103,R.drawable.ball_red_103,R.drawable.ball_b_103,R.drawable.ball_orange_103,R.drawable.ball_pastel_103,
            R.drawable.ball_pink_103,R.drawable.ball_sky_103,R.drawable.ball_brown_103};

    Diary diary;

    public Ball(int ballColor, ObjectType type) {
        this.ballColor = ballColor;
        this.type = type;
    }

    public int getBallColor() {
        return ballColor;
    }

    public void setBallColor(int ballColor) {
        this.ballColor = ballColor;
    }

    public ObjectType getType() {
        return type;
    }

    public void setType(ObjectType type) {
        this.type = type;
    }

    public Diary getDiary() {
        return diary;
    }

    public void setDiary(Diary diaryRealm) {
        this.diary = diaryRealm;
    }
}
