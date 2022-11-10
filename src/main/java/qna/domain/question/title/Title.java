package qna.domain.question.title;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import qna.domain.validate.string.LengthValidator;
import qna.domain.validate.string.NullAndEmptyValidator;

@Embeddable
public class Title {

    private static final int LIMIT_LENGTH = 100;

    @Column(length = LIMIT_LENGTH, nullable = false)
    private String title;

    protected Title() {
    }

    public Title(String title) {
        NullAndEmptyValidator.getInstance().validate(title);
        LengthValidator.getInstance().validate(title, LIMIT_LENGTH);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
