package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

    @Test
    void 질문의_작성자가_없으면_예외를_발생시킨다() {
        //given
        Question question = new Question("title3", "Writer가 비어있음");

        //when
        assertThatThrownBy(() -> question.writeBy(null))
                .isInstanceOf(UnAuthorizedException.class);
    }
}
