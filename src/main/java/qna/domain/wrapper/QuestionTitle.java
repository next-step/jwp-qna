package qna.domain.wrapper;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static java.lang.String.format;
import static qna.domain.option.LengthLimitation.MAXIMUM_QUESTION_TITLE_LENGTH;
import static qna.domain.option.LengthLimitation.MINIMUM_QUESTION_TITLE_LENGTH;

@Embeddable
public class QuestionTitle {

    @Column(length = MAXIMUM_QUESTION_TITLE_LENGTH, nullable = false)
    private String title;

    public QuestionTitle() { }

    public QuestionTitle(String title) {
        if (title == null || title.length() < MINIMUM_QUESTION_TITLE_LENGTH || title.length() > MAXIMUM_QUESTION_TITLE_LENGTH) {
            throw new IllegalArgumentException(format("질문 제목은 필수로, 길이는 %d~ %d 입니다.",
                    MINIMUM_QUESTION_TITLE_LENGTH, MAXIMUM_QUESTION_TITLE_LENGTH));
        }
        this.title = title;
    }

    public String get() {
        return this.title;
    }

}
