package qna.domain.vo;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.util.Objects;

@Embeddable
public class Contents {

    @Lob
    private String contents;

    protected Contents() {
    }

    private Contents(String contents) {
        validate(contents);
        this.contents = contents;
    }

    private void validate(String contents) {
        if (Objects.isNull(contents) || contents.isEmpty()) {
            throw new IllegalArgumentException("컨텐츠는 빈값일 수 없습니다.");
        }
    }

    public static Contents of(String contents) {
        return new Contents(contents);
    }

    public String getValue() {
        return contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contents contents1 = (Contents) o;

        return contents != null ? contents.equals(contents1.contents) : contents1.contents == null;
    }

    @Override
    public int hashCode() {
        return contents != null ? contents.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Contents{" +
                "contents='" + contents + '\'' +
                '}';
    }
}
