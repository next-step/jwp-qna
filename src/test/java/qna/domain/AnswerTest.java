package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.fixture.AnswerFixture;
import qna.fixture.QuestionFixture;
import qna.fixture.UserFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Answer 테스트")
class AnswerTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("Save 확인")
    @Test
    void save_확인() {
        // given
        User user = userRepository.save(UserFixture.create("user"));
        Question question = questionRepository.save(QuestionFixture.create("title", "contents", user));
        Answer answer = AnswerFixture.create(user, question, "Answers Contents");

        // when
        Answer result = answerRepository.save(answer);

        assertThat(result)
                .isEqualTo(answer);
    }

    @DisplayName("findById 확인")
    @Test
    void findById_확인() {
        // given
        User user = userRepository.save(UserFixture.create("user"));
        Question question = questionRepository.save(QuestionFixture.create("title", "contents", user));
        Answer answer = AnswerFixture.create(user, question, "Answers Contents");

        // when
        Answer savedAnswer = answerRepository.save(answer);
        Optional<Answer> actual = answerRepository.findById(savedAnswer.getId());

        // then
        assertThat(actual)
                .isPresent()
                .contains(savedAnswer);
    }

    @DisplayName("update 확인")
    @Test
    void update_확인() {
        // given
        User user = userRepository.save(UserFixture.create("user"));
        Question question = questionRepository.save(QuestionFixture.create("title", "contents", user));
        Answer answer = AnswerFixture.create(user, question, "Answers Contents");

        // when
        Answer savedAnswer = answerRepository.save(answer);
        savedAnswer.setContents("Answers Contents2");

        Optional<Answer> actual = answerRepository.findById(savedAnswer.getId());

        // then
        assertThat(actual)
                .isPresent();

        assertThat(actual.get().getContents())
                .isEqualTo("Answers Contents2");
    }
}
