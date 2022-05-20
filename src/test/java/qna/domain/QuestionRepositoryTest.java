package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void Question_저장() {
        Question question = QuestionTest.Q1;
        Question result = questionRepository.save(QuestionTest.Q1);

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getTitle()).isEqualTo(question.getTitle())
        );
    }

    @Test
    void Question_단건_조회() {
        Question question = questionRepository.save(QuestionTest.Q1);

        assertThat(questionRepository.findById(question.getId()).get())
                .usingRecursiveComparison()
                .isEqualTo(question);
    }

    @Test
    void Question_전체_조회() {
        questionRepository.save(QuestionTest.Q1);
        questionRepository.save(QuestionTest.Q2);

        assertThat(questionRepository.findAll()).hasSize(2);
    }

    @Test
    void Question_삭제여부_컬럼이_false인_단건_조회() {
        Question question = questionRepository.save(QuestionTest.Q1);

        assertThat(questionRepository.findByIdAndDeletedFalse(question.getId()).get()).isEqualTo(question);

        Question findQuestion = questionRepository.findById(question.getId()).get();
        findQuestion.setDeleted(true);

        assertThat(questionRepository.findByIdAndDeletedFalse(question.getId()).isPresent()).isFalse();
    }

    @Test
    void Question_삭제여부_컬럼이_false인_전체_조회() {
        Question question1 = questionRepository.save(QuestionTest.Q1);
        questionRepository.save(QuestionTest.Q2);

        assertThat(questionRepository.findByDeletedFalse()).hasSize(2);

        Question findQuestion = questionRepository.findById(question1.getId()).get();
        findQuestion.setDeleted(true);

        assertThat(questionRepository.findByDeletedFalse()).hasSize(1);
    }

}