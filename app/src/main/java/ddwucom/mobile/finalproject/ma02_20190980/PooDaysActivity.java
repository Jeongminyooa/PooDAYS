package ddwucom.mobile.finalproject.ma02_20190980;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PooDaysActivity extends AppCompatActivity {

    static final String TAG = "pooDAYS";
    Calendar c = Calendar.getInstance();
    int nYear = c.get(Calendar.YEAR);
    int nMon = c.get(Calendar.MONTH) + 1;
    int nDay = c.get(Calendar.DAY_OF_MONTH);

    private TextView tvTodayDate;
    private TextView tvIntro;

    private PooDBManager pooDBManager;

    private MaterialCalendarView materialCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poo_days);

        tvTodayDate = findViewById(R.id.tvTodayDate);
        tvIntro = findViewById(R.id.tvIntro);
        pooDBManager = new PooDBManager(this);

        // 캘린더 세팅
        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        settingCalendar();

        String todayDate = nYear + "-" + nMon + "-" + nDay;
        PooDTO todayDTO = pooDBManager.findTodayBM(todayDate);

        tvTodayDate.setText(nYear + "년 " + nMon + "월 " + nDay + "일");

        if(todayDTO.getIsPoo() == -1) { // 대변 기록 없는 경우
            tvIntro.setText("반갑습니다!\n\n" + "오늘의 기록도 추가해주세요!");
        } else if(todayDTO.getIsPoo() == 0) { // 대변 기록은 있으나, 대변을 보지 않은 경우
            tvIntro.setText("반갑습니다!\n\n" + "대변을 보기 위해 \n장에 좋은 음식을 섭취해보세요!");
        } else {
            tvIntro.setText("반갑습니다!\n\n" + "오늘은 " + todayDTO.getBM() + " 하셨군요!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String todayDate = nYear + "-" + nMon + "-" + nDay;
        PooDTO todayDTO = pooDBManager.findTodayBM(todayDate);

        tvTodayDate.setText(nYear + "년 " + nMon + "월 " + nDay + "일");

        if(todayDTO.getIsPoo() == -1) { // 대변 기록 없는 경우
            tvIntro.setText("반갑습니다!\n\n" + "오늘의 기록도 추가해주세요!");
        } else if(todayDTO.getIsPoo() == 0) { // 대변 기록은 있으나, 대변을 보지 않은 경우
            tvIntro.setText("반갑습니다!\n\n" + "대변을 보기 위해 \n장에 좋은 음식을 섭취해보세요!");
        } else {
            tvIntro.setText("반갑습니다!\n\n" + "오늘은 " + todayDTO.getBM() + " 하셨군요!");
        }

        // 달력에 배변 여부에 따라 표시
        CalendarDay calendarDay = materialCalendarView.getSelectedDate();
        int year = calendarDay.getYear();
        int month = calendarDay.getMonth() + 1;

        String queryDate = year + "-" + month;
        ArrayList<PooDTO> pooList =  pooDBManager.findMonthIsPoo(queryDate);

        ArrayList<CalendarDay> dates = new ArrayList<>();

        for(PooDTO record : pooList) {
            if (record.getIsPoo() == 1) {
                // 배변 O
                try {
                    String recordDate = record.getDate();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = simpleDateFormat.parse(recordDate);

                    Calendar c = Calendar.getInstance();
                    c.setTime(date);

                    CalendarDay day = CalendarDay.from(c);
                    dates.add(day);

                    materialCalendarView.addDecorators(new EventDecorator(getResources().getColor(R.color.dark_brown),
                            dates));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
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
            case R.id.btnInfo: {
                // 건강 정보 공유하는 Activity
                Intent intent = new Intent(this, InfoActivity.class);
                startActivity(intent);
                Log.d(TAG, "click");
            }
                break;
            case R.id.btnMap: {
                // 화장실 맵 API Activity
                Intent intent = new Intent(this, ToiletMapActivity.class);
                startActivity(intent);
            }
                break;
        }
    }
    
    public void settingCalendar() {
        // 오늘 날짜 표시
        materialCalendarView.setSelectedDate(CalendarDay.today());

        // 달력의 시작과 끝을 지정
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1))
                .setMaximumDate(CalendarDay.from(2030, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        // 달력에 주말과 오늘 날짜를 표시
        materialCalendarView.addDecorators(
                new SaturdayDecorator(),
                new SundayDecorator(),
                new TodayDecorator()
        );

        // 달력에 배변 여부에 따라 표시
        CalendarDay calendarDay = materialCalendarView.getSelectedDate();
        int year = calendarDay.getYear();
        int month = calendarDay.getMonth() + 1;

        String queryDate = year + "-" + month;
        ArrayList<PooDTO> pooList =  pooDBManager.findMonthIsPoo(queryDate);

        ArrayList<CalendarDay> dates = new ArrayList<>();

        for(PooDTO record : pooList) {
            if(record.getIsPoo() == 1) {
                // 배변 O
                try {
                    String recordDate = record.getDate();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = simpleDateFormat.parse(recordDate);

                    Calendar c = Calendar.getInstance();
                    c.setTime(date);

                    CalendarDay day = CalendarDay.from(c);
                    dates.add(day);

                    materialCalendarView.addDecorators(new EventDecorator(getResources().getColor(R.color.dark_brown),
                            dates));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }


        // 달이 바뀌어도 표시가 보임
        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                int year = date.getYear();
                int month = date.getMonth() + 1;

                String queryDate = year + "-" + month;
                ArrayList<PooDTO> pooList =  pooDBManager.findMonthIsPoo(queryDate);

                Log.d(TAG, queryDate + " ");
                ArrayList<CalendarDay> dates = new ArrayList<>();

                for(PooDTO record : pooList) {
                    if(record.getIsPoo() == 1) {
                        try {
                            String recordDate = record.getDate();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date1 = simpleDateFormat.parse(recordDate);

                            Calendar c = Calendar.getInstance();
                            c.setTime(date1);

                            CalendarDay day = CalendarDay.from(c);
                            dates.add(day);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                materialCalendarView.addDecorators(
                        new EventDecorator(getResources().getColor(R.color.dark_brown),
                                dates));
            }
        });
        // 달력이 선택될 때마다 기록이 보인다.
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int year = date.getYear();
                int month = date.getMonth() + 1;
                int day = date.getDay();

                String month_String = String.valueOf(month);
                String day_String = String.valueOf(day);

                if(day < 10) {
                    day_String = "0" + day;
                }

                if(month < 10) {
                    month_String = "0" + month;
                }
                String searchDate = year + "-" + month_String + "-" + day_String;

                int search_id = pooDBManager.findIdByDate(searchDate);

                Log.d(TAG, search_id + searchDate);
                if(search_id != 0) {
                    Intent intent = new Intent(PooDaysActivity.this, UpdateActivity.class);
                    PooDTO dto = pooDBManager.getPooById(search_id);
                    intent.putExtra("data", dto);
                    startActivity(intent);

                } else {
                    Toast.makeText(PooDaysActivity.this, "해당 날짜에는 기록이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}