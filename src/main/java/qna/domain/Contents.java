package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class Contents {
    public static final String EMPTY = "";

    @Column
    @Lob
    private final String contents;

    public Contents() {
        this.contents = EMPTY;
    }

    public Contents(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }
}
