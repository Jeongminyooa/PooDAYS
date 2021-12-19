package ddwucom.mobile.finalproject.ma02_20190980;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class RecordActivity extends AppCompatActivity {

    PooAdapter adapter;
    PooDBManager dbManager;
    Cursor cursor;
    ListView lvPoo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        lvPoo = findViewById(R.id.listViewPoo);
        dbManager = new PooDBManager(this);

        adapter = new PooAdapter(this, R.layout.listview_layout, null);

        lvPoo.setAdapter(adapter);

        // 수정
        lvPoo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent = new Intent(RecordActivity.this, UpdateActivity.class);
                PooDTO dto = dbManager.getPooById(id);
                intent.putExtra("data", dto);
                startActivity(intent);
            }
        });
        // 삭제
        lvPoo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);
                builder.setTitle("기록 삭제")
                        .setMessage("정말 기록을 삭제하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean rlt = dbManager.deletePoo(id);
                                if(rlt) Toast.makeText(RecordActivity.this, "기록 삭제 완료", Toast.LENGTH_SHORT).show();
                                else Toast.makeText(RecordActivity.this, "기록 삭제 실패", Toast.LENGTH_SHORT).show();

                                dbManager.getAllPoo(cursor, adapter);
                            }
                        })
                       .setNegativeButton("아니오", null)
                        .setCancelable(false)
                        .show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbManager.getAllPoo(cursor, adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null)
            cursor.close();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddInRecord: {
                // 상태 기록 추가하는 Activity
                Intent intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btnMain: {
                Intent intent = new Intent(this, PooDaysActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btnRecord:
                // 상태 기록 확인하는 Activity
                Toast.makeText(this, "기록 확인 페이지에 접속해있습니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnInfo:
                // 건강 정보 공유하는 Activity
                break;

            case R.id.btnMap:
                // 화장실 맵 API Activity
                break;
        }
    }

}