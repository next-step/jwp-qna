package qna.domain;

import org.junit.jupiter.api.BeforeEach;
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

    private Question question;
    private Answer answer;
    private Answer deletedAnswer;

    @BeforeEach
    public void setUp() {
        question = questionRepository.save(new Question("title", "contents"));
        deletedAnswer = new Answer(UserTest.JAVAJIGI, question, "contents");
        answer = new Answer(UserTest.JAVAJIGI, question, "contents");

        deletedAnswer.setDeleted(true);

        answerRepository.saveAll(Arrays.asList(deletedAnswer, answer));
    }

    @Test
    @DisplayName("저장을 하고, 다시 가져왔을 때 원본 객체와 같아야 한다")
    public void 저장을_하고_다시_가져왔을_때_원본_객체와_같아야_한다() {
        Answer foundAnswer = answerRepository.findById(answer.getId()).orElseThrow(EntityNotFoundException::new);

        assertSame(answer, foundAnswer);
    }

    @Test
    @DisplayName("삭제가 되어있으면, findByQuestionIdAndDeletedFalse는 찾지 못한다")
    public void 삭제가_되어있으면_findByQuestionIdAndDeletedFalse는_찾지_못한다() {
        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(question.getId()))
                .containsExactly(answer);
    }

    @Test
    @DisplayName("삭제가 되어있으면, findByIdAndDeletedFalse는 찾지 못한다")
    public void 삭제가_되어있으면_findByIdAndDeletedFalse는_찾지_못한다() {
        assertThat(answerRepository.findByIdAndDeletedFalse(deletedAnswer.getId()))
                .isNotPresent();
        assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId()))
                .isPresent();
    }
}
