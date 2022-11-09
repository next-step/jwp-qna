package qna.domain.content;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.util.Objects;

@Embeddable
public class Contents {
    @Lob
    private String contents;

    public Contents(String contents) {
        this.contents = contents;
    }

    public Contents() {
    }

    public static Contents of(String contents) {
        return new Contents(contents);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contents email1 = (Contents) o;
        return Objects.equals(contents, email1.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents);
    }
}
