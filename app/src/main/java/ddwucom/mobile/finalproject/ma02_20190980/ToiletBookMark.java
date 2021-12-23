package ddwucom.mobile.finalproject.ma02_20190980;

import androidx.appcompat.app.AppCompatActivity;

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

        lvBookMark = findViewById(R.id.lvBookMark);
        dbManager = new ToiletDBManager(this);

        adapter = new ToiletAdapter(this, R.layout.listview_toilet_bookmark, null);
        lvBookMark.setAdapter(adapter);

        lvBookMark.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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