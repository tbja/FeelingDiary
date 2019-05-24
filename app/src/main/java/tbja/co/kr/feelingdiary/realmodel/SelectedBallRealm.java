package tbja.co.kr.feelingdiary.realmodel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by apple on 2017. 12. 19..
 */

public class SelectedBallRealm extends RealmObject {
    @PrimaryKey
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
