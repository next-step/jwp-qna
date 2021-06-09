package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static java.lang.String.format;

@Embeddable
public class Contents {
    public static final int MAXIMUM_LENGTH = 3000;

    @Column(length = MAXIMUM_LENGTH, nullable = false)
    private String contents;

    protected Contents() {

    }

    public Contents(String contents) {
        validate(contents);

        this.contents = contents;
    }

    public void edit(String contents) {
        validate(contents);

        this.contents = contents;
    }

    private void validate(String contents) {
        if (Objects.isNull(contents)) {
            throw new IllegalArgumentException("제목은 null일 수 없습니다.");
        }
        if (contents.length() > MAXIMUM_LENGTH) {
            throw new IllegalArgumentException(format("내용의 최대 길이는 %d자 입니다.", MAXIMUM_LENGTH));
        }
    }

    @Override
    public String toString() {
        return contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contents contents1 = (Contents) o;
        return Objects.equals(contents, contents1.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents);
    }
}
