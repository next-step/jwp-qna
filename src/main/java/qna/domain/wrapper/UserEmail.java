package qna.domain.wrapper;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static java.lang.String.format;
import static qna.domain.option.LengthLimitation.MAXIMUM_USER_EMAIL_LENGTH;

@Embeddable
public class UserEmail {

    @Column(length = MAXIMUM_USER_EMAIL_LENGTH)
    private String userEmail;

    public UserEmail() { }

    public UserEmail(String userEmail) {
        if (userEmail == null) {
            return;
        }

        if (userEmail.length() > MAXIMUM_USER_EMAIL_LENGTH) {
            throw new IllegalArgumentException(format("유저 이메일의 최대 길이는 %d을 초과할 수 없습니다.",
                    MAXIMUM_USER_EMAIL_LENGTH));
        }

        this.userEmail = userEmail;
    }

    public String get() {
        return this.userEmail;
    }

}
