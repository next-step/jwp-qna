package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Answers 테스트")
class AnswersTest {

    private Answers answers;

    @BeforeEach
    void setUp() {
        answers = new Answers();
        answers.add(AnswerTest.A1);
        answers.add(AnswerTest.A2);
    }

    @Test
    @DisplayName("질문자와 답변자가 다른 답변이 존재하면 예외가 발생한다.")
    void validateAnswers() {
        // when &  then
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> answers.validateAnswers(UserTest.JAVAJIGI))
                .withMessageMatching(ErrorMessage.EXISTS_ANSWER_OF_OTHER.getMessage());
    }

    @Test
    @DisplayName("모든 질문들을 삭제한다.")
    void deleteAnswers() {
        // when
        answers.deleteAnswers();

        // then
        assertAll(
                () -> assertThat(AnswerTest.A1.isDeleted()).isTrue(),
                () -> assertThat(AnswerTest.A2.isDeleted()).isTrue()
        );
    }

    @Test
    @DisplayName("모든 질문들에 대한 삭제 이력을 리턴한다.")
    void getDeleteHistories() {
        // when
        List<DeleteHistory> deleteHistories = answers.getDeleteHistories();

        // then
        assertThat(deleteHistories).containsExactly(
                new DeleteHistory(ContentType.ANSWER, AnswerTest.A1.getId(), AnswerTest.A1.getWriter()),
                new DeleteHistory(ContentType.ANSWER, AnswerTest.A2.getId(), AnswerTest.A2.getWriter())
        );
    }
}
