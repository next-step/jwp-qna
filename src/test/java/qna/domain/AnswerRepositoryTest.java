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
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("저장을 하고, 다시 가져왔을 때 원본 객체와 같아야 한다")
    public void 저장을_하고_다시_가져왔을_때_원본_객체와_같아야_한다() {
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);
        Answer foundAnswer = answerRepository.findById(savedAnswer.getId()).orElseThrow(EntityNotFoundException::new);

        assertSame(savedAnswer, foundAnswer);
    }

    @Test
    @DisplayName("삭제가 되어있으면, findByQuestionIdAndDeletedFalse는 찾지 못한다")
    public void 삭제가_되어있으면_findByQuestionIdAndDeletedFalse는_찾지_못한다() {
        Question question = new Question(1L, "Title", "Contents");
        Answer deletedAnswer = new Answer(UserTest.JAVAJIGI, question, "contents");
        Answer notDeletedAnswer = new Answer(UserTest.JAVAJIGI, question, "contents");

        deletedAnswer.setDeleted(true);

        questionRepository.save(question);
        answerRepository.saveAll(Arrays.asList(deletedAnswer, notDeletedAnswer));

        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(deletedAnswer.getId()))
                .containsExactly(notDeletedAnswer);
    }

    @Test
    @DisplayName("삭제가 되어있으면, findByIdAndDeletedFalse는 찾지 못한다")
    public void 삭제가_되어있으면_findByIdAndDeletedFalse는_찾지_못한다() {
        Answer deleteTestAnswer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");

        Answer savedAnswer = answerRepository.save(deleteTestAnswer);

        assertThat(answerRepository.findByIdAndDeletedFalse(savedAnswer.getId()).isPresent())
                .isTrue();

        // 삭제처리
        savedAnswer.setDeleted(true);
        assertThat(answerRepository.findByIdAndDeletedFalse(savedAnswer.getId()).isPresent())
                .isFalse();
    }
}
