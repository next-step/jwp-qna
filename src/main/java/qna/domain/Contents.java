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

    public Contents(String contents) {
        this.contents = contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Contents contents1 = (Contents)o;
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
