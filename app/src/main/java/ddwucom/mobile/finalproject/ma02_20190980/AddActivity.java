package ddwucom.mobile.finalproject.ma02_20190980;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    private RadioGroup rgCondition; // condition
    private RadioGroup rgHealth; // isHealth
    private RadioGroup rgSmallBM; // samllBM(잔변감)

    private String condition; // 기분 저장할 변수
    private int isHealth = -1; // 운동 여부 저장
    private int isPoo = -1; //배변 여부
    private String BM; //배변 상태
    private int isSmallBM = -1; // 잔변감 여부

    private TextView tvDatePicker; // 기록 날짜
    private EditText etTime; // 배변 시간

    // 배변 여부
    private Button btnBMYes;
    private Button btnBMNo;

    // 배변 기록 관련 레이아웃
    private LinearLayout linearLayoutPoo;

    private Button btnPoo1;
    private Button btnPoo2;
    private Button btnPoo3;
    private Button btnPoo4;

    private TextView tvAdd;
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
        setContentView(R.layout.activity_add);

        tvDatePicker = findViewById(R.id.tvDatePicker);
        etTime = findViewById(R.id.etTime);
        btnBMYes = findViewById(R.id.btnBMYes);
        btnBMNo = findViewById(R.id.btnBMNo);
        linearLayoutPoo = findViewById(R.id.linearLayoutPoo);
        btnPoo1 = findViewById(R.id.btnPoo1);
        btnPoo2 = findViewById(R.id.btnPoo2);
        btnPoo3 = findViewById(R.id.btnPoo3);
        btnPoo4 = findViewById(R.id.btnPoo4);

        dbManager = new PooDBManager(this);

        // 저장
        tvAdd = findViewById(R.id.tvAdd);
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(!isCheckedEmpty()) {
                  return;
              } else {
                  PooDTO dto;

                  if(isPoo == 0) {
                      dto = new PooDTO(tvDatePicker.getText().toString(), condition, isHealth, isPoo);
                  } else {
                      dto = new PooDTO(tvDatePicker.getText().toString(), condition, isHealth, isPoo,
                              BM, etTime.getText().toString(), isSmallBM);
                  }

                  boolean rlt = dbManager.insertNewPoo(dto);
                  if(rlt) Toast.makeText(AddActivity.this, "기록 추가 완료", Toast.LENGTH_SHORT).show();
                  else Toast.makeText(AddActivity.this, "기록 추가 실패", Toast.LENGTH_SHORT).show();

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

}