package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityNotFoundException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("저장을 하고, 다시 가져왔을 때 원본 객체와 같아야 한다")
    public void 저장을_하고_다시_가져왔을_때_원본_객체와_같아야_한다() {
        Question savedQuestion = questionRepository.save(QuestionTest.Q1);
        Question foundQuestion = questionRepository.findById(savedQuestion.getId()).orElseThrow(EntityNotFoundException::new);

        assertSame(savedQuestion, foundQuestion);
    }

    @Test
    @DisplayName("삭제가 되어있으면, findByIdAndDeletedFalse는 찾지 못한다")
    public void 삭제를_하지_않으면_findByIdAndDeletedFalse는_찾지_못한다() {
        Question deleteTestQuestion = new Question("Hello", "Hello");

        Question savedQuestion = questionRepository.save(deleteTestQuestion);

        assertThat(questionRepository.findByIdAndDeletedFalse(savedQuestion.getId()).isPresent())
                .isTrue();

        // 삭제처리
        savedQuestion.setDeleted(true);
        assertThat(questionRepository.findByIdAndDeletedFalse(savedQuestion.getId()).isPresent())
                .isFalse();
    }

    @Test
    @DisplayName("삭제가 되어있으면, findByDeletedFalse에는 포함이 되면 안된다")
    public void 삭제가_되어있으면_findByDeletedFalse에는_포함이_되면_안된다() {
        Question deletedQuestion = new Question("A1", "A1");
        deletedQuestion.setDeleted(true);

        questionRepository.saveAll(
                Arrays.asList(
                        QuestionTest.Q1,
                        QuestionTest.Q2,
                        deletedQuestion
                )
        );

        assertThat(questionRepository.findByDeletedFalse())
                .containsExactlyInAnyOrder(QuestionTest.Q1, QuestionTest.Q2);
    }
}
