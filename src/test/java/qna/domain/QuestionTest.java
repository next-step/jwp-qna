package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("질문")
public class QuestionTest {
    public static final Question QUESTION_1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question QUESTION_2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("질문 생성")
    @Test
    void constructor() {
        assertThatNoException().isThrownBy(() -> new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI));
    }

    @DisplayName("질문자와 답변자가 다른 경우 답을 삭제할 수 없다.")
    @Test
    void delete_fail_exist_answer() {
        QuestionTest.QUESTION_2.addAnswer(AnswerTest.ANSWER_1);
        assertThatThrownBy(QuestionTest.QUESTION_2::delete)
                .isInstanceOf(CannotDeleteException.class);
    }
}
