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

    public Contents(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contents)) return false;
        Contents contents1 = (Contents) o;
        return Objects.equals(getContent(), contents1.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContent());
    }

    @Override
    public String toString() {
        return "Content{" +
                "content='" + content + '\'' +
                '}';
    }
}
