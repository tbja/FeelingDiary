package tbja.co.kr.feelingdiary.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import tbja.co.kr.feelingdiary.R;
import tbja.co.kr.feelingdiary.event.BusProvider;
import tbja.co.kr.feelingdiary.event.NotifyBGMChanged;
import tbja.co.kr.feelingdiary.event.NotifySoundOptChanged;
import tbja.co.kr.feelingdiary.sharedpre.SharedPreManager;
import tbja.co.kr.feelingdiary.view.feeling_choice.activity.FeelingChoiceActivity;

/**
 * Created by apple on 2017. 12. 5..
 */

public class FSounds {
    public static int MAXSOUNDPOOLCOUNT = 4;
    public static int BALLCLICK = R.raw.click_ball;
    public static int BALLCOLLISION = R.raw.moodball;
    public static int BGM = R.raw.bg;
    public static int INTROBGM = R.raw.intro_bgm;
    public static int DRAWER = R.raw.drawer_slide;
    public static int BALLDOWN = R.raw.ball_down;
    SoundPool soundPool;
    MediaPlayer mediaPlayer;
    int[] soundsIds = new int[MAXSOUNDPOOLCOUNT];
    Context context;
    SharedPreManager sharedPreManager;
    boolean sound;

    public FSounds() {

    }

    int count = 0;

    public FSounds(Context context) {
        this.context = context;
        sharedPreManager = new SharedPreManager(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(MAXSOUNDPOOLCOUNT).build();
        }
        else {
            soundPool = new SoundPool(MAXSOUNDPOOLCOUNT, AudioManager.STREAM_MUSIC, 0);
        }

        if (!sharedPreManager.loadCheckIntro()) {
            mediaPlayer = MediaPlayer.create(context, INTROBGM);
        } else {
            mediaPlayer = MediaPlayer.create(context, BGM);
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (count < 3) {
                    playBGM();
                    count++;
                }

            }
        });

        soundsIds[0] = soundPool.load(context, FSounds.BALLCLICK,1);
        soundsIds[1] = soundPool.load(context, FSounds.BALLCOLLISION,1);
        soundsIds[2] = soundPool.load(context, FSounds.DRAWER,1);
        soundsIds[3] = soundPool.load(context, FSounds.BALLDOWN,1);

        loadSoundOpt();
        BusProvider.getInstance().register(this);
    }

    public void loadSoundOpt() {
        sound = sharedPreManager.loadSoundOption();
    }

    public void  playBallClick() {
        if (!sound) return;
        if (soundPool == null) return;
        soundPool.play(soundsIds[0], 1, 1, 1, 0, 1);
    }

    public void playBallCollisoin() {
        if (!sound) return;
        if (soundPool == null) return;
        soundPool.play(soundsIds[1], 1, 1, 1, 0, 1);
    }

    public void playDrawerSlide() {
        if (!sound) return;
        if (soundPool == null) return;
        soundPool.play(soundsIds[2], 1, 1, 1, 0, 1);
    }

    public void playBallDown() {
        if (!sound) return;
        if (soundPool == null) return;
        soundPool.play(soundsIds[3], 1, 1, 1, 0, 1);
    }

    public void playBGM() {
        if (!sound) return;
        if (mediaPlayer == null ) return;

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void pauseBGM() {
        if (mediaPlayer == null ) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void destroy() {
        for (int i = 0 ; i < soundsIds.length ; i++) {
            soundsIds[i] = 0;
        }

        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    @Subscribe
    public void soundOptionChange(NotifySoundOptChanged event) {
        setSound(event.isSoundOpt());
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
