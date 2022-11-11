package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Title {
    @Column(length = 100, nullable = false)
    private String title;

    public Title() {
    }

    public Title(String title) {
        this.title = title;
    }
}
