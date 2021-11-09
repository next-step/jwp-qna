package qna.domain.question;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Title {

    @Column(length = 100, nullable = false)
    private String title;

    public Title(String title) {
        if (title.isEmpty() || title.length() > 100) {
            throw new IllegalArgumentException("제목을 올바르게 입력해주세요.");
        }
        this.title = title;
    }

    public Title() {

    }
}
