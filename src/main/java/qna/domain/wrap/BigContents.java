package qna.domain.wrap;

import java.util.Objects;

public class BigContents {
    private String contents;

    public BigContents(String contents) {
        this.contents = contents;
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
