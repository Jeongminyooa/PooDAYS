package ddwucom.mobile.finalproject.ma02_20190980;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class NaverBlogXmlParser {
    private static final String TAG = "NaverBlogXmlParser";

    public enum TagType {NONE, TITLE, LINK, DESCRIPTION, DATE};

    final static String TAG_ITEM = "item";
    final static String TAG_TITLE = "title";
    final static String TAG_LINK = "link";
    final static String TAG_DESCRIPTION = "description";
    final static String TAG_DATE = "postdate";

    public NaverBlogXmlParser() {

    }

    public ArrayList<BlogDTO> parse(String xml) {
        ArrayList<BlogDTO> resultList = new ArrayList<>();
        BlogDTO dto = null;

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
                        if (parser.getName().equals(TAG_ITEM)) {
                            dto = new BlogDTO();
                        } else if (parser.getName().equals(TAG_TITLE)) {
                            if (dto != null) {
                                tagType = TagType.TITLE;
                                Log.d(TAG, parser.getName());
                            }
                        } else if(parser.getName().equals(TAG_LINK)) {
                            if (dto != null) tagType = TagType.LINK;
                        } else if(parser.getName().equals(TAG_DESCRIPTION)) {
                            if (dto != null) tagType = TagType.DESCRIPTION;
                        } else if(parser.getName().equals(TAG_DATE)) {
                            if (dto != null) tagType = TagType.DATE;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(TAG_ITEM)) {
                            resultList.add(dto);
                            dto = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {
                            case TITLE:
                                Log.d(TAG, parser.getText());
                                dto.setTitle(parser.getText());
                                break;
                            case LINK :
                                dto.setLink(parser.getText());
                                break;
                            case DESCRIPTION :
                                dto.setDescription(parser.getText());
                                break;
                            case DATE :
                                dto.setPostdate(parser.getText());
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
