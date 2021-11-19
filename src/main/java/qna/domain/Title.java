package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Title {

    public static final int MAX_TITLE_LENGTH = 100;

    @Column(length = MAX_TITLE_LENGTH, nullable = false)
    private String title;

    protected Title() {
    }

    public Title(String title) {
        if (title == null) {
            throw new IllegalArgumentException("제목은 반드시 입력되어야 합니다. (최대 길이: " + MAX_TITLE_LENGTH + ")");
        }

        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("제목은 " + MAX_TITLE_LENGTH + "자 이하로 입력 가능합니다. (입력값: " + title + ")");
        }
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
