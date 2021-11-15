package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questions;

    @Test
    void save() {
        final Question actual = questions.save(Q1);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getContents()).isEqualTo(Q1.getContents()),
            () -> assertThat(actual.getTitle()).isEqualTo(Q1.getTitle()),
            () -> assertThat(actual.getWriterId()).isEqualTo(Q1.getWriterId())
        );
    }

    @Test
    void findByWriterId() {
        final Question question1 = questions.save(Q1);
        final Question question2 = questions.findByWriterId(UserTest.JAVAJIGI.getId());
        assertAll(
            () -> assertThat(question2.getId()).isEqualTo(question1.getId()),
            () -> assertThat(question2.getContents()).isEqualTo(question1.getContents()),
            () -> assertThat(question2.getWriterId()).isEqualTo(question1.getWriterId()),
            () -> assertThat(question2.getTitle()).isEqualTo(question1.getTitle()),
            () -> assertThat(question2).isEqualTo(question1),
            () -> assertThat(question2).isSameAs(question1)
        );
    }

    @Test
    void findByTitleLike() {
        final Question question1 = questions.save(Q1);
        final Question question2 = questions.findByTitleLike("%1");
        assertAll(
            () -> assertThat(question2.getId()).isEqualTo(question1.getId()),
            () -> assertThat(question2.getContents()).isEqualTo(question1.getContents()),
            () -> assertThat(question2.getWriterId()).isEqualTo(question1.getWriterId()),
            () -> assertThat(question2.getTitle()).isEqualTo(question1.getTitle()),
            () -> assertThat(question2).isEqualTo(question1),
            () -> assertThat(question2).isSameAs(question1)
        );
    }

    @Test
    void findByDeletedFalse() {
        final Question question1 = questions.save(Q1);
        final Question question2 = questions.save(Q2);
        List<Question> questionList = questions.findByDeletedFalse();

        assertThat(questionList).contains(question1, question2);
    }
}
