package ddwucom.mobile.finalproject.ma02_20190980;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ToiletXmlParser {
    public enum TagType {NONE, TITLE, TEL, MW, OPEN_TIME, LAT, LNG, AREA};

    final static String TAG_LIST = "list";
    final static String TAG_TITLE = "dataTitle";
    final static String TAG_TEL = "manTel";
    final static String TAG_MW = "mw";
    final static String TAG_TIME = "openTime";
    final static String TAG_LAT = "posy";
    final static String TAG_LNG = "posx";
    final static String TAG_AREA = "toiletArea";

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
                        if (parser.getName().equals(TAG_LIST)) {
                            dto = new ToiletDTO();
                        } else if (parser.getName().equals(TAG_TITLE)) {
                            if (dto != null) tagType = TagType.TITLE;
                        } else if (parser.getName().equals(TAG_TEL)) {
                            if (dto != null) tagType = TagType.TEL;
                        } else if (parser.getName().equals(TAG_MW)) {
                            if (dto != null) tagType = TagType.MW;
                        } else if(parser.getName().equals(TAG_TIME)) {
                            if (dto != null) tagType = TagType.OPEN_TIME;
                        } else if(parser.getName().equals(TAG_LAT)) {
                            if (dto != null) tagType = TagType.LAT;
                        } else if(parser.getName().equals(TAG_LNG)) {
                            if (dto != null) tagType = TagType.LNG;
                        } else if(parser.getName().equals(TAG_AREA)) {
                            if (dto != null) tagType = TagType.AREA;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(TAG_LIST)) {
                            resultList.add(dto);
                            dto = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {
                            case TITLE:
                                dto.setDataTitle(parser.getText());
                                break;
                            case TEL :
                                dto.setManTel(parser.getText());
                                break;
                            case MW :
                                dto.setMw(parser.getText());
                                break;
                            case OPEN_TIME:
                                dto.setOpenTime(parser.getText());
                                break;
                            case LAT :
                                dto.setPosy(Double.valueOf(parser.getText()));
                                break;
                            case LNG :
                                dto.setPosx(Double.valueOf(parser.getText()));
                                break;
                            case AREA :
                                dto.setToiletArea(parser.getText());
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
