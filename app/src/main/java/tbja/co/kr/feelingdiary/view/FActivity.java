package tbja.co.kr.feelingdiary.view;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import tbja.co.kr.feelingdiary.service.BGMService;
import tbja.co.kr.feelingdiary.utils.Util;
import tbja.co.kr.feelingdiary.view.feeling_choice.activity.FeelingChoiceActivity;

/**
 * Created by apple on 2017. 12. 8..
 */

public class FActivity extends AppCompatActivity {
    public BGMService bgmService;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BGMService.LocalBinder binder = (BGMService.LocalBinder) iBinder;
            bgmService = binder.getService();

            bgmService.startBGM();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bgmService = null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        if (!isServiceRunning()) {
            Intent intent = new Intent(FActivity.this,BGMService.class);
            startService(intent);
            bindService(intent,connection,BIND_AUTO_CREATE);
        } else {
            Intent intent = new Intent(FActivity.this,BGMService.class);
            bindService(intent,connection,BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (bgmService != null) {
            bgmService.startBGM();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (Util.applicazioneInBackground(FActivity.this)) {
            if (bgmService != null) {
                bgmService.pauseBGM();
            }
        } else {
        }
    }

    @Override
    protected void onDestroy() {
        if (bgmService != null ) {
            unbindService(connection);
        }
        super.onDestroy();
    }

    public boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (BGMService.class.getName().equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }

        return false;
    }
}
