package qna.domain;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Email implements Serializable {

    private static final Pattern emailPattern = Pattern.compile(
        "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");

    @Column(name = "email", length = 50)
    private String email;

    protected Email() {
    }

    public Email(String email) {
        validEmail(email);
        this.email = email;
    }

    public void validEmail(String email) {
        Matcher matcher = emailPattern.matcher(email);
        if (!matcher.matches()) {
            throw new RuntimeException("이메일 형식이 아닙니다.");
        }
    }

    public String getEmail() {
        return email;
    }
}
