package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import qna.validators.StringValidator;

@Embeddable
public class Title {

    private static final int TITLE_LENGTH = 100;

    @Column(length = TITLE_LENGTH, nullable = false)
    private String title;

    protected Title() {
    }

    public Title(String title) {
        StringValidator.validate(title, TITLE_LENGTH);
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public void edit(String title) {
        StringValidator.validate(title, TITLE_LENGTH);
        this.title = title;
    }

}
