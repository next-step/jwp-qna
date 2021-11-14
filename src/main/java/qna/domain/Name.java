package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
        if (isEmpty(name)) {
            throw new IllegalArgumentException("이름을 입력하지 않았습니다.");
        }
        if (name.length() > NAME_MAX_SIZE) {
            throw new IllegalArgumentException(String.format("이름 길이가 %d자를 초과했습니다.", NAME_MAX_SIZE));
        }
    }
    
    private static boolean isEmpty(String name) {
        return name.isEmpty();
    }

}
