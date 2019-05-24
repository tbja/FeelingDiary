package tbja.co.kr.feelingdiary.box2d.model;

import android.content.Context;

import tbja.co.kr.feelingdiary.R;

/**
 * Created by apple on 2017. 12. 3..
 */

public class FeelName {
    public static int FEELINGCOUNT = 11;
    public static int HAPPY = 0;
    public static int SAD = 1;
    public static int SOSO = 2;
    public static int GLOOMY = 3;
    public static int ANGRY = 4;
    public static int NOFEELING = 5;
    public static int TOUCHED = 6;
    public static int BORED = 7;
    public static int ROMANCE = 8;
    public static int WISH = 9;
    public static int ACHEIVEMENT = 10;

    private String[] feelingName;
    private String[] feelingDesc;
    private Context context;

    public FeelName(Context context) {
        feelingName = new String[FEELINGCOUNT];
        feelingName[0] = context.getString(R.string.happy);
        feelingName[1] = context.getString(R.string.sad);
        feelingName[2] = context.getString(R.string.soso);
        feelingName[3] = context.getString(R.string.gloomy);
        feelingName[4] = context.getString(R.string.angry);
        feelingName[5] = context.getString(R.string.nofeel);
        feelingName[6] = context.getString(R.string.touched);
        feelingName[7] = context.getString(R.string.bored);
        feelingName[8] = context.getString(R.string.romance);
        feelingName[9] = context.getString(R.string.wish);
        feelingName[10] = context.getString(R.string.acheivement);

        feelingDesc = new String[FEELINGCOUNT];
        feelingDesc[0] = context.getString(R.string.happy_desc);
        feelingDesc[1] = context.getString(R.string.sad_desc);
        feelingDesc[2] = context.getString(R.string.soso_desc);
        feelingDesc[3] = context.getString(R.string.gloomy_desc);
        feelingDesc[4] = context.getString(R.string.angry_desc);
        feelingDesc[5] = context.getString(R.string.nofeel_desc);
        feelingDesc[6] = context.getString(R.string.touched_desc);
        feelingDesc[7] = context.getString(R.string.bored_desc);
        feelingDesc[8] = context.getString(R.string.romance_desc);
        feelingDesc[9] = context.getString(R.string.wish_desc);
        feelingDesc[10] = context.getString(R.string.acheivement_desc);
    }

    public String getFeelingOneName(int index) {
        return feelingName[index];
    }

    public String[] getFeelingName() {
        return feelingName;
    }

    public void setFeelingName(String[] feelingName) {
        this.feelingName = feelingName;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String[] getFeelingDesc() {
        return feelingDesc;
    }

    public void setFeelingDesc(String[] feelingDesc) {
        this.feelingDesc = feelingDesc;
    }

    public String getFeelingOneDesc(int index) {
        return feelingDesc[index];
    }
}
