package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answers;

    @Test
    void save() {
        final Answer actual = answers.save(A1);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getContents()).isEqualTo(A1.getContents()),
            () -> assertThat(actual.getQuestionId()).isEqualTo(A1.getQuestionId())
        );
    }

    @Test
    void findByWriterId() {
        final Answer user1 = answers.save(A1);
        final Answer user2 = answers.findByWriterId(UserTest.JAVAJIGI.getId());
        assertAll(
            () -> assertThat(user2.getId()).isEqualTo(user1.getId()),
            () -> assertThat(user2.getContents()).isEqualTo(user1.getContents()),
            () -> assertThat(user2.getQuestionId()).isEqualTo(user1.getQuestionId()),
            () -> assertThat(user2.getWriterId()).isEqualTo(user1.getWriterId()),
            () -> assertThat(user2).isEqualTo(user1),
            () -> assertThat(user2).isSameAs(user1)
        );
    }

    @Test
    void findByQuestionId() {
        final Answer user1 = answers.save(A1);
        final Answer user2 = answers.findByQuestionId(QuestionTest.Q1.getId());
        assertAll(
            () -> assertThat(user2.getId()).isEqualTo(user1.getId()),
            () -> assertThat(user2.getContents()).isEqualTo(user1.getContents()),
            () -> assertThat(user2.getQuestionId()).isEqualTo(user1.getQuestionId()),
            () -> assertThat(user2.getWriterId()).isEqualTo(user1.getWriterId()),
            () -> assertThat(user2).isEqualTo(user1),
            () -> assertThat(user2).isSameAs(user1)
        );
    }

    @Test
    void findByIdAndDeletedFalse() {
        final Answer user1 = answers.save(A2);
        final Answer user2 = answers.findByIdAndDeletedFalse(A2.getId()).get();
        assertAll(
            () -> assertThat(user2.getId()).isEqualTo(user1.getId()),
            () -> assertThat(user2.getContents()).isEqualTo(user1.getContents()),
            () -> assertThat(user2.getQuestionId()).isEqualTo(user1.getQuestionId()),
            () -> assertThat(user2.getWriterId()).isEqualTo(user1.getWriterId()),
            () -> assertThat(user2).isEqualTo(user1),
            () -> assertThat(user2).isSameAs(user1)
        );
    }

    @Test
    void findByContentsLike() {
        final Answer user1 = answers.save(A1);
        final Answer user2 = answers.findByContentsLike("%Contents1%");
        assertAll(
            () -> assertThat(user2.getId()).isEqualTo(user1.getId()),
            () -> assertThat(user2.getContents()).isEqualTo(user1.getContents()),
            () -> assertThat(user2.getQuestionId()).isEqualTo(user1.getQuestionId()),
            () -> assertThat(user2.getWriterId()).isEqualTo(user1.getWriterId()),
            () -> assertThat(user2).isEqualTo(user1),
            () -> assertThat(user2).isSameAs(user1)
        );
    }
}
