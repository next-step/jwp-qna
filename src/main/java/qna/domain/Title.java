package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import qna.ExceedLengthExcpetion;

@Embeddable
public class Title {
    public static final int MAX_LENGTH = 100;

    @Column(nullable = false, length = MAX_LENGTH)
    private String title;

    protected Title() {
    }

    public Title(String text) {
        validation(text);
        this.title = text;
    }

    private void validation(String text) {
        if(isMaxLengthExceeded(text)){
            throw new ExceedLengthExcpetion(MAX_LENGTH, text.length());
        }
    }

    private boolean isMaxLengthExceeded(String text) {
        return MAX_LENGTH < text.length();
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
