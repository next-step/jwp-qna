package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @AfterEach
    void clear() {
        questionRepository.deleteAll();
    }

    @Test
    @DisplayName("질문을 저장한다.")
    void save() {
        Question savedQuestion = questionRepository.save(Q1);
        Question foundQuestion = questionRepository.getOne(savedQuestion.getId());

        assertThat(savedQuestion)
                .isNotNull()
                .isEqualTo(foundQuestion);
    }

    @Test
    @DisplayName("질문 Id로 조회한다.")
    void findByIdAndDeletedFalse() {
        Question savedQuestion = questionRepository.save(Q1);
        Optional<Question> foundQuestion = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId());

        assertThat(foundQuestion)
                .isNotEmpty()
                .hasValueSatisfying(question -> assertThat(question).isEqualTo(savedQuestion));
    }

    @Test
    @DisplayName("저장된 전체 질문들을 조회한다.")
    void findByDeletedFalse() {
        Question question1 = questionRepository.save(Q1);
        Question question2 = questionRepository.save(Q2);
        List<Question> questions = questionRepository.findByDeletedFalse();

        assertThat(questions)
                .containsExactly(question1, question2);
    }
}
