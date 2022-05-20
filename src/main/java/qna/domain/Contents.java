package qna.domain;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
public class Contents {

    @Lob
    private String contents;

    protected Contents() {
    }

    protected Contents(String contents) {
        this.contents = contents;
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
