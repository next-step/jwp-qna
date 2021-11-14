package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import qna.util.ValidationUtils;

@Embeddable
public class Title {
    public static final int TITLE_MAX_SIZE = 100;
    
    @Column(nullable = false, length = TITLE_MAX_SIZE)
    private String title;
    
    protected Title() {
    }

    private Title(String title) {
        checkValidation(title);
        this.title = title;
    }

    public static Title of(String title) {
        return new Title(title);
    }
    
    private static void checkValidation(String title) {
        ValidationUtils.checkEmpty(title);
        ValidationUtils.checkLength(title, TITLE_MAX_SIZE);
    }
}
