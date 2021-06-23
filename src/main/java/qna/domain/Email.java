package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
public class Email {

    private static final Pattern EMAIL_PATTERN
            = Pattern.compile("^[a-z](\\w{2,19})@(\\w{2,20})\\.(\\w{2,20})$");
    public static final String INVALID_EMAIL_PATTERN = "Invalid Email pattern";

    @Column(length = 50)
    private String email;

    public Email(String email) {
        this.email = validEmail(email);
    }

    public Email() {

    }

    private String validEmail(String email) {
        if (Objects.isNull(email) || email.isEmpty() || !EMAIL_PATTERN.matcher(email).find()) {
            throw new IllegalArgumentException(INVALID_EMAIL_PATTERN);
        }
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
