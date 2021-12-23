package ddwucom.mobile.finalproject.ma02_20190980;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class ToiletMapActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

     LocationManager locationManager;

     PooNetworkManager networkManager;
    private ToiletXmlParser parser;
    private List<ToiletDTO> resultList;

    private GoogleMap mGoogleMap;

    private LatLng currentLoc;
     MarkerOptions markerOptions;
     ArrayList<Marker> markerList;

     public static final String TAG = "ToiletMapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet_map);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        networkManager =  new PooNetworkManager(this);
        parser = new ToiletXmlParser();
        resultList = new ArrayList<>();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 1) map 객체 준비
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        // data가 언제 로딩될지 몰라 비동기적으로 Map 정보를 얻어옴 - network로 map image 집합 정보를 가져온다.
        mapFragment.getMapAsync(mapReadyCallBack);

        if(checkPermission())
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, locationListener);
        
        // 한 번만 xml 파싱 해놓고 재사용 하도록
        String query = "http://openAPI.seoul.go.kr:8088/"+ getString(R.string.seoul_toilet) + "/xml/SearchPublicToiletPOIService/";

        new NetworkAsyncTask().execute(query);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnSearchPlace:
                break;
            case R.id.btnBookmark:
                Intent intent = new Intent(this, ToiletBookMark.class);
                startActivity(intent);
                break;
        }
    }

    // map 로딩이 완료되면 자동으로 호출
    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            // 맵 정보가 로딩된 GoogleMap 객체를 가져옴.
            mGoogleMap = googleMap;

            currentLoc = getLastLocation();

            markerList = new ArrayList<>();

            if(checkPermission()) {
                mGoogleMap.setMyLocationEnabled(true);
            }

            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    ToiletDTO dto = (ToiletDTO) marker.getTag();

                    Log.d(TAG, dto.getToiletName() + " 으으으");

                    Intent intent = new Intent(ToiletMapActivity.this, AddBookMark.class);
                    intent.putExtra("toiletData", dto);
                    startActivity(intent);
                }
            });
        }
    };

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            currentLoc = new LatLng(location.getLatitude(), location.getLongitude());

            Log.d(TAG, "현재 위치 정보 가져옴!" + currentLoc.latitude);
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));

            // 근처 화장실 정보 가져와 마커로 표시하는 메소드
           getToiletInfo();
        }
    };

    public void getToiletInfo() {
        // 기존 마커는 삭제
        for(Marker marker : markerList) {
            marker.remove();
        }
        markerList = new ArrayList<>();

        ArrayList<ToiletDTO> currentToilet = new ArrayList<ToiletDTO>();
        for(ToiletDTO dto : resultList) {
            // 현재 위치의 경도&위도와 화장실의 위도 경도 계산해서 일정 거리라면 어레이리스트(새로 만들어)에 추가
            int distance = getDistance(currentLoc.latitude, dto.getLatitude(), currentLoc.longitude, dto.getLongitude());

            Log.d(TAG, "위치 " + distance);
            // 현재 위치와 화장실의 위치가 1000m 이내라면
            if(distance <= 1000) {
                currentToilet.add(dto);
            }
        }

        for(ToiletDTO dto : currentToilet) {
            LatLng markerLoc = new LatLng(dto.getLatitude(), dto.getLongitude());
            //마커 옵션 정의
            markerOptions = new MarkerOptions();
            markerOptions.position(markerLoc);
            markerOptions.title(dto.getToiletName());
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            Marker newMarker = mGoogleMap.addMarker(markerOptions);

            // 해당 화장실 정보를 tag에 넣는다.
            newMarker.setTag(dto);

            markerList.add(newMarker);
        }
    }

    // 화장실 정보를 가지고 오고, 파싱함 - 네트워크 어싱크태스크
    class NetworkAsyncTask extends AsyncTask<String, Integer, List<ToiletDTO>> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(ToiletMapActivity.this, "Wait", "Downloading...");
        }
        @Override
        protected List<ToiletDTO> doInBackground(String... strings) {
            String address = strings[0];
            String result = null;

            int startIndex = 1;
            int step = 1000;
            int lastIndex = step;

            for(int i = 0; i < 4; i++) {
                String newAddress = address + startIndex + "/" + lastIndex;
                // networking
                result = networkManager.downloadContents(newAddress);

                if (result == null)
                    return null;
                Log.d(TAG, result + " : " + lastIndex + " : " + newAddress);

                resultList.addAll(parser.parse(result));

                startIndex += step;
                lastIndex += step;
            }

            return resultList;
        }

        @Override
        protected void onPostExecute(List<ToiletDTO> result) {
            progressDlg.dismiss();
        }
        
    }
    // 좌표 간의 거리를 구함
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

    // 마지막 위치 가져옴.
    public LatLng getLastLocation() {
        if(checkPermission()) {
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(lastLocation != null)
                return new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        }
        return new LatLng(37.606320, 127.041808);
    }
    
    // 위치 권환 설정 확인
    public boolean checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                return false;
            } else
                return true;
        }
        return false;
    }

    // 권환 확인 후
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "위치 권환 획득", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "위치 권환 미획득", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}