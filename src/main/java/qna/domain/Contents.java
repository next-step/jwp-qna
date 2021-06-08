package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.util.Objects;

@Embeddable
public class Contents {
    public static final String INVALID_NULL_OR_EMPTY_CONTENTS_MESSAGE = "컨텐츠는 빈 문자열이 될 수 없습니다.";

    @Lob
    private String contents;

    protected Contents() {
    }

    public Contents(String contents) {
        validate(contents);
        this.contents = contents;
    }

    private void validate(String contents) {
        if (Objects.isNull(contents) || contents.isEmpty()) {
            throw new IllegalArgumentException(INVALID_NULL_OR_EMPTY_CONTENTS_MESSAGE);
        }
    }

    public String getContents() {
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

    @Override
    public String toString() {
        return "Contents{" +
                "contents='" + contents + '\'' +
                '}';
    }
}
