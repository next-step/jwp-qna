package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class AnswersTest {
    @DisplayName("Answers 일급 컬렉션 생성")
    @Test
    void test_new() {
        //given & when
        Answers answers = Answers.empty();
        //then
        assertThat(answers).isNotNull();
    }

    @DisplayName("Answers 일급 컬렉션에 Answer 추가")
    @Test
    void test_add_answer() {
        //given
        Answers answers = Answers.empty();
        //when
        answers.addAnswer(AnswerTest.A1);
        answers.addAnswer(AnswerTest.A2);
        //then
        assertAll(
                () -> assertThat(answers.getAnswers()).hasSize(2),
                () -> assertThat(answers.getAnswers()).contains(AnswerTest.A1, AnswerTest.A2)
        );
    }

    @DisplayName("Answers 내 모든 답변 삭제")
    @Test
    void test_delete_all() throws CannotDeleteException {
        //given
        Answers answers = Answers.from(Arrays.asList(AnswerTest.A1, AnswerTest.A1));
        //when
        answers.deleteAll(UserTest.JAVAJIGI);
        //then
        assertAll(
                () -> assertThat(answers.getAnswers()).hasSize(2),
                () -> assertThat(answers.getAnswers().get(0).isDeleted()).isTrue(),
                () -> assertThat(answers.getAnswers().get(1).isDeleted()).isTrue()
        );
    }

    @DisplayName("Answers 내 답변 삭제 시 로그인 사용자와 작성자(writer)가 동일하지 않은 경우 오류")
    @Test
    void test_delete_not_equals_writer() {
        //given
        Answers answers = Answers.from(Arrays.asList(AnswerTest.A1, AnswerTest.A2));
        //when & then
        assertThatThrownBy(() -> answers.deleteAll(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}