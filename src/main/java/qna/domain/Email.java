package qna.domain;

import qna.exception.InvalidEmailException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static java.util.regex.Pattern.matches;

@Embeddable
public class Email {

    @Column(name = "email", length = 50)
    private String email;

    protected Email() {/*empty*/}

    protected Email(String email) {
        checkAddress(email);

        this.email = email;
    }

    public static Email of(String address) {
        return new Email(address);
    }

    private static void checkAddress(String address) {
        if(!matches("[\\w~\\-.+]+@[\\w~\\-]+(\\.[\\w~\\-]+)+", address))
            throw new InvalidEmailException("잘못된 이메일 형식입니다.");
    }
}
