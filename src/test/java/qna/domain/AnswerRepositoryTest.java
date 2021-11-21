package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("save하면 id가 자동으로 생성되야 한다.")
    void saveTest() {
        userRepository.save(UserTest.JAVAJIGI);
        Answer answer = answerRepository.save(AnswerTest.A1);

        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getContents()).isEqualTo(AnswerTest.A1.getContents())
        );
    }

    @Test
    @DisplayName("findById 결과는 동일성이 보장되어야한다.")
    void findByIdTest() {
        User user = userRepository.save(UserTest.SANJIGI);
        Question question = questionRepository.save(new Question("title", Contents.of("question")));
        Answer save = answerRepository.save(new Answer(user, question, null));

        Answer find = answerRepository.findById(save.getId()).get();

        assertAll(
                () -> assertThat(find).isEqualTo(save),
                () -> assertThat(find.getId()).isEqualTo(save.getId())
        );
    }

    @Test
    @DisplayName("nullable false 칼럼은 반드시 값이 있어야한다.")
    void nullableTest() {
        User user = userRepository.save(new User("ssw", "1234", "name", "mail"));
        Question question = questionRepository.save(new Question("title", Contents.of("question")));
        Answer save = answerRepository.save(new Answer(user, question, null));

        Answer find = answerRepository.findById(save.getId()).get();

        assertAll(
                () -> assertThat(find.getCreatedAt()).isBefore(LocalDateTime.now()),
                () -> assertThat(find.isDeleted()).isFalse()
        );
    }
}
