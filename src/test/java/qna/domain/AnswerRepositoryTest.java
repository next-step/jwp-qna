package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
class AnswerRepositoryTest {

    private Answer A1;
    private Answer A2;
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");
        A2 = new Answer(SANJIGI, Q1, "Answers Contents2");
    }

    @Test
    @DisplayName("save하면 id가 자동으로 생성되고, 입력시간/수정 시간이 입력됨. Question은 Id가 null이므로 저장된 것이 없음 ")
    void test1() {
        Answer save = answerRepository.save(A1);

        assertAll(
                () -> assertThat(save.getId()).isPositive(),
                () -> assertThat(save.getCreatedAt()).isNotNull(),
                () -> assertThat(save.getUpdatedAt()).isNotNull(),
                () -> assertThat(save.getWriterId()).isPositive(),
                () -> assertThat(save.getQuestionId()).isNull(),
                () -> assertThat(save.isOwner(JAVAJIGI)).isTrue()
        );
    }

    @Test
    @DisplayName("Question Id와 삭제되지 않은 Answer를 검색하면 한 건이 조회됨")
    void findByQuestionIdAndDeletedFalse() {
        A1.setQuestionId(1L);
        A2.setQuestionId(2L);
        Answer deletedAnswer = new Answer(JAVAJIGI, Q1, "some contents");
        deletedAnswer.setQuestionId(1L);
        deletedAnswer.setDeleted(true);

        answerRepository.saveAll(Arrays.asList(A1, A2, deletedAnswer));
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(1L);

        assertAll(
                () -> assertThat(answers.size()).isEqualTo(1),
                () -> assertThat(answers.get(0).getQuestionId()).isEqualTo(1L),
                () -> assertThat(answers.get(0).isOwner(JAVAJIGI)).isTrue()
        );
    }

    @Test
    @DisplayName("Question Id와 삭제되지 않은 Answer를 검색하면 한 건이 조회됨")
    void findByDeletedFalse() {
        A1.setDeleted(true);
        A2.setDeleted(false);

        answerRepository.saveAll(Arrays.asList(A1, A2));
        List<Answer> answers = answerRepository.findByDeletedTrue();

        assertAll(
                () -> assertThat(answers.size()).isEqualTo(1),
                () -> assertThat(answers.get(0).isOwner(JAVAJIGI)).isTrue()
        );
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
        save.setContents("updated");

        em.flush();
        em.clear();

        Optional<Answer> updated = answerRepository.findById(save.getId());

        assertThat(updated.get().getContents()).isEqualTo("updated");
    }

    @Test
    @DisplayName("contains 로 % ~ % 검색이 가능함")
    void findByRegEx() {
        A1.setContents("12345");
        A2.setContents("abcde");
        answerRepository.saveAll(Arrays.asList(A1, A2));

        List<Answer> matchesRegex = answerRepository.findByContentsContains("123");

        assertAll(
                () -> assertThat(matchesRegex.size()).isEqualTo(1),
                () -> assertThat(matchesRegex.get(0).getContents()).isEqualTo("12345")
        );
    }
}