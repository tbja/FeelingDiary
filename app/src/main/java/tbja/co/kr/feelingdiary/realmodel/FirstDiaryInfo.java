package tbja.co.kr.feelingdiary.realmodel;

import io.realm.RealmObject;

/**
 * Created by apple on 2017. 12. 27..
 */

public class FirstDiaryInfo extends RealmObject {
    private Integer year;
    private Integer month;
    private Integer day;

    public FirstDiaryInfo() {

    }

    public FirstDiaryInfo(Integer year, Integer month, Integer day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }
}
