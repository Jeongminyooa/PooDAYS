package ddwucom.mobile.finalproject.ma02_20190980;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowToiletBookMark extends AppCompatActivity {

    TextView tvBookMarkDate;
    TextView tvBookMarkName;
    TextView tvBookMarkAddress;
    ImageView ivBookMarkImage;
    TextView tvBookMarkMemo;
    ImageView ivBookMark;
    ImageView ivCancel;

    ToiletDBManager dbManager;

    private boolean isBookMark = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_toilet_book_mark);

        dbManager = new ToiletDBManager(this);

        tvBookMarkDate = findViewById(R.id.tvBookMarkName);
        tvBookMarkName = findViewById(R.id.tvBookMarkName);
        tvBookMarkAddress = findViewById(R.id.tvBookMarkAddress);
        ivBookMarkImage = findViewById(R.id.ivAddImage);
        tvBookMarkMemo = findViewById(R.id.tvBookMarkMemo);
        ivBookMark = findViewById(R.id.ivBookMark);
        ivCancel = findViewById(R.id.ivCancel);

        Intent intent = getIntent();
        BookMarkDTO dto = (BookMarkDTO) intent.getSerializableExtra("data");

        tvBookMarkDate.setText(dto.getDate());
        tvBookMarkName.setText(dto.getName());
        tvBookMarkAddress.setText(dto.getAddress());
        ivBookMarkImage.setImageBitmap(BitmapFactory.decodeFile(dto.getImage()));
        tvBookMarkMemo.setText(dto.getMemo());

        // 북마크 이미지 버튼 -> 해지 가능...
        ivBookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBookMark) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowToiletBookMark.this);
                    builder.setTitle("주의")
                            .setMessage("북마크를 해지하고 나가면 기록이 사라집니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    boolean rlt = dbManager.deleteBookMark(dto.get_id());

                                    if(rlt) Toast.makeText(ShowToiletBookMark.this, "북마크 해지 완료", Toast.LENGTH_SHORT).show();
                                    else Toast.makeText(ShowToiletBookMark.this, "북마크 해지 오류", Toast.LENGTH_SHORT).show();

                                    ivBookMark.setImageResource(R.mipmap.baseline_bookmark_border_white_24);
                                    isBookMark = false;
                                }
                            })
                            .setNegativeButton("아니오", null)
                            .setCancelable(false)
                            .show();
                } else {
                    // 북마크 재등록
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowToiletBookMark.this);
                    builder.setTitle("북마크 재등록")
                            .setMessage("북마크를 재등록합니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    boolean rlt = dbManager.insertBookMark(dto);

                                    if(rlt) Toast.makeText(ShowToiletBookMark.this, "북마크 재등록 완료", Toast.LENGTH_SHORT).show();
                                    else Toast.makeText(ShowToiletBookMark.this, "북마크 재등록 오류", Toast.LENGTH_SHORT).show();

                                    ivBookMark.setImageResource(R.mipmap.baseline_bookmark_white_24);
                                    isBookMark = true;
                                }
                            })
                            .setNegativeButton("아니오", null)
                            .setCancelable(false)
                            .show();
                }
                return;
            }
        });

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}