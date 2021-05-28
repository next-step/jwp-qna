package qna.domain.wrap;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.util.Objects;

@Embeddable
public class BigContents {
    @Lob
    private String contents;

    protected BigContents() {
    }

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
