package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

class AnswersTest {

    @DisplayName("Answers를 생성한다.")
    @Test
    void add() {
        //given & when
        Answers answers = new Answers();
        answers.add(AnswerTest.A1);

        //then
        assertThat(answers).isNotNull();
    }

    @DisplayName("모든 Answer를 삭제한다.")
    @Test
    void delete() throws CannotDeleteException {
        //given
        Answers answers = new Answers();
        answers.add(AnswerTest.A1);

        //when
        DeleteHistories actual = answers.delete(UserTest.JAVAJIGI);

        //then
        assertThat(actual.elements()).hasSize(1);
    }

    @DisplayName("다른 답변자의 답변은 삭제할 수 없다.")
    @Test
    void invalid_delete() {
        //given
        Answers answers = new Answers();
        answers.add(AnswerTest.A1);

        //when & then
        assertThatThrownBy(() -> answers.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

}
