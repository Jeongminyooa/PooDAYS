package ddwucom.mobile.finalproject.ma02_20190980;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class InfoActivity extends AppCompatActivity {


    // 변 상태 입력시 Naver 블로그 API 사용해서 관련 포스트 가져오기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }
}