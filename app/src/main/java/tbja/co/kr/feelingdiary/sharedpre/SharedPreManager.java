package tbja.co.kr.feelingdiary.sharedpre;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by apple on 2017. 12. 13..
 */

public class SharedPreManager {
    Context context;

    public SharedPreManager(Context context) {
        this.context = context;
    }
    public void saveTitle(String title) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("diary_title", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("diary_title_value", title);
        editor.commit();
    }

    public String loadTitle() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("diary_title", 0);
        String title = sharedPreferences.getString("diary_title_value","");

        return title;
    }

    public Boolean loadCheckIntro() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("diary_intro", 0);
        Boolean res = sharedPreferences.getBoolean("diary_intro_value",false);

        return res;
    }

    public void checkIntro() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("diary_intro", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("diary_intro_value", true);
        editor.commit();
    }

    public void saveSoundOption(boolean sound) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("option_sound", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("option_sound_value", sound);
        editor.commit();
    }

    public boolean loadSoundOption() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("option_sound", 0);
        boolean sound = sharedPreferences.getBoolean("option_sound_value",true);

        return sound;
    }

}
