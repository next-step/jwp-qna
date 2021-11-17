package qna.domain.answer;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Embeddable
public class Contents {
    @Lob
    private String contents;

    protected Contents() {
    }

    public Contents(String contents) {
        requireNonNull(contents, "답변 내용이 입력되지 않았습니다.");
        this.contents = contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contents contents1 = (Contents) o;
        return Objects.equals(contents, contents1.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents);
    }
}