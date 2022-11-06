package qna.domain;

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

    public static Contents from(String contents) {
        return new Contents(contents);
    }

    @Override
    public String toString() {
        return "Contents{" +
                "contents='" + contents + '\'' +
                '}';
    }
}
