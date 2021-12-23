package ddwucom.mobile.finalproject.ma02_20190980;

import java.io.Serializable;

public class BlogDTO implements Serializable {
    private String postdate; // 작성 날짜
    private String title; // 포스트 제목
    private String link; // 포스트 링크
    private String description; // 포스트 설명

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
