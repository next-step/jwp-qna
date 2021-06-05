package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import qna.util.StringValidateUtils;

@Embeddable
public class Title {

    public static final int LENGTH = 100;

    @Column(name = "title", nullable = false, length = LENGTH)
    private String value;

    protected Title() {
    }

    public Title(final String value) {
        this.value = StringValidateUtils.validate(value, LENGTH);
    }

    public String value() {
        return value;
    }
}
