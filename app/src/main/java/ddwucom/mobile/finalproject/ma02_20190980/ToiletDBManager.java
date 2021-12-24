package ddwucom.mobile.finalproject.ma02_20190980;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ToiletDBManager {
    ToiletDBHelper toiletDBHelper;
    static final String TAG = "ToiletDBManger";

    public ToiletDBManager(Context context) {toiletDBHelper = new ToiletDBHelper(context);}

    // 화장실 즐겨찾기 정보 모두 가져오기
    public void getAllToilet(Cursor cursor, ToiletAdapter adapter) {
        SQLiteDatabase db = toiletDBHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + ToiletDBHelper.TABLE_NAME, null);

        adapter.changeCursor(cursor);
        toiletDBHelper.close();
    }

    // 즐겨찾기 추가
    public boolean insertBookMark(BookMarkDTO dto) {
        SQLiteDatabase db = toiletDBHelper.getWritableDatabase();
        ContentValues row = new ContentValues();

        row.put(ToiletDBHelper.COL_NAME, dto.getName());
        row.put(ToiletDBHelper.COL_ADDRESS, dto.getAddress());
        row.put(ToiletDBHelper.COL_DATE, dto.getDate());
        row.put(ToiletDBHelper.COL_IMAGE, dto.getImage());
        row.put(ToiletDBHelper.COL_MEMO, dto.getMemo());

        long result = db.insert(ToiletDBHelper.TABLE_NAME, null, row);
        toiletDBHelper.close();

        if(result > 0) return true;
        else return false;
    }

    // id로 북마크 정보 가져오기
    public BookMarkDTO getBookMarkById(long _id) {
        SQLiteDatabase db = toiletDBHelper.getReadableDatabase();

        String selection = ToiletDBHelper.COL_ID +"=?";
        String[] selectAtgs = new String[] { String.valueOf(_id) };

        Cursor cursor = db.query(ToiletDBHelper.TABLE_NAME, null, selection, selectAtgs, null, null, null, null);

        BookMarkDTO dto = new BookMarkDTO();
        if(cursor.moveToNext()) {
            dto.set_id(cursor.getInt(cursor.getColumnIndexOrThrow(ToiletDBHelper.COL_ID)));
            dto.setName(cursor.getString(cursor.getColumnIndexOrThrow(ToiletDBHelper.COL_NAME)));
            dto.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(ToiletDBHelper.COL_ADDRESS)));
            dto.setImage(cursor.getString(cursor.getColumnIndexOrThrow(ToiletDBHelper.COL_IMAGE)));
            dto.setMemo(cursor.getString(cursor.getColumnIndexOrThrow(ToiletDBHelper.COL_MEMO)));
        }

        toiletDBHelper.close();
        return dto;
    }

}
