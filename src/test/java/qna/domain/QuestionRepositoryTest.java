package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    Question q1, q2;

    @BeforeEach
    void setup(){
        q1 = questionRepository.save(QuestionTest.Q1);
        q2 = questionRepository.save(QuestionTest.Q2);
    }

    @Test
    void save() {
        Question expected = new Question(
                null,
                "질문 제목이에요",
                "질문 내용이에요.");
        Question actual = questionRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle())
        );
    }

    @Test
    void findByDeletedFalse() {
        List<Question> actual = questionRepository.findByDeletedFalse();
        assertThat(actual).contains(q1, q2);
    }

    @Test
    void findByIdAndDeletedFalse() {
        Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(q1.getId());
        assertThat(actual).contains(q1);
    }
}
