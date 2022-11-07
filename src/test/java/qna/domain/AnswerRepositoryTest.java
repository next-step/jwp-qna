package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class AnswerRepositoryTest extends NewEntityTestBase {
    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("Question Id와 삭제되지 않은 Answer를 검색하면 한 건이 조회됨")
    void findByQuestionQIdAndDeletedFalse() throws CannotDeleteException {
        Answer answer = new Answer(NEWUSER1, Q2, "some contents");
        answer.delete(NEWUSER1);

        answerRepository.save(answer);
        List<Answer> answers = answerRepository.findByQuestionAndDeletedFalse(Q2);

        assertThat(answers.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Question Id와 삭제되지 않은 Answer를 검색하면 한 건이 조회됨")
    void findByDeletedFalse() throws CannotDeleteException {
        A1.delete(NEWUSER1);

        List<Answer> answers = answerRepository.findByDeletedTrue();

        assertThat(answers.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("저장된 Answer를 검색하면 동일한 Entity가 반환됨")
    void findByIdAndDeletedFalse() {
        List<Answer> answers = answerRepository.saveAll(Arrays.asList(A1, A2));

        Optional<Answer> result = answerRepository.findByIdAndDeletedFalse(answers.get(0).getId());

        assertThat(result.get()).isSameAs(answers.get(0));
    }

    @Test
    @DisplayName("쿼리 메소드로 마지막에 생성된 answer를 조회함")
    void findTopByOrderByIdDesc() {
        answerRepository.saveAll(Arrays.asList(A1, A2));

        Optional<Answer> lastAnswer = answerRepository.findTopByOrderByIdDesc();

        assertThat(lastAnswer.get()).isSameAs(A2);
    }

    @Test
    @DisplayName("content가 업데이트 된다. dynamicUpdate적용으로 변경된 컬럼만 쿼리에 포함됨.")
    void update() {
        Answer save = answerRepository.save(A1);
        save.updateContents("updated");

        em.flush();
        em.clear();

        Optional<Answer> updated = answerRepository.findById(save.getId());

        assertThat(updated.get().getContents()).isEqualTo("updated");
    }

    @Test
    @DisplayName("contains 로 % ~ % 검색이 가능함")
    void findByRegEx() {
        A1.updateContents("12345");
        A2.updateContents("abcde");
        answerRepository.saveAll(Arrays.asList(A1, A2));

        List<Answer> matchesRegex = answerRepository.findByContentsContains("123");

        assertAll(
                () -> assertThat(matchesRegex.size()).isEqualTo(1),
                () -> assertThat(matchesRegex.get(0).getContents()).isEqualTo("12345")
        );
    }
}