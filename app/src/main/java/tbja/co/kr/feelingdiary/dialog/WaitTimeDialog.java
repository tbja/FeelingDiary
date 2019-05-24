package tbja.co.kr.feelingdiary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import tbja.co.kr.feelingdiary.R;

/**
 * Created by apple on 2017. 12. 22..
 */

public class WaitTimeDialog extends Dialog {
    @BindView(R.id.text_today_feeling) TextView text_today_feeling;
    @BindView(R.id.text_wait_time) TextView text_wait_time;

    Context context;
    public WaitTimeDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public WaitTimeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        init();
    }

    protected WaitTimeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
        init();
    }

    public void init() {
        setContentView(R.layout.dialog_waittime);
        ButterKnife.bind(this);
    }

    public void setTodayFeeling(String feeling) {
        text_today_feeling.setText(context.getString(R.string.dialog_today,feeling));
    }

    public void setRemainingTime(int rHour,int rMin) {
        text_wait_time.setText(context.getString(R.string.dialog_time_format,rHour,rMin));
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }
}
