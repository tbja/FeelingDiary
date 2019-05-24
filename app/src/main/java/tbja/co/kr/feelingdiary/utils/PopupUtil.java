package tbja.co.kr.feelingdiary.utils;

import tbja.co.kr.feelingdiary.R;

/**
 * Created by apple on 2017. 11. 17..
 */

public class PopupUtil {
    public static int getPopupImageRes(int ballColor) {
        switch (ballColor) {
            case 0:
                return R.drawable.ball_yellow_bg;
            case 1:
                return R.drawable.ball_blue_bg;
            case 2:
                return R.drawable.ball_green_bg;
            case 3:
                return R.drawable.ball_pupple_bg;
            case 4:
                return R.drawable.ball_red_bg;
            case 5:
                return 0;
            case 6:
                return R.drawable.ball_orange_bg;
            case 7:
                return R.drawable.ball_pastel_bg;
            case 8:
                return R.drawable.ball_pink_bg;
            case 9:
                return R.drawable.ball_sky_bg;
            case 10:
                return R.drawable.ball_brown_bg;
        }

        return -1;
    }
}
