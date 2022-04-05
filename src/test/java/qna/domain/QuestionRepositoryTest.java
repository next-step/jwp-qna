package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setting() {
        userRepository.saveAll(Arrays.asList(UserTest.JAVAJIGI, UserTest.SANJIGI, UserTest.TESTUSER));
    }

    @Test
    void save() {
        final Question actual = questionRepository.save(QuestionTest.Q3);
        assertThat(actual.getId()).isEqualTo(QuestionTest.Q3.getId());
    }

    @Test
    @DisplayName("삭제되지 않은 질문들을 찾는다")
    void findByDeletedFalse() {
        questionRepository.saveAll(Arrays.asList(QuestionTest.Q1, QuestionTest.Q2));
        List<Question> findQuestions = questionRepository.findByDeletedFalse();

        assertThat(findQuestions.size()).isEqualTo(2);
        for (Question question : findQuestions) {
            assertThat(question.isDeleted()).isFalse();
        }
    }

    @Test
    @DisplayName("삭제되지 않은 질문들을 id 를 통해서 찾는다")
    void findByIdAndDeletedFalse() {
        List<Question> actual = questionRepository.saveAll(Arrays.asList(QuestionTest.Q4));
        actual.get(0).setDeleted(true);

        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(QuestionTest.Q4.getId());
        assertThat(findQuestion.isPresent()).isFalse();
    }
}