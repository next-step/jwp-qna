package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    QuestionRepository questionRepository;
    @Test
    @DisplayName("save 테스트")
    public void save() {
        assertThat(AnswerTest.A1.getId()).isNull();
        Answer a1 = answerRepository.save(AnswerTest.A1);
        assertThat(a1).isSameAs(AnswerTest.A1);
        assertThat(a1.getId()).isNotNull();
    }

    @Test
    @DisplayName("1차 캐시로 조회로 인한 sql 체크")
    public void findByName() {
        Answer a1 = answerRepository.save(AnswerTest.A1);
        assertThat(a1).isSameAs(AnswerTest.A1);
        assertThat(a1.getId()).isNotNull();
        Answer a2 = answerRepository.findById(a1.getId()).get();
        assertThat(a1).isSameAs(a2);
    }

    @Test
    @DisplayName("questionId이면서 삭제되지 않은건 조회")
    public void findByQuestionIdAndDeletedFalse() {
        // Question을 persist로 id 생성
        Question q1  = QuestionTest.Q1;
        questionRepository.save(q1);

        Answer a1 = answerRepository.save(AnswerTest.A1);
        Answer a2 = answerRepository.save(AnswerTest.A2);

        // 2개가 조회
        assertThat(a1.getQuestionId()).isNotNull();
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(a1.getQuestionId());
        assertThat(answers).hasSize(2);
        assertThat(answers).contains(a1,a2);

        a2.setDeleted(true);

        // 1개 조회
        answers = answerRepository.findByQuestionIdAndDeletedFalse(a1.getQuestionId());
        assertThat(answers).hasSize(1);
        assertThat(answers).contains(a1);
    }

    @Test
    @DisplayName("findByIdAndDeletedFalse 테스트")
    public void findByIdAndDeletedFalse() {
        Answer a1 = answerRepository.save(AnswerTest.A1);

        // 1개 조회
        Answer a2 = answerRepository.findByIdAndDeletedFalse(a1.getId()).get();

        assertThat(a2).isSameAs(a1);

        // deleted가 적용되므로 0개
        a1.setDeleted(true);
        assertThatThrownBy(()->answerRepository
                .findByIdAndDeletedFalse(a1.getId())
                .orElseThrow(()->new RuntimeException("invalid")))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("invalid");
    }

    @Test
    public void detached() {
        Answer a1 = answerRepository.save(AnswerTest.A1);
        a1.setDeleted(true);
        // 없으므로 throw exception
        assertThatThrownBy(()->answerRepository.findByIdAndDeletedFalse(a1.getId()).get())
                .isInstanceOf(NoSuchElementException.class);

        entityManager.detach(a1);
        // detach상태에서는 트래킹이 안된다.
        a1.setDeleted(false);
        // false임에도 트래킹이 안되기때문에 exception
        assertThatThrownBy(()->answerRepository.findByIdAndDeletedFalse(a1.getId()).get())
                .isInstanceOf(NoSuchElementException.class);
    }

}
