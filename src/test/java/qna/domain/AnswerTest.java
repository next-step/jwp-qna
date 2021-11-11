package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnswerTest {
    @DisplayName("Answer를 객체 생성한다")
    @Test
    void testCreate() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        assertAll(
                () -> assertThat(answer.getWriterId()).isEqualTo(answer.getWriterId()),
                () -> assertThat(answer.getQuestionId()).isEqualTo(answer.getQuestionId()),
                () -> assertThat(answer.getContents()).isEqualTo(answer.getContents()),
                () -> assertThat(answer.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS)),
                () -> assertThat(answer.getUpdatedAt()).isNull()
        );
    }

    @DisplayName("Write가 없으면 UnAuthorizedException을 던진다")
    @Test
    void testGivenNoWriteThenThrowException() {
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "Answers Contents1"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("Question이 없으면 UnAuthorizedException을 던진다")
    @Test
    void testGivenNoQuestionThenThrowException() {
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "Answers Contents1"))
                .isInstanceOf(NotFoundException.class);
    }
}
