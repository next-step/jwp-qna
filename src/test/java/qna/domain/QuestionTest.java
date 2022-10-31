package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void Question_객체에_Writer_등록_시_Writer가_비어있으면_에러_발생() {
        Question question = new Question("title3", "Writer가 비어있음");
        assertThatThrownBy(() -> question.writeBy(null))
                .isInstanceOf(UnAuthorizedException.class);
    }
}
