package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Title {

    @Column(nullable = false, length = 100)
    private String title;

    protected Title() {
    }

    public Title(String title) {
        this.title = title;
    }

    public static Title of(String title) {
        return new Title(title);
    }
}