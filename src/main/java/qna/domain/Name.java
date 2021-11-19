package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

    public static final int MAX_NAME_LENGTH = 20;

    @Column(length = MAX_NAME_LENGTH, nullable = false)
    private String name;

    protected Name() {
    }

    public Name(String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름은 반드시 입력되어야 합니다. (최대 길이: " + MAX_NAME_LENGTH + ")");
        }
        
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("이름은 " + MAX_NAME_LENGTH + "자 이하로 입력 가능합니다. (입력값: " + MAX_NAME_LENGTH + ")");
        }
        this.name = name;
    }

    public void update(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
