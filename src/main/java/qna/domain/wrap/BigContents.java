package qna.domain.wrap;

import org.apache.logging.log4j.util.Strings;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.util.Objects;

import static java.lang.String.format;

@Embeddable
public class BigContents {
    private static final int MAXIMUM_LENGTH = 3000;

    @Lob
    private String contents;

    protected BigContents() {
    }

    public BigContents(String contents) {
        validate(contents);

        this.contents = contents;
    }

    private void validate(String contents) {
        if (Objects.isNull(contents)) {
            throw new IllegalArgumentException("내용은 null일 수 없습니다.");
        }
        if (Strings.isEmpty(contents)) {
            throw new IllegalArgumentException("내용은 공백일 수 없습니다.");
        }
        if (contents.length() > MAXIMUM_LENGTH) {
            throw new IllegalArgumentException(format("내용은 %d자를 넘길 수 없습니다.", MAXIMUM_LENGTH));
        }
    }

    public String toString() {
        return contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BigContents that = (BigContents) o;
        return Objects.equals(contents, that.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents);
    }
}
