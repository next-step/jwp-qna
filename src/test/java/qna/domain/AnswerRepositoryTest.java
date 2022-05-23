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
    AnswerRepository answerRepository;

    private User user;
    private Question question;

    @BeforeEach
    void init() {
        user = new User(1L, "yulmucha", "password", "Yul", "yul@google.com");
        question = new Question("title1", "contents1").writeBy(user);
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
    void findById() {
        Answer answer = new Answer(user, question, "Answers Contents1");
        Answer savedAnswer = answerRepository.save(answer);

        Answer foundAnswer = answerRepository.findById(savedAnswer.getId()).get();
        assertThat(foundAnswer).isEqualTo(answer);
    }
}
