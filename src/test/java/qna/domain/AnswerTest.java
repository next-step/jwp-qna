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
    private static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

    @DisplayName("Answer를 객체 생성한다")
    @Test
    void testCreate() {
        String contents = "Answers Contents1";
        Answer answer = Answer.of(UserTest.JAVAJIGI, Q1, contents);
        assertAll(
                () -> assertThat(answer.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId()),
                () -> assertThat(answer.getQuestion()).isEqualTo(Q1),
                () -> assertThat(answer.getContents()).isEqualTo(contents),
                () -> assertThat(answer.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS)),
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
}
