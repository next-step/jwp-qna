package qna.domain;

import javax.persistence.Column;
import java.util.Objects;

public class UserEmail {
    @Column(length = 50)
    private String email;

    protected UserEmail() {
    }

    public UserEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
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
