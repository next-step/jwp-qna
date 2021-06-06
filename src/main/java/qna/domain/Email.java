package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
public class Email {
    private static final int MAXIMUM_EMAIL_LENGTH = 50;
    private static final Pattern EMAIL_PATTERN = PatternEnum.EMAIL.toPattern();
    public static final String INVALID_EMAIL_MESSAGE = "잘못된 이메일 형식입니다.";

    @Column(length = MAXIMUM_EMAIL_LENGTH)
    private String email;

    protected Email() {
    }

    public Email(String email) {
        validate(email);
        this.email = email;
    }

    private void validate(String email) {
        if (Objects.isNull(email) || email.isEmpty() || email.length() > MAXIMUM_EMAIL_LENGTH) {
            throw new IllegalArgumentException(INVALID_EMAIL_MESSAGE);
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.find()) {
            throw new IllegalArgumentException(INVALID_EMAIL_MESSAGE);
        }
    }

    public String getEmail() {
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
