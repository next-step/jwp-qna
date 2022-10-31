package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        questionRepository.deleteAllInBatch();
    }

    @Test
    void 질문_저장_테스트() {
        Question question = questionRepository.save(Q1);
        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(question.getTitle()).isEqualTo(Q1.getTitle()),
                () -> assertThat(question.isDeleted()).isFalse()
        );
    }

    @Test
    void 질문_저장_후_삭제되지_않은_질문들_조회_테스트() {
        Question saveQuestion1 = questionRepository.save(Q1);
        Question saveQuestion2 = questionRepository.save(Q2);
        List<Question> findQuestion = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertThat(findQuestion.size()).isEqualTo(2),
                () -> assertThat(findQuestion).contains(saveQuestion1),
                () -> assertThat(findQuestion).contains(saveQuestion2)
        );
    }

    @Test
    void 질문_저장_후_삭제여부_true로_업데이트하여_조회_안됨_테스트() {
        Question saveQuestion = questionRepository.save(Q1);
        Long saveQuestionId = saveQuestion.getId();

        saveQuestion.setDeleted(true);
        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestionId);

        assertThat(findQuestion.isPresent()).isFalse();
    }

    @Test
    void 질문_조회_후_삭제하면_더_이상_조회가_되지_않음_테스트() {
        Question saveQuestion = questionRepository.save(Q2);
        Long saveQuestionId = saveQuestion.getId();
        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestionId);

        assertThat(findQuestion.isPresent()).isTrue();
        findQuestion.ifPresent(question -> questionRepository.delete(question));
        Optional<Question> deletedQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestionId);

        assertThat(deletedQuestion.isPresent()).isFalse();
    }
}
