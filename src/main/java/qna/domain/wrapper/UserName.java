package qna.domain.wrapper;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static java.lang.String.format;
import static qna.domain.option.LengthLimitation.MAXIMUM_USERNAME_LENGTH;
import static qna.domain.option.LengthLimitation.MINIMUM_USERNAME_LENGTH;

@Embeddable
public class UserName {

    @Column(length = MAXIMUM_USERNAME_LENGTH, nullable = false)
    private String userName;

    public UserName() { }

    public UserName(String userName) {
        if (userName == null || userName.length() < MINIMUM_USERNAME_LENGTH || userName.length() > MAXIMUM_USERNAME_LENGTH) {
            throw new IllegalArgumentException(format("유저 이름은 필수로, 길이는 %d~ %d 입니다.",
                    MINIMUM_USERNAME_LENGTH, MAXIMUM_USERNAME_LENGTH));
        }
        this.userName = userName;
    }

    public String get() {
        return this.userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserName userName1 = (UserName) o;
        return Objects.equals(userName, userName1.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }
}
