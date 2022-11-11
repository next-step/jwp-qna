package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.util.Objects;

@Embeddable
public class Contents {

    @Lob
    private String content;

    protected Contents() {
    }

    public Contents(String contents) {
        this.content = contents;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String contents) {
        this.content = contents;
    }

    public static Contents from(String contents) {
        return new Contents(contents);
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
        return Objects.equals(content, contents1.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
