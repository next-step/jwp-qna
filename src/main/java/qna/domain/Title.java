package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Title {
    public static final int TITLE_MAX_SIZE = 100;
    
    @Column(nullable = false, length = TITLE_MAX_SIZE)
    private String title;
    
    protected Title() {
    }

    private Title(String title) {
        checkValidation(title);
        this.title = title;
    }

    public static Title of(String title) {
        return new Title(title);
    }
    
    private static void checkValidation(String title) {
        if (isEmpty(title)) {
            throw new IllegalArgumentException("제목을 입력하지 않았습니다.");
        }
        if (title.length() > TITLE_MAX_SIZE) {
            throw new IllegalArgumentException(String.format("제목의 길이가 %d자를 초과했습니다.", TITLE_MAX_SIZE));
        }
    }
    
    private static boolean isEmpty(String title) {
        return title.isEmpty();
    }
}
