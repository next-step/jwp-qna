package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.FixtureUtils.*;

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
    @Autowired
    UserRepository userRepository;

    private User JAVAJIGI;
    private Answer A1;
    private Question Q1;

    @BeforeEach
    void setup() {
        JAVAJIGI = userRepository.save(JAVAJIGI());
        Q1 = questionRepository.save(Q1(JAVAJIGI));
        A1 = answerRepository.save(A1(JAVAJIGI, Q1));

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
