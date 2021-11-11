package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.NotFoundException;
import qna.UnAuthorizedException;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void deleteddeleted_기본값_false() {
        // then
        assertThat(A1.isDeleted()).isFalse();
    }

    @Test
    void deleted() {
        // given
        Answer answer = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

        // when
        answer.setDeleted(true);

        // then
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    void isOwner_질문_작성자_확인() {

        // then
        assertAll(
            () -> assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue(),
            () -> assertThat(A1.isOwner(UserTest.SANJIGI)).isFalse()
        );
    }

    @Test
    @DisplayName("toQuestion 메소드는 questionId 를 저장")
    void toQuestion() {
        // given
        QuestionTest.Q2.setId(1L);

        // when
        A2.toQuestion(QuestionTest.Q2);

        // then
        assertThat(A2.getQuestionId()).isEqualTo(QuestionTest.Q2.getId());
    }

    @Test
    void exception_create_User_null() {
        assertThatExceptionOfType(UnAuthorizedException.class) // then
            .isThrownBy(() -> {
                // when
                new Answer(null, QuestionTest.Q1, "Answers Contents1");
            });
    }

    @Test
    void exception_create_Question_null() {
        assertThatExceptionOfType(NotFoundException.class) // then
            .isThrownBy(() -> {
                // when
                new Answer(UserTest.JAVAJIGI, null, "Answers Contents1");
            });
    }
}
