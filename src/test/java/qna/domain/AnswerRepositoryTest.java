package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository target;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    @Autowired
    private TestEntityManager manager;

    private Answer answer;

    @BeforeEach
    public void setUp() {
        final User senior = users.save(
            new User(1L, "senior", "pwd", "name", "gosu@slipp.net"));
        final User newbie = users.save(
            new User(2L, "newbie", "pwd", "name", "new@slipp.net"));

        final Question question = questions.save(
            new Question("JPA", "Proxy")
                .writeBy(senior));

        answer = new Answer(newbie, question, "I'm not sure about that");
    }

    @AfterEach
    void tearDown() {
        manager.flush();
        manager.clear();
    }

    @DisplayName("저장 후 조회시, 동등성과 동일성을 보장해야 한다")
    @Test
    void find() {
        final Answer saved = target.save(answer);
        final Answer actual = target.findById(saved.getId()).get();
        assertThat(actual).isEqualTo(saved);
        assertThat(actual).isSameAs(saved);
    }

    @DisplayName("저장 후 갱신시, 영속선 컨텍스트 캐시로 인해 동등성과 동일성을 보장해야 한다")
    @Test
    void update() {
        final Question newQuestion = questions.save(new Question("JPA", "MayToOne"));
        final Answer saved = target.save(answer);

        Answer updated = target.findById(saved.getId()).get();
        updated.toQuestion(newQuestion);

        final Answer actual = target.findById(saved.getId()).get();
        assertThat(actual.getQuestion()).isEqualTo(newQuestion);
        assertThat(actual).isSameAs(updated);
    }

    @DisplayName("저장 후 삭제 가능 해야 한다")
    @Test
    void delete() {
        final Answer saved = target.save(answer);

        target.deleteById(saved.getId());
        assertThat(target.findById(saved.getId()).orElse(null)).isNull();
    }

    @DisplayName("특정 질문에 달린 답변을 조회 요청시, 주어진 답변이 null이라면, empty list를 반환해야 한다")
    @Test
    void findByQuestionAndDeletedFalse_nullQuestion() {
        assertThat(target.findByQuestionAndDeletedFalse(null)).isEmpty();
    }

    @DisplayName("특정 질문에 달린 답변을 조회 요청시, 답변이 존재한지 않다면, empty list를 반환해야 한다")
    @Test
    void findByQuestionAndDeletedFalse_notAnsweredYet() {
        final Question newQuestion = questions.save(new Question("JPA", "Entity Mapping Proxy"));
        assertThat(target.findByQuestionAndDeletedFalse(newQuestion)).isEmpty();
    }

    @DisplayName("특정 질문에 달린 답변을 조회 요청시, 답변이 존재한다면, 해당 답변 목록을 반환해야 한다")
    @Test
    void findByQuestionAndDeletedFalse() {
        final Answer saved = target.save(answer);
        assertThat(target.findByQuestionAndDeletedFalse(answer.getQuestion()))
            .containsExactly(saved);
    }

}
