package qna.domain.wrappers;

import qna.util.ValidationUtil;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserName {

    private static final int USER_NAME_MAX_LENGTH = 20;

    @Column(nullable = false, length = 20)
    private String name;

    protected UserName() {
    }

    public UserName(String name) {
        ValidationUtil.checkValidNullOrEmpty(name);
        ValidationUtil.checkValidTitleLength(name, USER_NAME_MAX_LENGTH);
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserName userName = (UserName) o;
        return Objects.equals(name, userName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
