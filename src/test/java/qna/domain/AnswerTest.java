package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnswerTest {
    private static final Question Q1 = Question.of("title1", "contents1", UserTest.JAVAJIGI);

    @DisplayName("Answer를 객체 생성한다")
    @Test
    void testCreate() {
        String contents = "Answers Contents1";
        Answer answer = Answer.of(UserTest.JAVAJIGI, Q1, contents);
        assertAll(
                () -> assertThat(answer.getWriter()).isEqualTo(UserTest.JAVAJIGI),
                () -> assertThat(answer.getQuestion()).isEqualTo(Q1),
                () -> assertThat(answer.getContents()).isEqualTo(contents),
                () -> assertThat(answer.getUpdatedAt()).isNull()
        );
    }

    @DisplayName("Write가 없으면 UnAuthorizedException을 던진다")
    @Test
    void testGivenNoWriteThenThrowException() {
        assertThatThrownBy(() -> Answer.of(null, Q1, "Answers Contents1"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("Question이 없으면 UnAuthorizedException을 던진다")
    @Test
    void testGivenNoQuestionThenThrowException() {
        assertThatThrownBy(() -> Answer.of(UserTest.JAVAJIGI, null, "Answers Contents1"))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("같은 질문인지 여부를 반환한다")
    @Test
    void hasSameQuestion() {
        Answer answer = Answer.of(UserTest.JAVAJIGI, Q1, "Answers Contents1");
        assertThat(answer.hasSameQuestion(Q1)).isTrue();
    }

    @DisplayName("답변을 삭제한다")
    @Nested
    class TestDelete {
        private Answer answer;

        @BeforeEach
        void setUp() {
            answer = Answer.of(UserTest.JAVAJIGI, Q1, "Answers Contents1");
        }

        @DisplayName("삭제여부가 참이 된다")
        @Test
        void testDelete() throws CannotDeleteException {
            answer.delete(UserTest.JAVAJIGI);
            assertThat(answer.isDeleted()).isTrue();
        }

        @DisplayName("질문에 포함된 답변이 삭제된다")
        @Test
        void testRemoveAnswerInQuestion() throws CannotDeleteException {
            answer.delete(UserTest.JAVAJIGI);
            Answers answers = Q1.getAnswers();
            assertThat(answers.contains(answer)).isFalse();
        }

        @DisplayName("답변자와 동일한 사람만 삭제할 수 있다")
        @Test
        void givenOtherWriterThenThrowException() {
            assertThatThrownBy(() -> answer.delete(UserTest.SANJIGI))
                    .isInstanceOf(CannotDeleteException.class);
        }

        @DisplayName("답변을 삭제하면 삭제 기록을 생성한다")
        @Test
        void whenDeleteAnswerThenCrateDeleteHistory() throws CannotDeleteException {
            answer.delete(UserTest.JAVAJIGI);
            AnswerDeleteHistory deleteHistory = answer.getAnswerDeleteHistory();
            assertAll(
                    () -> assertThat(deleteHistory.getAnswer()).isEqualTo(answer),
                    () -> assertThat(deleteHistory.getDeletedBy()).isEqualTo(answer.getWriter())
            );
        }
    }
}
