package ddwucom.mobile.finalproject.ma02_20190980;

import static java.lang.Math.sin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class ToiletMapActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    LocationManager locationManager;

    PooNetworkManager networkManager;
    ToiletXmlParser parser;
    ArrayList<ToiletDTO> resultList;

    GoogleMap mGoogleMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet_map);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        networkManager =  new PooNetworkManager(this);
        parser = new ToiletXmlParser();
        resultList = new ArrayList();

        // 1) map 객체 준비
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        // data가 언제 로딩될지 몰라 비동기적으로 Map 정보를 얻어옴 - network로 map image 집합 정보를 가져온다.
        mapFragment.getMapAsync(mapReadyCallBack);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(checkPermission())
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListener);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 마커 삭제

        // 마커 , 선 다 삭제

    }
    // map 로딩이 완료되면 자동으로 호출
    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            // 맵 정보가 로딩된 GoogleMap 객체를 가져옴.
            mGoogleMap = googleMap;
        }
    }

    public void getToiletInfo() {
        String query = getString(R.string.toilet_api_link);

        try {
            // encoding이 제대로 안되는 예외 상황 발생
            new NetworkAsyncTask().execute(apiAddress
                    + URLEncoder.encode(query, "UTF-8"));
        } catch (UnsupportedEncodingException e) { e.printStackTrace(); }
        return;
    }

    class NetworkAstncTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(ToiletMapActivity.this, "Wait", "Downloading...");
        }
        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0];
            String result = null;
            // networking
            result = networkManager.downloadContents(address);
            if (result == null) return "Error";

            // parsing - 수행시간이 많이 걸릴 경우 이곳(스레드 내부)에서 수행하는 것을 고려
            // parsing 을 이곳에서 수행할 경우 AsyncTask의 반환타입을 적절히 변경
            resultList = parser.parse(result);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // parsing - 수행시간이 짧을 경우 이 부분에서 수행하는 것을 고려
            //resultList = parser.parse(result);
            for(ToiletDTO dto : resultList) {
                // 현재 위치의 경도&위도와 화장실의 위도 경도 계산해서 일정 거리라면 어레이리스트(새로 만들어)에 추가
            }
            progressDlg.dismiss();
        }


        public int getDistance(double lat1, double lat2, double lng1, double lng2) {
            Location locationA = new Location("point A");
            locationA.setLatitude(lat1);
            locationA.setLongitude(lng1);

            Location locationB = new Location("point B");
            locationB.setLatitude(lat2);
            locationB.setLongitude(lng2);

            int distance = (int) locationA.distanceTo(locationB);

            return distance;
        }
    }

}