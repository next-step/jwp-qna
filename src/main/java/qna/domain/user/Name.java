package qna.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

    @Column(length = 20, nullable = false)
    private String name;

    public Name(String name) {
        if (name.isEmpty() || name.length() > 20) {
            throw new IllegalArgumentException("Name이 올바르지 않습니다.");
        }
        this.name = name;
    }

    public Name() {

    }

    public String getName() {
        return name;
    }
}
