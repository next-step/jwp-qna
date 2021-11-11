package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.NotFoundException;
import qna.UnAuthorizedException;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private QuestionRepository questions;

    @Test
    void save() {

        // when
        Answer actual = answers.save(A1);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId()),
            () -> assertThat(actual.getQuestionId()).isEqualTo(QuestionTest.Q1.getId())
        );
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

    @Test
    void deleted() {

        // when
        Answer actual = answers.save(A1);
        actual.setDeleted(true);
        answers.flush();

        // then
        Optional<Answer> expected = answers.findById(actual.getId());
        assertThat(expected.get().isDeleted()).isTrue();
    }

    @Test
    void deleted_기본값_false() {
        // when
        Answer actual = answers.save(A1);

        // then
        assertThat(actual.isDeleted()).isFalse();
    }

    @Test
    void isOwner_질문_작성자_확인() {
        // when
        Answer actual = answers.save(A1);

        // then
        assertAll(
            () -> assertThat(actual.isOwner(UserTest.JAVAJIGI)).isTrue(),
            () -> assertThat(actual.isOwner(UserTest.SANJIGI)).isFalse()
        );
    }

    @Test
    @DisplayName("toQuestion 은 questionId(질문) 을 지정합니다. ")
    void toQuestion() {
        // when
        Question question = questions.save(new Question("제목", "컨텐츠").writeBy(UserTest.JAVAJIGI));

        // then
        A2.toQuestion(question);
        Answer actual = answers.save(A2);

        assertAll(
            () -> assertThat(actual.getQuestionId()).isNotNull(),
            () -> assertThat(actual.getQuestionId()).isEqualTo(question.getId())
        );
    }
}
