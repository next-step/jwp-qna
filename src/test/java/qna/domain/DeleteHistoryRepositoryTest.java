package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    private User user;
    private Question question;
    private Answer answer;

    @BeforeEach
    void init() {
        user = userRepository.save(new User("yulmucha", "password", "Yul", "yul@google.com"));
        question = questionRepository.save(new Question(user, "title1", "contents1"));
        answer = answerRepository.save(new Answer(user, question, "contents"));
    }

    @Test
    @DisplayName("저장이 잘 되는지 테스트")
    void save() {
        DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, answer.getId(), user, LocalDateTime.now());
        DeleteHistory actual = deleteHistoryRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContentType()).isEqualTo(expected.getContentType())
        );
    }

    @Test
    @DisplayName("개체를 저장한 후 다시 가져왔을 때 기존의 개체와 동일한지 테스트")
    void identity() {
        DeleteHistory d1 = deleteHistoryRepository.save(new DeleteHistory(ContentType.QUESTION, question.getId(), user, LocalDateTime.now()));
        DeleteHistory d2 = deleteHistoryRepository.findById(d1.getId()).get();
        assertThat(d1).isSameAs(d2);
    }

    @Test
    @DisplayName("개체를 저장하고 영속성 컨텍스트를 초기화한 후 개체를 다시 가져왔을 때, 저장했을 당시의 개체와 동등한지 테스트")
    void equality() {
        DeleteHistory d1 = deleteHistoryRepository.save(new DeleteHistory(ContentType.QUESTION, question.getId(), user, LocalDateTime.now()));
        entityManager.clear();
        DeleteHistory d2 = deleteHistoryRepository.findById(d1.getId()).get();
        assertThat(d1).isEqualTo(d2);
    }
}
