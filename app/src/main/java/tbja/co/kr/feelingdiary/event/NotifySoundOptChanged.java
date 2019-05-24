package tbja.co.kr.feelingdiary.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by apple on 2017. 12. 20..
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotifySoundOptChanged {
    private boolean soundOpt;
}
