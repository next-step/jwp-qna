package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
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

    public String getContents() {
        return contents;
    }

    public void changeContents(String contents) {
        this.contents = contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contents otherContents = (Contents) o;
        return Objects.equals(contents, otherContents.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents);
    }
}
