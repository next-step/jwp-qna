package qna.domain.vo;

import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Password {
    private static final int MAX_LENGTH = 20;

    @Column(nullable = false, length = 20)
    private String password;

    protected Password() {
    }

    private Password(String password) {
        validate(password);
        this.password = password;
    }

    private void validate(String password) {
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("비밀번호는 빈값일 수 없습니다.");
        }
        if (password.trim().length() > MAX_LENGTH) {
            throw new IllegalArgumentException("비밀번호는 최대 20자 까지 가능합니다.");
        }
    }

    public static Password of(String password) {
        return new Password(password);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Password password1 = (Password) o;

        return password != null ? password.equals(password1.password) : password1.password == null;
    }

    @Override
    public int hashCode() {
        return password != null ? password.hashCode() : 0;
    }
}
