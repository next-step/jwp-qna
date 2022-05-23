package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    AnswerRepository answerRepository;

    private User user;
    private Question question;

    @BeforeEach
    void init() {
        user = userRepository.save(new User("yulmucha", "password", "Yul", "yul@google.com"));
        question = questionRepository.save(new Question(user, "title1", "contents1"));
    }

    @Test
    @DisplayName("저장이 잘 되는지 테스트")
    void save() {
        Answer expected = new Answer(user, question, "Answers Contents1");
        Answer actual = answerRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    @DisplayName("개체를 저장한 후 다시 가져왔을 때 기존의 개체와 동일한지 테스트")
    void identity() {
        Answer a1 = answerRepository.save(new Answer(user, question, "contents"));
        Answer a2 = answerRepository.findById(a1.getId()).get();
        assertThat(a1).isSameAs(a2);
    }
}
