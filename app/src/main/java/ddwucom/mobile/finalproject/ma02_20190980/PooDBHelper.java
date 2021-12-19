package ddwucom.mobile.finalproject.ma02_20190980;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class PooDBHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "pooDays_db";
    public final static String TABLE_NAME = "pooRecord_table";
    public final static String COL_ID = "_id";
    public final static String COL_DATE = "date"; // 기록 날짜
    public final static String COL_CONDITION = "condition"; // 기분
    public final static String COL_HEALTH = "health"; //운동 여부
    public final static String COL_ISPOO = "isPoo"; // 배변 여부
    public final static String COL_BM = "bowelMovement"; // 배변 상태
    public final static String COL_TIME = "time"; // 배변 시간
    public final static String COL_SmallBM = "smallBM"; //잔변감

    public PooDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " + COL_ID + " integer primary key autoincrement,"
                + COL_DATE + " TEXT, " + COL_CONDITION + " TEXT, " + COL_HEALTH + " integer, " +
                COL_ISPOO + " integer, " + COL_BM + " TEXT, " + COL_TIME + " TEXT, " +
                COL_SmallBM + " integer);");

        //		샘플 데이터 삽입 (id, 날짜, 기분, 운동여부, 배변여부, 배변상태, 배변시간, 잔변감)
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '2021-12-06', '나빠요', 1, 1, '딱딱한 변', '15', 1);");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '2021-12-07', '좋아요', 0, 1, '촉촉한 변', '21', 0);");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '2021-12-08', '슬퍼요', 1, 0, null, null, null);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + TABLE_NAME);
        onCreate(db);
    }
}