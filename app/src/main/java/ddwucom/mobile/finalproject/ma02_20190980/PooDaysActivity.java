package ddwucom.mobile.finalproject.ma02_20190980;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class PooDaysActivity extends AppCompatActivity {

    static final String TAG = "pooDAYS";
    Calendar c = Calendar.getInstance();
    int nYear = c.get(Calendar.YEAR);
    int nMon = c.get(Calendar.MONTH);
    int nDay = c.get(Calendar.DAY_OF_MONTH) + 1;

    private TextView tvTodayDate;
    private TextView tvIntro;

    private PooDBManager pooDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poo_days);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String todayDate = nYear + "-" + nMon + "-" + nDay;

        tvTodayDate = findViewById(R.id.tvTodayDate);
        tvIntro = findViewById(R.id.tvIntro);
        Log.d(TAG, todayDate);

        pooDBManager = new PooDBManager(this);

        PooDTO todayDTO = pooDBManager.findTodayBM(todayDate);

        tvTodayDate.setText(nYear + "년 " + nMon + "월 " + nDay + "일");

        if(todayDTO.getIsPoo() == -1) { // 대변 기록 없는 경우
            tvIntro.setText("반갑습니다. " + name + " 님!\n\n" + "오늘의 기록도 추가해주세요!");
        } else if(todayDTO.getIsPoo() == 0) { // 대변 기록은 있으나, 대변을 보지 않은 경우
            tvIntro.setText("반갑습니다. " + name + " 님!\n\n" + "장에 좋은 음식을 섭취해보세요!");
        } else {
            tvIntro.setText("반갑습니다. " + name + " 님!\n\n" + "오늘은 " + todayDTO.getBM() + " 하셨군요!");
        }
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnAdd: {
                // 상태 기록 추가하는 Activity
                Intent intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btnMain:
                Toast.makeText(this, "메인 페이지에 접속해있습니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnRecord: {
                // 상태 기록 확인하는 Activity
                Intent intent = new Intent(this, RecordActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btnInfo:
                // 건강 정보 공유하는 Activity
                break;

            case R.id.btnMap:
                // 화장실 맵 API Activity
                Intent intent = new Intent(this, ToiletMapActivity.class);
                startActivity(intent);
                break;
        }
    }
}