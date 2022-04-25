package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class Contents {
    @Lob
    private String contents;

    public Contents() {
    }

    public Contents(String contents) {
        this.contents = contents;
    }
}
