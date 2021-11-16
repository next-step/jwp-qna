package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.ForbiddenException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QuestionTest {
    private static User USER1 = new User("user1", "1234", "userName", "email");
    private static User USER2 = new User("user2", "1234", "userName", "email");

    @DisplayName("Question을 생성한다")
    @Test
    void testCreate() {
        String title = "title1";
        String contents = "contents1";
        Question question = Question.of(title, contents, UserTest.JAVAJIGI);
        assertAll(
            () -> assertThat(question.getTitle()).isEqualTo(title),
            () -> assertThat(question.getContents()).isEqualTo(contents),
            () -> assertThat(question.getUpdatedAt()).isNull()
        );
    }

    @DisplayName("Question에 작성자가 없으면 오류를 던진다")
    @Test
    void testWriteBy() {
        assertThatThrownBy(() -> Question.of("title1", "contents1", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("내 질문에 대한 답변을 등록한다")
    @Test
    void testAddAnswer() {
        Question question = Question.of("title1", "contents1", USER1);
        Answer answer = Answer.of(USER1, question, "대답");
        question.addAnswer(answer);
        assertThat(question.getAnswers().size()).isEqualTo(1);
    }

    @DisplayName("답변 작성자와 내용으로 내 질문에 대한 답변을 등록한다.")
    @Test
    void testAddAnswerWithWriterAndContents() {
        Question question = Question.of("title1", "contents1", USER1);
        question.addAnswer(USER1, "답변");
        assertThat(question.getAnswers().size()).isEqualTo(1);
    }

    @DisplayName("내 질문에 대한 답변이 아닌 것을 등록하면 오류를 던진다")
    @Test
    void testGivenAnotherAnswerThrowException() {
        Question question1 = Question.of("title1", "contents1", USER1);
        Question question2 = Question.of("title2", "contents3", USER1);
        Answer answer = Answer.of(USER1, question1, "대답");
        assertThatThrownBy(() -> question2.addAnswer(answer))
                .isInstanceOf(ForbiddenException.class);
    }

    @DisplayName("질문을 삭제한다")
    @Nested
    class DeleteQuestionTest {

        private Question question;

        @BeforeEach
        void setUp() {
            question = Question.of("title1", "contents1", USER1);
        }

        @DisplayName("질문을 삭제하면 삭제여부가 참이 된다")
        @Test
        void testDelete() throws CannotDeleteException {
            question.delete(USER1);
            assertThat(question.isDeleted()).isTrue();
        }

        @DisplayName("답변자와 동일한 사람만 삭제할 수 있다")
        @Test
        void givenOtherWriterThenThrowException() {
            assertThatThrownBy(() -> question.delete(USER2))
                    .isInstanceOf(CannotDeleteException.class);
        }

        @DisplayName("답변이 있다면 모든 답변자가 질문자와 동일해야 한다")
        @Test
        void givenAnswersAndSameWriterWhenDeleteQuestionThenDeleted() throws CannotDeleteException {
            addAnswerWithSameWriter(USER1);
            question.delete(USER1);
            assertThat(question.getAnswers()).hasSize(0);
        }

        @DisplayName("질문자와 다른 답변자가 있다면 오류를 던진다")
        @Test
        void givenAnswersWhenDeleteQuestionThenThrowException() {
            addAnswerWithDifferentWriter(USER1, USER2);
            assertThatThrownBy(() -> question.delete(USER1))
                    .isInstanceOf(CannotDeleteException.class);
        }

        @DisplayName("질문자와 다른 답변자가 있다면 삭제되지 않는다")
        @Test
        void givenAnswersWhenDeleteQuestionThenDoNotDeleted() {
            addAnswerWithDifferentWriter(USER1, USER2);
            assertThat(question.isDeleted()).isFalse();
        }

        @DisplayName("답변을 삭제하면 삭제 기록을 생성한다")
        @Test
        void whenDeleteAnswerThenCrateDeleteHistory() throws CannotDeleteException {
            addAnswerWithSameWriter(USER1);
            List<DeleteHistory> deleteHistories = question.delete(USER1);
            assertThat(deleteHistories).hasSize(4);
            assertThat(deleteHistories).contains(
                    DeleteHistory.of(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()));
        }

        private void addAnswerWithSameWriter(User user) {
            question.addAnswer(user, "content1");
            question.addAnswer(user, "content2");
            question.addAnswer(user, "content3");
        }

        private void addAnswerWithDifferentWriter(User user1, User user2) {
            question.addAnswer(user1, "content1");
            question.addAnswer(user1, "content2");
            question.addAnswer(user2, "content3");
        }
    }
}
