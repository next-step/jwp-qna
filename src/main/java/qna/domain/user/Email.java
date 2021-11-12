package qna.domain.user;

import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class Email {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    private String email;

    protected Email() {
    }

    public Email(String email) {
        validate(email);
        this.email = email;
    }

    private void validate(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalStateException("이메일 형식을 확인해 주세요.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Email email1 = (Email) o;
        return Objects.equals(email, email1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
