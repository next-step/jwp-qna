package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.util.StringUtils;

@Embeddable
public class Password {

    @Column(name = "password", length = 20, nullable = false)
    private String value;

    protected Password() {
    }

    private Password(String value) {
        validate(value);
        this.value = value;
    }

    public static Password from(String value) {
        return new Password(value);
    }

    private void validate(String value) {
        if (StringUtils.hasText(value)) {
            return;
        }

        throw new IllegalArgumentException("비밀번호는 널 또는 공백일 수 없습니다.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Password password = (Password) o;
        return Objects.equals(value, password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
