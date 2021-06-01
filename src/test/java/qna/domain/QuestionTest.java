package qna.domain;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

public class QuestionTest {
    public static final Question Q1 = new Question(1L, "title1", "contents1").writeBy(JAVAJIGI);
    public static final Question Q2 = new Question(2L, "title2", "contents2").writeBy(SANJIGI);

    private DateTimeStrategy dateTimeStrategy;

    @BeforeEach
    void setUp() {
        dateTimeStrategy = () -> LocalDateTime.of(2021, 6, 1, 0, 0, 0);
    }

    @DisplayName("writeBy로 질문자 설정 가능")
    @Test
    void writeByTest() {
        Question question = new Question("title", "contents").writeBy(JAVAJIGI);
        assertThat(question.getWriter()).isEqualTo(JAVAJIGI);
        assertThat(question.getWriter()).isNotEqualTo(SANJIGI);
    }

    @DisplayName("질문자 동일여부 테스트")
    @Test
    void isOwnerTest() {
        assertTrue(Q1.isOwner(JAVAJIGI));
        assertTrue(Q2.isOwner(SANJIGI));
        assertFalse(Q1.isOwner(SANJIGI));
        assertFalse(Q2.isOwner(JAVAJIGI));
    }

    @DisplayName("질문에 답변 추가 테스트")
    @Test
    void addAnswerTest() {

        Answer answer1 = new Answer(1L, JAVAJIGI, Q1, "contents");
        Answer answer2 = new Answer(2L, JAVAJIGI, Q1, "contents");
        Answer answer3 = new Answer(3L, JAVAJIGI, Q1, "contents");

        Q1.addAnswer(answer1);
        Q1.addAnswer(answer2);
        Q1.addAnswer(answer3);
        Q1.addAnswer(answer3); // duplicate test

        assertThat(Q1.getAnswers()).hasSize(3)
                                   .contains(answer1, answer2, answer3);

        assertThat(answer1.getQuestion()).isEqualTo(Q1);
        assertThat(answer2.getQuestion()).isEqualTo(Q1);
        assertThat(answer3.getQuestion()).isEqualTo(Q1);
    }

    @DisplayName("질문 목록에서 답변 삭제 테스트")
    @Test
    void deleteAnswerTest() {

        Answer answer1 = new Answer(1L, JAVAJIGI, Q1, "contents");
        Answer answer2 = new Answer(2L, JAVAJIGI, Q1, "contents");

        Q1.addAnswer(answer1);
        Q1.addAnswer(answer2);
        Q1.deleteAnswer(answer2);

        assertThat(Q1.getAnswers()).hasSize(1)
                                   .contains(answer1)
                                   .doesNotContain(answer2);
    }

    @DisplayName("로그인 사용자와 질문한 사람이 다르면 질문 삭제 불가")
    @Test
    void deleteFailTest01() {
        assertThatExceptionOfType(CannotDeleteException.class).isThrownBy(() -> Q1.delete(SANJIGI, dateTimeStrategy));
    }

    @DisplayName("답변자 중 질문자와 다른 사람이 1명이라도 존재하면 질문 삭제 불가")
    @Test
    void deleteFailTest02() {
        Q1.addAnswer(new Answer(1L, JAVAJIGI, Q1, "answer1"));
        Q1.addAnswer(new Answer(2L, SANJIGI, Q1, "answer2"));

        assertThatExceptionOfType(CannotDeleteException.class).isThrownBy(() -> Q1.delete(JAVAJIGI, dateTimeStrategy));
    }

    @DisplayName("이미 삭제된 질문을 삭제하려고 시도하면 예외 발생")
    @Test
    void deleteFailTest03() throws CannotDeleteException {

        Question question = new Question(3L, "title", "contents").writeBy(JAVAJIGI);
        question.delete(JAVAJIGI, dateTimeStrategy);

        assertThatExceptionOfType(CannotDeleteException.class).isThrownBy(() -> question.delete(JAVAJIGI, dateTimeStrategy));
    }

    @DisplayName("질문 삭제 성공 시 질문과 답변 모두 삭제")
    @Test
    void deleteSuccess() throws CannotDeleteException {
        Answer answer1 = new Answer(1L, JAVAJIGI, Q1, "answer1");
        Answer answer2 = new Answer(2L, JAVAJIGI, Q1, "answer2");

        Q1.addAnswer(answer1);
        Q1.addAnswer(answer2);

        Q1.delete(JAVAJIGI, dateTimeStrategy);
        assertThat(Q1.getAnswers()).hasSize(0);
        assertThat(Q1.isDeleted()).isTrue();
        assertThat(answer1.isDeleted()).isTrue();
        assertThat(answer2.isDeleted()).isTrue();
    }
}
