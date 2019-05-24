package tbja.co.kr.feelingdiary.utils;

/**
 * Created by apple on 2017. 11. 29..
 */

public class DiaryUtil {
    public static String getDiaryFileName(int year,int month,int day) {
        return "diary_" + year + "_" + month + "_" + day + ".jpg";
    }

    public static String getMonthStr(int month) {
        switch (month) {
            case 0 :
                return "Jan";
            case 1 :
                return "Feb";
            case 2 :
                return "Mar";
            case 3 :
                return "Apr";
            case 4 :
                return "May";
            case 5 :
                return "Jun";
            case 6 :
                return "July";
            case 7 :
                return "Aug";
            case 8 :
                return "Sep";
            case 9 :
                return "Oct";
            case 10 :
                return "Nov";
            case 11 :
                return "Dec";
        }

        return "";
    }
}
