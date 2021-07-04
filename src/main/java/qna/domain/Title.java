package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Title {

    public static final String INVALID_TITLE_LENGTH = "Title length is too long (length: 100)";

    @Column(length = 100, nullable = false)
    private String title;

    public Title(String title) {
        if(title.length()>100){
            throw new IllegalArgumentException(INVALID_TITLE_LENGTH);
        }
        this.title = title;
    }

    public Title() {
    }

    public String title(){
        return title;
    }
}
