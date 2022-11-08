package qna.domain.question.title;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Title {

    @Column(length = 100, nullable = false)
    private String title;

    protected Title() {
    }

    public Title(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
