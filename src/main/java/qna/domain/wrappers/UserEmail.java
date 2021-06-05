package qna.domain.wrappers;

import qna.util.ValidationUtil;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserEmail {

    private static final int EMAIL_MAX_LENGTH = 50;
    @Column(length = 50)
    private String email;

    protected UserEmail() {
    }

    public UserEmail(String email) {
        ValidationUtil.checkValidTitleLength(email, EMAIL_MAX_LENGTH);
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEmail userEmail = (UserEmail) o;
        return Objects.equals(email, userEmail.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
