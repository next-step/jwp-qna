package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    public Name() {
    }

    public Name(String name) {
        this.name = name;
    }

    public void change(String name) {
        this.name = name;
    }
}
