package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    QuestionRepository questionRepository;

    private Answer A1;
    private Question Q1;

    @BeforeEach
    void setup() {
        A1 = answerRepository.save(new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1"));
        Q1 = questionRepository.save(new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI));
        A1.toQuestion(Q1);
    }

    @Test
    void createdAt_updatedAt_반영_확인() {
        assertAll(
            () -> assertThat(A1.getCreatedAt()).isNotNull(),
            () -> assertThat(A1.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    void findByIdAndDeletedFalse() {
        Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(A1.getId());
        assertThat(actual).isEqualTo(Optional.of(A1));
    }
}
