package tbja.co.kr.feelingdiary.box2d.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tbja.co.kr.feelingdiary.realmodel.SelectedBallRealm;

/**
 * Created by apple on 2017. 12. 19..
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectedBall {
    private Integer id;

    public static SelectedBall copyFromRealmObj(SelectedBallRealm obj) {
        SelectedBall item = new SelectedBall(obj.getId());

        return item;
    }
}
