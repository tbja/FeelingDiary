package tbja.co.kr.feelingdiary.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.squareup.otto.Subscribe;

import tbja.co.kr.feelingdiary.event.BusProvider;
import tbja.co.kr.feelingdiary.event.NotifyBGMChanged;
import tbja.co.kr.feelingdiary.event.NotifySoundOptChanged;
import tbja.co.kr.feelingdiary.sound.FSounds;

/**
 * Created by apple on 2017. 12. 8..
 */

public class BGMService extends Service {
    private IBinder binder = new LocalBinder();
    FSounds fSounds;

    public class LocalBinder extends Binder {
        public BGMService getService() {
            return BGMService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        fSounds = new FSounds(this);
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (fSounds != null) {
            fSounds.destroy();
        }
    }

    public void startBGM() {
        fSounds.playBGM();
    }

    public void pauseBGM() {
        fSounds.pauseBGM();
    }

    @Subscribe
    public void notifyBGMChanged(NotifyBGMChanged event) {
        fSounds.destroy();
        fSounds = new FSounds(getApplicationContext());
    }

}
