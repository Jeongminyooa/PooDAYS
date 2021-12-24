package ddwucom.mobile.finalproject.ma02_20190980;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ToiletBookMark extends AppCompatActivity {

    private ToiletAdapter adapter;
    private ToiletDBManager dbManager;
    Cursor cursor;
    ListView lvBookMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet_book_mark);

        lvBookMark = findViewById(R.id.lvBookMarkList);
        dbManager = new ToiletDBManager(this);

        adapter = new ToiletAdapter(this, R.layout.listview_toilet_bookmark, null);
        lvBookMark.setAdapter(adapter);

        // 클릭 시 상세 정보 조회
        lvBookMark.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent = new Intent(ToiletBookMark.this, ShowToiletBookMark.class);
                BookMarkDTO dto = dbManager.getBookMarkById(id);
                intent.putExtra("data", dto);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbManager.getAllToilet(cursor, adapter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null)
            cursor.close();
    }
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnCancel:
                finish();
            break;
        }
    }
}