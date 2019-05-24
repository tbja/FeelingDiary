package tbja.co.kr.feelingdiary.event;

import com.squareup.otto.Bus;

/**
 * Created by apple on 2017. 12. 20..
 */

public final class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}