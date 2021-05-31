package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question(
            "title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(
            "title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    QuestionRepository questions;

    @BeforeEach
    void setUp() {
        questions.save(Q1);
        questions.save(Q2);
    }

    @Test
    void findByDeletedFalse() {

        List<Question> actual = questions.findByDeletedFalse();
        assertAll(
                () -> assertThat(actual.size()).isEqualTo(2),
                () -> assertThat(actual.stream()
                        .filter(question -> question.isDeleted() != false)
                        .count())
                        .isEqualTo(0)
        );

    }

    @Test
    void findByIdAndDeletedFalse() {
        Optional<Question> actual = questions.findByIdAndDeletedFalse(Q1.getId());
        assertThat(actual.get()).isSameAs(Q1);
    }

}
