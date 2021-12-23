package ddwucom.mobile.finalproject.ma02_20190980;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class ToiletDBHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "toiletBookmark_db";
    public final static String TABLE_NAME = "toiletBookmark_table";
    public final static String COL_ID = "_id";
    public final static String COL_NAME = "name"; // 화장실 명
    public final static String COL_ADDRESS = "address"; //주소
    public final static String COL_DATE = "date"; // 추가 날짜
    public final static String COL_IMAGE = "image"; // 사진
    public final static String COL_MEMO = "memo"; // 메모

    public ToiletDBHelper(@Nullable  Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + " ( " + COL_ID + " integer primary key autoincrement,"
                + COL_NAME + " TEXT, " + COL_ADDRESS + " TEXT, " + COL_DATE + " TEXT, " + COL_IMAGE + " TEXT, " + COL_MEMO + " TEXT);");

        // 샘플 데이터 삽입
        /*sqLiteDatabase.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '경기도 여주시 북내면 신남1길 35-8','2021-12-24', 'null', '저번에 월곡 놀러왔다가 들렀음. 되게 깨끗함!');");*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
