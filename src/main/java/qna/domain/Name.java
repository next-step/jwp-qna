package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import qna.util.ValidationUtils;

@Embeddable
public class Name {
    public static final int NAME_MAX_SIZE = 20;
    
    @Column(nullable = false, length = NAME_MAX_SIZE)
    private String name;
    
    protected Name() {
    }

    private Name(String name) {
        this.name = name;
    }

    public static Name of(String name) {
        checkValidation(name);
        return new Name(name);
    }
    
    private static void checkValidation(String name) {
        ValidationUtils.checkEmpty(name);
        ValidationUtils.checkLength(name, NAME_MAX_SIZE);
    }
    
}
