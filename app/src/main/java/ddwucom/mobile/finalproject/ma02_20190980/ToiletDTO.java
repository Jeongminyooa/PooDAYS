package ddwucom.mobile.finalproject.ma02_20190980;

import java.io.Serializable;

public class ToiletDTO implements Serializable {
    public String ToiletName; // 화장실 이름
    public double latitude; // 위도
    public double longitude; // 경도

    public String getToiletName() {
        return ToiletName;
    }

    public void setToiletName(String toiletName) {
        ToiletName = toiletName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
