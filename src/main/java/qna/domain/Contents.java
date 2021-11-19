package qna.domain;

import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class Contents {

    @Lob
    private String contents;

    protected Contents() {
    }

    private Contents(final String contents) {
        this.contents = contents;
    }

    public static Contents from(final String contents) {
        return new Contents(contents);
    }

    public String contents() {
        return contents;
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
