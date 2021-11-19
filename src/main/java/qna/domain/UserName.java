package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserName {
    public static final String MAX_USER_NAME_EXCEPTION_MESSAGE = "userName 최대입력 길이를 초과하였습니다.";
    public static final int MAX_USER_NAME_LENGTH = 20;

    @Column(nullable = false, length = MAX_USER_NAME_LENGTH)
    private String name;

    protected UserName() {
    }

    public UserName(String name) {
        validateUserNameLength(name);
        this.name = name;
    }

    private void validateUserNameLength(String name) {
        if (name.length() > MAX_USER_NAME_LENGTH) {
            throw new IllegalArgumentException(MAX_USER_NAME_EXCEPTION_MESSAGE);
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserName otherUserName = (UserName) o;
        return Objects.equals(name, otherUserName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
