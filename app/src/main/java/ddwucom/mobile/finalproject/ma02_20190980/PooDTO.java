package ddwucom.mobile.finalproject.ma02_20190980;

import java.io.Serializable;

public class PooDTO implements Serializable {

    private int _id;
    private String date; // 기록 날짜
    private String condition; //기분
    private int health; // 운동 여부
    private int isPoo; // 배변 여부
    private String BM; // 배변 상태
    private String time; // 배변 시간
    private int smallBM; // 잔여감

    public PooDTO() {}
    // 배변 O
    public PooDTO(String date, String condition, int health, int isPoo) {
        this.date = date;
        this.condition = condition;
        this.health = health;
        this.isPoo = isPoo;
    }

    // 배변 X
    public PooDTO(String date, String condition, int health, int isPoo, String BM, String time, int smallBM) {
        this.date = date;
        this.condition = condition;
        this.health = health;
        this.isPoo = isPoo;
        this.BM = BM;
        this.time = time;
        this.smallBM = smallBM;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getIsPoo() {
        return isPoo;
    }

    public void setIsPoo(int isPoo) {
        this.isPoo = isPoo;
    }

    public String getBM() {
        return BM;
    }

    public void setBM(String BM) {
        this.BM = BM;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSmallBM() {
        return smallBM;
    }

    public void setSmallBM(int smallBM) {
        this.smallBM = smallBM;
    }
}
