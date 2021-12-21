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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class ToiletMapActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

     LocationManager locationManager;

     PooNetworkManager networkManager;
    private ToiletXmlParser parser;
    private ArrayList<ToiletDTO> resultList;

    private GoogleMap mGoogleMap;

    private LatLng currentLoc;
     MarkerOptions markerOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet_map);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        networkManager =  new PooNetworkManager(this);
        parser = new ToiletXmlParser();
        resultList = new ArrayList<>();

        // 1) map 객체 준비
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        // data가 언제 로딩될지 몰라 비동기적으로 Map 정보를 얻어옴 - network로 map image 집합 정보를 가져온다.
        mapFragment.getMapAsync(mapReadyCallBack);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(checkPermission())
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 3000, 5, locationListener);

    }

    // map 로딩이 완료되면 자동으로 호출
    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            // 맵 정보가 로딩된 GoogleMap 객체를 가져옴.
            mGoogleMap = googleMap;

            if(checkPermission()) {
                mGoogleMap.setMyLocationEnabled(true);
            }

            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    ToiletDTO dto = (ToiletDTO) marker.getTag();

                    Intent intent = new Intent(ToiletMapActivity.this, InfoActivity.class);
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

            Log.d("TAG_ACTI", "d" + currentLoc.latitude);
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));

            // 근처 화장실 정보 가져와 마커로 표시하는 메소드
           // getToiletInfo();
        }
    };
    public void getToiletInfo() {
        String query = getString(R.string.toilet_api_link);

        new NetworkAsyncTask().execute(query);

        return;
    }

    class NetworkAsyncTask extends AsyncTask<String, Integer, ArrayList<ToiletDTO>> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(ToiletMapActivity.this, "Wait", "Downloading...");
        }
        @Override
        protected ArrayList<ToiletDTO> doInBackground(String... strings) {
            String address = strings[0];
            String result = null;
            // networking
            result = networkManager.downloadContents(address);
            if (result == null)
                return null;

            resultList = parser.parse(result);

            ArrayList<ToiletDTO> currentToilet = new ArrayList<ToiletDTO>();
            for(ToiletDTO dto : resultList) {
                // 현재 위치의 경도&위도와 화장실의 위도 경도 계산해서 일정 거리라면 어레이리스트(새로 만들어)에 추가
                int distance = getDistance(currentLoc.latitude, dto.getPosy(), currentLoc.longitude, dto.getPosx());

                // 현재 위치와 화장실의 위치가 1000m 이내라면
                if(distance <= 1000) {
                    currentToilet.add(dto);
                }
            }
            return currentToilet;
        }

        @Override
        protected void onPostExecute(ArrayList<ToiletDTO> result) {
            for(ToiletDTO dto : result) {
                //마커 옵션 정의
                markerOptions = new MarkerOptions();
                markerOptions.position(currentLoc);
                markerOptions.title(dto.getDataTitle());
                markerOptions.snippet("개방시간 : " + dto.getOpenTime());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                Marker newMarker = mGoogleMap.addMarker(markerOptions);
                // 해당 화장실 정보를 tag에 넣는다.
                newMarker.setTag(dto);
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

    public boolean checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                return true;
            }
        }
        return false;
    }
}