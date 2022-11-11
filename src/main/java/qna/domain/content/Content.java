package qna.domain.content;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.util.Objects;

@Embeddable
public class Content {
    @Lob
    private String contents;

    public Content(String contents) {
        this.contents = contents;
    }

    public Content() {
    }

    public static Content of(String contents) {
        return new Content(contents);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content email1 = (Content) o;
        return Objects.equals(contents, email1.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents);
    }
}
