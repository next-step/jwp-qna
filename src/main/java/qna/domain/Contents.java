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

    public Contents(String contents) { //TODO: 컨텐츠의 역할이 흐릿함.. 비즈니스가 추가된 후에 검증로직 추가해야 할듯
        this.contents = contents;
    }

    public String getContents() {
        return contents;
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

    @Override
    public String toString() {
        return "Contents{" +
                "contents='" + contents + '\'' +
                '}';
    }
}
