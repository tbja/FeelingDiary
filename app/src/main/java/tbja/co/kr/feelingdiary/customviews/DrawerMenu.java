package tbja.co.kr.feelingdiary.customviews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.otto.Bus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import tbja.co.kr.feelingdiary.R;
import tbja.co.kr.feelingdiary.event.BusProvider;
import tbja.co.kr.feelingdiary.event.NotifySoundOptChanged;
import tbja.co.kr.feelingdiary.sharedpre.SharedPreManager;
import tbja.co.kr.feelingdiary.view.feeling_choice.activity.FeelingChoiceActivity;

/**
 * Created by apple on 2017. 12. 20..
 */

public class DrawerMenu extends RelativeLayout {
    @BindView(R.id.sw_sound) Switch sw_sound;
    @BindView(R.id.text_drawer_title) TextView text_drawer_title;

    SharedPreManager sharedPreManager;
    Context context;

    public DrawerMenu(Context context) {
        super(context);
        this.context = context;

        init();
    }

    public DrawerMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        init();
    }

    public DrawerMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        init();
    }

    public void init() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.drawer_menu_layout, this, false);
        addView(v);
        ButterKnife.bind(this);

        sharedPreManager = new SharedPreManager(getContext());
        sw_sound.setChecked(sharedPreManager.loadSoundOption());

    }

    @OnClick(R.id.text_opensource)
    public void clickTextOpenSource(View veiw) {
        String url = "http://www.wobstudio.co.kr/opensource.html";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    @OnClick(R.id.text_drawer_title)
    public void clickTextdrawerTitle(View view) {
        Intent intent = new Intent(context, FeelingChoiceActivity.class);
        context.startActivity(intent);
    }

    @OnCheckedChanged(R.id.sw_sound)
    public void checkedChangeSwSound(CompoundButton buttonView, boolean isChecked) {
        sharedPreManager.saveSoundOption(isChecked);
        BusProvider.getInstance().post(new NotifySoundOptChanged(isChecked));
    }
}
