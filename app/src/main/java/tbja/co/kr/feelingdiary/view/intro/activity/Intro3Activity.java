package tbja.co.kr.feelingdiary.view.intro.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import tbja.co.kr.feelingdiary.R;
import tbja.co.kr.feelingdiary.box2d.model.FeelName;
import tbja.co.kr.feelingdiary.realmodel.SelectedBallRealm;
import tbja.co.kr.feelingdiary.view.FActivity;

public class Intro3Activity extends FActivity {
    @BindView(R.id.btn_feeling_0) Button btn_feeling_0;
    @BindView(R.id.btn_feeling_1) Button btn_feeling_1;
    @BindView(R.id.btn_feeling_2) Button btn_feeling_2;
    @BindView(R.id.btn_feeling_3) Button btn_feeling_3;
    @BindView(R.id.btn_feeling_4) Button btn_feeling_4;
    //@BindView(R.id.btn_feeling_5) Button btn_feeling_5;
    @BindView(R.id.btn_feeling_6) Button btn_feeling_6;
    @BindView(R.id.btn_feeling_7) Button btn_feeling_7;
    @BindView(R.id.btn_feeling_8) Button btn_feeling_8;
    @BindView(R.id.btn_feeling_9) Button btn_feeling_9;
    @BindView(R.id.btn_feeling_10) Button btn_feeling_10;

    Button[] btn_feelings = new Button[FeelName.FEELINGCOUNT];
    boolean[] selectedFeelings = new boolean[FeelName.FEELINGCOUNT];
    int selectedMaxCount = 5;
    FeelName feelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro3);
        ButterKnife.bind(this);

        Realm.init(this);

        feelName = new FeelName(this);

        btn_feelings[0] = btn_feeling_0;
        btn_feelings[1] = btn_feeling_1;
        btn_feelings[2] = btn_feeling_2;
        btn_feelings[3] = btn_feeling_3;
        btn_feelings[4] = btn_feeling_4;
        btn_feelings[5] = new Button(this);
        btn_feelings[6] = btn_feeling_6;
        btn_feelings[7] = btn_feeling_7;
        btn_feelings[8] = btn_feeling_8;
        btn_feelings[9] = btn_feeling_9;
        btn_feelings[10] = btn_feeling_10;

        for (int i = 0 ; i < selectedFeelings.length ; i++) {
            selectedFeelings[i] = false;
        }

        for (int i = 0 ; i < btn_feelings.length ; i++) {
            btn_feelings[i].setText(feelName.getFeelingOneName(i));
            final int finalI = i;
            btn_feelings[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button v = (Button)view;
                    if (selectedFeelings[finalI]) {
                        selectedFeelings[finalI] = false;
                        v.setBackgroundResource(R.drawable.shape_round_feeling);
                    } else {
                        selectedFeelings[finalI] = true;
                        v.setBackgroundResource(R.drawable.shape_round_feeling_selected);
                    }

                    if (getSelectedCount() == selectedMaxCount) {
                        saveFeelingToDB();
                        Intent intent = new Intent(Intro3Activity.this,Intro4Activity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    public int getSelectedCount() {
        int count = 0;
        for (int i = 0 ; i < selectedFeelings.length ; i++) {
            if (selectedFeelings[i]) {
                count++;
            }
        }

        return count;
    }

    public void saveFeelingToDB() {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        RealmResults<SelectedBallRealm> selectedBalls = realm.where(SelectedBallRealm.class).findAll();
        selectedBalls.deleteAllFromRealm();

        for (int i = 0 ; i < selectedFeelings.length ; i++) {
            if (selectedFeelings[i]) {
                SelectedBallRealm selectedBallRealm = realm.createObject(SelectedBallRealm.class,i);
            }
        }

        realm.commitTransaction();
        realm.close();
    }
}
