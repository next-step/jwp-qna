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
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager manager;

    private User userA;
    private User userB;
    private Answer answer;

    @BeforeEach
    public void setUp() {
        userA = userRepository.save(new User(1L, "senior", "pwd", "name", "gosu@slipp.net"));
        userB = userRepository.save(new User(2L, "newbie", "pwd", "name", "new@slipp.net"));
        final Question questionFromA = questionRepository.save(new Question("JPA", "Proxy").writeBy(userA));
        answer = new Answer(userB, questionFromA, "I'm not sure about that");
    }

    @AfterEach
    void tearDown() {
        manager.flush();
        manager.clear();
    }

    @DisplayName("저장 후 조회시, 영속선 컨텍스트 캐시로 인해 동등성과 동일성을 보장해야 한다")
    @Test
    void find() {
        final Answer saved = answerRepository.save(answer);
        final Answer actual = answerRepository.findById(saved.getId()).get();
        assertThat(actual).isEqualTo(saved);
        assertThat(actual).isSameAs(saved);
    }

    @DisplayName("저장 후 갱신시, 영속선 컨텍스트 캐시로 인해 동등성과 동일성을 보장해야 한다")
    @Test
    void update() {
        final Question newQuestion = questionRepository.save(new Question("JPA", "MayToOne"));
        final Answer saved = answerRepository.save(answer);

        Answer updated = answerRepository.findById(saved.getId()).get();
        updated.toQuestion(newQuestion);

        final Answer actual = answerRepository.findById(saved.getId()).get();
        assertThat(actual.getQuestion()).isEqualTo(newQuestion);
        assertThat(actual).isSameAs(updated);
    }

    @DisplayName("저장 후 삭제 가능 해야 한다")
    @Test
    void delete() {
        final Answer saved = answerRepository.save(answer);

        answerRepository.deleteById(saved.getId());
        assertThat(answerRepository.findById(saved.getId()).orElse(null)).isNull();
    }
}
