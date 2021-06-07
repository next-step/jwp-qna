package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Title {
    private int MAX_TITLE_LENGTH = 100;

    @Column(length = 100, nullable = false)
    private String title;


    public Title(String title) {
        validateNull(title);
        validateLength(title);
        this.title = title;
    }

    protected Title(){
    }

    private void validateNull(String title) {
        if (Objects.isNull(title) || title.isEmpty()) {
            throw new IllegalArgumentException("제목을 입력해주세요");
        }
    }

    private void validateLength(String title) {
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("글자 수는 100을 넘을수 없습니다.");
        }
    }
}
