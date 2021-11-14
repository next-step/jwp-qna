package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class Contents {
    @Lob
    @Column(name = "contents")
    private String value;

    protected Contents() {
    }

    Contents(String value) {
        this.value = value;
    }

    public static Contents from(String value) {
        return new Contents(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
