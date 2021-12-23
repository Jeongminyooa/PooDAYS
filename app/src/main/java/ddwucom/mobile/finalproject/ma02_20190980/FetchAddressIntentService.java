package ddwucom.mobile.finalproject.ma02_20190980;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddressIntentService extends IntentService {
    private Geocoder geocoder;
    private ResultReceiver receiver;

    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        if(intent == null) return;
        double latitude = intent.getDoubleExtra(Constants.LAT_DATA_EXTRA, 0);
        double longitude = intent.getDoubleExtra(Constants.LNG_DATA_EXTRA, 0);
        receiver = intent.getParcelableExtra(Constants.RECEIVER);

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

//        결과로부터 주소 추출
        if (addresses == null || addresses.size()  == 0) {
            deliverResultToReceiver(Constants.FAILURE_RESULT, null);
        } else {
            Address addressList = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            for(int i = 0; i <= addressList.getMaxAddressLineIndex(); i++) {
                addressFragments.add(addressList.getAddressLine(i));
            }

            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments));
        }
    }

    //    ResultReceiver 에게 결과를 Bundle 형태로 전달
    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle(); // 데이터를 묶어주는 역할
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        receiver.send(resultCode, bundle);
    }

}
