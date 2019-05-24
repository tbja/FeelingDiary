package tbja.co.kr.feelingdiary.box2d.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tbja.co.kr.feelingdiary.realmodel.DiaryRealm;

/**
 * Created by apple on 2017. 11. 29..
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diary {
    private int id;
    private String content;
    private String img_src;
    private int year;
    private int month;
    private int day;
    private int ballColor;


    public static Diary copyFromRealmObj(DiaryRealm obj) {
        Diary item = new Diary(obj.getId(),obj.getContent(),obj.getImg_src(),obj.getYear(),obj.getMonth(),obj.getDay(),obj.getBallColor());

        return item;
    }
}
