package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {
    @Column(nullable = false, length = 20)
    private String name;
    
    protected Name() {
    }

    private Name(String name) {
        this.name = name;
    }

    public static Name of(String name) {
        return new Name(name);
    }
    
    public boolean isEmpty() {
        return name.isEmpty();
    }

}
