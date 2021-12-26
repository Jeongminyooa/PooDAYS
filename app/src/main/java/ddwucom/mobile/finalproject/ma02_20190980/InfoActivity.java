package ddwucom.mobile.finalproject.ma02_20190980;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {

    final static String TAG = "InfoActivity";
    ArrayList<BlogDTO> blogList;
    InfoAdapter adapter;
    ListView listView;
    ImageView ivShare;
    EditText etShareText;

    PooNetworkManager networkManager;
    NaverBlogXmlParser parser;

    private String query = "https://openapi.naver.com/v1/search/blog.xml?query=";

    // 변 상태 입력시 Naver 블로그 API 사용해서 관련 포스트 가져오기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        listView = findViewById(R.id.lvInfo);
        ivShare = findViewById(R.id.ivShare);
        etShareText = findViewById(R.id.etShareText);

        blogList = new ArrayList<>();
        networkManager = new PooNetworkManager(this);
        parser = new NaverBlogXmlParser();

        adapter = new InfoAdapter(this, R.layout.activity_listview_blog_layout, blogList);
        listView.setAdapter(adapter);

        // 클릭 시 링크로 이동!
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);

                builder.setTitle("안내")
                        .setMessage("관련 네이버 블로그로 이동합니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent (Intent.ACTION_VIEW, Uri.parse(blogList.get(pos).getLink()));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();
            }
        });

        
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);

                intent.setType("text/plain");

                // Set default text message
                // 카톡, 이메일, MMS 다 이걸로 설정 가능

                String subject = "나만의 장 건강 비법을 공유하겠습니다";
                String text = etShareText.getText().toString();
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, text);

                // Title of intent
                Intent chooser = Intent.createChooser(intent, "친구에게 공유하기");
                startActivity(chooser);
            }
        });
    }
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnSearchPoo1: {
                new NetworkAsyncTask().execute(query, "변비 원인");
            }
            break;
            case R.id.btnSearchPoo2: {
                new NetworkAsyncTask().execute(query, "바나나 똥");
            }
                break;
            case R.id.btnSearchPoo3: {
                new NetworkAsyncTask().execute(query, "설사 원인");
            }
                break;
            case R.id.btnSearchPoo4: {
                new NetworkAsyncTask().execute(query, "물똥 원인");
            }
                break;
            case R.id.btnSearchPoo5: {
                new NetworkAsyncTask().execute(query, "건강 습관");
            }
            break;
            case R.id.btnSearchPoo6: {
                new NetworkAsyncTask().execute(query, "장내미생물");
            }
            break;
            case R.id.btnSearchPoo7: {
                new NetworkAsyncTask().execute(query, "식이섬유");
            }
            break;
            case R.id.btnSearchPoo8: {
                new NetworkAsyncTask().execute(query, "유산균");
            }
            break;
            case R.id.btnMain: {
                // 건강 정보 공유하는 Activity
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btnRecord: {
                // 상태 기록 확인하는 Activity
                Intent intent = new Intent(this, RecordActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btnInfo: {
                Toast.makeText(this, "Info 페이지에 접속해있습니다.", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.btnMap:
                // 화장실 맵 API Activity
                Intent intent = new Intent(this, ToiletMapActivity.class);
                startActivity(intent);
                break;
        }
    }

    class NetworkAsyncTask extends AsyncTask<String, Integer, ArrayList<BlogDTO>> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(InfoActivity.this, "Wait", "Downloading...");
        }
        @Override
        protected ArrayList<BlogDTO> doInBackground(String... strings) {
            String address = strings[0];
            String keyword = strings[1];
            String result = null;

            // networking
            result = networkManager.downloadNaverContents(address, keyword);

            if (result == null) return null;

            Log.d(TAG, result);

            blogList.clear();
            blogList.addAll(parser.parse(result));

            Log.d(TAG, blogList.size() + " ");

            return blogList;
        }

        @Override
        protected void onPostExecute(ArrayList<BlogDTO> result) {
            adapter.notifyDataSetChanged();
            progressDlg.dismiss();
        }

    }

    // SNS에 공유 : 영양제나 건강 꿀팁들
    private void shareTwitter() {

        try {
            String sharedText = String.format("http://twitter.com/intent/tweet?text=%s",
                    URLEncoder.encode("공유할 텍스트 입력", "utf-8"));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharedText));
            startActivity(intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}