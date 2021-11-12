package qna.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Email {

    @Column(length = 50)
    private String email;

    public Email(String email) {
        if (email.length() > 50) {
            throw new IllegalArgumentException("Email이 올바르지 않습니다.");
        }
        this.email = email;
    }

    public Email() {

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

    @Override
    public String toString() {
        return "Email{" +
                "email='" + email + '\'' +
                '}';
    }
}
