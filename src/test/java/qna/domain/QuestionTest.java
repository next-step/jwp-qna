package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.*;

@DisplayName("질문")
public class QuestionTest {
    public static final Question QUESTION_1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question QUESTION_2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
    public static final Question QUESTION_1_ID = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI_ID);
    public static final Question QUESTION_2_ID = new Question(2L, "title2", "contents2").writeBy(UserTest.SANJIGI_ID);

    @DisplayName("질문 생성")
    @Test
    void constructor() {
        assertThatNoException().isThrownBy(() -> new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI));
    }

    @DisplayName("질문자와 답변자가 다른 경우 답을 삭제할 수 없다.")
    @Test
    void delete_fail_exist_answer() {
        QuestionTest.QUESTION_2.addAnswer(AnswerTest.ANSWER_1);
        assertThatThrownBy(() -> QuestionTest.QUESTION_2.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("로그인 사용자와 질문한 사람이 다르면 삭제할 수 없다.")
    @Test
    void delete_fail_login_user() {
        QuestionTest.QUESTION_2.addAnswer(AnswerTest.ANSWER_1);
        assertThatThrownBy(() -> QuestionTest.QUESTION_2.delete(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("질문을 삭제할 때 답변도 삭제한다.")
    @Test
    void delete_answer() throws CannotDeleteException {

        Question question = QuestionTest.QUESTION_2_ID;
        question.addAnswer(AnswerTest.ANSWER_2_ID);
        question.delete(UserTest.SANJIGI_ID);

        assertThat(question.isDeleted()).isTrue();
        for (Answer answer : question.getAnswers()) {
            assertThat(answer.isDeleted()).isTrue();
        }
    }
}
