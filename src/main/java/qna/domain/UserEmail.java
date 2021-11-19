package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserEmail {
    public static final String MAX_USER_EMAIL_EXCEPTION_MESSAGE = "user email 최대입력 길이를 초과하였습니다.";
    public static final int MAX_USER_EMAIL_LENGTH = 50;

    @Column(length = MAX_USER_EMAIL_LENGTH)
    private String email;

    protected UserEmail() {
    }

    public UserEmail(String email) {
        validateUserEmailLength(email);
        this.email = email;
    }

    private void validateUserEmailLength(String email) {
        if (email.length() > MAX_USER_EMAIL_LENGTH) {
            throw new IllegalArgumentException(MAX_USER_EMAIL_EXCEPTION_MESSAGE);
        }
    }

    public String getEmail() {
        return email;
    }

    public void changeEmail(String email) {
        validateUserEmailLength(email);
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEmail otherUserEmail = (UserEmail) o;
        return Objects.equals(email, otherUserEmail.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
