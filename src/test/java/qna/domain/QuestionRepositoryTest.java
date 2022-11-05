package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    private Question Q1;

    @BeforeEach
    void setup() {
        Q1 = questionRepository.save(new Question(1L, "title1", "contents1"));
    }

    @Test
    void createdAt_updatedAt_반영_확인() {
        assertAll(
            () -> assertThat(Q1.getCreatedAt()).isNotNull(),
            () -> assertThat(Q1.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    void findByDeletedFalse() {
        List<Question> actual = questionRepository.findByDeletedFalse();
        actual.forEach(question -> assertThat(question.isDeleted()).isFalse());
    }

    @Test
    void findByIdAndDeletedFalse() {
        Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(Q1.getId());
        assertThat(actual).isEqualTo(Optional.of(Q1));
    }
}
