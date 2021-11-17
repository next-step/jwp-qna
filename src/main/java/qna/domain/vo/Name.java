package qna.domain.vo;

import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {
    private static final int MAX_LENGTH = 20;

    @Column(nullable = false, length = 20)
    private String name;

    protected Name() {
    }

    private Name(String name) {
        validate(name);
        this.name = name.trim();
    }

    private void validate(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("이름은 빈값일 수 없습니다.");
        }
        if (name.trim().length() > MAX_LENGTH) {
            throw new IllegalArgumentException("최대 20자 까지 가능합니다.");
        }
    }

    public static Name of(String name) {
        return new Name(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Name name1 = (Name) o;

        return name != null ? name.equals(name1.name) : name1.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }


}
