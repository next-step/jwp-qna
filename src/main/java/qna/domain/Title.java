package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Title {
    public static final int MAX_LENGTH = 100;
    public static final String EMPTY = "";
    public static final String ERROR_EXCEEDED_MAX_LENGTH = "TITLE 크기를 초과했습니다.";

    @Column(nullable = false, length = MAX_LENGTH)
    private final String title;

    protected Title() {
        this.title = EMPTY;
    }

    public Title(String text) {
        validation(text);
        this.title = text;
    }

    private void validation(String text) {
        if(isMaxLengthExceeded(text)){
            throw new IllegalArgumentException(ERROR_EXCEEDED_MAX_LENGTH);
        }
    }

    private boolean isMaxLengthExceeded(String text) {
        return MAX_LENGTH < text.length();
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Title title1 = (Title)o;
        return Objects.equals(title, title1.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
