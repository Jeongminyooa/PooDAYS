package ddwucom.mobile.finalproject.ma02_20190980;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ToiletXmlParser {
    public enum TagType {NONE, NAME, LAT, LNG};

    final static String TAG_ROW = "row";
    final static String TAG_NAME = "FNAME";
    final static String TAG_LAT = "Y_WGS84";
    final static String TAG_LNG = "X_WGS84";

    public ToiletXmlParser() {

    }

    public ArrayList<ToiletDTO> parse(String xml) {
        ArrayList<ToiletDTO> resultList = new ArrayList<>();
        ToiletDTO dto = null;

        TagType tagType = TagType.NONE;

        try {
            //Boilerplate Code : 특정 기능을 구현할 때 반복적으로 비슷한 형태로 나타나는 코드
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals(TAG_ROW)) {
                            dto = new ToiletDTO();
                        } else if (parser.getName().equals(TAG_NAME)) {
                            if (dto != null) tagType = TagType.NAME;
                        } else if(parser.getName().equals(TAG_LAT)) {
                            if (dto != null) tagType = TagType.LAT;
                        } else if(parser.getName().equals(TAG_LNG)) {
                            if (dto != null) tagType = TagType.LNG;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(TAG_ROW)) {
                            resultList.add(dto);
                            dto = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {
                            case NAME:
                                dto.setToiletName(parser.getText());
                                break;
                            case LAT :
                                dto.setLatitude(Double.valueOf(parser.getText()));
                                break;
                            case LNG :
                                dto.setLongitude(Double.valueOf(parser.getText()));
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
