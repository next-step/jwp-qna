package qna.domain.wrap;

import org.apache.logging.log4j.util.Strings;

import java.util.Objects;
import java.util.regex.Pattern;

import static java.lang.String.format;

public class Email {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$");
    private static final int MAXIMUM_LENGTH = 50;

    private final String email;

    public Email(String email) {
        validate(email);

        this.email = email;
    }

    private void validate(String email) {
        if (Objects.isNull(email)) {
            throw new IllegalArgumentException("이메일은 null일 수 없습니다.");
        }else if(!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("이메일 형식이 아닙니다.");
        } else if(email.length() > MAXIMUM_LENGTH) {
            throw new IllegalArgumentException(format("이메일의 최대 길이는 %d자 입니다.", MAXIMUM_LENGTH));
        }
    }

    public String toString() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email1 = (Email) o;
        return Objects.equals(email, email1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
