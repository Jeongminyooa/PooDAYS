package ddwucom.mobile.finalproject.ma02_20190980;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class PooDBManager {
    PooDBHelper pooDBHelper;
    static final String TAG = "pooDAYS";

    public PooDBManager(Context context) {
        pooDBHelper = new PooDBHelper(context);
    }

    // 배변 기록 모두 가져오기
    public void getAllPoo(Cursor cursor, PooAdapter adapter) {
        SQLiteDatabase db = pooDBHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + PooDBHelper.TABLE_NAME, null);

        adapter.changeCursor(cursor);
        pooDBHelper.close();
    }
    // id 에 해당하는 기록 가져오기
    public PooDTO getPooById(long _id) {
        SQLiteDatabase db = pooDBHelper.getReadableDatabase();

        String selection = PooDBHelper.COL_ID + "=?";
        String[] selectArgs = new String[] { String.valueOf(_id) };

        Cursor cursor = db.query(PooDBHelper.TABLE_NAME, null, selection, selectArgs, null, null, null, null);

        PooDTO dto = new PooDTO();

        while(cursor.moveToNext()) {
            // 가져오는 코드 작성!
            dto.set_id(cursor.getInt(cursor.getColumnIndexOrThrow(PooDBHelper.COL_ID)));
            dto.setDate(cursor.getString(cursor.getColumnIndexOrThrow(PooDBHelper.COL_DATE)));
            dto.setCondition(cursor.getString(cursor.getColumnIndexOrThrow(PooDBHelper.COL_CONDITION)));
            dto.setHealth(cursor.getInt(cursor.getColumnIndexOrThrow(PooDBHelper.COL_HEALTH)));
            dto.setIsPoo(cursor.getInt(cursor.getColumnIndexOrThrow(PooDBHelper.COL_ISPOO)));
            dto.setBM(cursor.getString(cursor.getColumnIndexOrThrow(PooDBHelper.COL_BM)));
            dto.setTime(cursor.getString(cursor.getColumnIndexOrThrow(PooDBHelper.COL_TIME)));
            dto.setSmallBM(cursor.getInt(cursor.getColumnIndexOrThrow(PooDBHelper.COL_SmallBM)));
        }
        return dto;
    }

    // 배변 기록 추가
    public boolean insertNewPoo(PooDTO dto) {
        SQLiteDatabase db = pooDBHelper.getWritableDatabase();
        ContentValues row = new ContentValues();

        row.put(PooDBHelper.COL_DATE, dto.getDate());
        row.put(PooDBHelper.COL_CONDITION, dto.getCondition());
        row.put(PooDBHelper.COL_HEALTH, dto.getHealth());
        row.put(PooDBHelper.COL_ISPOO, dto.getIsPoo());
        if(dto.getIsPoo() == 1) {
            row.put(PooDBHelper.COL_BM, dto.getBM());
            row.put(PooDBHelper.COL_TIME, dto.getTime());
            row.put(PooDBHelper.COL_SmallBM, dto.getSmallBM());
        }

        long result = db.insert(PooDBHelper.TABLE_NAME, null, row);
        pooDBHelper.close();

        if(result > 0) return true;
        else return false;
    }

    // 배변 기록 수정
    public boolean updatePoo(PooDTO dto) {
        SQLiteDatabase db = pooDBHelper.getWritableDatabase();

        ContentValues row = new ContentValues();

        row.put(PooDBHelper.COL_DATE, dto.getDate());
        row.put(PooDBHelper.COL_CONDITION, dto.getCondition());
        row.put(PooDBHelper.COL_HEALTH, dto.getHealth());
        row.put(PooDBHelper.COL_ISPOO, dto.getIsPoo());
        if(dto.getIsPoo() == 1) {
            row.put(PooDBHelper.COL_BM, dto.getBM());
            row.put(PooDBHelper.COL_TIME, dto.getTime());
            row.put(PooDBHelper.COL_SmallBM, dto.getSmallBM());
        }

        String whereClause = "_id=?";
        String[] whereArgs = new String[] {String.valueOf(dto.get_id())};
        int result = db.update(PooDBHelper.TABLE_NAME, row, whereClause, whereArgs);

        pooDBHelper.close();

        if(result > 0) return true;
        else return false;
    }
    // 배변 기록 삭제
    public boolean deletePoo(long _id) {
        SQLiteDatabase db = pooDBHelper.getWritableDatabase();

        String whereClause = PooDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(_id) };

        int result = db.delete(PooDBHelper.TABLE_NAME, whereClause, whereArgs);

        pooDBHelper.close();

        if(result > 0) return true;
        else return false;
    }

    // 해당 날짜의 배변 상태 가져오기
    public PooDTO findTodayBM(String date) {
        SQLiteDatabase db = pooDBHelper.getReadableDatabase();

        String selection = PooDBHelper.COL_DATE + "=?";
        String[] selectArgs = new String[] { date };

        Cursor cursor = db.query(PooDBHelper.TABLE_NAME, null, selection, selectArgs, null, null, null, null);

        PooDTO dto = new PooDTO();

        // 오늘 기록이 없다면
        if (cursor == null) {
            dto.setIsPoo(-1);
        } else {
            if(cursor.moveToNext()) {
                // 가져오는 코드 작성!
                dto.setIsPoo(cursor.getInt(cursor.getColumnIndexOrThrow(PooDBHelper.COL_ISPOO)));
                if(dto.getIsPoo() == 1)
                    dto.setBM(cursor.getString(cursor.getColumnIndexOrThrow(PooDBHelper.COL_BM)));
            }
        }
        return dto;
    }
}
