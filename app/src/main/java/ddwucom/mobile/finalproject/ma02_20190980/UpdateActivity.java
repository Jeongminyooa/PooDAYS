package ddwucom.mobile.finalproject.ma02_20190980;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {

    PooDTO dto;

    private TextView tvDatePicker; // 기록 날짜
    private EditText etTime; // 배변 시간

    private RadioGroup rgCondition; // condition
    private RadioButton rBtnGood;
    private RadioButton rBtnJust;
    private RadioButton rBtnBad;
    private RadioButton rBtnSad;

    private RadioGroup rgHealth; // isHealth
    private RadioButton rBtnHealthYes;
    private RadioButton rBtnHealthNo;

    private RadioGroup rgSmallBM; // samllBM(잔변감)
    private RadioButton rBtnSmallBMYes;
    private RadioButton rBtnSmallBMNo;

    private String condition; // 기분 저장할 변수
    private int isHealth = -1; // 운동 여부 저장
    private int isPoo = -1; //배변 여부
    private String BM; //배변 상태
    private int isSmallBM = -1; // 잔변감 여부

    // 배변 여부
    private Button btnBMYes;
    private Button btnBMNo;

    // 배변 기록 관련 레이아웃
    private LinearLayout linearLayoutPoo;

    // 배변 상태
    private Button btnPoo1;
    private Button btnPoo2;
    private Button btnPoo3;
    private Button btnPoo4;

    private TextView tvUpdate;
    private ImageView ivCancel;

    // DatePicker에서 현재 시간을 표시하기 위함.
    Calendar c = Calendar.getInstance();
    int nYear = c.get(Calendar.YEAR);
    int nMon = c.get(Calendar.MONTH);
    int nDay = c.get(Calendar.DAY_OF_MONTH);

    PooDBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Intent intent = getIntent();
        dto = (PooDTO) intent.getSerializableExtra("data");

        tvDatePicker = findViewById(R.id.tvDatePicker); //날짜

        // 기분
        rBtnGood = findViewById(R.id.rBtnGood);
        rBtnJust = findViewById(R.id.rBtnJust);
        rBtnBad = findViewById(R.id.rBtnBad);
        rBtnSad = findViewById(R.id.rBtnSad);

        //운동 여부
        rBtnHealthYes = findViewById(R.id.rBtnHealthYes);
        rBtnHealthNo = findViewById(R.id.rBtnHealthNo);

        etTime = findViewById(R.id.etTime); // 시간

        linearLayoutPoo = findViewById(R.id.linearLayoutPoo); // 배변 여부에 따른 문항

        // 배변 상태
        btnPoo1 = findViewById(R.id.btnPoo1);
        btnPoo2 = findViewById(R.id.btnPoo2);
        btnPoo3 = findViewById(R.id.btnPoo3);
        btnPoo4 = findViewById(R.id.btnPoo4);

        // 배변 여부
        btnBMYes = findViewById(R.id.btnBMYes);
        btnBMNo = findViewById(R.id.btnBMNo);

        // 잔변감
        rBtnSmallBMYes = findViewById(R.id.rBtnBMYes);
        rBtnSmallBMNo = findViewById(R.id.rBtnBMNo);

        dbManager = new PooDBManager(this);

        // 정보 세팅
        settingData();
        // 저장
        tvUpdate = findViewById(R.id.tvUpdate);
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isCheckedEmpty()) {
                    return;
                } else {
                    dto.setDate(tvDatePicker.getText().toString());
                    dto.setCondition(condition);
                    dto.setHealth(isHealth);
                    dto.setIsPoo(isPoo);
                    if(isPoo == 1) {
                        dto.setBM(BM);
                        dto.setTime(etTime.getText().toString());
                        dto.setSmallBM(isSmallBM);
                    }

                    boolean rlt = dbManager.updatePoo(dto);
                    if(rlt) Toast.makeText(UpdateActivity.this, "기록 추가 완료", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(UpdateActivity.this, "기록 추가 실패", Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        });

        // 취소
        ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 라디오 그룹 설정 (리스너 연결)
        rgCondition = (RadioGroup) findViewById(R.id.rgCondition);
        rgHealth = (RadioGroup) findViewById(R.id.rgHealth);
        rgSmallBM = (RadioGroup) findViewById(R.id.rgSmallBM);
        rgCondition.setOnCheckedChangeListener(conditionCheckedListener);
        rgHealth.setOnCheckedChangeListener(healthCheckedListener);
        rgSmallBM.setOnCheckedChangeListener(smallBMCheckedListener);

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.imgBtnDate:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        //날짜를 선택 후 확인 버튼을 누른다면 . .
                        String date = year + "-" + (month+1) + "-" + dayOfMonth;
                        tvDatePicker.setText(date);
                    }
                },nYear, nMon, nDay);
                datePickerDialog.setMessage("날짜를 선택하세요.");
                datePickerDialog.show();
                break;
            case R.id.btnPoo1:
                BM = getString(R.string.bm_poo1);
                btnPoo1.setBackgroundColor(getResources().getColor(R.color.white_brown));

                // 나머지 버튼의 백그라운드 색상 변경
                btnPoo2.setBackgroundColor(Color.WHITE);
                btnPoo3.setBackgroundColor(Color.WHITE);
                btnPoo4.setBackgroundColor(Color.WHITE);
                break;
            case R.id.btnPoo2:
                BM = getString(R.string.bm_poo2);
                btnPoo2.setBackgroundColor(getResources().getColor(R.color.white_brown));

                // 나머지 버튼의 백그라운드 색상 변경
                btnPoo1.setBackgroundColor(Color.WHITE);
                btnPoo3.setBackgroundColor(Color.WHITE);
                btnPoo4.setBackgroundColor(Color.WHITE);
                break;
            case R.id.btnPoo3:
                BM = getString(R.string.bm_poo3);
                btnPoo3.setBackgroundColor(getResources().getColor(R.color.white_brown));

                // 나머지 버튼의 백그라운드 색상 변경
                btnPoo2.setBackgroundColor(Color.WHITE);
                btnPoo1.setBackgroundColor(Color.WHITE);
                btnPoo4.setBackgroundColor(Color.WHITE);
                break;
            case R.id.btnPoo4:
                BM = getString(R.string.bm_poo4);
                btnPoo4.setBackgroundColor(getResources().getColor(R.color.white_brown));

                // 나머지 버튼의 백그라운드 색상 변경
                btnPoo2.setBackgroundColor(Color.WHITE);
                btnPoo3.setBackgroundColor(Color.WHITE);
                btnPoo1.setBackgroundColor(Color.WHITE);
                break;
            case R.id.btnBMYes:
                isPoo = 1;
                // 배변을 했다면 관련 기록도 가능하도록
                linearLayoutPoo.setVisibility(View.VISIBLE);

                //선택한 버튼 색깔
                btnBMYes.setBackgroundColor(getResources().getColor(R.color.white_brown));
                btnBMNo.setBackgroundColor(getResources().getColor(R.color.low));
                break;
            case R.id.btnBMNo:
                isPoo = 0;
                // 배변을 안했다면 관련 기록 불가능하도록
                linearLayoutPoo.setVisibility(View.GONE);

                //선택한 버튼 색깔
                btnBMNo.setBackgroundColor(getResources().getColor(R.color.white_brown));
                btnBMYes.setBackgroundColor(getResources().getColor(R.color.low));
                break;
        }
    }
    // 기분 관련 radioGroup 리스너
    RadioGroup.OnCheckedChangeListener conditionCheckedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int id) {
            if(id == R.id.rBtnGood) {
                condition = "좋아요";
            } else if(id == R.id.rBtnJust) {
                condition = "보통";
            } else if(id == R.id.rBtnBad) {
                condition = "나빠요";
            } else {
                condition = "슬퍼요";
            }
        }
    };

    // 운동 여부 관련 radioGroup 리스너
    RadioGroup.OnCheckedChangeListener healthCheckedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int id) {
            if(id == R.id.rBtnHealthYes) {
                isHealth = 1; //운동 O
            } else {
                isHealth = 0; // 운동 X
            }
        }
    };

    // 잔변감 여부 관련 radioGroup 리스너
    RadioGroup.OnCheckedChangeListener smallBMCheckedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int id) {
            if(id == R.id.rBtnBMYes) {
                isSmallBM = 1; //잔변감 O
            } else {
                isSmallBM = 0; //잔변감 X
            }
        }
    };

    // 필수 항목이 입력되지 않은 경우 토스트 출력
    public boolean isCheckedEmpty() {
        String empty_view;

        if(tvDatePicker.getText().toString().equals("")) empty_view = "기록 날짜";
        else if(condition == null) empty_view = "기분";
        else if(isHealth == -1) empty_view = "운동 여부";
        else if(isPoo == -1) empty_view = "배변 여부";
        else if(isPoo == 1) { // 배변을 했다면
            if(BM == null) empty_view = "배변 상태";
            else if(etTime.getText().toString().equals("")) empty_view = "배변 시간";
            else if(isSmallBM == -1) empty_view = "잔변감 여부";
            else return true;
        }
        else return true;

        Toast.makeText(this,  empty_view + "은(는) 반드시 입력해야 합니다.",Toast.LENGTH_SHORT).show();
        return false;

    }

    // 정보 세팅
    public void settingData() {
        // 기록 날짜
        tvDatePicker.setText(dto.getDate());
        // 기분
        switch(dto.getCondition()) {
            case "좋아요":
                condition = "좋아요";
                rBtnGood.setChecked(true);
                break;
            case "보통":
                condition = "보통";
                rBtnJust.setChecked(true);
                break;
            case "나빠요":
                condition = "나빠요";
                rBtnBad.setChecked(true);
                break;
            case "슬퍼요":
                condition = "슬퍼요";
                rBtnSad.setChecked(true);
                break;
        }
        // 운동
        switch(dto.getHealth()) {
            case 1:
                isHealth = 1;
                rBtnHealthYes.setChecked(true);
                break;
            case 0:
                isHealth = 0;
                rBtnHealthNo.setChecked(true);
                break;
        }
        // 배변 여부
        if(dto.getIsPoo() == 1) {
            isPoo = 1;
            btnBMYes.setBackgroundColor(getResources().getColor(R.color.white_brown));
            linearLayoutPoo.setVisibility(View.VISIBLE);
            // 배변 상태
            if(dto.getBM().equals(getString(R.string.bm_poo1))) {
                BM = getString(R.string.bm_poo1);
                btnPoo1.setBackgroundColor(getResources().getColor(R.color.white_brown));
            }else if(dto.getBM().equals(getString(R.string.bm_poo2))) {
                BM = getString(R.string.bm_poo2);
                btnPoo2.setBackgroundColor(getResources().getColor(R.color.white_brown));
            } else if(dto.getBM().equals(getString(R.string.bm_poo3))) {
                BM = getString(R.string.bm_poo3);
                btnPoo3.setBackgroundColor(getResources().getColor(R.color.white_brown));
            }else {
                BM = getString(R.string.bm_poo4);
                btnPoo4.setBackgroundColor(getResources().getColor(R.color.white_brown));
            }

            // 배변 시간
            etTime.setText(dto.getTime());

            // 잔변감 여부
            if(dto.getSmallBM() == 1) {
                isSmallBM = 1;
                rBtnSmallBMYes.setChecked(true);
            } else {
                isSmallBM = 0;
                rBtnSmallBMNo.setChecked(true);
            }
        } else {
            isPoo = 0;
            btnBMNo.setBackgroundColor(getResources().getColor(R.color.white_brown));
        }
    }

}