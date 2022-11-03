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
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void init() {
        questionRepository.deleteAll();
    }

    @Test
    @DisplayName("질문을 저장할 수 있어야 한다.")
    void save() {
        Question question = Q1;
        Question savedQuestion = questionRepository.save(question);
        assertThat(savedQuestion.getId()).isNotNull();

        assertAll(
                () -> assertThat(savedQuestion.getTitle()).isEqualTo(question.getTitle()),
                () -> assertThat(savedQuestion.getContents()).isEqualTo(question.getContents()),
                () -> assertThat(savedQuestion.getWriterId()).isEqualTo(question.getWriterId()),
                () -> assertThat(savedQuestion.getCreatedAt()).isNotNull());
    }

    @Test
    @DisplayName("삭제상태가 아닌 Question을 가져올 수 있어야 한다.")
    void get_all_deleted_false() {
        questionRepository.saveAll(Arrays.asList(Q1, Q2));

        List<Question> questions = questionRepository.findByDeletedFalse();

        assertThat(questions.size()).isEqualTo(Arrays.asList(Q1, Q2).size());
    }

    @Test
    @DisplayName("QuestionId로 삭제 상태가 아닌 Question을 가져올 수 있어야 한다.")
    void get_question_by_id() {
        questionRepository.save(Q1);

        Optional<Question> question = questionRepository.findByIdAndDeletedFalse(Q1.getId());

        assertThat(question).contains(Q1);
    }

    @Test
    @DisplayName("삭제된 Question은 조회되지 않는다.")
    void do_not_get_deleted_question() {
        List<Question> savedQuestions = questionRepository.saveAll(Arrays.asList(Q1, Q2));
        questionRepository.deleteById(savedQuestions.get(1).getId());

        List<Question> restQuestions = questionRepository.findByDeletedFalse();

        assertThat(savedQuestions.size()-1).isEqualTo(restQuestions.size());
    }
}
