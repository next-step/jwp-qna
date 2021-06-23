package qna.domain;

import qna.UnAuthorizedException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class Password {
    private static final Pattern PASSWORD_PATTERN
            = Pattern.compile("^[\\w\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\.\\-\\=\\+\\]]{8,20}$");
    public static final String INVALID_PASSWORD_PATTERN = "Invalid Password pattern";

    @Column(nullable = false, length = 20)
    private String password;

    public Password(String password) {
        this.password = validPassword(password);
    }

    public Password() {
    }

    private String validPassword(String password) {
        if (Objects.isNull(password) || password.isEmpty() || !PASSWORD_PATTERN.matcher(password).find()) {
            throw new IllegalArgumentException(INVALID_PASSWORD_PATTERN);
        }
        return password;
    }

    public void matchPassword(Password targetPassword) {
        if (!this.password.equals(targetPassword.password)) {
            throw new UnAuthorizedException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
