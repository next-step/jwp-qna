package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
public class Email {
    private static final int MAXIMUM_EMAIL_LENGTH = 50;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,50})$");
    public static final String INVALID_EMAIL = "잘못된 이메일 입력입니다.";

    @Column(length = 50)
    private String email;

    protected Email() {
    }

    public Email(String email) {
        this.email = validate(email);
    }

    private String validate(String email) {
        if (Objects.isNull(email) || email.isEmpty() || email.length() > MAXIMUM_EMAIL_LENGTH) {
            throw new IllegalArgumentException(INVALID_EMAIL);
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.find()) {
            throw new IllegalArgumentException(INVALID_EMAIL);
        }
        return email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email email1 = (Email) o;
        return Objects.equals(getEmail(), email1.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail());
    }

    @Override
    public String toString() {
        return "Email{" +
                "email='" + email + '\'' +
                '}';
    }
}
