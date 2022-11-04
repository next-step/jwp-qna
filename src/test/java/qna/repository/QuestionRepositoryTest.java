package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        questionRepository.deleteAll();
    }

    @Test
    @DisplayName("삭제상태가 false인 Question목록을 반환")
    void test_returns_questions_with_deleted_is_false() {
        questionRepository.saveAll(Arrays.asList(Q1, Q2));

        List<Question> questions = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertThat(questions.size()).isEqualTo(2),
                () -> assertThat(questions).containsExactly(Q1, Q2)
        );
    }

    @Test
    @DisplayName("questionId에 해당하고 삭제상태가 false인 Question을 반환")
    void test_returns_question_deleted_is_false() {
        Question savedQuestion = questionRepository.save(Q2);

        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId());

        assertThat(findQuestion).contains(savedQuestion);
    }
}

