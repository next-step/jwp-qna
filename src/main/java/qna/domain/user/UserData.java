package qna.domain.user;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import qna.common.exception.InvalidParamException;

@Embeddable
public class UserData {

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Embedded
    private Email email;

    protected UserData() {
    }

    public UserData(String name, Email email) {
        this.name = name;
        this.email = email;
        valid();
    }

    public void update(UserData userData) {
        this.name = userData.getName();
        this.email = userData.getEmail();
    }

    public boolean equalsNameAndEmail(UserData targetUserData) {
        if (Objects.isNull(targetUserData)) {
            return false;
        }

        return name.equals(targetUserData.getName()) && email.equals(targetUserData.getEmail());
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }


    private void valid() {
        if (Objects.isNull(name) || Objects.isNull(email)) {
            throw new InvalidParamException("모든 값이 존재해야 합니다.");
        }
    }

    @Override
    public String toString() {
        return "UserData{" +
            ", name='" + name + '\'' +
            ", email=" + email.getEmail() +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserData userData = (UserData) o;
        return Objects.equals(name, userData.name)
            && Objects.equals(email, userData.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }

}
