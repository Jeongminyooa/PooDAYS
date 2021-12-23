package ddwucom.mobile.finalproject.ma02_20190980;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddBookMark extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 200;
    private static final String TAG = "AddBookMark";

    private ToiletDTO toiletData;
    private AddressResultReceiver addressResultReceiver;
    private ToiletDBManager dbManager;

    private TextView tvBookMarkAddress;
    private TextView tvBookMarkName;
    private ImageView ivBookMarkImage;
    private TextView tvAdd;
    private ImageView ivCancel;
    private TextView tvDatePicker; // 기록 날짜
    private EditText etBookMarkMemo;

    private String mCurrentPhotoPath;

    // DatePicker에서 현재 시간을 표시하기 위함.
    Calendar c = Calendar.getInstance();
    int nYear = c.get(Calendar.YEAR);
    int nMon = c.get(Calendar.MONTH);
    int nDay = c.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_mark);

        tvBookMarkAddress = findViewById(R.id.tvBookMarkAddress);
        tvBookMarkName = findViewById(R.id.tvBookMarkName);
        tvDatePicker = findViewById(R.id.tvDatePicker);
        tvAdd = findViewById(R.id.tvAdd);
        ivCancel = findViewById(R.id.ivCancel);
        etBookMarkMemo = findViewById(R.id.etBookMarkMemo);
        ivBookMarkImage= (ImageView) findViewById(R.id.ivAddImage);

        dbManager = new ToiletDBManager(this);

        Intent intent = getIntent();
        toiletData = (ToiletDTO) intent.getSerializableExtra("toiletData");

        tvBookMarkName.setText(toiletData.getToiletName());
        addressResultReceiver = new AddressResultReceiver(new Handler());

        startAddressService();

        ivBookMarkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "click");
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;

                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.d(TAG, mCurrentPhotoPath + " ");

                    if (photoFile != null) {
                        Uri photoUri = FileProvider.getUriForFile(
                                AddBookMark.this,
                                "ddwucom.mobile.finalproject.ma02_20190980.fileprovider",
                                photoFile
                        );
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                        Toast.makeText(AddBookMark.this, mCurrentPhotoPath, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, takePictureIntent.resolveActivity(getPackageManager()) + " ");
                }
            }
        });


        // 저장
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isCheckedEmpty()) {
                    return;
                } else {
                    if(!isCheckedEmpty()) {
                        return;
                    }

                    BookMarkDTO dto = new BookMarkDTO();
                    dto.setAddress(tvBookMarkAddress.getText().toString());
                    dto.setName(tvBookMarkName.getText().toString());
                    dto.setImage(mCurrentPhotoPath);
                    dto.setMemo(etBookMarkMemo.getText().toString());
                    dto.setDate(tvDatePicker.getText().toString());

                    boolean rlt = dbManager.insertBookMark(dto);
                    if(rlt) Toast.makeText(AddBookMark.this, "기록 추가 완료", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(AddBookMark.this, "기록 추가 실패", Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        });

        // 취소
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            ivBookMarkImage.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
        }
    }


    public void onClick(View v) {
        switch (v.getId()) {
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
        }
    }

    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+ timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // 필수 항목이 입력되지 않은 경우 토스트 출력
    public boolean isCheckedEmpty() {
        String empty_view;

        if(tvDatePicker.getText().toString().equals("")) empty_view = "날짜";
        else if(mCurrentPhotoPath == null) empty_view = "사진";
        else if(etBookMarkMemo.getText().toString().equals("")) empty_view = "메모";
        else return true;

        Toast.makeText(this,  empty_view + "은(는) 반드시 입력해야 합니다.",Toast.LENGTH_SHORT).show();
        return false;

    }

    // 위도 -> 경도 주소 변환 IntentService 실행 */
    private void startAddressService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        intent.putExtra(Constants.RECEIVER, addressResultReceiver); // 결과를 수신할 리시버 클래스 객체
        intent.putExtra(Constants.LAT_DATA_EXTRA, toiletData.getLatitude());
        intent.putExtra(Constants.LNG_DATA_EXTRA, toiletData.getLongitude());
        startService(intent);
    }

    /* 위도/경도 → 주소 변환 ResultReceiver */
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            String addressOutput = null;

            if (resultCode == Constants.SUCCESS_RESULT) {
                if (resultData == null) return;
                addressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
                if (addressOutput == null) addressOutput = "";
                tvBookMarkAddress.setText(addressOutput);
            }
        }
    }



}