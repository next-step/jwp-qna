package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class Contents {
    @Lob
    private String contents;

    protected Contents() {
    }

    private Contents(String contents) {
        this.contents = contents;
    }

    public static Contents of(String contents) {
        return new Contents(contents);
    }
}
