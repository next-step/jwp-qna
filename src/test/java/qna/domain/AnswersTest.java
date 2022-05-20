package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

class AnswersTest {

    @Test
    @DisplayName("createEmpty() 를 통해 빈 답변 일급 컬랙션을 만들 수 있다.")
    void test01() {
        // given & when
        Answers answers = Answers.createEmpty();

        // then
        assertNotNull(answers);
    }

    @Test
    @DisplayName("add() 를 통해 답변을 일급 컬랙션에 추가할 수 있다.")
    void test02() {
        // given & when
        Answers answers = Answers.createEmpty();
        answers.add(AnswerTest.A1);
        answers.add(AnswerTest.A2);

        // then
        assertThat(answers.findReadOnlyElements()).contains(AnswerTest.A1, AnswerTest.A2);
    }

    @Test
    @DisplayName("deleteAll() 를 통해 모든 답변을 제거하고 그 결과로 DeleteHistory 를 반환받는다.")
    void test03() throws CannotDeleteException {
        // given & when
        Answers answers = Answers.createEmpty();
        answers.add(AnswerTest.A1);
        answers.add(AnswerTest.A1);

        // when
        DeleteHistories deleteHistories = answers.deleteAll(UserTest.JAVAJIGI);

        // then
        assertAll(
            () -> assertThat(deleteHistories.findReadOnlyElements()).hasSize(2)
        );
    }
}